package com.websarva.wings.android.higengokun.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.websarva.wings.android.higengokun.MainActivity;
import com.websarva.wings.android.higengokun.R;

public class NavigationManager {
    private static NavigationManager instance;
    private DrawerLayout drawerLayout;

    private NavigationManager() {
        // インスタンスの生成をプライベート化してSingleton を実現
    }

    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            instance = new NavigationManager();
        }
        return instance;
    }

    public void setDrawerLayout(DrawerLayout drawerLayout) {
        this.drawerLayout = drawerLayout;
    }

    public void navigateToActivity(Context context, Class<?> targetActivity) {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        // 100msのディレイを挟んで画面遷移を行う
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(context, targetActivity);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            if(targetActivity == MainActivity.class){
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    ((AppCompatActivity) context).finish();
                }
            }else {
                if (context instanceof AppCompatActivity) {
                    ((AppCompatActivity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }, 170);
    }
}
