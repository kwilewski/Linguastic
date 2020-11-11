package com.narrowstudio.wonski.linguastic;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<String> mDataset;
    private ArrayList<Integer> mSel;
    private OnItemListener mOnItemListener;
    private Context mContext;
    private boolean stateDarkModeSwitch;
    private SharedPreferences preferences;
    private static final String SHARED_PREFS = "PREFS";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textCardView;
        public ImageView imageView;
        OnItemListener onItemListener;
        RelativeLayout mLayout;
        public MyViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            textCardView = itemView.findViewById(R.id.textCardView);
            imageView = itemView.findViewById(R.id.imageView);
            mLayout = itemView.findViewById(R.id.itemLayout);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset, ArrayList<Integer> list, OnItemListener onItemListener, Context context) {
        this.mDataset = myDataset;
        this.mSel = list;
        this.mOnItemListener = onItemListener;
        this.mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recview_l_s, parent, false);
        MyViewHolder vh = new MyViewHolder(v, mOnItemListener);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textCardView.setText(mDataset.get(position));


        preferences = mContext.getSharedPreferences(SHARED_PREFS, 0);
        stateDarkModeSwitch = preferences.getBoolean("dark_mode", false);

        if(mSel.get(position)==0) {
            holder.imageView.setImageResource(0);
            int colorBackground, colorText;
            if (stateDarkModeSwitch){
                colorBackground = 0xff222222;
                colorText = 0xffaaaaaa;
            }
            else {
                colorBackground = 0xffcccccc;
                colorText = 0xff444444;
            }
            holder.mLayout.setBackgroundColor(colorBackground);
            holder.textCardView.setTextColor(colorText);

        }
        else{
            holder.imageView.setImageResource(R.drawable.ic_check_circle);
            int colorBackground, colorText;
            if (stateDarkModeSwitch){
                colorBackground = 0xff555555;
                colorText = 0xffaaaaaa;
            }
            else {
                colorBackground = 0xffe30000;
                colorText = 0xff000000;
            }
            holder.mLayout.setBackgroundColor(colorBackground);
            holder.textCardView.setTextColor(colorText);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }





    public interface OnItemListener{
        void onItemClick(int position);


    }

}
