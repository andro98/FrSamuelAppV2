package com.example.frsamuel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFrag extends Fragment {

    private RecyclerView postList;
    private List<Posts> post_List_data;
    private PostsRecycleAdapter postAdap;

    private FirebaseFirestore firebasefirestor;
    public HomeFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        post_List_data = new ArrayList<>();
        postList = view.findViewById(R.id.Post_view);

        postAdap = new PostsRecycleAdapter(post_List_data);
        postList.setLayoutManager(new LinearLayoutManager(getActivity()));
        postList.setAdapter(postAdap);


        firebasefirestor = FirebaseFirestore.getInstance();
        firebasefirestor.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Posts post = doc.getDocument().toObject(Posts.class);
                        post_List_data.add(post);
                        postAdap.notifyDataSetChanged();
                    }
                }
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

}
