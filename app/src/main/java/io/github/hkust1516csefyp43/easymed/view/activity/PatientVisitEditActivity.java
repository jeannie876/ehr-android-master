package io.github.hkust1516csefyp43.easymed.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Card;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.ListOfCards;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.PersonalData;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Pregnancy;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.VitalSigns;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Clinic;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Consultation;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Document;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.DocumentType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Prescription;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.RelatedData;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Triage;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Visit;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.PatientIdentifier;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.ChiefComplaintFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.DocumentFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.ListOfCardsFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.PersonalDataFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.PregnancyFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.RemarkFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit.VitalSignFragment;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientVisitEditActivity extends AppCompatActivity implements OnFragmentInteractionListener, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = PatientVisitEditActivity.class.getSimpleName();
    public static final String[] DEFAULT_PHYSICAL_EXAMINATION = {"General Appearance", "ENT", "Respiratory", "Cardiovascular", "Gastrointestinal", "Genital/Urinary", "Skin", "Other"};
    public static final String[] DEFAULT_REVIEW_OF_SYSTEM = {"General", "Respiratory", "Cardiovascular", "Gastrointestinal", "Genital/Urinary", "ENT", "Skin", "Locomotor", "Neurology", "Other"};
    public static final String[] DEFAULT_RED_FLAG = {"Alertness", "Breathing", "Circulation", "Dehydration", "DEFG"};

    private PersonalDataFragment personalDataFragment;
    private VitalSignFragment vitalSignFragment;
    private ChiefComplaintFragment chiefComplaintFragment;
    private RemarkFragment triageRemarkFragment;
    private DocumentFragment hpiFragment;
    private DocumentFragment pmhFragment;
    private DocumentFragment fhFragment;
    private DocumentFragment shFragment;
    private ListOfCardsFragment dhFragment;
    private ListOfCardsFragment screeningFragment;
    private ListOfCardsFragment allergyFragment;
    private PregnancyFragment pregnancyFragment;
    private ListOfCardsFragment rosFragment;
    private ListOfCardsFragment rfFragment;
    private ListOfCardsFragment peFragment;
    private ListOfCardsFragment diagnosisFragment;
    private ListOfCardsFragment investigationFragment;
    private ListOfCardsFragment medicationFragment;
    private ListOfCardsFragment adviceFragment;
    private ListOfCardsFragment followupFragment;
    private RemarkFragment consultationRemarkFragment;

    private Patient thisPatient;
    private Visit thisVisit;
    private Triage thisTriage;
    private Consultation thisConsultation;

    private boolean isTriage = true;
    private boolean showHistoryButton = true;

    private ArrayList<String> tabs = new ArrayList<>();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ActionBar supportActionBar;
    private DrawerLayout drawerLayout;
    private FloatingActionButton irisScanFAB;

    private boolean errorInAnyPage = false;

    private PatientIdentifier patientIdentifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_visit_edit);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        irisScanFAB = (FloatingActionButton) findViewById(R.id.irisScanFab);
        patientIdentifier = PatientIdentifier.getPatientIdentifier(this, PatientVisitEditActivity.this);

        irisScanFAB.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_remove_red_eye).color(Color.WHITE).actionBar());


        //Temp fab to scan iris
        irisScanFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!patientIdentifier.isDeviceConnected()) {
                    Toast.makeText(PatientVisitEditActivity.this, "No iris scanner connected, please connect one to continue.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(PatientVisitEditActivity.this, "Iris scanner connected!", Toast.LENGTH_LONG).show();
                    patientIdentifier.identifyIris();
                }
            }
        });

        irisScanFAB.setVisibility(View.GONE);


        //get extra
        Intent intent = getIntent();
        if (intent != null) {
            Serializable serializable;
            //isTriage
            isTriage = intent.getBooleanExtra(Const.BundleKey.IS_TRIAGE, true);
            //patient
            serializable = intent.getSerializableExtra(Const.BundleKey.EDIT_PATIENT);
            if (serializable instanceof Patient) {
                thisPatient = (Patient) serializable;
            }
            //visit
            serializable = intent.getSerializableExtra(Const.BundleKey.WHOLE_VISIT);
            if (serializable instanceof Visit) {
                thisVisit = (Visit) serializable;
            }
            //triage
            serializable = intent.getSerializableExtra(Const.BundleKey.WHOLE_TRIAGE);
            if (serializable instanceof Triage) {
                thisTriage = (Triage) serializable;
            }
            //consultation
            serializable = intent.getSerializableExtra(Const.BundleKey.WHOLE_CONSULTATION);
            if (serializable instanceof Consultation) {
                thisConsultation = (Consultation) serializable;
            }
        }

        if (thisPatient != null && thisPatient.getClinicId() != null) {
            OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
            ohc1.readTimeout(1, TimeUnit.MINUTES);
            ohc1.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Const.Database.getCurrentAPI())
                    .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                    .client(ohc1.build())
                    .build();
            v2API.clinics clinicService = retrofit.create(v2API.clinics.class);
            Call<Clinic> clinicCall = clinicService.getClinic("1", thisPatient.getClinicId());
            clinicCall.enqueue(new Callback<Clinic>() {
                @Override
                public void onResponse(Call<Clinic> call, Response<Clinic> response) {
                    if (response != null && response.body() != null && response.body().getEnglishName() != null) {
                        if (toolbar != null) {
                            toolbar.setSubtitle(response.body().getEnglishName());
                        }
                    }
                }

                @Override
                public void onFailure(Call<Clinic> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
//
//    Clinic clinic = Cache.CurrentUser.getClinic(this);
//    if (isTriage && clinic != null && toolbar != null) {
//      if (clinic.getEnglishName() != null) {
//        toolbar.setSubtitle(clinic.getEnglishName());
//      }
//    }

        if (isTriage && drawerLayout != null) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        if (tabLayout != null && viewPager != null) {
            //set tablayout pages
            //triage: personal data, vital signs, chief complain, triage remark
            tabs.add("Personal Data");
            tabs.add("Vital Signs");
            tabs.add("Chief Complaints");
            tabs.add("Triage Remark");
            if (!isTriage) {  //consultation: (triage pages), hpi, pmh, fs, ss, drug history, screening, allergy, preg, sr, rf, pe, diagnosis, invest, prescriptin, advice, fu, consul remark
                tabs.add("HPI");
                tabs.add("Previous Medical History");
                tabs.add("Family History");
                tabs.add("Social History");
                tabs.add("Drug History");
                tabs.add("Screening");
                tabs.add("Allergy");
                tabs.add("Pregnancy");
                tabs.add("Review of System");
                tabs.add("Red Flags");
                tabs.add("Physical Examination");
                tabs.add("Clinical Diagnosis");
                tabs.add("Investigation");
                tabs.add("Medication");
                tabs.add("Advice");
                tabs.add("Follow-up");
                tabs.add("Consultation Remark");
            }
            for (String s : tabs) {
                tabLayout.addTab(tabLayout.newTab().setText(s));
            }


            viewPager.setAdapter(new viewPagerAdapter(getSupportFragmentManager()));
            if (navigationView != null)
                viewPager.addOnPageChangeListener(new customViewPagerOnPageChangeListener(tabLayout, navigationView));
            viewPager.setOffscreenPageLimit(tabs.size());
            //viewpager need to set page adapter first
            //tabLayout.setupWithViewPager(viewPager);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem((tab.getPosition()));

                }
            });
        }

        setSupportActionBar(toolbar);
        supportActionBar = getSupportActionBar();
        if (isTriage) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
        } else {
            if (navigationView != null) {
                navigationView.setNavigationItemSelectedListener(this);
            }
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            if (drawerLayout != null) {
                drawerLayout.setDrawerListener(toggle);
                toggle.syncState();
            }
        }

        //if patient comes with visit_id >> get triage or both triage and consultation if exist

        //set toolbar title (last name first name)
        if (thisPatient != null && supportActionBar != null) {
            supportActionBar.setTitle(Util.displayNameBuilder(thisPatient.getLastName(), thisPatient.getFirstName()));
        }
        if (thisPatient == null && supportActionBar != null) {
            supportActionBar.setTitle("New Patient");
        }
        //set toolbar subtitle (clinic name)
        //cc button (only in consultation w/ triage)
        //confirm button >> dialog, progressbar, (some dialog if successful)
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                Log.d(TAG, "byeeeeeeeeeeee");
                return;
            }
        }
        MaterialDialog.SingleButtonCallback yes = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                PatientVisitEditActivity.this.finish();
            }
        };
        MaterialDialog.SingleButtonCallback no = new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
            }
        };
        new MaterialDialog.Builder(this)
                .title("Are you sure")
                .content("All unsaved data will be lost")
                .positiveText("Leave")
                .negativeText("Stay")
                .onPositive(yes)
                .onNegative(no)
                .autoDismiss(false)
                .theme(Theme.LIGHT)
                .show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem;
        if (thisPatient == null) {
            menuItem = menu.findItem(R.id.history);
            menuItem.setVisible(false);
            menuItem = menu.findItem(R.id.chief_complaint);
            menuItem.setVisible(false);
        }
        if (thisTriage == null) {
            menuItem = menu.findItem(R.id.chief_complaint);
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.chief_complaint:
                if (thisTriage != null && thisTriage.getChiefComplaints() != null) {
                    new MaterialDialog.Builder(this)
                            .title("Chief Complain")
                            .content(thisTriage.getChiefComplaints())
                            .autoDismiss(true)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .theme(Theme.LIGHT)
                            .positiveText("Dismiss")
                            .show();
                } else {
                    if (viewPager != null) {
                        Snackbar.make(viewPager, "No Chief Complain", Snackbar.LENGTH_LONG).show();
                    } else {
                        new MaterialDialog.Builder(this)
                                .content("No Chief Complain")
                                .autoDismiss(true)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .theme(Theme.LIGHT)
                                .positiveText("Dismiss")
                                .show();
                    }
                }
                return false;
            case R.id.history:
                Intent intent = new Intent(getBaseContext(), PatientVisitViewActivity.class);
                intent.putExtra(Const.BundleKey.ON_OR_OFF, false);
                if (thisPatient != null) {
                    intent.putExtra(Const.BundleKey.READ_ONLY_PATIENT, thisPatient);
                }
                startActivity(intent);
                return false;
            case R.id.confirm:
                final Context context = this;
                final ProgressDialog progressDialog = ProgressDialog.show(context, "Loading", "please wait...");
                Serializable serializable;
                PersonalData personalData = null;
                VitalSigns vitalSigns = null;
                String chiefComplaints = null;
                String triageRemark = null;
                io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document hpi = null;
                io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document pmh = null;
                io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document fh = null;
                io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document sh = null;
                ListOfCards dh = null;
                ListOfCards screening = null;
                ListOfCards allergy = null;
                Pregnancy pregnancy = null;
                ListOfCards ros = null;
                ListOfCards rf = null;
                ListOfCards pe = null;
                ListOfCards diagnosis = null;
                ListOfCards investigation = null;
                ListOfCards medication = null;
                ListOfCards advice = null;
                ListOfCards followup = null;
                String consultationRemark = null;
                errorInAnyPage = false;

                if (personalDataFragment != null) {
                    serializable = personalDataFragment.onSendData();
                    if (serializable instanceof PersonalData) {
                        personalData = (PersonalData) serializable;
                    } else if (serializable instanceof Throwable) {
                        errorInAnyPage = true;
                        viewPager.setCurrentItem(0);
                    }
                }
                if (vitalSignFragment != null) {
                    serializable = vitalSignFragment.onSendData();
                    if (serializable instanceof VitalSigns) {
                        vitalSigns = (VitalSigns) serializable;
                    } else if (serializable instanceof Throwable) {
                        errorInAnyPage = true;
                        viewPager.setCurrentItem(1);
                    }
                }
                if (chiefComplaintFragment != null) {
                    serializable = chiefComplaintFragment.onSendData();
                    if (serializable instanceof String) {
                        chiefComplaints = (String) serializable;
                    }
                }
                if (triageRemarkFragment != null) {
                    serializable = triageRemarkFragment.onSendData();
                    if (serializable instanceof String) {
                        triageRemark = (String) serializable;
                    } else if (serializable instanceof Throwable) {
                        errorInAnyPage = true;
                        viewPager.setCurrentItem(3);
                    }
                }
                if (!isTriage) {

                    if (hpiFragment != null) {
                        serializable = hpiFragment.onSendData();
                        if (serializable instanceof io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) {
                            hpi = (io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) serializable;
                        }
                    }
                    if (pmhFragment != null) {
                        serializable = pmhFragment.onSendData();
                        if (serializable instanceof io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) {
                            pmh = (io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) serializable;
                        }
                    }
                    if (fhFragment != null) {
                        serializable = fhFragment.onSendData();
                        if (serializable instanceof io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) {
                            fh = (io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) serializable;
                        }
                    }
                    if (shFragment != null) {
                        serializable = shFragment.onSendData();
                        if (serializable instanceof io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) {
                            sh = (io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document) serializable;
                        }
                    }
                    if (dhFragment != null) {
                        serializable = dhFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            dh = (ListOfCards) serializable;
                        }
                    }
                    if (screeningFragment != null) {
                        serializable = screeningFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            screening = (ListOfCards) serializable;
                        }
                    }
                    if (allergyFragment != null) {
                        serializable = allergyFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            allergy = (ListOfCards) serializable;
                        }
                    }
                    if (pregnancyFragment != null) {
                        Log.d(TAG, "PF is not null");
                        serializable = pregnancyFragment.onSendData();
                        if (serializable instanceof Pregnancy) {
                            Log.d(TAG, "Is pregnancy");
                            pregnancy = (Pregnancy) serializable;
                        } else if (serializable instanceof Throwable) {
                            Log.d(TAG, "Is throwable");
                            errorInAnyPage = true;
                            viewPager.setCurrentItem(11);
                        }
                    }
                    if (rosFragment != null) {
                        serializable = rosFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            ros = (ListOfCards) serializable;
                        }
                    }
                    if (rfFragment != null) {
                        serializable = rfFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            rf = (ListOfCards) serializable;
                        }
                    }
                    if (peFragment != null) {
                        serializable = peFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            pe = (ListOfCards) serializable;
                        }
                    }
                    if (diagnosisFragment != null) {
                        serializable = diagnosisFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            diagnosis = (ListOfCards) serializable;
                        }
                    }
                    if (investigationFragment != null) {
                        serializable = investigationFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            investigation = (ListOfCards) serializable;
                        }
                    }
                    if (medicationFragment != null) {
                        serializable = medicationFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            medication = (ListOfCards) serializable;
                        }
                    }
                    if (adviceFragment != null) {
                        serializable = adviceFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            advice = (ListOfCards) serializable;
                        }
                    }
                    if (followupFragment != null) {
                        serializable = followupFragment.onSendData();
                        if (serializable instanceof ListOfCards) {
                            followup = (ListOfCards) serializable;
                        }
                    }
                    if (consultationRemarkFragment != null) {
                        serializable = consultationRemarkFragment.onSendData();
                        if (serializable instanceof String) {
                            consultationRemark = (String) serializable;
                        } else if (serializable instanceof Throwable) {
                            errorInAnyPage = true;
                            viewPager.setCurrentItem(20);
                        }
                    }
                }

                OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
                ohc1.readTimeout(1, TimeUnit.MINUTES);
                ohc1.connectTimeout(1, TimeUnit.MINUTES);
                final Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl(Const.Database.getCurrentAPI())
                        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                        .client(ohc1.build())
                        .build();
                v2API.patients patientService = retrofit.create(v2API.patients.class);
                final v2API.triages triageService = retrofit.create(v2API.triages.class);
                final v2API.consultations consultationService = retrofit.create(v2API.consultations.class);
                final v2API.visits visitService = retrofit.create(v2API.visits.class);
                final v2API.related_data relatedDataService = retrofit.create(v2API.related_data.class);
                final v2API.prescriptions prescriptionService = retrofit.create(v2API.prescriptions.class);
                final v2API.documents documentService = retrofit.create(v2API.documents.class);
                final v2API.medications medicationService = retrofit.create(v2API.medications.class);

                final PersonalData pd = personalData;
                final VitalSigns vs = vitalSigns;
                final String cc = chiefComplaints;
                final String tr = triageRemark;

                final io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document finalHPI = hpi;
                final io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document finalPMH = pmh;
                final io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document finalFH = fh;
                final io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Document finalSH = sh;
                final ListOfCards finalDH = dh;
                final ListOfCards finalScreening = screening;
                final ListOfCards finalAllergy = allergy;
                final Pregnancy finalPregnancy = pregnancy;
                final ListOfCards finalROS = ros;
                final ListOfCards finalRF = rf;
                final ListOfCards finalPE = pe;
                final ListOfCards finalDiagnosis = diagnosis;
                final ListOfCards finalInvestigation = investigation;
                final ListOfCards finalMedication = medication;
                final ListOfCards finalAdvice = advice;
                final ListOfCards finalFollowUp = followup;
                final String finalConsultationRemark = consultationRemark;

                Log.d(TAG, "Input consultation: " + finalDH + '\n' + finalScreening + '\n' + finalAllergy + '\n' + finalPregnancy
                        + '\n' + finalROS + '\n' + finalRF + '\n' + finalPE + '\n' + finalDiagnosis + '\n' + finalInvestigation
                        + '\n' + finalMedication + '\n' + finalAdvice + '\n' + finalFollowUp + '\n' + finalConsultationRemark);

                if (!errorInAnyPage) {
                    if (thisPatient != null) {
                        if (isTriage) {
                            if (thisTriage != null) {                                                               //existing patient edit triage  TODO update profile pic if personalData.getProfilePicBase64 exists
                                //PUT patient
                                Log.d(TAG, "Existing patient edit visit edit triage");
                                Patient patient = generatePatient(personalData);
                                if (patient != null) {
                                    Call<Patient> patientCall = patientService.editPatient("1", thisPatient.getPatientId(), patient);
                                    patientCall.enqueue(new Callback<Patient>() {
                                        @Override
                                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                                            Log.d(TAG, "patient call response code: " + response.code());
                                            if (response.code() < 500 && response.code() >= 400) {
                                                try {
                                                    Log.d(TAG, response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d(TAG, response.body().toString());
                                            if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                onFailure(call, new Throwable("No response"));
                                            } else {
                                                //PUT visit (iff tag number have been modified?)
                                                Visit visit = generateVisit(response.body(), pd, Const.NextStation.CONSULTATION);
                                                Log.d(TAG, "Editing Visit: " + visit);
                                                if (visit.getTag() != thisVisit.getTag()) {
                                                    if (visit != null) {
                                                        Call<Visit> visitCall = visitService.editVisit("1", visit, thisVisit.getId());
                                                        visitCall.enqueue(new Callback<Visit>() {
                                                            @Override
                                                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                                Log.d(TAG, "visit call response code: " + response.code());
                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                    try {
                                                                        Log.d(TAG, response.errorBody().string());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                Log.d(TAG, response.body().toString());
                                                                if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                                    onFailure(call, new Throwable("No response"));
                                                                } else {
                                                                    //PUT triage
                                                                    Log.d(TAG, "Editing triage: " + thisTriage);
                                                                    Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                    Call<Triage> triageCall = triageService.editTriage("1", triage, thisTriage.getId());
                                                                    triageCall.enqueue(new Callback<Triage>() {
                                                                        @Override
                                                                        public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                            Log.d(TAG, "triage call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            progressDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Triage> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                            new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                                    .setTitle("Error")
                                                                                    .setMessage("Failed to update triage record. Please try again.\nDetail: '" + t.toString() + "'")
                                                                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                                        @Override
                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                            dialog.dismiss();
                                                                                        }
                                                                                    })
                                                                                    .show();
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Visit> call, Throwable t) {
                                                                t.printStackTrace();
                                                                progressDialog.dismiss();
                                                                new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                        .setTitle("Error")
                                                                        .setMessage("Failed to update visit record. Please try again.\nDetail: '" + t.toString() + "'")
                                                                        .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                dialog.dismiss();
                                                                            }
                                                                        })
                                                                        .show();
                                                            }
                                                        });
                                                    }
                                                }


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Patient> call, Throwable t) {
                                            t.printStackTrace();
                                            progressDialog.dismiss();
                                            new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                    .setTitle("Error")
                                                    .setMessage("Failed to update patient record. Please try again.\nDetail: '" + t.toString() + "'")
                                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                }
                            } else {                                                                                //existing patient new triage  TODO upload profile pic if personalData.getProfilePicBase64 exists
                                //PUT patient
                                Log.d(TAG, "Existing patient new visit new triage");
                                Patient patient = generatePatient(personalData);
                                if (patient != null) {
                                    Call<Patient> patientCall = patientService.editPatient("1", thisPatient.getPatientId(), patient);
                                    patientCall.enqueue(new Callback<Patient>() {
                                        @Override
                                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                                            Log.d(TAG, "patient call response code: " + response.code());
                                            if (response.code() < 500 && response.code() >= 400) {
                                                try {
                                                    Log.d(TAG, response.errorBody().string());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.d(TAG, response.body().toString());
                                            if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                onFailure(call, new Throwable("No response"));
                                            } else {
                                                //POST visit
                                                Visit visit = generateVisit(response.body(), pd, Const.NextStation.CONSULTATION);
                                                if (visit != null) {
                                                    Call<Visit> visitCall = visitService.addVisit("1", visit);
                                                    visitCall.enqueue(new Callback<Visit>() {
                                                        @Override
                                                        public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                            Log.d(TAG, "visit call response code: " + response.code());
                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                try {
                                                                    Log.d(TAG, response.errorBody().string());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            Log.d(TAG, response.body().toString());
                                                            if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                                onFailure(call, new Throwable("No response"));
                                                            } else {
                                                                Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                if (triage != null) {
                                                                    //POST triage
                                                                    Call<Triage> triageCall = triageService.addTriage("1", triage);
                                                                    triageCall.enqueue(new Callback<Triage>() {
                                                                        @Override
                                                                        public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                            Log.d(TAG, "triage call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            progressDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Triage> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                            //TODO
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Visit> call, Throwable t) {
                                                            t.printStackTrace();
                                                            progressDialog.dismiss();
                                                            //TODO
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Patient> call, Throwable t) {
                                            t.printStackTrace();
                                            progressDialog.dismiss();
                                            new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                    .setTitle("Error")
                                                    .setMessage("Failed to update patient record. Please try again.\nDetail: '" + t.toString() + "'")
                                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                }
                            }
                        } else {
                            if (thisConsultation != null) {                                                         //existing patient edit consultation (and triage)  TODO update profile pic if personalData.getProfilePicBase64 exists
                                //PUT patient
                                //PUT visit (iff tag number have been modified?)
                                //PUT triage
                                //PUT consultation
                                //PUT document
                                //PUT investigations (how?)
                                //Update related_data (how?)
                                Patient patient = generatePatient(personalData);
                                if (patient != null) {
                                    Call<Patient> patientCall = patientService.editPatient("1", thisPatient.getPatientId(), patient);  //PUT patient
                                    patientCall.enqueue(new Callback<Patient>() {
                                        @Override
                                        public void onResponse(Call<Patient> call, Response<Patient> response) {
                                            if (response == null || response.code() < 200 || response.code() >= 300 || response.body() == null) {
                                                onFailure(call, new Throwable("sth wrong"));
                                            } else {                                                          //PUT visit (iff tag number have been modified?)
                                                //TODO edit documents
                                                Call<Document> hpiCall = documentService.editDocument("1", finalHPI.getDocID(), new Document(finalHPI.getDocHTML()));
                                                Call<Document> shCall = documentService.editDocument("1", finalSH.getDocID(), new Document(finalSH.getDocHTML()));
                                                Call<Document> fhCall = documentService.editDocument("1", finalFH.getDocID(), new Document(finalFH.getDocHTML()));
                                                Call<Document> pmhCall = documentService.editDocument("1", finalPMH.getDocID(), new Document(finalPMH.getDocHTML()));
                                                hpiCall.enqueue(new Callback<Document>() {
                                                    @Override
                                                    public void onResponse(Call<Document> call, Response<Document> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Document> call, Throwable t) {
                                                        t.printStackTrace();

                                                    }
                                                });
                                                shCall.enqueue(new Callback<Document>() {
                                                    @Override
                                                    public void onResponse(Call<Document> call, Response<Document> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Document> call, Throwable t) {
                                                        t.printStackTrace();
                                                    }
                                                });
                                                fhCall.enqueue(new Callback<Document>() {
                                                    @Override
                                                    public void onResponse(Call<Document> call, Response<Document> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Document> call, Throwable t) {
                                                        t.printStackTrace();
                                                    }
                                                });
                                                pmhCall.enqueue(new Callback<Document>() {
                                                    @Override
                                                    public void onResponse(Call<Document> call, Response<Document> response) {

                                                    }

                                                    @Override
                                                    public void onFailure(Call<Document> call, Throwable t) {
                                                        t.printStackTrace();
                                                    }
                                                });

                                                Visit visit = generateVisit(response.body(), pd, Const.NextStation.PHARMACY);
                                                Log.d(TAG, "Editing Visit: " + visit);
                                                if (visit != null) {
                                                    Call<Visit> visitCall = visitService.editVisit("1", visit, thisVisit.getId());
                                                    visitCall.enqueue(new Callback<Visit>() {
                                                        @Override
                                                        public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                            Log.d(TAG, "visit call response code: " + response.code());
                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                try {
                                                                    Log.d(TAG, response.errorBody().string());
                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                            if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                                onFailure(call, new Throwable("No response"));
                                                            } else {
                                                                //PUT triage
                                                                Log.d(TAG, "Editing triage: " + thisTriage);
                                                                Log.d(TAG, "Editing consultation: " + thisConsultation);
                                                                Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                Call<Triage> triageCall = triageService.editTriage("1", triage, thisTriage.getId());
                                                                triageCall.enqueue(new Callback<Triage>() {
                                                                    @Override
                                                                    public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                        Log.d(TAG, "triage call response code: " + response.code());
                                                                        if (response.code() < 500 && response.code() >= 400) {
                                                                            try {
                                                                                Log.d(TAG, response.errorBody().string());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                        Log.d(TAG, response.body().toString());
                                                                        //do nothing, consultation and other stuff should take longer (i think)
//                                        progressDialog.dismiss();
//                                        finish();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Triage> call, Throwable t) {
                                                                        t.printStackTrace();
                                                                        progressDialog.dismiss();
                                                                        new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                                .setTitle("Error")
                                                                                .setMessage("Failed to update triage record. Please try again.\nDetail: '" + t.toString() + "'")
                                                                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                    }
                                                                });
                                                                //POST consultation (then POST rd and prescription (with consultation_id)
                                                                Consultation consultation = generateConsultation(response.body(), finalPregnancy, finalROS, finalRF, finalPE, finalConsultationRemark);
                                                                Call<Consultation> consultationCall = consultationService.editConsultation("1", consultation, thisConsultation.getId());
                                                                consultationCall.enqueue(new Callback<Consultation>() {
                                                                    @Override
                                                                    public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                                                                        Log.d(TAG, "consultation call response code: " + response.code());
                                                                        if (response.code() <= 500 && response.code() >= 400) {
                                                                            try {
                                                                                Log.d(TAG, response.errorBody().string());
                                                                            } catch (IOException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                        Log.d(TAG, response.body().toString());
                                                                        //POST all rd and prescriptions
                                                                        ArrayList<RelatedData> relatedDataArrayList = generateRelatedDataPlural(response.body(), finalDH, finalScreening, finalAllergy, finalDiagnosis, finalInvestigation, finalAdvice, finalFollowUp);
                                                                        for (RelatedData r : relatedDataArrayList) {
                                                                            Call<RelatedData> relatedDataCall = relatedDataService.addRelatedData("1", r);
                                                                            relatedDataCall.enqueue(new Callback<RelatedData>() {
                                                                                @Override
                                                                                public void onResponse(Call<RelatedData> call, Response<RelatedData> response) {
                                                                                    Log.d(TAG, "related data call response code: " + response.code());
                                                                                    if (response.code() < 500 && response.code() >= 400) {
                                                                                        try {
                                                                                            Log.d(TAG, response.errorBody().string());
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                    Log.d(TAG, "related data added:" + response.body());
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<RelatedData> call, Throwable t) {
                                                                                    t.printStackTrace();
                                                                                    progressDialog.dismiss();
                                                                                    new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                                            .setTitle("Error")
                                                                                            .setMessage("Failed to add some data. Please try again.\nDetail: '" + t.toString() + "'")
                                                                                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                                                @Override
                                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                                    dialog.dismiss();
                                                                                                }
                                                                                            })
                                                                                            .show();
                                                                                }
                                                                            });
                                                                        }
                                                                        final ArrayList<Medication> medicationArrayList = generateMedications(finalMedication);
                                                                        final Consultation c = response.body();
                                                                        if (medicationArrayList != null && medicationArrayList.size() > 0) {
                                                                            final ArrayList<Prescription> newMedPreArrList = new ArrayList<>(medicationArrayList.size());
                                                                            Log.d(TAG, "Medications: " + medicationArrayList.toString());
                                                                            for (final Medication m : medicationArrayList) {
                                                                                Call<Medication> medicationCall = medicationService.addMedication("1", m);
                                                                                medicationCall.enqueue(new Callback<Medication>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<Medication> call, Response<Medication> response) {
                                                                                        if (response == null) {
                                                                                            onFailure(call, new Throwable("Empty response"));
                                                                                        } else if (response.code() < 200 || response.code() >= 300) {
                                                                                            onFailure(call, new Throwable("Error from server: " + response.code() + response.raw()));
                                                                                        } else {
                                                                                            newMedPreArrList.add(new Prescription(response.body().getMedicationId(), m.getTempPrescriptionDescription()));
                                                                                            if (newMedPreArrList.size() >= medicationArrayList.size()) {
                                                                                                ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, newMedPreArrList);
                                                                                                Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                                for (Prescription p : prescriptionArrayList) {
                                                                                                    Log.d(TAG, "prescription before added:" + p);
                                                                                                    Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                                    prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                                        @Override
                                                                                                        public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                                            Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                                                try {
                                                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                                                } catch (IOException e) {
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                            Log.d(TAG, "prescription added:" + response.body());
                                                                                                        }

                                                                                                        @Override
                                                                                                        public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                                            t.printStackTrace();
                                                                                                            progressDialog.dismiss();
                                                                                                        }
                                                                                                    });
                                                                                                }
                                                                                                progressDialog.dismiss();
                                                                                                finish();
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<Medication> call, Throwable t) {
                                                                                        t.printStackTrace();
                                                                                        Log.d(TAG, call.request().toString());
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                });
                                                                            }
                                                                        } else {  //just prescriptions with existing medications
                                                                            ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, null);
                                                                            Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                            for (Prescription p : prescriptionArrayList) {
                                                                                Log.d(TAG, "prescription before added:" + p);
                                                                                Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                        Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                        if (response.code() < 500 && response.code() >= 400) {
                                                                                            try {
                                                                                                Log.d(TAG, response.errorBody().string());
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                        }
                                                                                        Log.d(TAG, "prescription added:" + response.body());
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                        t.printStackTrace();
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                });
                                                                            }
                                                                            progressDialog.dismiss();
                                                                            finish();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Consultation> call, Throwable t) {
                                                                        t.printStackTrace();
                                                                        progressDialog.dismiss();
                                                                        new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                                .setTitle("Error")
                                                                                .setMessage("Failed to update consultation record. Please try again.\nDetail: '" + t.toString() + "'")
                                                                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                                    @Override
                                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                                        dialog.dismiss();
                                                                                    }
                                                                                })
                                                                                .show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Visit> call, Throwable t) {
                                                            t.printStackTrace();
                                                            progressDialog.dismiss();
                                                            new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                                    .setTitle("Error")
                                                                    .setMessage("Failed to update visit record. Please try again.\nDetail: '" + t.toString() + "'")
                                                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.dismiss();
                                                                        }
                                                                    })
                                                                    .show();
                                                        }
                                                    });
                                                }


                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Patient> call, Throwable t) {
                                            t.printStackTrace();
                                            progressDialog.dismiss();
                                            new AlertDialog.Builder(PatientVisitEditActivity.this)
                                                    .setTitle("Error")
                                                    .setMessage("Failed to update patient record. Please try again.\nDetail: '" + t.toString() + "'")
                                                    .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .show();
                                        }
                                    });
                                }
                            } else {
                                if (thisTriage != null) {                                                             //existing patient new consultation edit triage  TODO update profile pic if personalData.getProfilePicBase64 exists
                                    Patient patient = generatePatient(personalData);
                                    if (patient != null) {
                                        Call<Patient> patientCall = patientService.editPatient("1", thisPatient.getPatientId(), patient);  //PUT patient
                                        patientCall.enqueue(new Callback<Patient>() {
                                            @Override
                                            public void onResponse(Call<Patient> call, Response<Patient> response) {
                                                if (response == null || response.code() < 200 || response.code() >= 300 || response.body() == null) {
                                                    onFailure(call, new Throwable("sth wrong: " + response.code()));
                                                } else {                                                          //PUT visit (iff tag number have been modified?)
                                                    //TODO edit documents
                                                    if (finalHPI != null) {
                                                        Call<Document> hpiCall = documentService.editDocument("1", finalHPI.getDocID(), new Document(finalHPI.getDocHTML()));
                                                        hpiCall.enqueue(new Callback<Document>() {
                                                            @Override
                                                            public void onResponse(Call<Document> call, Response<Document> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Document> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    }
                                                    if (finalSH != null) {
                                                        Call<Document> shCall = documentService.editDocument("1", finalSH.getDocID(), new Document(finalSH.getDocHTML()));
                                                        shCall.enqueue(new Callback<Document>() {
                                                            @Override
                                                            public void onResponse(Call<Document> call, Response<Document> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Document> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    }
                                                    if (finalFH != null) {
                                                        Call<Document> fhCall = documentService.editDocument("1", finalFH.getDocID(), new Document(finalFH.getDocHTML()));
                                                        fhCall.enqueue(new Callback<Document>() {
                                                            @Override
                                                            public void onResponse(Call<Document> call, Response<Document> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Document> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    }
                                                    if (finalPMH != null) {
                                                        Call<Document> pmhCall = documentService.editDocument("1", finalPMH.getDocID(), new Document(finalPMH.getDocHTML()));
                                                        pmhCall.enqueue(new Callback<Document>() {
                                                            @Override
                                                            public void onResponse(Call<Document> call, Response<Document> response) {

                                                            }

                                                            @Override
                                                            public void onFailure(Call<Document> call, Throwable t) {
                                                                t.printStackTrace();
                                                            }
                                                        });
                                                    }

                                                    Visit visit;
                                                    if (finalMedication.getCardArrayList().size() > 0 || finalMedication.getCardArrayList2().size() > 0) {
                                                        visit = generateVisit(response.body(), pd, Const.NextStation.PHARMACY);
                                                    } else {
                                                        visit = generateVisit(response.body(), pd, Const.NextStation.TRIAGE);
                                                    }
                                                    Log.d(TAG, "Editing Visit: " + visit);
                                                    if (visit != null) {
                                                        Call<Visit> visitCall = visitService.editVisit("1", visit, thisVisit.getId());
                                                        visitCall.enqueue(new Callback<Visit>() {
                                                            @Override
                                                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                                Log.d(TAG, "visit call response code: " + response.code());
                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                    try {
                                                                        Log.d(TAG, response.errorBody().string());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                                    onFailure(call, new Throwable("No response"));
                                                                } else {
                                                                    //PUT triage
                                                                    Log.d(TAG, "Editing triage: " + thisTriage);
                                                                    Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                    Call<Triage> triageCall = triageService.editTriage("1", triage, thisTriage.getId());
                                                                    triageCall.enqueue(new Callback<Triage>() {
                                                                        @Override
                                                                        public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                            Log.d(TAG, "triage call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            //do nothing, consultation and other stuff should take longer (i think)
//                                        progressDialog.dismiss();
//                                        finish();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Triage> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                            //TODO error dialog
                                                                        }
                                                                    });
                                                                    //POST consultation (then POST rd and prescription (with consultation_id)
//                                  Log.d(TAG, finalPregnancy.toString());
//                                  Log.d(TAG, finalROS.toString());
//                                  Log.d(TAG, finalRF.toString());
//                                  Log.d(TAG, finalPE.toString());
//                                  Log.d(TAG, finalConsultationRemark.toString());
                                                                    Consultation consultation = generateConsultation(response.body(), finalPregnancy, finalROS, finalRF, finalPE, finalConsultationRemark);
                                                                    Call<Consultation> consultationCall = consultationService.addConsultation("1", consultation);
                                                                    consultationCall.enqueue(new Callback<Consultation>() {
                                                                        @Override
                                                                        public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                                                                            if (response == null) {
                                                                                onFailure(call, new Throwable("Empty response"));
                                                                            } else if (response.code() >= 300 || response.code() < 200) {
                                                                                Log.d(TAG, "consultation call response code: " + response.code());
                                                                                onFailure(call, new Throwable("Error from server: " + response.code()));
                                                                            } else {
                                                                                //POST all rd and prescriptions
                                                                                ArrayList<RelatedData> relatedDataArrayList = generateRelatedDataPlural(response.body(), finalDH, finalScreening, finalAllergy, finalDiagnosis, finalInvestigation, finalAdvice, finalFollowUp);
                                                                                for (RelatedData r : relatedDataArrayList) {
                                                                                    Call<RelatedData> relatedDataCall = relatedDataService.addRelatedData("1", r);
                                                                                    relatedDataCall.enqueue(new Callback<RelatedData>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<RelatedData> call, Response<RelatedData> response) {
                                                                                            Log.d(TAG, "related data call response code: " + response.code());
                                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                                try {
                                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                                } catch (IOException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                            Log.d(TAG, "related data added:" + response.body());
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<RelatedData> call, Throwable t) {
                                                                                            t.printStackTrace();
                                                                                            progressDialog.dismiss();
                                                                                        }
                                                                                    });
                                                                                }
                                                                                final ArrayList<Medication> medicationArrayList = generateMedications(finalMedication);
                                                                                final Consultation c = response.body();
                                                                                if (medicationArrayList != null && medicationArrayList.size() > 0) {
                                                                                    final ArrayList<Prescription> newMedPreArrList = new ArrayList<>(medicationArrayList.size());
                                                                                    Log.d(TAG, "Medications: " + medicationArrayList.toString());
                                                                                    for (final Medication m : medicationArrayList) {
                                                                                        Call<Medication> medicationCall = medicationService.addMedication("1", m);
                                                                                        medicationCall.enqueue(new Callback<Medication>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<Medication> call, Response<Medication> response) {
                                                                                                if (response == null) {
                                                                                                    onFailure(call, new Throwable("Empty response"));
                                                                                                } else if (response.code() < 200 || response.code() >= 300) {
                                                                                                    onFailure(call, new Throwable("Error from server: " + response.code() + response.raw()));
                                                                                                } else {
                                                                                                    newMedPreArrList.add(new Prescription(response.body().getMedicationId(), m.getTempPrescriptionDescription()));
                                                                                                    if (newMedPreArrList.size() >= medicationArrayList.size()) {
                                                                                                        ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, newMedPreArrList);
                                                                                                        Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                                        for (Prescription p : prescriptionArrayList) {
                                                                                                            Log.d(TAG, "prescription before added:" + p);
                                                                                                            Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                                            prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                                                @Override
                                                                                                                public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                                                    Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                                                    if (response.code() < 500 && response.code() >= 400) {
                                                                                                                        try {
                                                                                                                            Log.d(TAG, response.errorBody().string());
                                                                                                                        } catch (IOException e) {
                                                                                                                            e.printStackTrace();
                                                                                                                        }
                                                                                                                    }
                                                                                                                    Log.d(TAG, "prescription added:" + response.body());
                                                                                                                }

                                                                                                                @Override
                                                                                                                public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                                                    t.printStackTrace();
                                                                                                                    progressDialog.dismiss();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                        progressDialog.dismiss();
                                                                                                        finish();
                                                                                                    }
                                                                                                }
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<Medication> call, Throwable t) {
                                                                                                t.printStackTrace();
                                                                                                Log.d(TAG, call.request().toString());
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                } else {  //just prescriptions with existing medications
                                                                                    ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, null);
                                                                                    Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                    for (Prescription p : prescriptionArrayList) {
                                                                                        Log.d(TAG, "prescription before added:" + p);
                                                                                        Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                        prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                            @Override
                                                                                            public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                                Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                                                    try {
                                                                                                        Log.d(TAG, response.errorBody().string());
                                                                                                    } catch (IOException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                }
                                                                                                Log.d(TAG, "prescription added:" + response.body());
                                                                                            }

                                                                                            @Override
                                                                                            public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                                t.printStackTrace();
                                                                                                progressDialog.dismiss();
                                                                                            }
                                                                                        });
                                                                                    }
                                                                                    progressDialog.dismiss();
                                                                                    finish();
                                                                                }
                                                                            }
                                                                            //POST all rd and prescriptions
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Consultation> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Visit> call, Throwable t) {
                                                                t.printStackTrace();
                                                                progressDialog.dismiss();
                                                                //TODO error dialog
                                                            }
                                                        });
                                                    }


                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Patient> call, Throwable t) {
                                                progressDialog.dismiss();
                                                t.printStackTrace();
                                            }
                                        });
                                    }
                                } else {                                                                              //existing patient new consultation new triage  TODO update profile pic if personalData.getProfilePicBase64 exists
                                    //PUT patient
                                    //POST visit
                                    //POST triage
                                    //POST consultation
                                    //POST related_data
                                    //POST investigation
                                    //POST prescription
                                    Patient patient = generatePatient(personalData);
                                    if (patient != null) {
                                        Call<Patient> patientCall = patientService.editPatient("1", thisPatient.getPatientId(), patient);  //PUT patient
                                        patientCall.enqueue(new Callback<Patient>() {
                                            @Override
                                            public void onResponse(Call<Patient> call, Response<Patient> response) {
                                                if (response == null || response.code() < 200 || response.code() >= 300 || response.body() == null) {
                                                    onFailure(call, new Throwable("sth wrong"));
                                                } else {                                                          //PUT visit (iff tag number have been modified?)
                                                    //TODO edit documents
                                                    Call<Document> hpiCall = documentService.editDocument("1", finalHPI.getDocID(), new Document(finalHPI.getDocHTML()));
                                                    Call<Document> shCall = documentService.editDocument("1", finalSH.getDocID(), new Document(finalSH.getDocHTML()));
                                                    Call<Document> fhCall = documentService.editDocument("1", finalFH.getDocID(), new Document(finalFH.getDocHTML()));
                                                    Call<Document> pmhCall = documentService.editDocument("1", finalPMH.getDocID(), new Document(finalPMH.getDocHTML()));
                                                    hpiCall.enqueue(new Callback<Document>() {
                                                        @Override
                                                        public void onResponse(Call<Document> call, Response<Document> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(Call<Document> call, Throwable t) {
                                                            t.printStackTrace();
                                                        }
                                                    });
                                                    shCall.enqueue(new Callback<Document>() {
                                                        @Override
                                                        public void onResponse(Call<Document> call, Response<Document> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(Call<Document> call, Throwable t) {
                                                            t.printStackTrace();
                                                        }
                                                    });
                                                    fhCall.enqueue(new Callback<Document>() {
                                                        @Override
                                                        public void onResponse(Call<Document> call, Response<Document> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(Call<Document> call, Throwable t) {
                                                            t.printStackTrace();
                                                        }
                                                    });
                                                    pmhCall.enqueue(new Callback<Document>() {
                                                        @Override
                                                        public void onResponse(Call<Document> call, Response<Document> response) {

                                                        }

                                                        @Override
                                                        public void onFailure(Call<Document> call, Throwable t) {
                                                            t.printStackTrace();
                                                        }
                                                    });
                                                    Visit visit = generateVisit(response.body(), pd, Const.NextStation.PHARMACY);
                                                    Log.d(TAG, "Editing Visit: " + visit);
                                                    if (visit != null) {
                                                        Call<Visit> visitCall = visitService.addVisit("1", visit);
                                                        visitCall.enqueue(new Callback<Visit>() {
                                                            @Override
                                                            public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                                Log.d(TAG, "visit call response code: " + response.code());
                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                    try {
                                                                        Log.d(TAG, response.errorBody().string());
                                                                    } catch (IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                                if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                                    onFailure(call, new Throwable("No response"));
                                                                } else {
                                                                    //PUT triage
                                                                    Log.d(TAG, "Editing triage: " + thisTriage);
                                                                    Log.d(TAG, "Editing consultation: " + thisConsultation);
                                                                    Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                    Call<Triage> triageCall = triageService.addTriage("1", triage);
                                                                    triageCall.enqueue(new Callback<Triage>() {
                                                                        @Override
                                                                        public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                            Log.d(TAG, "triage call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            //do nothing, consultation and other stuff should take longer (i think)
//                                        progressDialog.dismiss();
//                                        finish();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Triage> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                            //TODO error dialog
                                                                        }
                                                                    });
                                                                    //POST consultation (then POST rd and prescription (with consultation_id)
                                                                    Consultation consultation = generateConsultation(response.body(), finalPregnancy, finalROS, finalRF, finalPE, finalConsultationRemark);
                                                                    Call<Consultation> consultationCall = consultationService.addConsultation("1", consultation);
                                                                    consultationCall.enqueue(new Callback<Consultation>() {
                                                                        @Override
                                                                        public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                                                                            Log.d(TAG, "consultation call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            //POST all rd and prescriptions
                                                                            //TODO
//                                    Log.d(TAG, "consultation call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            Log.d(TAG, response.body().toString());
                                                                            //POST all rd and prescriptions
                                                                            //TODO
                                                                            ArrayList<RelatedData> relatedDataArrayList = generateRelatedDataPlural(response.body(), finalDH, finalScreening, finalAllergy, finalDiagnosis, finalInvestigation, finalAdvice, finalFollowUp);
                                                                            for (RelatedData r : relatedDataArrayList) {
                                                                                Call<RelatedData> relatedDataCall = relatedDataService.addRelatedData("1", r);
                                                                                relatedDataCall.enqueue(new Callback<RelatedData>() {
                                                                                    @Override
                                                                                    public void onResponse(Call<RelatedData> call, Response<RelatedData> response) {
                                                                                        Log.d(TAG, "related data call response code: " + response.code());
                                                                                        if (response.code() < 500 && response.code() >= 400) {
                                                                                            try {
                                                                                                Log.d(TAG, response.errorBody().string());
                                                                                            } catch (IOException e) {
                                                                                                e.printStackTrace();
                                                                                            }
                                                                                        }
                                                                                        Log.d(TAG, "related data added:" + response.body());
                                                                                    }

                                                                                    @Override
                                                                                    public void onFailure(Call<RelatedData> call, Throwable t) {
                                                                                        t.printStackTrace();
                                                                                        progressDialog.dismiss();
                                                                                    }
                                                                                });
                                                                            }
                                                                            final ArrayList<Medication> medicationArrayList = generateMedications(finalMedication);
                                                                            final Consultation c = response.body();
                                                                            if (medicationArrayList != null && medicationArrayList.size() > 0) {
                                                                                final ArrayList<Prescription> newMedPreArrList = new ArrayList<>(medicationArrayList.size());
                                                                                Log.d(TAG, "Medications: " + medicationArrayList.toString());
                                                                                for (final Medication m : medicationArrayList) {
                                                                                    Call<Medication> medicationCall = medicationService.addMedication("1", m);
                                                                                    medicationCall.enqueue(new Callback<Medication>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<Medication> call, Response<Medication> response) {
                                                                                            if (response == null) {
                                                                                                onFailure(call, new Throwable("Empty response"));
                                                                                            } else if (response.code() < 200 || response.code() >= 300) {
                                                                                                onFailure(call, new Throwable("Error from server: " + response.code() + response.raw()));
                                                                                            } else {
                                                                                                newMedPreArrList.add(new Prescription(response.body().getMedicationId(), m.getTempPrescriptionDescription()));
                                                                                                if (newMedPreArrList.size() >= medicationArrayList.size()) {
                                                                                                    ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, newMedPreArrList);
                                                                                                    Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                                    for (Prescription p : prescriptionArrayList) {
                                                                                                        Log.d(TAG, "prescription before added:" + p);
                                                                                                        Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                                        prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                                            @Override
                                                                                                            public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                                                Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                                                                    try {
                                                                                                                        Log.d(TAG, response.errorBody().string());
                                                                                                                    } catch (IOException e) {
                                                                                                                        e.printStackTrace();
                                                                                                                    }
                                                                                                                }
                                                                                                                Log.d(TAG, "prescription added:" + response.body());
                                                                                                            }

                                                                                                            @Override
                                                                                                            public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                                                t.printStackTrace();
                                                                                                                progressDialog.dismiss();
                                                                                                            }
                                                                                                        });
                                                                                                    }
                                                                                                    progressDialog.dismiss();
                                                                                                    finish();
                                                                                                }
                                                                                            }
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<Medication> call, Throwable t) {
                                                                                            t.printStackTrace();
                                                                                            Log.d(TAG, call.request().toString());
                                                                                        }
                                                                                    });
                                                                                }
                                                                            } else {  //just prescriptions with existing medications
                                                                                ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, null);
                                                                                Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                for (Prescription p : prescriptionArrayList) {
                                                                                    Log.d(TAG, "prescription before added:" + p);
                                                                                    Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                    prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                        @Override
                                                                                        public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                            Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                                try {
                                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                                } catch (IOException e) {
                                                                                                    e.printStackTrace();
                                                                                                }
                                                                                            }
                                                                                            Log.d(TAG, "prescription added:" + response.body());
                                                                                        }

                                                                                        @Override
                                                                                        public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                            t.printStackTrace();
                                                                                            progressDialog.dismiss();
                                                                                        }
                                                                                    });
                                                                                }
                                                                                progressDialog.dismiss();
                                                                                finish();
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Consultation> call, Throwable t) {
                                                                            t.printStackTrace();
                                                                            progressDialog.dismiss();
                                                                        }
                                                                    });
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<Visit> call, Throwable t) {
                                                                t.printStackTrace();
                                                                progressDialog.dismiss();
                                                                //TODO error dialog
                                                            }
                                                        });
                                                    }


                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Patient> call, Throwable t) {
                                                t.printStackTrace();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    } else {
                        if (isTriage) {                                                                           //new patient new triage  TODO upload profile pic if personalData.getProfilePicBase64 exists
                            //POST patient
                            Log.d(TAG, "New patient new visit new triage");
                            Patient patient = generatePatient(personalData);
                            if (patient != null) {
                                Call<Patient> patientCall = patientService.addPatient("1", patient);
                                patientCall.enqueue(new Callback<Patient>() {
                                    @Override
                                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                                        if (response == null) {
                                            onFailure(call, new Throwable("Empty response"));
                                        } else if (response.code() >= 300 || response.code() < 200) {
                                            onFailure(call, new Throwable("Error from server: " + response.code()));
                                        } else {
                                            if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                onFailure(call, new Throwable("No response"));
                                            } else {
                                                //TODO create 4 new documents
                                                List<DocumentType> documentTypeList = Cache.DatabaseData.getDocumentTypes(getBaseContext());
                                                for (DocumentType documentType : documentTypeList) {
                                                    Call<Document> documentCall = documentService.addDocument("1", new Document("<html><head></head><body></body></html>", documentType.getId(), response.body().getPatientId()));
                                                    documentCall.enqueue(new Callback<Document>() {
                                                        @Override
                                                        public void onResponse(Call<Document> call, Response<Document> response) {
                                                            if (response == null) {
                                                                onFailure(call, new Throwable("Empty response"));
                                                            } else if (response.code() >= 300 || response.code() < 200) {
                                                                onFailure(call, new Throwable("Wrong: " + response.code()));
                                                            } else if (response.body() == null) {
                                                                onFailure(call, new Throwable("Empty response body"));
                                                            } //else >> successful >> idc
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Document> call, Throwable t) { //TODO check if we need to dismiss the pd
                                                            t.printStackTrace();
                                                        }
                                                    });
                                                }
                                                //POST visit
                                                Visit visit = generateVisit(response.body(), pd, Const.NextStation.CONSULTATION);
                                                if (visit != null) {
                                                    Call<Visit> visitCall = visitService.addVisit("1", visit);
                                                    visitCall.enqueue(new Callback<Visit>() {
                                                        @Override
                                                        public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                            Log.d(TAG, "visit call response code: " + response.code());
                                                            if (response == null) {
                                                                onFailure(call, new Throwable("Empty response"));
                                                            } else if (response.code() >= 300 || response.code() < 200) {
                                                                onFailure(call, new Throwable("error from server"));
                                                            } else {
                                                                Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                                if (triage != null) {
                                                                    //POST triage
                                                                    Call<Triage> triageCall = triageService.addTriage("1", triage);
                                                                    triageCall.enqueue(new Callback<Triage>() {
                                                                        @Override
                                                                        public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                            Log.d(TAG, "triage call response code: " + response.code());
                                                                            if (response.code() < 500 && response.code() >= 400) {
                                                                                try {
                                                                                    Log.d(TAG, response.errorBody().string());
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                            progressDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                        @Override
                                                                        public void onFailure(Call<Triage> call, Throwable t) {
                                                                            progressDialog.dismiss();
                                                                            t.printStackTrace();
                                                                        }
                                                                    });
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Visit> call, Throwable t) {
                                                            t.printStackTrace();
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Patient> call, Throwable t) {
                                        t.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                });


                            } else {
                                Log.d(TAG, "What the hack? generate patient generate empty patient?");
                                //TODO
                            }

                        } else {                                                                                  //new patient new consultation (and triage)
                            //POST patient
                            //POST visit
                            //POST triage
                            //POST document
                            //POST consultation
                            //POST related_data
                            //POST investigation
                            //POST prescription
                            Patient patient = generatePatient(personalData);
                            if (patient != null) {
                                Call<Patient> patientCall = patientService.addPatient("1", patient);  //POST patient
                                patientCall.enqueue(new Callback<Patient>() {
                                    @Override
                                    public void onResponse(Call<Patient> call, Response<Patient> response) {
                                        if (response == null || response.code() < 200 || response.code() >= 300 || response.body() == null) {
                                            onFailure(call, new Throwable("sth wrong"));
                                        } else {                                                          //POST visit (iff tag number have been modified?)
                                            List<DocumentType> documentTypeList = Cache.DatabaseData.getDocumentTypes(getBaseContext());
                                            for (DocumentType documentType : documentTypeList) {
                                                Call<Document> documentCall = documentService.addDocument("1", new Document("<html><head></head><body></body></html>", documentType.getId(), response.body().getPatientId()));
                                                documentCall.enqueue(new Callback<Document>() {
                                                    @Override
                                                    public void onResponse(Call<Document> call, Response<Document> response) {
                                                        if (response == null) {
                                                            onFailure(call, new Throwable("Empty response"));
                                                        } else if (response.code() >= 300 || response.code() < 200) {
                                                            onFailure(call, new Throwable("Wrong: " + response.code()));
                                                        } else if (response.body() == null) {
                                                            onFailure(call, new Throwable("Empty response body"));
                                                        } //else >> successful >> idc
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Document> call, Throwable t) { //TODO pd.dismiss?
                                                        t.printStackTrace();
                                                    }
                                                });
                                            }
                                            Visit visit = generateVisit(response.body(), pd, Const.NextStation.PHARMACY);
                                            Log.d(TAG, "Editing Visit: " + visit);
                                            if (visit != null) {
                                                Call<Visit> visitCall = visitService.addVisit("1", visit);
                                                visitCall.enqueue(new Callback<Visit>() {
                                                    @Override
                                                    public void onResponse(Call<Visit> call, Response<Visit> response) {
                                                        Log.d(TAG, "visit call response code: " + response.code());
                                                        if (response.code() < 500 && response.code() >= 400) {
                                                            try {
                                                                Log.d(TAG, response.errorBody().string());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        if (response.body() == null || response.code() > 299 || response.code() < 200) {
                                                            onFailure(call, new Throwable("No response"));
                                                        } else {
                                                            //POST triage
                                                            Log.d(TAG, "Editing triage: " + thisTriage);
                                                            Log.d(TAG, "Editing consultation: " + thisConsultation);
                                                            Triage triage = generateTriage(response.body(), vs, cc, tr);
                                                            Call<Triage> triageCall = triageService.addTriage("1", triage);
                                                            triageCall.enqueue(new Callback<Triage>() {
                                                                @Override
                                                                public void onResponse(Call<Triage> call, Response<Triage> response) {
                                                                    Log.d(TAG, "triage call response code: " + response.code());
                                                                    if (response.code() < 500 && response.code() >= 400) {
                                                                        try {
                                                                            Log.d(TAG, response.errorBody().string());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                    Log.d(TAG, response.body().toString());
                                                                    //do nothing, consultation and other stuff should take longer (i think)
//                                        progressDialog.dismiss();
//                                        finish();
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Triage> call, Throwable t) {
                                                                    t.printStackTrace();
                                                                    progressDialog.dismiss();
                                                                    //TODO error dialog
                                                                }
                                                            });
                                                            //POST consultation (then POST rd and prescription (with consultation_id)
                                                            Consultation consultation = generateConsultation(response.body(), finalPregnancy, finalROS, finalRF, finalPE, finalConsultationRemark);
                                                            Call<Consultation> consultationCall = consultationService.addConsultation("1", consultation);
                                                            consultationCall.enqueue(new Callback<Consultation>() {
                                                                @Override
                                                                public void onResponse(Call<Consultation> call, Response<Consultation> response) {
                                                                    Log.d(TAG, "consultation call response code: " + response.code());
                                                                    if (response.code() < 500 && response.code() >= 400) {
                                                                        try {
                                                                            Log.d(TAG, response.errorBody().string());
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                    Log.d(TAG, response.body().toString());
                                                                    //POST all rd and prescriptions
                                                                    //TODO
                                                                    ArrayList<RelatedData> relatedDataArrayList = generateRelatedDataPlural(response.body(), finalDH, finalScreening, finalAllergy, finalDiagnosis, finalInvestigation, finalAdvice, finalFollowUp);
                                                                    for (RelatedData r : relatedDataArrayList) {
                                                                        Call<RelatedData> relatedDataCall = relatedDataService.addRelatedData("1", r);
                                                                        relatedDataCall.enqueue(new Callback<RelatedData>() {
                                                                            @Override
                                                                            public void onResponse(Call<RelatedData> call, Response<RelatedData> response) {
                                                                                Log.d(TAG, "related data call response code: " + response.code());
                                                                                if (response.code() < 500 && response.code() >= 400) {
                                                                                    try {
                                                                                        Log.d(TAG, response.errorBody().string());
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                }
                                                                                Log.d(TAG, "related data added:" + response.body());
                                                                            }

                                                                            @Override
                                                                            public void onFailure(Call<RelatedData> call, Throwable t) {
                                                                                t.printStackTrace();
                                                                                progressDialog.dismiss();
                                                                            }
                                                                        });
                                                                    }
                                                                    final ArrayList<Medication> medicationArrayList = generateMedications(finalMedication);
                                                                    final Consultation c = response.body();
                                                                    if (medicationArrayList != null && medicationArrayList.size() > 0) {
                                                                        final ArrayList<Prescription> newMedPreArrList = new ArrayList<>(medicationArrayList.size());
                                                                        Log.d(TAG, "Medications: " + medicationArrayList.toString());
                                                                        for (final Medication m : medicationArrayList) {
                                                                            Call<Medication> medicationCall = medicationService.addMedication("1", m);
                                                                            medicationCall.enqueue(new Callback<Medication>() {
                                                                                @Override
                                                                                public void onResponse(Call<Medication> call, Response<Medication> response) {
                                                                                    if (response == null) {
                                                                                        onFailure(call, new Throwable("Empty response"));
                                                                                    } else if (response.code() < 200 || response.code() >= 300) {
                                                                                        onFailure(call, new Throwable("Error from server: " + response.code() + response.raw()));
                                                                                    } else {
                                                                                        newMedPreArrList.add(new Prescription(response.body().getMedicationId(), m.getTempPrescriptionDescription()));
                                                                                        if (newMedPreArrList.size() >= medicationArrayList.size()) {
                                                                                            ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, newMedPreArrList);
                                                                                            Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                                            for (Prescription p : prescriptionArrayList) {
                                                                                                Log.d(TAG, "prescription before added:" + p);
                                                                                                Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                                                prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                                    @Override
                                                                                                    public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                                        Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                                        if (response.code() < 500 && response.code() >= 400) {
                                                                                                            try {
                                                                                                                Log.d(TAG, response.errorBody().string());
                                                                                                            } catch (IOException e) {
                                                                                                                e.printStackTrace();
                                                                                                            }
                                                                                                        }
                                                                                                        Log.d(TAG, "prescription added:" + response.body());
                                                                                                    }

                                                                                                    @Override
                                                                                                    public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                                        t.printStackTrace();
                                                                                                        progressDialog.dismiss();
                                                                                                    }
                                                                                                });
                                                                                            }
                                                                                            progressDialog.dismiss();
                                                                                            finish();
                                                                                        }
                                                                                    }
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<Medication> call, Throwable t) {
                                                                                    t.printStackTrace();
                                                                                    Log.d(TAG, call.request().toString());
                                                                                }
                                                                            });
                                                                        }
                                                                    } else {  //just prescriptions with existing medications
                                                                        ArrayList<Prescription> prescriptionArrayList = generatePrescriptions(c, finalMedication, null);
                                                                        Log.d(TAG, "Prescription list: " + prescriptionArrayList);
                                                                        for (Prescription p : prescriptionArrayList) {
                                                                            Log.d(TAG, "prescription before added:" + p);
                                                                            Call<Prescription> prescriptionCall = prescriptionService.addPrescription("1", p);
                                                                            prescriptionCall.enqueue(new Callback<Prescription>() {
                                                                                @Override
                                                                                public void onResponse(Call<Prescription> call, Response<Prescription> response) {
                                                                                    Log.d(TAG, "prescription data call response code b: " + response.code());
                                                                                    if (response.code() < 500 && response.code() >= 400) {
                                                                                        try {
                                                                                            Log.d(TAG, response.errorBody().string());
                                                                                        } catch (IOException e) {
                                                                                            e.printStackTrace();
                                                                                        }
                                                                                    }
                                                                                    Log.d(TAG, "prescription added:" + response.body());
                                                                                }

                                                                                @Override
                                                                                public void onFailure(Call<Prescription> call, Throwable t) {
                                                                                    t.printStackTrace();
                                                                                    progressDialog.dismiss();
                                                                                }
                                                                            });
                                                                        }
                                                                        progressDialog.dismiss();
                                                                        finish();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onFailure(Call<Consultation> call, Throwable t) {
                                                                    t.printStackTrace();
                                                                    progressDialog.dismiss();
                                                                }
                                                            });
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<Visit> call, Throwable t) {
                                                        t.printStackTrace();
                                                        progressDialog.dismiss();
                                                        //TODO error dialog
                                                    }
                                                });
                                            }


                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Patient> call, Throwable t) {
                                        t.printStackTrace();
                                        progressDialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                } else {
                    progressDialog.dismiss();
                }
                return false;
            default:
                return false;
        }
    }

    private Patient generatePatient(PersonalData personalData) {
        if (personalData != null) {
            Patient patient = new Patient();
            patient.setAddress(personalData.getAddress());
            patient.setBirthDate(personalData.getBirthDate());
            patient.setBirthMonth(personalData.getBirthMonth());
            patient.setBirthYear(personalData.getBirthYear());
            patient.setFirstName(personalData.getFirstName());
            patient.setMiddleName(personalData.getMiddleName());
            patient.setLastName(personalData.getLastName());
            patient.setNativeName(personalData.getNativeName());
            patient.setPhoneNumber(personalData.getPhoneNumber());
            patient.setClinicId(Cache.CurrentUser.getClinic(getBaseContext()).getClinicId());
            patient.setGenderId(personalData.getGenderId());
            Log.d(TAG, "output " + patient.toString());
            return patient;
        } else
            return null;
    }

    private Visit generateVisit(Patient patient, PersonalData personalData, int nextStation) {
        Visit visit = new Visit();
        try {
            visit.setTag(personalData.getTagNumber());
        } catch (Exception e) {
            e.printStackTrace();
            visit.setTag((int) (Math.random() * 100));
        }
        visit.setPatientId(patient.getPatientId());
        visit.setNextStation(nextStation);
        Log.d(TAG, "output " + visit.toString());
        return visit;
    }

    private Triage generateTriage(Visit visit, VitalSigns vitalSigns, String chiefComplaint, String remark) {
        Triage triage = new Triage();
        //TODO
        Date date = new Date();
        triage.setStartTime(date);
        triage.setEndTime(date);
        if (vitalSigns != null) {
            triage.setSystolic(vitalSigns.getSystolic());
            triage.setDiastolic(vitalSigns.getDiastolic());
            triage.setHeartRate(vitalSigns.getPulseRate());
            triage.setRespiratoryRate(vitalSigns.getRespiratoryRate());
            triage.setWeight(vitalSigns.getWeight());
            triage.setHeight(vitalSigns.getHeight());
            triage.setTemperature(vitalSigns.getTemperature());
            triage.setSpo2(vitalSigns.getSpo2());
            triage.setHeadCircumference(vitalSigns.getHeadCircumference());
            triage.setBloodSugar(vitalSigns.getBloodSugar());
            triage.setLastDewormingTabletDate(vitalSigns.getLdd());
        }
        if (chiefComplaint != null) {
            triage.setChiefComplaints(chiefComplaint);
        }
        if (remark != null) {
            triage.setRemark(remark);
        }
        triage.setVisitId(visit.getId());
        Log.d(TAG, "output " + triage.toString());
        return triage;
    }

    private Consultation generateConsultation(@NonNull Visit visit, Pregnancy pregnancy, ListOfCards ros, ListOfCards rf, ListOfCards pe, String consultationRemark) {
        Consultation consultation = new Consultation();
        Date date = new Date();
        consultation.setVisitId(visit.getId());
        consultation.setStartTime(date);
        consultation.setEndTime(date);
        if (pregnancy != null) {
            consultation.setPregLmp(pregnancy.getLmdDate());
            consultation.setPregCurrPreg(pregnancy.getCurrPreg());
            if (consultation.getPregCurrPreg())
                consultation.setPregGestation(pregnancy.getGestation());
            consultation.setPregBreastFeeding(pregnancy.getBreastFeeding());
            consultation.setPregContraceptive(pregnancy.getContraceptiveUse());
            consultation.setPregNumPreg(pregnancy.getNoOfPregnancy());
            consultation.setPregNumLiveBirth(pregnancy.getNoOfLiveBirth());
            consultation.setPregNumMiscarriage(pregnancy.getNoOfMiscarriage());
            consultation.setPregNumAbortion(pregnancy.getNoOfAbortion());
            consultation.setPregNumStillBirth(pregnancy.getNoOfStillBirth());
            consultation.setPregRemark(pregnancy.getOtherInformation());
        }
        if (ros != null && ros.getCardArrayList() != null && ros.getCardArrayList().size() > 0) {
            HashMap<String, String> cardHM = new HashMap<>();
            for (Card c : ros.getCardArrayList()) {
                cardHM.put(c.getCardTitle(), c.getCardDescription());
            }
            consultation.setRosGeneral(cardHM.get("General"));
            consultation.setRosRespi(cardHM.get("Respiratory"));
            consultation.setRosGastro(cardHM.get("Gastrointestinal"));
            consultation.setRosGenital(cardHM.get("Genital/Urinary"));
            consultation.setRosEnt(cardHM.get("ENT"));
            consultation.setRosSkin(cardHM.get("Skin"));
            consultation.setRosLocomotor(cardHM.get("Locomotor"));
            consultation.setRosNeruology(cardHM.get("Neruology"));
            consultation.setRosOther(cardHM.get("Other"));
        }
        if (rf != null && rf.getCardArrayList() != null && rf.getCardArrayList().size() > 0) {
            HashMap<String, String> cardHM = new HashMap<>();
            for (Card c : rf.getCardArrayList()) {
                cardHM.put(c.getCardTitle(), c.getCardDescription());
            }
            consultation.setRfAlertness(cardHM.get("Alertness"));
            consultation.setRfBreathing(cardHM.get("Breathing"));
            consultation.setRfCirculation(cardHM.get("Circulation"));
            consultation.setRfDehydration(cardHM.get("Dehydration"));
            consultation.setRfDefg(cardHM.get("DEFG"));
        }
        if (pe != null && pe.getCardArrayList() != null && pe.getCardArrayList().size() > 0) {
            HashMap<String, String> cardHM = new HashMap<>();
            for (Card c : pe.getCardArrayList()) {
                cardHM.put(c.getCardTitle(), c.getCardDescription());
            }
            consultation.setPeGeneral(cardHM.get("General Appearance"));
            consultation.setPeRespiratory(cardHM.get("Respiratory"));
            consultation.setPeCardio(cardHM.get("Cardiovascular"));
            consultation.setPeGastro(cardHM.get("Gastrointestinal"));
            consultation.setPeGenital(cardHM.get("Genital/Urinary"));
            consultation.setPeEnt(cardHM.get("ENT"));
            consultation.setPeSkin(cardHM.get("Skin"));
            consultation.setPeOther(cardHM.get("Other"));
        }
        if (consultationRemark != null) {
            consultation.setRemark(consultationRemark);
        }
        Log.d(TAG, "output " + consultation.toString());
        return consultation;
    }

    private ArrayList<RelatedData> generateRelatedDataPlural(@NonNull Consultation consultation, @Nullable ListOfCards drugHistory, @Nullable ListOfCards screening, @Nullable ListOfCards allergy, @Nullable ListOfCards diagnosis, @Nullable ListOfCards investigations, @Nullable ListOfCards advice, @Nullable ListOfCards followup) {
        ArrayList<RelatedData> relatedDataArrayList = new ArrayList<>();
        if (drugHistory != null && drugHistory.getCardArrayList() != null && drugHistory.getCardArrayList().size() > 0) {
            for (Card c : drugHistory.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setCategory(6);
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (screening != null && screening.getCardArrayList() != null && screening.getCardArrayList().size() > 0) {
            for (Card c : screening.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(1);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (allergy != null && allergy.getCardArrayList() != null && allergy.getCardArrayList().size() > 0) {
            for (Card c : allergy.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(2);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (diagnosis != null && diagnosis.getCardArrayList() != null && diagnosis.getCardArrayList().size() > 0) {
            for (Card c : diagnosis.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(3);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (investigations != null && investigations.getCardArrayList() != null && investigations.getCardArrayList().size() > 0) {
            for (Card c : investigations.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(8);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (advice != null && advice.getCardArrayList() != null && advice.getCardArrayList().size() > 0) {
            for (Card c : advice.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(4);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        if (followup != null && followup.getCardArrayList() != null && followup.getCardArrayList().size() > 0) {
            for (Card c : followup.getCardArrayList()) {
                RelatedData aRelatedData = new RelatedData();
                aRelatedData.setCategory(5);
                aRelatedData.setConsultationId(consultation.getId());
                aRelatedData.setData(c.getCardTitle());
                aRelatedData.setRemark(c.getCardDescription());
                relatedDataArrayList.add(aRelatedData);
            }
        }
        return relatedDataArrayList;
    }

    private ArrayList<Medication> generateMedications(ListOfCards prescriptions) {
        ArrayList<Card> newMedi = prescriptions.getCardArrayList2();
        if (newMedi != null && newMedi.size() > 0) {
            ArrayList<Medication> medications = new ArrayList<>();
            for (Card c : newMedi) {
                Medication m = new Medication(c.getCardTitle(), c.getCardDescription());
                medications.add(m);
            }
            return medications;
        } else
            return null;
    }

    /**
     * TODO before calling this someone needs to call generateMedications, and generate new prescriptions and add to this queue
     *
     * @param consultation
     * @param prescriptions
     * @param fromNewMedications
     * @return
     */
    private ArrayList<Prescription> generatePrescriptions(@NonNull Consultation consultation, ListOfCards prescriptions, @Nullable ArrayList<Prescription> fromNewMedications) {
        ArrayList<Prescription> prescriptionArrayList = new ArrayList<>();
        if (fromNewMedications != null) {
            for (Prescription prescription : fromNewMedications) {
                prescription.setConsultationId(consultation.getId());
                prescription.setPrescribed(false);
                prescriptionArrayList.add(prescription);
            }
        }
        if (prescriptions != null) {
            if (prescriptions.getCardArrayList() != null) {
                if (prescriptions.getCardArrayList().size() > 0) {
                    for (Card c : prescriptions.getCardArrayList()) {
                        Prescription aPrescription = new Prescription();
                        aPrescription.setConsultationId(consultation.getId());
                        aPrescription.setPrescribed(false);
                        aPrescription.setDetail(c.getCardDescription());
                        aPrescription.setMedicationId(c.getCardTitle());
                        prescriptionArrayList.add(aPrescription);
                    }
                } else {
                    Log.d(TAG, "size is 0");
                }
            } else {
                Log.d(TAG, "null list");
            }
        } else {
            Log.d(TAG, "null presc");
        }
        return prescriptionArrayList;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (viewPager != null) {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_personal_data:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.nav_vital_signs:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.nav_cc:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.nav_triage_remark:
                    viewPager.setCurrentItem(3);
                    break;
                case R.id.nav_hpi:
                    viewPager.setCurrentItem(4);
                    break;
                case R.id.nav_pmh:
                    viewPager.setCurrentItem(5);
                    break;
                case R.id.nav_fh:
                    viewPager.setCurrentItem(6);
                    break;
                case R.id.nav_sh:
                    viewPager.setCurrentItem(7);
                    break;
                case R.id.nav_dh:
                    viewPager.setCurrentItem(8);
                    break;
                case R.id.nav_screening:
                    viewPager.setCurrentItem(9);
                    break;
                case R.id.nav_allergy:
                    viewPager.setCurrentItem(10);
                    break;
                case R.id.nav_pregnancy:
                    viewPager.setCurrentItem(11);
                    break;
                case R.id.nav_ros:
                    viewPager.setCurrentItem(12);
                    break;
                case R.id.nav_rf:
                    viewPager.setCurrentItem(13);
                    break;
                case R.id.nav_pe:
                    viewPager.setCurrentItem(14);
                    break;
                case R.id.nav_cd:
                    viewPager.setCurrentItem(15);
                    break;
                case R.id.nav_investigation:
                    viewPager.setCurrentItem(16);
                    break;
                case R.id.nav_medication:
                    viewPager.setCurrentItem(17);
                    break;
                case R.id.nav_advice:
                    viewPager.setCurrentItem(18);
                    break;
                case R.id.nav_fu:
                    viewPager.setCurrentItem(19);
                    break;
                case R.id.nav_consultation_remark:
                    viewPager.setCurrentItem(20);
                    break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_patient_edit, menu);
        MenuItem menuItem1 = menu.findItem(R.id.confirm);
        MenuItem menuItem2 = menu.findItem(R.id.history);
        menuItem1.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_check).color(Color.WHITE).actionBar());
        menuItem2.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_history).color(Color.WHITE).actionBar());
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * TODO education page?
     */
    public class viewPagerAdapter extends FragmentStatePagerAdapter {
        public viewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (personalDataFragment == null) {
                        personalDataFragment = PersonalDataFragment.newInstance(thisPatient);
                    }
                    return personalDataFragment;
                case 1:
                    if (vitalSignFragment == null) {
                        vitalSignFragment = VitalSignFragment.newInstance(thisTriage);
                    }
                    return vitalSignFragment;
                case 2:
                    if (chiefComplaintFragment == null) {
                        if (thisTriage != null)
                            chiefComplaintFragment = ChiefComplaintFragment.newInstance(thisTriage.getChiefComplaints());
                        else
                            chiefComplaintFragment = ChiefComplaintFragment.newInstance(null);
                    }
                    return chiefComplaintFragment;
                case 3:
                    if (triageRemarkFragment == null) {
                        if (thisTriage != null)
                            triageRemarkFragment = RemarkFragment.newInstance(thisTriage.getRemark(), true);
                        else
                            triageRemarkFragment = RemarkFragment.newInstance(null, false);
                    }
                    return triageRemarkFragment;
                case 4:
                    if (hpiFragment == null) {
                        if (thisPatient != null)
                            hpiFragment = DocumentFragment.newInstance(thisPatient.getPatientId(), 0);
                        else
                            hpiFragment = DocumentFragment.newInstance(null, 0);
                    }
                    return hpiFragment;
                case 5:
                    if (pmhFragment == null) {
                        if (thisPatient != null)
                            pmhFragment = DocumentFragment.newInstance(thisPatient.getPatientId(), 3);
                        else
                            pmhFragment = DocumentFragment.newInstance(null, 1);
                    }
                    return pmhFragment;
                case 6:
                    if (fhFragment == null) {
                        if (thisPatient != null)
                            fhFragment = DocumentFragment.newInstance(thisPatient.getPatientId(), 1);
                        else
                            fhFragment = DocumentFragment.newInstance(null, 1);
                    }
                    return fhFragment;
                case 7:
                    if (shFragment == null) {
                        if (thisPatient != null)
                            shFragment = DocumentFragment.newInstance(thisPatient.getPatientId(), 2);
                        else
                            shFragment = DocumentFragment.newInstance(null, 2);
                    }
                    return shFragment;
                case 8:
                    if (dhFragment == null) {
                        if (thisConsultation != null)
                            dhFragment = ListOfCardsFragment.newInstance("Drug History", Const.RelatedDataCategory.DRUG_HISTORY, thisConsultation.getId());
                        else
                            dhFragment = ListOfCardsFragment.newInstance("Drug History", Const.RelatedDataCategory.DRUG_HISTORY, null);
                    }
                    return dhFragment;
                case 9:
                    if (screeningFragment == null) {
                        if (thisConsultation != null)
                            screeningFragment = ListOfCardsFragment.newInstance("Screening", Const.RelatedDataCategory.SCREENING, thisConsultation.getId());
                        else
                            screeningFragment = ListOfCardsFragment.newInstance("Screening", Const.RelatedDataCategory.SCREENING, null);
                    }
                    return screeningFragment;
                case 10:
                    if (allergyFragment == null) {
                        if (thisConsultation != null)
                            allergyFragment = ListOfCardsFragment.newInstance("Allergy", Const.RelatedDataCategory.ALLERGY, thisConsultation.getId());
                        else
                            allergyFragment = ListOfCardsFragment.newInstance("Allergy", Const.RelatedDataCategory.ALLERGY, null);
                    }
                    return allergyFragment;
                case 11:
                    if (pregnancyFragment == null) {
                        pregnancyFragment = PregnancyFragment.newInstance(thisConsultation);
                    }
                    return pregnancyFragment;
                case 12:
                    if (rosFragment == null) {
                        rosFragment = ListOfCardsFragment.newInstance("Review of System", DEFAULT_REVIEW_OF_SYSTEM, thisConsultation);
                    }
                    return rosFragment;
                case 13:
                    if (rfFragment == null) {
                        rfFragment = ListOfCardsFragment.newInstance("Red Flags", DEFAULT_RED_FLAG, thisConsultation);
                    }
                    return rfFragment;
                case 14:
                    if (peFragment == null) {
                        peFragment = ListOfCardsFragment.newInstance("Physical Examination", DEFAULT_PHYSICAL_EXAMINATION, thisConsultation);
                    }
                    return peFragment;
                case 15:
                    if (diagnosisFragment == null) {
                        if (thisConsultation != null)
                            diagnosisFragment = ListOfCardsFragment.newInstance("Clinical Diagnosis", Const.RelatedDataCategory.DIAGNOSIS, thisConsultation.getId());
                        else
                            diagnosisFragment = ListOfCardsFragment.newInstance("Clinical Diagnosis", 3, null);
                    }
                    return diagnosisFragment;
                case 16:
                    if (investigationFragment == null) {
                        investigationFragment = ListOfCardsFragment.newInstance("Investigation");
                    }
                    return investigationFragment;
                case 17:
                    if (medicationFragment == null) {
                        if (thisConsultation != null)
                            medicationFragment = ListOfCardsFragment.newInstance("Medication", 6, thisConsultation.getId());
                        else
                            medicationFragment = ListOfCardsFragment.newInstance("Medication", 6, null);
                    }
                    return medicationFragment;
                case 18:
                    if (adviceFragment == null) {
                        if (thisConsultation != null)
                            adviceFragment = ListOfCardsFragment.newInstance("Advice", 4, thisConsultation.getId());
                        else
                            adviceFragment = ListOfCardsFragment.newInstance("Advice", 4, null);
                    }
                    return adviceFragment;
                case 19:
                    if (followupFragment == null) {
                        if (thisConsultation != null)
                            followupFragment = ListOfCardsFragment.newInstance("Follow-up", 5, thisConsultation.getId());
                        else
                            followupFragment = ListOfCardsFragment.newInstance("Follow-up", 5, null);
                    }
                    return followupFragment;
                case 20:
                    if (consultationRemarkFragment == null) {
                        if (thisConsultation != null)
                            consultationRemarkFragment = RemarkFragment.newInstance(thisConsultation.getRemark(), true);
                        else
                            consultationRemarkFragment = RemarkFragment.newInstance(null, false);
                    }
                    return consultationRemarkFragment;
                default:
                    return PersonalDataFragment.newInstance(thisPatient);
            }
        }

        @Override
        public int getCount() {
            return tabs.size();
        }
    }

    private class customViewPagerOnPageChangeListener extends TabLayout.TabLayoutOnPageChangeListener {
        private NavigationView navigationView;

        public customViewPagerOnPageChangeListener(TabLayout tabLayout, NavigationView navigationView) {
            super(tabLayout);
            this.navigationView = navigationView;
        }

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            if (navigationView != null) {
                switch (position) {
                    case 0:
                        navigationView.setCheckedItem(R.id.nav_personal_data);
                        break;
                    case 1:
                        navigationView.setCheckedItem(R.id.nav_vital_signs);
                        break;
                    case 2:
                        navigationView.setCheckedItem(R.id.nav_cc);
                        break;
                    case 3:
                        navigationView.setCheckedItem(R.id.nav_triage_remark);
                        break;
                    case 4:
                        navigationView.setCheckedItem(R.id.nav_hpi);
                        break;
                    case 5:
                        navigationView.setCheckedItem(R.id.nav_pmh);
                        break;
                    case 6:
                        navigationView.setCheckedItem(R.id.nav_fh);
                        break;
                    case 7:
                        navigationView.setCheckedItem(R.id.nav_sh);
                        break;
                    case 8:
                        navigationView.setCheckedItem(R.id.nav_dh);
                        break;
                    case 9:
                        navigationView.setCheckedItem(R.id.nav_screening);
                        break;
                    case 10:
                        navigationView.setCheckedItem(R.id.nav_allergy);
                        break;
                    case 11:
                        navigationView.setCheckedItem(R.id.nav_pregnancy);
                        break;
                    case 12:
                        navigationView.setCheckedItem(R.id.nav_ros);
                        break;
                    case 13:
                        navigationView.setCheckedItem(R.id.nav_rf);
                        break;
                    case 14:
                        navigationView.setCheckedItem(R.id.nav_pe);
                        break;
                    case 15:
                        navigationView.setCheckedItem(R.id.nav_cd);
                        break;
                    case 16:
                        navigationView.setCheckedItem(R.id.nav_investigation);
                        break;
                    case 17:
                        navigationView.setCheckedItem(R.id.nav_medication);
                        break;
                    case 18:
                        navigationView.setCheckedItem(R.id.nav_advice);
                        break;
                    case 19:
                        navigationView.setCheckedItem(R.id.nav_fu);
                        break;
                    case 20:
                        navigationView.setCheckedItem(R.id.nav_consultation_remark);
                        break;
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            super.onPageScrollStateChanged(state);
        }
    }

}
