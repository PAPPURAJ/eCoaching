package com.blogspot.rajbtc.onlineclass.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ClassUserAdapter extends RecyclerView.Adapter<ClassUserAdapter.ClassUserViewHolder> {

    private Context context;
    private ArrayList<ClassData> arrayList;

    public ClassUserAdapter(Context context,ArrayList<ClassData> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public ClassUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassUserViewHolder(LayoutInflater.from(context).inflate(R.layout.single_class_recy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassUserViewHolder holder, final int position) {

        holder.timeAndDuration.setText(arrayList.get(position).getStartTime()+arrayList.get(position).getDuration());
        holder.subName.setText(arrayList.get(position).getClassName());
        holder.teacherName.setText(arrayList.get(position).getTeacher());

        holder.joinClassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+arrayList.get(position).getLink()));
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(context);
                ab.setTitle("Are you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.get(position).getDocID().removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context,"Data removed!",Toast.LENGTH_LONG).show();
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).setNegativeButton("No",null).show();

                return  true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ClassUserViewHolder extends RecyclerView.ViewHolder{

        TextView joinClassTv,timeAndDuration,teacherName,subName;

        public ClassUserViewHolder(@NonNull View itemView) {
            super(itemView);
            joinClassTv=itemView.findViewById(R.id.single_class_recy_joinClassTv);
            timeAndDuration=itemView.findViewById(R.id.single_class_recy_timeAndDurationTv);
            teacherName=itemView.findViewById(R.id.single_class_recy_teacherTv);
            subName=itemView.findViewById(R.id.single_class_recy_subTv);
        }
    }
}
