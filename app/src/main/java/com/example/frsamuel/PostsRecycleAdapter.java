package com.example.frsamuel;

import android.support.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostsRecycleAdapter extends RecyclerView.Adapter<PostsRecycleAdapter.ViewHolder>{
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFire;

    public List<Posts> post_List_data;
    public PostsRecycleAdapter(List<Posts> post_List_data)
    {
        this.post_List_data = post_List_data;
        mAuth = FirebaseAuth.getInstance();
        mFire = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item,viewGroup ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String postView = post_List_data.get(i).getPost();
        String nameView = post_List_data.get(i).getUser_id();
        mFire.collection("Users").document(nameView).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().exists())
                    {
                        String  name = task.getResult().getString("name").toString();
                        setHolder(viewHolder,postView,name);
                    }
                }
            }
        });

    }

    public void setHolder(ViewHolder holder, String postView, String name)
    {
        holder.setpostText(postView);
        holder.setUser_idText(name);
    }

    @Override
    public int getItemCount() {
        return post_List_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
       private View mView;

        private TextView postView;
        private TextView user_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setpostText(String text){
            postView = mView.findViewById(R.id.PostText);
            postView.setText(text);
        }

        public void setUser_idText(String id)
        {
            user_id = mView.findViewById(R.id.UserName_ListView);
            user_id.setText(id);
        }
    }
}
