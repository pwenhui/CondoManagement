package com.example.condomanagement;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class RoleSelectionActivity extends AppCompatActivity {

    private ImageButton tenantButton, guardHouseButton, ownerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        // Find the Toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(""); // Removes the default "CondoManagement" title

        tenantButton = findViewById(R.id.button_visitor);
        guardHouseButton = findViewById(R.id.button_guard_house);
        ownerButton = findViewById(R.id.button_owner);

        // Handle button clicks for each role
        tenantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Visitor sign-up page
                Intent intent = new Intent(RoleSelectionActivity.this, VisitorSignUpActivity.class);
                startActivity(intent);
            }
        });

        guardHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Guard House sign-up page
                Intent intent = new Intent(RoleSelectionActivity.this, GuardHouseSignUpActivity.class);
                startActivity(intent);
            }
        });

        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Owner sign-up page
                Intent intent = new Intent(RoleSelectionActivity.this, OwnerSignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource file
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        return true;
    }

    //handle concept of My Account Clicked
//    public void onMyAccountClick(View view) {
//        Toast.makeText(this, "My Account clicked!", Toast.LENGTH_SHORT).show();
//    }

}
