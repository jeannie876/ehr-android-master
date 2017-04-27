package io.github.hkust1516csefyp43.easymed.view.fragment.static_data;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.BloodType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Gender;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Suitcase;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.custom.TableRenderer;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaticDataFragment extends Fragment {
  private TableView tableView;
  private Integer whichStaticData;

  public static StaticDataFragment newInstance() {
    return new StaticDataFragment();
  }

  public static StaticDataFragment newInstance(Integer whichOne) {
    StaticDataFragment fragment = new StaticDataFragment();
    Bundle args = new Bundle();
    args.putInt(Const.BundleKey.WHICH_STATIC_DATA, whichOne);
    fragment.setArguments(args);
    return fragment;
  }

  public StaticDataFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      whichStaticData = getArguments().getInt(Const.BundleKey.WHICH_STATIC_DATA);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_static_data, container, false);
    tableView = (TableView) view.findViewById(R.id.tableView);
    if (tableView != null && whichStaticData != null) {
      final DynamicBox box = new DynamicBox(getActivity(), tableView);
      box.showLoadingLayout();

      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(1, TimeUnit.MINUTES);
      ohc1.connectTimeout(1, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      switch (whichStaticData) {
        case Const.StaticPages.BLOOD_TYPES:
          v2API.bloodTypes bloodTypeServices = retrofit.create(v2API.bloodTypes.class);
          Call<List<BloodType>> bloodTypesCall = bloodTypeServices.getBloodTypes("1", null);
          bloodTypesCall.enqueue(new Callback<List<BloodType>>() {
            @Override
            public void onResponse(Call<List<BloodType>> call, Response<List<BloodType>> response) {
              if (response.code() < 200 || response.code() >= 300) {
                onFailure(call, new Throwable(response.toString()));
              } else {
                tableView.setColumnCount(1);    //because only display blood type
                SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getContext(), "Blood Type");
//                simpleTableHeaderAdapter.setTextColor(get.getResources().getColor(R.color.table_header_text));
                tableView.setHeaderAdapter(simpleTableHeaderAdapter);
                tableView.setDataAdapter(new BloodTypeDataAdapter(getContext(), response.body()));
                box.hideAll();
              }
            }

            @Override
            public void onFailure(Call<List<BloodType>> call, Throwable t) {
              t.printStackTrace();
              box.showExceptionLayout();
            }
          });
          break;
        case Const.StaticPages.GENDERS:
          v2API.genders genderService = retrofit.create(v2API.genders.class);
          Call<List<Gender>> gendersCall = genderService.getGenders("1");
          gendersCall.enqueue(new Callback<List<Gender>>() {
            @Override
            public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
              if (response.code() < 200 || response.code() >= 300) {
                onFailure(call, new Throwable(response.toString()));
              } else {
                tableView.setColumnCount(2);
                SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getContext(), "Gender", "Biologically speaking");
                tableView.setHeaderAdapter(simpleTableHeaderAdapter);
                tableView.setDataAdapter(new GenderDataAdapter(getContext(), response.body()));
                box.hideAll();
              }
            }

            @Override
            public void onFailure(Call<List<Gender>> call, Throwable t) {
              t.printStackTrace();
              box.showExceptionLayout();
            }
          });
          break;
        //TODO keywords, clinics & countries are too complicated for this table view?
//        case Const.StaticPages.KEYWORDS:
//          break;
//        case Const.StaticPages.CLINICS:
//          break;
//        case Const.StaticPages.COUNTRIES:
//          break;
        case Const.StaticPages.SUITCASES:
          v2API.suitcases suitcaseService = retrofit.create(v2API.suitcases.class);
          Call<List<Suitcase>> suitcasesCall = suitcaseService.getSuitcases("1");
          suitcasesCall.enqueue(new Callback<List<Suitcase>>() {
            @Override
            public void onResponse(Call<List<Suitcase>> call, Response<List<Suitcase>> response) {
              if (response.code() < 200 || response.code() >= 300) {
                onFailure(call, new Throwable(response.toString()));
              } else {
                tableView.setColumnCount(1);
                SimpleTableHeaderAdapter simpleTableHeaderAdapter = new SimpleTableHeaderAdapter(getContext(), "Name");
                tableView.setHeaderAdapter(simpleTableHeaderAdapter);
                tableView.setDataAdapter(new SuitcaseDataAdapter(getContext(), response.body()));
                box.hideAll();
              }
            }

            @Override
            public void onFailure(Call<List<Suitcase>> call, Throwable t) {
              t.printStackTrace();
              box.showExceptionLayout();
            }
          });
          break;
        default:
          break;
      }
    }
    return view;
  }

  private class BloodTypeDataAdapter extends TableDataAdapter<BloodType> {

    public BloodTypeDataAdapter(Context context, List<BloodType> data) {
      super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
      BloodType bloodType = getRowData(rowIndex);
      View renderedView;
      switch (columnIndex) {
        case 0:
          renderedView = TableRenderer.renderString(bloodType.getType(), getContext());
          break;
        default:
          renderedView = TableRenderer.renderString("?", getContext());
      }
      return renderedView;
    }
  }

  private class GenderDataAdapter extends TableDataAdapter<Gender> {

    public GenderDataAdapter(Context context, List<Gender> data) {
      super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
      Gender gender = getRowData(rowIndex);
      View renderedView;
      switch (columnIndex) {
        case 0:
          renderedView = TableRenderer.renderString(gender.getGender(), getContext());
          break;
        case 1:
          renderedView = TableRenderer.renderString(gender.getBiologicalGender(), getContext());
          break;
        default:
          renderedView = TableRenderer.renderString("?", getContext());
          break;
      }
      return renderedView;
    }
  }

  private class SuitcaseDataAdapter extends TableDataAdapter<Suitcase> {

    public SuitcaseDataAdapter(Context context, List<Suitcase> data) {
      super(context, data);
    }

    @Override
    public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
      Suitcase suitcase = getRowData(rowIndex);
      View renderedView;
      switch (columnIndex) {
        case 0:
          renderedView = TableRenderer.renderString(suitcase.getName(), getContext());
          break;
        default:
          renderedView = TableRenderer.renderString("?", getContext());
      }
      return renderedView;
    }
  }

}

//checked for deprecated command lines
