package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Card;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.ListOfCards;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Consultation;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Keyword;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Prescription;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.RelatedData;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.custom.TwoEditTextDialogCustomView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListOfCardsFragment extends Fragment implements OnFragmentInteractionListener, OnSendData {
  private static final String TAG = ListOfCardsFragment.class.getSimpleName();

  private OnFragmentInteractionListener mListener;

  private RecyclerView recyclerView;
  private FloatingActionButton fab;
  private TextView tvAssistance;

  private FragRecyclerViewAdapter adapter;

  private String title;
  private String[] preFillItems;
  private int category;
  private String consultationId = null;
  private Consultation consultation = null;

  private ArrayList<Card> cardList = new ArrayList<>();

  //prescription specific stuff
  private HashMap<String, String> medicationVsIdHM = new HashMap<>();
  private HashMap<String, String> idVsMedicationHM = new HashMap<>();
  private boolean inMedicationPage = false;
  private String[] shortcutsArray = {"po", "od", "bid", "tid", "mg", "ml", "/7", "/52", "/12"};

  public static ListOfCardsFragment newInstance(String title) {
    ListOfCardsFragment fragment = new ListOfCardsFragment();
    Bundle args = new Bundle();
    args.putString(Const.BundleKey.KEY_TITLE, title);
    fragment.setArguments(args);
    return fragment;
  }

  public static ListOfCardsFragment newInstance(String title, int category, @Nullable String consultationId) {
    ListOfCardsFragment fragment = new ListOfCardsFragment();
    Bundle args = new Bundle();
    args.putString(Const.BundleKey.KEY_TITLE, title);
    args.putInt(Const.BundleKey.RELATED_DATA_CATEGORY, category);
    args.putString(Const.BundleKey.CONSULTATION_ID, consultationId);
    fragment.setArguments(args);
    return fragment;
  }

  public static ListOfCardsFragment newInstance(String title, String[] preFillItems, @Nullable Consultation consultation) {
    ListOfCardsFragment fragment = new ListOfCardsFragment();
    Bundle args = new Bundle();
    args.putString(Const.BundleKey.KEY_TITLE, title);
    args.putStringArray(Const.BundleKey.KEY_PRE_FILL_ITEMS, preFillItems);
    args.putSerializable(Const.BundleKey.WHOLE_CONSULTATION, consultation);
    fragment.setArguments(args);
    return fragment;
  }

  public ListOfCardsFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Bundle bundle = getArguments();
    if (bundle != null) {
      title = bundle.getString(Const.BundleKey.KEY_TITLE);
      preFillItems = bundle.getStringArray(Const.BundleKey.KEY_PRE_FILL_ITEMS);
      category = bundle.getInt(Const.BundleKey.RELATED_DATA_CATEGORY, -1);
      Log.d(TAG, "qqq5: " + category);
      consultationId = bundle.getString(Const.BundleKey.CONSULTATION_ID);
      Serializable serializable = bundle.getSerializable(Const.BundleKey.WHOLE_CONSULTATION);
      if (serializable instanceof Consultation)
        consultation = (Consultation) serializable;
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_list_of_cards, container, false);
    fab = (FloatingActionButton) view.findViewById(R.id.fab_patient_edit);
    recyclerView = (RecyclerView) view.findViewById(R.id.rv_patient_edit);
    tvAssistance = (TextView) view.findViewById(R.id.tv_assistance);

    Log.d(TAG, "existing data no default cards");
    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(1, TimeUnit.MINUTES);
    ohc1.connectTimeout(1, TimeUnit.MINUTES);
    final Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();

    recyclerView.setHasFixedSize(true);
    fab.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_add).color(Color.WHITE).paddingDp(3).sizeDp(16));

    final TwoEditTextDialogCustomView tetdcv = new TwoEditTextDialogCustomView(getContext(), null, title);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new MaterialDialog.Builder(getContext())
            .title("Add")
            .customView(tetdcv, true)
            .positiveText("Confirm")
            .negativeText("Cancel")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (adapter != null) {
                  if (tvAssistance != null) {
                    tvAssistance.setVisibility(View.GONE);
                  }
                  ArrayList<String> data = tetdcv.getData();
                  tetdcv.clearData();
                  if (data != null) {
                    Log.d(TAG, data.toString());
                    if (data.get(0) != null && data.get(1) != null) {
                      cardList.add(new Card(data.get(0), data.get(1)));
                      if (adapter != null)
                        adapter.notifyDataSetChanged();
                      else
                        Log.d(TAG, "opps");
                    }
                  } else {
                    Toast.makeText(getContext(), "Something is wrong. ", Toast.LENGTH_SHORT).show();
                  }
                }
              }
            })
            .onNegative(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                if (adapter != null && cardList != null && cardList.size() <= 0 && tvAssistance != null) {
                  tvAssistance.setVisibility(View.VISIBLE);
                }
                tetdcv.clearData();
                dialog.dismiss();
              }
            })
            .show();
      }
    });

    if (tvAssistance != null && title != null && title.length() > 0) {
      tvAssistance.setText("Add " + title + " ->");
    }

    if (consultation != null && preFillItems != null && preFillItems.length > 0) {                  //TODO fill default cards with existing data
      Log.d(TAG, "existing data w/ default cards");
      fab.setVisibility(View.GONE);
      tvAssistance.setVisibility(View.GONE);
      if (title.compareTo("Physical Examination") == 0) {
        cardList.add(new Card("General Appearance", consultation.getPeGeneral()));
        cardList.add(new Card("Respiratory", consultation.getPeRespiratory()));
        cardList.add(new Card("Cardiovascular", consultation.getPeCardio()));
        cardList.add(new Card("Gastrointestinal", consultation.getPeGastro()));
        cardList.add(new Card("Genital/Urinary", consultation.getPeGenital()));
        cardList.add(new Card("ENT", consultation.getPeEnt()));
        cardList.add(new Card("Skin", consultation.getPeSkin()));
        cardList.add(new Card("Other", consultation.getPeOther()));
      } else if (title.compareTo("Review of System") == 0) {
        cardList.add(new Card("General", consultation.getRosGeneral()));
        cardList.add(new Card("Respiratory", consultation.getRosRespi()));
        cardList.add(new Card("Cardiovascular", consultation.getRosCardio()));
        cardList.add(new Card("Gastrointestinal", consultation.getRosGastro()));
        cardList.add(new Card("Genital/Urinary", consultation.getRosGenital()));
        cardList.add(new Card("ENT", consultation.getRosEnt()));
        cardList.add(new Card("Skin", consultation.getRosSkin()));
        cardList.add(new Card("Locomotor", consultation.getRosLocomotor()));
        cardList.add(new Card("Neurology", consultation.getRosNeruology()));
        cardList.add(new Card("Other", consultation.getRosOther()));
      } else if (title.compareTo("Red Flags") == 0) {
        cardList.add(new Card("Alertness", consultation.getRfAlertness()));
        cardList.add(new Card("Breathing", consultation.getRfBreathing()));
        cardList.add(new Card("Circulation", consultation.getRfCirculation()));
        cardList.add(new Card("Dehydration", consultation.getRfDehydration()));
        cardList.add(new Card("DEFG", consultation.getRfDefg()));
      }
      adapter = new FragRecyclerViewAdapter(getContext(), true, null, title);
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    } else if (category > 0 && consultationId != null) {                                            //fill with existing data (from API call)
      if (category == 6) {
        inMedicationPage = true;
        v2API.medications medicationService = retrofit.create(v2API.medications.class);
        Call<List<Medication>> medicationsCall = medicationService.getMedications("1", null, null, null, null, null, null);
        medicationsCall.enqueue(new Callback<List<Medication>>() {
          @Override
          public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
            if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0 && response.body().size() > 0) {
              ArrayList<String> keywordArrayList = new ArrayList<>();
              for (Medication k : response.body()) {
                keywordArrayList.add(k.getMedication());
                medicationVsIdHM.put(k.getMedication(), k.getMedicationId());
                idVsMedicationHM.put(k.getMedicationId(), k.getMedication());
              }
              final TwoEditTextDialogCustomView tetdcv = new TwoEditTextDialogCustomView(getContext(), keywordArrayList, title, null, null, false, shortcutsArray);
              fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  new MaterialDialog.Builder(getContext())
                      .title("Add")
                      .customView(tetdcv, true)
                      .positiveText("Confirm")
                      .negativeText("Cancel")
                      .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                          if (adapter != null) {
                            if (tvAssistance != null) {
                              tvAssistance.setVisibility(View.GONE);
                            }
                            ArrayList<String> data = tetdcv.getData();
                            tetdcv.clearData();
                            Log.d(TAG, data.toString());
                            if (data.get(0) != null && data.get(1) != null) {
                              cardList.add(new Card(data.get(0), data.get(1)));
                              if (adapter != null)
                                adapter.notifyDataSetChanged();
                              else
                                Log.d(TAG, "opps");
                            }
                          }
                        }
                      })
                      .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                          if (adapter != null && cardList != null && cardList.size() <= 0 && tvAssistance != null) {
                            tvAssistance.setVisibility(View.VISIBLE);
                          }
                          tetdcv.clearData();
                          dialog.dismiss();
                        }
                      })
                      .show();
                }
              });
              v2API.prescriptions prescriptionService = retrofit.create(v2API.prescriptions.class);
              Call<List<Prescription>> prescriptionCall = prescriptionService.getPrescriptions("1", null, null, consultationId, null, null, null, null);
              final ArrayList<String> finalKeywordArrayList = keywordArrayList;
              prescriptionCall.enqueue(new Callback<List<Prescription>>() {
                @Override
                public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                  if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
                    List<Prescription> prescriptions = response.body();
//                    ArrayList<Card> cards = new ArrayList<>();
                    for (Prescription p: prescriptions) {
                      p.setMedicationName(idVsMedicationHM.get(p.getMedicationId()));
                      Card card;
                      if (p.getMedicationName() == null || p.getMedicationName().isEmpty())
                        card = new Card("Medication ID: " + p.getMedicationId(), p.getDetail());
                      else
                        card = new Card(p.getMedicationName(), p.getDetail());
                      cardList.add(card);
                    }
                    adapter = new FragRecyclerViewAdapter(getContext(), false, finalKeywordArrayList, title);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                  } else {
                    onFailure(call, new Throwable("sth wrong"));
                  }
                }

                @Override
                public void onFailure(Call<List<Prescription>> call, Throwable t) {
                  t.printStackTrace();
                  adapter = new FragRecyclerViewAdapter(getContext(), false, finalKeywordArrayList, title);
                  recyclerView.setAdapter(adapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                }
              });
            } else {
              onFailure(call, new Throwable("sth wrong when fetching medication as keywords"));
            }
          }

          @Override
          public void onFailure(Call<List<Medication>> call, Throwable t) {
            t.printStackTrace();
            v2API.prescriptions prescriptionService = retrofit.create(v2API.prescriptions.class);
            Call<List<Prescription>> prescriptionCall = prescriptionService.getPrescriptions("1", null, null, consultationId, null, null, null, null);
            prescriptionCall.enqueue(new Callback<List<Prescription>>() {
              @Override
              public void onResponse(Call<List<Prescription>> call, Response<List<Prescription>> response) {
                if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
                  List<Prescription> prescriptions = response.body();
//                  ArrayList<Card> cards = new ArrayList<>();
                  for (Prescription p: prescriptions) {
                    Card card = new Card("ID: " + p.getConsultationId(), p.getDetail());
                    cardList.add(card);
                  }
                  adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
                  recyclerView.setAdapter(adapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                } else {
                  onFailure(call, new Throwable("sth wrong"));
                }
              }

              @Override
              public void onFailure(Call<List<Prescription>> call, Throwable t) {
                t.printStackTrace();
                adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
              }
            });
          }
        });
      } else {
        v2API.keywords keywordService = retrofit.create(v2API.keywords.class);
        Call<List<Keyword>> keywordsCall;
        switch (category) {
          case 1:
            keywordsCall = keywordService.getKeywords("1", null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 2:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 3:
            keywordsCall = keywordService.getKeywords("1", null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 4:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 5:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 7:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case 8:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, true, null, null, null);
            break;
          default:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }
        if (keywordsCall != null) {
          keywordsCall.enqueue(new Callback<List<Keyword>>() {
            @Override
            public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
              if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
                ArrayList<String> keywordArrayList = new ArrayList<>();
                for (Keyword k : response.body()) {
                  keywordArrayList.add(k.getKeyword());
                }
                final TwoEditTextDialogCustomView tetdcv = new TwoEditTextDialogCustomView(getContext(), keywordArrayList, title, null, null, false);
                fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    new MaterialDialog.Builder(getContext())
                        .title("Add")
                        .customView(tetdcv, true)
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (adapter != null) {
                              if (tvAssistance != null) {
                                tvAssistance.setVisibility(View.GONE);
                              }
                              ArrayList<String> data = tetdcv.getData();
                              tetdcv.clearData();
                              Log.d(TAG, data.toString());
                              if (data.get(0) != null && data.get(1) != null) {
                                cardList.add(new Card(data.get(0), data.get(1)));
                                if (adapter != null)
                                  adapter.notifyDataSetChanged();
                                else
                                  Log.d(TAG, "opps");
                              }
                            }
                          }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (adapter != null && cardList != null && cardList.size() <= 0 && tvAssistance != null) {
                              tvAssistance.setVisibility(View.VISIBLE);
                            }
                            tetdcv.clearData();
                            dialog.dismiss();
                          }
                        })
                        .show();
                  }
                });
                v2API.related_data relatedDataService = retrofit.create(v2API.related_data.class);
                Call<List<RelatedData>> relatedDataCall;
                relatedDataCall = relatedDataService.getRelatedDataPlural("1", consultationId, category, null, null, null, null, null);
                final ArrayList<String> finalKeywordArrayList = keywordArrayList;
                relatedDataCall.enqueue(new Callback<List<RelatedData>>() {
                  @Override
                  public void onResponse(Call<List<RelatedData>> call, Response<List<RelatedData>> response) {
                    if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
//                      ArrayList<Card> cards = new ArrayList<>();
                      for (RelatedData rd: response.body()) {
                        cardList.add(new Card(rd.getData(), rd.getRemark()));
                      }
                      adapter = new FragRecyclerViewAdapter(getContext(), false, finalKeywordArrayList, title);
                      recyclerView.setAdapter(adapter);
                      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    } else {
                      onFailure(call, new Throwable("sth wrong"));
                    }
                  }

                  @Override
                  public void onFailure(Call<List<RelatedData>> call, Throwable t) {
                    t.printStackTrace();
                    adapter = new FragRecyclerViewAdapter(getContext(), false, finalKeywordArrayList, title);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                  }
                });
              } else {
                onFailure(call, new Throwable("sth wrong when fetching keywords"));
              }
            }

            @Override
            public void onFailure(Call<List<Keyword>> call, Throwable t) {
              t.printStackTrace();
              v2API.related_data relatedDataService = retrofit.create(v2API.related_data.class);
              Call<List<RelatedData>> relatedDataCall = null;
              relatedDataCall = relatedDataService.getRelatedDataPlural("1", consultationId, category, null, null, null, null, null);
              relatedDataCall.enqueue(new Callback<List<RelatedData>>() {
                @Override
                public void onResponse(Call<List<RelatedData>> call, Response<List<RelatedData>> response) {
                  if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null) {
                    //TODO no keywords to fill, just the related data then
//                    ArrayList<Card> cards = new ArrayList<>();
                    for (RelatedData rd: response.body()) {
                      cardList.add(new Card(rd.getData(), rd.getRemark()));
                    }
                    adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                  } else {
                    onFailure(call, new Throwable("sth wrong"));
                  }
                }

                @Override
                public void onFailure(Call<List<RelatedData>> call, Throwable t) {
                  t.printStackTrace();
                  adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
                  recyclerView.setAdapter(adapter);
                  recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                }
              });
            }
          });
        } else {    //TODO keywordcall is empty, which should never happen (because switch + default)

        }
      }

    } else if (preFillItems != null && preFillItems.length > 0) {                                   //TODO create empty default cards
      Log.d(TAG, "create empty default cards");
      fab.setVisibility(View.GONE);
      ArrayList<Card> preFillCards = new ArrayList<>();
      for (String s : preFillItems) {
        cardList.add(new Card(s, ""));
      }
      tvAssistance.setVisibility(View.GONE);
      adapter = new FragRecyclerViewAdapter(getContext(), true, null, title);
      recyclerView.setAdapter(adapter);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    } else if (category > 0) {                                                                      //TODO create a blank page w/ keywords
      if (category == 6) {
        inMedicationPage = true;
        v2API.medications medicationService = retrofit.create(v2API.medications.class);
        Call<List<Medication>> medicationsCall = medicationService.getMedications("1", null, null, null, null, null, null);
        medicationsCall.enqueue(new Callback<List<Medication>>() {
          @Override
          public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
            if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
              Log.d(TAG, response.body().toString());
              ArrayList<String> keywordArrayList = new ArrayList<>();
              for (Medication m:response.body()) {
                medicationVsIdHM.put(m.getMedication(), m.getMedicationId());
                idVsMedicationHM.put(m.getMedicationId(), m.getMedication());
                keywordArrayList.add(m.getMedication());
              }
              final TwoEditTextDialogCustomView tetdcv = new TwoEditTextDialogCustomView(getContext(), keywordArrayList, title, null, null, false, shortcutsArray);
              fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  new MaterialDialog.Builder(getContext())
                      .title("Add")
                      .customView(tetdcv, true)
                      .positiveText("Confirm")
                      .negativeText("Cancel")
                      .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                          if (adapter != null) {
                            if (tvAssistance != null) {
                              tvAssistance.setVisibility(View.GONE);
                            }
                            ArrayList<String> data = tetdcv.getData();
                            tetdcv.clearData();
                            Log.d(TAG, data.toString());
                            if (data.get(0) != null && data.get(1) != null) {
                              cardList.add(new Card(data.get(0), data.get(1)));
                              if (adapter != null)
                                adapter.notifyDataSetChanged();
                              else
                                Log.d(TAG, "opps");
                            }
                          }
                        }
                      })
                      .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                          if (adapter != null && cardList != null && cardList.size() <= 0 && tvAssistance != null) {
                            tvAssistance.setVisibility(View.VISIBLE);
                          }
                          tetdcv.clearData();
                          dialog.dismiss();
                        }
                      })
                      .show();
                }
              });
              adapter = new FragRecyclerViewAdapter(getContext(), false, keywordArrayList, title);
              recyclerView.setAdapter(adapter);
              recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            } else {
              onFailure(call, new Throwable("sth wrong when getting list of medications"));
            }
          }

          @Override
          public void onFailure(Call<List<Medication>> call, Throwable t) {
            t.printStackTrace();                //no keywords, just type
            adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
          }
        });
      } else {
        v2API.keywords keywordService = retrofit.create(v2API.keywords.class);
        Call<List<Keyword>> keywordsCall;
        switch (category) {
          case Const.RelatedDataCategory.SCREENING:
            keywordsCall = keywordService.getKeywords("1", null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case Const.RelatedDataCategory.ALLERGY:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case Const.RelatedDataCategory.DIAGNOSIS:
            keywordsCall = keywordService.getKeywords("1", null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case Const.RelatedDataCategory.ADVICE:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case Const.RelatedDataCategory.FOLLOW_UP:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null, null, null);
            break;
          case Const.RelatedDataCategory.EDUCATION:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, true, null, null, null, null, null, null, null, null, null, null, null);
            break;
          default:
            keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }
        if (keywordsCall != null) {
          keywordsCall.enqueue(new Callback<List<Keyword>>() {
            @Override
            public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
              if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().size() > 0) {
                ArrayList<String> keywordArrayList = new ArrayList<>();
                for (Keyword k : response.body()) {
                  keywordArrayList.add(k.getKeyword());
                }
                adapter = new FragRecyclerViewAdapter(getContext(), false, keywordArrayList, title);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                final TwoEditTextDialogCustomView tetdcv = new TwoEditTextDialogCustomView(getContext(), keywordArrayList, title, null, null, false);
                fab.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    new MaterialDialog.Builder(getContext())
                        .title("Add")
                        .customView(tetdcv, true)
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (adapter != null) {
                              if (tvAssistance != null) {
                                tvAssistance.setVisibility(View.GONE);
                              }
                              ArrayList<String> data = tetdcv.getData();
                              tetdcv.clearData();
                              Log.d(TAG, data.toString());
                              if (data.get(0) != null && data.get(1) != null) {
                                cardList.add(new Card(data.get(0), data.get(1)));
                                if (adapter != null)
                                  adapter.notifyDataSetChanged();
                                else
                                  Log.d(TAG, "opps");
                              }
                            }
                          }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            if (adapter != null && cardList != null && cardList.size() <= 0 && tvAssistance != null) {
                              tvAssistance.setVisibility(View.VISIBLE);
                            }
                            tetdcv.clearData();
                            dialog.dismiss();
                          }
                        })
                        .show();
                  }
                });
              } else {
                onFailure(call, new Throwable("sth wrong when fetching keywords"));
              }
            }

            @Override
            public void onFailure(Call<List<Keyword>> call, Throwable t) {
              t.printStackTrace();
              adapter = new FragRecyclerViewAdapter(getContext(), false, null, title);
              recyclerView.setAdapter(adapter);
              recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            }
          });
        } else {    //TODO keywordcall is empty, which should never happen (because switch + default)

        }
      }
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

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  @Override
  public Serializable onSendData() {
    if (adapter != null) {
      if (inMedicationPage) {         //translate medication name into id
        ArrayList<Card> newMedicationList = new ArrayList<>();
        ArrayList<Card> newCardList = new ArrayList<>();
        for (Card c:cardList) {
          String mId = medicationVsIdHM.get(c.getCardTitle());
          Card newCard;
          if (mId != null) {
            newCardList.add(new Card(mId, c.getCardDescription()));
          } else {
            newMedicationList.add(new Card(c.getCardTitle(), c.getCardDescription()));
          }
        }
        return new ListOfCards(newCardList, newMedicationList);
      } else{
        return new ListOfCards(cardList);
      }
    }
    return null;
  }

  private class FragRecyclerViewAdapter extends RecyclerView.Adapter<FragRecyclerViewHolder> {
    Context context;
    boolean displaySwitch = false;
    String title;
    ArrayList<String> suggestions;

    /**
     * Create a new FragRecyclerViewAdapter
     * @param c                   is the context, nothing special
     * @param displaySwitch       is true if you want to display switches on each individual card. It will also A) disable fab and B)disable edit on title
     * @param listOfKeywords      i.e. a list of suggestions for auto complete
     * @param title               i.e. the title for the dialog
     */
    public FragRecyclerViewAdapter(Context c, boolean displaySwitch, @Nullable ArrayList<String> listOfKeywords, @Nullable String title) {
      this.context = c;
      this.displaySwitch = displaySwitch;
      this.suggestions = listOfKeywords;
      this.title = title;
    }


    @Override
    public FragRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new FragRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card_visit, parent, false));
    }

    @Override
    public void onBindViewHolder(final FragRecyclerViewHolder holder, int position) {
      if (cardList != null){
        if (cardList.size() > 0) {
          final FragRecyclerViewHolder viewHolder = holder;
          viewHolder.cardTitle.setText(cardList.get(position).getCardTitle());
          viewHolder.cardDescription.setText(cardList.get(position).getCardDescription());
          if (displaySwitch) {
            viewHolder.setEditableTitle(false);
            viewHolder.cardSwitch.setVisibility(View.VISIBLE);
            viewHolder.tvEmptyIndicator.setVisibility(View.VISIBLE);
            viewHolder.cardSwitch.setChecked(cardList.get(position).isChecked());
            viewHolder.cardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                  viewHolder.cardDescription.setVisibility(View.GONE);
                  cardList.get(holder.getAdapterPosition()).setChecked(false);
                } else {
                  viewHolder.cardDescription.setVisibility(View.VISIBLE);
                  cardList.get(holder.getAdapterPosition()).setChecked(true);
                }
              }
            });
          }

          final int pos = holder.getAdapterPosition();
          if (pos >= 0 && pos < cardList.size()) {
            final TwoEditTextDialogCustomView twoEditTextDialogCustomView = new TwoEditTextDialogCustomView(getContext(), suggestions, title, cardList.get(pos).getCardTitle(), cardList.get(pos).getCardDescription(), displaySwitch);
            holder.thisWholeCard.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                MaterialDialog.Builder b = new MaterialDialog.Builder(getContext())
                        .customView(twoEditTextDialogCustomView, true)
                        .title("Modify")
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ArrayList<String> data = twoEditTextDialogCustomView.getData();
                            twoEditTextDialogCustomView.clearData();
                            if (data != null && cardList != null && adapter != null) {
                              Log.d("qqq141", data.toString());
                              cardList.set(pos, new Card(data.get(0), data.get(1)));      //TODO crash @ PE
                              adapter.notifyDataSetChanged();
                            } else {
                              Toast.makeText(getContext(), "Empty data. Please seek tech support", Toast.LENGTH_LONG).show();
                            }
                          }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                          @Override
                          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            twoEditTextDialogCustomView.clearData();
                            dialog.dismiss();
                          }
                        });
                b.show();
              }
            });
          }
        } else
          Log.d(TAG, "empty cardList");
      } else
        Log.d(TAG, "no cardList");
    }

    @Override
    public int getItemCount() {
      if (cardList != null){
        return cardList.size();
      } else
        return 0;
    }
  }

  private class FragRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView cardTitle;
    public TextView cardDescription;
    public SwitchCompat cardSwitch;
    public TextView tvEmptyIndicator;
    public boolean editableTitle = true;
    public View thisWholeCard;


    public FragRecyclerViewHolder(View itemView) {
      super(itemView);
      cardTitle = (TextView) itemView.findViewById(R.id.item_name);
      cardDescription = (TextView) itemView.findViewById(R.id.item_description);
      cardSwitch = (SwitchCompat) itemView.findViewById(R.id.scNull);
      tvEmptyIndicator = (TextView) itemView.findViewById(R.id.tv_empty_indicator);
      thisWholeCard = itemView.findViewById(R.id.cardVisitEdit);
    }

    @Override
    public void onClick(View v) {

    }

    public void setEditableTitle(boolean editableTitle) {
      this.editableTitle = editableTitle;
    }
  }
}

//checked for deprecated command lines
