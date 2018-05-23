package com.amit.wiprocodetest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amit.wiprocodetest.Adapters.MyRecyclerViewAdapter;
import com.amit.wiprocodetest.Interfaces.ItemInterface;
import com.amit.wiprocodetest.module.Items;
import com.amit.wiprocodetest.module.Row;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.my_recycler_view)
    RecyclerView myRecyclerView;
    //Not able to bind with butterknife
    SwipeRefreshLayout mSwipeRefreshLayout ;
    List<Row> row;
    Retrofit retrofit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Binding the Butterknife after view has been cretaed
        ButterKnife.bind(this);
        //Initializing toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Set the LinearLayoutManger
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Initialize SwipeRefreshLayout and setOnRefreshListener
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(MainActivity.this);


        /*Using Retrofit to fetch the JSON data
        * Build the retrofit object*/
         retrofit = new Retrofit.Builder()
                .baseUrl(ItemInterface.URL)
                .addConverterFactory(GsonConverterFactory.create()) //User GSON Parsing
                .build();

        //load the data in RecyclerView by using Retrofit
        loadDataIntoRecyclerView();

    }



    /************************HANDLING ACTIVITY LIFE CYCLES *************************/

    @Override
    protected void onStart() {
        super.onStart();
        /*Check for internet connection when app is launched.
        If data is not turned on redirect to switch on data.
        Otherwise end the app*/
        checkInternetConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        /*Handle the application when back pressed
        * Avoid restart of app while bringing it back to foreground*/
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister the listeners to avoid memory leak
        mSwipeRefreshLayout.setOnRefreshListener(null);
    }






    /************************METHOD IMPLEMENTATIONS *************************/

    /*Implementation of Retrofit */
    private void loadDataIntoRecyclerView() {
        //Implement Interface
        ItemInterface itemInterface = retrofit.create(ItemInterface.class);

        Call<Items> call = itemInterface.getItems();

        call.enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Call<Items> call, Response<Items> response) {
                Log.e("MAIN Success", response.toString());
                Items items = response.body();
                getSupportActionBar().setTitle(items.getTitle());
                row = items.getRows();
                myRecyclerView.setAdapter(new MyRecyclerViewAdapter(getApplicationContext(), row));
            }

            @Override
            public void onFailure(Call<Items> call, Throwable t) {
                Log.e("MAIN Error", t.toString());
            }
        });
    }


    /*Implement onRefresh from SwipeRefreshLayout.OnRefreshListener
             * to Refresh the RecyclerView */
    @Override
    public void onRefresh() {

        //Check internet connection when refreshed
        checkInternetConnection();

        new MyRecyclerViewAdapter(getApplicationContext(),row).notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                Log.e("Main", "Refreshed");
            }
        }, 3000);
        Log.e("Main", "Refreshing");
    }



    /*Check for internet connection when app is launched
    * If data is not turned on redirect to switch on data
    * otherwise end the app*/
    private void checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.need_internet_conn);
            builder.setMessage(R.string.tun_on_data);
            builder.setCancelable(false);
            builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.show();
        }
    }


    /*Try to load data after returning from Settings page.
    * Successful if data connection exists*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //load the data in RecyclerView by using Retrofit
        loadDataIntoRecyclerView();
    }
}
