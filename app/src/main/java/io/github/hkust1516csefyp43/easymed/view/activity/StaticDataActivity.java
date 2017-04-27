package io.github.hkust1516csefyp43.easymed.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.view.fragment.static_data.ClinicsFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.static_data.KeywordsFragment;
import io.github.hkust1516csefyp43.easymed.view.fragment.static_data.StaticDataFragment;

public class StaticDataActivity extends AppCompatActivity {
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private String[] tabs = {"Blood Types", "Genders", "Keywords", "Suitcases", "Clinics", "Countries"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_static_data);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setTitle("Static");
      actionBar.setDisplayHomeAsUpEnabled(true);
      actionBar.setDisplayShowHomeEnabled(true);
    }

    findViews();

    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    viewPager.setAdapter(new staticDataAdapter(getSupportFragmentManager()));
    viewPager.setOffscreenPageLimit(6);
    tabLayout.setupWithViewPager(viewPager);
    tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
  }

  private void findViews() {
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);
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

  private class staticDataAdapter extends FragmentStatePagerAdapter {

    public staticDataAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return StaticDataFragment.newInstance(Const.StaticPages.BLOOD_TYPES);
        case 1:
          return StaticDataFragment.newInstance(Const.StaticPages.GENDERS);
        case 2:
          return KeywordsFragment.newInstance();
        case 3:
          return StaticDataFragment.newInstance(Const.StaticPages.SUITCASES);
        case 4:
//          return StaticDataFragment.newInstance(Const.StaticPages.CLINICS);
          return ClinicsFragment.newInstance();
        case 5:
          return StaticDataFragment.newInstance(Const.StaticPages.COUNTRIES);
        //TODO
        default:
          //TODO show error
          return StaticDataFragment.newInstance();
      }
    }

    @Override
    public int getCount() {
      return tabs.length;
    }
  }

}
