package com.newtest.test.test;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;


public class CardScan extends AppCompatActivity {

    User user;

    SharedPreferences sp;

    private static final int REQUEST_SCAN = 101;
    private static final int REQUEST_AUTOTEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardscan);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        user = getIntent().getParcelableExtra("UserKey");

        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        Button btnscan = (Button)findViewById(R.id.btnScan);
        btnscan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(CardScan.this, CardIOActivity.class);
                intent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true);
                intent.putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, true);
                intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true);
                intent.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);
                intent.putExtra(CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE, "en");
                intent.putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.RED);
                intent.putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, true);
                intent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, false);
                intent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
                startActivityForResult(intent, REQUEST_SCAN);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_SCAN || requestCode == REQUEST_AUTOTEST) && data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            SharedPreferences.Editor ed = sp.edit();

            ((TextView) findViewById(R.id.tvCardDetail)).setVisibility(View.VISIBLE);
            String resultDisplayStr;
            if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                ed.putString("CardNum", scanResult.cardNumber);

                // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
                resultDisplayStr = "Card Number: " + scanResult.getRedactedCardNumber() + "\n";

                if (scanResult.isExpiryValid()) {
                    resultDisplayStr += "Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
                    ed.putString("CardExpirationDate", String.valueOf(scanResult.expiryMonth)
                                                      + "/" + String.valueOf(scanResult.expiryYear));
                }
                if (scanResult.cvv != null) {
                    // Never log or display a CVV
                    resultDisplayStr += "CVV has " + scanResult.cvv.length() + " digits.\n";
                }
                if (scanResult.postalCode != null) {
                    resultDisplayStr += "Postal Code: " + scanResult.postalCode + "\n";
                }

                ed.putString("CardHolderName", scanResult.cardholderName);
                ed.apply();
            } else {
                resultDisplayStr = "Scan was canceled.";
            }
            ((TextView) findViewById(R.id.tvCardDetail)).setText(resultDisplayStr);
        }
    }
}


