package com.kelvingabe.bakerapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvingabe.bakerapp.R;
import com.kelvingabe.bakerapp.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<Step> steps;
    private final StepSelectedListener selectedListener;


    public StepAdapter(List<Step> steps, StepSelectedListener selectedListener) {
        this.steps = steps;
        this.selectedListener = selectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View patentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new ViewHolder(patentView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }


    public interface StepSelectedListener {
        void stepSelected(Step step, int index);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_description_text)
        TextView stepDescriptionText;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(Step step) {
            stepDescriptionText.setText(step.shortDescription);
        }


        @Override
        public void onClick(View v) {
            int selectedItemPosition = getAdapterPosition();
            selectedListener.stepSelected(steps.get(selectedItemPosition), selectedItemPosition);

        }
    }
}
