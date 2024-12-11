package com.example.condomanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private Button visitorButton, guardHouseButton, ownerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        visitorButton = findViewById(R.id.button_visitor);
        guardHouseButton = findViewById(R.id.button_guard_house);
        ownerButton = findViewById(R.id.button_owner);

        // Handle button clicks for each role
        visitorButton.setOnClickListener(new View.OnClickListener() {
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
}
