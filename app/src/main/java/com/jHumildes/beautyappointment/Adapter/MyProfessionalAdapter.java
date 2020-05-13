package com.jHumildes.beautyappointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Interface.IRecyclerItemSelectedListener;
import com.jHumildes.beautyappointment.Model.Professional;

import java.util.ArrayList;
import java.util.List;

public class MyProfessionalAdapter extends RecyclerView.Adapter<MyProfessionalAdapter.MyViewHolder> {

    Context context;
    List<Professional> professionalList;
    List<CardView>cardViewList;
    LocalBroadcastManager localBroadcastManager; //to enable single choice

    public MyProfessionalAdapter(Context context, List<Professional> professionalList) {
        this.context = context;
        this.professionalList = professionalList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
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

        if (!cardViewList.contains(holder.card_professional))
            cardViewList.add(holder.card_professional);//to enable a single choice

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set Bg for all item no choice
                for (CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources()
                            .getColor(R.color.colorBlue));
                }
                //set bg for choice
                holder.card_professional.setCardBackgroundColor(
                        context.getResources()
                        .getColor(R.color.colorLightPink)
                );
                //send localBroadcast to enable Button next
                Intent intent = new Intent(Common.ENABLE_NEXT_BUTTON_KEY);
                intent.putExtra(Common.KEY_PROFESSIONAL_SELECTED,professionalList.get(pos));
                intent.putExtra(Common.KEY_FASE,2);
                localBroadcastManager.sendBroadcast(intent);
            }

        });

    }

    @Override
    public int getItemCount() {
        return professionalList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_professional_name;
        RatingBar ratingBar;
        CardView card_professional;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_professional = (CardView)itemView.findViewById(R.id.card_professional);
            txt_professional_name = (TextView)itemView.findViewById(R.id.txt_professional_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_professional);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }

}
