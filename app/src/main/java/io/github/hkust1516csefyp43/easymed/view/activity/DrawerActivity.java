package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.cketti.library.changelog.ChangeLog;
import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.listener.OnPatientsFetchedListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.DocumentType;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Gender;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Notification;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.ServerTime;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.AdminFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.ConsultationFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.InventoryFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.PharmacyFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.ReportsFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.station.TriageFragment;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnPatientsFetchedListener, MaterialDialog.SingleButtonCallback {
  public final static String TAG = DrawerActivity.class.getSimpleName();
  private User currentUser;
  public Bundle intentData;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_drawer);

    ChangeLog cl = new ChangeLog(this);
    if (cl.isFirstRun()) {
      cl.getLogDialog().show();
    }


    intentData = getIntent().getExtras();
    listenIntentMethod();


    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      if (currentUser != null) {
        TextView uEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        TextView uName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvName);
        ImageView uProPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        String username = currentUser.getUsername();
        String firstname = currentUser.getFirstName();
        if (uEmail != null && username != null) {
          uEmail.setText(currentUser.getUsername());
        }
        if (uName != null && firstname != null) {
          uName.setText(currentUser.getFirstName());
        }
        if (firstname != null)
          uProPic.setImageDrawable(TextDrawable.builder().buildRound(firstname.substring(0,1), ColorGenerator.MATERIAL.getColor(firstname)));
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            //TODO put user in extra
            startActivity(intent);
          }
        });
      }
    }

    navigationView.setNavigationItemSelectedListener(this);
    Menu menu = navigationView.getMenu();
    MenuItem menuItem = menu.findItem(R.id.nav_triage);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_thermometer).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_consultation);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_hospital).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_pharmacy);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_pharmacy).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_inventory);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_medkit).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_reports);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_file_chart).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_admin);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_male).color(Color.GRAY).actionBar().paddingDp(2));
    }
