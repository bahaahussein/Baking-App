package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Professor on 9/19/2017.
 */

public class RecipesSelectionAdapter extends RecyclerView.Adapter<RecipesSelectionAdapter.RecipesSelectionViewHolder>{

    private ArrayList<RecipesItem> mRecipesItems;
    private static ListItemClickListener mOnClickListener;


    public RecipesSelectionAdapter(ArrayList<RecipesItem> recipesItems) {
        mRecipesItems = recipesItems;
    }

    public static void setRecipesSelectionAdapterListener(ListItemClickListener listener) {
        mOnClickListener = listener;
    }

    @Override
    public RecipesSelectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_recipe_select;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipesSelectionViewHolder viewHolder = new RecipesSelectionViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipesSelectionViewHolder holder, int position) {
        holder.recipeNameView.setText(mRecipesItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mRecipesItems.size();
    }

    public interface ListItemClickListener {
        void onListItemClick(RecipesItem recipesItem);
    }

    class RecipesSelectionViewHolder extends RecyclerView.ViewHolder
    implements View.OnClickListener {
        private TextView recipeNameView;

        public RecipesSelectionViewHolder(View itemView) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.tv_recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(mRecipesItems.get(clickedPosition));
        }
    }
}
