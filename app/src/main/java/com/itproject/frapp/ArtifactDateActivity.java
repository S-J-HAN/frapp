package com.itproject.frapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ArtifactDateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private final int MIN_YEAR = 1900;
    private final int NUM_DAYS = 31;
    private final int NUM_MONTHS = 12;

    private ArrayList years;
    private ArrayList months;
    private ArrayList days;

    private String selectedDate = null;
    private String selectedMonth = null;
    private String selectedYear = null;

    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artifact_date);

        // create lists for spinners and add initial display value
        this.days = createList(1, NUM_DAYS);
        days.add(0, "DD");
        this.months = createList(1, NUM_MONTHS);
        months.add(0,"MM");
        this.years = createList(MIN_YEAR, Calendar.getInstance().get(Calendar.YEAR));
        years.add(0, "YYYY");

        // days spinner
        Spinner daysSpinner = (Spinner) findViewById(R.id.daySpinner);
        ArrayAdapter<String> daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, days) {
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
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, years) {
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
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, months) {
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
                        Toast.makeText(getApplicationContext(), selectedDay, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedDate = selectedDay;
                    break;
                case R.id.monthSpinner:
                    Spinner monthSelectedSpinner = (Spinner) findViewById((int) id);
                    String selectedMonth = monthSelectedSpinner.getSelectedItem().toString();
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(), selectedMonth, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedMonth = selectedMonth;
                    break;
                case R.id.yearSpinner:
                    Spinner yearSelectedSpinner = (Spinner) findViewById((int) id);
                    String selectedYear = yearSelectedSpinner.getSelectedItem().toString();
                    if (position > 0) {
                        Toast.makeText(getApplicationContext(), selectedYear, Toast.LENGTH_SHORT).show();
                    }
                    this.selectedYear = selectedYear;
                    break;
                default:
                    break;
            }
        }

        if ((selectedDate == null) || (selectedMonth == null) || (selectedYear == null)) {

            String birthday = selectedDate + "/" + selectedMonth + "/" + selectedYear;

        }
    }


    public void onNothingSelected(AdapterView<?> parent) {

    }


    private ArrayList createList(int min, int max) {
        ArrayList list = new ArrayList();

        for (int i = min; i <= max; i++) {
            list.add(Integer.toString(i));
        }
        return list;
    }


    public void openDPSetting(View view) {
        Intent intent = new Intent(this, ChooseDPSetting.class);
        startActivity(intent);
        finish();
    }

    public void openArtifactDescription() {
        Intent intent = new Intent(this, ArtifactDescriptionActivity.class);
        startActivity(intent);

    }
}
