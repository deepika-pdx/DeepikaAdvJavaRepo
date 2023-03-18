package edu.pdx.cs410J.velapure;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchNewAirlineFlightActivity(View view) {
        startActivity(new Intent(this, NewAirlineFlightActivity.class));
    }

    public void launchReadMeActivity(View view) {
        startActivity(new Intent(this, ReadMeActivity.class));
    }
}