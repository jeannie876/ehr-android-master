package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.view.fragment.PatientListFragment;

public class SearchActivity extends AppCompatActivity implements OnFragmentInteractionListener, MaterialSearchView.OnQueryTextListener {
  private MaterialSearchView searchView;
  private boolean isTriage;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    searchView = (MaterialSearchView) findViewById(R.id.search_view);


    if (searchView != null) {
      searchView.setOnQueryTextListener(this);
    }

    if (toolbar != null) {
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle("Search by name ->");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    Intent intent = getIntent();
    if (intent != null) {
      if(intent.hasExtra("triage")){
          isTriage = true;
      } else isTriage = false;

    }



    //triage or consultation
    //from extra: patient

    //bottom >> PatientListFragment

    //(search == no current visit, triage and consultation)
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_search, menu);
    MenuItem item = menu.findItem(R.id.action_search);
    item.setIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_search).actionBar().color(Color.WHITE));
    if (searchView != null)
      searchView.setMenuItem(item);
    return true;
  }

  @Override
  public void onBackPressed() {
    if (searchView != null) {
      if (searchView.isSearchOpen()) {
        searchView.closeSearch();
      } else {
        super.onBackPressed();
      }
    } else {
      super.onBackPressed();
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
  public void onFragmentInteraction(Uri uri) {

  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    PatientListFragment patientListFragment;
    if (isTriage) {
      patientListFragment = PatientListFragment.newInstance(Const.PatientListPageId.TRIAGE_SEARCH, query);
    } else {
      patientListFragment = PatientListFragment.newInstance(Const.PatientListPageId.CONSULTATION_SEARCH, query);
    }
    if (patientListFragment != null) {
      FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
      fragmentTransaction.replace(R.id.fragment_container, patientListFragment).commit();
    }
    return true;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }
}
