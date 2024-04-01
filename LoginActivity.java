package com.sp.android_studio_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button btnlogin, btnexit;
    TextView textsignup;
    ProgressBar progressBar;
    //DBHelper DB;

    private FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        btnexit = (Button) findViewById(R.id.btnexit);
        textsignup = (TextView) findViewById(R.id.textsignup);
        progressBar = (ProgressBar) findViewById(R.id.login_progress_bar);
        //DB = new DBHelper(this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty()) {
                        if (password.length() >= 6) {
                            progressBar.setVisibility(View.VISIBLE);
                            auth.signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            SharePreferenceUtil.saveData(LoginActivity.this,"email",email);
                                            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), nav_headerP2.class);
                                            startActivity(intent);
                                            //finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });
                        } else {
                            passwordInput.setError("Password length should be at least 6 characters!");
                            passwordInput.requestFocus();
                            return;
                        }
                    } else {
                        passwordInput.setError("Password cannot be empty");
                        passwordInput.requestFocus();
                        return;
                    }
                } else if (email.isEmpty()) {
                    emailInput.setError("Email cannot be empty");
                    emailInput.requestFocus();
                    return;
                } else {
                    emailInput.setError("Please enter valid email!");
                    emailInput.requestFocus();
                    return;
                }
            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(0);
            }
        });

        textsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }
}