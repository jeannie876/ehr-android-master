package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.BloodType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Gender;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BioFragment extends Fragment {
  public static final String TAG = BioFragment.class.getSimpleName();
  private Patient patient;
  private static String key = Const.BundleKey.READ_ONLY_PATIENT;

  private LinearLayout llPatientInfo;

  public static BioFragment newInstance(Patient patient) {
    BioFragment fragment = new BioFragment();
    Bundle args = new Bundle();
    args.putSerializable(key, patient);
    fragment.setArguments(args);
    return fragment;
  }

  public BioFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      Serializable o = getArguments().getSerializable(key);
      if (o instanceof Patient) {
        patient = (Patient) o;
      }
    }

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bio, container, false);
    llPatientInfo = (LinearLayout) view.findViewById(R.id.llPatientInfo);
    if (llPatientInfo != null) {
      if (patient != null) {
        final Context context = getContext();
        if (context != null) {
          TextView tvBioTitle = new TextView(context);
          tvBioTitle.setText("Basic Information");
          tvBioTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
          tvBioTitle.setTextColor(Color.BLACK);
          tvBioTitle.setTypeface(null, Typeface.BOLD);
          llPatientInfo.addView(tvBioTitle);

          if (patient.getPatientId() != null){
            TextView tvBioPatientId = new TextView(context);
            tvBioPatientId.setText("Patient ID: " + patient.getPatientId());
            llPatientInfo.addView(tvBioPatientId);
          }

          if (patient.getGenderId() != null){
            Log.d(TAG, "Gender ID: " + patient.getGenderId());
            OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
            ohc1.readTimeout(1, TimeUnit.MINUTES);
            ohc1.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Const.Database.getCurrentAPI())
                .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                .client(ohc1.build())
                .build();

            v2API.genders gendersService = retrofit.create(v2API.genders.class);
            Call<Gender> genderCall = gendersService.getGender("1", patient.getGenderId());
            genderCall.enqueue(new Callback<Gender>() {
              @Override
              public void onResponse(Call<Gender> call, Response<Gender> response) {
                if (response.body() != null){
                  Log.d(TAG, "Gender response code: " + response.code());
                  Log.d(TAG, "Gender request: " + response.body());

                  TextView tvBioGender = new TextView(context);
                  tvBioGender.setText("Gender: " + response.body().getGender());
                  llPatientInfo.addView(tvBioGender);
                } else {
                  Log.d(TAG, "Sth wrong");
                  Log.d(TAG, "Gender response code: " + response.code());
                  Log.d(TAG, "Gender request: " + response.body());
                }
              }

              @Override
              public void onFailure(Call<Gender> call, Throwable t) {

              }
            });
          }

          if (patient.getBirthDate() != null) {
            TextView tvBioBirthDate = new TextView(context);
            tvBioBirthDate.setText("Birthday: " + patient.getBirthYear() + "/" + (patient.getBirthMonth() + 1) + "/" + patient.getBirthDate());
            llPatientInfo.addView(tvBioBirthDate);
          }

          if (patient.getBloodTypeId() != null) {

            OkHttpClient.Builder ohc2 = new OkHttpClient.Builder();
            ohc2.readTimeout(1, TimeUnit.MINUTES);
            ohc2.connectTimeout(1, TimeUnit.MINUTES);
            Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(Const.Database.getCurrentAPI())
                .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                .client(ohc2.build())
                .build();

            v2API.bloodTypes bloodTypesService = retrofit.create(v2API.bloodTypes.class);
            Call<BloodType> bloodTypeCall = bloodTypesService.getBloodType("1", patient.getBloodTypeId());
            bloodTypeCall.enqueue(new Callback<BloodType>() {
              @Override
              public void onResponse(Call<BloodType> call, Response<BloodType> response) {
                if (response.body() != null){
                  TextView tvBioBloodType = new TextView(context);
                  tvBioBloodType.setText("Blood type: " + response.body().getType());
                  llPatientInfo.addView(tvBioBloodType);
                }
              }

              @Override
              public void onFailure(Call<BloodType> call, Throwable t) {
                t.printStackTrace();
              }
            });
          }

          if (patient.getPhoneNumberCountryCode() != null) {
            TextView tvBioPhoneNumberCountryCode = new TextView(context);
            tvBioPhoneNumberCountryCode.setText("Phone number country code: " + patient.getPhoneNumberCountryCode());
            llPatientInfo.addView(tvBioPhoneNumberCountryCode);
          }

          if (patient.getPhoneNumber() != null) {
            TextView tvPhoneNumber = new TextView(context);
            tvPhoneNumber.setText("Phone number: " + patient.getPhoneNumber());
            llPatientInfo.addView(tvPhoneNumber);
          }

          if (patient.getAddress() != null) {
            TextView tvBioAddress = new TextView(context);
            tvBioAddress.setText("Address: " + patient.getAddress());
            llPatientInfo.addView(tvBioAddress);
          }

          if (patient.getEmail() != null) {
            TextView tvBioEmail = new TextView(context);
            tvBioEmail.setText("Email: " + patient.getEmail());
            llPatientInfo.addView(tvBioEmail);
          }

          if (patient.getCreateTimeStamp() != null) {
            TextView tvBioCreateTimeStamp = new TextView(context);
            tvBioCreateTimeStamp.setText("First created: " + patient.getCreateTimeStamp());
            llPatientInfo.addView(tvBioCreateTimeStamp);
          }

          //TODO buttons to read document


        }
      } else {
        //TODO some error message?
      }
    }//else there is nothing you can do
    return view;
  }

}

//checked for deprecated command lines
