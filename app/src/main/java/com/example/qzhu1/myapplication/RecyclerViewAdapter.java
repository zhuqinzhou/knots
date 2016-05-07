package com.example.qzhu1.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.HashMap;

/**
 * Created by ZQZ on 2016/2/18.
 */
public class RecyclerViewAdapter extends FirebaseRecyclerAdapter<Bill, RecyclerViewAdapter.ViewHolder> {

    private OnFragmentInteractionListener mListener;
    private ItemClickListener mItemClickListener;
    private RecyclerViewAdapter mAdapter = this;
    private Context mContext;
    private int mode;

    public RecyclerViewAdapter(Class<Bill> modelClass, int modelLayout,
                               Class<ViewHolder> holder, Query ref, Context context, int position) {
        super(modelClass, modelLayout, holder, ref);
        this.mContext = context;
        this.mode=position;
    }

    public interface ItemClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);

        public void onOverflowMenuClick(View view, int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if(mode==2)
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        else
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view2, viewGroup, false);
        mListener = (OnFragmentInteractionListener) v.getContext();
        return new ViewHolder(v);
    }

    public RecyclerViewAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void populateViewHolder(ViewHolder billViewHolder, Bill bill, int i) {
        billViewHolder.description.setText(bill.getTime() + "\n" + bill.getName() + "\n" + bill.getDescription());
        billViewHolder.name.setText(bill.getOwe().substring(0, bill.getOwe().indexOf("@")) + " owe " + bill.getPay().substring(0,  bill.getPay().indexOf("@")));
        billViewHolder.amount.setText("$" + bill.getAmount());
        billViewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (mItemClickListener != null) {
                    mItemClickListener.onClick(view, position);
                }

            }

            @Override
            public void onOverflowMenuClick(View view, int position) {
                mListener.onMovieMenuSelected(position, view, getAdapter());
            }

            @Override
            public void onLongClick(View view, int position) {
                mListener.onMovieLongClickSelected(position, view, getAdapter());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onMoiveFragmentSelected(HashMap<String, ?> movie);

        void onMovieMenuSelected(int position, View view, RecyclerViewAdapter rvAdapter);

        void onMovieLongClickSelected(int position, View view, RecyclerViewAdapter rvAdapter);
//        void onFragmentInteraction(Uri uri);
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView description;
        TextView amount;
        TextView name;
        ImageView photo;
        ImageView vMenu;
        private ItemClickListener clickListener;

        ViewHolder(View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.description);
            name = (TextView) itemView.findViewById(R.id.name);
            photo = (ImageView) itemView.findViewById(R.id.photo);
            amount = (TextView) itemView.findViewById(R.id.amount);
            vMenu = (ImageView)itemView.findViewById(R.id.card_menu);


            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null)
                        clickListener.onClick(v, getAdapterPosition());
                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (clickListener != null) {
                        clickListener.onLongClick(view, getAdapterPosition());
                    }
                    return true;
                }
            });

            if(vMenu != null){
                vMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mItemClickListener != null){
                            mItemClickListener.onOverflowMenuClick(v, getAdapterPosition());
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getAdapterPosition());
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }
    }

    public void SetOnItemClickListener(final ItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
