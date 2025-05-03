package com.techvipin130524.mychatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.stream.Stream;

public class MyLoginActivity extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private Button buttonSignin, buttonSignup;
    private TextView textViewForgot;

    FirebaseAuth auth ;
    FirebaseUser firebaseUser;


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextEmail = findViewById(R.id.editTextEmailSignup);
        editTextPassword = findViewById(R.id.editTextPasswordSignup);
        buttonSignin = findViewById(R.id.buttonSignin);
        buttonSignup = findViewById(R.id.buttonSignup);
        textViewForgot = findViewById(R.id.textViewForgot);

        auth = FirebaseAuth.getInstance();

        buttonSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (!email.equals("") && !password.equals("")){
                    signin(email, password);
                }else {
                    Toast.makeText(MyLoginActivity.this,
                            "Please enter an email and password.", Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyLoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyLoginActivity.this,ForgotActivity.class);
                startActivity(i);
                finish();

            }
        });



    }
    public void signin(String email, String password){

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(MyLoginActivity.this, MainActivity.class);
                    Toast.makeText(MyLoginActivity.this,
                            "Sign in is successful.", Toast.LENGTH_LONG).show();
                    startActivity(intent);

                }else {
                    Toast.makeText(MyLoginActivity.this,
                            "Sign in is not successful.", Toast.LENGTH_LONG).show();
                }
                
            }
        });

    }
}