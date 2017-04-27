package io.github.hkust1516csefyp43.easymed.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.MedicationVariant;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.DividerItemDecoration;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MedicationVariantListFragment extends Fragment {
  private static final String TAG = MedicationVariantListFragment.class.getSimpleName();

  private OnFragmentInteractionListener mListener;
  private static String key = Const.BundleKey.WHICH_MV_PAGE;

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView recyclerView;
  private DynamicBox box;

  private int whichPage;
  private int counter = 0;

  private List<MedicationVariant> medicationVariants;
  private HashSet<String> medicationIdHashSet = new HashSet<>();
  private HashMap<String, String> medicationNames = new HashMap();

  public static MedicationVariantListFragment newInstance(int whichPage) {
    MedicationVariantListFragment fragment = new MedicationVariantListFragment();
    Bundle args = new Bundle();
    args.putInt(key, whichPage);
    fragment.setArguments(args);
    return fragment;
  }

  public MedicationVariantListFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      whichPage = getArguments().getInt(key);
    }
    Log.d(TAG, "which page = " + whichPage);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //whichPage are 0(out of stock), 1(inadequate) or 2(enough)
    //recyclerview, call api, fill the list, swipe refresh, fab >> new activity
    View view = inflater.inflate(R.layout.fragment_medication_variant_list, container, false);
    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl);
    swipeRefreshLayout.setVisibility(View.GONE);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    box = new DynamicBox(getContext(), recyclerView);
    box.showLoadingLayout();
    refresh(view);
    return view;
  }

  private void refresh(View view) {
    if (view != null) {
      if (recyclerView != null) {
        final Context context = getContext();
        if (context != null)
          recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        String suitcaseId = Cache.CurrentUser.getClinic(getContext()).getSuitcaseId();
        Log.d(TAG, "Suitcase id: " + suitcaseId);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            Log.d(TAG, "mvlf: " + message);
          }
        });
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
        ohc1.readTimeout(1, TimeUnit.MINUTES);
        ohc1.connectTimeout(1, TimeUnit.MINUTES);
        ohc1.addInterceptor(logging);
        Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(Const.Database.getCurrentAPI())
            .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
            .client(ohc1.build())
            .build();
        v2API.medication_variants medicationVariantService = retrofit.create(v2API.medication_variants.class);
        Call<List<MedicationVariant>> medicationCall = medicationVariantService.getMedicationVariants("1", whichPage, suitcaseId);
        medicationCall.enqueue(new Callback<List<MedicationVariant>>() {
          @Override
          public void onResponse(Call<List<MedicationVariant>> call, Response<List<MedicationVariant>> response) {
            if (response != null) {
              if (response.body() != null) {
                if (response.body().size() > 0) {
                  Log.d(TAG, response.body().toString());
                  medicationVariants = response.body();
                  Log.d(TAG, "before: " + medicationVariants.size());
                  for (MedicationVariant mv:medicationVariants) {
                    medicationIdHashSet.add(mv.getMedicationId());
                  }
                  Log.d(TAG, "after: " + medicationIdHashSet.size());
                  Iterator<String> iterator = medicationIdHashSet.iterator();
                  while (iterator.hasNext()) {
                    final String thisOne = iterator.next();
                    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
                    ohc1.readTimeout(1, TimeUnit.MINUTES);
                    ohc1.connectTimeout(1, TimeUnit.MINUTES);
                    Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl(Const.Database.getCurrentAPI())
                        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                        .client(ohc1.build())
                        .build();
                    v2API.medications medicationService = retrofit.create(v2API.medications.class);
                    Call<Medication> medicationCall1 = medicationService.getMedication("1", thisOne);
                    medicationCall1.enqueue(new Callback<Medication>() {
                      @Override
                      public void onResponse(Call<Medication> call, Response<Medication> response) {
                        if (response != null) {
                          if (response.body() != null) {
                            if (response.body().getMedication() != null) {
                              medicationNames.put(thisOne, response.body().getMedication());
                              counter++;
                              Log.d(TAG, "counter: " + counter + "; mhs: " + medicationIdHashSet.size());
                              if (counter >= medicationIdHashSet.size())
                                showUI();
                            } else {
                              onFailure(null, new Throwable("Medication have no name -_-"));
                            }
                          } else {
                            onFailure(null, new Throwable("Empty body"));
                          }
                        } else {
                          onFailure(null, new Throwable("No response"));
                        }
                      }

                      @Override
                      public void onFailure(Call<Medication> call, Throwable t) {
                        medicationNames.put(thisOne, "ID: " + thisOne);
                        counter++;
                        Log.d(TAG, "counter: " + counter + "; mhs: " + medicationIdHashSet.size());
                        if (counter >= medicationIdHashSet.size())
                          showUI();
                      }
                    });
                  }
                } else {
                  //TODO show no item ui (not failure, just nothing)
                  showEmptyUI();
                }
              } else {
                onFailure(call, new Throwable("empty response body"));
              }
            } else {
              onFailure(call, new Throwable("Empty response"));
            }
          }

          @Override
          public void onFailure(Call<List<MedicationVariant>> call, Throwable t) {
            t.printStackTrace();
            showError();
          }
        });

      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    refresh(getView());
  }

  private void showError() {
    if (box != null) {
      box.showExceptionLayout();
    }
  }

  private void showEmptyUI() {
    //TODO custom view
    if (box != null) {
      box.showExceptionLayout();
    }
  }

  private void showUI() {
    if (recyclerView != null && swipeRefreshLayout != null) {
      recyclerView.setAdapter(new medicationVariantRecyclerViewAdapter());
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      if (box != null) {
        box.hideAll();
      }
      swipeRefreshLayout.setRefreshing(false);
      swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
          onResume();
        }
      });
