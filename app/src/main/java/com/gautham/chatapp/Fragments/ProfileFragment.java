package com.gautham.chatapp.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gautham.chatapp.Model.Users;
import com.gautham.chatapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
TextView text;
ImageView image;
StorageReference storagereference;
private static final int image_request=1;
private Uri imageuri;
private StorageTask uploadtask;
DatabaseReference reference;
FirebaseUser fuser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        image=view.findViewById(R.id.profile_image2);
        text=view.findViewById(R.id.usernamesss);
        storagereference= FirebaseStorage.getInstance().getReference("uploads");
       fuser= FirebaseAuth.getInstance().getCurrentUser();
       reference= FirebaseDatabase.getInstance().getReference("Myusers").child(fuser.getUid());
       reference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               Users user=dataSnapshot.getValue(Users.class);
               text.setText(user.getUsername());
               if(user.getImageURL().equals("default")){
                   image.setImageResource(R.mipmap.ic_launcher_round);
               }else{
                   Glide.with(getContext()).load(user.getImageURL()).into(image);
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
       image.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Selectimage();
           }


       });

       return view;
    }
    private void Selectimage() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,image_request);



    }
    private String getFileExtention(Uri uri){
        ContentResolver contentResolver=getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadimage(){
        final ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("uploading");
        progressDialog.show();
        if(imageuri!=null){
            final StorageReference fileReference=storagereference.child(System.currentTimeMillis()+"."+getFileExtention(imageuri));

        uploadtask=fileReference.putFile(imageuri);
        uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot>task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()) {
                    Uri downloaduri= (Uri) task.getResult();
                    String muri=downloaduri.toString();
                    reference=FirebaseDatabase.getInstance().getReference("Myusers").child(fuser.getUid());
                    HashMap<String,Object> map=new HashMap<>();
                    map.put("imageURL",muri);
                    reference.updateChildren(map);
                    progressDialog.dismiss();

                }
                else {
                    Toast.makeText(getContext(),"Failed",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });
        }else{
            Toast.makeText(getContext(),"no image selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == image_request && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageuri = data.getData();
            if (uploadtask != null && uploadtask.isInProgress()) {
                Toast.makeText(getContext(), "upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadimage();
            }
        }
    }
}