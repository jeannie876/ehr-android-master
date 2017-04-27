package io.github.hkust1516csefyp43.easymed.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Role;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
  public static final String TAG = SignUpActivity.class.getSimpleName();

  //view
  private TextInputEditText tietFirstname;
  private TextInputEditText tietUsername;
  private TextInputEditText tietPassword1;
  private TextInputEditText tietPassword2;
  private ProgressBar progressBar;
  private AppCompatSpinner acsRole;

  private Role role;

  private boolean isQr;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    Intent intent = getIntent();
    if (intent != null) {
      isQr = intent.getBooleanExtra(Const.BundleKey.IS_QR, true);
    }

    final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle(R.string.sign_up);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    findViewsById();
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.readTimeout(1, TimeUnit.MINUTES);
    httpClient.connectTimeout(1, TimeUnit.MINUTES);
    final Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(httpClient.build())
        .build();

    if (acsRole != null && !isQr) {
      acsRole.setVisibility(View.VISIBLE);
      //TODO call API, get list of roles, display name of each role
      if (progressBar != null) {
        progressBar.setVisibility(View.VISIBLE);
      }
      v2API.roles rolesService = retrofit.create(v2API.roles.class);
      Call<List<Role>> rolesCall = rolesService.getRoles("1");
      rolesCall.enqueue(new Callback<List<Role>>() {
        @Override
        public void onResponse(Call<List<Role>> call, final Response<List<Role>> response) {
          if (response.body() != null && response.body().size() > 0) {
            acsRole.setVisibility(View.VISIBLE);
            Log.d(TAG, "list of roles: " + response.body().toString());
            if (progressBar != null) {
              progressBar.setVisibility(View.GONE);
            }
            ArrayAdapter<Role> roleArrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, response.body());
            acsRole.setAdapter(roleArrayAdapter);
            acsRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "Role: " + response.body().get(position).getName());
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                role = response.body().get(position);
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "?");
              }
            });
          } else{
            Log.d(TAG, "Something wrong: " + response.code());
          }
        }

        @Override
        public void onFailure(Call<List<Role>> call, Throwable t) {
          Log.d(TAG, "received nothing");
        }
      });
    }

    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    if (fab != null) {
      if (isQr) {
        fab.setImageDrawable(new IconicsDrawable(this).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_qrcode));
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //TODO check
            if (isEveryBoxValid()) {
              QRCodeWriter qrCodeWriter = new QRCodeWriter();
              try {
                BitMatrix bitMatrix = qrCodeWriter.encode(generateUserAccountData().toString(), BarcodeFormat.QR_CODE, 1024, 1024);  //TODO replace with the user input
                int height = bitMatrix.getHeight();
                int width = bitMatrix.getWidth();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++){
                  for (int y = 0; y < height; y++){
                    bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
                  }
                }
                Dialog builder = new Dialog(SignUpActivity.this);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setCanceledOnTouchOutside(true);
                builder.setCancelable(true);
                ImageView imageView = new ImageView(SignUpActivity.this);
                imageView.setImageBitmap(bmp);

                TextView textView = new TextView(SignUpActivity.this);
                textView.setText("Ask admin to create your account");

                LinearLayout linearLayout = new LinearLayout(SignUpActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(textView);
                linearLayout.addView(imageView);

                builder.addContentView(linearLayout, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
              } catch (WriterException e) {
                e.printStackTrace();
              }
            }
          }
        });
      } else {
        fab.setImageDrawable(new IconicsDrawable(this).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_check));
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (isEveryBoxValid()) {
              if (role != null){
                User newUser = new User();
                newUser.setFirstName(tietFirstname.getText().toString());
                newUser.setUsername(tietUsername.getText().toString());
                newUser.setPassword(tietPassword2.getText().toString());
                newUser.setRole(role);
                v2API.users userService = retrofit.create(v2API.users.class);
                Call<User> userCall = userService.addUser("1", newUser);
                userCall.enqueue(new Callback<User>() {
                  @Override
                  public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null){
                      if (response.code() >= 200 && response.code() < 300) {
                        Toast.makeText(getApplicationContext(), "Successfully created the user!", Toast.LENGTH_LONG).show();
                        finish();
                      }else {
                        onFailure(call, new Throwable("Some error: " + response.code()));
                        Toast.makeText(getApplicationContext(), "Some error: " + response.code(), Toast.LENGTH_LONG).show();
                      }
                    }
                  }

                  @Override
                  public void onFailure(Call<User> call, Throwable t) {
                    t.printStackTrace();
                  }
                });
              }
            }
          }
        });
      }
    }
  }

  private void findViewsById() {
    tietFirstname = (TextInputEditText) findViewById(R.id.etFirstName);
    tietUsername = (TextInputEditText) findViewById(R.id.etUsername);
    tietPassword1 = (TextInputEditText) findViewById(R.id.etPassword1);
    tietPassword2 = (TextInputEditText) findViewById(R.id.etPassword2);
    progressBar = (ProgressBar) findViewById(R.id.sign_up_progress);
    acsRole = (AppCompatSpinner) findViewById(R.id.sRole);
  }

  private boolean isEveryBoxValid() {
    if (tietUsername != null && tietPassword1 != null && tietPassword2 != null) {
      if (tietUsername.getText() != null && !tietUsername.getText().toString().equals("") && tietUsername.getText().length() > 0) {
        if (tietPassword1.getText() != null && !tietPassword1.getText().toString().equals("") && tietPassword1.getText().length() > 0) {
          if (tietPassword2.getText() != null && !tietPassword2.getText().toString().equals("") && tietPassword2.getText().length() > 0) {
            if (tietPassword1.getText().toString().equals(tietPassword2.getText().toString())) {
              return true;
            } else {
              Log.d(TAG, "pw1 != pw2");
              tietPassword2.setError("Not match");
              return false;
            }
          } else {
            Log.d(TAG, "empty pw2");
            tietPassword2.setError("Cannot be empty");
            return false;
          }
        } else {
          Log.d(TAG, "empty pw1");
          tietPassword1.setError("Cannot be empty");
          return false;
        }
      } else {
        Log.d(TAG, "empty username");
        tietUsername.setError("Cannot be empty");
        return false;
      }
    } else {
      Log.d(TAG, "Something wrong. Can't find the views.");
      return false;
    }
  }

  private JSONObject generateUserAccountData() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put(Const.SignUp.USERNAME, tietUsername.getText().toString());
      jsonObject.put(Const.SignUp.PASSWORD, tietPassword1.getText().toString());
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return jsonObject;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
