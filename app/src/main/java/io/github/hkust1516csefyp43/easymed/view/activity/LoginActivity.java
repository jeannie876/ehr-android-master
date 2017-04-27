package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.LoginEvent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.LoginCredentials;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Clinic;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity{

  private static final String TAG = LoginActivity.class.getSimpleName();

  private UserLoginTask mAuthTask = null;

  private TextInputEditText mUsername;
  private EditText mPasswordView;
  private ProgressBar mProgressView;
  private ScrollView mLoginFormView;
  private AppCompatSpinner clinicList;

  private Clinic currentClinic;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    mUsername = (TextInputEditText) findViewById(R.id.username);

    mPasswordView = (EditText) findViewById(R.id.password);
    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });

    Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
    if (mSignInButton != null) {
      mSignInButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          attemptLogin();
        }
      });
    }

    Button mRegisterButton = (Button) findViewById(R.id.register_button);
    if (mRegisterButton != null) {
      mRegisterButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
          startActivity(intent);
        }
      });
    }

    mLoginFormView = (ScrollView) findViewById(R.id.login_form);
    mProgressView = (ProgressBar) findViewById(R.id.login_progress);
    clinicList = (AppCompatSpinner) findViewById(R.id.spinner);

    if (mProgressView != null) {
      mProgressView.setVisibility(View.VISIBLE);
    }
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.readTimeout(1, TimeUnit.MINUTES);
    httpClient.connectTimeout(1, TimeUnit.MINUTES);
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(httpClient.build())
        .build();
    v2API.clinics clinicsService = retrofit.create(v2API.clinics.class);
    Call<List<Clinic>> clinicListCall = clinicsService.getSimplifiedClinics();
    clinicListCall.enqueue(new Callback<List<Clinic>>() {
      @Override
      public void onResponse(Call<List<Clinic>> call, final Response<List<Clinic>> response) {
        if (response.body() != null && response.body().size() > 0) {
          clinicList.setVisibility(View.VISIBLE);
          Log.d(TAG, "list of clinics: " + response.body().toString());
          if (mProgressView != null) {
            mProgressView.setVisibility(View.GONE);
          }
          ArrayAdapter<Clinic> clinicArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, response.body());
          clinicList.setAdapter(clinicArrayAdapter);
          clinicList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              Log.d(TAG, "Clinic: " + response.body().get(position).getEnglishName());
              currentClinic = response.body().get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              Log.d(TAG, "?");
            }
          });
        } else
          Log.d(TAG, "nope");
      }

      @Override
      public void onFailure(Call<List<Clinic>> call, Throwable t) {
        Log.d(TAG, "Received nothing");
      }
    });
  }

  /**
   * Attempts to sign in or register the account specified by the login form.
   * If there are form errors (invalid username, missing fields, etc.), the
   * errors are presented and no actual login attempt is made.
   */
  private void attemptLogin() {
    if (currentClinic == null) {
      Toast.makeText(getBaseContext(), "Select clinic first", Toast.LENGTH_LONG).show();
    } else {
      if (mAuthTask != null) {
        return;
      }
      mUsername.setError(null);
      mPasswordView.setError(null);
      String username = mUsername.getText().toString();
      String password = mPasswordView.getText().toString();
      boolean cancel = false;
      View focusView = null;
      if (!isPasswordValid(password)) {
        mPasswordView.setError(getString(R.string.error_invalid_password));
        focusView = mPasswordView;
        cancel = true;
      }
      if (TextUtils.isEmpty(username)) {
        mUsername.setError(getString(R.string.error_field_required));
        focusView = mUsername;
        cancel = true;
      }

      if (cancel) {
        focusView.requestFocus();
      } else {
        showProgress(true);
        mAuthTask = new UserLoginTask(username, password);
        mAuthTask.execute((Void) null);
      }
    }
  }

  private boolean isPasswordValid(String password) {
    return password.length() > 0;
  }

  private void showProgress(final boolean show) {
    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
  }

  private void openEverything() {
    Intent intent = new Intent(this, LandingPageActivity.class);
    startActivity(intent);
  }

  public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

    private final String mUsername;
    private final String mPassword;

    UserLoginTask(String username, String password) {
      mUsername = username;
      mPassword = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
      httpClient.readTimeout(1, TimeUnit.MINUTES);
      httpClient.connectTimeout(1, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(httpClient.build())
          .build();
      v2API.login loginService = retrofit.create(v2API.login.class);
      Call<Object> objectCall = loginService.login(new LoginCredentials(mUsername, mPassword, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)));
      try {
        Response<Object> response = objectCall.execute();
        if (response == null) {
//          onFailure(call, new Throwable("Something's wrong. Please try again."));
          Log.d(TAG, "Null response");
          return false;
        } else if (response.code() >= 300 || response.code() < 200) {
//          onFailure(call, new Throwable("Username or password is wrong"));
          Log.d(TAG, "Null error from server");
          return false;
        } else {    //successful
          return true;
        }
      } catch (IOException e) {
        e.printStackTrace();
        return false;
      }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
      mAuthTask = null;
      showProgress(false);

      if (success) {
        //TODO get the actual user json
        User user = new User();
        user.setUsername(mUsername);
        Answers.getInstance().logLogin(new LoginEvent().putSuccess(true));
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(1, TimeUnit.MINUTES);
        httpClient.connectTimeout(1, TimeUnit.MINUTES);
        Retrofit retrofit = new Retrofit
            .Builder()
            .baseUrl(Const.Database.getCurrentAPI())
            .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
            .client(httpClient.build())
            .build();
        v2API.clinics clinicsService = retrofit.create(v2API.clinics.class);
        Call<Clinic> clinicCall = clinicsService.getClinic("1", currentClinic.getClinicId());
        clinicCall.enqueue(new Callback<Clinic>() {
          @Override
          public void onResponse(Call<Clinic> call, Response<Clinic> response) {
            if (response == null) {
              onFailure(call, new Throwable("empty body"));
            } else if (response.code() < 200 || response.code() >= 300) {
              onFailure(call, new Throwable("Error from server: " + response.code()));
            } else {
              Cache.CurrentUser.setClinic(getBaseContext(), response.body());
            }
          }

          @Override
          public void onFailure(Call<Clinic> call, Throwable t) {
            t.printStackTrace();
          }
        });
        v2API.users userService = retrofit.create(v2API.users.class);
        Call<List<User>> usersCall = userService.getUsers("1", null);
        usersCall.enqueue(new Callback<List<User>>() {
          @Override
          public void onResponse(Call<List<User>> call, Response<List<User>> response) {
            if (response == null) {
              onFailure(call, new Throwable("Empty response"));
            } else if (response.code() < 200 || response.code() >= 300) {
              onFailure(call, new Throwable("Error from server: " + response.code()));
            } else {
              //TODO manual search -_- because i have forgot to do that in API -_-
              for (User u:response.body()) {
                if (u.getUsername().compareTo(mUsername) == 0) {
                  Cache.CurrentUser.setUser(getApplicationContext(), u);
                  openEverything();
                  finish();
                }
              }
            }
          }

          @Override
          public void onFailure(Call<List<User>> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(getApplicationContext(), "Failed to fetch user info: " + t.toString(), Toast.LENGTH_LONG).show();
            openEverything();
            finish();
          }
        });
        Cache.CurrentUser.setClinic(getApplicationContext(), currentClinic);
        Cache.CurrentUser.setUser(getApplicationContext(), user);
      } else {
        Answers.getInstance().logLogin(new LoginEvent().putSuccess(false));
        mPasswordView.setError(getString(R.string.error_incorrect_password));
        mPasswordView.requestFocus();
      }
    }

    @Override
    protected void onCancelled() {
      mAuthTask = null;
      showProgress(false);
    }
  }
}

