package com.example.timetablesys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    int p = 0;
    Date date = new Date();
    Calendar c = Calendar.getInstance();
    int d = c.get(Calendar.DAY_OF_WEEK)-2;
    String days[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String day = days[d];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
        dayDisplay.setText(day);

        Resources res = getResources();
        InputStream file = res.openRawResource(R.raw.timetable);
        Scanner scanner = new Scanner(file);
        StringBuilder timetable = new StringBuilder();

        while (scanner.hasNextLine()){
            timetable.append(scanner.nextLine());
        }

        TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
        TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);

        try {
            JSONObject root = new JSONObject(timetable.toString());
            JSONObject timeTable = root.getJSONObject(day);
            JSONArray classes = timeTable.getJSONArray("timetable");
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date time = new Date();
            String currTime = formatter.format(time);
            LocalTime curTime = LocalTime.parse(currTime+":00");
            for(int i = 0; i < classes.length(); i++){
                JSONObject period = classes.getJSONObject(i);
                String clsTime = period.getString("time");
                LocalTime classTime = LocalTime.parse(clsTime+":00");
                int value = curTime.compareTo(classTime);
                if(value>0){
                    timeDisplay.setText(period.getString("time"));
                    perDisplay.setText(period.getString("subject"));
                    p = i;
                }
            }

        } catch (JSONException e){
            e.printStackTrace();
        }

        perDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                InputStream file = res.openRawResource(R.raw.timetable);
                Scanner scanner = new Scanner(file);
                StringBuilder timetable = new StringBuilder();

                while (scanner.hasNextLine()){
                    timetable.append(scanner.nextLine());
                }

                try {
                    JSONObject root = new JSONObject(timetable.toString());
                    JSONObject timeTable = root.getJSONObject(day);
                    JSONArray classes = timeTable.getJSONArray("timetable");
                    JSONObject period = classes.getJSONObject(p);
                    String link = period.getString("link");
                    Uri uri = Uri.parse(link);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        Button startBtn = (Button) findViewById(R.id.startBtn);
        Button nextBtn = (Button) findViewById(R.id.nxtBtn);
        Button prevBtn = (Button) findViewById(R.id.prvBtn);
        Button nxtDay = (Button) findViewById(R.id.nxtDay);
        Button prvDay = (Button) findViewById(R.id.prvDay);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                p = 0;
                Resources res = getResources();
                InputStream file = res.openRawResource(R.raw.timetable);
                Scanner scanner = new Scanner(file);
                StringBuilder timetable = new StringBuilder();

                while (scanner.hasNextLine()) {
                    timetable.append(scanner.nextLine());
                }

                parseJson(timetable.toString());
            }

            public void parseJson(String s) {
                TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
                TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);
                TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
                dayDisplay.setText(day);
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject timetable = root.getJSONObject(day);
                    JSONArray classes = timetable.getJSONArray("timetable");
                    JSONObject period = classes.getJSONObject(p);
                    timeDisplay.setText(period.getString("time"));
                    perDisplay.setText(period.getString("subject"));
                    perDisplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Resources res = getResources();
                            InputStream file = res.openRawResource(R.raw.timetable);
                            Scanner scanner = new Scanner(file);
                            StringBuilder timetable = new StringBuilder();

                            while (scanner.hasNextLine()){
                                timetable.append(scanner.nextLine());
                            }

                            try {
                                JSONObject root = new JSONObject(timetable.toString());
                                JSONObject timeTable = root.getJSONObject(day);
                                JSONArray classes = timeTable.getJSONArray("timetable");
                                JSONObject period = classes.getJSONObject(p);
                                String link = period.getString("link");
                                Uri uri = Uri.parse(link);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                InputStream file = res.openRawResource(R.raw.timetable);
                Scanner scanner = new Scanner(file);
                StringBuilder timetable = new StringBuilder();
                while (scanner.hasNextLine()) {
                    timetable.append(scanner.nextLine());
                }
                goNext(timetable.toString());
            }

            public void goNext(String s) {
                TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
                TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);
                TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
                dayDisplay.setText(day);
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject timetable = root.getJSONObject(day);
                    JSONArray classes = timetable.getJSONArray("timetable");
                    if (classes.length() - 1 > p) {
                        p++;
                        JSONObject period = classes.getJSONObject(p);
                        timeDisplay.setText(period.getString("time"));
                        perDisplay.setText(period.getString("subject"));
                        perDisplay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Resources res = getResources();
                                InputStream file = res.openRawResource(R.raw.timetable);
                                Scanner scanner = new Scanner(file);
                                StringBuilder timetable = new StringBuilder();

                                while (scanner.hasNextLine()){
                                    timetable.append(scanner.nextLine());
                                }

                                try {
                                    JSONObject root = new JSONObject(timetable.toString());
                                    JSONObject timeTable = root.getJSONObject(day);
                                    JSONArray classes = timeTable.getJSONArray("timetable");
                                    JSONObject period = classes.getJSONObject(p);
                                    String link = period.getString("link");
                                    Uri uri = Uri.parse(link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Done for the day!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                InputStream file = res.openRawResource(R.raw.timetable);
                Scanner scanner = new Scanner(file);
                StringBuilder timetable = new StringBuilder();
                while (scanner.hasNextLine()) {
                    timetable.append(scanner.nextLine());
                }
                goPrev(timetable.toString());

            }

            public void goPrev(String s) {
                TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
                TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);
                TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
                dayDisplay.setText(day);
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject timetable = root.getJSONObject(day);
                    JSONArray classes = timetable.getJSONArray("timetable");
                    if (p > 0) {
                        p--;
                        JSONObject period = classes.getJSONObject(p);
                        timeDisplay.setText(period.getString("time"));
                        perDisplay.setText(period.getString("subject"));
                        perDisplay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Resources res = getResources();
                                InputStream file = res.openRawResource(R.raw.timetable);
                                Scanner scanner = new Scanner(file);
                                StringBuilder timetable = new StringBuilder();

                                while (scanner.hasNextLine()){
                                    timetable.append(scanner.nextLine());
                                }

                                try {
                                    JSONObject root = new JSONObject(timetable.toString());
                                    JSONObject timeTable = root.getJSONObject(day);
                                    JSONArray classes = timeTable.getJSONArray("timetable");
                                    JSONObject period = classes.getJSONObject(p);
                                    String link = period.getString("link");
                                    Uri uri = Uri.parse(link);
                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                    startActivity(intent);

                                } catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        nxtDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d < 4){
                    d++;
                    day = days[d];
                    p = 0;
                    Resources res = getResources();
                    InputStream file = res.openRawResource(R.raw.timetable);
                    Scanner scanner = new Scanner(file);
                    StringBuilder timetable = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        timetable.append(scanner.nextLine());
                    }
                    parseJson(timetable.toString());
                }
            }
            public void parseJson(String s) {
                TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
                TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);
                TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
                dayDisplay.setText(day);
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject timetable = root.getJSONObject(day);
                    JSONArray classes = timetable.getJSONArray("timetable");
                    JSONObject period = classes.getJSONObject(p);
                    timeDisplay.setText(period.getString("time"));
                    perDisplay.setText(period.getString("subject"));
                    perDisplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Resources res = getResources();
                            InputStream file = res.openRawResource(R.raw.timetable);
                            Scanner scanner = new Scanner(file);
                            StringBuilder timetable = new StringBuilder();

                            while (scanner.hasNextLine()){
                                timetable.append(scanner.nextLine());
                            }

                            try {
                                JSONObject root = new JSONObject(timetable.toString());
                                JSONObject timeTable = root.getJSONObject(day);
                                JSONArray classes = timeTable.getJSONArray("timetable");
                                JSONObject period = classes.getJSONObject(p);
                                String link = period.getString("link");
                                Uri uri = Uri.parse(link);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
        prvDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d > 0){
                    d--;
                    day = days[d];
                    p = 0;
                    Resources res = getResources();
                    InputStream file = res.openRawResource(R.raw.timetable);
                    Scanner scanner = new Scanner(file);
                    StringBuilder timetable = new StringBuilder();

                    while (scanner.hasNextLine()) {
                        timetable.append(scanner.nextLine());
                    }
                    parseJson(timetable.toString());
                }
            }
            public void parseJson(String s) {
                TextView timeDisplay = (TextView) findViewById(R.id.timeDisplay);
                TextView perDisplay = (TextView) findViewById(R.id.periodDisplay);
                TextView dayDisplay = (TextView) findViewById(R.id.dayDisplay);
                dayDisplay.setText(day);
                try {
                    JSONObject root = new JSONObject(s);
                    JSONObject timetable = root.getJSONObject(day);
                    JSONArray classes = timetable.getJSONArray("timetable");
                    JSONObject period = classes.getJSONObject(p);
                    timeDisplay.setText(period.getString("time"));
                    perDisplay.setText(period.getString("subject"));
                    perDisplay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Resources res = getResources();
                            InputStream file = res.openRawResource(R.raw.timetable);
                            Scanner scanner = new Scanner(file);
                            StringBuilder timetable = new StringBuilder();

                            while (scanner.hasNextLine()){
                                timetable.append(scanner.nextLine());
                            }

                            try {
                                JSONObject root = new JSONObject(timetable.toString());
                                JSONObject timeTable = root.getJSONObject(day);
                                JSONArray classes = timeTable.getJSONArray("timetable");
                                JSONObject period = classes.getJSONObject(p);
                                String link = period.getString("link");
                                Uri uri = Uri.parse(link);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);

                            } catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}






