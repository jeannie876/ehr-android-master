package io.github.hkust1516csefyp43.easymed.view.activity;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Attachment;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Patient;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Visit;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_view.BioFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.patient_visit_view.VisitDetailFragment;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatientVisitViewActivity extends AppCompatActivity implements OnFragmentInteractionListener{
  public final static String TAG = PatientVisitViewActivity.class.getSimpleName();

  private Patient thisPatient;
  private List<Visit> visits;

  private ViewPager viewPager;
  private TabLayout tabLayout;

  private Boolean fabOn = false;      //should each visitdetailfragment display edit button on the corner
  private Boolean isTriage = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    final Dialog dialog = new Dialog(this, R.style.AppTheme);
    dialog.setContentView(R.layout.dialog_loading);
    dialog.show();

    setContentView(R.layout.activity_patient_visit_view);

    viewPager = (ViewPager) findViewById(R.id.viewpager);
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

    setSupportActionBar(toolbar);

    final ImageView ivProfilePic = (ImageView) findViewById(R.id.profile_pic);

    thisPatient = (Patient) this.getIntent().getSerializableExtra(Const.BundleKey.READ_ONLY_PATIENT);
    fabOn = getIntent().getBooleanExtra(Const.BundleKey.ON_OR_OFF, false);
    isTriage = getIntent().getBooleanExtra(Const.BundleKey.IS_TRIAGE, true);
    //TODO get extra triage
    //TODO get extra consultation

    if (tabLayout != null) {
      tabLayout.addTab(tabLayout.newTab().setText("Bio"));
    }

    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
      if (thisPatient != null) {
        StringBuilder s = new StringBuilder();
        if (thisPatient.getTag() != null) {
          s.append(thisPatient.getTag().toString() + ". ");
        }
        s.append(Util.displayNameBuilder(thisPatient.getLastName(), thisPatient.getFirstName()));
        actionBar.setTitle(s.toString());
      }
    }

    if (ivProfilePic != null && thisPatient != null) {
      ivProfilePic.setImageDrawable(TextDrawable.builder().buildRect(Util.getTextDrawableText(thisPatient), ColorGenerator.MATERIAL.getColor(Util.displayNameBuilder(thisPatient.getLastName(), thisPatient.getFirstName()))));        //act as placeholder + fallback
      if (thisPatient.getImageId() != null && thisPatient.getProfilePicBase64() == null) {
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
        Call<Attachment> attachmentCall = attachmentService.getAttachment("1", thisPatient.getImageId());
        attachmentCall.enqueue(new Callback<Attachment>() {
          @Override
          public void onResponse(Call<Attachment> call, Response<Attachment> response) {
            if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null && response.body().getFileInBase64() != null) {
              byte[] decodedString = Base64.decode(response.body().getFileInBase64(), Base64.DEFAULT);
              Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
              if (decodedByte != null) {
                ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ivProfilePic.setImageBitmap(decodedByte);
              } else {
                thisPatient.setProfilePicBase64(Const.EMPTY_STRING);
              }
            }
          }

          @Override
          public void onFailure(Call<Attachment> call, Throwable t) {
            t.printStackTrace();
            ivProfilePic.setImageDrawable(TextDrawable.builder().buildRound(Util.getTextDrawableText(thisPatient), ColorGenerator.MATERIAL.getColor(Util.displayNameBuilder(thisPatient.getLastName(), thisPatient.getFirstName()))));
            thisPatient.setProfilePicBase64(Const.EMPTY_STRING);
          }
        });
      } else if (thisPatient.getProfilePicBase64() != null && !thisPatient.getProfilePicBase64().equals(Const.EMPTY_STRING)) {
        byte[] decodedString = Base64.decode(thisPatient.getProfilePicBase64(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        if (decodedByte != null) {
          ivProfilePic.setScaleType(ImageView.ScaleType.CENTER_CROP);
          ivProfilePic.setImageBitmap(decodedByte);
        } else {
          thisPatient.setProfilePicBase64(Const.EMPTY_STRING);
        }
      }
    }

    //TODO /v2/visits/ token patient_id >> populate ui accordingly
    //>> get tcp of each visits >> the visit fragment (not create yet) will handle it

    if (thisPatient != null) {
      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(1, TimeUnit.MINUTES);
      ohc1.connectTimeout(1, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      v2API.visits visitService = retrofit.create(v2API.visits.class);
      Call<List<Visit>> visitsCall = visitService.getVisits("1", null, thisPatient.getPatientId(), "create_timestamp");
      visitsCall.enqueue(new Callback<List<Visit>>() {
        @Override
        public void onResponse(Call<List<Visit>> call, Response<List<Visit>> response) {
          visits = response.body();
          Collections.reverse(visits);
          if (tabLayout != null && viewPager != null) {
            for (Visit v: visits) {
              Log.d(TAG, "visit: " + v.toString());
              tabLayout.addTab(tabLayout.newTab().setText(Util.dateInStringOrToday(v.getCreateTimestamp())));
            }
            viewPager.setAdapter(new patientHistory(getSupportFragmentManager()));
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            if (visits.size() <= 20)
              viewPager.setOffscreenPageLimit(visits.size());
            else
              viewPager.setOffscreenPageLimit(20);
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
              @Override
              public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
              }

              @Override
              public void onTabUnselected(TabLayout.Tab tab) {

              }

              @Override
              public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
              }
            });
          }

          new Handler().postDelayed(new Runnable() {      //Dismiss dialog 1s later (avoid the dialog flashing >> weird)
            @Override
            public void run() {
              dialog.dismiss();               //TODO add animation?
            }
          }, 400);
        }

        @Override
        public void onFailure(Call<List<Visit>> call, Throwable t) {

        }
      });
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.menu_patient_visit_view, menu);
    MenuItem menuItem = menu.findItem(R.id.report);
    if (menuItem != null) {
      menuItem.setIcon(new IconicsDrawable(this).color(Color.WHITE).actionBar().icon(CommunityMaterial.Icon.cmd_file_excel));
    }
    return true;
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  private class patientHistory extends FragmentStatePagerAdapter {

    public patientHistory(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      if (position == 0) {
        return BioFragment.newInstance(thisPatient);
      } else {
        if (visits != null) {
          if (position-1 < visits.size()) {               //-1 because of the Bio page
            if (thisPatient != null) {
              return VisitDetailFragment.newInstance(thisPatient, visits.get(position-1), fabOn, isTriage);
            }
          }
        }
      }
      if (visits != null) {
        Log.d(TAG, "Something is wrong with this: visits.size(): " + visits.size() + "; position: " + position + "; the whole visits: " + visits.toString());
      }
      return new VisitDetailFragment();
    }

    @Override
    public int getCount() {
      return visits.size()+1;
    }
  }


}