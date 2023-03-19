package edu.pdx.cs410J.velapure;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class PrettyPrintAirlineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pretty_print_airline);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent airlineIntent = getIntent();
        String airlinePrettyData = airlineIntent.getStringExtra(SearchAirlineAndFlightActivity.AIRLINE_PRETTY_TEXT_VALUE);

        TextView prettyPrintText = findViewById(R.id.pretty_print_text_view);
        prettyPrintText.setText(airlinePrettyData);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}