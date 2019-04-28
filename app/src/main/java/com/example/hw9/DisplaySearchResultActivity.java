package com.example.hw9;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DisplaySearchResultActivity extends AppCompatActivity {

    private List<Item> itemList = new ArrayList<>();
    RecyclerViewAdapterSearchResult myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_arrow_back));
        toolbar.setTitle("Search Results");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestItem();
    }


    private void showRecyclerView() {
/*
        itemList = new ArrayList<>();
        itemList.add(new Item("a","APPLE IPHONE X 256GB \"FACTORY UNLOCKED\" EXCELLENT sogoodcoolgenius", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "New", "$0.00", "FreeShipping", "311500"));
        itemList.add(new Item("b", "APPLE IPHONE X 64GB A1901 GSM UNLOCKED EXCELLENT", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "Manufacture Refurbished", "$0.00", "FreeShipping", "311500"));
        itemList.add(new Item("c", "APPLE IPHONE X 64GB A1901 GSM UNLOCKED EXCELLENT", "https://upload.wikimedia.org/wikipedia/en/thumb/1/16/Zhejiang_University_Logo.svg/1200px-Zhejiang_University_Logo.svg.png", "Manufacture Refurbished", "$0.00", "FreeShipping", "311500"));
*/
        if (itemList.size() == 0) {
            findViewById(R.id.searchResultErrorText).setVisibility(View.VISIBLE);
            return;
        }
        String countMessage = "Showing <font color='#FF0000'>"+ itemList.size() +"</font> results for <font color='#FF0000'>" + getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE).split("&")[3].split("=")[1].replace("+", " ") + "</font>";
        TextView countMessageTextView = findViewById(R.id.countMessageTextView);
        countMessageTextView.setText(Html.fromHtml(countMessage));
        countMessageTextView.setVisibility(View.VISIBLE);

        RecyclerView myRecyclerView = findViewById(R.id.recyclerViewSearchResult);
        myRecyclerViewAdapter = new RecyclerViewAdapterSearchResult(this, itemList);
        myRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void requestItem() {
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Log.e(getString(R.string.TAG), message);
        String url = message;

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
                        findViewById(R.id.progressLayout).setVisibility(View.GONE);
                        showRecyclerView();

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

}
