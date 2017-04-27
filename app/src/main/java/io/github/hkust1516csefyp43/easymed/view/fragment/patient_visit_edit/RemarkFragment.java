package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.io.Serializable;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.listener.scrollToError;
import io.github.hkust1516csefyp43.easymed.utility.Const;

public class RemarkFragment extends Fragment implements OnSendData, scrollToError {
  private OnFragmentInteractionListener mListener;
  private SwitchCompat scRemark;
  private EditText etRemark;
  private TextInputLayout tilRemark;

  private View errorView = null;    //null >> no error

  private String remarkData;
  private boolean maybeEmpty;

  public RemarkFragment() {
    // Required empty public constructor
  }

  public static RemarkFragment newInstance(String data, boolean maybeEmpty) {
    RemarkFragment fragment= new RemarkFragment();
    Bundle args = new Bundle();
    args.putString(Const.BundleKey.REMARK_DATA, data);
    args.putBoolean(Const.BundleKey.MAYBE_EMPTY, maybeEmpty);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      remarkData = getArguments().getString(Const.BundleKey.REMARK_DATA, null);
      maybeEmpty = getArguments().getBoolean(Const.BundleKey.MAYBE_EMPTY, false);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_remark, container, false);
    scRemark = (SwitchCompat) view.findViewById(R.id.scRemark);
    etRemark = (EditText) view.findViewById(R.id.etRemark);
    tilRemark = (TextInputLayout) view.findViewById(R.id.tilRemark);

    if (maybeEmpty && remarkData == null) { //checked as false
      scRemark.setChecked(false);
      tilRemark.setVisibility(View.GONE);
    } else if (remarkData != null) {
      scRemark.setChecked(true);
      tilRemark.setVisibility(View.VISIBLE);
      etRemark.setText(remarkData);
    }
    scRemark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (tilRemark != null) {
          if (isChecked)
            tilRemark.setVisibility(View.VISIBLE);
          else
            tilRemark.setVisibility(View.GONE);
        }

      }
    });
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
    if (scRemark != null && etRemark != null) {
      if (scRemark.isChecked()) {
        if (etRemark.getText().length() <= 0) {
          etRemark.setError("Either turn me off or fill me up");
          return new Throwable("Switch checked but empty content");
        } else
         return etRemark.getText().toString();
      } else
        return null;
    }
    return null;
  }

  @Override
  public void scrollToError() {
    if (errorView != null) {
      etRemark.requestFocus();
    }
  }
}

//checked for deprecated command lines