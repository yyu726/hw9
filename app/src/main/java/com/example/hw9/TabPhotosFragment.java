package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class TabPhotosFragment extends Fragment {
    private Item item;
    private JSONObject photosObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_photos, container, false);
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
                            photosObj = obj.getJSONObject("google");

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
        String url = "";
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

        ArrayList<String> photos = new ArrayList<>();
        int count = 0;
        try {
            for (int i = 0; i < photosObj.getJSONArray("items").length(); i++) {
                try {
                    photos.add(photosObj.getJSONArray("items").getJSONObject(i).getString("link"));
                    count++;
                } catch (JSONException e) {
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = count; i < 8; i++) {
            photos.add("http://1.com");
        }

        try {

            ((ImageView) getView().findViewById(R.id.img_photo_0)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(0)).into(((ImageView) getView().findViewById(R.id.img_photo_0)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_0).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_1)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(1)).into(((ImageView) getView().findViewById(R.id.img_photo_1)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_1).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_2)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(2)).into(((ImageView) getView().findViewById(R.id.img_photo_2)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_2).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_3)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(3)).into(((ImageView) getView().findViewById(R.id.img_photo_3)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_3).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_4)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(4)).into(((ImageView) getView().findViewById(R.id.img_photo_4)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_4).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_5)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(5)).into(((ImageView) getView().findViewById(R.id.img_photo_5)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_5).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_6)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(6)).into(((ImageView) getView().findViewById(R.id.img_photo_6)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_6).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });
            ((ImageView) getView().findViewById(R.id.img_photo_7)).setAdjustViewBounds(true);
            Picasso.with(getActivity()).load(photos.get(7)).into(((ImageView) getView().findViewById(R.id.img_photo_7)), new Callback() {
                @Override
                public void onSuccess() {
                    try {
                        getActivity().findViewById(R.id.img_photo_7).setVisibility(View.VISIBLE);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError() {

                }
            });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        getActivity().findViewById(R.id.progressLayout).setVisibility(View.GONE);
        if (count == 0) {
            getActivity().findViewById(R.id.photoResultErrorText).setVisibility(View.VISIBLE);
        }

    }
}
