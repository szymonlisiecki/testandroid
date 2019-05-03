package com.example.myapplication.Login;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private ProgressBar loginProgressBar;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        loginButton = findViewById(R.id.login_button);
        loginProgressBar = findViewById(R.id.login_progressBar);
        loginProgressBar.setVisibility(View.INVISIBLE);

        editTextEmail = (EditText) findViewById(R.id.login_mail);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        textViewSignin = (TextView)  findViewById(R.id.login_textViewSignin);

        loginButton.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Podaj email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Podaj hasło", Toast.LENGTH_SHORT).show();
            return;
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }
        });

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is successfully registered and logged in
                            //we will start the progile activity here
                            //narazie tylko toasta wyswietle
                            Toast.makeText(LoginActivity.this, "Zarejestrowano", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(LoginActivity.this, "Nie mozna zarejestrowac", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == loginButton){
            registerUser();
        }

        if (view == textViewSignin){
            //otworzymy activity z rejestracja
        }
    }
}
