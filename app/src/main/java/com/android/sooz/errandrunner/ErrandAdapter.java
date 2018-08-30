package com.android.sooz.errandrunner;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ErrandAdapter
        extends RecyclerView.Adapter<ErrandAdapter.MyViewHolder>{

    public List<Errands> errands;

    public ErrandAdapter(){
        errands = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate((R.layout.errand_item, viewGroup, false));
        MyViewHolder viewHolder = new MyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(errands.get(i));

    }

    @Override
    public int getItemCount() {
        return errands.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        View mView;
        TextView desc;
        CheckBox checkBox;
        Errands errand;

        public  MyViewHolder(@NonNull View itemView){
            super(itemView);
            mView = itemView;
            mView.setOnClickListener(this);

            //lecture 5:06 - 7
            desc= itemView.findViewById(R.id.description);
            checkBox = itemView.findViewById(R.id.isComplete);
        }

        public void bind (Errands errand){
            this.errand = errand;
            desc.setText(errand.description);
            checkBox.setChecked(errand.isComplete);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(mView.getContext(), MapsActivity.class);
            intent.putExtra("id", errand.id);

            mView.getContext().startActivity(intent);
        }
    }
}
