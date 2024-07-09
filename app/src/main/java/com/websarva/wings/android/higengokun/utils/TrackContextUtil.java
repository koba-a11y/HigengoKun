package com.websarva.wings.android.higengokun.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.websarva.wings.android.higengokun.R;

import java.util.Queue;

public class TrackContextUtil {
    //trackContextの設定

    //項目のR値
    private final int[] menuItems = {R.id.trackContext3, R.id.trackContext2, R.id.trackContext1};


    public void setTrackContext(ContextMenu menu, MenuInflater inflater, Queue<Boolean> recent){
        inflater.inflate(R.menu.track_context_menu_list, menu);
        menu.setHeaderTitle(R.string.track_context_title);

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
