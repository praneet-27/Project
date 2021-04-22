package com.example.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button b1;
    EditText e1,e2,e3;
    ProgressDialog progressDialog;
    ImageButton imageButton;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        b1 = findViewById(R.id.signup);
        e1 = findViewById(R.id.userid);
        e2 = findViewById(R.id.password);
        e3=findViewById(R.id.conpass);
        imageButton=findViewById(R.id.gmail);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
       mGoogleSignInClient = GoogleSignIn.getClient(signup.this, gso);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                String cpassword=e3.getText().toString();
                if(TextUtils.isEmpty(email)) {
                    e1.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    e2.setError("Password is required");
                    return;
                }
                if(password.length()<=6)
                {
                    e2.setError("Password length should be >6");
                    return;
                }
                if(!password.equals(cpassword))
                {
                    e3.setError("Password and confirm password should match ");
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            init();
                            showPDialog();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(signup.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(signup.this,home.class);
                                    startActivity(i);
                                    progressDialog.dismiss();
                                }
                            },2000);

                            Toast.makeText(signup.this, "User created", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(signup.this,home.class);
                            startActivity(i);
                        }
                        else
                        {
                            Toast.makeText(signup.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            startActivity(new Intent(signup.this,home.class));
        }
        catch (ApiException e)
        {
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(signup.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(signup.this, home.class));
        }
        super.onStart();
    }
    public void init(){
        progressDialog=new ProgressDialog(signup.this);
    }
    public void showPDialog(){
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );
    }

}