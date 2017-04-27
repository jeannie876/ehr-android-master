package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Notification;
import io.github.hkust1516csefyp43.easymed.utility.Cache;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.Util;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
  public final static String TAG = NotificationActivity.class.getSimpleName();

  private SwipeRefreshLayout swipeRefreshLayout;
  private RecyclerView recyclerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    if (toolbar != null) {
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setTitle("Notification");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setOnRefreshListener(this);
    }

    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    if (recyclerView != null) {
      recyclerView.setAdapter(new NotificationRecyclerViewAdapter(getBaseContext()));
      recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
    }
  }

  private void refreshUI() {
    if (swipeRefreshLayout != null) {
      swipeRefreshLayout.setRefreshing(false);
    }
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    if (recyclerView != null) {
      recyclerView.setAdapter(new NotificationRecyclerViewAdapter(getBaseContext()));
      recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
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
  public void onRefresh() {
    swipeRefreshLayout.setRefreshing(true);
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
        Log.d(TAG, response.body().toString());
        Cache.CurrentUser.setNotifications(getBaseContext(), response.body());
        refreshUI();
      }

      @Override
      public void onFailure(Call<List<Notification>> call, Throwable t) {

      }
    });
  }

  private class NotificationRecyclerViewViewHolder extends RecyclerView.ViewHolder {
    TextView tvNotification;
    TextView tvDate;
    ImageView ivReadButton;
    View ivUnreadCircle;

    public NotificationRecyclerViewViewHolder(View itemView) {
      super(itemView);
      tvNotification = (TextView) itemView.findViewById(R.id.tv_notification);
      tvDate = (TextView) itemView.findViewById(R.id.tvDate);
      ivReadButton = (ImageView) itemView.findViewById(R.id.ivReadUnread);
      ivUnreadCircle = itemView.findViewById(R.id.vUnreadCircle);
    }
  }

  private class NotificationRecyclerViewAdapter extends RecyclerView.Adapter<NotificationRecyclerViewViewHolder> {
    List<Notification> notifications;

    public NotificationRecyclerViewAdapter(Context context) {
      notifications = Cache.CurrentUser.getNotifications(context);
    }

    @Override
    public NotificationRecyclerViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new NotificationRecyclerViewViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(NotificationRecyclerViewViewHolder holder, int position) {
      Notification thisNotification = notifications.get(holder.getAdapterPosition());
      holder.tvNotification.setText(thisNotification.getMessage());
      holder.tvDate.setText(Util.dateInString(thisNotification.getRemindDate()));
      if (thisNotification.getRead()) {
        holder.ivReadButton.setVisibility(View.INVISIBLE);
        holder.ivReadButton.setImageDrawable(new IconicsDrawable(getBaseContext()).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_checkbox_blank_circle_outline));
        holder.ivReadButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //TODO PUT notification/:id
          }
        });
      } else {
        holder.ivReadButton.setVisibility(View.VISIBLE);
        holder.ivReadButton.setImageDrawable(new IconicsDrawable(getBaseContext()).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_checkbox_marked_circle_outline));
        holder.ivReadButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //TODO PUT notification/:id
          }
        });
      }
    }

    @Override
    public int getItemCount() {
      if (notifications != null)
        return notifications.size();
      else
        return 0;
    }
  }
}
