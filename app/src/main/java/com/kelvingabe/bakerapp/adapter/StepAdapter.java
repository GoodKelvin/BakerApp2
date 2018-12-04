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


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    private final List<Step> mStepList;
    private final StepSelectedListener mStepSelectedListener;


    public StepAdapter(List<Step> mStepList, StepSelectedListener mStepSelectedListener) {
        this.mStepList = mStepList;
        this.mStepSelectedListener = mStepSelectedListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View patentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item, parent, false);
        return new StepViewHolder(patentView);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(mStepList.get(position));
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }


    //interface to implement to be notified of task completions
    public interface StepSelectedListener {
        void stepSelected(Step step, int index);
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_description_text)
        TextView stepDescriptionText;

        StepViewHolder(View itemView) {
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
            mStepSelectedListener.stepSelected(mStepList.get(selectedItemPosition), selectedItemPosition);

        }
    }
}
