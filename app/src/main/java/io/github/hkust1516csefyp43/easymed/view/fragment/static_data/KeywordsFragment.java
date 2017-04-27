package io.github.hkust1516csefyp43.easymed.view.fragment.static_data;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Keyword;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class KeywordsFragment extends Fragment {
  private static final String TAG = KeywordsFragment.class.getSimpleName();
  List<Keyword> keywordList;

  DynamicBox box;
  RecyclerView recyclerView;
  FloatingActionButton floatingActionButton;

  public KeywordsFragment() {
    Log.d(TAG, "empty constructor");
  }

  public static KeywordsFragment newInstance() {
    KeywordsFragment fragment = new KeywordsFragment();
    Log.d(TAG, "new instance");
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    Log.d(TAG, "ocv");
    View view =  inflater.inflate(R.layout.fragment_keywords, container, false);
    recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabAdd);
    floatingActionButton.setVisibility(View.GONE);
    floatingActionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //TODO add keyword
      }
    });
    box = new DynamicBox(getContext(), recyclerView);
    box.showLoadingLayout();
    Log.d(TAG, "loading");
    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(2, TimeUnit.MINUTES);
    ohc1.connectTimeout(2, TimeUnit.MINUTES);
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.currentAPI)
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();
    v2API.keywords keywordService = retrofit.create(v2API.keywords.class);
    Call<List<Keyword>> keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, "keyword");
    keywordsCall.enqueue(new Callback<List<Keyword>>() {
      @Override
      public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {
        Log.d(TAG, "got sth");
        if (response == null) {
          onFailure(call, new Throwable("Empty response!?"));
        } else if (response.code() >= 300 || response.code() < 200) {
          onFailure(call, new Throwable("Error from server: " + response.code()));
        } else if (response.body() == null){
          onFailure(call, new Throwable("Empty response body"));
        } else {
          if(response.body().size() <= 0) {
            //TODO empty >> show sth
            Log.d(TAG, "nothing to show");
            box.showExceptionLayout();
          } else {
            //not empty
            keywordList = response.body();
            Log.d(TAG, "sth to show: " + keywordList.toString());
            recyclerView.setAdapter(new keywordsAdapter());
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            floatingActionButton.setVisibility(View.VISIBLE);
            box.hideAll();
          }
        }
      }

      @Override
      public void onFailure(Call<List<Keyword>> call, Throwable t) {
        t.printStackTrace();
        box.showExceptionLayout();
      }
    });
    return view;
  }

  private class keywordViewHolder extends RecyclerView.ViewHolder {
    TextView keyword, description;

    public keywordViewHolder(View itemView) {
      super(itemView);
      keyword = (TextView) itemView.findViewById(R.id.biggest_tv);
      description = (TextView) itemView.findViewById(R.id.bigger_tv);
    }
  }

  private class keywordsAdapter extends RecyclerView.Adapter<keywordViewHolder> {

    public keywordsAdapter() {

    }

    @Override
    public keywordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new keywordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keyword, parent, false));
    }

    @Override
    public void onBindViewHolder(keywordViewHolder holder, int position) {
      if (keywordList != null && position < keywordList.size()) {
        Keyword keyword = keywordList.get(holder.getAdapterPosition());
        if (keyword != null) {
          holder.keyword.setText(keyword.getKeyword());
          //put all categories into 1 line
          String description = null;
          if (keyword.getAdvice()) {
            description = "Advice";
          }
          if (keyword.getAllergen()) {
            if (description == null || description.isEmpty()) {
              description = "Allergen";
            } else {
              description = description + " / " + "Allergen";
            }
          }

          if (keyword.getChiefComplain()) {
            if (description == null || description.isEmpty()) {
              description = "Chief Complain";
            } else {
              description = description + " / " + "Chief Complain";
            }
          }

          if (keyword.getDiagnosis()) {
            if (description == null || description.isEmpty()) {
              description = "Diagnosis";
            } else {
              description = description + " / " + "Diagnosis";
            }
          }

          if (keyword.getEducation()) {
            if (description == null || description.isEmpty()) {
              description = "Education";
            } else {
              description = description + " / " + "Education";
            }
          }

          if (keyword.getFollowUp()) {
            if (description == null || description.isEmpty()) {
              description = "Follow up";
            } else {
              description = description + " / " + "Follow up";
            }
          }

          if (keyword.getGeneral()) {
            if (description == null || description.isEmpty()) {
              description = "General";
            } else {
              description = description + " / " + "General";
            }
          }

          if (keyword.getInvestigation()) {
            if (description == null || description.isEmpty()) {
              description = "Investigation";
            } else {
              description = description + " / " + "Investigation";
            }
          }

          if (keyword.getMedicationForm()) {
            if (description == null || description.isEmpty()) {
              description = "Medication Form";
            } else {
              description = description + " / " + "Medication Form";
            }
          }

          if (keyword.getMedicationFrequency()) {
            if (description == null || description.isEmpty()) {
              description = "Medication Frequency";
            } else {
              description = description + " / " + "Medication Frequency";
            }
          }

          if (keyword.getMedicationRoute()) {
            if (description == null || description.isEmpty()) {
              description = "Medication Route";
            } else {
              description = description + " / " + "Medication Route";
            }
          }

          if (keyword.getRelationshipTypes()) {
            if (description == null || description.isEmpty()) {
              description = "Relationship Type";
            } else {
              description = description + " / " + "Relationship Type";
            }
          }

          if (keyword.getScreening()) {
            if (description == null || description.isEmpty()) {
              description = "Screening";
            } else {
              description = description + " / " + "Screening";
            }
          }

          if (keyword.getUnit()) {
            if (description == null || description.isEmpty()) {
              description = "Unit";
            } else {
              description = description + " / " + "Unit";
            }
          }
          if (description == null || description.isEmpty()) {
            description = "No categories";
          }

          holder.description.setText(description);
        }
      }
    }

    @Override
    public int getItemCount() {
      if (keywordList != null)
        return keywordList.size();
      else
        return 0;
    }
  }

}

//checked for deprecated command lines