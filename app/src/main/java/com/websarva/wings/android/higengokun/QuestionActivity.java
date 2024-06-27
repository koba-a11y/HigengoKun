package com.websarva.wings.android.higengokun;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.websarva.wings.android.higengokun.enums.Category;
import com.websarva.wings.android.higengokun.models.Question;
import com.websarva.wings.android.higengokun.models.Response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/*TODO
* WeekActivityから遷移したことがわかる処理
*   intentにbefore等を追加してenumにアクティビティの値を設定する？
*   posに関連する処理を直す
* posの名前の変更
*   一つ前の画面所法をbeforeでやるならposの名前を変える。(分かりにくすぎる)
* */

public class QuestionActivity extends AppCompatActivity {


    LocalTime time;
    Button btNext;
    TextView tvCount;
    TextView tvQuestion;
    ListView lvAnsGroup;
    ActionBar actionBar;
    ImageView ivResult;
    Handler handler;

    private int pos ;

    private Response response;
    private Question question;
    private Timer timer;

    private int cnt = 1;
    private int arryindex;




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        tvCount = findViewById(R.id.tvCount);
        tvQuestion = findViewById(R.id.tvQuestion);

        btNext = findViewById(R.id.btNext);

        ivResult = findViewById(R.id.ivResult);

        lvAnsGroup = findViewById(R.id.lvansGroup);
        arryindex = 0;

        handler = new Handler();




        //アクションバー設定
        actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(Integer.valueOf(cnt).toString() + getString(R.string.ab_qcnt));
        }

        //パディング設定
        int extraPaddingTopBottom = 15; // 任意の追加サイズ（ピクセル）
        int extraPaddingSide = 30;
        tvQuestion.setPadding(tvQuestion.getPaddingLeft() + extraPaddingSide,
                tvQuestion.getPaddingTop() + extraPaddingTopBottom,
                tvQuestion.getPaddingRight() + extraPaddingSide,
                tvQuestion.getPaddingBottom() + extraPaddingTopBottom);



        //フォント
        Typeface nikkyou = Typeface.createFromAsset(getAssets(), "NikkyouSans-mLKax.ttf");
        tvCount.setTypeface(nikkyou);
        //レスポンスオブジェクト作成
        response = new Response(new int[10], new Category[10], new int[10], new boolean[10], "", 0);
        //乱数初期化
        Question.makernd();


        //問題処理
        Intent intent = getIntent();
        if(intent != null) {
            pos = (int) intent.getSerializableExtra("pos");
            if(-1 != pos) {
                btNext.setVisibility(View.INVISIBLE);
                question = Question.getQuestion(pos);
                actionBar.setTitle("カテゴリー:"+Question.getCategory(Question.geterCategory(question)));
            }else {
                question = Question.getQuestion(Question.rndlist.get(arryindex));
            }
        }

        //回答履歴読み込み
        question.loadRecentAnswers(this);

        show();

        //タイマー処理
        setTimer();


        //リスナ登録
        lvAnsGroup.setOnItemClickListener(new ListItemClickListener());


    }

    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            if(position == Question.geterAindex(question)) {
                ivResult.setImageResource(R.drawable.animal_quiz_neko_maru);
                popResult();
                response.r_ans[cnt-1] = true;
                response.r_tcnt += 1;

            }else {
                ivResult.setImageResource(R.drawable.animal_quiz_neko_batsu);
                popResult();
                response.r_ans[cnt-1] = false;
            }
            response.r_choice[cnt-1] = position;
            if(pos == -1) {//MainActivityからの遷移
                lvAnsGroup.setEnabled(false);
                question.addAnswer(response.r_ans[cnt-1]);//回答履歴の保存
                question.saveRecentAnswers(QuestionActivity.this);
            }

            btNext.setEnabled(true);
        }
    }

    void show() {
        if(question != null) {
            Log.d("QuestionActivity.", "show(): "+question.getRecentAnswers());
            response.r_ID[cnt-1] = question.rndlist.get(arryindex);
            response.r_category[cnt-1] = Question.geterCategory(question);
            lvAnsGroup.setEnabled(true);
            tvQuestion.setText(getString(Question.geterRid(question)));
            ArrayAdapter<String>adapter = new ArrayAdapter<>(QuestionActivity.this, android.R.layout.simple_list_item_1, Question.geterChoices(question));
            lvAnsGroup.setAdapter(adapter);
            arryindex++;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void setTimer () {
        /*タイマー処理*/
        //既に Start ボタンが押されてタイマーが起動中の場合
        if (timer != null) {
            return;
        }
        final Handler handler = new Handler();
        //ストップウォッチの時間をリセット
        time = LocalTime.of(0, 0);
        //100m.sec ごとに画面を更新する
        int period = 100;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //ストップウォッチの時間に 100m.sec 加算
                time = time.plusNanos((long) (period * Math.pow(10, 6)));
                //UIの更新はメインスレッドで行う必要があるため、Handlerを使用する
                handler.post(() -> {
                    //画面に現在の経過時間を表示
                    String fmt = time.format(DateTimeFormatter.ofPattern("mm:ss"));
                    tvCount.setText(fmt);
                });
            }
        }, 0, period);
    }

    public void onClickNext(View view) {
        cnt++;
        if(cnt > 10) {//10問終了
            response.r_time = (String) tvCount.getText();
            Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
            intent.putExtra("Res", response);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }else {
            actionBar.setTitle(Integer.valueOf(cnt).toString() + getString(R.string.ab_qcnt));
            question = Question.getQuestion(Question.rndlist.get(arryindex));
            show();
            btNext.setEnabled(false);
        }
    }


    //アクションバー戻るボタン
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean returnVal = true;
        int itemId = item.getItemId();
        if(itemId == android.R.id.home) {
            finish();
        } else {
            returnVal = super.onOptionsItemSelected(item);
        }
        return returnVal;
    }

    private void popResult(){
        // フェードインアニメーション
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500); // 0.5秒かけてフェードイン

        lvAnsGroup.setEnabled(false);
        ivResult.setVisibility(View.VISIBLE);
        ivResult.startAnimation(fadeIn);

        // 2秒後にフェードアウトアニメーションを開始する
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // フェードアウトアニメーション
                AlphaAnimation fadeOut = new AlphaAnimation(1, 0);
                fadeOut.setDuration(500); // 0.5秒かけてフェードアウト
                fadeOut.setAnimationListener(new AlphaAnimation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ivResult.setVisibility(View.GONE);
                        if(pos != -1) {
                            lvAnsGroup.setEnabled(true);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });

                ivResult.startAnimation(fadeOut);
            }
        }, 2000); // 2秒後に実行

    }
}