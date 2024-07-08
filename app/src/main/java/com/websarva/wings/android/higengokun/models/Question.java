package com.websarva.wings.android.higengokun.models;



import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.websarva.wings.android.higengokun.R;
import com.websarva.wings.android.higengokun.enums.Category;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class Question  {
    public static final int NumberOfQuestions = 50;//配列の数+1

    private final Category q_category; //問題カテゴリ
    private final int q_RID;//問題のR値
    private final ArrayList<String> choices; //選択肢
    private final int ans_index;
    private Queue<Boolean> recentAnswers;//各問題直近3回の回答履歴

    private static final Question[] questions = new Question[NumberOfQuestions];
    public static ArrayList<Integer> _questionsList = new ArrayList<>();


    private Question(Category q_category, int q_RID, String[] choices, int ans_index) {
        this.q_category = q_category;
        this.q_RID = q_RID;
        this.choices = new ArrayList<>(Arrays.asList(choices));
        this.ans_index = ans_index;
        this.recentAnswers = new LinkedList<>();//初期化
    }

    public static Category geterCategory(Question question) {
        return question.q_category;
    }
    public static int geterRid(Question question) {
        return question.q_RID;
    }
    public static int geterAindex(Question question){
        return question.ans_index;
    }
    public static ArrayList<String> geterChoices(Question question) {
        return question.choices;
    }

    //問題の準備
    public static void makeQuestions() {
        for(int i = 0; i < NumberOfQuestions; i++){
            _questionsList.add(i);
        }
        Collections.shuffle(_questionsList);
    }

    //問題の登録
    public static void init() {
        //場合の数
        questions[0] = new Question(Category.BAAI, R.string.question_baai_1_1, new String[]{"12通り", "15通り", "18通り", "24通り", "36通り", "48通り", "72通り", "96通り"}, 1);
        questions[1] = new Question(Category.BAAI, R.string.question_baai_1_2, new String[]{"20通り", "30通り", "45通り", "60通り", "90通り", "120通り", "180通り", "240通り"}, 4);
        questions[2] = new Question(Category.BAAI, R.string.question_baai_2_1, new String[]{"15通り", "21通り", "37通り", "42通り", "74通り", "84通り", "148通り", "168通り"}, 4);
        questions[3] = new Question(Category.BAAI, R.string.question_baai_2_2, new String[]{"14通り", "20通り", "28通り", "35通り", "56通り", "70通り", "112通り", "140通り"}, 5);
        questions[4] = new Question(Category.BAAI, R.string.question_baai_3_1, new String[]{"6通り", "12通り", "18通り", "24通り", "36通り", "48通り", "72通り", "96通り"}, 3);
        questions[5] = new Question(Category.BAAI, R.string.question_baai_3_2, new String[]{"6通り", "12通り", "18通り", "24通り", "36通り", "48通り", "72通り", "96通り"}, 5);
        questions[6] = new Question(Category.BAAI, R.string.question_baai_4_1, new String[]{"36通り", "48通り", "72通り", "96通り", "144通り", "192通り", "256通り", "288通り"}, 1);
        questions[7] = new Question(Category.BAAI, R.string.question_baai_4_2, new String[]{"8通り", "12通り", "16通り", "18通り", "24通り", "36通り", "48通り", "96通り"}, 2);
        questions[8] = new Question(Category.BAAI, R.string.question_baai_5_1, new String[]{"12通り", "15通り", "24通り", "30通り", "36通り", "48通り", "60通り", "72通り"}, 6);
        questions[9] = new Question(Category.BAAI, R.string.question_baai_5_2, new String[]{"5通り", "8通り", "10通り", "16通り", "20通り", "32通り", "40通り", "64通り"}, 6);
        questions[10] = new Question(Category.BAAI, R.string.question_baai_6, new String[]{"6通り", "9通り", "12通り", "15通り", "18通り", "24通り", "36通り", "48通り"}, 3);
        questions[11] = new Question(Category.BAAI, R.string.question_baai_7, new String[]{"30通り", "40通り", "60通り", "80通り", "90通り", "120通り", "150通り", "180通り"}, 0);
        //濃度計算
        questions[12] = new Question(Category.NOUDO, R.string.question_noudo_1, new String[]{"16.8%", "17.0%", "17.2%", "17.4%", "17.5%", "17.6%", "17.8%", "18.0%"}, 2);
        questions[13] = new Question(Category.NOUDO, R.string.question_noudo_2, new String[]{"12.8%", "13.2%", "13.6","14.0%", "14.4%", "14.8%", "15.2%", "15.6%"}, 4);
        questions[14] = new Question(Category.NOUDO, R.string.question_noudo_3, new String[]{"50g", "75g", "100g", "125g", "150g", "175g", "200g", "225g"}, 2);
        questions[15] = new Question(Category.NOUDO, R.string.question_noudo_4, new String[]{"150g", "175g", "200g", "225g", "250g", "275g", "300g", "325g"}, 4);
        questions[16] = new Question(Category.NOUDO, R.string.question_noudo_5, new String[]{"55g", "60g", "65g", "70g", "75g", "80g", "85g", "90g"}, 4);
        questions[17] = new Question(Category.NOUDO, R.string.question_noudo_6, new String[]{"16.8%", "17.0%", "17.2%", "17.4%", "17.5%", "17.6%", "17.8%", "18.0%"}, 2);
        //確立
        questions[18] = new Question(Category.KAKURITU, R.string.question_kakuritu_1_1, new String[]{"2/7", "5/14", "3/7", "1/2", "4/7", "9/14", "5/7", "11/14"}, 0);
        questions[19] = new Question(Category.KAKURITU, R.string.question_kakuritu_1_2, new String[]{"2/7", "5/14", "3/7", "1/2", "4/7", "9/14", "5/7", "11/14"}, 4);
        questions[20] = new Question(Category.KAKURITU, R.string.question_kakuritu_2_1, new String[]{"0.18", "0.21", "0.24", "0.28", "0.32", "0.42", "0.54", "0.62"}, 3);
        questions[21] = new Question(Category.KAKURITU, R.string.question_kakuritu_2_2, new String[]{"0.18", "0.21", "0.24", "0.28", "0.32", "0.42", "0.54", "0.62"}, 6);
        questions[22] = new Question(Category.KAKURITU, R.string.question_kakuritu_3_1, new String[]{"1/16", "1/14", "1/12", "1/10", "1/9", "1/8", "1/7", "1/4"}, 1);
        questions[23] = new Question(Category.KAKURITU, R.string.question_kakuritu_3_2, new String[]{"4/49", "6/49", "8/49", "10/49", "12/49", "2/7", "16/49", "18/49"}, 1);
        questions[24] = new Question(Category.KAKURITU, R.string.question_kakuritu_4_1, new String[]{"16/169", "25/169", "36/169", "54/169", "68/169", "81/169", "88/169", "99/169"}, 6);
        questions[25] = new Question(Category.KAKURITU, R.string.question_kakuritu_4_2, new String[]{"1/78", "11/78", "11/39", "23/78", "14/39", "33/78", "17/39", "37/78"}, 3);
        questions[26] = new Question(Category.KAKURITU, R.string.question_kakuritu_5_1, new String[]{"1/16", "1/8", "1/4", "3/8", "1/2", "5/8", "3/4", "7/8"}, 6);
        questions[27] = new Question(Category.KAKURITU, R.string.question_kakuritu_5_2, new String[]{"1/18", "1/12", "1/9", "5/36", "1/6", "7/36", "2/9", "1/4"}, 4);
        questions[28] = new Question(Category.KAKURITU, R.string.question_kakuritu_6_1, new String[]{"1/216", "1/108", "1/72", "1/36", "1/24", "1/18", "1/12", "1/6"}, 3);
        questions[29] = new Question(Category.KAKURITU, R.string.question_kakuritu_6_2, new String[]{"1/36", "1/27", "5/108", "1/18", "7/108", "2/27", "1/12", "5/54"}, 2);
        //損益
        questions[30] = new Question(Category.SONEKI,R.string.question_soneki_1,new String[]{"1296円","1332円","1368円","1404円","1440円","1476円","1512円","1548円"}, 3);
        questions[31] = new Question(Category.SONEKI,R.string.question_soneki_2,new String[]{"1000円","1100円","1200円","1300円","1400円","1500円","1600円","1700円"}, 3);
        questions[32] = new Question(Category.SONEKI,R.string.question_soneki_3,new String[]{"1240円","1320円","1400円","1480円","1560円","1640円","1720円","1800円"}, 2);
        questions[33] = new Question(Category.SONEKI,R.string.question_soneki_4,new String[]{"2400円","2500円","2600円","2700円","2800円","2900円","3000円","3100円"}, 0);
        questions[34] = new Question(Category.SONEKI,R.string.question_soneki_5,new String[]{"32%","34%","36%","38%","40%","42%","44%","46%"}, 4);
        questions[35] = new Question(Category.SONEKI,R.string.question_soneki_6,new String[]{"154円","166円","178円","190円","202円","214円","226円","238円"}, 7);
        questions[36] = new Question(Category.SONEKI,R.string.question_soneki_7,new String[]{"5960円","6000円","6040円","6080円","6120円","6160円","6200円","6240円"}, 7);
        questions[37] = new Question(Category.SONEKI,R.string.question_soneki_8,new String[]{"100円","110円","120円","130円","140円","150円","160円","170円"}, 5);
        questions[38] = new Question(Category.SONEKI,R.string.question_soneki_9,new String[]{"4%","5%","6%","7%","8%","9%","10%","11%","12%"}, 4);
        //代金
        questions[39] = new Question(Category.DAIKIN,R.string.question_daikin_1,new String[]{"SはPに600円、Rに450円支払う","SはPに600円、Rに500円支払う","SはPに650円、Rに450円支払う","SはPに650円、Rに500円支払う","SはPに700円、Qに50円、Rに450円支払う","SはPに700円、Qに50円、Rに500円支払う","SはPに750円、Qに50円、Rに450円支払う","SはPに750円、Qに50円、Rに500円支払う"}, 6);
        questions[40] = new Question(Category.DAIKIN,R.string.question_daikin_2,new String[]{"6200円","6300円","6400円","6500円","6600円","6700円","6800円","6900円"}, 7);
        questions[41] = new Question(Category.DAIKIN,R.string.question_daikin_3,new String[]{"PがQに3000円支払う","PがQに3500円支払う","PがQに4000円支払う","PがQに4500円支払う","QがPに3000円支払う","QがPに3500円支払う","QがPに4000円支払う","QがPに4500円支払う"}, 7);
        questions[42] = new Question(Category.DAIKIN,R.string.question_daikin_4_1,new String[]{"ア：1000円、イ：4000円","ア：1000円、イ：5000円","ア：2000円、イ：3000円","ア：2000円、イ：4000円","ア：3000円、イ：2000円","ア：3000円、イ：3000円","ア：4000円、イ：1000円","ア：4000円、イ：2000円"}, 6);
        questions[43] = new Question(Category.DAIKIN,R.string.question_daikin_4_2,new String[]{"ウ：2000円、エ：5000円","ウ：2000円、エ：6000円","ウ：3000円、エ：5000円","ウ：3000円、エ：6000円","ウ：4000円、エ：5000円","ウ：4000円、エ：6000円","ウ：5000円、エ：5000円","ウ：5000円、エ：6000円"}, 4);
        questions[44] = new Question(Category.DAIKIN,R.string.question_daikin_5_1,new String[]{"2500円損をする","3500円損をする","4500円損をする","5500円損をする","2500円得をする","3500円得をする","4500円得をする","5500円得をする"}, 3);
        questions[45] = new Question(Category.DAIKIN,R.string.question_daikin_5_2,new String[]{"PがQに2000円支払い、RがPに1000円支払う","PがQに2000円支払い、RがPに2000円支払う","PがQに2000円支払い、RがPに3000円支払う","PがQに2000円支払い、RがPに4000円支払う","QがPに2000円支払い、RがPに1000円支払う","QがPに2000円支払い、RがPに2000円支払う","QがPに2000円支払い、RがPに3000円支払う","QがPに2000円支払い、RがPに4000円支払う"}, 6);
        //割引
        questions[46] = new Question(Category.WARIBIKI,R.string.question_waribiki_1,new String[]{"4900円", "5000円", "5100円", "5200円", "5300円", "5400円", "5500円", "5600円"}, 6);
        questions[47] = new Question(Category.WARIBIKI,R.string.question_waribiki_2,new String[]{"350時間", "400時間", "450時間", "500時間", "550時間", "600時間", "650時間", "700時間"}, 3);
        questions[48] = new Question(Category.WARIBIKI,R.string.question_waribiki_3,new String[]{"720円", "740円", "760円", "780円", "800円", "820円", "840円", "860円"}, 0);
        questions[49] = new Question(Category.WARIBIKI,R.string.question_waribiki_4,new String[]{"29000円", "30000円", "31000円", "32000円", "33000円", "34000円", "35000円", "36000円"}, 1);

    }

    //問題を取得する
    public static Question getQuestion(int num) {
        if(num >= questions.length) {
            return null;
        }
        return questions[num];
    }

    public static String getCategory(Category cat) {
        switch (cat) {
            case BAAI:
                return "場合の数";
            case NOUDO:
                return "濃度";
            case KAKURITU:
                return "確率";
            case SONEKI:
                return "損益";
            case DAIKIN:
                return "代金の清算";
            case WARIBIKI:
                return "料金の割引";
            default:
                return "エラー";
        }
    }

    private static int questionSearch(Category cat, int pos){
        int questionsIndex = 0;
        for (int i = 0; i < NumberOfQuestions; i++) {
            if (cat == questions[i].q_category) {
                if (questionsIndex == pos) {
                    return i;
                }
                questionsIndex++;
            }
        }
        return NumberOfQuestions - 1;
    }

    public static int getQuestionFromCategory(Category cat, int pos) {
        return questionSearch(cat,pos);
    }

    public static Queue<Boolean> getRecentAnswers(Category cat, int pos){
        if(cat != Category.ALL){
            pos = questionSearch(cat,pos);
        }

        return new LinkedList<>(questions[pos].recentAnswers);
    }

    //過去３回の回答履歴を取得
    public static Queue<Boolean> getRecentAnswers(int number) {
        return new LinkedList<>(questions[number].recentAnswers);
    }

    //直近３回の回答履歴を更新
    public void addAnswer(boolean isCorrect) {
        if(recentAnswers.size() >=3) {
            recentAnswers.poll();
        }
        recentAnswers.add(isCorrect);
    }

    public static void makeWeaknessList(){
        _questionsList.clear();
        for(int i = 0; i < NumberOfQuestions; i++) {
            if(isWeekQuestion(Question.questions[i].recentAnswers)){
                _questionsList.add(i);
            }
        }
        Collections.shuffle(_questionsList);
    }

    //苦手問題かどうかを判定
    public static boolean isWeekQuestion(Queue<Boolean> recent) {
        return recent.contains(false);
    }

    //回答履歴を保存
    public void saveRecentAnswers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AnswerTrackingPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(this.recentAnswers);
        editor.putString("recentAnswers_" + this.q_RID, json);
        editor.apply();
    }

    // 回答履歴を読み込み
    public void loadRecentAnswers(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AnswerTrackingPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("recentAnswers_" + this.q_RID, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<Queue<Boolean>>() {}.getType();
            this.recentAnswers = gson.fromJson(json, type);
        }
    }

    public static int countWeak(){
        int cnt = 0;

        for(int i = 0; i < NumberOfQuestions; i++){
            if(isWeekQuestion(getRecentAnswers(i))){
                cnt++;
            }
        }

        return cnt;
    }
}
