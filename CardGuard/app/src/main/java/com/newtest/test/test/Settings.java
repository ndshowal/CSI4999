package com.newtest.test.test;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        //to create a logout button
        Button logoutBtn = (Button)findViewById(R.id.logout_button);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, SignIn.class));
            }
        });

        //to create a button to update your account info
        Button updateAccountInfoBtn = (Button)findViewById(R.id.update_account_info_button);

        updateAccountInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, AccountSettings.class));
            }
        });

        //to create a button to update billing info
        Button updateBillingInfoBtn = (Button)findViewById(R.id.update_billing_info_button);

        updateBillingInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, BillingInformation.class));
            }
        });

        //to create a button to update your identification settings
        Button updateIdentificationSettingsBtn = (Button)findViewById(R.id.update_identification_settings_button);

        updateIdentificationSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this, IdentificationSettings.class));
            }
        });

        Button websiteBtn = (Button)findViewById(R.id.website_button);

        websiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cardguardwebsite.azurewebsites.net/"));
                startActivity(browserIntent);
            }
        });
    }
}
