package com.example.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Professor on 9/20/2017.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{

    private ArrayList<Step> mSteps;
    private static StepsItemClickListener mListener;

    public StepsAdapter(ArrayList<Step> steps) {
        mSteps = steps;
    }

    public static void setListener(StepsItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.list_item_steps;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepsViewHolder viewHolder = new StepsViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepsViewHolder holder, int position) {
        holder.stepTextView.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public interface StepsItemClickListener {
        void onStepsItemClick(int position);
    }

    class StepsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView stepTextView;

        public StepsViewHolder(View itemView) {
            super(itemView);
            stepTextView = itemView.findViewById(R.id.tv_step);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mListener.onStepsItemClick(clickedPosition);
        }
    }
}
