package ru.nofap.nofaptimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.text.format.Time;
import android.content.SharedPreferences;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {

    protected TextView textview1;
    protected Button button1;
    protected EditText edittext1;

    protected Time start = new Time();
    protected Time now = new Time();

    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start.setToNow();
        now.setToNow();

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String set = settings.getString("start", start.format("%s"));
        start.set(Long.valueOf(set+"000"));
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("start", start.format("%s"));

        textview1 = (TextView)findViewById(R.id.textView);
        button1 = (Button)findViewById(R.id.button);
        edittext1 = (EditText)findViewById(R.id.editText);
        edittext1.setText(start.format("%d.%m.%Y %H:%M:%S"));

        Timer myTimer = new Timer(); // Создаем таймер
        final Handler uiHandler = new Handler();
        final TextView txtResult = (TextView)findViewById(R.id.textView);
        myTimer.schedule(new TimerTask() { // Определяем задачу
            @Override
            public void run() {
                final String result = calcTime();
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        assert txtResult != null;
                        txtResult.setText(result);
                    }
                });
            };
        }, 0L, 350L); // интервал - 250 миллисекунд, 0 миллисекунд до первого запуска.
    }

    public void onClick(View view) {
        String str = edittext1.getText().toString();
        String[] info = str.split(" ");
        String[] date = info[0].split("\\.");
        String[] time = info[1].split(":");
        int sec, min, hour, day, month, year;
        sec = Integer.valueOf(time[2]);
        min = Integer.valueOf(time[1]);
        hour = Integer.valueOf(time[0]);
        day = Integer.valueOf(date[0]);
        month = Integer.valueOf(date[1]);
        year = Integer.valueOf(date[2]);;
        boolean date_check = (day>=1 && day<=31 && month>=1 && month<=12);
        boolean time_check = (sec>=0 && sec<60 && min>=0 && min<60 && hour>=0 && hour<24);
        if(date_check && time_check) {
            start.set(sec, min, hour, day, month-1, year);;
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("start", start.format("%s"));
            editor.apply();
        }
        else{
            System.out.println("error");
        }
    }

    public String calcTime() {
        now.setToNow();
        long d1 = Long.parseLong(start.format("%s"));
        long d2 = Long.parseLong(now.format("%s"));
        //now.toMillis(true);
        long res = d2 - d1;
        double days, hours, min, sec;
        String str = "";
        days = Math.floor(res/(60*60*24));
        hours = Math.floor((res-days*60*60*24)/(60*60));
        min = Math.floor((res-days*60*60*24-hours*60*60)/60);
        sec = Math.floor(res-days*60*60*24-hours*60*60-min*60);
        str += String.valueOf((int)days)+" д., ";
        str += String.valueOf((int)hours)+" ч., ";
        str += String.valueOf((int)min)+" м., ";
        str += String.valueOf((int)sec)+" с.";

        if(days < 1)
            str += "\nНовобранец";
        else if(days <= 1)
            str += "\nРядовой";
        else if(days <= 2)
            str += "\nЕфрейтор";
        else if(days <= 4)
            str += "\nМл. сержант";
        else if(days <= 7)
            str += "\nСержант";
        else if(days <= 10)
            str += "\nСт. сержант";
        else if(days <= 2*7)
            str += "\nСтаршина";
        else if(days <= 3*7)
            str += "\nПрапорщик";
        else if(days <= 1*30)
            str += "\nСт. прапорщик";
        else if(days <= 1.5*30)
            str += "\nМл. лейтенант";
        else if(days <= 2*30)
            str += "\nЛейтенант";
        else if(days <= 3*30)
            str += "\nСт. лейтенант";
        else if(days <= 4*30)
            str += "\nКапитан";
        else if(days <= 5*30)
            str += "\nМайор";
        else if(days <= 6*30)
            str += "\nПодполковник";
        else if(days <= 7*30)
            str += "\nПолковник";
        else if(days <= 8*30)
            str += "\nГенерал-майор";
        else if(days <= 10*30)
            str += "\nГенерал-лейтенант";
        else if(days <= 365)
            str += "\nГенерал-подполковник";
        else if(days <= 365+3*30)
            str += "\nГенерал армии";
        else
            str += "\nМаршал";

        return str;
        //return String.valueOf((d2 - d1)*3000);
    }
}