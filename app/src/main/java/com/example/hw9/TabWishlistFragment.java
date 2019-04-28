package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw9.databinding.FragmentSearchformBinding;
import com.example.hw9.databinding.FragmentWishlistBinding;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TabWishlistFragment extends Fragment implements OnItemClick {
    private HashSet<String> idSet = new HashSet<>();
    private List<Item> itemList = new ArrayList<>();
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private DataWishlist datawishlist = new DataWishlist();

    @Override
    // source: https://stackoverflow.com/questions/41655797/refresh-fragment-when-change-between-tabs
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            init();
            RecyclerView myRecyclerView = getView().findViewById(R.id.recyclerViewWishList);
            RecyclerViewAdapterWishlist myRecyclerViewAdapter = new RecyclerViewAdapterWishlist(getContext(), itemList, this){};
            myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
            myRecyclerView.setAdapter(myRecyclerViewAdapter);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentWishlistBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_wishlist, container, false);
        View view = binding.getRoot();
        //here data must be an instance of the class MarsDataProvider

        binding.setDatawishlist(datawishlist);
       // return inflater.inflate(R.layout.fragment_wishlist, container, false);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init();
        RecyclerView myRecyclerView = getView().findViewById(R.id.recyclerViewWishList);
        RecyclerViewAdapterWishlist myRecyclerViewAdapter = new RecyclerViewAdapterWishlist(getContext(), itemList, this){};
        myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    public void init() {
        idSet = new HashSet<>();
        itemList = new ArrayList<>();
        sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_PATH), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        Gson gson = new Gson();
        String idSetJSON = sharedPref.getString(getString(R.string.PREFERENCE_IDSET), "N/A");
        if (idSetJSON == "N/A") {
            idSet = new HashSet<>();
            idSetJSON = gson.toJson(idSet, idSet.getClass());
            editor.putString(getString(R.string.PREFERENCE_IDSET), idSetJSON);
            commitChange.run();
            Log.e("test", "no set found");
        } else {

            idSet = gson.fromJson(idSetJSON, new HashSet<String>().getClass());

            datawishlist.setCountNum(0);
            datawishlist.setSubtotalNum(0);

            for (String id : idSet) {
                String itemJSON = sharedPref.getString(id, "N/A");
                if (itemJSON != "N/A") {
                    Item tmpItem = gson.fromJson(itemJSON, Item.class);
                    itemList.add(tmpItem);

                    datawishlist.addCountNum();
                    String num = tmpItem.getPrice();
                    if (num != "N/A") {
                        datawishlist.addSubtotalNum(Double.parseDouble(num.split("\\$")[1]));
                    }
                }


            }
        }

        ((TextView)getActivity().findViewById(R.id.countTextView)).setText(datawishlist.getCount());
        ((TextView)getActivity().findViewById(R.id.subTotalTextView)).setText(datawishlist.getSubtotal());

        if (datawishlist.getCountNum() != 0) {
            getActivity().findViewById(R.id.layoutNoWish).setVisibility(View.GONE);
            getActivity().findViewById(R.id.recyclerViewWishList).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.layoutNoWish).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.recyclerViewWishList).setVisibility(View.GONE);
        }
    }

/*    public void showItems() {

        itemList.add(new Item("123456","APPLE IPHONE X 256GB \"FACTORY UNLOCKED\" EXCELLENT sogoodcoolgenius", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "New", "$0.00", "FreeShipping", "311500"));
        itemList.add(new Item("123456", "APPLE IPHONE X 64GB A1901 GSM UNLOCKED EXCELLENT", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "Manufacture Refurbished", "$0.00", "FreeShipping", "311500"));
        itemList.add(new Item("APPLE IPHONE X 256GB \"FACTORY UNLOCKED\" EXCELLENT sogoodcoolgenius", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png"));
        itemList.add(new Item("123456", "APPLE IPHONE X 64GB A1901 GSM UNLOCKED EXCELLENT", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "Manufacture Refurbished", "$0.00", "FreeShipping", "311500"));


    }*/

    private Runnable commitChange = new Runnable() {
        public void run() {
            editor.commit();
        }
    };
    @Override
    public void onResume(){
        super.onResume();
        init();
        RecyclerView myRecyclerView = getView().findViewById(R.id.recyclerViewWishList);
        RecyclerViewAdapterWishlist myRecyclerViewAdapter = new RecyclerViewAdapterWishlist(getContext(), itemList, this){};
        myRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
    @Override
    public void onClick(String title, String value) {
        datawishlist.subtractCountNum();
        ((TextView)getActivity().findViewById(R.id.countTextView)).setText(datawishlist.getCount());
        if (value != "N/A") {
            datawishlist.subtractSubtotalNum(Double.parseDouble(value.split("\\$")[1]));
        }
        ((TextView)getActivity().findViewById(R.id.subTotalTextView)).setText(datawishlist.getSubtotal());

        if (datawishlist.getCountNum() != 0) {
            getActivity().findViewById(R.id.layoutNoWish).setVisibility(View.GONE);
            getActivity().findViewById(R.id.recyclerViewWishList).setVisibility(View.VISIBLE);
        } else {
            getActivity().findViewById(R.id.layoutNoWish).setVisibility(View.VISIBLE);
            getActivity().findViewById(R.id.recyclerViewWishList).setVisibility(View.GONE);
        }
        Context context = getActivity();
        CharSequence text = title + " was removed from wish list";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();

    }
}
