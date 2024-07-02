package com.websarva.wings.android.higengokun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import java.util.Queue;

public class CategoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ListView lvCategory;
    TextView tvCategoryTitle;


    private List<Map<String,String>> _categoryList;


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;
    private Category actionMode = Category.ALL;
    //項目のR値
    int[] menuItems = {R.id.trackContext3, R.id.trackContext2, R.id.trackContext1};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        lvCategory = findViewById(R.id.lvCategory);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));

        // ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // DrawerLayoutのトグル設定
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //アクションバーの文字変更
        // アクションバーにナビゲーションアイコンを表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_all));
        }

        //フォントの変更
        Typeface ronde = Typeface.createFromAsset(getAssets(), "Ronde-B_square.otf");
        tvCategoryTitle.setTypeface(ronde);
        //リストを生成する
        _categoryList = createCategoryList(Category.ALL);
        //アダプタに自作レイアウトを設定する
        OriginalRowAdapter adapter = new OriginalRowAdapter(this, _categoryList);
        lvCategory.setAdapter(adapter);
        lvCategory.setOnItemClickListener(new ListItemClickListener());

        //コンテキスト設定
        registerForContextMenu(lvCategory);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_optiion_2,menu);
        return true;
    }

    //アクションバーをタップしたときの処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (toggle.onOptionsItemSelected(item)) {
                return true;
            }

            switch (item.getItemId()) {
                case R.id.action_home:
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                    return true;
                default:
                    // ここで選択されたカテゴリを取得
                    Category selectedCategory = getCategoryFromMenuItem(item.getItemId());
                    if (selectedCategory != null) {
                        updateCategory(selectedCategory);
                        _categoryList = createCategoryList(selectedCategory);
                        OriginalRowAdapter adapter = new OriginalRowAdapter(this, _categoryList);
                        lvCategory.setAdapter(adapter);
                        return true;
                    }
                    return super.onOptionsItemSelected(item);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_desc) {
            navigationManager.navigateToActivity(this, DescriptionActivity.class);
        } else if (id == R.id.nav_home) {
            navigationManager.navigateToActivity(this, MainActivity.class);
        } else if (id == R.id.nav_weakness) {
            navigationManager.navigateToActivity(this, WeaknessActivity.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private List<Map<String, String>> createCategoryList(Category category) {
        List<Map<String, String>> categoryList = new ArrayList<>();

        for (int i = 0; i < Question.NumberOfQuestions; i++) {
            Question question = Question.getQuestion(i);
            if (question != null && (category == Category.ALL || Question.geterCategory(question) == category)) {
                Map<String, String> categoryItem = new HashMap<>();
                categoryItem.put("category", Question.getCategory(Question.geterCategory(question)) + ":");
                categoryItem.put("question", getString(Question.geterRid(question)).substring(0, 9) + "・・・・・");
                categoryList.add(categoryItem);
            } else if (question != null && category.ordinal() < Question.geterCategory(question).ordinal()) {
                break;
            }
        }

        return categoryList;
    }

    //カテゴリーのアップデート
    private void updateCategory(Category category) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            switch (category) {
                case ALL:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_all));
                    break;
                case BAAI:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_baai));
                    break;
                case NOUDO:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_noudo));
                    break;
                case KAKURITU:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_kakuritu));
                    break;
                case SONEKI:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_soneki));
                    break;
                case DAIKIN:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_daikin));
                    break;
                case WARIBIKI:
                    actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_waribiki));
                    break;
                default:
                    break;
            }
            actionMode = category;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private Category getCategoryFromMenuItem(int itemId) {
        switch (itemId) {
            case R.id.menuListOptionAll:
                return Category.ALL;
            case R.id.menuListOptionBaai:
                return Category.BAAI;
            case R.id.menuListOptionNoudo:
                return Category.NOUDO;
            case R.id.menuListOptionKakuritu:
                return Category.KAKURITU;
            case R.id.menuListOptionSoneki:
                return Category.SONEKI;
            case R.id.menuListOptionDaikin:
                return Category.DAIKIN;
            case R.id.menuListoptionWaribiki:
                return Category.WARIBIKI;
            default:
                return null;
        }
    }



    private class ListItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(actionMode != Category.ALL)//アクションモードがallじゃない場合に関数実行
                position = Question.getQuestionFromCategory(actionMode,position);
            Intent intent = new Intent(CategoryActivity.this, QuestionActivity.class);
            intent.putExtra("Number",position);
            intent.putExtra("Type", ScreenType.CategoryActivity.getId());
            startActivity(intent);

        }
    }

    //trackContextの設定
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        TrackContextUtil util = new TrackContextUtil();
        util.setTrackContext(menu,getMenuInflater(),Question.getRecentAnswers(actionMode, position));
        }


}

