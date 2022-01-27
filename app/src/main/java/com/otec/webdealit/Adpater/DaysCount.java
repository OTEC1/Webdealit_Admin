package com.otec.webdealit.Adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.otec.webdealit.R;
import com.otec.webdealit.model.ViewCount;

import java.util.List;

public class DaysCount  extends RecyclerView.Adapter<DaysCount.Myholder> {



    private List<ViewCount>  list;
    private Context context;


    public DaysCount(List<ViewCount> list, Context context) {
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
            holder.date.setText(list.get(position).getDate());
            holder.date.setText(String.valueOf(list.get(position).getCount()));
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
