package edu.pdx.cs410J.velapure;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadMeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_me);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView readMeText = findViewById(R.id.read_me_text);
        setContentInReadMeTextView(readMeText);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setContentInReadMeTextView(TextView readMeText) {
        StringBuilder readmeStringBuilder = new StringBuilder();
        try {
            InputStream readme = this.getAssets().open("README.txt");
            if (readme != null) {
                BufferedReader readmeReader = new BufferedReader(new InputStreamReader(readme));
                String readLine = readmeReader.readLine();
                while (readLine != null) {
                    readmeStringBuilder.append(readLine);
                    readmeStringBuilder.append("\n");
                    readLine = readmeReader.readLine();
                }
                readMeText.setText(readmeStringBuilder.toString());
            } else {
                Toast.makeText(this, "Error occurred while reading from the README file!!", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error occurred while reading from the README file!!", Toast.LENGTH_LONG).show();
        }
    }
}