package com.jHumildes.beautyappointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.BookingActivity;
import com.jHumildes.beautyappointment.Interface.ItemClickListener;
import com.jHumildes.beautyappointment.Model.ModelRow;
import com.jHumildes.beautyappointment.RowHolder;

import java.util.ArrayList;

public class MyRowAdapter extends RecyclerView.Adapter<RowHolder> {

    Context c;
    ArrayList<ModelRow> modelRows;

    public MyRowAdapter(Context c, ArrayList<ModelRow> modelRows) {
        this.c = c;
        this.modelRows = modelRows;
    }

    @NonNull
    @Override
    public RowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,null);
        return new RowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RowHolder holder, int position) {
        holder.mtitle.setText(modelRows.get(position).getTitle());
        holder.imageView.setImageResource(modelRows.get(position).getImg());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {

               Intent intent = new Intent(c, BookingActivity.class);
               c.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelRows.size();
    }


}
