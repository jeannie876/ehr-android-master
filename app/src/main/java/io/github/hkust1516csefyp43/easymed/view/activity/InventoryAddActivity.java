package io.github.hkust1516csefyp43.easymed.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;

import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.github.hkust1516csefyp43.easymed.R;
import io.github.hkust1516csefyp43.easymed.pojo.Stock;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Keyword;
import io.github.hkust1516csefyp43.easymed.pojo.server_response.Medication;
import io.github.hkust1516csefyp43.easymed.utility.Const;
import io.github.hkust1516csefyp43.easymed.utility.v2API;
import mehdi.sakout.dynamicbox.DynamicBox;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InventoryAddActivity extends AppCompatActivity {
  List<Keyword> forms;
  List<Medication> medications;

  DynamicBox box;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory_add);

    ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    final AppCompatSpinner acsMedication = (AppCompatSpinner) findViewById(R.id.acs_medication);
    AppCompatSpinner acsStock = (AppCompatSpinner) findViewById(R.id.acs_stock);
    EditText etStrength = (EditText) findViewById(R.id.et_strength);
    final AppCompatAutoCompleteTextView etForm = (AppCompatAutoCompleteTextView) findViewById(R.id.et_form);
    EditText etRemar = (EditText) findViewById(R.id.tv_remark);

    setSupportActionBar(toolbar);
    if (toolbar != null) {
      toolbar.setTitle("Add item");
      setSupportActionBar(toolbar);
      ActionBar actionBar = getSupportActionBar();
      if (actionBar != null) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
      }
    }

    if (scrollView != null && etForm != null) {
      box = new DynamicBox(this, scrollView);
      box.showLoadingLayout();
      OkHttpClient.Builder ohc1 = new OkHttpClient.Builder();
      ohc1.readTimeout(1, TimeUnit.MINUTES);
      ohc1.connectTimeout(1, TimeUnit.MINUTES);
      Retrofit retrofit = new Retrofit
          .Builder()
          .baseUrl(Const.Database.getCurrentAPI())
          .addConverterFactory(GsonConverterFactory.create(Const.GsonParserThatWorksWithPGTimestamp))
          .client(ohc1.build())
          .build();
      //TODO form (keywords)
      v2API.medications medicationService = retrofit.create(v2API.medications.class);
      Call<List<Medication>> medicationsCall = medicationService.getMedications("1", null, null, null, null, null, null);
      medicationsCall.enqueue(new Callback<List<Medication>>() {
        @Override
        public void onResponse(Call<List<Medication>> call, Response<List<Medication>> response) {
          if (response == null) {
            onFailure(call, new Throwable("Empty response"));
          } else if (response.code() >= 300 || response.code() < 200) {
            onFailure(call, new Throwable("Error from server: " + response.code()));
          } else if (response.body().size() <= 0) {
            onFailure(call, new Throwable("Empty list of medications"));
          } else {
            medications = response.body();
            ArrayAdapter<Medication> medicationArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_list_item_1, medications);
            acsMedication.setAdapter(medicationArrayAdapter);
            acsMedication.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
            });
            checkIfICanHideDynamicBoxNow();
          }
        }

        @Override
        public void onFailure(Call<List<Medication>> call, Throwable t) {
          t.printStackTrace();
          box.showExceptionLayout();
        }
      });

      v2API.keywords keywordService = retrofit.create(v2API.keywords.class);
      Call<List<Keyword>> keywordsCall = keywordService.getKeywords("1", null, null, null, null, null, null, null, null, null, null, true, null, null, null, null, null, null, null, null);
      keywordsCall.enqueue(new Callback<List<Keyword>>() {
        @Override
        public void onResponse(Call<List<Keyword>> call, Response<List<Keyword>> response) {if (response == null) {
          onFailure(call, new Throwable("Empty response"));
        } else if (response.code() >= 300 || response.code() < 200) {
          onFailure(call, new Throwable("Error from server: " + response.code()));
        } else if (response.body().size() <= 0) {
          onFailure(call, new Throwable("Empty list of medications"));
        } else {
          forms = response.body();
          //TODO how to do auto complete text view
          String[] list = new String[forms.size()];
          for (int i = 0; i < forms.size(); i++) {
            list[i] = forms.get(i).getKeyword();
          }
          ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_selectable_list_item, list);
          etForm.setThreshold(1);
          etForm.setAdapter(arrayAdapter);
          checkIfICanHideDynamicBoxNow();
        }

        }

        @Override
        public void onFailure(Call<List<Keyword>> call, Throwable t) {
          t.printStackTrace();
          box.showExceptionLayout();
        }
      });

      if (fab != null) {
        fab.setImageDrawable(new IconicsDrawable(this).actionBar().color(Color.WHITE).icon(CommunityMaterial.Icon.cmd_check));
        fab.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            //TODO check if anything is missing, then call API
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
          }
        });
      }

      if (acsStock != null) {
        ArrayList<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("Enough", 2));
        stocks.add(new Stock("Inadequate", 1));
        stocks.add(new Stock("Out of stock", 0));
        ArrayAdapter<Stock> stockArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stocks);
        acsStock.setAdapter(stockArrayAdapter);
        acsStock.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            //TODO jot it down
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {
            //TODO ?
          }
        });
      }
    }
  }

  private void checkIfICanHideDynamicBoxNow() {
    if (forms != null && medications != null && forms.size() > 0 && medications.size() > 0) {
      box.hideAll();
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

}
