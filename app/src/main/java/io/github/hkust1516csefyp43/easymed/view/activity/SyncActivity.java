package io.github.hkust1516csefyp43.easymed.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.AsyncResponse;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Query;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Connectivity;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SyncActivity extends AppCompatActivity {
  private static final String TAG = SyncActivity.class.getSimpleName();

  private Button bPullFromCloud;
  private TextView tvPullFromCloud;
  private Button bPushToLocal;
  private TextView tvPushToLocal;
  private Button bPullFromLocal;
  private TextView tvPullFromLocal;
  private Button bPushToCloud;
  private TextView tvPushToCloud;

  private int pushProgress = 0;
  private int pushTotal;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sync);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    bPullFromCloud = (Button) findViewById(R.id.bPullFromCloud);
    tvPullFromCloud = (TextView) findViewById(R.id.tvPullFromCloud);
    bPushToLocal = (Button) findViewById(R.id.bPushToLocal);
    tvPushToLocal = (TextView) findViewById(R.id.tvPushToLocal);
    bPullFromLocal = (Button) findViewById(R.id.bPullFromLocal);
    tvPullFromLocal = (TextView) findViewById(R.id.tvPullFromLocal);
    bPushToCloud = (Button) findViewById(R.id.bPushToCloud);
    tvPushToCloud = (TextView) findViewById(R.id.tvPushToCloud);

    if (toolbar != null) {
      toolbar.setTitle(R.string.synchronization);
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    GregorianCalendar lastPullFromCloud = Cache.Synchronisation.getLastPullFromCloud(this);
    GregorianCalendar lastPushToLocal = Cache.Synchronisation.getLastPushToLocal(this);
    GregorianCalendar lastPullFromLocal = Cache.Synchronisation.getLastPullFromLocal(this);
    GregorianCalendar lastPushToCloud = Cache.Synchronisation.getLastPushToCloud(this);

    if (tvPullFromCloud != null) {
      if (lastPullFromCloud != null) {
        tvPullFromCloud.setText("Last pull: " + Util.GCInStringForSync(lastPullFromCloud));
      } else {
        tvPullFromCloud.setText("Last pull: Never");
      }
    }
    if (tvPushToLocal != null) {
      if (lastPushToLocal != null) {
        tvPushToLocal.setText("Last push: " + Util.GCInStringForSync(lastPushToLocal));
      } else {
        tvPushToLocal.setText("Last push: Never");
      }
    }
    if (tvPullFromLocal != null) {
      if (lastPullFromLocal != null) {
        tvPullFromLocal.setText("Last pull: " + Util.GCInStringForSync(lastPullFromLocal));
      } else {
        tvPullFromLocal.setText("Last pull: Never");
      }
    }
    if (tvPushToCloud != null) {
      if (lastPushToCloud != null) {
        tvPushToCloud.setText("Last push: " + Util.GCInStringForSync(lastPushToCloud));
      } else {
        tvPushToCloud.setText("Last push: Never");
      }
    }

    final Context c = this;
    bPullFromCloud.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(c, "Loading", "Please wait");
        CheckIfServerIsAvailable checkIfServerIsAvailable = new CheckIfServerIsAvailable("ehr-api.herokuapp.com", 443, 10000, new AsyncResponse() {
          @Override
          public void processFinish(String output, Boolean successful) {
            if (successful) {
              OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
              ohc1.readTimeout(2, TimeUnit.MINUTES);
              ohc1.connectTimeout(2, TimeUnit.MINUTES);
              Retrofit retrofit = new Retrofit
                  .Builder()
                  .baseUrl(Const.Database.CLOUD_API_BASE_URL_121_dev)
                  .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                  .client(ohc1.build())
                  .build();
              v2API.queries queryService = retrofit.create(v2API.queries.class);
              Call<List<Query>> queriesCall = queryService.getQueries("1");
              queriesCall.enqueue(new Callback<List<Query>>() {
                @Override
                public void onResponse(Call<List<Query>> call, Response<List<Query>> response) {
                  if (response != null && response.body() != null) {
                    Log.d(TAG, response.body().toString());
                    Cache.Synchronisation.setPullFromCloudData(getBaseContext(), response.body());
                    Cache.Synchronisation.setLastPullFromCloud(getBaseContext(), new GregorianCalendar());
                    progressDialog.dismiss();
                    tvPullFromCloud.setText("Last pull: " + Util.GCInStringForSync(Cache.Synchronisation.getLastPullFromCloud(getBaseContext())));
                  }
                }

                @Override
                public void onFailure(Call<List<Query>> call, Throwable t) {
                  t.printStackTrace();
                  Log.d(TAG, "failed: " + t.toString());
                  if (progressDialog != null) {
                    progressDialog.dismiss();
                    new AlertDialog
                        .Builder(SyncActivity.this)
                        .setMessage("Something goes wrong. Please try again.\nError: " + t.toString())
                        .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                          }
                        })
                        .show();

                  }
                }
              });
            } else {
              progressDialog.dismiss();                                                             //cannot access local server
              new AlertDialog
                  .Builder(SyncActivity.this)
                  .setMessage("Cannot access Local Server (The Bag)")
                  .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
                  .show();
            }
          }
        });
        checkIfServerIsAvailable.execute();
      }
    });

    bPushToLocal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ProgressDialog searchingServerDialog = ProgressDialog.show(c, "Loading", "Please wait");
        CheckIfServerIsAvailable checkIfServerIsAvailable = new CheckIfServerIsAvailable("192.168.0.194", 3000, new AsyncResponse() {
          @Override
          public void processFinish(String output, Boolean successful) {
            if (successful) {
              searchingServerDialog.dismiss();
              List<Query> queryList = Cache.Synchronisation.getPullFromCloudData(getBaseContext());
              if (queryList != null && queryList.size() > 0) {

                pushTotal = queryList.size();

                PushToServer pushToServer = new PushToServer(queryList, Const.Database.LOCAL_API_BASE_URL_121_dev, tvPushToLocal);
                pushToServer.execute();

              } else {  //nothing to push
                new MaterialDialog.Builder(SyncActivity.this).title("Nothing to push").positiveText("Dismiss").onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                  }
                }).show();
              }
            } else {  //TODO server not accessible
              searchingServerDialog.dismiss();
              new AlertDialog
                  .Builder(SyncActivity.this)
                  .setTitle("Error")
                  .setMessage("Cannot access Local Server (The Bag). You can check the following:\n1. Are you on the right Wi-Fi network?\n2. Is the server on?")
                  .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
                  .show();
            }
          }
        });
        checkIfServerIsAvailable.execute();
      }
    });

    bPullFromLocal.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ProgressDialog progressDialog = ProgressDialog.show(c, "Loading", "Please wait");
        CheckIfServerIsAvailable checkIfServerIsAvailable = new CheckIfServerIsAvailable("192.168.0.194", 3000, 10000, new AsyncResponse() {
          @Override
          public void processFinish(String output, Boolean successful) {
            if (successful) {
              OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
              ohc1.readTimeout(2, TimeUnit.MINUTES);
              ohc1.connectTimeout(2, TimeUnit.MINUTES);
              Retrofit retrofit = new Retrofit
                  .Builder()
                  .baseUrl(Const.Database.LOCAL_API_BASE_URL_121_dev)
                  .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
                  .client(ohc1.build())
                  .build();
              v2API.queries queryService = retrofit.create(v2API.queries.class);
              Call<List<Query>> queriesCall = queryService.getQueries("1");
              queriesCall.enqueue(new Callback<List<Query>>() {
                @Override
                public void onResponse(Call<List<Query>> call, Response<List<Query>> response) {
                  if (response != null && response.body() != null) {
                    Log.d(TAG, response.body().toString());
                    Cache.Synchronisation.setPullFromLocalData(getBaseContext(), response.body());
                    Cache.Synchronisation.setLastPullFromLocal(getBaseContext(), new GregorianCalendar());
                    progressDialog.dismiss();
                    tvPullFromLocal.setText("Last pull: " + Util.GCInStringForSync(Cache.Synchronisation.getLastPullFromLocal(getBaseContext())));
                  } else {
                    Log.d(TAG, "failed");
                  }
                }

                @Override
                public void onFailure(Call<List<Query>> call, Throwable t) {
                  t.printStackTrace();
                  Log.d(TAG, "failed: " + t.toString());
                  progressDialog.dismiss();
                }
              });
            } else {
              //TODO cannot access local server
              progressDialog.dismiss();
            }
          }
        });
        checkIfServerIsAvailable.execute();
      }
    });

    bPushToCloud.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final ProgressDialog searchingServerDialog = ProgressDialog.show(c, "Loading", "Please wait");
        CheckIfServerIsAvailable checkIfServerIsAvailable = new CheckIfServerIsAvailable("ehr-api.herokuapp.com", 443, 10000, new AsyncResponse() {
          @Override
          public void processFinish(String output, Boolean successful) {
            if (successful) {
              searchingServerDialog.dismiss();
              List<Query> queryList = Cache.Synchronisation.getPullFromLocalData(getBaseContext());
              if (queryList != null && queryList.size() > 0) {

                pushTotal = queryList.size();

                PushToServer pushToServer = new PushToServer(queryList, Const.Database.CLOUD_API_BASE_URL_121_dev, tvPushToCloud);
                pushToServer.execute();

              } else {  //nothing to push
                new MaterialDialog.Builder(SyncActivity.this).title("Nothing to push").positiveText("Dismiss").onPositive(new MaterialDialog.SingleButtonCallback() {
                  @Override
                  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                  }
                }).show();
              }
            } else {  //TODO server not accessible
              searchingServerDialog.dismiss();
              new AlertDialog
                  .Builder(SyncActivity.this)
                  .setTitle("Error")
                  .setMessage("Cannot access the cloud server. Are you sure you are connected to the internet?")
                  .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss();
                    }
                  })
                  .show();
            }
          }
        });
        checkIfServerIsAvailable.execute();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        pushTotal = 0;
        pushProgress = 0;
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public View onCreateView(String name, Context context, AttributeSet attrs) {
    View view = super.onCreateView(name, context, attrs);
    //TODO Dynamicbox
    return view;
  }

  @Override
  protected void onResume() {
    super.onResume();
    //TODO disable buttons according to network connection
  }

  private void setPushProgress(int i) {
    pushProgress = i;
  }

  private boolean finishedPushingYet(ProgressDialog pd) {
    if (pushTotal - pushProgress <= 0) {
      pd.dismiss();
      tvPushToLocal.setText("Last pull: " + Util.GCInStringForSync(new GregorianCalendar()));
      return true;
    } else
      return false;
  }

  private class CheckIfServerIsAvailable extends AsyncTask<Void, Boolean, Boolean> {
    String host;
    int port;
    int timeout = 10000;
    AsyncResponse delegate;

    public CheckIfServerIsAvailable(String host, int port, AsyncResponse delegate) {
      this.host = host;
      this.port = port;
      this.delegate = delegate;
    }

    public CheckIfServerIsAvailable(String host, int port, int timeout, AsyncResponse delegate) {
      this.host = host;
      this.port = port;
      this.delegate = delegate;
      this.timeout = timeout;
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
        delegate.processFinish("successful", true);
      } else {
        Log.d(TAG, host + " is not accessible");
        delegate.processFinish("failed", false);
      }
    }
  }

  private class PushToServer extends AsyncTask<Void, Integer, Integer> {

    List<Query> queries;
    String baseUrl;
    ProgressDialog progressDialog;
    TextView textView;

    public PushToServer(List<Query> queries, String baseUrl, TextView textView) {
      this.queries = queries;
      this.textView = textView;
      this.baseUrl = baseUrl;
      this.progressDialog = new ProgressDialog(SyncActivity.this);
    }

    public PushToServer(List<Query> queries, String baseUrl, ProgressDialog progressDialog, TextView textView) {
      this.queries = queries;
      this.textView = textView;
      this.baseUrl = baseUrl;
      this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      if (progressDialog != null) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgress(0);
        progressDialog.setMax(queries.size());
        progressDialog.show();
      }
    }

    @Override
    protected Integer doInBackground(Void... params) {
      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(2, TimeUnit.MINUTES);
      ohc1.connectTimeout(2, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(baseUrl)
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      v2API.queries queryService = retrofit.create(v2API.queries.class);
      Log.d(TAG, "Before for loop");
      for (int i = 0; i < queries.size(); i++) {
        Log.d(TAG, "In the for loop: " + i);
        Call<Query> queryCall = queryService.pushQuery("1", queries.get(i));
        try {
          Response<Query> queryResponse = queryCall.execute();
          Log.d(TAG, "in progress: " + i + "/" + new GsonBuilder().create().toJson(queryResponse));
          publishProgress(i);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
//      super.onProgressUpdate(values);
      if (progressDialog != null)
        progressDialog.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(Integer integer) {
      progressDialog.dismiss();
      textView.setText("Last push: " + Util.GCInStringForSync(new GregorianCalendar()));
    }
  }

}
