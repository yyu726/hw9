package com.example.hw9;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.wssholmes.stark.circular_score.CircularScoreView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class TabShippingFragment extends Fragment {
    // TODO: needs to be modified
    private Item item;
    private JSONObject detailObj;
    TextView temp = null;
    private String[] shippingData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        return inflater.inflate(R.layout.fragment_shipping, container, false);
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

    private void loadShippingData() {
        shippingData = new String[12];

        try{
            shippingData[0] = detailObj.getJSONObject("Item").getJSONObject("Storefront").getString("StoreName") + "¥" + detailObj.getJSONObject("Item").getJSONObject("Storefront").getString("StoreURL");
        } catch (JSONException e){
            shippingData[0] = "N/A¥ ";
        }

        try{
            shippingData[1] = detailObj.getJSONObject("Item").getJSONObject("Seller").getString("FeedbackScore");
        } catch(JSONException e){
            shippingData[1] = "";
        }

        try{
            shippingData[2] = detailObj.getJSONObject("Item").getJSONObject("Seller").getString("PositiveFeedbackPercent");
        }catch(JSONException e){
            shippingData[2] = "";
        }

        try{
            shippingData[3] = detailObj.getJSONObject("Item").getJSONObject("Seller").getString("FeedbackRatingStar");
        }catch(JSONException e){
            shippingData[3] = "";
        }

        try{
            shippingData[4] = item.getShipping();
        }catch(Exception e){
            shippingData[4] = "";
        }

        try{
            shippingData[5] = detailObj.getJSONObject("Item").getString("GlobalShipping");
            if(shippingData[5].equals("false")){
                shippingData[5]="No";
            }else{
                shippingData[5]="Yes";
            }
        }catch(JSONException e){
            shippingData[5] = "";
        }

        try{
            shippingData[6] = detailObj.getJSONObject("Item").getString("HandlingTime");
            if(Integer.parseInt(shippingData[6]) <= 1){
                shippingData[6] = shippingData[6] + " day";
            }else{
                shippingData[6] = shippingData[6] + " days";
            }
        }catch(JSONException e){
            shippingData[6] = "";
        }

        try{
            shippingData[7] = detailObj.getJSONObject("Item").getString("ConditionDescription");
        }catch(JSONException e){
            shippingData[7] = "";
        }

        try{
            shippingData[8] = detailObj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ReturnsAccepted");
        }catch(JSONException e){
            shippingData[8] = "";
        }

        try{
            shippingData[9] = detailObj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ReturnsWithin");
        }catch(JSONException e){
            shippingData[9] = "";
        }

        try{
            shippingData[10] = detailObj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("Refund");
        }catch(JSONException e){
            shippingData[10] = "";
        }

        try{
            shippingData[11] = detailObj.getJSONObject("Item").getJSONObject("ReturnPolicy").getString("ShippingCostPaidBy");
        }catch(JSONException e){
            shippingData[11] = "";
        }
    }

    private void loadData() {


        loadShippingData();



        String storeName = shippingData[0];
        String[] store = storeName.split("¥");
        storeName = store[0];
        String storeURL = store[1];
        String feedbackScore = shippingData[1];
        String popularity = shippingData[2];
        String feedbackStar = shippingData[3];
        String shippingCost = shippingData[4];
        String globalShipping = shippingData[5];
        String handlingTime = shippingData[6];
        String condition = shippingData[7];
        String policy = shippingData[8];
        String returnsWithin = shippingData[9];
        String refundMode = shippingData[10];
        String shippedBy = shippingData[11];

        try{
            ImageView staricon = getView().findViewById(R.id.star_content);
            HashMap<String, Integer> map = new HashMap<>();
            map.put("Blue", R.drawable.ic_starcircle_outline_blue);
            map.put("Red", R.drawable.ic_starcircle_outline_red);
            map.put("Green", R.drawable.ic_starcircle_outline_green);
            map.put("Purple", R.drawable.ic_starcircle_outline_purple);
            map.put("Turquoise", R.drawable.ic_starcircle_outline_turquoise);
            map.put("Yellow", R.drawable.ic_starcircle_outline_yellow);
            map.put("GreenShooting", R.drawable.ic_starcircle_green);
            map.put("PurpleShooting", R.drawable.ic_starcircle_purple);
            map.put("RedShooting", R.drawable.ic_starcircle_red);
            map.put("SilverShooting", R.drawable.ic_starcircle_sliver);
            map.put("TurquoiseShooting", R.drawable.ic_starcircle_turquoise);
            map.put("YellowShooting", R.drawable.ic_starcircle_yellow);
            if (map.containsKey(feedbackStar)) {
                staricon.setImageResource(map.get(feedbackStar));
            } else {
                TableRow starrow = getView().findViewById(R.id.star_row);
                starrow.setVisibility(starrow.GONE);
            }

        }catch(Exception e){
        }

        try{
            Log.e("teststar", "loadData: "+popularity);
            CircularScoreView circularScoreView = (CircularScoreView) getView().findViewById(R.id.pop_content);

            circularScoreView.setScore((int) Math.round(Double.parseDouble(popularity)));

        }catch(Exception e){
        }
        try {
            temp = getView().findViewById(R.id.store_name_content);
            String helper = "<a href='"+storeURL+"'>"+storeName+"</a>";
            temp.setText(storeName);
            temp.setText(Html.fromHtml(helper));
            temp.setMovementMethod(LinkMovementMethod.getInstance());
            temp = getView().findViewById(R.id.score_content);
            temp.setText(feedbackScore);
            temp = getView().findViewById(R.id.shipping_cost_content);
            temp.setText(shippingCost);
            temp = getView().findViewById(R.id.global_shipping_content);
            temp.setText(globalShipping);
            temp = getView().findViewById(R.id.handling_content);
            temp.setText(handlingTime);
            temp = getView().findViewById(R.id.condition_content);
            temp.setText(condition);
            temp = getView().findViewById(R.id.policy_content);
            temp.setText(policy);
            temp = getView().findViewById(R.id.return_within_content);
            temp.setText(returnsWithin);
            temp = getView().findViewById(R.id.refund_mode_content);
            temp.setText(refundMode);
            temp = getView().findViewById(R.id.shipped_by_content);
            temp.setText(shippedBy);
        }catch(Exception e){
        }
        try {
            View hr1 = getView().findViewById(R.id.hr1);
            LinearLayout soldby = getView().findViewById(R.id.soldby);
            View hr2 = getView().findViewById(R.id.hr2);
            LinearLayout shipping_info = getView().findViewById(R.id.shipping_info);
            LinearLayout return_policy = getView().findViewById(R.id.return_policy);

            hr1.setVisibility(hr1.VISIBLE);
            soldby.setVisibility(soldby.VISIBLE);
            hr2.setVisibility(hr2.VISIBLE);
            shipping_info.setVisibility(shipping_info.VISIBLE);
            return_policy.setVisibility(return_policy.VISIBLE);

            if(storeName.length()==0){
                TableRow temp = getView().findViewById(R.id.store_name_row);
                temp.setVisibility(temp.GONE);
            }
            if(feedbackScore.length()==0){
                TableRow temp = getView().findViewById(R.id.score_row);
                temp.setVisibility(temp.GONE);
            }
            if(popularity.length()==0){
                TableRow temp = getView().findViewById(R.id.pop_row);
                temp.setVisibility(temp.GONE);
            }
            if(feedbackStar.length()==0){
                TableRow temp = getView().findViewById(R.id.star_row);
                temp.setVisibility(temp.GONE);
            }
            if(storeName.length()+feedbackScore.length()+popularity.length()+feedbackStar.length()==0){
                hr1.setVisibility(hr1.GONE);
                soldby.setVisibility(soldby.GONE);
            }

            if(shippingCost.length()==0){
                TableRow temp = getView().findViewById(R.id.shipping_cost_row);
                temp.setVisibility(temp.GONE);
            }
            if(globalShipping.length()==0){
                TableRow temp = getView().findViewById(R.id.global_shipping_row);
                temp.setVisibility(temp.GONE);
            }
            if(handlingTime.length()==0){
                TableRow temp = getView().findViewById(R.id.handling_row);
                temp.setVisibility(temp.GONE);
            }
            if(condition.length()==0){
                TableRow temp = getView().findViewById(R.id.condition_row);
                temp.setVisibility(temp.GONE);
            }
            if(shippingCost.length()+globalShipping.length()+handlingTime.length()+condition.length()==0){
                hr2.setVisibility(hr1.GONE);
                shipping_info.setVisibility(soldby.GONE);
            }

            if(policy.length()==0){
                TableRow temp = getView().findViewById(R.id.policy_row);
                temp.setVisibility(temp.GONE);
            }
            if(returnsWithin.length()==0){
                TableRow temp = getView().findViewById(R.id.return_within_row);
                temp.setVisibility(temp.GONE);
            }
            if(refundMode.length()==0){
                TableRow temp = getView().findViewById(R.id.refund_mode_row);
                temp.setVisibility(temp.GONE);
            }
            if(shippedBy.length()==0){
                TableRow temp = getView().findViewById(R.id.shipped_by_row);
                temp.setVisibility(temp.GONE);
            }
            if(policy.length()+shippedBy.length()+refundMode.length()+returnsWithin.length()==0){
                return_policy.setVisibility(soldby.GONE);
            }
        }catch(Exception e){

        }
    }

}
