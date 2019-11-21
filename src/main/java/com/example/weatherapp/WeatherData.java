package com.example.weatherapp;

import android.os.AsyncTask;


import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherData extends AsyncTask<String, Void, String> { //Async for background web stuff standard api


    @Override
    protected String doInBackground(String... strings) { //this method gets the json data from the api and puts into a string
        String result = ""; //JSON data
        URL url;
        HttpURLConnection urldata = null; //this will be input from the main

        try { //if user has no connection
            url = new URL(strings[0]); //takes in url from parameter

            urldata = (HttpURLConnection) url.openConnection(); //deals with connection to web

            InputStream in = urldata.getInputStream(); //standard inputstream

            InputStreamReader rdr = new InputStreamReader(in); //reader instance

            int data = rdr.read(); //basically a count variable

            while (data != -1){ //transferring everything in json file to data
                char current = (char) data;
                result += current; //JSON data in String form
                data = rdr.read();

            }



        } catch (Exception e) { //generalized because there are a bunch of exception possibilities here
            e.printStackTrace();
        }


        return result;
    }

    protected void onPostExecute (String result){ //takes the unadulterated json data and sorts into useful info after bkgrnd
        super.onPostExecute(result);          //standard but necessary? comeback later
        try{
            JSONObject fullJ = new JSONObject(result); //entire file
            JSONObject tmp = new JSONObject(fullJ.getString("main")); //all temperature data

            Double temp = Double.parseDouble(tmp.getString("temp"));  //temp right now
            int finaltemp = (int) temp.intValue();


            String weather = fullJ.getString("weather");            //type of weather e.g. clouds
            String finl="";
            int count = 0; //stupid parsing problem I have to go through
            int index = 0;
            while(count<6){
                if(count==5&&weather.charAt(index)!='"'){
                    finl=finl.concat(Character.toString(weather.charAt(index)));
                }
                if(weather.charAt(index)=='"'){
                    count++;
                }
                index++;
            }
            switch (finl){  //for the int output
                case "Clouds":
                    MainActivity.wType=0;
                    MainActivity.im.setImageResource(R.drawable.cloudy);
                    break;
                case "Clear":
                    MainActivity.wType=1;
                    MainActivity.im.setImageResource(R.drawable.sunny);
                    break;
                case "Drizzle":
                    MainActivity.wType=2;
                    MainActivity.im.setImageResource(R.drawable.drizzle);
                    break;
                case "Rain":
                    MainActivity.wType=3;
                    MainActivity.im.setImageResource(R.drawable.rain);
                    break;
                case "Snow":
                    MainActivity.wType=4;
                    MainActivity.im.setImageResource(R.drawable.snowy);
                    break;
                case "Thunderstorm":
                    MainActivity.wType=5;
                    break;
                case "Haze":
                    MainActivity.wType=6;
                    MainActivity.wType=0;
                    MainActivity.im.setImageResource(R.drawable.cloudy);
                    break;
                case "Dust":
                    MainActivity.wType=7;
                    MainActivity.wType=0;
                    MainActivity.im.setImageResource(R.drawable.cloudy);
                    break;
                case "Smoke":
                    MainActivity.wType=8;
                    break;
                case "Mist":
                    MainActivity.wType=9;
                    MainActivity.wType=0;
                    MainActivity.im.setImageResource(R.drawable.cloudy);
                    break;
            }



            MainActivity.tempTV.setText(""+finaltemp+"° F");
            MainActivity.weatherTV.setText(finl);
            MainActivity.degrees=finaltemp;

        } catch (Exception e) { //too lazy to be more specific
            e.printStackTrace();
        }

    }
}