//      Context context = getContext();
//      if (context != null) {  //if context is null just skip the drag scroll bar /shrug
//        dragScrollBar = new DragScrollBar(context, recyclerView, true);
//        dragScrollBar.addIndicator(new AlphabetIndicator(getContext()), true);
//      }
      swipeRefreshLayout.setVisibility(View.VISIBLE);
    }
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

  public class medicationVariantItemViewHolder extends RecyclerView.ViewHolder {
    TextView biggest, bigger, big;

    public medicationVariantItemViewHolder(View itemView) {
      super(itemView);
      biggest = (TextView) itemView.findViewById(R.id.biggest_tv);
      bigger = (TextView) itemView.findViewById(R.id.bigger_tv);
      big = (TextView) itemView.findViewById(R.id.big_tv);
    }

  }

  public class medicationVariantRecyclerViewAdapter extends RecyclerView.Adapter<medicationVariantItemViewHolder>{

    public medicationVariantRecyclerViewAdapter() {
      Log.d(TAG, "creating adapter");
    }

    @Override
    public medicationVariantItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new medicationVariantItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false));
    }

    @Override
    public void onBindViewHolder(final medicationVariantItemViewHolder holder, int position) {
      Log.d(TAG, "position: " + position);
      if (medicationVariants != null) {
        if (position < medicationVariants.size()) {
          final MedicationVariant medicationVariant = medicationVariants.get(holder.getAdapterPosition());
          if (medicationVariant != null) {
            holder.biggest.setText(medicationNames.get(medicationVariant.getMedicationId()));
            StringBuilder stringBuilder = new StringBuilder();
            if (medicationVariant.getName() != null) {
              stringBuilder.append(medicationVariant.getName());
            }
            if (medicationVariant.getStrength() != null) {
              if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(", ");
              }
              stringBuilder.append(medicationVariant.getStrength());
            }
            if (medicationVariant.getForm() != null) {
              if (!stringBuilder.toString().equals("")) {
                stringBuilder.append(", ");
              }
              stringBuilder.append(medicationVariant.getForm());
            }
            holder.bigger.setText(stringBuilder.toString());
            if (medicationVariant.getRemark() == null || medicationVariant.getRemark().length() == 0) {
              holder.big.setVisibility(View.GONE);
            } else {
              holder.big.setText(medicationVariant.getRemark());
            }
          }
        }
      }
    }

    @Override
    public int getItemCount() {
      if (medicationVariants != null)
        return medicationVariants.size();
      else
        return 0;
    }
  }

}

//checked for deprecated command lines
