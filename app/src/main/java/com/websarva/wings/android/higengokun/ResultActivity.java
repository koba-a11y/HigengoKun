package com.websarva.wings.android.higengokun;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.websarva.wings.android.higengokun.enums.Category;
import com.websarva.wings.android.higengokun.models.Response;
import com.websarva.wings.android.higengokun.utils.FileUtil;
import com.websarva.wings.android.higengokun.models.Question;
import com.websarva.wings.android.higengokun.utils.OriginalRowAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ResultActivity extends AppCompatActivity {

    Response response;

    TextView tvAnsResult;
    TextView tvTimeResult;
    ListView lvQuestion;


    private Question question;
    private List<Map<String,String>> _feedBackList;

    SimpleDateFormat date;

    private final String fileName = "login.txt";
    String today;

    int pos;
    int[] menuItems = {R.id.trackContext3, R.id.trackContext2, R.id.trackContext1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //アクションバー設定
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("リザルト！！！！");
        }

        tvAnsResult = findViewById(R.id.tvAnsResult);
        tvTimeResult = findViewById(R.id.tvTimeResult);
        lvQuestion = findViewById(R.id.lvQuestion);


        Date d = new Date();
        date = new SimpleDateFormat("yyyy/MM/dd");
        today = date.format(d);

        //フォント
        Typeface Ronde = Typeface.createFromAsset(getAssets(), "Ronde-B_square.otf");
        tvAnsResult.setTypeface(Ronde);
        tvTimeResult.setTypeface(Ronde);

        //Resを受け取る
        Intent intent = getIntent();
        if(intent != null) {
            response = (Response) intent.getSerializableExtra("Res");
        }


        tvAnsResult.setText("10問中" +response.r_tcnt+ "問正解！！！");
        printTime(response.r_time);

        _feedBackList = createFeedBackList(response);
        OriginalRowAdapter adapter = new OriginalRowAdapter(this, _feedBackList);
        lvQuestion.setAdapter(adapter);
        lvQuestion.setOnItemClickListener(new ListItemClickListener());

        //コンテキスト設定
        registerForContextMenu(lvQuestion);
    }

    //アクションバー戻るボタン
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
            return true;
        } else {
            returnVal = super.onOptionsItemSelected(item);
        }
        return returnVal;
    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
            pos = response.r_ID[position];
            intent.putExtra("pos",pos);
            startActivity(intent);
        }
    }

    private List<Map<String,String>> createFeedBackList(Response res) {
        List<Map<String, String>> feedBackList = new ArrayList<>();
        Map<String, String> item = new HashMap<>();
        for(int i = 0; i < 10; i++) {
            question = Question.getQuestion(res.r_ID[i]);
            item = new HashMap<>();
            item.put("category", Question.getCategory(res.r_category[i]) + ":");
            item.put("question", getString(Question.geterRid(question)).substring(0, 9) + "・・・・・");
            if(res.r_ans[i]){
                item.put("feedback", i+1 + "問目　" + "正解！！！");
            }else {
                item.put("feedback", i+1 + "問目　" + "不正解！！");
            }
            feedBackList.add(item);
        }


        return feedBackList;
    }

    public void printTime(String time) {
        try {
            int total = Integer.parseInt(time.substring(time.length()-2)) + Integer.parseInt(time.substring(0,2))*60;
            int avg = total/10;
            String t_total;
            String t_avg;
            String sec = time.substring(time.length()-2) + "秒";
            String min = "";

            if(avg == 0) {
                tvTimeResult.setText("適当やめてね～～～～～");
                return;
            }

            if(total >= 60){//合計を秒から分に変換
                min = Integer.valueOf(total/60).toString() + "分";
            }
            t_total = min + sec;

            if(avg >= 60){//平均を秒から分に変換
                t_avg = Integer.valueOf(avg/60).toString() + "分" + Integer.valueOf(avg%60).toString() + "秒";
            }else {
                t_avg = Integer.valueOf(avg%60).toString() + "秒";
            }
            tvTimeResult.setText("合計:" + t_total + "\n" + "平均:" + t_avg + "\n" + "回答した問題！タップして解き直そう！");
            FileUtil.saveFile(this, fileName, today);

        }catch (NumberFormatException ex) {
            ex.printStackTrace();
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
        Queue<Boolean> recent = Question.getRecentAnswers(response.r_ID[position]);


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