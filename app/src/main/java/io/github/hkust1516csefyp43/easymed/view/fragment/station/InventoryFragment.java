package io.github.hkust1516csefyp43.easymed.view.fragment.station;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.listener.OnFragmentInteractionListener;
import io.github.hkust1516csefyp43.easymed.view.activity.InventoryAddActivity;
import io.github.hkust1516csefyp43.easymed.view.fragment.MedicationVariantListFragment;

//import android.support.design.widget.FloatingActionButton;

public class InventoryFragment extends Fragment {
  private String TAG = InventoryFragment.class.getSimpleName();

  private OnFragmentInteractionListener mListener;
  //  private FloatingActionButton floatingActionButton;
  private TabLayout tabLayout;
  private ViewPager viewPager;

  public static InventoryFragment newInstance(String param1, String param2) {
    InventoryFragment fragment = new InventoryFragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  public InventoryFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_toolbar_tablayout_viewpager_fam, container, false);
    Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.setTitle("Inventory");
    toolbar.setSubtitle("In this suitcase");
    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
    ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    if (drawer != null) {
      drawer.addDrawerListener(toggle); //change from setDrawerListener
      toggle.syncState();
    }


    tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
    viewPager = (ViewPager) view.findViewById(R.id.viewPager); //change from vieger to viwePager

    if (viewPager != null && tabLayout != null) {
      tabLayout.addTab(tabLayout.newTab().setText("Out of stock"));
      tabLayout.addTab(tabLayout.newTab().setText("Inadequate"));
      tabLayout.addTab(tabLayout.newTab().setText("Enough"));
      tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() //change from setOnTabSelectedListener to addOnTabSelectedListener
      {
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

      viewPager.setAdapter(new medicationVariantsPagesAdapter(getFragmentManager()));
      viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
      viewPager.setOffscreenPageLimit(3);
    }

    //TODO change it to fab menu
    final FloatingActionsMenu fab = (FloatingActionsMenu) view.findViewById(R.id.fab);
    FloatingActionButton fabInventory = (FloatingActionButton) view.findViewById(R.id.fabInventory);

//    if (fabInventory != null) {
//      fabInventory.setIconDrawable(new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_basket).actionBar().color(Color.WHITE));
//      fabInventory.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//          Toast.makeText(getContext(), "Coming soon", Toast.LENGTH_SHORT).show();
//          fab.collapse();
//          new MaterialDialog.Builder(this)
//              .title("New Medicine")
//              .customView(, true)
//              .positiveText("Add")
//              .negativeText("Cancel")
//              .onNegative(new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                }
//              })
//              .show();
//        }
//      });
//    }


    FloatingActionButton fabMedicine = (FloatingActionButton) view.findViewById(R.id.fabMedicine);
    if (fabMedicine != null) {
      fabMedicine.setIconDrawable(new IconicsDrawable(getContext()).icon(CommunityMaterial.Icon.cmd_pill).actionBar().color(Color.WHITE));
      fabMedicine.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          showAddItemActivity();
          fab.collapse();
        }
      });
    }

    return view;
  }

  private void showAddItemActivity() {
    Intent intent = new Intent(getContext(), InventoryAddActivity.class);
    startActivity(intent);
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  private class medicationVariantsPagesAdapter extends FragmentPagerAdapter {

    public medicationVariantsPagesAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      Log.d(TAG, "starting mvll page " + position);
      return MedicationVariantListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
      return 3;
    }
  }
}

//checked for deprecated command lines