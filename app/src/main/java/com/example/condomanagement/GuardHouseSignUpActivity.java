package com.example.condomanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GuardHouseSignUpActivity extends AppCompatActivity {

    //declare field & button
    private EditText fullNameField, emailField, phoneField, EmergencyContactField, passwordField, guardIDField, registerdateField, AssignedZonesField;
    private Button submitButton;


    //declare firebase auth
    private FirebaseAuth mAuth;

    //declare database ref
    private DatabaseReference mDatabase;

    private static String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_guard_house);

        //Initialize FirebaseAuth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        //Initialize views
        fullNameField = findViewById(R.id.edit_text_full_name);
        emailField = findViewById(R.id.edit_text_email);
        phoneField = findViewById(R.id.edit_text_phone);
        EmergencyContactField = findViewById(R.id.edit_text_emergencyContact);
        passwordField = findViewById(R.id.password);
        guardIDField = findViewById(R.id.edit_text_guard_id);
        AssignedZonesField = findViewById(R.id.edit_text_assigned_location);
        submitButton = findViewById(R.id.button_submit);

        //Implement OnDateChangeListener for CalendarView
        CalendarView calendarView = findViewById(R.id.calendar_view_shift);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Get the selected date as a string or Date object
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });

        //set up submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSignUpForm();

            }
        });
    }

    private void submitSignUpForm(){
        String fullName = fullNameField.getText().toString();
        String email = emailField.getText().toString();
        String phone = phoneField.getText().toString();
        String emergencycontact = EmergencyContactField.getText().toString();
        String password = passwordField.getText().toString();
        String guardID = guardIDField.getText().toString();
        String assignedzone =  AssignedZonesField.getText().toString();

        // Validate form inputs
        if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(emergencycontact) || TextUtils.isEmpty(password) || TextUtils.isEmpty(guardID) ||
                selectedDate == null || TextUtils.isEmpty(assignedzone)){
            Toast.makeText(GuardHouseSignUpActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String this_user = "Security Guard";

        //Create the user in Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Add user details to Firebase Realtime Database
                            saveUserDetails(user.getUid(), fullName, email, phone, emergencycontact, password, guardID, selectedDate, assignedzone, this_user);
                        }
                    } else {
                        // Handle sign-up failure (e.g., email already exists, weak password)
                        handleSignupFailure(task.getException());
                    }
                });
    }

    private void saveUserDetails(String userId,String fullname, String email, String phone, String emergencyContact, String password, String guardID, String selectedDate, String assignedZone, String role){

        Guard newUser = new Guard(fullname,email,phone,emergencyContact,password,guardID,selectedDate, assignedZone,"Pending","Guard");

        mDatabase.child("guards").child(userId).setValue(newUser)
                .addOnCompleteListener(task->{
                    if(task.isSuccessful()){
                        Toast.makeText(GuardHouseSignUpActivity.this,"Sign-up successful",Toast.LENGTH_SHORT).show();

                        //Navigate back to login page
                        Intent intent = new Intent(GuardHouseSignUpActivity.this,Login_SignUp_Activity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(GuardHouseSignUpActivity.this,"Failed to save user date", Toast.LENGTH_SHORT).show();
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
    public static class Guard {
        public String fullName, email, phone, emergencyContact, password, guardID, selecteddate, assignedZone, status,role;

        public Guard(String fullName, String email, String phone, String emergencyContact, String password, String guardID, String selecteddate, String assignedZone, String status, String role) {
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.emergencyContact = emergencyContact;
            this.password = password;
            this.guardID = guardID;
            this.selecteddate = selecteddate;
            this.assignedZone = assignedZone;
            this.status = status;
            this.role = role;
        }
    }
}