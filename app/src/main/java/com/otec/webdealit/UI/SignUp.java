package com.otec.webdealit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.otec.webdealit.R;

import static com.otec.webdealit.Utils.utils.message;


public class SignUp extends AppCompatActivity {

    FirebaseApp firebaseFirestore;
    EditText email,pass;
    Button register,login;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        register = findViewById(R.id.Register);
        login = findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);




        register.setOnClickListener(u->{
            if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(s -> {
                            if (s.isSuccessful()) {
                                message("Admin Registered", getApplicationContext());
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                message("Registration failed" + s.getException(), getApplicationContext());
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        });
            }
        });


        login.setOnClickListener(u -> {
            if(!email.getText().toString().isEmpty() && !pass.getText().toString().isEmpty()) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString())
                        .addOnCompleteListener(f -> {

                            if (f.isSuccessful()) {
                                message("Welcome Admin " + email.getText().toString(), getApplicationContext());
                                progressBar.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                message("Registration failed", getApplicationContext());
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });
            }
        });




    }
}