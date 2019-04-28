package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.List;

import static com.example.hw9.MainActivity.EXTRA_MESSAGE;

public class RecyclerViewAdapterSearchResult extends RecyclerView.Adapter<RecyclerViewAdapterSearchResult.MyViewHolder> {

    private Context mContext;
    private List<Item> mData;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private HashSet<String> idSet;
    private Gson gson = new Gson();


    public RecyclerViewAdapterSearchResult(Context mContext, List<Item> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_item, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int position) {

        sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.PREFERENCE_PATH), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String idSetJSON = sharedPref.getString(mContext.getString(R.string.PREFERENCE_IDSET), "N/A");
        idSet = gson.fromJson(idSetJSON, new HashSet<String>().getClass());

        myViewHolder.itemTitle.setText(mData.get(position).getTitle());

        if (mData.get(position).getImage() == "N/A") {
            Picasso.with(mContext).load("https://dubsism.files.wordpress.com/2017/12/image-not-found.png?w=547").into(myViewHolder.itemImage);
        } else {
            Picasso.with(mContext).load(mData.get(position).getImage()).into(myViewHolder.itemImage);
        }

        myViewHolder.itemZip.setText(mData.get(position).getZip());
        myViewHolder.itemShipping.setText(mData.get(position).getShipping());
        myViewHolder.itemCondition.setText(mData.get(position).getCondition());
        myViewHolder.itemPrice.setText(mData.get(position).getPrice());


        if (idSet.contains(mData.get(position).getId())) {
            myViewHolder.itemCart.setImageResource(R.drawable.ic_cart_remove);
        } else {
            myViewHolder.itemCart.setImageResource(R.drawable.ic_cart_add);
        }
        myViewHolder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DisplayDetailResultActivity.class);


                int position = myViewHolder.getAdapterPosition();

                String itemJSON = gson.toJson(mData.get(position), Item.class);
                intent.putExtra(EXTRA_MESSAGE, itemJSON);

                editor.putString(mContext.getString(R.string.PREFERENCE_ITEM), gson.toJson(mData.get(position)));
                editor.commit();
                mContext.startActivity(intent);

            }
        });

        myViewHolder.itemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = myViewHolder.getAdapterPosition();
                Log.e("aoo", Integer.toString(position));

                if (idSet.contains(mData.get(position).getId())) {
                    myViewHolder.itemCart.setImageResource(R.drawable.ic_cart_add);
                    idSet.remove(mData.get(position).getId());
                    String idSetJSON = gson.toJson(idSet, idSet.getClass());
                    editor.putString(mContext.getString(R.string.PREFERENCE_IDSET), idSetJSON);
                    editor.remove(mData.get(position).getId());
                    commitChange.run();

                    CharSequence text = mData.get(position).getTitle() + " was removed from wish list";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(mContext, text, duration).show();
                } else {
                    myViewHolder.itemCart.setImageResource(R.drawable.ic_cart_remove);
                    idSet.add(mData.get(position).getId());
                    String idSetJSON = gson.toJson(idSet, idSet.getClass());
                    editor.putString(mContext.getString(R.string.PREFERENCE_IDSET), idSetJSON);

                    Item tmpItem = mData.get(position);
                    String itemListJSON = gson.toJson(tmpItem, Item.class);
                    editor.putString(mData.get(position).getId(), itemListJSON);
                    commitChange.run();


                    CharSequence text = mData.get(position).getTitle() + " was added to wish list";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(mContext, text, duration).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView itemImage;
        TextView itemZip;
        TextView itemShipping;
        TextView itemCondition;
        TextView itemPrice;
        ImageView itemCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemZip = itemView.findViewById(R.id.itemZip);
            itemShipping = itemView.findViewById(R.id.itemShipping);
            itemCondition = itemView.findViewById(R.id.itemCondition);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemCart = itemView.findViewById(R.id.itemCart);
        }
    }

    private Runnable commitChange = new Runnable() {
        public void run() {
            editor.commit();
        }
    };

}
