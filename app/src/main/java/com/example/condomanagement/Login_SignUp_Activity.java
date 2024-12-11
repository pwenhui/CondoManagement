package com.example.condomanagement;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login_SignUp_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private EditText usernameField;
    private EditText passwordField;
    private Spinner roleSpinner;
    private Button loginButton, signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_SignUp_Activity.this, RoleSelectionActivity.class);
                startActivity(intent);
            }

        });
    }

    private void loginUser() {
        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login_SignUp_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        navigateToMainActivity();
                    } else {
                        Toast.makeText(Login_SignUp_Activity.this, "Login Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signUpUser() {
        String email = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String selectedrole = roleSpinner.getSelectedItem().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();

                        // Store additional user details in Realtime Database
                        User newUser = new User(email,selectedrole);
                        mDatabase.child("users").child(userId).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(Login_SignUp_Activity.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                                        navigateToMainActivity();
                                    } else {
                                        Toast.makeText(Login_SignUp_Activity.this, "Failed to store user details: " +
                                                task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(Login_SignUp_Activity.this, "Signup Failed: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //navigate to "Owner" MainActivity
    private void navigateToMainActivity() {
        Intent intent = new Intent(Login_SignUp_Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    // User class to store in Realtime Database
    public static class User {
        public String email;
        public String role;

        //constructor
        public User() {
        }

        public User(String email, String role) {
            this.email = email;
            this.role = role;
        }
    }
}

