package com.example.frsamuel;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFrag extends Fragment {

    private RecyclerView postList;
    private List<Posts> post_List_data;
    private PostsRecycleAdapter postAdap;
    private Boolean firstPage = true;

    private DocumentSnapshot lastVisible;

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

        postList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Boolean reachedButton = !recyclerView.canScrollVertically(1);

                if(reachedButton){
                    LoadMorePost();
                }
            }
        });

        Query newQuery = firebasefirestor.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(3);
        newQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if(firstPage) {
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                }
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String postID = doc.getDocument().getId();

                        Posts post = doc.getDocument().toObject(Posts.class).withID(postID);
                        if(firstPage){
                        post_List_data.add(post);}
                        else{
                            post_List_data.add(0,post);
                        }
                        postAdap.notifyDataSetChanged();
                    }
                }
                firstPage = false;

            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    public void LoadMorePost()
    {
        Query newQuery = firebasefirestor.collection("Posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .startAfter(lastVisible)
                .limit(3);

        newQuery.addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){
                lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        String postID = doc.getDocument().getId();

                        Posts post = doc.getDocument().toObject(Posts.class).withID(postID);
                        post_List_data.add(post);
                        postAdap.notifyDataSetChanged();
                    }
                }
            }}
        });
    }

}
