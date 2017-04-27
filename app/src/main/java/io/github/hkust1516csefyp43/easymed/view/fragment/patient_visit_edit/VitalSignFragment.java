package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.VitalSigns;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Triage;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.Util;

public class VitalSignFragment extends Fragment implements OnSendData{
  private static final String TAG = VitalSignFragment.class.getSimpleName();

  private TextView tvLDD;
  private TextView tvBMI;
  private EditText etSystolic;
  private EditText etDiastolic;
  private EditText etPulseRate;
  private EditText etRespiratoryRate;
  private EditText etTemperature;
  private TextView tvTemperatureUnit;
  private EditText etBloodSugar;
  private EditText etSpo2;
  private EditText etHeadCircumference;
  private EditText etWeight;
  private TextView tvWeightUnit;
  private EditText etHeight;

  private double dWeight;
  private double dHeight;
  private int[] lddDate = new int[3];
  private Date outputLDD;
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

  private OnFragmentInteractionListener mListener;

  private Triage thisTriage = null;

  private boolean tempIsCelsius = true;
  private boolean weightIsKg = true;

  private ColorStateList defaultTextColor;

  public VitalSignFragment() {
    // Required empty public constructor
  }

  public static VitalSignFragment newInstance(Triage triage) {
    VitalSignFragment fragment = new VitalSignFragment();
    Bundle args = new Bundle();
    args.putSerializable(Const.BundleKey.WHOLE_TRIAGE, triage);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      Serializable serializable = getArguments().getSerializable(Const.BundleKey.WHOLE_TRIAGE);
      if (serializable instanceof Triage) {
        thisTriage = (Triage) serializable;
      }
    }
  }

  /**
   * TODO if thisTriage != null inflate the UI
   * @param inflater
   * @param container
   * @param savedInstanceState
   * @return
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_vital_sign, container, false);
    tvLDD = (TextView) view.findViewById(R.id.ldd);
    etSystolic = (EditText) view.findViewById(R.id.etSystolic);
    etDiastolic = (EditText) view.findViewById(R.id.etDiastolic);
    etPulseRate = (EditText) view.findViewById(R.id.etPulseRate);
    etRespiratoryRate = (EditText) view.findViewById(R.id.etRespiratoryRate);
    etTemperature = (EditText) view.findViewById(R.id.etTemperature);
    tvTemperatureUnit = (TextView) view.findViewById(R.id.tv_temperature_unit);
    etBloodSugar = (EditText) view.findViewById(R.id.etBloodSugar);
    etSpo2 = (EditText) view.findViewById(R.id.etSpo2);
    etHeadCircumference = (EditText) view.findViewById(R.id.etHeadCircumference);
    etWeight = (EditText) view.findViewById(R.id.etWeight);
    tvWeightUnit = (TextView) view.findViewById(R.id.tv_weight_unit);
    etHeight = (EditText) view.findViewById(R.id.etHeight);
    tvBMI = (TextView) view.findViewById(R.id.tvBMI);

    if (thisTriage != null){
      if (tvLDD != null){
        if (thisTriage.getLastDewormingTabletDate() != null){
          String text = dateFormat.format(thisTriage.getLastDewormingTabletDate());
          tvLDD.setText(text);
          Log.d(TAG, text);
        }
      }
      if (etSystolic != null) {
        if (thisTriage.getSystolic() != null) {
          etSystolic.setText(String.valueOf(thisTriage.getSystolic()));
        }
      }
      if (etDiastolic != null) {
        if (thisTriage.getDiastolic() != null) {
          etDiastolic.setText(String.valueOf(thisTriage.getDiastolic()));
        }
      }
      if (etWeight != null) {
        if (thisTriage.getWeight() != null) {
          etWeight.setText(String.valueOf(thisTriage.getWeight()));
        }
      }
      if (etHeight != null) {
        if (thisTriage.getHeight() != null) {
          etHeight.setText(String.valueOf(thisTriage.getHeight()));
        }
      }
      if (etPulseRate != null) {
        if (thisTriage.getHeartRate() != null) {
          etPulseRate.setText(String.valueOf(thisTriage.getHeartRate()));
        }
      }
      if (etRespiratoryRate != null) {
        if (thisTriage.getRespiratoryRate() != null) {
          etRespiratoryRate.setText(String.valueOf(thisTriage.getRespiratoryRate()));
        }
      }
      if (etTemperature != null) {
        if (thisTriage.getWeight() != null) {
          etTemperature.setText(String.valueOf(thisTriage.getTemperature()));
        }
      }
      if (etSpo2 != null) {
        if (thisTriage.getSpo2() != null) {
          etSpo2.setText(String.valueOf(thisTriage.getSpo2()));
        }
      }
      if (etHeadCircumference != null) {
        if (thisTriage.getHeadCircumference() != null) {
          etHeadCircumference.setText(String.valueOf(thisTriage.getHeadCircumference()));
        }
      }
      if (etBloodSugar != null) {
        if (thisTriage.getBloodSugar() != null) {
          etBloodSugar.setText(String.valueOf(thisTriage.getBloodSugar()));
        }
      }
    }
//      inflaterEverything();

    tvWeightUnit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (weightIsKg) {
          tvWeightUnit.setText(R.string.lb);
          weightIsKg = false;
          if (etWeight != null && !etWeight.getText().toString().equals("")) {
            etWeight.setText(String.valueOf(Util.roundDouble(Util.kgTolb(Double.parseDouble(etWeight.getText().toString())), 2)));
          }
        } else {
          tvWeightUnit.setText(R.string.kg);
          weightIsKg = true;
          if (etWeight != null && !etWeight.getText().toString().equals("")) {
            etWeight.setText(String.valueOf(Util.roundDouble(Util.lbToKg(Double.parseDouble(etWeight.getText().toString())), 2)));
          }
        }
      }
    });

    defaultTextColor = etSystolic.getTextColors();
    etSystolic.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          int systolic = Integer.parseInt(s.toString());
          if (systolic > 140 || systolic < 80) {
            etSystolic.setTextColor(Color.RED);
          } else {
            etSystolic.setTextColor(defaultTextColor);
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    });

    etDiastolic.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          int diastolic = Integer.parseInt(s.toString());
          if (diastolic > 90 || diastolic < 60) {
            etDiastolic.setTextColor(Color.RED);
          } else {
            etDiastolic.setTextColor(defaultTextColor);
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    });

    etBloodSugar.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          double bs = Double.parseDouble(s.toString());
          if (bs > 200 || bs < 70) {
            etBloodSugar.setTextColor(Color.RED);
          } else {
            etBloodSugar.setTextColor(defaultTextColor);
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    });

    etTemperature.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          double bs = Double.parseDouble(s.toString());
          if (!tempIsCelsius) {
            bs = Util.fahrenheitToCelsius(bs);
          }
          if (bs > 38 || bs < 36) {
            etTemperature.setTextColor(Color.RED);
          } else {
            etTemperature.setTextColor(defaultTextColor);
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
        }
      }
    });

    tvTemperatureUnit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (tempIsCelsius) {
          tvTemperatureUnit.setText(R.string.degreeF);
          tempIsCelsius = false;
          if (etTemperature != null && !etTemperature.getText().toString().equals("")) {
            etTemperature.setText(String.valueOf(Util.roundDouble(Util.celsiusToFahrenheit(Double.parseDouble(etTemperature.getText().toString())), 2)));
          }
        } else {
          tvTemperatureUnit.setText(R.string.degreeC);
          tempIsCelsius = true;
          if (etTemperature != null && !etTemperature.getText().toString().equals("")) {
            etTemperature.setText(String.valueOf(Util.roundDouble(Util.fahrenheitToCelsius(Double.parseDouble(etTemperature.getText().toString())), 2)));
          }
        }
      }
    });

    tvBMI.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (tvBMI != null) {
          try {
            double bmi = Double.parseDouble(s.toString());
            if (bmi < 18.5)
              tvBMI.setTextColor(Color.GREEN);
            else if (bmi < 25)
              tvBMI.setTextColor(Color.BLUE);
            else if (bmi < 30)
              tvBMI.setTextColor(Color.YELLOW);
            else
              tvBMI.setTextColor(Color.RED);
          } catch (NumberFormatException e) {
            e.printStackTrace();
            //e.g. "?", "Please input sth"
            tvBMI.setTextColor(getResources().getColor(R.color.primary_text_color));
          }
        }
      }
    });
    etWeight.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        Log.d(TAG, s.toString());
        try {
          dWeight = Double.parseDouble(s.toString());
          if (tvBMI != null)
            tvBMI.setText(BMICalculator(dWeight, dHeight));
        } catch (NumberFormatException e) {
          dWeight = 0;
          if (tvBMI != null) {
            tvBMI.setText("?");
          }
          e.printStackTrace();
        }
      }
    });
    etHeight.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        try {
          dHeight = Double.parseDouble(s.toString());
          if (tvBMI != null)
            tvBMI.setText(BMICalculator(dWeight, dHeight));
        } catch (NumberFormatException e) {
          dHeight = 0;
          if (tvBMI != null) {
            tvBMI.setText("?");
          }
          e.printStackTrace();
        }
      }
    });
    //TODO weight & height listener: calculate BMI
    tvLDD.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        GregorianCalendar gc = new GregorianCalendar();
        DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
          @Override
          public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
            if (tvLDD != null) {
              //TODO before today check
              String date = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
              lddDate[0] = year;
              lddDate[1] = monthOfYear;
              lddDate[2] = dayOfMonth;
              tvLDD.setText(date);
              try {
                outputLDD = dateFormat.parse(date);
              } catch (ParseException e) {
                e.printStackTrace();
              }
            }
          }
        }, gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH));
        dpd.show(getActivity().getFragmentManager(), TAG);
      }
    });

    Button removeButton = (Button) view.findViewById(R.id.removeButton2);
    removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        lddDate[0] = 0;
        lddDate[1] = 0;
        lddDate[2] = 0;
        tvLDD.setText("Click to select date");
      }
    });
    return view;
  }

  private String BMICalculator(double w, double h) {
    if (w <= 0 || h <= 0)
      return "?";
    double h2 = h / 100;
    h2 = h2 * h2;
    h2 = w / h2;
    BigDecimal bd = new BigDecimal(h2);
    bd = bd.round(new MathContext(3));
    h2 = bd.doubleValue();
    return roundNumber(h2, 2);
  }
  private String roundNumber(double num, int length) {
    String format = "#";
    if (length > 0)
      format += ".";
    for (int i = 0; i < length; i++) {
      format += "#";
    }
    DecimalFormat df = new DecimalFormat(format);
    df.setRoundingMode(RoundingMode.CEILING);
    return df.format(num);
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

    VitalSigns vs = new VitalSigns();
    if (etSystolic != null && etSystolic.getText() != null && etSystolic.getText().length() != 0) {
      try {
        vs.setSystolic(Integer.parseInt(etSystolic.getText().toString()));
      } catch (NumberFormatException e) {
        etSystolic.setError("This is not a number");
        return new Throwable("Systolic is not a number");
      }
    }
    if (etDiastolic != null && etDiastolic.getText() != null && etDiastolic.getText().length() != 0) {
      try {
        vs.setDiastolic(Integer.parseInt(etDiastolic.getText().toString()));
      } catch (NumberFormatException e) {
        etDiastolic.setError("This is not a number");
        return new Throwable("Diastolic is not a number");
      }
    }
    if (etPulseRate != null && etPulseRate.getText() != null && etPulseRate.getText().length() != 0) {
      try {
        vs.setPulseRate(Integer.parseInt(etPulseRate.getText().toString()));
      } catch (NumberFormatException e) {
        etPulseRate.setError("This is not a number");
        return new Throwable("Pulse Rate is not a number");
      }
    }
    if (etRespiratoryRate != null && etRespiratoryRate.getText() != null && etRespiratoryRate.getText().length() != 0) {
      try {
        vs.setRespiratoryRate(Integer.parseInt(etRespiratoryRate.getText().toString()));
      } catch (NumberFormatException e) {
        etRespiratoryRate.setError("This is not a number");
        return new Throwable("Respiratory Rate is not a number");
      }
    }
    if (etSpo2 != null && etSpo2.getText() != null && etSpo2.getText().length() != 0) {
      try {
        vs.setSpo2(Integer.parseInt(etSpo2.getText().toString()));
      } catch (NumberFormatException e) {
        etSpo2.setError("This is not a number");
        return new Throwable("SpO2 is not a number");
      }
    }
    if (etTemperature != null && etTemperature.getText() != null && etTemperature.getText().length() != 0) {
      try {
        double temp = Double.parseDouble(etTemperature.getText().toString());
        if (!tempIsCelsius) {
          temp = Util.fahrenheitToCelsius(temp);
        }
        vs.setTemperature(temp);
      } catch (NumberFormatException e) {
        etTemperature.setError("This is not a number");
        return new Throwable("Temperature is not a number");
      }
    }
    if (etWeight != null && etWeight.getText() != null && etWeight.getText().length() != 0) {
      try {
        double temp = Double.parseDouble(etWeight.getText().toString());
        if (!weightIsKg) {
          temp = Util.lbToKg(temp);
        }
        vs.setWeight(temp);
      } catch (NumberFormatException e) {
        etWeight.setError("This is not a number");
        return new Throwable("Weight is not a number");
      }
    }
    if (etHeight != null && etHeight.getText() != null && etHeight.getText().length() != 0) {
      try {
        vs.setHeight(Double.parseDouble(etHeight.getText().toString()));
      } catch (NumberFormatException e) {
        etHeight.setError("This is not a number");
        return new Throwable("Height is not a number");
      }
    }
    if (etBloodSugar != null && etBloodSugar.getText() != null && etBloodSugar.getText().length() != 0) {
      try {
        vs.setBloodSugar(Double.parseDouble(etBloodSugar.getText().toString()));
      } catch (NumberFormatException e) {
        etBloodSugar.setError("This is not a number");
        return new Throwable("Blood sugar is not a number");
      }
    }
    if (etHeadCircumference != null && etHeadCircumference.getText() != null && etHeadCircumference.getText().length() != 0) {
      try {
        vs.setHeadCircumference(Double.parseDouble(etHeadCircumference.getText().toString()));
      } catch (NumberFormatException e) {
        etHeadCircumference.setError("This is not a number");
        return new Throwable("Head circumference is not a number");
      }
    }
    if (outputLDD != null) {
        vs.setLdd(outputLDD);
    }
    return vs;
  }
}

//checked for deprecated command lines