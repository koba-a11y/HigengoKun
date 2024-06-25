package com.websarva.wings.android.higengokun;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.websarva.wings.android.higengokun.models.Question;
import com.websarva.wings.android.higengokun.utils.*;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Button btCalendar;
    TextView tvMainTitle;
    TextView tvPlayedCheck;
    private final String fileName = "login.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCalendar = findViewById(R.id.btCalendar);
        tvPlayedCheck = findViewById(R.id.tvPlayedCheck);
        tvMainTitle = findViewById(R.id.tvMainTitle);

        //アクションバーの文字変更
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.ab_welcome));
        }

        //フォントの変更
        Typeface ronde = Typeface.createFromAsset(getAssets(), "Ronde-B_square.otf");
        tvMainTitle.setTypeface(ronde);
        tvPlayedCheck.setTypeface(ronde);

        //問題初期化
        Question.init();


    }

    //カレンダー処理
    public void onClickCalendar(View view) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog;
        datePickerDialog = new DatePickerDialog(
                this, (view1, year, month, dayOfMonth) -> {
            String selectedDate = String.format(Locale.JAPAN, "%02d/%02d/%02d", year, month + 1, dayOfMonth);
            btCalendar.setText(selectedDate);

            // .txtの照合
            boolean doCheck = FileUtil.readFile(this, fileName, selectedDate);
            if (doCheck) {
                tvPlayedCheck.setText(getString(R.string.tv_played_check_true));
            } else {
                tvPlayedCheck.setText(getString(R.string.tv_played_check_flase));
            }
        },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }



    public void onClickStart(View view) {
        Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
        intent.putExtra("pos", -1);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onClickCategory(View view) {
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

    }

}

