package com.example.binge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binge.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().hide();

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(SignInActivity.this);
        progressDialog.setTitle("Login In");
        progressDialog.setMessage("Wait a moment...");



        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();


        //if user already logged in
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {

            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }


        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If email is empty
                if(binding.etEmail.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Email Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                //if password is empty
                if(binding.etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Password Required",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                auth.signInWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            if(binding.etEmail.getText().toString().equals("bingeadmin@gmail.com"))
                            {
                                Toast.makeText(SignInActivity.this, "Welcome Admin", Toast.LENGTH_SHORT).show();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://console.firebase.google.com/u/1/project/binge-dab29/database/binge-dab29-default-rtdb/data"));
                                startActivity(browserIntent);
                            }
                            else
                            {
                                Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                                startActivity(intent );
                            }
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this,  task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);

                startActivity(intent);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}