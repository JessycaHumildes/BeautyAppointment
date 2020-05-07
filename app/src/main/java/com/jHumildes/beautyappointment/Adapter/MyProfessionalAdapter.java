package com.jHumildes.beautyappointment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Model.Professional;

import java.util.List;

public class MyProfessionalAdapter extends RecyclerView.Adapter<MyProfessionalAdapter.MyViewHolder> {

    Context context;
    List<Professional> professionalList;

    public MyProfessionalAdapter(Context context, List<Professional> professionalList) {
        this.context = context;
        this.professionalList = professionalList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_professional,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_professional_name.setText(professionalList.get(position).getName());
        holder.ratingBar.setRating((float)professionalList.get(position).getRating());

    }

    @Override
    public int getItemCount() {
        return professionalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_professional_name;
        RatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_professional_name = (TextView)itemView.findViewById(R.id.txt_professional_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_professional);
        }
    }

}
