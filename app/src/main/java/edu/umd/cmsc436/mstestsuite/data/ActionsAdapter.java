package edu.umd.cmsc436.mstestsuite.data;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import edu.umd.cmsc436.mstestsuite.R;

/**
 * Display the main actions of the frontend
 */

public class ActionsAdapter extends RecyclerView.Adapter<ItemHolder> {

    private Action[] mActions;

    public ActionsAdapter (Action[] actions) {
        this.mActions = actions;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        FrameLayout fl = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false);

        return new ItemHolder(fl);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        holder.icon.setImageResource(mActions[position].getIconResource());
        holder.label.setText(mActions[position].getDisplayName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActions[holder.getAdapterPosition()].run();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActions.length;
    }

    public GridLayoutManager.SpanSizeLookup getSpanLookup (RecyclerView.LayoutManager layoutManager) {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };
    }
}
