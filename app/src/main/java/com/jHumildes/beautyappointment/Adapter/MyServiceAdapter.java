package com.jHumildes.beautyappointment.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.beautyappointment.R;
import com.jHumildes.beautyappointment.Common.Common;
import com.jHumildes.beautyappointment.Interface.IRecyclerItemSelectedListener;
import com.jHumildes.beautyappointment.Model.Services;

import java.util.ArrayList;
import java.util.List;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.MyViewHolder> {

    Context context;
    List<Services>servicesList;
    List<CardView> cardViewList; //professional
    LocalBroadcastManager localBroadcastManager;


    public MyServiceAdapter(Context context, List<Services> servicesList){
        this.context = context;
        this.servicesList = servicesList;

        cardViewList = new ArrayList<>();

        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_services,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_services_name.setText(servicesList.get(position).getName());
       // holder.txt_services_price.setText(servicesList.get(position).getName());

        if (!cardViewList.contains(holder.card_services))
            cardViewList.add(holder.card_services);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int pos) {
                //set color white bg for all card not to be selected
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlue));

                //set Selected BG for only selected item
                holder.card_services.setCardBackgroundColor(context.getResources()
                .getColor(R.color.colorLightPink));

                //send Broadcast to tell BookingActivity enable Button next
                Intent intent = new Intent(Common.ENABLE_NEXT_BUTTON_KEY);
                intent.putExtra(Common.SERVICES_STORE_KEY,servicesList.get(pos));
                intent.putExtra(Common.KEY_FASE,1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return servicesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_services_name, txt_services_price;
        CardView card_services;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_services = (CardView)itemView.findViewById(R.id.card_services);
            txt_services_name = (TextView)itemView.findViewById(R.id.txt_services_name);
            //txt_services_price = (TextView)itemView.findViewById(R.id.txt_services_price);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());

        }
    }
}
