package com.otec.webdealit.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otec.webdealit.R;

import java.util.List;
import java.util.Map;

public class DaysCount  extends RecyclerView.Adapter<DaysCount.Myholder> {



    private List<Map<String,Object>>  list;
    private Context context;


    public DaysCount(List<Map<String,Object>>  list, Context context) {
        this.list = list;
        this.context = context;
    }



    @NonNull
    @Override
    public DaysCount.Myholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  DaysCount.Myholder holder, int position) {
            holder.date.setText("Date: "+list.get(position).get("date").toString());
            holder.count.setText("Visit: "+(list.get(position).get("count").toString()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    public class Myholder extends RecyclerView.ViewHolder {

       private TextView date,count;

        public Myholder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            count = itemView.findViewById(R.id.visit);
        }
    }
}
