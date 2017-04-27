package io.github.hkust1516csefyp43.easymed.view.fragment.station;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.db.chart.Tools;
import com.db.chart.listener.OnEntryClickListener;
import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Count;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.ExcelGenerator;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.activity.GenerateNewReportActivity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReportsFragment extends Fragment {
  private static final String TAG = ReportsFragment.class.getSimpleName();
  private OnFragmentInteractionListener mListener;
  ProgressBar pbLineChartView;

//  public static ReportsFragment newInstance() {
//    ReportsFragment fragment = new ReportsFragment();
//    return fragment;
//  }

  public ReportsFragment() {
    // Required empty public constructor
  }

//  @Override
//  public void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//    if (getArguments() != null) {
//
//    }
//  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_reports, container, false);
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.setTitle("Reports");
    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    if (drawer != null) {
      drawer.addDrawerListener(toggle); //change from setDrawerListener
      toggle.syncState();
    }

    FloatingActionButton fabOne = (FloatingActionButton) view.findViewById(R.id.fabOne);
    fabOne.setIconDrawable(new IconicsDrawable(getContext()).color(Color.WHITE).actionBar().icon(CommunityMaterial.Icon.cmd_calendar));
    fabOne.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ExcelGenerator.createWorkbook("Test3.xls");
      }
    });

    FloatingActionButton fabRange = (FloatingActionButton) view.findViewById(R.id.fabRange);
    fabRange.setIconDrawable(new IconicsDrawable(getContext()).color(Color.WHITE).actionBar().icon(CommunityMaterial.Icon.cmd_calendar_multiple));

    pbLineChartView = (ProgressBar) view.findViewById(R.id.pbLineChardView);
    monthlyVisitsGraphGenerator((LineChartView) view.findViewById(R.id.linechart));
//    LineChartView lineChartView = monthlyVisitsGraphGenerator((LineChartView) view.findViewById(R.id.linechart));
//    if (lineChartView != null && pbLineChartView != null) {
//      pbLineChartView.setVisibility(View.GONE);
//      lineChartView.show();
//    } //TODO else some error message?
    return view;
  }

  /**
   * TODO Call API, animations, etc
   * @param lineChartView
   * @return
   */
  private void monthlyVisitsGraphGenerator(final LineChartView lineChartView) {
    final HashMap<Integer, Point> pointHashMap = new HashMap<>();
    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(1, TimeUnit.MINUTES);
    ohc1.connectTimeout(1, TimeUnit.MINUTES);
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();
    final v2API.visits visitService = retrofit.create(v2API.visits.class);
    GregorianCalendar today = new GregorianCalendar();
    int year = today.get(Calendar.YEAR);
    int month = today.get(Calendar.MONTH);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL yy", Locale.US);
    int callsQueue = 6;
    while (callsQueue > 0) {
      final String text = simpleDateFormat.format(new LocalDate(year, month, 1)); //change from new Date to new LocalDate
      final int myPosition = callsQueue;
      Call<Count> countCall = visitService.getVisitCount("1", Util.getMonthEndDateStringWithTimeZone(year, month), Util.getMonthStartDateStringWithTimeZone(year, month));
      callsQueue--;
      month--;
      if (month < 0) {
        month = 11;
        year--;
      }
      countCall.enqueue(new Callback<Count>() {
        @Override
        public void onResponse(Call<Count> call, Response<Count> response) {
          if (response != null) {
            if (response.code() < 200 || response.code() >= 300) {
              onFailure(call, new Throwable(response.toString()));
            } else {
              Point point = new Point(text, (float) response.body().getCount());
              Log.d(TAG, "point: " + point.toString());
              pointHashMap.put(myPosition, point);
              if (pointHashMap.size() >= 6) {
                Log.d(TAG, "coming soon: " + pointHashMap.toString());
                continuePlotting(lineChartView, pointHashMap);
              }
            }
          } else {
            onFailure(call, new Throwable("Something's wrong (Error code: 100000)"));
          }
        }

        @Override
        public void onFailure(Call<Count> call, Throwable t) {
          t.printStackTrace();
          //TODO dialog
        }
      });
    }
  }

  private void continuePlotting(LineChartView lineChartView, HashMap<Integer, Point> pointHashMap) {
    if (lineChartView != null) {
      Log.d(TAG, "coming soon2: " + pointHashMap.toString());
      lineChartView.setClickable(true);
      lineChartView.setClickablePointRadius(10);
//      lineChartView.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          Log.d(TAG, "ocl");
//        }
//      });
      lineChartView.setOnEntryClickListener(new OnEntryClickListener() {
        @Override
        public void onClick(int setIndex, int entryIndex, Rect rect) {
          Log.d(TAG, "oecl");
        }
      });
      final LineSet lineSet = new LineSet();
      float max = 0;
      for (int i = 1; i <= 6; i++) {
        if (pointHashMap.get(i).getValue() > max)
          max = pointHashMap.get(i).getValue();
        Log.d(TAG, "coming soon 3: " + i + '/' + pointHashMap.get(i));
        lineSet.addPoint(pointHashMap.get(i));
      }
      lineSet.setColor(Color.parseColor("#758cbb"))
          .setDotsColor(Color.parseColor("#758cbb"))
          .setThickness(1)
          .setSmooth(true)
          .beginAt(0);
      lineChartView.addData(lineSet);
      lineChartView.setBorderSpacing(Tools.fromDpToPx(15))
          .setAxisBorderValues(0, Math.round(max))
          .setYLabels(AxisController.LabelPosition.NONE)
          .setLabelsColor(Color.parseColor("#6a84c3"))
          .setXAxis(true)
          .setYAxis(true);
      lineChartView.setVisibility(View.VISIBLE);
      pbLineChartView.setVisibility(View.GONE);
      lineChartView.show();
    }

  }

  private void openGenerateNewReport() {
    Intent intent = new Intent(getContext(), GenerateNewReportActivity.class);
    startActivity(intent);
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