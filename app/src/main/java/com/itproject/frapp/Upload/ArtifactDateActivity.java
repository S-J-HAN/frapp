package com.itproject.frapp.Upload;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.itproject.frapp.R;
import com.itproject.frapp.Schema.Artifact;
import com.itproject.frapp.Settings.ChooseDPSetting;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class ArtifactDateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static Activity dateActivity;
    private Artifact artifact;

    private final int MIN_YEAR = 1900;
    private final int NUM_DAYS = 31;
    private final int NUM_MONTHS = 12;

    private ArrayList years;
    private ArrayList months;
    private ArrayList days;

    private String selectedDate = null;
    private String selectedMonth = null;
    private String selectedYear = null;

    private ImageButton nextButton;
    private ImageView artifactImage;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_date);
        dateActivity = this;
        artifactImage = findViewById(R.id.artifactImageView);

        // Get artifact from ArtifactUploadActivity
        artifact = (Artifact) getIntent().getSerializableExtra("Artifact");
        System.out.println(artifact.getUrl());
        Glide.with(this)
            .load(artifact.getUrl())
            .fitCenter()
            .into(artifactImage);

        // create lists for spinners and add initial display value
        this.days = createList(1, NUM_DAYS);
        days.add(0, "DD");
        this.months = createList(1, NUM_MONTHS);
        months.add(0,"MM");
        this.years = (createList(MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR)));
        Collections.reverse(this.years);
        years.add(0, "YYYY");

        // days spinner
        Spinner daysSpinner = findViewById(R.id.daySpinner);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_dropdown_item, days) {
            @Override
            public boolean isEnabled(int pos) {
                if (pos == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int pos, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(pos, convertView, parent);
                TextView textview = (TextView) view;
                if (pos == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        daysSpinner.setAdapter(daysAdapter);
        daysSpinner.setOnItemSelectedListener(this);
        daysSpinner.setSelection(0);

        // years spinner
        Spinner yearsSpinner = (Spinner) findViewById(R.id.yearSpinner);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, years) {
            @Override
            public boolean isEnabled(int pos) {
                if (pos == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int pos, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(pos, convertView, parent);
                TextView textview = (TextView) view;
                if (pos == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        yearsSpinner.setAdapter(yearsAdapter);
        yearsSpinner.setOnItemSelectedListener(this);
        yearsSpinner.setSelection(0);


        // months spinner
        Spinner monthsSpinner = (Spinner) findViewById(R.id.monthSpinner);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, months) {
            @Override
            public boolean isEnabled(int pos) {
                if (pos == 0) {
                    return false;
                } else {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int pos, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(pos, convertView, parent);
                TextView textview = (TextView) view;
                if (pos == 0) {
                    textview.setTextColor(Color.GRAY);
                } else {
                    textview.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        monthsSpinner.setAdapter(monthsAdapter);
        monthsSpinner.setOnItemSelectedListener(this);
        monthsSpinner.setSelection(0);

        nextButton = findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = daysSpinner.getSelectedItem().toString();
                selectedMonth = monthsSpinner.getSelectedItem().toString();
                selectedYear = yearsSpinner.getSelectedItem().toString();
                // Add selected date to artifact
                artifact.setDate(selectedDate + "/" + selectedMonth + "/" + selectedYear);
                //Open settings page
                openArtifactDescription();
            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (findViewById((int) id) != null) {
            switch (parent.getId()) {
                case R.id.daySpinner:
                    Spinner daySelectedSpinner = (Spinner) findViewById((int) id);
                    String selectedDay = daySelectedSpinner.getSelectedItem().toString();
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(),
                                selectedDay, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedDate = selectedDay;
                    System.out.println("NEW DATE SELECTED!");
                    break;
                case R.id.monthSpinner:
                    Spinner monthSelectedSpinner = (Spinner) findViewById((int) id);
                    String selectedMonth = monthSelectedSpinner.getSelectedItem().toString();
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(),
                                selectedMonth, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedMonth = selectedMonth;
                    break;
                case R.id.yearSpinner:
                    Spinner yearSelectedSpinner = (Spinner) findViewById((int) id);
                    String selectedYear = yearSelectedSpinner.getSelectedItem().toString();
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(),
                                selectedYear, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedYear = selectedYear;
                    break;
                default:
                    break;
            }
        }
        // Handle if one of date isn't selected
        if ((selectedDate == null) || (selectedMonth == null) || (selectedYear == null)) {

        }
    }

    // =============================== HELPER FUNCTIONS ======================================

    public void onNothingSelected(AdapterView<?> parent) { }

    private ArrayList createList(int min, int max) {
        ArrayList list = new ArrayList();

        for (int i = min; i <= max; i++) {
            list.add(Integer.toString(i));
        }
        return list;
    }

    public void openArtifactDescription() {
        Intent intent = new Intent(this, ArtifactDescriptionActivity.class);
        intent.putExtra("Artifact", artifact);
        startActivity(intent);

    }
}
