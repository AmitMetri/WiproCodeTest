package com.amit.wiprocodetest.Interfaces;

import com.amit.wiprocodetest.module.Items;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by dell on 5/19/2018.
 */

public interface ItemInterface {

    String URL = "https://dl.dropboxusercontent.com/s/2iodh4vg0eortkl/";

    @GET("facts")
    Call<Items> getItems();
}
