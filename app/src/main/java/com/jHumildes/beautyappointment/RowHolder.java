package com.jHumildes.beautyappointment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Interface.ItemClickListener;

public class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView imageView;
    public TextView mtitle;
    ItemClickListener itemClickListener;

    public RowHolder(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.image);
        this.mtitle = itemView.findViewById(R.id.title);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        this.itemClickListener.onItemClickListener(view,getLayoutPosition());

    }
    public void setItemClickListener(ItemClickListener ic){

        this.itemClickListener = ic;


    }
}
