package com.example.binge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.binge.Models.Users;
import com.example.binge.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Signing Up");
        progressDialog.setMessage("Building your account...");

        //////////////////////////////////////
        //////////// Configure Google Sign In
        /////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /////////////////////////////////////////////
        ////    if user already logged in   ///////
        /////////////////////////////////////////
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // No user is signed in
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
        }


        //////////////////////////////////////////
        ///// Manual Signup Button Click
        /////////////////////////////////////////
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //If Username is Empty
                if(binding.etUsername.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Username Required",Toast.LENGTH_SHORT).show();
                    return;
                }
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
                auth.createUserWithEmailAndPassword(binding.etEmail.getText().toString(),binding.etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            Users users = new Users(binding.etUsername.getText().toString(),binding.etEmail.getText().toString(),binding.etPassword.getText().toString());
                            String id = task.getResult().getUser().getUid();

                            database.getReference().child("Users").child(id).setValue(users);

                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });


        ///////////////////////////
        ///     Move to Login Page
        ////////////////////////////
        binding.tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        /////////////////////////////////////
        //      Google Signup Button Clicked
        ////////////////////////////////////
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                FirebaseAuth.getInstance().signOut();   // Singing out just for assurance
                signIn();       //Calling singin function
            }
        });
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int RC_SIGN_IN = 65;
    private void signIn() {
//        FirebaseAuth.getInstance().signOut();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        ////////////////////////
        // If already Logged in
        /////////////////////////
        if(currentUser != null)
        {
            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
            startActivity(intent);
        }
//        updateUI(currentUser);
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Users users = new Users();
                            users.setEmail(user.getEmail());
                            users.setUserid(user.getUid());
                            users.setUsername(user.getDisplayName());
                            users.setProfilepic(user.getPhotoUrl().toString());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
                        }
                    }
                });
    }
}