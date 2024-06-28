package com.websarva.wings.android.higengokun;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.websarva.wings.android.higengokun.models.Question;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.websarva.wings.android.higengokun.enums.Category;
import com.websarva.wings.android.higengokun.utils.OriginalRowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class CategoryActivity extends AppCompatActivity {

    ListView lvCategory;
    TextView tvCategoryTitle;


    private List<Map<String,String>> _categoryList;


    private Category actionMode = Category.ALL;
    //項目のR値
    int[] menuItems = {R.id.trackContext1, R.id.trackContext2, R.id.trackContext3};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        //アクションバーの文字変更
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.menu_list_category) + getString(R.string.menu_list_options_all));
        }



        lvCategory = findViewById(R.id.lvCategory);
        tvCategoryTitle = findViewById(R.id.tvCategoryTitle);

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
            switch (item.getItemId()) {
                case android.R.id.home:
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
            intent.putExtra("pos",position);
            startActivity(intent);

        }
    }

    //trackContextの設定
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.track_context_menu_list, menu);
        menu.setHeaderTitle(R.string.track_context_title);


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;
        Queue<Boolean> recent = Question.getRecentAnswers(actionMode, position);


        //Queueの要素数で項目を表示、非表示
        int size = recent.size();
        for(int i = 0; i < menuItems.length; i++) {
            MenuItem item =menu.findItem(menuItems[i]);
            if(i < size) {
                item.setVisible(true);
                if(recent.poll()){
                    item.setTitle(R.string.track_context_true);
                }else {
                    item.setTitle(R.string.track_context_false);
                }
            }else {
                item.setVisible(false);
            }
        }

        // 長押しされたリスト項目の位置に応じてメニューの表示を変更する
        /*Map<String, String> selectedItem = _categoryList.get(position);
        String questionText = selectedItem.get("question");

        menu.findItem(R.id.trackContext1).setTitle("Question: " + questionText);
        menu.findItem(R.id.trackContext2).setTitle("Position: " + position);
        menu.findItem(R.id.trackContext3).setTitle("Custom Option for Item " + position);*/


        // カスタムスタイルを適用するアイテムを取得
        MenuItem trackContextNew = menu.findItem(R.id.trackContextNew);
        MenuItem trackContextOld = menu.findItem(R.id.trackContextOld);

        // スタイルを適用する
        applyCustomStyleForMenuItems(menu);
        applyCustomStyleGray(trackContextNew);
        applyCustomStyleGray(trackContextOld);

    }

    //なんかスタイル適用するやつ
    private void applyCustomStyleForMenuItems(ContextMenu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(item.getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spanString.length(), 0); // 黒色に設定
            item.setTitle(spanString);
        }
    }
    private void applyCustomStyleGray(MenuItem item) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, spanString.length(), 0); // テキストカラーを赤色に設定
        spanString.setSpan(new AbsoluteSizeSpan(11, true), 0, spanString.length(), 0); // 文字サイズを設定
        item.setTitle(spanString);
    }
}

