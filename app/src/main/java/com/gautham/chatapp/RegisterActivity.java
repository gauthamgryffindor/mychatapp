package com.gautham.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gautham.chatapp.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText username,email,password,confirm;
    Button signup;
    FirebaseAuth auth;
 String  usert,emailt,passwordt,confirmt;
    DatabaseReference myref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username=findViewById(R.id.etuser);
        email=findViewById(R.id.etemail);
        password=findViewById(R.id.etpass);
        confirm=findViewById(R.id.etconfirm);
        signup=findViewById(R.id.btnsignup);
        auth=FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usert = username.getText().toString();
                emailt = email.getText().toString();
                passwordt = password.getText().toString();
                confirmt = confirm.getText().toString();
                if (usert.isEmpty() || emailt.isEmpty() || passwordt.isEmpty() || confirmt.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "enter all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean a = passwordt.equals(confirmt);
                    if (a == false) {
                        Toast.makeText(RegisterActivity.this, "password is not matching", Toast.LENGTH_LONG).show();
                    } else {
                        Registernow(usert, emailt, passwordt);
                    }
                }
            }
            });




    }

    private void Registernow(final String username,String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println("working");
                        if (task.isSuccessful()) {
                            System.out.println("working2");

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();
                            myref = FirebaseDatabase.getInstance().getReference("Myusers")
                                    .child(userid);
                            System.out.println("working3");
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");
                            hashMap.put("status", "offline");
                            System.out.println("working4");

                            myref.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        System.out.println("working5");

                                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, "not working", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this, "email already taken or password is not strong", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
    }
    }