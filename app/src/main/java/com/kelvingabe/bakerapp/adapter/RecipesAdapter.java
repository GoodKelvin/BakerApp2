package com.kelvingabe.bakerapp.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kelvingabe.bakerapp.R;
import com.kelvingabe.bakerapp.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    public List<Recipe> list;
    public RecipeSelectedListener listener;

    public RecipesAdapter(List<Recipe> list, RecipeSelectedListener recipeSelectedListener) {
        this.list = list;
        listener = recipeSelectedListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = list.get(position);
        holder.bind(recipe);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(parentView);
    }


    public interface RecipeSelectedListener {

        void recipeSelected(Recipe selectedRecipe);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.recipe_image)
        ImageView mRecipeImage;

        @BindView(R.id.recipe_name_text)
        TextView mRecipeNameText;

        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }


        void bind(Recipe recipe) {

            if (TextUtils.isEmpty(recipe.imageUrl)) {
                Picasso.get().load(Uri.parse(recipe.imageUrl))
                        .placeholder(R.drawable.default_recipe)
                        .error(R.drawable.default_recipe)
                        .into(mRecipeImage);
            }
            mRecipeNameText.setText(recipe.name);
        }


        @Override
        public void onClick(View v) {
            int selectedItemPosition = getAdapterPosition();
            listener.recipeSelected(list.get(selectedItemPosition));
        }
    }
}
