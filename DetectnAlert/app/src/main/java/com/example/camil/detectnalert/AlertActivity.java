package com.example.camil.detectnalert;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AlertActivity extends MainActivity {

    private Button mAlertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        super.onCreateDrawer();

        mAlertButton= (Button) findViewById(R.id.alertButton);

        //Action boutton alerte prise en charge
        mAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User clicked the button
                Intent HistoricIntent = new Intent(AlertActivity.this, HistoricActivity.class);
                startActivity(HistoricIntent);

            }

        });

    }
}