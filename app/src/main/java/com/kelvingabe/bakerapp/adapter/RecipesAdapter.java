package com.kelvingabe.bakerapp.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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


//Class to handle data presentation of the recipes
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    public List<Recipe> mRecipeList;
    public RecipeSelectedListener mRecipeSelectedListener;

    public RecipesAdapter(List<Recipe> recipeList, RecipeSelectedListener recipeSelectedListener) {
        mRecipeList = recipeList;
        mRecipeSelectedListener = recipeSelectedListener;
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        holder.bind(recipe);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View parentView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(parentView);
    }


    public interface RecipeSelectedListener {

        void recipeSelected(Recipe selectedRecipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        @BindView(R.id.recipe_image)
        ImageView mRecipeImage;

        @BindView(R.id.recipe_name_text)
        TextView mRecipeNameText;

        RecipeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        /**
         * Binds recipe to view. Uses a default image if recipe image is not available
         * <p>
         * default image used is from unsplash.com (https://unsplash.com/photos/PMx1Xb-XB1U)
         *
         * @param recipe the recipe to bind
         */
        void bind(Recipe recipe) {
            //load default image if recipe url is empty
            Log.d("XXX", "Binding: " + recipe.name);
            if (TextUtils.isEmpty(recipe.imageUrl)) {
                //load image url
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
            mRecipeSelectedListener.recipeSelected(mRecipeList.get(selectedItemPosition));
        }
    }
}
