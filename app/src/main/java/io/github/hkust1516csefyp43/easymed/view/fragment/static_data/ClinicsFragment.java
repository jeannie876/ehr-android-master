package io.github.hkust1516csefyp43.easymed.view.fragment.static_data;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Clinic;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClinicsFragment extends Fragment {
  DynamicBox box;
  List<Clinic> clinicList;

  public ClinicsFragment() {
    // Required empty public constructor
  }
  public static ClinicsFragment newInstance() {
    ClinicsFragment fragment = new ClinicsFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_clinics, container, false);
    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    box = new DynamicBox(getContext(), recyclerView);
    box.showLoadingLayout();
    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(2, TimeUnit.MINUTES);
    ohc1.connectTimeout(2, TimeUnit.MINUTES);
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.currentAPI)
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();
    v2API.clinics clinicService = retrofit.create(v2API.clinics.class);
    Call<List<Clinic>> clinicsCall = clinicService.getClinics("1", null, null, null, null, null, null, null, null, null, null, null, "english_name", null);
    clinicsCall.enqueue(new Callback<List<Clinic>>() {
      @Override
      public void onResponse(Call<List<Clinic>> call, Response<List<Clinic>> response) {
        if (response == null) {
          onFailure(call, new Throwable("empty response"));
        } else if (response.code() >= 300 || response.code() < 200) {
          onFailure(call, new Throwable("Error from server: " + response.code()));
        } else if (response.body() == null) {
          onFailure(call, new Throwable("Empty response body"));
        } else if (response.body().isEmpty()) {
          onFailure(call, new Throwable("Empty list"));
        } else {
          //TODO
          clinicList = response.body();
          recyclerView.setAdapter(new clinicAdapter());
          recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          box.hideAll();
        }
      }

      @Override
      public void onFailure(Call<List<Clinic>> call, Throwable t) {
        t.printStackTrace();
        box.showExceptionLayout();
      }
    });
    return view;
  }

  private class clinicViewHolder extends RecyclerView.ViewHolder {
    LinearLayout llGlobalBadge;
    TextView ivGlobalIcon;
    TextView tvClinicNames;
    TextView tvCountry;

    public clinicViewHolder(View itemView) {
      super(itemView);
      llGlobalBadge = (LinearLayout) itemView.findViewById(R.id.ll_global_badge);
      ivGlobalIcon = (TextView) itemView.findViewById(R.id.iv_globe);
      tvClinicNames = (TextView) itemView.findViewById(R.id.tv_clinic_names);
      tvCountry = (TextView) itemView.findViewById(R.id.tv_country);
    }
  }

  private class clinicAdapter extends RecyclerView.Adapter<clinicViewHolder> {

    public clinicAdapter() {
    }

    @Override
    public clinicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new clinicViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clinic, parent, false));
    }

    @Override
    public void onBindViewHolder(clinicViewHolder holder, int position) {
      if (clinicList != null && position < clinicList.size()) {
        Clinic thisClinic = clinicList.get(position);
        if (!thisClinic.getGlobal()) {
          holder.llGlobalBadge.setVisibility(View.GONE);
        } else {
          holder.ivGlobalIcon.setText(new String(Character.toChars(0x1F30F)));
        }
        String clinicName = thisClinic.getEnglishName();
        if (thisClinic.getNativeName() != null) {
          clinicName = clinicName + "(" + thisClinic.getNativeName() + ")";
        }
        holder.tvClinicNames.setText(clinicName);
        //TODO translate id to string
        String subtitle = thisClinic.getCountryId();
        holder.tvCountry.setVisibility(View.GONE);
      }
    }

    @Override
    public int getItemCount() {
      if (clinicList == null)
        return 0;
      else
        return clinicList.size();
    }
  }

}

//checked for deprecated command lines
