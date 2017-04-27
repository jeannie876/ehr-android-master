package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.Pregnancy;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Consultation;
import io.github.hkust1516csefyp43.easymed.utility.Const;


public class PregnancyFragment extends Fragment implements OnSendData{
  private static final String TAG = PregnancyFragment.class.getSimpleName();
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  private TextView LMPDate;
  private CheckBox isPregnant;
  private EditText gestation;
  private LinearLayout ifPregnant;
  private EditText howMany;
  private CheckBox isBreastFeeding;
  private CheckBox isContraceptive;
  private EditText noPregnancy;
  private EditText noLiveBirth;
  private EditText noMiscarriage;
  private EditText noAbortion;
  private EditText noStillBirth;
  private EditText otherInfo;

  private int[] pregDate = new int[3];
  private Date outputLMP;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

  private Consultation thisConsultation = null;

  private OnFragmentInteractionListener mListener;

  public PregnancyFragment() {
    // Required empty public constructor
  }

  public static PregnancyFragment newInstance(Consultation consultation) {
    PregnancyFragment fragment = new PregnancyFragment();
    Bundle args = new Bundle();
    args.putSerializable(Const.BundleKey.WHOLE_CONSULTATION, consultation);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      Serializable serializable = getArguments().getSerializable(Const.BundleKey.WHOLE_CONSULTATION);
      if (serializable instanceof Consultation) {
        thisConsultation = (Consultation) serializable;
      }
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_pregnancy, container, false);
    LMPDate = (TextView) view.findViewById(R.id.tvLmpdate);
    isPregnant = (CheckBox) view.findViewById(R.id.cbIspregnant);
    gestation = (EditText) view.findViewById(R.id.etGestation);
    ifPregnant = (LinearLayout) view.findViewById(R.id.llIfPregnant);
    howMany = (EditText) view.findViewById(R.id.etHowMany);
    isBreastFeeding = (CheckBox) view.findViewById(R.id.cbBreastfeeding);
    isContraceptive = (CheckBox) view.findViewById(R.id.cbContraceptive);
    noPregnancy = (EditText) view.findViewById(R.id.etNoPreg);
    noLiveBirth = (EditText) view.findViewById(R.id.etNoLivebirth);
    noMiscarriage = (EditText) view.findViewById(R.id.etNoMiscarriage);
    noAbortion = (EditText) view.findViewById(R.id.etNoAbortion);
    noStillBirth = (EditText) view.findViewById(R.id.etNoStillbirth);
    otherInfo = (EditText) view.findViewById(R.id.etPregRemark);

    if (thisConsultation != null) {
      if (LMPDate != null) {
        if (thisConsultation.getPregLmp() != null) {
          String text = dateFormat.format(thisConsultation.getPregLmp());
          LMPDate.setText(text);
          Log.d(TAG, text);
        }
      }
      if (isPregnant != null) {
        if (thisConsultation.getPregCurrPreg() != null) {
          isPregnant.setChecked(thisConsultation.getPregCurrPreg());
        }
      }
      if (thisConsultation.getPregGestation() != null) {
        gestation.setText(String.valueOf(thisConsultation.getPregGestation()));
      }
      if (isBreastFeeding != null) {
        if (thisConsultation.getPregBreastFeeding() != null) {
          isBreastFeeding.setChecked(thisConsultation.getPregBreastFeeding());
        }
      }
      if (isContraceptive != null) {
        if (thisConsultation.getPregContraceptive() != null) {
          isContraceptive.setChecked(thisConsultation.getPregContraceptive());
        }
      }
      if (noPregnancy != null) {
        if (thisConsultation.getPregNumPreg() != null) {
          noPregnancy.setText(String.valueOf(thisConsultation.getPregNumPreg()));
        }
      }
      if (noLiveBirth != null) {
        if (thisConsultation.getPregNumLiveBirth() != null) {
          noLiveBirth.setText(String.valueOf(thisConsultation.getPregNumLiveBirth()));
        }
      }
      if (noMiscarriage != null) {
        if (thisConsultation.getPregNumMiscarriage() != null) {
          noMiscarriage.setText(String.valueOf(thisConsultation.getPregNumMiscarriage()));
        }
      }
      if (noAbortion != null) {
        if (thisConsultation.getPregNumAbortion() != null) {
          noAbortion.setText(String.valueOf(thisConsultation.getPregNumAbortion()));
        }
      }
      if (noStillBirth != null) {
        if (thisConsultation.getPregNumStillBirth() != null) {
          noStillBirth.setText(String.valueOf(thisConsultation.getPregNumStillBirth()));
        }
      }
      if (otherInfo != null) {
        if (thisConsultation.getPregRemark() != null) {
          otherInfo.setText(thisConsultation.getPregRemark());
        }
      }
    }

    LMPDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        GregorianCalendar gc = new GregorianCalendar();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            if (LMPDate != null) {
              //TODO before today check
              String date = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
              pregDate[0] = year;
              pregDate[1] = monthOfYear;
              pregDate[2] = dayOfMonth;
              LMPDate.setText(date);
              try {
                outputLMP = dateFormat.parse(date);
              } catch (ParseException e) {
                e.printStackTrace();
              }
            }
          }
        }, gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
        dpd.show(getActivity().getFragmentManager(), TAG);
      }
    });

    Button removeButton = (Button) view.findViewById(R.id.removeButton3);
    removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        pregDate[0] = 0;
        pregDate[1] = 0;
        pregDate[2] = 0;
        LMPDate.setText("Click to select date");
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
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public Serializable onSendData() {
    Pregnancy p = new Pregnancy();
    if (outputLMP != null) {
      p.setLmdDate(outputLMP);
    }
    if (isBreastFeeding != null) {
      p.setBreastFeeding(isBreastFeeding.isChecked());
    }
    if (isContraceptive != null) {
      p.setContraceptiveUse(isContraceptive.isChecked());
    }
    if (isPregnant != null) {
      p.setCurrPreg(isPregnant.isChecked());
    }
    if (gestation != null && !gestation.getText().toString().isEmpty()) {
      try {
        p.setGestation(Integer.parseInt(gestation.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        gestation.setError("This is not a number");
        return new Throwable("No. of Gestation is not a number");
      }
    }
    if (noPregnancy != null && !noPregnancy.getText().toString().isEmpty()) {
      try {
        p.setNoOfPregnancy(Integer.parseInt(noPregnancy.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        noPregnancy.setError("This is not a number");
        return new Throwable("No. of Pregnancy is not a number");
      }
    }
    if (noLiveBirth != null && !noLiveBirth.getText().toString().isEmpty()) {
      try {
        p.setNoOfLiveBirth(Integer.parseInt(noLiveBirth.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        noLiveBirth.setError("This is not a number");
        return new Throwable("No. of Live Birth is not a number");
      }
    }
    if (noMiscarriage != null && !noMiscarriage.getText().toString().isEmpty()) {
      try {
        p.setNoOfMiscarriage(Integer.parseInt(noMiscarriage.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        noMiscarriage.setError("This is not a number");
        return new Throwable("No. of Miscarriage is not a number");
      }
    }
    if (noAbortion != null && !noAbortion.getText().toString().isEmpty()) {
      try {
        p.setNoOfAbortion(Integer.parseInt(noAbortion.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        noAbortion.setError("This is not a number");
        return new Throwable("No. of Abortion is not a number");
      }
    }
    if (noStillBirth != null && !noStillBirth.getText().toString().isEmpty()) {
      try {
        p.setNoOfStillBirth(Integer.parseInt(noStillBirth.getText().toString()));
      } catch (NumberFormatException e) {
        e.printStackTrace();
        noStillBirth.setError("This is not a number");
        return new Throwable("No. of Still Birth is not a number");
      }
    }
    if (otherInfo != null) {
      p.setOtherInformation(otherInfo.getText().toString());
    }
    return p;
  }
}

//checked for deprecated command lines
