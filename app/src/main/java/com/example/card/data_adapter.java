package com.example.card;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class data_adapter extends RecyclerView.Adapter<data_adapter.data_viewholder>{

    private Context context;
    private List<model>list;

    data_adapter(Context context1,List<model>list1){
        this.list=list1;
        this.context=context1;
    }

    @NonNull
    @Override
    public data_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.card_layout,parent,false);
        data_viewholder holder = new data_viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull data_viewholder holder, int position) {
        final model m =  list.get(position);
        holder.tcase.setText(m.getTotalCases());
        holder.rcase.setText(m.getTotalRecovered());
        holder.dcase.setText(m.getTotalDeaths());
        holder.ccase.setText(m.getTotalCurrentCases());
        holder.mstate.setText(m.getStateName());
        /*holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Clicked On "+m.getStateName(),Toast.LENGTH_SHORT).show();
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class data_viewholder extends RecyclerView.ViewHolder {

        TextView tcase,ccase,dcase,rcase,mstate;
        //LinearLayout layout;
        public data_viewholder(@NonNull View itemView) {
            super(itemView);
            tcase=(TextView) itemView.findViewById(R.id.total);
            ccase=(TextView)itemView.findViewById(R.id.cur);
            dcase=(TextView) itemView.findViewById(R.id.totalDeath);
            rcase=(TextView) itemView.findViewById(R.id.recovered);
            mstate=(TextView)itemView.findViewById(R.id.state);
            //layout=(LinearLayout)itemView.findViewById(R.id.linearlayout);
        }
    }
    void filerList(ArrayList<model>fileredList){
        list=fileredList;
        notifyDataSetChanged();
    }
}
