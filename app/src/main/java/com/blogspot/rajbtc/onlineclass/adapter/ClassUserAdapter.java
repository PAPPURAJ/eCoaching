package com.blogspot.rajbtc.onlineclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;

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
