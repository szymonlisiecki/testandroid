package com.example.myapplication.Login;

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


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressBar loginProgressBar;

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

       if(firebaseAuth.getCurrentUser() != null){
            //jezeli user jest juz zalogowany
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }



        editTextEmail = (EditText) findViewById(R.id.login_mail);
        editTextPassword = (EditText) findViewById(R.id.login_password);
        buttonSignIn = (Button) findViewById(R.id.login_button);
        textViewSignup = (TextView) findViewById(R.id.login_textViewSignin);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    private void userLogin(){
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

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProgressBar.setVisibility(View.VISIBLE);
                buttonSignIn.setVisibility(View.INVISIBLE);
            }
        });

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //start the profile activity
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                    }
                });
    }

    @Override
    public void onClick(View view){
        if(view == buttonSignIn){
            userLogin();
        }

        if(view == textViewSignup){
            startActivity(new Intent(this, RegisterActivity.class));
        }
    }
}
