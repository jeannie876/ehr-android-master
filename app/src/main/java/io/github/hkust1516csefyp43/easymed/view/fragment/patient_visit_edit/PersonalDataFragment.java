package io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_edit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnSendData;
import io.github.hkust1516csefyp43.easymed.pojo.patient_visit_edit.PersonalData;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Attachment;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Gender;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.ImageTransformer;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PersonalDataFragment extends Fragment implements OnSendData{
  public final static String TAG = PersonalDataFragment.class.getSimpleName();

  private static Patient patient;



  private ScrollView scrollView;
  private ImageView ivProfilePic;
  private EditText etTag;
  private EditText etFirstName;
  private EditText etMiddleName;
  private EditText etLastName;
  private EditText etNativeName;
  private EditText etAddress;
  //TODO phone country code spinner
  private EditText etPhoneNumber;
  private TextView etAgeYear;
  private TextView etAgeMonth;
  private TextView etAgeWeek;
  private TextView tvBirthday;
  private Spinner sGender;
  private Spinner sStatus;
  private String[] genderArray;
  private String[] statusArray;
  private int[] birthday = new int[3];      //0: year; 1: month(0-11); 2: date

  private boolean anyError = false;
  private int cantTouchThis = 0;
  private String profilePicBase64 = null;
  private String currentGenderId;

  public static PersonalDataFragment newInstance(Patient p) {
    PersonalDataFragment fragment = new PersonalDataFragment();
    patient = p;
    return fragment;
  }

  public PersonalDataFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_personal_data, container, false);

    inflateEveryBoxes(view);

    scrollView = (ScrollView) view.findViewById(R.id.scrollView);
    ImageView ivBirthdayRemove = (ImageView) view.findViewById(R.id.ivRemoveBirthday);
    ivBirthdayRemove.setImageDrawable(new IconicsDrawable(getContext()).actionBar().color(ResourcesCompat.getColor(getResources(), R.color.secondary_text_color, null)).icon(FontAwesome.Icon.faw_trash_o));
    ivBirthdayRemove.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        birthday[0] = 0;
        birthday[1] = 0;
        birthday[2] = 0;
        tvBirthday.setText("Click to select date");
      }
    });
    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  public void inflateEveryBoxes(View view) {
    ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
    etTag = (EditText) view.findViewById(R.id.etTag);
    etFirstName = (EditText) view.findViewById(R.id.first_name);
    etAgeYear = (EditText) view.findViewById(R.id.etYear);
    etAgeMonth = (EditText) view.findViewById(R.id.etMonth);
    etAgeWeek = (EditText) view.findViewById(R.id.etWeek);
    tvBirthday = (TextView) view.findViewById(R.id.tvBirthday);
    etMiddleName = (EditText) view.findViewById(R.id.middle_name);
    etLastName = (EditText) view.findViewById(R.id.last_name);
    etNativeName = (EditText) view.findViewById(R.id.native_name);
    etAddress = (EditText) view.findViewById(R.id.etAddress);
    etPhoneNumber = (EditText) view.findViewById(R.id.etPhoneNumber);

    if (ivProfilePic != null) {
      if (patient != null) {
        ivProfilePic.setImageDrawable(TextDrawable.builder().buildRect(Util.getTextDrawableText(patient), ColorGenerator.MATERIAL.getColor(Util.displayNameBuilder(patient.getLastName(), patient.getFirstName()))));        //act as placeholder + fallback
        if (patient.getImageId() != null && patient.getProfilePicBase64() == null) {
          OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
          ohc1.readTimeout(1, TimeUnit.MINUTES);
          ohc1.connectTimeout(1, TimeUnit.MINUTES);
          Retrofit retrofit = new Retrofit
              .Builder()
              .baseUrl(Const.Database.getCurrentAPI())
              .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
              .client(ohc1.build())
              .build();
          v2API.attachments attachmentService = retrofit.create(v2API.attachments.class);
          Call<Attachment> attachmentCall = attachmentService.getAttachment("1", patient.getImageId());
          attachmentCall.enqueue(new Callback<Attachment>() {
            @Override
            public void onResponse(Call<Attachment> call, Response<Attachment> response) {
              if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().getFileInBase64() != null) {
                byte[] decodedString = Base64.decode(response.body().getFileInBase64(), Base64.DEFAULT);
                Bitmap decodedByte = ImageTransformer.centerCrop(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
                ivProfilePic.setImageBitmap(decodedByte);
              }
            }

            @Override
            public void onFailure(Call<Attachment> call, Throwable t) {
              t.printStackTrace();
              ivProfilePic.setImageDrawable(TextDrawable.builder().buildRound(Util.getTextDrawableText(patient), ColorGenerator.MATERIAL.getColor(Util.displayNameBuilder(patient.getLastName(), patient.getFirstName()))));
              patient.setProfilePicBase64(Const.EMPTY_STRING);
            }
          });
        } else if (patient.getProfilePicBase64() != null && !patient.getProfilePicBase64().equals(Const.EMPTY_STRING)) {
          byte[] decodedString = Base64.decode(patient.getProfilePicBase64(), Base64.DEFAULT);
          Bitmap decodedByte = ImageTransformer.centerCrop(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
          ivProfilePic.setImageBitmap(decodedByte);
        }
      }
//      else if (patient != null) {   //// FIXME: 28/4/16 
//        ivProfilePic.setImageDrawable(TextDrawable.builder().buildRect(Util.getTextDrawableText(patient), ColorGenerator.MATERIAL.getColor(patient.getLastNameSpaceFirstName())));
//      }
      
      ivProfilePic.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          new MaterialDialog.Builder(getContext())
              .title("Patient picture")
              .items(R.array.image_array)
              .itemsCallback(new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                  switch (which) {
                    case Const.ACTION_TAKE_PICTURE:
                      openCamera();
                      break;
                    case Const.ACTION_SELECT_PICTURE:
                      pickImage();
                      break;
                    case Const.ACTION_REMOVE_PICTURE:
                      ivProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.easymed));
                      //save as default
                    default:
                  }
                }
              })
              .theme(Theme.LIGHT)
              .show();
        }
      });
    }

    if (patient != null && etTag != null) {
      if (patient.getTag() != null) {
        etTag.setText(patient.getTag().toString());
      }
    }

    if (patient != null && patient.getFirstName() != null) {
      etFirstName.setText(patient.getFirstName());
    }
    etFirstName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (s.toString().length() < 1) {
          anyError = true;
          etFirstName.setError("Patient must have a given name");
        } else {
          anyError = false;
        }
      }
    });


    if (patient != null && patient.getMiddleName() != null) {
      etMiddleName.setText(patient.getMiddleName());
    }

    if (patient != null && patient.getLastName() != null) {
      etLastName.setText(patient.getLastName());
    }

    if (patient != null && patient.getNativeName() != null) {
      etNativeName.setText(patient.getNativeName());
    }

    if (patient != null && patient.getAddress() != null) {
      etAddress.setText(patient.getAddress());
    }

    //TODO phone country code

    if (patient != null && patient.getPhoneNumber() != null){
      etPhoneNumber.setText(patient.getPhoneNumber());
    }

    ivProfilePic = (ImageView) view.findViewById(R.id.ivProfilePic);
    //TODO get attachment by id

    sGender = (Spinner) view.findViewById(R.id.sGender);
    final List<Gender> genders = Cache.DatabaseData.getGenders(getContext());
    if (genders != null) {
      genderArray = new String[genders.size()];
      final HashMap<String, String> genderHM = new HashMap<>(genders.size());
      final HashMap<String, String> rGenderHM = new HashMap<>(genders.size());
      for (int i = 0; i < genders.size(); i++) {
        genderArray[i] = genders.get(i).getGender();
        genderHM.put(genders.get(i).getGender(), genders.get(i).getId());
        rGenderHM.put(genders.get(i).getId(), genders.get(i).getGender());
      }

      ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, genderArray);
      sGender.setAdapter(adapter1);

      if (patient != null && patient.getGenderId() != null) {
        int index;
        Log.d(TAG, "Gender needs to be filled");
        for (int i = 0; i < genders.size(); i++){
          if (rGenderHM.get(patient.getGenderId()).equals(genderArray[i])){
            index = i;
            Log.d(TAG, "Found gender: " + index + rGenderHM.get(patient.getGenderId()));
            sGender.setSelection(index);
          }
        }
      }
