package com.triplea.kankyo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
        SimpleDateFormat dateFormatter = new SimpleDateFormat("E, y-M-d");

        holder.rName.setText(user.reportName);
        holder.rDate.setText(dateFormatter.format(user.reportDate));

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ViewReport.class);
                intent.putExtra("name", user.reportName);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rName, rDate;
        ConstraintLayout item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rName = itemView.findViewById(R.id.report_name);
            rDate = itemView.findViewById(R.id.report_date);
            item = itemView.findViewById(R.id.card_view);
        }
    }
}
