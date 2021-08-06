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
import com.gautham.chatapp.Model.ChatList;
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

public class ChatsFragment extends Fragment {
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    FirebaseUser fuser;
    DatabaseReference ref;
    private List<ChatList> userList;
    RecyclerView rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_chats, container, false);
        View view=inflater.inflate(R.layout.fragment_chats,container,false);
        rv=view.findViewById(R.id.recycler_view2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        userList=new ArrayList<>();
        ref= FirebaseDatabase.getInstance().getReference("ChatList").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    ChatList chatlist=snapshot.getValue(ChatList.class);
                    userList.add(chatlist);
                }
                chatlists();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
    private void chatlists() {
        mUsers=new ArrayList<>();
        ref=FirebaseDatabase.getInstance().getReference("Myusers");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Users user=snapshot.getValue(Users.class);
                    for(ChatList chatList:userList){
                        if(user.getId().equals(chatList.getId())){
                          mUsers.add(user);
                        }
                    }
                }
                userAdapter=new UserAdapter(getContext(),mUsers,true);
                rv.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}