//    if(patient != null && patient.getGenderId() != null){
//
//    }
      sGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          String a = (String) parent.getItemAtPosition(position);
          currentGenderId = genderHM.get(a);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
          currentGenderId = genders.get(0).getId();
        }
      });
    } else
    sGender.setVisibility(View.GONE);

    //TODO from keywords
    sStatus = (Spinner) view.findViewById(R.id.sStatus);
    statusArray = new String[]{
        "Single", "Married", "Divorced", "Widowed", "Custom"
    };
    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, statusArray);
    sStatus.setAdapter(adapter2);
    sStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });


    if (etAgeYear != null && etAgeMonth != null && etAgeWeek != null && tvBirthday != null) {
      TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
          try {
            if (cantTouchThis <= 0) {
              GregorianCalendar birthdayGC = Util.ageToBirthday(Integer.parseInt(etAgeYear.getText().toString()), Integer.parseInt(etAgeMonth.getText().toString()), Integer.parseInt(etAgeWeek.getText().toString()));
              birthday[0] = birthdayGC.get(Calendar.YEAR);
              birthday[1] = birthdayGC.get(Calendar.MONTH);
              birthday[2] = birthdayGC.get(Calendar.DAY_OF_MONTH);
              String date = "" + birthday[0] + "/" + (birthday[1] + 1) + "/" + birthday[2];
              tvBirthday.setText(date);
            } else
              cantTouchThis--;
          } catch (NumberFormatException e) {
            e.printStackTrace();
          }
        }
      };
      etAgeYear.addTextChangedListener(textWatcher);
      etAgeMonth.addTextChangedListener(textWatcher);
      etAgeWeek.addTextChangedListener(textWatcher);
    }
