package com.example.condomanagement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OwnerSignUpActivity extends AppCompatActivity {

    private EditText fullNameField, emailField, phoneField, houseAddressField, unitNumberField, idNumberField, proofOfOwnershipField, passwordField;
    private Button submitButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_owner);

        // Initialize FirebaseAuth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        fullNameField = findViewById(R.id.edit_text_full_name);
        emailField = findViewById(R.id.edit_text_email);
        phoneField = findViewById(R.id.edit_text_phone);
        houseAddressField = findViewById(R.id.edit_text_house_address);
        unitNumberField = findViewById(R.id.edit_text_unit_number);
        idNumberField = findViewById(R.id.edit_text_id_number);
        proofOfOwnershipField = findViewById(R.id.edit_text_proof_of_ownership);
        passwordField = findViewById(R.id.password);
        submitButton = findViewById(R.id.button_submit);

        // Set up submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSignUpForm();
            }
        });

    }
    private void submitSignUpForm() {
        String fullName = fullNameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String houseAddress = houseAddressField.getText().toString();
        String unitNumber = unitNumberField.getText().toString();
        String idNumber = idNumberField.getText().toString();
        String proofOfOwnership = proofOfOwnershipField.getText().toString();
        String password =  passwordField.getText().toString(); // consider idNumber as the password to login

        // Validate form inputs
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(houseAddress) || TextUtils.isEmpty(unitNumber) || TextUtils.isEmpty(idNumber) ||
                TextUtils.isEmpty(proofOfOwnership)) {
            Toast.makeText(OwnerSignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String this_user = "Owner";

        // Create the user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Add user details to Firebase Realtime Database
                            saveUserDetails(user.getUid(), fullName, email, phone, houseAddress, unitNumber, idNumber, proofOfOwnership, this_user);
                        }
                    } else {
                        // Handle sign-up failure (e.g., email already exists, weak password)
                        handleSignupFailure(task.getException());
                    }
                });
    }

    private void saveUserDetails(String userId, String fullName, String email, String phone, String houseAddress, String unitNumber, String idNumber, String proofOfOwnership, String role) {

        Owner newUser = new Owner(fullName, email, phone, houseAddress, unitNumber, idNumber, proofOfOwnership,"Pending" , "Owner");

        mDatabase.child("owners").child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(OwnerSignUpActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();

                        //Navigate back to login page
                        Intent intent = new Intent(OwnerSignUpActivity.this, Login_SignUp_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(OwnerSignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleSignupFailure(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            Toast.makeText(this, "Weak password", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show();
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sign-up failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Define a User class for storing user details in Firebase
    public static class Owner {
        public String fullName, email, phone, houseAddress, unitNumber, idNumber, proofOfOwnership,status,role;

        public Owner(String fullName, String email, String phone, String houseAddress, String unitNumber, String idNumber, String proofOfOwnership, String status, String role) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.houseAddress = houseAddress;
            this.unitNumber = unitNumber;
            this.idNumber = idNumber;
            this.proofOfOwnership = proofOfOwnership;
            this.status = status;
            this.role = role;
        }
    }
}