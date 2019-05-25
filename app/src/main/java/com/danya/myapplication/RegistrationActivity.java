package com.danya.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
    private EditText userName, userPassword, userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString().trim(),userPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                sendEmailVerification();
                                // Toast.makeText(RegistrationActivity.this, "Registration successfull", Toast.LENGTH_SHORT).show();
                                // startActivity(new Intent (RegistrationActivity.this,MainActivity.class));
                            }

                            else
                                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });
    }
    private void setupUIViews(){
        userName=(EditText)findViewById(R.id.etUserName);
        userPassword=(EditText)findViewById(R.id.etUserPassword);
        userEmail=(EditText)findViewById(R.id.etUserEmail);
        regButton=(Button)findViewById(R.id.btnRegister);
        userLogin=(TextView)findViewById(R.id.tvUserLogin);
    }

    private boolean validate(){
        if(userName.getText().toString().isEmpty() || userPassword.getText().toString().isEmpty() || userEmail.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            return false;
        }

        else return true;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegistrationActivity.this, "Verification email send successfully", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(RegistrationActivity.this, "Error while sending verification email", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
