package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.io.Serializable;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.User;
import io.github.hkust1516csefyp43.easymed.utility.Const;

public class ProfileActivity extends AppCompatActivity {
  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);

    Intent intent = getIntent();
    if (intent != null) {
      Serializable serializable = intent.getSerializableExtra(Const.BundleKey.CURRENT_USER);
      if (serializable instanceof User)
        user = (User) serializable;
    }

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle("Louis Tsai");            //TODO if user != null
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    //TODO if user != null
    ImageView ivProfilePic = (ImageView) findViewById(R.id.profile_pic);
    if (ivProfilePic != null) {
      ivProfilePic.setImageDrawable(TextDrawable.builder().buildRect("LT", ColorGenerator.MATERIAL.getColor("Louis Tsai")));
    }

    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setImageDrawable(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_edit).actionBar().color(Color.WHITE));
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openEditProfile();
      }
    });
  }

  private void openEditProfile(){
    Intent intent = new Intent(this, EditProfileActivity.class);
    startActivity(intent);
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
