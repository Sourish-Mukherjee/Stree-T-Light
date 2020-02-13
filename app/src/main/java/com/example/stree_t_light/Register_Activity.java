package com.example.stree_t_light;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register_Activity extends AppCompatActivity {


    private EditText name, ycont, emercont, email, pass, repass;
    private Button create;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        name = findViewById(R.id.Name_Edit_Text);
        ycont = findViewById(R.id.Your_Contact_Number);
        emercont = findViewById(R.id.Emergency_Contact_Number);
        email = findViewById(R.id.Email_ID_Edit_Text);
        pass = findViewById(R.id.Password_Edit_Text);
        repass = findViewById(R.id.Re_Password_Edit_Text);
        create = findViewById(R.id.create);
        firebaseAuth = FirebaseAuth.getInstance();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check()) {
                    if (pass.getText().toString().equals(repass.getText().toString()) == true) {
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(Register_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login unsuccessful: " + task.getException().getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Account Created, Please Verify Your Email!!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else
                                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });
                    } else
                        Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private boolean check() {
        Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error_black_24dp);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());
        if (name.getText().toString().isEmpty()) {
            name.setError("Field Can't be Empty!", customErrorDrawable);
            name.requestFocus();
            return false;
        }
        if (ycont.getText().toString().isEmpty()) {
            ycont.setError("Field Can't be Empty!", customErrorDrawable);
            ycont.requestFocus();
            return false;
        }
        if (emercont.getText().toString().isEmpty()) {
            emercont.setError("Field Can't be Empty!", customErrorDrawable);
            emercont.requestFocus();
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            email.setError("Field Can't be Empty!", customErrorDrawable);
            email.requestFocus();
            return false;
        }
        if (pass.getText().toString().isEmpty()) {
            pass.setError("Field Can't be Empty!", customErrorDrawable);
            pass.requestFocus();
            return false;
        }
        if (repass.getText().toString().isEmpty()) {
            repass.setError("Field Can't be Empty!", customErrorDrawable);
            repass.requestFocus();
            return false;
        }
        return true;
    }
}
