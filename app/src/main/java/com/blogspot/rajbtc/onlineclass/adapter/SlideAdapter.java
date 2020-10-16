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
import com.blogspot.rajbtc.onlineclass.dataclass.SlideDataforAdapter;

import java.util.ArrayList;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private Context context;
    private ArrayList<SlideDataforAdapter> arrayList;

    public SlideAdapter(Context context,ArrayList<SlideDataforAdapter> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SlideViewHolder(LayoutInflater.from(context).inflate(R.layout.single_slide_recy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, final int position) {
        holder.teacherName.setText(arrayList.get(position).getGivenBy());
        holder.slideName.setText(arrayList.get(position).getSlideName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

    class SlideViewHolder extends RecyclerView.ViewHolder{
        TextView slideName, teacherName;
        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            slideName=itemView.findViewById(R.id.singleSlideRecySlideNameTv);
            teacherName=itemView.findViewById(R.id.singleSlideRecyTcrNmTv);
        }
    }
}
