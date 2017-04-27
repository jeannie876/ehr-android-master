package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.utility.Const;

public class ChiefComplaintFragment extends Fragment implements OnSendData{
  private OnFragmentInteractionListener mListener;
  private AppCompatMultiAutoCompleteTextView acmactv;

  private String chiefComplaintData;

  public ChiefComplaintFragment() {
    // Required empty public constructor
  }

  public static ChiefComplaintFragment newInstance(String data) {
    ChiefComplaintFragment fragment = new ChiefComplaintFragment();
    Bundle args = new Bundle();
    args.putString(Const.BundleKey.CHIEF_COMPLAINT_DATA, data);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      chiefComplaintData = getArguments().getString(Const.BundleKey.CHIEF_COMPLAINT_DATA, null);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_chief_complaint, container, false);
    acmactv = (AppCompatMultiAutoCompleteTextView) view.findViewById(R.id.chief_complaint);
    if (chiefComplaintData != null && acmactv != null) {
      acmactv.setText(chiefComplaintData);
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
  public Serializable onSendData() {
    String chiefComplaint = "";
    if (acmactv != null) {
      chiefComplaint = acmactv.getText().toString();
    }
    return chiefComplaint;
  }
}
//checked for deprecated command lines