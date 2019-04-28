package com.example.hw9;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.ToDoubleBiFunction;

public class MainActivity extends AppCompatActivity {


    private static final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 0;
    private TabSearchformFragment tabForm = new TabSearchformFragment();
    private TabWishlistFragment tabList = new TabWishlistFragment();
    private SearchForm searchform = tabForm.getSearchForm();
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getLocation();
        initTab();
        getZip();

    }
    public void initTab() {
        ViewPager viewPager = findViewById(R.id.viewPagerMain);
        TabLayout tabLayout = findViewById(R.id.tabLayoutMain);

        TabAdapter adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(tabForm, "SEARCH");
        adapter.addFragment(tabList, "WISH LIST");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void sendMessage(View view) {
        if (!searchform.isValid()) {
            if( searchform.getKeyword().trim().length() == 0){
                findViewById(R.id.errorMessageKeyword).setVisibility(View.VISIBLE);
            }
            if( searchform.getZipRadio() && searchform.getCustomZip().trim().length() != 5){
                findViewById(R.id.errorMessageZip).setVisibility(View.VISIBLE);
            }
            showToast("Please fix all fields with errors");
            return;
        } else {
            clearErrorMessage();
        }

        //Intent intent = new Intent(this, DisplayDetailResultActivity.class);


        Intent intent = new Intent(this, DisplaySearchResultActivity.class);
        String message = searchform.getSearchUrl();
        //String message = searchform.showInstance();
        intent.putExtra(EXTRA_MESSAGE, message);



        startActivity(intent);
    }

    public void resetSearchForm(View view) {
        searchform.reset();
        clearErrorMessage();

        //TODO: Remember not to clear the wishlist
        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);;
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.PREFERENCE_PATH), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    public void showToast(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(context, text, duration).show();
    }

    public void clearErrorMessage() {
        findViewById(R.id.errorMessageKeyword).setVisibility(View.GONE);
        findViewById(R.id.errorMessageZip).setVisibility(View.GONE);
    }
    public void getZip() {

    // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://ip-api.com/json/";
    // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.e(getString(R.string.TAG), response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
/*                            Log.e(getString(R.string.TAG), jsonObj.toString());
                            if (jsonObj.has("zip")) {
                                Log.e(getString(R.string.TAG), jsonObj.getString("zip"));
                            } else {
                                Log.e(getString(R.string.TAG), "no such item");
                            }*/
                            searchform.setCurrentZip(jsonObj.getString("zip"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(getString(R.string.TAG),"That didn't work!");
                Context context = getApplicationContext();
                CharSequence text = "Connection failed.\nPlease check your Internet.";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(context, text, duration).show();
                //finish();
            }
        });

// Add the request to the RequestQueue.

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                2000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(stringRequest);
    }

    public void getLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            getLocationFromGPS();
        }
    }

    public void getLocationFromGPS() {
        GPSTracker mGPS = new GPSTracker(this);
        if(mGPS.canGetLocation ){
            mGPS.getLocation();
            Log.e(getString(R.string.TAG), "Lat"+mGPS.getLatitude()+"Lon"+mGPS.getLongitude());
        }else{
            Log.e(getString(R.string.TAG),"can't get location");
        }
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSIONS_ACCESS_FINE_LOCATION);
/*        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(getString(R.string.TAG), "permission granted");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getLocationFromGPS();
                } else {
                    Log.e(getString(R.string.TAG), "permission not granted");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