//
    birthday[0] = 1992;
    birthday[1] = 8;    //September
    birthday[2] = 14;

    if (tvBirthday != null) {
      tvBirthday.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          DatePickerDialog dpd = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
              if (tvBirthday != null) {
                String date = "" + year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                birthday[0] = year;
                birthday[1] = monthOfYear;
                birthday[2] = dayOfMonth;
                tvBirthday.setText(date);
                cantTouchThis = 3;
              }
              if (etAgeYear != null && etAgeMonth != null && etAgeWeek != null) {
                int[] ageArray = Util.birthdayToAgeYMW(birthday[0], birthday[1], birthday[2]);
                etAgeYear.setText(String.valueOf(ageArray[0]));
                etAgeMonth.setText(String.valueOf(ageArray[1]));
                etAgeWeek.setText(String.valueOf(ageArray[2]));
              }
            }
          }, birthday[0], birthday[1], birthday[2]);
          dpd.showYearPickerFirst(true);
          dpd.show(getActivity().getFragmentManager(), TAG);
        }
      });
    }

    if (patient != null && patient.getBirthYear() != null && patient.getBirthMonth() != null && patient.getBirthDate() != null) {
      //set tvBirthday
      birthday[0] = patient.getBirthYear();
      birthday[1] = patient.getBirthMonth();
      birthday[2] = patient.getBirthDate();
      String date = "" + birthday[0] + "/" + (birthday[1] + 1) + "/" + birthday[2];
      tvBirthday.setText(date);
      Log.d(TAG, date);
      //set etAge
      int[] ageArray = Util.birthdayToAgeYMW(birthday[0], birthday[1], birthday[2]);
      etAgeYear.setText(String.valueOf(ageArray[0]));
      etAgeMonth.setText(String.valueOf(ageArray[1]));
      etAgeWeek.setText(String.valueOf(ageArray[2]));
    } else {
      if (tvBirthday != null)
        tvBirthday.setText("Click to select date");
    }
  }

  private void openCamera() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
      startActivityForResult(takePictureIntent, 1024);
    }
  }

  public void pickImage() {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.setType("image/*");
    startActivityForResult(intent, 4021);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1024) {
      if (resultCode == Activity.RESULT_OK) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        if (imageBitmap != null && ivProfilePic != null) {
          //TODO imageBitmap is just a thumbnail >> low resolution >> ugly
          //http://developer.android.com/training/camera/photobasics.html
          ivProfilePic.setImageBitmap(ImageTransformer.centerCrop(imageBitmap));
          ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
          imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
          byte[] byteArray = byteArrayOutputStream .toByteArray();
          profilePicBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        } else {

        }
      } else {
        new MaterialDialog.Builder(getContext())
            .title("Error")
            .content("Cannot open the file. Please try again.")
            .positiveText("Dismiss")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
              }
            })
            .theme(Theme.LIGHT)
            .show();
      }
    } else if (requestCode == 4021) {
      if (resultCode == Activity.RESULT_OK) {
        if (data == null) {                         //Display an error
          new MaterialDialog.Builder(getContext())
              .title("Error")
              .content("Cannot get the data. Please try again")
              .positiveText("Dismiss")
              .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                  dialog.dismiss();
                }
              })
              .theme(Theme.LIGHT)
              .show();
          return;
        }
        Context context = getContext();
        if (context != null) {
          try {
            InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
            ivProfilePic.setImageBitmap(ImageTransformer.centerCrop(imageBitmap));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            profilePicBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
          } catch (FileNotFoundException e) {
            e.printStackTrace();
            new MaterialDialog.Builder(getContext())
                .title("Error")
                .content("Cannot open the file. Please try again.")
                .positiveText("Dismiss")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                  }
                })
                .theme(Theme.LIGHT)
                .show();
          }
        }
      } else {
        new MaterialDialog.Builder(getContext())
            .title("Error")
            .content("Cannot open the file. Please try again.")
            .positiveText("Dismiss")
            .onPositive(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
              }
            })
            .theme(Theme.LIGHT)
            .show();
      }
    }
  }

  private final void focusOnView(final View view){
    if (scrollView != null) {
      new Handler().post(new Runnable() {
        @Override
        public void run() {
          scrollView.scrollTo(0, view.getBottom());
        }
      });
    }
  }

  @Override
  public Serializable onSendData() {
    PersonalData pd = new PersonalData();
    if (profilePicBase64 != null) {
      pd.setProfilePicBase64(profilePicBase64);
    }
    if (etFirstName != null) {
      if (etFirstName.getText() == null || etFirstName.getText().length() <= 0) {
        etFirstName.setError("Given name must not be empty");
        focusOnView(etFirstName);
        return new Throwable("Null given name");
      } else
       pd.setFirstName(etFirstName.getText().toString());
    }
    if (etMiddleName != null) {
      pd.setMiddleName(etMiddleName.getText().toString());
    }
    if (etLastName != null) {
      pd.setLastName(etLastName.getText().toString());
    }
    if (etNativeName != null) {
      pd.setNativeName(etNativeName.getText().toString());
    }
    if (etTag != null) {
      if (etTag.getText() == null || etTag.getText().length() <= 0) {
        etTag.setError("Tag must not be empty");
        return new Throwable("Empty tag");
      } else {
        try {
          pd.setTagNumber(Integer.valueOf(etTag.getText().toString()));
        } catch (NumberFormatException e) {
          e.printStackTrace();
          etTag.setError("This is not a number");
          focusOnView(etTag);
          return new Throwable("Tag is not an integer");
        }
      }
    }
    if (tvBirthday != null) {
      pd.setBirthYear(birthday[0]);
      pd.setBirthMonth(birthday[1]);
      pd.setBirthDate(birthday[2]);
    }
    if (etAddress != null) {
      pd.setAddress(etAddress.getText().toString());
    }
    if (etPhoneNumber != null) {
      pd.setPhoneNumber(etPhoneNumber.getText().toString());
    }
    if (currentGenderId != null) {
      pd.setGenderId(currentGenderId);
    }
    return pd;
  }
}

//checked for deprecated command lines
