package com.example.weatherapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    //interactive UI stuff
    static TextView tempTV;
    static TextView weatherTV;
    static TextView skTV;
    static TextView city;
    Switch s1;
    Switch s2;
    Switch s3;
    static ImageView im;
    SeekBar sk;
    WeatherData weather = new WeatherData();

    //int output variables
    int brightness=0; //seekbar progress
    int gambit=1; //color gambit
    static int degrees=000; //temp;
    static int wType = 0; //weathertype, key is in WeatherData

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        im = (ImageView) findViewById(R.id.imageView);
        skTV = (TextView) findViewById(R.id.skTV);
        tempTV = (TextView) findViewById(R.id.tempTV);
        weatherTV= (TextView) findViewById(R.id.wthTV);
        s1= (Switch) findViewById(R.id.switch1);
        s1.toggle();
        s2= (Switch) findViewById(R.id.switch2);
        s3= (Switch) findViewById(R.id.switch3);
        sk= (SeekBar) findViewById(R.id.seekBar);
        city= (TextView) findViewById(R.id.textView);


        weather.execute("http://api.openweathermap.org/data/2.5/weather?q=Fresno,us&APPID=87e3c3bb474cc0be0e7747a2d9d2f1fb&units=imperial"); //api key
        if (wifi()){
            city.setText("Fresno CA");
        }
        else city.setText("");


        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                brightness=i;
                skTV.setText("Brightness: \n"+i);
            }

            //unused but I can't get rid of them
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
    });

        s1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {  //all this makes sure at least and only 1 switch is toggled at any given time
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (s1.isChecked()){
                if(s2.isChecked()){
                    s2.toggle();
                }
                if(s3.isChecked()){
                    s3.toggle();
                }
            }
            if(!s1.isChecked()&&!s2.isChecked()&&!s3.isChecked()){
                s1.toggle();
            }
            gambit = 1;
            }
        });
        s3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (s3.isChecked()){
                    if(s2.isChecked()){
                        s2.toggle();
                    }
                    if(s1.isChecked()){
                        s1.toggle();
                    }
                }
                if(!s1.isChecked()&&!s2.isChecked()&&!s3.isChecked()){
                    s3.toggle();
                }
                gambit = 3;
            }
        });
        s2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (s2.isChecked()){
                    if(s1.isChecked()){
                        s1.toggle();
                    }
                    if(s3.isChecked()){
                        s3.toggle();
                    }
                }
                if(!s1.isChecked()&&!s2.isChecked()&&!s3.isChecked()){
                    s2.toggle();
                }
                gambit = 2;
            }
        });

        //int output
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String out1, out2, out3, out4="";
                if(brightness<=99&brightness>=10){    //always length 3 of brightness
                    out1="0"+brightness;
                }
                else if(brightness<10){
                    out1="00"+brightness;
                }
                else{
                    out1=""+brightness;
                }

                out2=""+gambit; //gambit length 1

                if(degrees<=99&&degrees>=10){ //degrees length 3
                    out3="0"+degrees;
                }
                else if(degrees<10){
                    out3="00"+degrees;
                }
                else{
                    out3=""+degrees;
                }

                out4=""+wType; //weather type length 1

                Snackbar.make(view, out1+out2+out3+out4, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //checks to see if there if device is connected to a wireless network
    public boolean wifi() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network temp = cm.getActiveNetwork();
        boolean rsl = temp != null;
        return rsl;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        city= (TextView) findViewById(R.id.textView);


        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.fresno:
                new WeatherData().execute("http://api.openweathermap.org/data/2.5/weather?q=Fresno,us&APPID=87e3c3bb474cc0be0e7747a2d9d2f1fb&units=imperial");

                if (wifi()==true){
                    city.setText("Fresno CA");
                    im.setVisibility(View.VISIBLE);
                }
                else {
                    tempTV.setText("No\nConnection");
                    weatherTV.setText("");
                    city.setText("");
                    im.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.ny:
                new WeatherData().execute("http://api.openweathermap.org/data/2.5/weather?lat=45.5202&lon=-122.6747&APPID=87e3c3bb474cc0be0e7747a2d9d2f1fb&units=imperial");

                if (wifi()) {
                    city.setText("Portland OR");
                    im.setVisibility(View.VISIBLE);
                }
                else{
                    tempTV.setText("No\nConnection");
                    weatherTV.setText("");
                    city.setText("");
                    im.setVisibility(View.INVISIBLE);
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
