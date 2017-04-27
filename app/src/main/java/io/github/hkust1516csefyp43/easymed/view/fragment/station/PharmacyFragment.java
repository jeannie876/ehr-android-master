package io.github.hkust1516csefyp43.easymed.view.fragment.station;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Clinic;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.view.fragment.PatientListFragment;

public class PharmacyFragment extends Fragment {

  private OnFragmentInteractionListener mListener;
  private TabLayout tabLayout;
  private ViewPager viewPager;

  public PharmacyFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_toolbar_tablayout_viewpager, container, false);
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.setTitle("Pharmacy");
    Clinic thisClinic = Cache.CurrentUser.getClinic(getContext());
    if (thisClinic != null) {
      toolbar.setSubtitle(thisClinic.getEnglishName());
    }

    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    if (drawer != null) {
      //need to removeDrawerListener(DrawerLayout.DrawerListener)...somewhere
      drawer.addDrawerListener(toggle);
      toggle.syncState();
    }

    tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
    tabLayout.addTab(tabLayout.newTab().setText("Waiting"));
    tabLayout.addTab(tabLayout.newTab().setText("Finished"));
    viewPager = (ViewPager) view.findViewById(R.id.viewPager);

    //where to removeOnTabSelectedListener??
    tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    viewPager.setAdapter(new TwoPagesAdapter(getFragmentManager()));
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    viewPager.setOffscreenPageLimit(2);

    return view;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
//    if (context instanceof OnFragmentInteractionListener) {
//      mListener = (OnFragmentInteractionListener) context;
//    } else {
//      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
//    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  private class TwoPagesAdapter extends FragmentStatePagerAdapter {

    public TwoPagesAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          return PatientListFragment.newInstance(Const.PatientListPageId.PRE_PHARMACY, null);
        case 1:
          return PatientListFragment.newInstance(Const.PatientListPageId.POST_PHARMACY, null);
        default:
          return PatientListFragment.newInstance(Const.PatientListPageId.NOT_YET, null);    //TODO idk?

      }
    }

    @Override
    public int getCount() {
      return 2;
    }
  }
}

//checked for deprecated command lines