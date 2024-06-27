package com.websarva.wings.android.higengokun;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.websarva.wings.android.higengokun.models.Question;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.websarva.wings.android.higengokun.enums.Category;
import com.websarva.wings.android.higengokun.utils.OriginalRowAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    ListView lvCategory;


    private List<Map<String,String>> _categoryList;


    private Category actionMode = Category.ALL;


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

        //タップを無効化する
        MenuItem item1 = menu.findItem(R.id.trackContext1);
        MenuItem item2 = menu.findItem(R.id.trackContext2);
        MenuItem item3 = menu.findItem(R.id.trackContext3);

        item1.setEnabled(false);
        item2.setEnabled(false);
        item3.setEnabled(false);


        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;


        /*
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int position = info.position;

        // 長押しされたリスト項目の位置に応じてメニューの表示を変更する
        Map<String, String> selectedItem = _categoryList.get(position);
        String questionText = selectedItem.get("question");

        menu.findItem(R.id.trackContext1).setTitle("Question: " + questionText);
        menu.findItem(R.id.trackContext2).setTitle("Position: " + position);
        menu.findItem(R.id.trackContext3).setTitle("Custom Option for Item " + position);*/

        //見えなくする
        /*item1.setVisible(false);
        item2.setVisible(false);
        item3.setVisible(false);*/


        // スタイルを適用する
        applyCustomStyleForMenuItems(menu);
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
}

