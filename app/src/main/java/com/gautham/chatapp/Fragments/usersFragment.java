package com.gautham.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gautham.chatapp.Adapter.UserAdapter;

import com.gautham.chatapp.Model.Users;
import com.gautham.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class usersFragment extends Fragment {
    private RecyclerView rv;
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    public usersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);
        rv=view.findViewById(R.id.recyclerview);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers=new ArrayList<>();
        ReadUsers();
        return view;
    }
    private  void ReadUsers(){
        final FirebaseUser f= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference d= FirebaseDatabase.getInstance().getReference("Myusers");
        d.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    //Users user=snapshot.getValue(Users.class);
                    Users user=snapshot.getValue(Users.class);
                    assert user != null;
                    System.out.println("the dgh"+user.getUsername());
                    System.out.println("the user"+f.getUid());
                    System.out.println("the users "+user.getId());
                    if(!user.getId().equals(f.getUid())){

                        mUsers.add(user);
                    }
                    userAdapter=new UserAdapter(getContext(),mUsers,false);
                    rv.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}