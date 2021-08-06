package com.gautham.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText username,password;
    Button signin,signup;
    FirebaseAuth auth;
    FirebaseUser firebaseuser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseuser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseuser != null){
            Intent i= new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username=findViewById(R.id.lemail);
        password=findViewById(R.id.etlpass);
        signin=findViewById(R.id.sign);
        signup=findViewById(R.id.kk);
        auth=FirebaseAuth.getInstance();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailtext=username.getText().toString();
                String passtext=password.getText().toString();

                if(TextUtils.isEmpty(emailtext)||TextUtils.isEmpty(passtext)){
                    Toast.makeText(LoginActivity.this,"enter all fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    auth.signInWithEmailAndPassword(emailtext,passtext)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                  if(task.isSuccessful()){
                                      Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                      i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                      startActivity(i);
                                      finish();
                                  }
                                  else{
                                      Toast.makeText(LoginActivity.this,"invalid password or email id",Toast.LENGTH_SHORT).show();
                                  }
                                }
                            });
                }

            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent w= new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(w);
            }
        });
    }
}