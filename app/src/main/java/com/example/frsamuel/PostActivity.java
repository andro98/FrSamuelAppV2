package com.example.frsamuel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.lang.*;
import java.util.*;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {

    private Toolbar PostToolbar;
    private EditText postText;
    private Button AddPostBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore fbs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();
        fbs = FirebaseFirestore.getInstance();

        postText = (EditText) findViewById(R.id.Post_Text);
        AddPostBtn = (Button)findViewById(R.id.Add_btn);
        AddPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = postText.getText().toString();
                if(!TextUtils.isEmpty(post))
                {
                    Map<String,Object> postMap = new HashMap<>();
                    postMap.put("post", post);
                    postMap.put("user_id",mAuth.getCurrentUser().getUid());
                    postMap.put("time", FieldValue.serverTimestamp());

                    fbs.collection("Posts").add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(PostActivity.this, "تم", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(PostActivity.this,MainActivity.class);
                                startActivity(mainIntent);
                                finish();
                            }else{

                            }
                        }
                    });
                }else{
                    Toast.makeText(PostActivity.this, "اكتب شىء", Toast.LENGTH_SHORT).show();
                }
            }
        });
        PostToolbar = findViewById(R.id.Post_toolbar);
        setSupportActionBar(PostToolbar);
        getSupportActionBar().setTitle("إضافة بوست جديد");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
