package com.example.stree_t_light;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {

    private Button register, login;
    private EditText email, password;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email_ID_Edit_Text_Login);
        password = findViewById(R.id.Password_Edit_Text_Login);
        login = findViewById(R.id.login_button);
        register = findViewById(R.id.register_button);
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                {
                    if (firebaseUser != null) {
                        Toast.makeText(getApplicationContext(), "FireBaseLogin!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "FIreBaseLogin! Please ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        register.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(getApplicationContext(), Register_Activity.class));
                                        }
                                    }
        );
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful())
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            else {

                                if (firebaseAuth.getCurrentUser().isEmailVerified())
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                else
                                    Toast.makeText(getApplicationContext(), "Email Address Not Verified!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }


    private boolean check() {
        Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error_black_24dp);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        if (email.getText().toString().isEmpty()) {
            email.setError("Field Can't be Empty!", customErrorDrawable);
            email.requestFocus();
            return false;
        }
        if (password.getText().toString().isEmpty()) {
            password.setError("Field Can't Be Empty!", customErrorDrawable);
            password.requestFocus();
        }
        return true;
    }
}
