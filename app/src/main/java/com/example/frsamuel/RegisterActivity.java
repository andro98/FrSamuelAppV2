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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.*;

public class RegisterActivity extends AppCompatActivity {

    private EditText regEmail;
    private EditText regPSW;
    private EditText regConfPSW;
    private EditText regName;
    private EditText regPhone;
    private EditText regAddress;
    private Button regBtn;
    private ProgressBar regProg;

    FirebaseAuth mAuth;
    StorageReference mStr;
    FirebaseFirestore mFire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mStr = FirebaseStorage.getInstance().getReference();
        mFire = FirebaseFirestore.getInstance();

        regEmail = (EditText) findViewById(R.id.RegEmailText);
        regPSW = (EditText) findViewById(R.id.RegPswText);
        regConfPSW = (EditText) findViewById(R.id.RegConfPswText);
        regName = (EditText) findViewById(R.id.RegNameText);
        regPhone = (EditText) findViewById(R.id.RegPhoneText);
        regAddress = (EditText) findViewById(R.id.RegAddressText);
        regBtn = (Button) findViewById(R.id.RegRegBtn);
        regProg = (ProgressBar) findViewById(R.id.RegProgress);

    }

    public void Submitmeth(View view) {
        final String Name =  regName.getText().toString();
        final String Email = regEmail.getText().toString();
        final String PSW = regPSW.getText().toString();
        final String confirm_PSW = regConfPSW.getText().toString();
        final String Phone = regPhone.getText().toString();
        final String Address = regAddress.getText().toString();

        if(!TextUtils.isEmpty(Name) &&!TextUtils.isEmpty(Email)&&!TextUtils.isEmpty(PSW)&&!TextUtils.isEmpty(confirm_PSW)&&!TextUtils.isEmpty(Phone)&&!TextUtils.isEmpty(Address))
        {
            if(PSW.equals(confirm_PSW))
            {
                regProg.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(Email,PSW).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            String userID = mAuth.getCurrentUser().getUid();
                            Map<String,String> user = new HashMap<>();
                            user.put("name",Name);
                            user.put("phone", Phone);
                            user.put("address",Address);
                            mFire.collection("Users").document(userID).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        sendToMain();
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, error,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            String e = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, e,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else
            {
                Toast.makeText(RegisterActivity.this, "تأكد من كلمة المرور",Toast.LENGTH_LONG).show();
            }
        }

    }

    private void sendToMain()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
