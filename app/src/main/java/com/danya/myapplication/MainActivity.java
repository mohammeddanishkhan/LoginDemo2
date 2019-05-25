package com.danya.myapplication;

import android.app.ProgressDialog;
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

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private TextView Info;
    private Button Login;
    private int counter=5;
    private TextView userRegistration;
    private FirebaseAuth firebaseAuth;
    private TextView forgotPassword;
    //    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name=(EditText) findViewById(R.id.etName);
        Password=(EditText) findViewById(R.id.etPassword);
        Info=(TextView)findViewById(R.id.tvInfo);
        Login=(Button)findViewById(R.id.btnLogin);
        Info.setText("Initially No of attempst:5");
        userRegistration=(TextView)findViewById(R.id.tvRegister);
        firebaseAuth=FirebaseAuth.getInstance();
        forgotPassword=(TextView)findViewById(R.id.tvForgotPassword);
        //progressBar=new ProgressBar(this);
        progressDialog=new ProgressDialog(this);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user!=null){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });

        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });
    }

    private void validate(String userName, String password) {
        progressDialog.setMessage("You are being verified");
        firebaseAuth.signInWithEmailAndPassword(userName,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){

                    //Toast.makeText(MainActivity.this,"Login successfull",Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(MainActivity.this,SecondActivity.class));
                    checkEmailVerification();
                }
                else {
                    Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No. of attempts remaining:" + counter);
                    if(counter==0){
                        Login.setEnabled(false);
                    }
                }
            }
        });
    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailFlag=firebaseUser.isEmailVerified();

        if(emailFlag){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));

        }
        else{
            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
