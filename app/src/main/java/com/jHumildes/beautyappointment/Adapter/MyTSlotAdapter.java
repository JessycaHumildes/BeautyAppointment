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
import com.jHumildes.beautyappointment.Model.CalendarTimeSlot;

import java.util.ArrayList;
import java.util.List;

public class MyTSlotAdapter extends RecyclerView.Adapter<MyTSlotAdapter.MyViewHolder> {



    Context context;
    List<CalendarTimeSlot> calendarTimeSlotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTSlotAdapter(Context context) {
        this.context = context;
        this.calendarTimeSlotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTSlotAdapter(Context context, List<CalendarTimeSlot> calendarTimeSlotList) {
        this.context = context;
        this.calendarTimeSlotList = calendarTimeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(context)
              .inflate(R.layout.layout_timeslot,parent,false);
      return  new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTSlotToString(position)).toString());
        if (calendarTimeSlotList.size() == 0)//If positions are available, show list
        {
            holder.card_timeslot.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlue));

            holder.txt_time_slot_desc.setText("I am Available");
            holder.txt_time_slot_desc.setTextColor(context.getResources()
            .getColor(android.R.color.black));

            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }
        else //if timeslot is full booked
        {

            for (CalendarTimeSlot calendarTimeSlotValue:calendarTimeSlotList)
            {
                //loop all time slot from server and set different color
                int slot = Integer.parseInt(calendarTimeSlotValue.getSlot().toString());
                if (slot == position)//if slot== position
                {
                    //settling time for all timeslot picked
                    //so basing on it, i can settle all remain card background without changing full tslot
                    holder.card_timeslot.setTag(Common.DISABLE_TAG);
                    holder.card_timeslot.setCardBackgroundColor(context.getResources().getColor(R.color.colorLightPink));

                    holder.txt_time_slot_desc.setText("Try Another Time");
                    holder.txt_time_slot_desc.setTextColor(context.getResources()
                            .getColor(R.color.colorLightPink));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
                    holder.card_timeslot.setCardBackgroundColor(context.getResources().getColor(R.color.colorBlue));

                }
            }

        }
        //ADD all time slot card to list
        if (!cardViewList.contains(holder.card_timeslot))
            cardViewList.add(holder.card_timeslot);
        //check if time slot is available

           holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
               @Override
               public void onItemSelectedListener(View view, int pos) {
                   //Loop All Card in to card list
                   for (CardView cardView:cardViewList)
                   {
                       if (cardView.getTag()==null) //only available time slot to be changed
                           cardView.setCardBackgroundColor(context.getResources()
                                   .getColor(R.color.colorBlue));
                   }
                   //selected card will change color
                   holder.card_timeslot.setCardBackgroundColor(context.getResources()
                           .getColor(R.color.colorLightPink));

                   //than send broadcast to enable button
                   Intent intent = new Intent(Common.ENABLE_NEXT_BUTTON_KEY);
                   intent.putExtra(Common.KEY_TIME_SLOT,position);//index time slot selected
                   intent.putExtra(Common.KEY_FASE,3);
                   localBroadcastManager.sendBroadcast(intent);

               }
           });
       }



    @Override
    public int getItemCount() {
        return Common.CALENDAR_TSLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_desc;
        CardView card_timeslot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_timeslot = (CardView) itemView.findViewById(R.id.card_timeslot);
            txt_time_slot = (TextView) itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_desc = (TextView) itemView.findViewById(R.id.txt_time_slot_desc);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecyclerItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
