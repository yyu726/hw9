package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapterSimilar extends RecyclerView.Adapter<RecyclerViewAdapterSimilar.mViewHolder> {

    Context mContext;
    List<ItemSimilar> mData;

    public RecyclerViewAdapterSimilar(Context mContext, List<ItemSimilar> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item_similar,viewGroup,false);
        return new mViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder mViewHolder, final int i) {
        mViewHolder.item_title2.setText(mData.get(i).getTitle());
        mViewHolder.item_price.setText(mData.get(i).getPrice());
        mViewHolder.item_days_left.setText(mData.get(i).getDaysLeft());
        mViewHolder.item_shipping.setText(mData.get(i).getShipping());
        if(!mData.get(i).getImage().equals("")){
            Picasso.with(mContext).load(mData.get(i).getImage()).into(mViewHolder.itemURL);
        }
        mViewHolder.cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(mData.get(i).getOnclick())));
                    mContext.startActivity(browserIntent);
                }catch(Exception e){}
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class mViewHolder extends  RecyclerView.ViewHolder{
        TextView item_title2;
        TextView item_price;
        TextView item_days_left;
        TextView item_shipping;
        ImageView itemURL;
        CardView cardView2;

        public mViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView2 = (CardView)itemView.findViewById(R.id.similar_item_view);
            item_title2 = (TextView)itemView.findViewById(R.id.similar_item_title);
            item_price = (TextView)itemView.findViewById(R.id.similar_item_price);
            item_days_left = (TextView)itemView.findViewById(R.id.similar_item_days_left);
            item_shipping = (TextView)itemView.findViewById(R.id.similar_item_shipping);
            itemURL = (ImageView)itemView.findViewById(R.id.item_photo);
        }
    }
}
