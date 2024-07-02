package com.websarva.wings.android.higengokun;



import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.websarva.wings.android.higengokun.enums.ScreenType;
import com.websarva.wings.android.higengokun.models.Question;
import com.websarva.wings.android.higengokun.utils.*;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button btCalendar;
    TextView tvMainTitle;
    TextView tvPlayedCheck;
    private final String fileName = "login.txt";
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btCalendar = findViewById(R.id.btCalendar);
        tvPlayedCheck = findViewById(R.id.tvPlayedCheck);
        tvMainTitle = findViewById(R.id.tvMainTitle);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));


        //ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        // すべてのquestionsの回答履歴を読み込む
        for (int i = 0; i < Question.NumberOfQuestions; i++) {
            Question question = Question.getQuestion(i);
            if (question != null) {
                question.loadRecentAnswers(this);
            }
        }

        // DrawerLayoutのトグル設定
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // アクションバーにナビゲーションアイコンを表示
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }


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
        intent.putExtra("Type", ScreenType.MainActivity.getId());
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

    //サイドバーをタップした時の処理
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_desc) {
            navigationManager.navigateToActivity(this,DescriptionActivity.class);
        } else if (id == R.id.nav_category) {
            navigationManager.navigateToActivity(this,CategoryActivity.class);
        } else if (id == R.id.nav_weakness) {
            navigationManager.navigateToActivity(this, WeaknessActivity.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //アイコンをタップした時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

