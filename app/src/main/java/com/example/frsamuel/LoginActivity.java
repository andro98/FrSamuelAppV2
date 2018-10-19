package com.example.frsamuel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private EditText LoginEmail;
    private EditText LoginPsw;
    private Button LoginBtn;
    private Button LoginRegBtn;
    private ProgressBar LoginProg;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        LoginEmail = (EditText) findViewById(R.id.LoginEmailText);
        LoginPsw = (EditText) findViewById(R.id.LoginPSWText);
        LoginBtn = (Button) findViewById(R.id.LoginLogBtn);
        LoginRegBtn = (Button) findViewById(R.id.LoginRegBtn);
        LoginProg = (ProgressBar) findViewById(R.id.LoginProgress);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) // if the user is already logged in
        {
            sendToMain();
        }
    }

    private void sendToMain()
    {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void LoginBtnMeth(View view) {
        String loginEmail = LoginEmail.getText().toString();
        String loginPsw = LoginPsw.getText().toString();

        if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPsw))
        {
            LoginProg.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(loginEmail,loginPsw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        sendToMain();
                    }else{
                        String errorMSG = task.getException().getMessage();
                        Toast.makeText(LoginActivity.this, "Error: "+ errorMSG, Toast.LENGTH_LONG).show();
                    }
                    LoginProg.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    public void Regmeth(View view) {
        Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
