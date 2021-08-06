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
import com.gautham.chatapp.Model.Chats;
import com.gautham.chatapp.Model.Chats;
import com.gautham.chatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Context;
//import com.google.firebase.database.core.Context;

import java.util.List;

public class messageAdapter extends RecyclerView.Adapter<messageAdapter.ViewHolder> {
    private Context context;
    private List<Chats>mchat;
    private String imgURL;
    private boolean isseen;
    public static final int msgtypeleft=0;
    public static final int msgtyperight=1;
    FirebaseUser fuser;

    public messageAdapter(Context context, List<Chats> mchat,String imgURL,boolean isseen) {
        this.context = context;
        this.mchat=mchat;
        this.imgURL=imgURL;
        this.isseen=isseen;
    }


    @Override
    public messageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == msgtyperight) {
           View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new messageAdapter.ViewHolder(view);
        } else {

            View view = LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false);
            return new messageAdapter.ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(@NonNull messageAdapter.ViewHolder holder, int position) {
     Chats chat=mchat.get(position);
     holder.showmsg.setText(chat.getMessage());

        if(imgURL.equals("default")){
            holder.profileimage.setImageResource(R.mipmap.ic_launcher_round);
        }else{
            Glide.with(context).load(imgURL).into(holder.profileimage);

        }
        //holder.seen.setVisibility(View.GONE);

       if(position == mchat.size()-1){
            System.out.println(mchat.size()-1);
            System.out.println(position);
            if(chat.isIsseen()){
                holder.seen.setText("seen");
            }

            else{
                holder.seen.setText("delivered");
            }

        }
        else{
            holder.seen.setVisibility(View.GONE);
        }

    }

   @Override
    public int getItemCount() {
        return mchat.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView showmsg,seen;
        public ImageView profileimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            showmsg=itemView.findViewById(R.id.showmsg);
           seen=itemView.findViewById(R.id.textView3);
            profileimage=itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {


            fuser= FirebaseAuth.getInstance().getCurrentUser();
            if(mchat.get(position).getSender().equals(fuser.getUid())){
                return msgtyperight;
            }else {
                return msgtypeleft;
            }
    }
}

