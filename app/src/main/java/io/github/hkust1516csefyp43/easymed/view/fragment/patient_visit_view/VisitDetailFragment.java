package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Consultation;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Document;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Prescription;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.RelatedData;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Triage;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Visit;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.activity.PatientVisitEditActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VisitDetailFragment extends Fragment {
    public final static String TAG = VisitDetailFragment.class.getSimpleName();
    private static String key1 = Const.BundleKey.VISIT_ID;
    private static String key2 = Const.BundleKey.ON_OR_OFF;
    private static String key3 = Const.BundleKey.EDIT_PATIENT;
    private static String key4 = Const.BundleKey.IS_TRIAGE;

    private OnFragmentInteractionListener mListener;

    private FloatingActionButton floatingActionButton;

    /**
     * Difference between this fabOn and the fabOn in PatientVisitViewActivity (PVVA): the code starts
     * PVVA, and the PVVA will be passed into this. However, if for some reason PVVA does not pass
     * fabOn, this would still works.
     * TODO what should the default be?
     */
    private Boolean fabOn = false;
    private Boolean isTriage = true;

    private Patient patient;
    private Visit visit;
    private Triage triage;
    private Consultation consultation;
    private List<RelatedData> allRelatedData;

    private List<Prescription> allPrescriptions;

    private List<RelatedData> drugHistories;
    private List<RelatedData> screenings;
    private List<RelatedData> allergies;
    private List<RelatedData> diagnosis;
    private List<RelatedData> investigations;
    private List<RelatedData> advices;
    private List<RelatedData> followups;

    private List<RelatedData> physicalExaminations;
    private List<RelatedData> pmhs;
    private List<RelatedData> ross;
    private List<RelatedData> redFlags;
    private Document hpiDoc;
    private Document fhDoc;
    private Document shDoc;
    private LinearLayout linearLayout;

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");


    public static VisitDetailFragment newInstance(Patient patient, Visit visit, Boolean fabOn, Boolean isTriage) {
        VisitDetailFragment fragment = new VisitDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(key1, visit);
        args.putBoolean(key2, fabOn);
        args.putSerializable(key3, patient);
        args.putBoolean(key4, isTriage);
        fragment.setArguments(args);
        return fragment;
    }

    public VisitDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Serializable serializable = getArguments().getSerializable(key1);
            if (serializable instanceof Visit) {
                visit = (Visit) serializable;
            }
            fabOn = getArguments().getBoolean(key2, false);
            //key3
            serializable = getArguments().getSerializable(key3);
            if (serializable instanceof Patient) {
                patient = (Patient) serializable;
            }
            isTriage = getArguments().getBoolean(key4, true);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_visit_detail, container, false);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);

        if (!fabOn) {
            floatingActionButton.setVisibility(View.GONE);
        } else {
            floatingActionButton.setImageDrawable(new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_edit).actionBar().color(Color.WHITE));
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PatientVisitEditActivity.class);
                    intent.putExtra(Const.BundleKey.IS_TRIAGE, isTriage);
                    if (patient != null) {                                //pass patient
                        intent.putExtra(Const.BundleKey.EDIT_PATIENT, patient);
                    }
                    if (visit != null) {                                  //pass visit
                        intent.putExtra(Const.BundleKey.WHOLE_VISIT, visit);
                    }
                    if (triage != null) {                                 //pass triage
                        intent.putExtra(Const.BundleKey.WHOLE_TRIAGE, triage);
                    }
                    if (consultation != null) {
                        intent.putExtra(Const.BundleKey.WHOLE_CONSULTATION, consultation);
                    }
                    //enter from post pharmacy >> never (fabOn should be false)
                    //enter from not yet >> ?
                    startActivity(intent);
                }
            });
        }
        linearLayout = (LinearLayout) view.findViewById(R.id.ll_visit_info);
        if (linearLayout != null) {

            //TODO fix sometimes triage first, sometimes consultation first (race condition)

            //Create retrofit
            OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
            ohc1.readTimeout(1, TimeUnit.MINUTES);
            ohc1.connectTimeout(1, TimeUnit.MINUTES);
            final Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(Const.Database.getCurrentAPI())
                    .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                    .client(ohc1.build())
                    .build();

            //Triage
            v2API.triages triages = retrofit.create(v2API.triages.class);
            Call<List<Triage>> triageCall = triages.getTriages("1", visit.getId());
            triageCall.enqueue(new Callback<List<Triage>>() {
                @Override
                public void onResponse(Call<List<Triage>> call, Response<List<Triage>> response) {
                    Log.d(TAG, visit.getId());
                    if (response.body() != null && response.body().size() != 0) {
                        triage = response.body().get(0);
                        if (triage != null) {
                            Context context = getContext();
                            if (context != null) {
                                TextView tvTriageTitle = new TextView(context);
                                tvTriageTitle.setText("Triage");
                                tvTriageTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                tvTriageTitle.setTextColor(Color.BLACK);
                                tvTriageTitle.setTypeface(null, Typeface.BOLD);
                                linearLayout.addView(tvTriageTitle);

                                if (triage.getUserId() != null) {
                                    TextView tvTriageUserId = new TextView(context);
                                    tvTriageUserId.setText("User ID: " + triage.getUserId());
                                    linearLayout.addView(tvTriageUserId);
                                }
                                if (triage.getId() != null) {
                                    TextView tvTriageId = new TextView(context);
                                    tvTriageId.setText("Triage ID: " + triage.getId());
                                    linearLayout.addView(tvTriageId);
                                }
                                if (triage.getVisitId() != null) {
                                    TextView tvTriageVisitId = new TextView(context);
                                    tvTriageVisitId.setText("Visit ID: " + triage.getVisitId());
                                    linearLayout.addView(tvTriageVisitId);
                                }
                                if (triage.getStartTime() != null) {
                                    TextView tvTriageStart = new TextView(context);
                                    tvTriageStart.setText("Start time: " + timeFormat.format(triage.getStartTime()));
                                    linearLayout.addView(tvTriageStart);
                                }
                                if (triage.getEndTime() != null) {
                                    TextView tvTriageEnd = new TextView(context);
                                    tvTriageEnd.setText("End time: " + timeFormat.format(triage.getEndTime()));
                                    linearLayout.addView(tvTriageEnd);
                                }
                                if (triage.getChiefComplaints() != null) {
                                    TextView tvTriageCC = new TextView(context);
                                    tvTriageCC.setText("Chief complaint: " + triage.getChiefComplaints());
                                    linearLayout.addView(tvTriageCC);
                                }
                                if (triage.getDiastolic() != null && triage.getSystolic() != null) {
                                    TextView tvBloodPressure = new TextView(context);
                                    tvBloodPressure.setText("Blood pressure: " + triage.getSystolic() + " / " + triage.getDiastolic());
                                    linearLayout.addView(tvBloodPressure);
                                }
                                if (triage.getHeartRate() != null) {
                                    TextView tvTriageHeartRate = new TextView(context);
                                    tvTriageHeartRate.setText("Heart rate: " + triage.getHeartRate());
                                    linearLayout.addView(tvTriageHeartRate);
                                }
                                if (triage.getRespiratoryRate() != null) {
                                    TextView tvTriageRespRate = new TextView(context);
                                    tvTriageRespRate.setText("Respiratory rate: " + triage.getRespiratoryRate());
                                    linearLayout.addView(tvTriageRespRate);
                                }
                                if (triage.getTemperature() != null) {
                                    TextView tvTriageTemp = new TextView(context);
                                    tvTriageTemp.setText("Temperature: " + triage.getTemperature());
                                    linearLayout.addView(tvTriageTemp);
                                }
                                if (triage.getBloodSugar() != null) {
                                    TextView tvTriageBloodSugar = new TextView(context);
                                    tvTriageBloodSugar.setText("Blood sugar: " + triage.getBloodSugar());
                                    linearLayout.addView(tvTriageBloodSugar);
                                }
                                if (triage.getSpo2() != null) {
                                    TextView tvTriageSpo2 = new TextView(context);
                                    tvTriageSpo2.setText("Spo2: " + triage.getSpo2());
                                    linearLayout.addView(tvTriageSpo2);
                                }
                                if (triage.getHeadCircumference() != null) {
                                    TextView tvTriageHeadCircumference = new TextView(context);
                                    tvTriageHeadCircumference.setText("Head circumference: " + triage.getHeadCircumference());
                                    linearLayout.addView(tvTriageHeadCircumference);
                                }
                                if (triage.getHeight() != null) {
                                    TextView tvTriageHeight = new TextView(context);
                                    tvTriageHeight.setText("Height: " + String.valueOf(triage.getHeight()));
                                    linearLayout.addView(tvTriageHeight);
                                }
                                if (triage.getWeight() != null) {
                                    TextView tvTriageWeight = new TextView(context);
                                    tvTriageWeight.setText("Weight: " + String.valueOf(triage.getWeight()));
                                    linearLayout.addView(tvTriageWeight);
                                }
                                if (triage.getLastDewormingTabletDate() != null) {
                                    TextView tvTriageLDD = new TextView(context);
                                    tvTriageLDD.setText("Last deworming tablet date: " + dateFormat.format(triage.getLastDewormingTabletDate()));
                                    linearLayout.addView(tvTriageLDD);
                                }
                                if (triage.getRemark() != null) {
                                    TextView tvTriageRemark = new TextView(context);
                                    tvTriageRemark.setText("Remark: " + triage.getRemark());
                                    linearLayout.addView(tvTriageRemark);
                                }
                            }
                        }
                    } else {
                        onFailure(null, null);
                    }

                    //Consultation
                    v2API.consultations consultations = retrofit.create(v2API.consultations.class);
                    Call<List<Consultation>> consultationCall = consultations.getConsultations("1", visit.getId());
                    consultationCall.enqueue(new Callback<List<Consultation>>() {
                        @Override
                        public void onResponse(Call<List<Consultation>> call, Response<List<Consultation>> response) {
                            Log.d(TAG, visit.getId());
                            Log.d(TAG, response.body().toString());
                            if (response.body() != null && response.body().size() != 0) {
                                consultation = response.body().get(0);
                                if (consultation != null) {
                                    final Context context = getContext();
                                    if (context != null) {
                                        final List<Medication> medicationList = Cache.DatabaseData.getMedications(context);
                                        TextView tvConsultationTitle = new TextView(context);
                                        tvConsultationTitle.setText("Consultation");
                                        tvConsultationTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                                        tvConsultationTitle.setTextColor(Color.BLACK);
                                        tvConsultationTitle.setTypeface(null, Typeface.BOLD);
                                        linearLayout.addView(tvConsultationTitle);

                                        if (consultation.getUserId() != null) {
                                            TextView tvConsultationUserId = new TextView(context);
                                            tvConsultationUserId.setText("User ID: " + consultation.getUserId());
                                            linearLayout.addView(tvConsultationUserId);
                                        }
                                        if (consultation.getId() != null) {
                                            TextView tvConsultationId = new TextView(context);
                                            tvConsultationId.setText("Consultation ID: " + consultation.getId());
                                            linearLayout.addView(tvConsultationId);
                                        }
                                        if (consultation.getVisitId() != null) {
                                            TextView tvConsultationVisitId = new TextView(context);
                                            tvConsultationVisitId.setText("Visit ID: " + consultation.getVisitId());
                                            linearLayout.addView(tvConsultationVisitId);
                                        }
                                        if (consultation.getStartTime() != null) {
                                            TextView textView4 = new TextView(context);
                                            textView4.setText(String.valueOf("Start time: " + timeFormat.format(consultation.getStartTime())));
                                            linearLayout.addView(textView4);
                                        }
                                        if (consultation.getEndTime() != null) {
                                            TextView textView5 = new TextView(context);
                                            textView5.setText(String.valueOf("End time: " + timeFormat.format(consultation.getEndTime())));
                                            linearLayout.addView(textView5);
                                        }
                                        if (consultation.getPeCardio() != null) {
                                            TextView textView7 = new TextView(context);
                                            textView7.setText("PE cardio: " + consultation.getPeCardio());
                                            linearLayout.addView(textView7);
                                        }
                                        if (consultation.getPeEnt() != null) {
                                            TextView textView8 = new TextView(context);
                                            textView8.setText("PE ent: " + consultation.getPeEnt());
                                            linearLayout.addView(textView8);
                                        }
                                        if (consultation.getPeGastro() != null) {
                                            TextView textView9 = new TextView(context);
                                            textView9.setText("PE gastro: " + consultation.getPeGastro());
                                            linearLayout.addView(textView9);
                                        }
                                        if (consultation.getPeGenital() != null) {
                                            TextView textView10 = new TextView(context);
                                            textView10.setText("PE genital: " + consultation.getPeGenital());
                                            linearLayout.addView(textView10);
                                        }
                                        if (consultation.getPeGeneral() != null) {
                                            TextView textView11 = new TextView(context);
                                            textView11.setText("PE general: " + consultation.getPeGeneral());
                                            linearLayout.addView(textView11);
                                        }
                                        if (consultation.getPeOther() != null) {
                                            TextView textView12 = new TextView(context);
                                            textView12.setText("PE other: " + consultation.getPeOther());
                                            linearLayout.addView(textView12);
                                        }
                                        if (consultation.getPeRespiratory() != null) {
                                            TextView textView13 = new TextView(context);
                                            textView13.setText("PE respiratory: " + consultation.getPeRespiratory());
                                            linearLayout.addView(textView13);
                                        }
                                        if (consultation.getPeSkin() != null) {
                                            TextView textView14 = new TextView(context);
                                            textView14.setText("PE skin: " + consultation.getPeSkin());
                                            linearLayout.addView(textView14);
                                        }
                                        if (consultation.getPregBreastFeeding() != null) {
                                            TextView textView15 = new TextView(context);
                                            textView15.setText("Preg breast feeding: " + consultation.getPregBreastFeeding());
                                            linearLayout.addView(textView15);
                                        }
                                        if (consultation.getPregContraceptive() != null) {
                                            TextView textView16 = new TextView(context);
                                            textView16.setText("Preg contraceptive: " + consultation.getPregContraceptive());
                                            linearLayout.addView(textView16);
                                        }
                                        if (consultation.getPregCurrPreg() != null) {
                                            TextView textView17 = new TextView(context);
                                            textView17.setText("Preg curr preg: " + consultation.getPregCurrPreg());
                                            linearLayout.addView(textView17);
                                        }
                                        if (consultation.getPregGestation() != null) {
                                            TextView textView18 = new TextView(context);
                                            textView18.setText("Preg gestation: " + consultation.getPregGestation());
                                            linearLayout.addView(textView18);
                                        }
                                        if (consultation.getPregLmp() != null) {
                                            TextView textView19 = new TextView(context);
                                            textView19.setText("Preg lmp: " + dateFormat.format(consultation.getPregLmp()));
                                            linearLayout.addView(textView19);
                                        }
                                        if (consultation.getPregNumAbortion() != null) {
                                            TextView textView20 = new TextView(context);
                                            textView20.setText("Preg num abortion: " + consultation.getPregNumAbortion());
                                            linearLayout.addView(textView20);
                                        }
                                        if (consultation.getPregNumLiveBirth() != null) {
                                            TextView textView21 = new TextView(context);
                                            textView21.setText("Preg num live birth: " + consultation.getPregNumLiveBirth());
                                            linearLayout.addView(textView21);
                                        }
                                        if (consultation.getPregNumMiscarriage() != null) {
                                            TextView textView22 = new TextView(context);
                                            textView22.setText("Preg num miscarriage: " + consultation.getPregNumMiscarriage());
                                            linearLayout.addView(textView22);
                                        }
                                        if (consultation.getPregNumPreg() != null) {
                                            TextView textView23 = new TextView(context);
                                            textView23.setText("Preg num preg: " + consultation.getPregNumPreg());
                                            linearLayout.addView(textView23);
                                        }
                                        if (consultation.getPregNumStillBirth() != null) {
                                            TextView textView24 = new TextView(context);
                                            textView24.setText("Preg num still birth: " + consultation.getPregNumStillBirth());
                                            linearLayout.addView(textView24);
                                        }
                                        if (consultation.getPregRemark() != null) {
                                            TextView textView25 = new TextView(context);
                                            textView25.setText("Preg remark: " + consultation.getPregRemark());
                                            linearLayout.addView(textView25);
                                        }
                                        if (consultation.getRemark() != null) {
                                            TextView textView26 = new TextView(context);
                                            textView26.setText("Remark " + consultation.getRemark());
                                            linearLayout.addView(textView26);
                                        }
                                        if (consultation.getRfAlertness() != null) {
                                            TextView textView27 = new TextView(context);
                                            textView27.setText("RF alertness: " + consultation.getRfAlertness());
                                            linearLayout.addView(textView27);
                                        }
                                        if (consultation.getRfBreathing() != null) {
                                            TextView textView28 = new TextView(context);
                                            textView28.setText("RF breathing: " + consultation.getRfBreathing());
                                            linearLayout.addView(textView28);
                                        }
                                        if (consultation.getRfCirculation() != null) {
                                            TextView textView29 = new TextView(context);
                                            textView29.setText("RF circulation: " + consultation.getRfCirculation());
                                            linearLayout.addView(textView29);
                                        }
                                        if (consultation.getRfDefg() != null) {
                                            TextView textView30 = new TextView(context);
                                            textView30.setText("RF defg: " + consultation.getRfDefg());
                                            linearLayout.addView(textView30);
                                        }
                                        if (consultation.getRfDehydration() != null) {
                                            TextView textView31 = new TextView(context);
                                            textView31.setText("RF Dehydration: " + consultation.getRfDehydration());
                                            linearLayout.addView(textView31);
                                        }
                                        if (consultation.getRosRespi() != null) {
                                            TextView textView33 = new TextView(context);
                                            textView33.setText("ROS respi: " + consultation.getRosRespi());
                                            linearLayout.addView(textView33);
                                        }
                                        if (consultation.getRosCardio() != null) {
                                            TextView textView34 = new TextView(context);
                                            textView34.setText("ROS cardio: " + consultation.getRosCardio());
                                            linearLayout.addView(textView34);
                                        }
                                        if (consultation.getRosGastro() != null) {
                                            TextView textView35 = new TextView(context);
                                            textView35.setText("ROS gastro: " + consultation.getRosGastro());
                                            linearLayout.addView(textView35);
                                        }
                                        if (consultation.getRosGenital() != null) {
                                            TextView textView36 = new TextView(context);
                                            textView36.setText("ROS genital: " + consultation.getRosGenital());
                                            linearLayout.addView(textView36);
                                        }
                                        if (consultation.getRosEnt() != null) {
                                            TextView textView37 = new TextView(context);
                                            textView37.setText("ROS Ent: " + consultation.getRosEnt());
                                            linearLayout.addView(textView37);
                                        }
                                        if (consultation.getRosSkin() != null) {
                                            TextView textView38 = new TextView(context);
                                            textView38.setText("ROS skin: " + consultation.getRosSkin());
                                            linearLayout.addView(textView38);
                                        }
                                        if (consultation.getRosLocomotor() != null) {
                                            TextView textView39 = new TextView(context);
                                            textView39.setText("ROS locomotor: " + consultation.getRosLocomotor());
                                            linearLayout.addView(textView39);
                                        }
                                        if (consultation.getRosNeruology() != null) {
                                            TextView textView40 = new TextView(context);
                                            textView40.setText("ROS neurology: " + consultation.getRosNeruology());
                                            linearLayout.addView(textView40);
                                        }

                                        //related data call
                                        v2API.related_data related_data = retrofit.create(v2API.related_data.class);
                                        Call<List<RelatedData>> relatedDataCall = related_data.getRelatedDataPlural("1", consultation.getId(), null, null, null, null, null, null);
                                        relatedDataCall.enqueue(new Callback<List<RelatedData>>() {
                                            @Override
                                            public void onResponse(Call<List<RelatedData>> call, Response<List<RelatedData>> response) {

                                                if (response.body() != null && response.body().size() > 0) {
                                                    allRelatedData = response.body();
                                                    advices = new ArrayList<RelatedData>();
                                                    allergies = new ArrayList<RelatedData>();
                                                    diagnosis = new ArrayList<RelatedData>();
                                                    drugHistories = new ArrayList<RelatedData>();
                                                    followups = new ArrayList<RelatedData>();
                                                    investigations = new ArrayList<RelatedData>();
                                                    screenings = new ArrayList<RelatedData>();

                                                    for (RelatedData r : allRelatedData) {
                                                        if (r.getCategory() == Const.RelatedDataCategory.ADVICE) {
                                                            advices.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.ALLERGY) {
                                                            allergies.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.DIAGNOSIS) {
                                                            diagnosis.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.DRUG_HISTORY) {
                                                            drugHistories.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.FOLLOW_UP) {
                                                            followups.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.INVESTIGATION) {
                                                            investigations.add(r);
                                                        } else if (r.getCategory() == Const.RelatedDataCategory.SCREENING) {
                                                            screenings.add(r);
                                                        }
                                                    }

                                                    if (advices.size() > 0) {
                                                        TextView tv1 = new TextView(context);
                                                        String output = "Advices: ";
                                                        for (RelatedData data : advices) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }
                                                        }
                                                        tv1.setText(output);
                                                        linearLayout.addView(tv1);
                                                    }
                                                    if (allergies.size() > 0) {
                                                        TextView tv2 = new TextView(context);
                                                        String output = "Allergies: ";
                                                        for (RelatedData data : allergies) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }                                                        }
                                                        tv2.setText(output);
                                                        linearLayout.addView(tv2);
                                                    }
                                                    if (diagnosis.size() > 0) {
                                                        TextView tv3 = new TextView(context);
                                                        String output = "Diagnosis: ";
                                                        for (RelatedData data : diagnosis) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }                                                        }
                                                        tv3.setText(output);
                                                        linearLayout.addView(tv3);
                                                    }
                                                    if (investigations.size() > 0) {
                                                        TextView tv4 = new TextView(context);
                                                        String output = "Investigations: ";
                                                        for (RelatedData data : investigations) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }                                                        }
                                                        tv4.setText(output);
                                                        linearLayout.addView(tv4);
                                                    }
                                                    if (drugHistories.size() > 0) {
                                                        TextView tv5 = new TextView(context);
                                                        String output = "Drug Histories: ";
                                                        for (RelatedData data : drugHistories) {
                                                            if (medicationList != null){
                                                                for (Medication m: medicationList){
                                                                    if (data.getData().equals(m.getMedicationId())) {
                                                                        if (data.getRemark() == null) {
                                                                            output += ("\n\t" + data.getData());
                                                                        } else{
                                                                            output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                                        }                                                                    }
                                                                }
                                                            }
                                                        }
                                                        tv5.setText(output);
                                                        linearLayout.addView(tv5);
                                                    }
                                                    if (followups.size() > 0) {
                                                        TextView tv6 = new TextView(context);
                                                        String output = "Follow-ups: ";
                                                        for (RelatedData data : followups) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }                                                        }
                                                        tv6.setText(output);
                                                        linearLayout.addView(tv6);
                                                    }
                                                    if (screenings.size() > 0) {
                                                        TextView tv7 = new TextView(context);
                                                        String output = "Screenings: ";
                                                        for (RelatedData data : screenings) {
                                                            if (data.getRemark() == null) {
                                                                output += ("\n\t" + data.getData());
                                                            } else{
                                                                output += ("\n\t" + data.getData() + ": " + data.getRemark());
                                                            }                                                        }
                                                        tv7.setText(output);
                                                        linearLayout.addView(tv7);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<RelatedData>> call, Throwable t) {

                                            }
                                        });

                                        //prescription call
                                        v2API.prescriptions prescriptionService = retrofit.create(v2API.prescriptions.class);
                                        final Call<List<Prescription>> prescriptions = prescriptionService.getPrescriptions("1", null, null, consultation.getId(), null, null, null, null);
                                        prescriptions.enqueue(new Callback<List<Prescription>>() {
                                            @Override
                                            public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                                                //TODO fill the UI
                                                if (response.body() != null && response.body().size() > 0) {
                                                    allPrescriptions = response.body();
                                                    String output = "Prescriptions: ";
                                                    for (Prescription p: allPrescriptions){
                                                        if (medicationList != null){
                                                            for (Medication m: medicationList){
                                                                if (p.getMedicationId().equals(m.getMedicationId())) {
                                                                    if (p.getDetail() != null){
                                                                        output += ("\n\t" + m.getMedication() + ": " + p.getDetail());
                                                                    } else{
                                                                        output += ("\n\t" + m.getMedication());
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    TextView tvPrescription = new TextView(context);
                                                    tvPrescription.setText(output);
                                                    linearLayout.addView(tvPrescription);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<List<Prescription>> call, Throwable t) {

                                            }
                                        });
                                    }
                                }
                            } else {
                                onFailure(null, null);
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Consultation>> call, Throwable t) {

                        }
                    });
                }

                @Override
                public void onFailure(Call<List<Triage>> call, Throwable t) {

                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

//checked for deprecated command lines