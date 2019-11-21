package com.example.weatherapp;

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

public class MainActivity extends AppCompatActivity {

    //interactive UI stuff
    static TextView tempTV;
    static TextView weatherTV;
    static TextView skTV;
    Switch s1;
    Switch s2;
    Switch s3;
    static ImageView im;
    SeekBar sk;

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

        WeatherData weather = new WeatherData();  //weatherdata instance
        weather.execute("http://api.openweathermap.org/data/2.5/weather?q=Fresno,us&APPID=87e3c3bb474cc0be0e7747a2d9d2f1fb&units=imperial"); //api key

       /* switch (wType){   didn't end up working
            case 0:      //clouds
                im.setImageResource(R.drawable.cloudy);
                break;
            case 1:      //clear
                im.setImageResource(R.drawable.sunny);
                break;
            case 2:      //drizzle
                im.setImageResource(R.drawable.drizzle);
                break;
            case 3:      //rain
                im.setImageResource(R.drawable.rain);
                break;
            case 4:      //snow
                im.setImageResource(R.drawable.snowy);
                break;
        }
       */
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
