package com.example.condomanagement;

import android.content.Intent;
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

public class VisitorSignUpActivity extends AppCompatActivity {

    private EditText fullNameField, emailField, phoneField, IDField, VehichleInfoField, passwordField;

    private Button registerButton;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_visitor);

        // Initialize FirebaseAuth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        fullNameField = findViewById(R.id.edit_text_full_name);
        emailField = findViewById(R.id.edit_text_email);
        phoneField = findViewById(R.id.edit_text_phone);
        IDField = findViewById(R.id.edit_text_id_number);
        VehichleInfoField = findViewById(R.id.edit_text_vehicle_info);
        passwordField = findViewById(R.id.visitor_password);
        registerButton = findViewById(R.id.button_submit);

        // Set up register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
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
        String ID = IDField.getText().toString();
        String vehicleNum = VehichleInfoField.getText().toString();
        String password =  passwordField.getText().toString(); // consider idNumber as the password to login


        // Validate form inputs
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(ID) || TextUtils.isEmpty(vehicleNum)) {
            Toast.makeText(VisitorSignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String this_user = "Visitor";

        // Create the user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Add user details to Firebase Realtime Database
                            saveUserDetails(user.getUid(), fullName, email, phone, ID,  vehicleNum, this_user);
                        }
                    } else {
                        // Handle sign-up failure (e.g., email already exists, weak password)
                        handleSignupFailure(task.getException());
                    }
                });
    }

    private void saveUserDetails(String userId, String fullName, String email, String phone, String id, String vehicleNumber, String role) {

        String node;
        switch (role){
            case "Owner":
                node = "owners";
                break;
            case "visitor":
                node = "visitors";
                break;
            case "guard":
                node = "guards";
                break;
            default:
                node = "visitors";
        }

        Visitor newUser = new Visitor(fullName, email, phone, id, vehicleNumber, "Visitor");

        mDatabase.child(node).child(userId).setValue(newUser)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(VisitorSignUpActivity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();

                        //Navigate back to login page
                        Intent intent = new Intent(VisitorSignUpActivity.this, Login_SignUp_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(VisitorSignUpActivity.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
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
    public static class Visitor {
        public String fullName, email, phone, id, vehicle, role;

        public Visitor(String fullName, String email, String phone, String id, String vehicle, String role) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.id = id;
            this.vehicle = vehicle;
            this.role = role;
        }
    }
}
