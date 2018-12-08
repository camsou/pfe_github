package com.example.camil.detectnalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewAccountActivity extends AppCompatActivity {

    private Button mCreateNewAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        mCreateNewAccountButton = (Button) findViewById(R.id.CreateNewAccount);

        mCreateNewAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // User clicked the button
                Intent mainActivityIntent = new Intent(NewAccountActivity.this, MainActivity.class);
                startActivity(mainActivityIntent);
            }
        });
    }
}
