package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.optimizely.Optimizely;

import net.danlew.android.joda.JodaTimeAndroid;

import io.fabric.sdk.android.Fabric;
import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.AsyncResponse;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Connectivity;
import io.github.hkust1516csefyp43.easymed.utility.Const;

public class SplashActivity extends AppCompatActivity {

  private static final String TAG = SplashActivity.class.getSimpleName();
  private String error;
  private TextView tvWhichServer;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    Optimizely.startOptimizelyWithAPIToken(getString(R.string.com_optimizely_api_key), getApplication());
    JodaTimeAndroid.init(this);

    setContentView(R.layout.activity_splash);
    tvWhichServer = (TextView) findViewById(R.id.tv_which_server);

    //TODO extract urls from cache/const
    final CheckIfServerIsAvailable task2 = new CheckIfServerIsAvailable(this, "Internet", Const.Database.CLOUD, "ehr-api.herokuapp.com", Const.Database.CLOUD_API_BASE_URL_121_dev, 443, new AsyncResponse() {
      @Override
      public void processFinish(String output, Boolean successful) {
        if (!successful) {
          error = "Even heroku is not available";
          if (tvWhichServer != null) {
            tvWhichServer.setText(error);
          }
          //TODO try again button for both server >> execute task1/task2
        }
      }
    });
    CheckIfServerIsAvailable task1 = new CheckIfServerIsAvailable(this, "Local", Const.Database.LOCAL, "192.168.0.194", 3000, 3000, Const.Database.LOCAL_API_BASE_URL_121_dev, new AsyncResponse() {
      @Override
      public void processFinish(String output, Boolean successful) {
        if (!successful)
          task2.execute();
      }
    });
    if (Connectivity.isConnected(getApplicationContext())) {
      if (Connectivity.isConnectedWifi(getApplicationContext())) {
        task1.execute();
      } else {
        task2.execute();
      }
    } else {
      error = "Internet is not available";
    }
  }

  public class CheckIfServerIsAvailable extends AsyncTask<Void, Boolean, Boolean> {
    Context context;
    String host;
    int port;
    String apiUrl;
    AsyncResponse delegate;
    String serverName;
    int serverType;
    int timeout = 10000;

    public CheckIfServerIsAvailable(Context context, String serverName, int serverType, String host, String apiUrl, int port, AsyncResponse delegate) {
      this.context = context;
      this.host = host;
      this.port = port;
      this.delegate = delegate;
      this.serverName = serverName;
      this.serverType = serverType;
      this.apiUrl = apiUrl;
    }

    public CheckIfServerIsAvailable(Context context, String serverName, int serverType, String host, int port, int timeout, String apiUrl, AsyncResponse delegate) {
      this.context = context;
      this.host = host;
      this.port = port;
      this.delegate = delegate;
      this.serverName = serverName;
      this.serverType = serverType;
      this.timeout = timeout;
      this.apiUrl = apiUrl;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
      return Connectivity.isReachableByTcp(host, port, timeout);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
      super.onPostExecute(aBoolean);
      if (aBoolean) {
        Log.d(TAG, "successful: " + host);
        if (tvWhichServer != null) {
          tvWhichServer.setVisibility(View.VISIBLE);
          tvWhichServer.setText("You are now connected to the '" + serverName + "' server");
          Const.Database.setCurrentAPI(apiUrl);
          Const.Database.currentServerType = serverType;
        }
        final Class target;
        User user = Cache.CurrentUser.getUser(context);
        if (user == null){
          target = LoginActivity.class;
        }else{
          //target = DrawerActivity.class;
          target = LandingPageActivity.class;
        }
        new Handler().postDelayed(new Runnable() {
          @Override
          public void run() {
            Intent mainIntent = new Intent(SplashActivity.this, target);
            startActivity(mainIntent);
            finish();
          }
        }, Const.SPLASH_DISPLAY_LENGTH);
      } else {
        Log.d(TAG, host + " is not accessible");
        delegate.processFinish("failed", false);
      }
    }
  }

}
