package edu.umd.cmsc436.mstestsuite.data;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import edu.umd.cmsc436.mstestsuite.R;

/**
 * Simple holder for recyclerview items
 */
class ItemHolder extends RecyclerView.ViewHolder {

    FrameLayout root;
    TextView label;
    ImageView icon;

    ItemHolder(FrameLayout itemView) {
        super(itemView);

        root = itemView;
        label = (TextView) itemView.findViewById(R.id.item_text_view);
        icon = (ImageView) itemView.findViewById(R.id.item_image_view);
    }
}
