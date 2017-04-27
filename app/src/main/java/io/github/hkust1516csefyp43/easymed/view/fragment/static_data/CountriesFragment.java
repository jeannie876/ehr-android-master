package io.github.hkust1516csefyp43.easymed.view.fragment.static_data;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.hkust1516csefyp43.easymed.R;

public class CountriesFragment extends Fragment {
  public CountriesFragment() {
    // Required empty public constructor
  }
  public static CountriesFragment newInstance(String param1, String param2) {
    CountriesFragment fragment = new CountriesFragment();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_clinics, container, false);
  }
}

//checked for deprecated command lines