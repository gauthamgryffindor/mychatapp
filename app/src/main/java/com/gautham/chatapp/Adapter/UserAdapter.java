package com.gautham.chatapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gautham.chatapp.MessageActivity;
import com.gautham.chatapp.Model.Users;
import com.gautham.chatapp.R;
import android.content.Context;
//import com.google.firebase.database.core.Context;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context context;
    private List<Users> mUsers;
    private boolean ischat;
    public UserAdapter(Context context, List<Users> mUsers,boolean ischat) {
        this.context = context;
        this.mUsers = mUsers;
        this.ischat=ischat;
    }

    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
        view = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
       final Users users=mUsers.get(position);

       holder.username.setText(users.getUsername());
       if(users.getImageURL().equals("default")){
           holder.imageview.setImageResource(R.mipmap.ic_launcher_round);
       }else{
           Glide.with(context).load(users.getImageURL()).into(holder.imageview);

       }
       if(ischat){
           if((users.getStatus()).equals("online")) {
              // holder.statuson.setVisibility(View.VISIBLE);
               holder.statuson.setText("Online");
           }
           else {
               //holder.statuson.setVisibility(View.VISIBLE);
               holder.statuson.setText("Offline");
           }
       }
       else {
           holder.statuson.setVisibility(View.GONE);

       }

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               Intent i=new Intent(context, MessageActivity.class);
               i.putExtra("userid",users.getId());
               context.startActivity(i);

           }
       });
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username,statuson;
        public ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.fname);
            imageview=itemView.findViewById(R.id.imageView3);
            statuson=itemView.findViewById(R.id.textView2);




        }
    }
}
