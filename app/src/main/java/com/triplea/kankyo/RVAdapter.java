package com.triplea.kankyo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> userArrayList;

    public RVAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User user = userArrayList.get(position);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d':' h:m a");

        holder.rName.setText(user.reportName);
        holder.rDate.setText(user.reportDate.toString());

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rName, rDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rName = itemView.findViewById(R.id.report_name);
            rDate = itemView.findViewById(R.id.report_date);
        }
    }
}
