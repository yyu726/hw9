package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TabSimilarFragment extends Fragment {

    private Item item;
    private JSONObject similarObj;
    private JSONArray similarArr;
    private View view;
    private int type = 0;
    private int order = 0;
    private RecyclerView recyclerView;
    private List<ItemSimilar> itemList = new ArrayList<>();

    public TabSimilarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        view = inflater.inflate(R.layout.fragment_similar,container,false);
        return view;
    }


    public void init() {

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.PREFERENCE_PATH), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String itemJSON = sharedPref.getString(getString(R.string.PREFERENCE_ITEM), "N/A");
        item = new Gson().fromJson(itemJSON, Item.class);

        requestDetail();

    }

    private void requestDetail() {

        String url = makeDetailUrl();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        //String url ="http://www.google.com:81/";
        //String url ="http://yyu726-cs571-hw8.us-west-1.elasticbeanstalk.com/?&type=search&query=&keywords=iphone&buyerPostalCode=12345&itemFilter(0).name=MaxDistance&itemFilter(0).value=125&itemFilter(1).name=FreeShippingOnly&itemFilter(1).value=true&itemFilter(2).name=LocalPickupOnly&itemFilter(2).value=true&itemFilter(3).name=HideDuplicateItems&itemFilter(3).value=true&itemFilter(4).name=Condition&itemFilter(4).value(0)=New&itemFilter(4).value(1)=Used&itemFilter(4).value(2)=Unspecified&outputSelector(0)=SellerInfo&outputSelector(1)=StoreInfo";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e(getString(R.string.TAG), response);
                        try {
                            JSONObject obj = new JSONObject(response);
                            similarObj = obj.getJSONObject("similar");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            similarArr = similarObj.getJSONObject("getSimilarItemsResponse").getJSONObject("itemRecommendations").getJSONArray("item");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loadData();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    private String makeDetailUrl() {
        String url ="";
        url += "http://yyu726-cs571-hw8.us-west-1.elasticbeanstalk.com/?";
        url += "&type=detail";
        url += "&query=";
        url += "&id=" + item.getId();
        try {
            url += "&title=" + URLEncoder.encode(item.getTitle(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    private void loadData() {

        for(int i = 0; i < similarArr.length();i++){
            ItemSimilar tmp = new ItemSimilar();
            try {
                tmp.setImage(similarArr.getJSONObject(i).getString("imageURL"));
            } catch (JSONException e) {
                tmp.setImage("");
            }
            try {
                tmp.setTitle(similarArr.getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                tmp.setTitle("");
            }
            try {
                tmp.setShipping(similarArr.getJSONObject(i).getJSONObject("shippingCost").getString("__value__"));
            } catch (JSONException e) {
                tmp.setShipping("");
            }
            try {
                tmp.setDaysLeft(similarArr.getJSONObject(i).getString("timeLeft"));
            } catch (JSONException e) {
                tmp.setDaysLeft("");
            }
            try {
                tmp.setPrice(similarArr.getJSONObject(i).getJSONObject("buyItNowPrice").getString("__value__"));
            } catch (JSONException e) {
                tmp.setPrice("");
            }
            try {
                tmp.setOnclick(similarArr.getJSONObject(i).getString("viewItemURL"));
            } catch (JSONException e) {
                tmp.setOnclick("");
            }
            itemList.add(tmp);
        }
        showRecyclerView();

    }

    private void showRecyclerView() {
        recyclerView = view.findViewById(R.id.similar_recyclerview);
        RecyclerViewAdapterSimilar recyclerViewAdapterSimilar = new RecyclerViewAdapterSimilar(getContext(),itemList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerView.setAdapter(recyclerViewAdapterSimilar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinnerType = getView().findViewById(R.id.spinnerType);
        Spinner spinnerOrder = getView().findViewById(R.id.spinnerOrder);
        spinnerOrder.setEnabled(false);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    getView().findViewById(R.id.spinnerOrder).setEnabled(false);
                }else{
                    type = position;
                    getView().findViewById(R.id.spinnerOrder).setEnabled(true);
                    refreshList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        spinnerOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                order = position;
                refreshList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    private void refreshList() {
        if (type != 0) {
            Collections.sort(itemList, new MyComparator(type, order).getComparator());
        }
        showRecyclerView();
    }

    private class MyComparator {
        private int type;
        private int order;
        private Comparator<ItemSimilar> comparator;


        public MyComparator(int type, int order) {
            this.type = type;
            this.order = order;
        }

        public Comparator<ItemSimilar> getComparator() {
            if(type==1){
                //Sort by name
                if(order==0) {
                    comparator =  new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    };
                } else {
                    comparator =  new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        }
                    };
                }
            }

            if(type==2) {
                //Sort by price
                if (order == 0) {
                    comparator = new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return (int) Double.parseDouble(o1.getPrice().substring(1)) - (int) Double.parseDouble(o2.getPrice().substring(1));
                        }
                    };
                } else {
                    comparator = new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return (int) Double.parseDouble(o2.getPrice().substring(1)) - (int) Double.parseDouble(o1.getPrice().substring(1));
                        }
                    };
                }
            }

            if(type==3){
                //Sort by days left
                if(order==0) {
                    comparator = new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return Integer.parseInt(o1.getDaysLeft().split(" ")[0])-Integer.parseInt(o2.getDaysLeft().split(" ")[0]);
                        }
                    };
                } else {
                    comparator =  new Comparator<ItemSimilar>() {
                        @Override
                        public int compare(ItemSimilar o1, ItemSimilar o2) {
                            return Integer.parseInt(o2.getDaysLeft().split(" ")[0])-Integer.parseInt(o1.getDaysLeft().split(" ")[0]);
                        }
                    };
                }
            }
            return comparator;
        }
    }
}
