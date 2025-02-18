package com.example.condomanagement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the role from the intent
        String role = getIntent().getStringExtra("ROLE");
        View view;

        // Inflate the layout based on the role
        switch (role) {
            case "tenant":
                view = LayoutInflater.from(this).inflate(R.layout.activity_sign_up_tenant, null);
                break;
            case "guard":
                view = LayoutInflater.from(this).inflate(R.layout.activity_sign_up_guard_house, null);
                break;
            case "owner":
                view = LayoutInflater.from(this).inflate(R.layout.activity_sign_up_owner, null);
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        // Set the inflated view as the content view
        setContentView(view);
    }
}
