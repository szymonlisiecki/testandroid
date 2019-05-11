package com.example.myapplication.Login;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private Button registerButton;
    private ProgressBar registerProgressBar;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    //firebase auth object
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

      /*  if(firebaseAuth.getCurrentUser() != null){
            //jezeli user jest juz zalogowany
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }*/
        registerButton = findViewById(R.id.buttonLogout);
        registerProgressBar = findViewById(R.id.register_progressBar);
        registerProgressBar.setVisibility(View.INVISIBLE);

        editTextEmail = (EditText) findViewById(R.id.register_mail);
        editTextPassword = (EditText) findViewById(R.id.register_password);
        textViewSignin = (TextView)  findViewById(R.id.register_textViewSignin);

        registerButton.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //sprawdza czy email i hasło są puste
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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerProgressBar.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.INVISIBLE);
            }
        });

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(RegisterActivity.this, "Nie mozna zarejestrowac", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == registerButton){
            registerUser();
        }

        if (view == textViewSignin){
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}
