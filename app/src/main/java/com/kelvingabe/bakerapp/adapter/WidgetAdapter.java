package com.kelvingabe.bakerapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelvingabe.bakerapp.R;
import com.kelvingabe.bakerapp.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetAdapter extends RecyclerView.Adapter<WidgetAdapter.ViewHolder> {
    private final List<Recipe> recipeList;
    private final WidgetRecipeListener recipeListener;
    private String TAG = "WidgetAdapter";

    public WidgetAdapter(List<Recipe> recipeList, WidgetRecipeListener widgetRecipeListener) {
        this.recipeList = recipeList;
        recipeListener = widgetRecipeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_recipe_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(recipeList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public interface WidgetRecipeListener {
        void recipeSelected(Recipe recipe);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.widget_recipe_name)
        TextView mRecipeNameText;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(String name) {
            mRecipeNameText.setText(name);
        }

        @Override
        public void onClick(View v) {
            recipeListener.recipeSelected(recipeList.get(getAdapterPosition()));
        }
    }
}