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

public class WidgetAdapter extends RecyclerView.Adapter<RecipeWidgetAdapter.RecipeViewHolder> {

    private final List<Recipe> mRecipeList;
    private final WidgetRecipeListener mWidgetRecipeListener;

    public RecipeWidgetAdapter(List<Recipe> recipeList, WidgetRecipeListener widgetRecipeListener) {
        mRecipeList = recipeList;
        mWidgetRecipeListener = widgetRecipeListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_recipe_item, parent, false);

        return new RecipeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(mRecipeList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    interface WidgetRecipeListener {
        void recipeSelected(Recipe recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.widget_recipe_name)
        TextView mRecipeNameText;

        RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(String name) {
            mRecipeNameText.setText(name);

        }

        @Override
        public void onClick(View v) {
            mWidgetRecipeListener.recipeSelected(mRecipeList.get(getAdapterPosition()));
        }
    }
}