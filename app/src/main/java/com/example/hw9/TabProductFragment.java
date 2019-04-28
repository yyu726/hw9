package com.example.hw9;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TabProductFragment extends Fragment {
    // TODO: needs to be modified
    private Item item;
    private JSONObject detailObj;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_product, container, false);
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
                            detailObj = obj.getJSONObject("detail");
/*                            photosObj = obj.getJSONObject("google");
                            similarObj = obj.getJSONObject("similar");*/
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


    public void loadData() {
        ArrayList<String> pictureUrl = new ArrayList<>();
        String titleString = "";
        String price="";
        String shipping="";
        String pricecontent="";
        String brandcontent="";
        String subtitlecontent="";
        JSONArray specList;
        ArrayList<String> specs = new ArrayList<>();


        try {
            Log.e("asdf","asdf");
            titleString = detailObj.getJSONObject("Item").getString("Title");
            int count = detailObj.getJSONObject("Item").getJSONArray("PictureURL").length();
            for(int i = 0; i< count;i++){
                pictureUrl.add(detailObj.getJSONObject("Item").getJSONArray("PictureURL").getString(i));
            }
            price = detailObj.getJSONObject("Item").getJSONObject("CurrentPrice").getString("Value");
            shipping = item.getShipping();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try{
            pricecontent = detailObj.getJSONObject("Item").getJSONObject("CurrentPrice").getString("Value");
            specList = detailObj.getJSONObject("Item").getJSONObject("ItemSpecifics").getJSONArray("NameValueList");
            int specCount = specList.length();
            for(int i = 0;i < specCount;i++){
                JSONObject temp = specList.getJSONObject(i);
                if(temp.getString("Name").equals("Brand")){
                    brandcontent = temp.getJSONArray("Value").getString(0);
                    specs.add(0,brandcontent);
                }else{
                    specs.add(temp.getJSONArray("Value").getString(0));
                }
            }
        }catch(JSONException e){
        }

        try{
            subtitlecontent = detailObj.getJSONObject("Item").getString("Subtitle");
        }catch(JSONException e){
        }


        ProgressBar progressBar = getView().findViewById(R.id.progressbar);
        progressBar.setVisibility(progressBar.GONE);
        TextView textView = getView().findViewById(R.id.texthint);
        textView.setVisibility(textView.GONE);
        LinearLayout ll = getView().findViewById(R.id.id_gallery);
        for (int i = 0; i < pictureUrl.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
            Picasso.with(getActivity()).load(pictureUrl.get(i)).resize(1080, 1080).centerInside().into(imageView);
            ll.addView(imageView);
        }

        TextView title = getView().findViewById(R.id.item_title_id);
        title.setText(titleString);

        String tempString = "&nbsp;&nbsp;<strong><font color='#5728e4'><big>$"+price+"</big></font></strong>&nbsp;&nbsp;With "+shipping;
        TextView priceandshipping = getView().findViewById(R.id.priceandshipping);
        priceandshipping.setText(Html.fromHtml(tempString));

        View hr1 = getView().findViewById(R.id.hr1);
        LinearLayout highlights = getView().findViewById(R.id.highlights);
        View hr2 = getView().findViewById(R.id.hr2);
        LinearLayout specification = getView().findViewById(R.id.specification);

        hr1.setVisibility(hr1.VISIBLE);
        highlights.setVisibility(highlights.VISIBLE);
        hr2.setVisibility(hr2.VISIBLE);
        specification.setVisibility(specification.VISIBLE);


        TextView textViewtemp = getView().findViewById(R.id.price_content);
        textViewtemp.setText("$"+pricecontent);
        textViewtemp = getView().findViewById(R.id.brand_content);
        textViewtemp.setText(brandcontent);
        textViewtemp = getView().findViewById(R.id.subtitle_content);
        textViewtemp.setText(subtitlecontent);

        if(subtitlecontent.length()==0){
            TableRow temp = getView().findViewById(R.id.subtitle_row);
            temp.setVisibility(temp.GONE);
        }

        if(pricecontent.length()==0){
            TableRow temp = getView().findViewById(R.id.price_row);
            temp.setVisibility(temp.GONE);
        }

        if(brandcontent.length()==0){
            TableRow temp = getView().findViewById(R.id.brand_row);
            temp.setVisibility(temp.GONE);
        }

        if(subtitlecontent.length()+pricecontent.length()+brandcontent.length()==0){
            hr1.setVisibility(hr1.GONE);
            highlights.setVisibility(highlights.GONE);
        }

        LinearLayout lists = getView().findViewById(R.id.spec_list);
        for(int i = 0; i < specs.size();i++){
            TextView tempView = new TextView(getContext());
            tempView.setText("· "+specs.get(i));
            tempView.setPadding(0,3,0,3);
            lists.addView(tempView);
        }
        if(specs.size()==0){
            hr2.setVisibility(hr2.GONE);
            specification.setVisibility(specification.GONE);
        }

    }
}
