package com.example.nanhijaan;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.MyViewHolder> {

    private Context mContext;
    private List<Disease> diseaseList;
    private ItemClickListener mClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView disease_title;
        public ImageView thumbnail;
        CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            disease_title = view.findViewById(R.id.title);
            thumbnail = view.findViewById(R.id.thumbnail);

//            thumbnail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });

            cardView = view.findViewById(R.id.card_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }


    }

    public DiseaseAdapter(Context mContext, List<Disease> diseaseList) {
        this.mContext = mContext;
        this.diseaseList = diseaseList;
    }

    @NonNull
    @Override
    public DiseaseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.disease_card, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseAdapter.MyViewHolder myViewHolder, int i) {
        Disease disease = diseaseList.get(i);
        myViewHolder.disease_title.setText(disease.getDisease_name());
        myViewHolder.thumbnail.setImageResource(disease.getThumbnail());

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