//    menuItem = menu.findItem(R.id.nav_settings);
//    if (menuItem != null) {
//      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_settings).color(Color.GRAY).actionBar().paddingDp(2));
//    }
    menuItem = menu.findItem(R.id.nav_about);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_info).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_logout);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.GRAY).actionBar().paddingDp(2));
    }

    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
    if (frameLayout != null) {

      TriageFragment triageFragment = new TriageFragment();
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, triageFragment).commit();
    }



  }

  @Override
  protected void onResume() {
    intentData = getIntent().getExtras();
    listenIntentMethod();
    super.onResume();
    Log.d(TAG, "before");
    new ThingsToDoInBackground().execute();
    Log.d(TAG, "after");
    //TODO check if time is right
    if (Const.Database.currentServerType == Const.Database.LOCAL) {
      Log.d(TAG, "Local server");
      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(1, TimeUnit.MINUTES);
      ohc1.connectTimeout(1, TimeUnit.MINUTES);
      final Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      final v2API.staticAPI staticService = retrofit.create(v2API.staticAPI.class);
      Call<ServerTime> serverTimeCall = staticService.getServerTime();
      serverTimeCall.enqueue(new Callback<ServerTime>() {
        @Override
        public void onResponse(Call<ServerTime> call, Response<ServerTime> response) {
          if (response == null) {
            onFailure(call, new Throwable("Empty response"));
          } else if (response.code() >= 300 || response.code() < 200) {
            onFailure(call, new Throwable("Error from server"));
          } else {
            if (response.body() != null)
              Log.d(TAG, response.body().toString());
            GregorianCalendar gc = new GregorianCalendar();
            TimeZone timeZone = gc.getTimeZone();
            Date d = new Date();
            d.setTime(d.getTime()-timeZone.getRawOffset());
            gc.setTime(d);
            final Date serverTime = response.body().getTime();
            long diff = Math.abs(serverTime.getTime() - gc.getTime().getTime());
            Log.d(TAG, "qqq" + String.valueOf(diff));
            if (diff >= Const.MAX_TIME_DIFF) {
              new MaterialDialog.Builder(DrawerActivity.this)
                  .title("Which has the wrong time?")
                  .content("The local server thinks that it's now " + serverTime.toString() + ", which is quite different from the time of the phone. Which one is incorrect?")
                  .positiveText("Server")
                  .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                      ServerTime newST = new ServerTime(Util.dateForPutClock());
                      Log.d(TAG, newST.toString());   //What i need is sth like '2016-06-14T00:15:10.987Z' for 0710 @ Phnom Penh
                      Call<ServerTime> serverTimeCall2 = staticService.setServerTime("1", newST);
                      serverTimeCall2.enqueue(new Callback<ServerTime>() {
                        @Override
                        public void onResponse(Call<ServerTime> call, Response<ServerTime> response) {
                          if (response == null) {
                            onFailure(call, new Throwable("Empty response"));
                          } else if (response.code() >= 300 || response.code() < 200) {
                            onFailure(call, new Throwable("Error from server: " + response.code()));
                          } else {
                            dialog.dismiss();
                          }
                        }

                        @Override
                        public void onFailure(Call<ServerTime> call, Throwable t) {
                          t.printStackTrace();
                          dialog.dismiss();
                          Toast.makeText(getBaseContext(), t.toString(), Toast.LENGTH_LONG).show();
                        }
                      });
                    }
                  })
                  .negativeText("This phone")
                  .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), 0);
                    }
                  })
                  .neutralText("Ignore")
                  .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                      dialog.dismiss();
                    }
                  })
                  .show();
            }
          }
        }

        @Override
        public void onFailure(Call<ServerTime> call, Throwable t) {

        }
      });
    } else {
      Log.d(TAG, "Cloud server");
    }
    currentUser = Cache.CurrentUser.getUser(getApplicationContext());

   /* NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    if (navigationView != null) {
      if (currentUser != null) {
        TextView uEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsername);
        TextView uName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvName);
        ImageView uProPic = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.imageView);
        String username = currentUser.getUsername();
        String firstname = currentUser.getFirstName();
        if (uEmail != null && username != null) {
          uEmail.setText(currentUser.getUsername());
        }
        if (uName != null && firstname != null) {
          uName.setText(currentUser.getFirstName());
        }
        if (firstname != null)
          uProPic.setImageDrawable(TextDrawable.builder().buildRound(firstname.substring(0,1), ColorGenerator.MATERIAL.getColor(firstname)));
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Intent intent = new Intent(getBaseContext(), ProfileActivity.class);
            //TODO put user in extra
            startActivity(intent);
          }
        });
      }
    }


    navigationView.setNavigationItemSelectedListener(this);
    Menu menu = navigationView.getMenu();
    MenuItem menuItem = menu.findItem(R.id.nav_triage);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_thermometer).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_consultation);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_hospital).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_pharmacy);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_pharmacy).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_inventory);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_medkit).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_reports);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(CommunityMaterial.Icon.cmd_file_chart).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_admin);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(FontAwesome.Icon.faw_male).color(Color.GRAY).actionBar().paddingDp(2));
    }
//    menuItem = menu.findItem(R.id.nav_settings);
//    if (menuItem != null) {
//      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_settings).color(Color.GRAY).actionBar().paddingDp(2));
//    }
    menuItem = menu.findItem(R.id.nav_about);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_info).color(Color.GRAY).actionBar().paddingDp(2));
    }
    menuItem = menu.findItem(R.id.nav_logout);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_exit_to_app).color(Color.GRAY).actionBar().paddingDp(2));
    }

    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
    if (frameLayout != null) {

      TriageFragment triageFragment = new TriageFragment();
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, triageFragment).commit();
    }*/
  }

  private void cacheData(final Context context) {
    OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
    ohc1.readTimeout(1, TimeUnit.MINUTES);
    ohc1.connectTimeout(1, TimeUnit.MINUTES);
    final Retrofit retrofit = new Retrofit
        .Builder()
        .baseUrl(Const.Database.getCurrentAPI())
        .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
        .client(ohc1.build())
        .build();

    //1. Cache Document Types
    v2API.documentTypes documentTypeService = retrofit.create(v2API.documentTypes.class);
    Call<List<DocumentType>> documentTypesCall = documentTypeService.getDocumentTypes("1", null, null, null, null);
    documentTypesCall.enqueue(new Callback<List<DocumentType>>() {
      @Override
      public void onResponse(Call<List<DocumentType>> call, Response<List<DocumentType>> response) {
        if (response.code() >= 200 && response.code() < 300) {
          Cache.DatabaseData.setDocumentTypes(context, response.body());
        } else {
          onFailure(call, new Throwable("Something wrong when getting document types"));
        }
      }

      @Override
      public void onFailure(Call<List<DocumentType>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Cannot get document types...", Toast.LENGTH_SHORT).show();
      }
    });

    //2. Cache Genders
    v2API.genders genderService = retrofit.create(v2API.genders.class);
    Call<List<Gender>> gendersCall = genderService.getGenders("1");
    gendersCall.enqueue(new Callback<List<Gender>>() {
      @Override
      public void onResponse(Call<List<Gender>> call, Response<List<Gender>> response) {
        if (response == null) {
          onFailure(call, new Throwable("Empty response"));
        } else if (response.code() >= 300 || response.code() < 200) {
          onFailure(call, new Throwable("Error from server: " + response.code()));
        } else if (response.body().size() <= 0){
          onFailure(call, new Throwable("Empty list of genders"));
        } else {
          Cache.DatabaseData.setGenders(getBaseContext(), response.body());
        }
      }

      @Override
      public void onFailure(Call<List<Gender>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Cannot get gender list...", Toast.LENGTH_SHORT).show();
        //TODO try fetch again
      }
    });

    //3. TODO Cache Blood Types
    //4. Cache Medications
    v2API.medications medicationsService = retrofit.create(v2API.medications.class);
    Call<List<Medication>> medicationsCall = medicationsService.getMedications("1", null, null, null, null, null, null);
    medicationsCall.enqueue(new Callback<List<Medication>>() {
      @Override
      public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
        if (response == null) {
          onFailure(call, new Throwable("Empty response"));
        } else if (response.code() >= 300 || response.code() < 200) {
          onFailure(call, new Throwable("Error from server: " + response.code()));
        } else if (response.body().size() <= 0){
          onFailure(call, new Throwable("Empty list of medications"));
        } else {
          Cache.DatabaseData.setMedications(getBaseContext(), response.body());
        }
      }

      @Override
      public void onFailure(Call<List<Medication>> call, Throwable t) {
        t.printStackTrace();
        Toast.makeText(context, "Cannot get medication list..." + t.toString(), Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer != null) {
      if (drawer.isDrawerOpen(GravityCompat.START)) {
        drawer.closeDrawer(GravityCompat.START);
      } else {
        super.onBackPressed();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.drawer, menu);
    MenuItem mi = menu.findItem(R.id.notification);
    if (mi != null) {
      Log.d(TAG, "setting notification icon");
      mi.setIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_notifications_none).color(Color.WHITE).actionBar());
    } else {
      Log.d(TAG, "cannot set notification icon");
    }
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.notification) {
      Intent i = new Intent(this, NotificationActivity.class);
      startActivity(i);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }


  //Made this method for landing page
  public void listenIntentMethod() {

    if (intentData == null) {
      return;
    }

    if(intentData.containsKey("Consultation")) {
      NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      Menu menu = navigationView.getMenu();
      MenuItem menuItemConsultationFragment = menu.findItem(R.id.nav_consultation);
      onNavigationItemSelected(menuItemConsultationFragment);
    }

    if(intentData.containsKey("Pharmacy")) {
      NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
      Menu menu = navigationView.getMenu();
      MenuItem menuItemPharmacyFragment = menu.findItem(R.id.nav_pharmacy);
      onNavigationItemSelected(menuItemPharmacyFragment);
    }


  }


  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    int id = item.getItemId();
    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
    switch (id) {
      case R.id.nav_triage:
        TriageFragment triageFragment = new TriageFragment();
        fragmentTransaction.replace(R.id.fragment_container, triageFragment).commit();
        break;
      case R.id.nav_consultation:
        ConsultationFragment consultationFragment = new ConsultationFragment();
        fragmentTransaction.replace(R.id.fragment_container, consultationFragment).commit();
        break;
      case R.id.nav_pharmacy:
        PharmacyFragment pharmacyFragment = new PharmacyFragment();
        fragmentTransaction.replace(R.id.fragment_container, pharmacyFragment).commit();
        break;
      case R.id.nav_inventory:
        InventoryFragment inventoryFragment = new InventoryFragment();
        fragmentTransaction.replace(R.id.fragment_container, inventoryFragment).commit();
        break;
      case R.id.nav_reports:
        ReportsFragment reportsFragment = new ReportsFragment();
        fragmentTransaction.replace(R.id.fragment_container, reportsFragment).commit();
        break;
      case R.id.nav_admin:
        AdminFragment adminFragment = new AdminFragment();
        fragmentTransaction.replace(R.id.fragment_container, adminFragment).commit();
        break;
//      case R.id.nav_settings:
//        Intent intent = new Intent(DrawerActivity.this, SettingsActivity.class);
//        startActivity(intent);
//        break;
      case R.id.nav_about:
        new LibsBuilder()
            .withActivityTitle("About")
            .withFields(R.string.class.getFields())
            .withAboutIconShown(true)
            .withAboutVersionShown(true)
            .withVersionShown(true)
            .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
            .start(DrawerActivity.this);
        break;
      case R.id.nav_logout:
        new MaterialDialog.Builder(this)
            .theme(Theme.LIGHT)
            .autoDismiss(true)
            .content("Are you sure you want to logout?")
            .positiveText("Logout")
            //TODO icon?
            .negativeColor(getResources().getColor(R.color.colorAccent))
            .onPositive(this)
            .negativeText("Dismiss")
            .onNegative(new MaterialDialog.SingleButtonCallback() {
              @Override
              public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
              }
            })
            .show();
        break;
    }
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer != null) {
      drawer.closeDrawer(GravityCompat.START);
    }
    return true;
  }

  @Override
  public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
    Cache.CurrentUser.logout(this);
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  @Override
  public void updateTabTitleCounter(int whichPage, int howMuch) {
    Log.d(TAG, "wp = " + whichPage + "; hm = " + howMuch);
  }

  private class ThingsToDoInBackground extends AsyncTask<Void, Void, Void> {

    public ThingsToDoInBackground() {
      Log.d(TAG, "ast constructor");
    }

    @Override
    protected Void doInBackground(Void... params) {
      //fetch notifications
      Log.d(TAG, "do in background");

      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(1, TimeUnit.MINUTES);
      ohc1.connectTimeout(1, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      v2API.notifications notificationService = retrofit.create(v2API.notifications.class);
      Call<List<Notification>> notificationList = notificationService.getMyNotifications("1");
      notificationList.enqueue(new Callback<List<Notification>>() {
        @Override
        public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
//          Log.d(TAG, response.body().toString());
          Cache.CurrentUser.setNotifications(getBaseContext(), response.body());
        }

        @Override
        public void onFailure(Call<List<Notification>> call, Throwable t) {
          Toast.makeText(getApplicationContext(), "Cannot get notification list...", Toast.LENGTH_SHORT).show();
        }
      });

      cacheData(getBaseContext());
      return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      Log.d(TAG, "ope");
    }
  }
}
