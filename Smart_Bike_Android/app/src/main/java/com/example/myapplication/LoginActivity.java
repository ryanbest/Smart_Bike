package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button Signup;
    private Button Login;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Signup = findViewById(R.id.buttonsignup);
        Login = findViewById(R.id.buttonlogin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                finish();
                startActivity(intent);

            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptlogin();


            }

        });


    }

    private void attemptlogin() {
        String emailId = email.getText().toString();
        String password1 = password.getText().toString();
        if(checkEmailPassword(emailId,password1)) {
            Login(emailId, password1);
        }

    }

    private boolean checkEmailPassword(String Email, String Password) {
        Log.d("Biker","email:"+email);
        Log.d("Biker","password:"+password);
        email.setError(null);
        password.setError(null);
        if(!Email.contains("@"))
        {
            email.requestFocus();
            email.setError("INVALID EMAIL");
        }
        else{

            if(Password.length()<=6)
            {
                password.requestFocus();
                password.setError("Incorrect Pasword");
            }
            else{
                return true;
            }

        }
        return false;

    }

    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        UpdateUI(currentUser);
    }

    private void UpdateUI(FirebaseUser currentUser) {
    }

    private void Login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Biker", "succesful sign in");
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            UpdateUI(firebaseUser);
                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                            finish();
                            startActivity(intent);
                            //AccesingUserInfo();
                        } else {
                            Log.d("Biker", "UNsuccesful sign in");
                            Toast.makeText(getApplicationContext(), "Unsuccesful Sign in", Toast.LENGTH_SHORT)
                                    .show();
                            UpdateUI(null);


                        }
                    }
                });

    }



}
