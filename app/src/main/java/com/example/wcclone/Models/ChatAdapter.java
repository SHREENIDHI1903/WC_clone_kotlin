package com.example.wcclone.Models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wcclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter{
    ArrayList<MessageMOdel> messageMOdels;
    Context context;
    String recId;

    public ChatAdapter(ArrayList<MessageMOdel> messageMOdels, Context context, String recId) {
        this.messageMOdels = messageMOdels;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_VIEW_TYPE=1;
    int RECEIVER_VIEW_TYPE=2;
    public ChatAdapter(ArrayList<MessageMOdel> messageMOdels, Context context) {
        this.messageMOdels = messageMOdels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       if(viewType==SENDER_VIEW_TYPE){
           View view= LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
           return new SenderViewHolder(view);
       }
       else{
           View view= LayoutInflater.from(context).inflate(R.layout.sample_reciever,parent,false);
           return new RecieverViewHolder(view);
       }
    }
    @Override
    public int getItemViewType(int position){
        if(messageMOdels.get(position).getuID().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageMOdel messageMOdel=messageMOdels.get(position);
    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want 2 del")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            String senderRoom=FirebaseAuth.getInstance().getUid()+recId;
                            database.getReference().child("chats").child(senderRoom)
                                    .child(messageMOdel.getMessageID())
                                    .setValue(null);

                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();




            return false;
        }
    });
        if(holder.getClass()==SenderViewHolder.class)
        {
            ((SenderViewHolder)holder).senderMsg.setText(messageMOdel.getMessage());

            Date date=new Date(messageMOdel.getTimestamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
            String strDate=simpleDateFormat.format(date);
            ((SenderViewHolder)holder).senderTime.setText(strDate.toString());
        }
        else{
            ((RecieverViewHolder)holder).receiverMsg.setText(messageMOdel.getMessage());
            Date date=new Date(messageMOdel.getTimestamp());
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm:ss");
            String strDate=simpleDateFormat.format(date);
            ((RecieverViewHolder)holder).receiverTime.setText(strDate.toString());
        }

    }

    @Override
    public int getItemCount() {
        return messageMOdels.size();
    }

    public class RecieverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg,receiverTime;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg=itemView.findViewById(R.id.receiverText);
            receiverTime=itemView.findViewById(R.id.receiverTime);

        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView senderMsg,senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg=itemView.findViewById(R.id.senderText);
            senderTime=itemView.findViewById(R.id.senderTime);


        }
    }
}
