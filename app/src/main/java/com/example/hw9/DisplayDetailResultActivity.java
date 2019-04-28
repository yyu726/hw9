package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.HashSet;

public class DisplayDetailResultActivity extends AppCompatActivity {
    private TabProductFragment tabProduct = new TabProductFragment();
    private TabShippingFragment tabShipping = new TabShippingFragment();
    private TabPhotosFragment tabPhotos = new TabPhotosFragment();
    private TabSimilarFragment tabSimilar = new TabSimilarFragment();
    private Gson gson = new Gson();
    private Item item;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private HashSet<String> idSet;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TabAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_detail_result);

        init();
        setToolbar();
        setFab();
        setTab();
        //tabProduct.init();
        //requestDetail();

    }

    private Runnable commitChange = new Runnable() {
        public void run() {
            editor.commit();
        }
    };

    private void requestDetail() {

        String url = makeDetailUrl();
        RequestQueue queue = Volley.newRequestQueue(this);
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
                            JSONObject detailObj = obj.getJSONObject("detail");
                            JSONObject photosObj = obj.getJSONObject("google");
                            JSONObject similarObj = obj.getJSONObject("similar");
                            String detailJSON = gson.toJson(detailObj);
                            String photosJSON = gson.toJson(photosObj);
                            String similarJSON = gson.toJson(similarObj);
                            String itemJSON = gson.toJson(item);
                            editor.putString(getString(R.string.PREFERENCE_DETAIL), detailJSON);
                            editor.putString(getString(R.string.PREFERENCE_PHOTOS), photosJSON);
                            editor.putString(getString(R.string.PREFERENCE_SIMILAR), similarJSON);
                            editor.putString(getString(R.string.PREFERENCE_ITEM), itemJSON);
                            commitChange.run();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tabProduct.init();
/*                        try {
                            JSONObject obj;
                            JSONArray arr;
                            obj = new JSONObject(response);
                            obj = obj.getJSONObject("result");
                            arr = obj.getJSONArray("findItemsAdvancedResponse");
                            obj = arr.getJSONObject(0);
                            arr = obj.getJSONArray("searchResult");
                            obj = arr.getJSONObject(0);
                            arr = obj.getJSONArray("item");
                            String s = obj.toString();
                            Log.e("asfsdf",s);

                            int count = Integer.parseInt(obj.getString("@count"));
                            for (int i = 0; i < count; i++) {
                                Item item = new Item();
                                obj = arr.getJSONObject(i);
                                item.setObj(obj);
                                try{
                                    item.setId(obj.getJSONArray("itemId").getString(0));
                                }catch(JSONException e){
                                    item.setId("N/A");
                                }
                                try{
                                    item.setTitle(obj.getJSONArray("title").getString(0));
                                }catch(JSONException e){
                                    item.setTitle("N/A");
                                }
                                try{
                                    item.setImage(obj.getJSONArray("galleryURL").getString(0));
                                }catch(JSONException e){
                                    item.setImage("N/A");
                                }
                                try{
                                    item.setCondition(obj.getJSONArray("condition").getJSONObject(0).getJSONArray("conditionDisplayName").getString(0));
                                }catch(JSONException e){
                                    item.setCondition("N/A");
                                }
                                try{
                                    item.setPrice("$" + obj.getJSONArray("sellingStatus").getJSONObject(0).getJSONArray("currentPrice").getJSONObject(0).getString("__value__"));
                                }catch(JSONException e){
                                    item.setPrice("N/A");
                                }
                                try{
                                    item.setShipping("$" + obj.getJSONArray("shippingInfo").getJSONObject(0).getJSONArray("shippingServiceCost").getJSONObject(0).getString("__value__"));
                                }catch(JSONException e){
                                    item.setShipping("N/A");
                                }
                                if (item.getShipping().equals("$0.0")) {
                                    item.setShipping("Free Shipping");
                                }
                                try{
                                    item.setZip("Zip:" + obj.getJSONArray("postalCode").getString(0));
                                }catch(JSONException e){
                                    item.setZip("N/A");
                                }
                                itemList.add(item);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        findViewById(R.id.progressLayout).setVisibility(View.GONE);*/

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(getString(R.string.TAG),"That didn't work!");
                Context context = getApplicationContext();
                CharSequence text = "Connection failed.\nPlease check your Internet.";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                finish();
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

    private void init() {
        Intent intent = getIntent();
        String itemJSON = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        item = gson.fromJson(itemJSON, Item.class);
        sharedPref = getSharedPreferences(getString(R.string.PREFERENCE_PATH), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        String idSetJSON = sharedPref.getString(getString(R.string.PREFERENCE_IDSET), "N/A");
        idSet = gson.fromJson(idSetJSON, new HashSet<String>().getClass());
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(item.getTitle());
    }

    private void setFab() {
        fab = findViewById(R.id.fab);
        if (idSet.contains(item.getId())) {
            fab.setImageResource(R.drawable.ic_cart_remove_white);
        } else {
            fab.setImageResource(R.drawable.ic_cart_add_white);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idSet.contains(item.getId())) {
                    ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_cart_add_white);
                    idSet.remove(item.getId());
                    String idSetJSON = gson.toJson(idSet, idSet.getClass());
                    editor.putString(getString(R.string.PREFERENCE_IDSET), idSetJSON);
                    editor.remove(item.getId());
                    commitChange.run();

                    CharSequence text = item.getTitle() + " was removed from wish list";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getApplicationContext(), text, duration).show();
                } else {
                    ((FloatingActionButton) findViewById(R.id.fab)).setImageResource(R.drawable.ic_cart_remove_white);
                    idSet.add(item.getId());
                    String idSetJSON = gson.toJson(idSet, idSet.getClass());
                    editor.putString(getString(R.string.PREFERENCE_IDSET), idSetJSON);

                    String itemListJSON = gson.toJson(item, Item.class);
                    editor.putString(item.getId(), itemListJSON);
                    commitChange.run();

                    CharSequence text = item.getTitle() + " was add to wish list";
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getApplicationContext(), text, duration).show();
                }
            }
        });
    }
    
    private void setTab() {
        viewPager = findViewById(R.id.viewPagerDetail);
        tabLayout = findViewById(R.id.tabLayoutDetail);

        adapter = new TabAdapter(getSupportFragmentManager());

        Fragment[] tabFragments = {tabProduct, tabShipping, tabPhotos, tabSimilar};
        String[] tabTitles = {"PRODUCT", "SHIPPING", "PHOTOS", "SIMILAR"};
        int[] tabIcons = {R.drawable.ic_tab_product, R.drawable.ic_tab_shipping, R.drawable.ic_tab_photos, R.drawable.ic_tab_similar};

        for (int i = 0; i < tabFragments.length; i++) {
            adapter.addFragment(tabFragments[i], tabTitles[i]);
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabFragments.length; i++) {
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }
}
