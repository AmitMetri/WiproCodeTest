package com.amit.wiprocodetest;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ItemInterface.URL)
                .addConverterFactory(GsonConverterFactory.create()) //User GSON Parsing
                .build();


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


  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
    }*/



    /*Implement onRefresh from SwipeRefreshLayout.OnRefreshListener
     * to Refresh the RecyclerView */
    @Override
    public void onRefresh() {

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
}
