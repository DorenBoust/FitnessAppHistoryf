package com.example.fitnessapp.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.user.HistoryExName;
import com.squareup.picasso.Picasso;

public class FitnessHistoryReciclerAdapter extends RecyclerView.Adapter<FitnessHistoryReciclerAdapter.HistoryHolder> {


    private LayoutInflater layoutInflater;
    private AllHistoryExName allHistoryExName;

    public FitnessHistoryReciclerAdapter(LayoutInflater layoutInflater, AllHistoryExName allHistoryExName) {
        this.layoutInflater = layoutInflater;
        this.allHistoryExName = allHistoryExName;
    }

    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = layoutInflater.inflate(R.layout.recyclerview_fitness_history_foldingcell, parent, false );
        return new HistoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {


        HistoryExName historyExName = allHistoryExName.getHistoryExNames().get(position);

        holder.exName.setText(historyExName.getExName());
//        holder.layer1FinalDate.setText(exerciseFullHistory.getLastDateUpdate());
        Picasso.get().load(historyExName.getExImage()).into(holder.exImage);

    }

    @Override
    public int getItemCount() {
        return allHistoryExName.getHistoryExNames().size();
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        private TextView exName;
        private ImageView exImage;

        public HistoryHolder(@NonNull View itemView) {
            super(itemView);

            exImage = itemView.findViewById(R.id.iv_ex_image_history);
            exName = itemView.findViewById(R.id.tv_ex_name_history);

        }
    }
}
