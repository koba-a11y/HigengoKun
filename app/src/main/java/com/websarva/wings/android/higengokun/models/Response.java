package com.websarva.wings.android.higengokun.models;

import com.websarva.wings.android.higengokun.enums.Category;

import java.io.Serializable;
import java.util.Arrays;

//intentで情報を渡すためにSerializableをimplements
public class Response implements Serializable {

    public int[] r_ID;//出題された問題のID
    public Category[] r_category;//問題カテゴリ
    public int[] r_choice;//選択した回答
    public boolean[] r_ans;//true or false
    public String r_time;//回答時間(いったんstr)
    public int r_tcnt;//true count


    public Response(int[] r_ID, Category[] r_category, int[] r_choice, boolean[] r_ans, String time, int r_tcnt) {
        this.r_ID = Arrays.copyOf(r_ID, 10);
        this.r_category = Arrays.copyOf(r_category, 10);
        this.r_choice = Arrays.copyOf(r_choice, 10);
        this.r_ans = Arrays.copyOf(r_ans, 10);
        this.r_time = time;
        this.r_tcnt = r_tcnt;
    }

}
