package edu.umd.cmsc436.mstestsuite.data;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import edu.umd.cmsc436.mstestsuite.R;

/**
 * Put test apps in the recycler view
 */

public class PracticeModeAdapter extends ActionsAdapter {

    private TestApp[] mTestApps;
    private Events mEventCallbacks;

    private static final int VIEWTYPE_HEADER = 0;
    private static final int VIEWTYPE_ITEM = 1;

    public PracticeModeAdapter(TestApp[] testApps, Events callbacks) {
        super(testApps);
        this.mTestApps = testApps;
        this.mEventCallbacks = callbacks;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEWTYPE_HEADER : VIEWTYPE_ITEM;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        int layout_resource = viewType == VIEWTYPE_HEADER ?
                R.layout.recyclerview_header : R.layout.recyclerview_item;

        FrameLayout fl = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(layout_resource, parent, false);

        return new ItemHolder(fl);
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
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
                mEventCallbacks.appSelected(mTestApps[holder.getAdapterPosition()-1]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTestApps.length+1;
    }

    @Override
    public GridLayoutManager.SpanSizeLookup getSpanLookup(final RecyclerView.LayoutManager layoutManager) {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    try {
                        return ((GridLayoutManager) layoutManager).getSpanCount();
                    } catch (ClassCastException cce) {
                        // fall through to return 1
                    }
                }

                return 1;
            }
        };
    }

    public interface Events {
        void appSelected (TestApp app);
    }
}
