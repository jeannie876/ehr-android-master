package io.github.hkust1516csefyp43.easymed.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import io.github.hkust1516csefyp43.easymed.R;

/**
 * Created by Tejas_K on 6/4/2017.
 */

public class LandingPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);
        ImageButton triageButton = (ImageButton) findViewById(R.id.triageButton);
        ImageButton consultationButton = (ImageButton) findViewById(R.id.consultationButton);
        ImageButton faqButton = (ImageButton) findViewById(R.id.faqButton);
        ImageButton pharmacyButton = (ImageButton) findViewById(R.id.pharmacyButton);

        if (triageButton != null) {
            triageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LandingPageActivity.this, DrawerActivity.class);
                    startActivity(intent);
                }
            });
        }

        if (consultationButton != null) {
            consultationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LandingPageActivity.this, DrawerActivity.class);
                    intent.putExtra("Consultation", R.id.nav_consultation);
                    startActivity(intent);
                }
            });
        }

//        if (faqButton != null) {
//            pharmacyButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(LandingPageActivity.this, .class);
//                    startActivity(intent);
//                }
//            });
//        }

        if (pharmacyButton != null) {
            pharmacyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LandingPageActivity.this, DrawerActivity.class);
                    intent.putExtra("Pharmacy", R.id.nav_pharmacy);
                    startActivity(intent);
                }
            });
        }
    }

}
