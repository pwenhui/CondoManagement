package com.example.condomanagement;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class VisitorSignUpActivity extends AppCompatActivity {

    private EditText firstNameField, emailField, phoneField, IDField, VehichleInfoField, passwordField, editTextDOB;

    private Button registerButton;

    private FirebaseAuth mAuth;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_tenant);

        // Initialize FirebaseAuth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Initialize views
        firstNameField = findViewById(R.id.edit_firstName);
        emailField = findViewById(R.id.edit_text_email);
        phoneField = findViewById(R.id.edit_text_phone);
        IDField = findViewById(R.id.edit_text_id_number);
        VehichleInfoField = findViewById(R.id.edit_text_vehicle_info);
        passwordField = findViewById(R.id.visitor_password);
        registerButton = findViewById(R.id.button_submit);
        editTextDOB = findViewById(R.id.edit_text_id_number);

        // Open DatePickerDialog on EditText click
        editTextDOB.setOnClickListener(v -> showDatePickerDialog());

        //Account Type Spinner
        Spinner spinner = findViewById(R.id.dropdown_selection);

        // Load account type options from the string array
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dropdown_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //Flag to skip first trigger
        final boolean[] isSpinnerFirstCall = {true};

        // Handle the account type selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isSpinnerFirstCall[0]){
                    isSpinnerFirstCall[0] = false;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    // "Please Select" is selected
                    Toast.makeText(getApplicationContext(), "Please select a valid option", Toast.LENGTH_SHORT).show();
                } else {
                    // Valid selection
                    Toast.makeText(getApplicationContext(), "You selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        //Home Country Spinner
        Spinner HomeCountrySpinner = findViewById(R.id.HomeCountrydropdown_selection);

        //Flag to skip first trigger
        final boolean[] isHomeCountrySpinnerFirstCall = {true};

        // Load account type options from the string array
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.HomeCountryDropdown_items, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        HomeCountrySpinner.setAdapter(adapter1);

        // Handle the Home Country selection
        HomeCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(isHomeCountrySpinnerFirstCall[0]){
                    isHomeCountrySpinnerFirstCall[0] = false;
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    // "Please Select" is selected
                    Toast.makeText(getApplicationContext(), "Please select a valid option", Toast.LENGTH_SHORT).show();
                } else {
                    // Valid selection
                    Toast.makeText(getApplicationContext(), "You selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        //Home Country Spinner
        Spinner PresentCountrySpinner = findViewById(R.id.PresentCountrydropdown_selection);

        // Flag to skip first trigger
        final boolean[] isSpinnerPresentCountryFirstCall = {true};

        // Load account type options from the string array
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.HomeCountryDropdown_items, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PresentCountrySpinner.setAdapter(adapter2);

        // Handle the Present Country selection
        PresentCountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerPresentCountryFirstCall[0]) {
                    isSpinnerPresentCountryFirstCall[0] = false; // skip the first automatic call
                    return;
                }

                String selectedItem = parent.getItemAtPosition(position).toString();
                if (position == 0) {
                    // "Please Select" is selected
                    Toast.makeText(getApplicationContext(), "Please select a valid option", Toast.LENGTH_SHORT).show();
                } else {
                    // Valid selection
                    Toast.makeText(getApplicationContext(), "You selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });



        // Set up register button click listener
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSignUpForm();
            }
        });
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // DatePickerDialog setup
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set selected date to EditText
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editTextDOB.setText(selectedDate);
                },
                year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void submitSignUpForm() {
        String fullName = firstNameField.getText().toString();
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

        Visitor newUser = new Visitor(fullName, email, phone, id, vehicleNumber, "Visitor");

        mDatabase.child("visitors").child(userId).setValue(newUser)
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
