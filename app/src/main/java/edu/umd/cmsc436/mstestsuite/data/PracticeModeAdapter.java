package edu.umd.cmsc436.mstestsuite.data;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import edu.umd.cmsc436.mstestsuite.R;

/**
 * Put test apps in the recycler view
 */

public class PracticeModeAdapter extends RecyclerView.Adapter<PracticeModeAdapter.MyViewHolder> {

    private TestApp[] mTestApps;
    private Events mEventCallbacks;

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_ITEM = 1;

    public PracticeModeAdapter(TestApp[] testApps, Events callbacks) {
        this.mTestApps = testApps;
        this.mEventCallbacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEWTYPE_HEADER : VIEWTYPE_ITEM;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout_resource = viewType == VIEWTYPE_HEADER ?
                R.layout.recyclerview_header : R.layout.recyclerview_item;

        FrameLayout fl = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(layout_resource, parent, false);

        return new MyViewHolder(fl);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        if (position == 0) {
            holder.label.setText(R.string.recyclerview_header_text);
            return;
        }

        if (holder.icon != null) {
            holder.icon.setImageResource(mTestApps[position-1].getIconResource());
        }

        holder.label.setText(mTestApps[position-1].getDisplayName());
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(mTestApps[holder.getAdapterPosition() - 1].getPackageName());
                    // TODO test arguments for practice mode, or move to activity through callbacks
                    holder.root.getContext().startActivity(i);
                } catch (ActivityNotFoundException anfe) {
                    mEventCallbacks.toast(mTestApps[holder.getAdapterPosition()-1].getDisplayName() + " not found");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTestApps.length+1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        FrameLayout root;
        TextView label;
        ImageView icon;

        MyViewHolder(FrameLayout itemView) {
            super(itemView);

            root = itemView;
            label = (TextView) itemView.findViewById(R.id.item_text_view);
            icon = (ImageView) itemView.findViewById(R.id.item_image_view);
        }
    }

    public interface Events {
        void toast (String message);
    }
}
