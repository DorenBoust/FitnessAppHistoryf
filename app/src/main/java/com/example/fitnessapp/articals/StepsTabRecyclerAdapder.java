package com.example.fitnessapp.articals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

public class StepsTabRecyclerAdapder extends RecyclerView.Adapter<StepsTabRecyclerAdapder.StepHolder> {

    private Recpie recpie;
    private LayoutInflater inflater;

    public StepsTabRecyclerAdapder(Recpie recpie, LayoutInflater inflater) {
        this.recpie = recpie;
        this.inflater = inflater;
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.recipe_activity_step_recycler, parent,false);

        return new StepHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {

        holder.stepNumber.setText(String.valueOf(position+1));
        String step = recpie.getSteps().getSteps().get(position);
        holder.stepProcess.setText(step);

    }

    @Override
    public int getItemCount() {
        return recpie.getSteps().getSteps().size();
    }

    public class StepHolder extends RecyclerView.ViewHolder {

        TextView stepNumber;
        TextView stepProcess;

        public StepHolder(@NonNull View itemView) {
            super(itemView);


            stepNumber = itemView.findViewById(R.id.step_number_tv);
            stepProcess = itemView.findViewById(R.id.step_process_tv_recycler);
        }
    }
}
