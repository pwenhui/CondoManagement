package com.example.condomanagement;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class GuardHouseSignUpActivity extends AppCompatActivity {

    //declare field & button
    private EditText fullNameField, emailField, phoneField, houseAddressField, unitNumberField, idNumberField, proofOfOwnershipField, passwordField;

    private Button submitButton;

    //declare firebase auth
    private FirebaseAuth mAuth;

    //declare database ref
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_guard_house);

        //Initialize FirebaseAuth and Database reference
    }
}