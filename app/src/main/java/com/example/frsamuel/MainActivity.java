package com.example.frsamuel;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mainToolbar;
    private TextView userData;
    private String adminMac;
    private Person p;
    private Button post;
    private BottomNavigationView mainBottomNav;

    private HomeFrag homeFrag;
    private InboxFrag inboxFrag;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();

       // userData = (TextView) findViewById(R.id.UserData);

        mainToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setTitle("لنتواصل");

        if(mAuth.getCurrentUser() != null){

            mainBottomNav = findViewById(R.id.MainButtonNav);
            homeFrag = new HomeFrag();
            inboxFrag = new InboxFrag();
            replaceFragment(homeFrag);


            post = (Button) findViewById(R.id.AddPost);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent PostInt = new Intent(MainActivity.this,PostActivity.class);
                    startActivity(PostInt);
                }
            });

            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId())
                    {
                        case R.id.Buttom_Home:
                            replaceFragment(homeFrag);
                            return true;
                        case R.id.Buttom_Inbox:
                            replaceFragment(inboxFrag);
                            return true;
                        default:
                            return false;
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged in
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser == null)
        {
            sendToLogin();
        }
        p = new Person();
        String user_id = mAuth.getCurrentUser().getUid();
        DocumentReference dr = mFire.collection("Users").document(user_id);
        dr.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        p.setName(task.getResult().getString("name"));
                        p.setAddress(task.getResult().getString("address"));
                        p.setPhone(task.getResult().getString("phone"));
                      //  userData.setText(p.getName() +"\n" + p.getPhone() + "\n" + p.getAddress() +"\n" );
                    }
                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return false;

        }
    }

    public void logOut()
    {
        mAuth.signOut();
        sendToLogin();
    }

    private void sendToLogin()
    {
        Intent LoginIntent = new Intent (MainActivity.this, LoginActivity.class);
        startActivity(LoginIntent);
        finish(); //user cannot go back until log in
    }

    private void replaceFragment(Fragment fragment)
    {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.replace(R.id.MainContainer,fragment);
        fragTrans.commit();
    }


}
