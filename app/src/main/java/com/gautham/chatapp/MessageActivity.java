package com.gautham.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.gautham.chatapp.Adapter.messageAdapter;
import com.gautham.chatapp.Model.Chats;
import com.gautham.chatapp.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    TextView username;
    ImageView imageview;
    FirebaseUser fuser;
    DatabaseReference ref;
    Intent intent;
    RecyclerView rv, rv1;
    EditText msg;
    ToggleButton sendbtn;
    messageAdapter messageadapter;
    List<Chats> mchats;
    String userid;
    ValueEventListener seenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        imageview = findViewById(R.id.imageView1);
        username = findViewById(R.id.usname);
        sendbtn = findViewById(R.id.send);
        msg = findViewById(R.id.textsend);
        rv1 = findViewById(R.id.recycler_view);
        rv1.setHasFixedSize(true);
        System.out.println("test");
        LinearLayoutManager l = new LinearLayoutManager(getApplicationContext());
        l.setStackFromEnd(true);
        rv1.setLayoutManager(l);
        intent = getIntent();


        userid = intent.getStringExtra("userid");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Myusers").child(userid);
        System.out.println("test1");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")) {
                    imageview.setImageResource(R.mipmap.ic_launcher_round);

                } else {
                    Glide.with(MessageActivity.this).load(user.getImageURL()).into(imageview);
                }
                System.out.println("test2");


                readMessage(fuser.getUid(), userid, user.getImageURL());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgs = msg.getText().toString();
                if (msgs.equals("")) {
                    Toast.makeText(MessageActivity.this, "please type some text", Toast.LENGTH_LONG).show();

                } else {
                    sendmsg(fuser.getUid(), userid, msgs);
                }
                msg.setText("");
            }
        });
       seenmessage(userid);

    }
    private  void seenmessage(final String userid){
        ref=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chats chat=snapshot.getValue(Chats.class);
                    if(chat.getReceiver().equals(fuser.getUid())&&chat.getSender().equals(userid)){
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("isseen",true);
                        snapshot.getRef().updateChildren(map);


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendmsg(String sender, String receiver, String message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashmap = new HashMap<>();
        hashmap.put("sender", sender);
        hashmap.put("Receiver", receiver);
        hashmap.put("message", message);
        hashmap.put("isseen",false);
        ref.child("Chats").push().setValue(hashmap);

        final DatabaseReference chatref=FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(fuser.getUid())
                .child(userid);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    chatref.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void readMessage(final String myid, final String userid, final String imageurl) {
        mchats = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Chats").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mchats.clear();
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Chats chat = snapshot.getValue(Chats.class);

                            if (( chat.getReceiver().equals(myid) &&chat.getSender().equals(userid)) ||
                                    ( chat.getReceiver().equals(userid)&& chat.getSender().equals(myid))) {
                                mchats.add(chat);

                            }

                            messageadapter = new messageAdapter(MessageActivity.this, mchats,imageurl,true);
                            rv1.setAdapter(messageadapter);
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void checkstatus(String status){
        ref=FirebaseDatabase.getInstance().getReference("Myusers").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        ref.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkstatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        ref.removeEventListener(seenListener);
        checkstatus("offline");
    }

}


