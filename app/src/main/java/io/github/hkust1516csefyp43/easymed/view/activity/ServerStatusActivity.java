package io.github.hkust1516csefyp43.easymed.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.ServerStatus;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerStatusActivity extends AppCompatActivity {

  private static final String TAG = ServerStatusActivity.class.getSimpleName();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_server_status);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if (toolbar != null) {
      toolbar.setTitle(R.string.server_status);
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    final TextView tvAppVersion = (TextView) findViewById(R.id.tv_server_app_version);
    final TextView tvNodeJsVersion = (TextView) findViewById(R.id.tv_nodejs_version);
    final TextView tvNpmVersion = (TextView) findViewById(R.id.tv_npm_version);
    final TextView tvRunTime = (TextView) findViewById(R.id.tv_run_time);

    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear_layout);
    final DynamicBox box = new DynamicBox(this, linearLayout);
    box.showLoadingLayout();

    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(1, TimeUnit.MINUTES);
    ohc1.connectTimeout(1, TimeUnit.MINUTES);
    Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();
    v2API.staticAPI staticAPI = retrofit.create(v2API.staticAPI.class);
    Call<ServerStatus> statusCall = staticAPI.getStatus();
    statusCall.enqueue(new Callback<ServerStatus>() {
      @Override
      public void onResponse(Call<ServerStatus> call, Response<ServerStatus> response) {
        Log.d(TAG, "receiving sth");
        if (response == null) {
          onFailure(call, new Throwable("empty response"));
        } else if (response.code() < 200 || response.code() >= 300){
          onFailure(call, new Throwable("Error: " + response.code()));
        } else {
          Log.d(TAG, "yes?");
          Log.d(TAG, "yes: " + response.body().toString());
//          textView.setText(response.body().toString());
          fillTheView(tvAppVersion, tvNodeJsVersion, tvNpmVersion, tvRunTime, response.body());
          box.hideAll();
        }
      }

      @Override
      public void onFailure(Call<ServerStatus> call, Throwable t) {
        t.printStackTrace();
        box.showExceptionLayout();
      }
    });

    //TODO hide if is currently connected to cloud server
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    if (fab != null) {
      if (Const.Database.currentServerType == Const.Database.CLOUD) {
        fab.setVisibility(View.GONE);
      } else {
        fab.setImageDrawable(new IconicsDrawable(ServerStatusActivity.this).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_power));
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            new MaterialDialog.Builder(ServerStatusActivity.this)
                .title("Shut down local server")
                .content("Are you sure?")
                .positiveText("Shut down")
                .negativeText("Leave it on")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    //TODO call API
                    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
                    ohc1.readTimeout(1, TimeUnit.MINUTES);
                    ohc1.connectTimeout(1, TimeUnit.MINUTES);
                    Retrofit retrofit = new Retrofit
                        .Builder()
                        .baseUrl(Const.Database.getCurrentAPI())
                        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                        .client(ohc1.build())
                        .build();
                    v2API.staticAPI staticAPI = retrofit.create(v2API.staticAPI.class);
                    Call<Object> responseBodyCall = staticAPI.shutdown("1");
                    responseBodyCall.enqueue(new Callback<Object>() {
                      @Override
                      public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response == null) {
                          onFailure(call, new Throwable("Empty response"));
                        } else if (response.code() >= 300 || response.code() < 200){
                          onFailure(call, new Throwable("Error " + response.code()));
                        } else {
                          killApp();
                        }
                      }

                      @Override
                      public void onFailure(Call<Object> call, Throwable t) {
                        t.printStackTrace();

                      }
                    });
                  }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                  }
                })
                .autoDismiss(true)
                .theme(Theme.LIGHT)
                .show();
          }
        });
      }
    }
  }

  /**
   * TODO test if it works
   */
  private void killApp() {
    Cache.CurrentUser.logout(getBaseContext());
    finish();
    System.exit(0);
  }

  private void fillTheView(@NonNull TextView tvAppVersion, @NonNull TextView tvNodeJsVersion, @NonNull TextView tvNpmVersion, @NonNull TextView tvRunTime, ServerStatus body) {
    if (body != null) {
      tvAppVersion.setText(body.getAppVersion());
      if (body.getNodeVersion() != null)
        tvNodeJsVersion.setText(body.getNodeVersion());
      else
        tvNodeJsVersion.setText("Unknown");
      if (body.getNpmVersion() != null)
        tvNpmVersion.setText(body.getNpmVersion());
      else
        tvNpmVersion.setText("Unknown");
      //TODO tidy tvRunTime up. It's so ugly right now
//      tvRunTime.setText(body.getRunningFor().getYear() + " years " + body.getRunningFor().getWeek() + " weeks " + body.getRunningFor().getDay() + " days " + body.getRunningFor().getHour() + " hours " + body.getRunningFor().getMinute() + " minutes " + body.getRunningFor().getSecond() + " seconds " + body.getRunningFor().getMillisecond() + " milliseconds " );
      tvRunTime.setText(body.getRunningFor().getHour() + " hours " + body.getRunningFor().getMinute() + " minutes " + body.getRunningFor().getSecond() + " seconds");
    }
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
