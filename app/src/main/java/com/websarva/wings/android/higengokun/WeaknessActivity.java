package com.websarva.wings.android.higengokun;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.websarva.wings.android.higengokun.enums.Category;
import com.websarva.wings.android.higengokun.enums.ScreenType;
import com.websarva.wings.android.higengokun.models.Question;
import com.websarva.wings.android.higengokun.utils.NavigationManager;
import com.websarva.wings.android.higengokun.utils.OriginalRowAdapter;
import com.websarva.wings.android.higengokun.utils.TrackContextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeaknessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;
    private List<Map<String,String>> _weakList;

    TextView tvWeak;
    ListView lvWeak;
    ArrayList<Integer> numbers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weakness);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));

        tvWeak = findViewById(R.id.tvWeak);
        lvWeak = findViewById(R.id.lvWeak);

        numbers = new ArrayList<>();

        //フォントの変更
        Typeface ronde = Typeface.createFromAsset(getAssets(), "Ronde-B_square.otf");
        tvWeak.setTypeface(ronde);

        //ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // DrawerLayoutのトグル設定
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //アクションバーの文字変更
        //アクションバーにアイコンを表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.menu_weakness));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        //苦手問題があれば
        if(Question.countWeak() != 0) {
            tvWeak.setText("苦手問題は"+Question.countWeak()+"問です！");
            _weakList = createWeakList();
            OriginalRowAdapter adapter = new OriginalRowAdapter(this, _weakList);
            lvWeak.setAdapter(adapter);
            lvWeak.setOnItemClickListener(new ListItemClickListener());
            //コンテキスト設定
            registerForContextMenu(lvWeak);
        }else {
            lvWeak.setVisibility(View.GONE);
        }

    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(WeaknessActivity.this, QuestionActivity.class);
            position = numbers.get(position);
            intent.putExtra("Type", ScreenType.WeaknessActivity.getId());
            intent.putExtra("Number",position);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    public void onClickWeaknessStart(View view) {
        if(Question.countWeak() != 0) {
            Intent intent = new Intent(WeaknessActivity.this, QuestionActivity.class);
            intent.putExtra("Type", ScreenType.WeaknessActivity.getId());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }else {
            Toast.makeText(this, "苦手問題はありません", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_option,menu);
        return true;
    }

    //サイドバーをタップした時の処理
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_desc) {
            navigationManager.navigateToActivity(this,DescriptionActivity.class);
        } else if (id == R.id.nav_category) {
            navigationManager.navigateToActivity(this,CategoryActivity.class);
            //navigateToActivity(CategoryActivity.class);
        } else if (id == R.id.nav_home) {
            navigationManager.navigateToActivity(this, MainActivity.class);
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

    private List<Map<String, String>> createWeakList(){
        List<Map<String, String>> _weakList = new ArrayList<>();
        for(int i = 0; i< Question.NumberOfQuestions; i++) {
            if(Question.isWeekQuestion(Question.getRecentAnswers(i))){
                numbers.add(i);
                Log.d("WeaknessActivity", "Weak question found: " + i); // デバッグログを追加
                Log.d("WeaknessActivity", "Weak question Numbers: " + numbers.get(0)); // デバッグログを追加
                Question question = Question.getQuestion(i);
                Map<String, String> _weakItem = new HashMap<>();
                _weakItem.put("category", Question.getCategory(Question.geterCategory(question)) + ":");
                _weakItem.put("question", getString(Question.geterRid(question)).substring(0, 9) + "・・・・・");
                _weakList.add(_weakItem);
            }
        }
        return _weakList;
    }

    //trackContext
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        TrackContextUtil util = new TrackContextUtil();
        util.setTrackContext(menu,getMenuInflater(),Question.getRecentAnswers(numbers.get(position)));
    }
}