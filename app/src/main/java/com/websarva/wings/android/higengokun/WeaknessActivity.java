package com.websarva.wings.android.higengokun;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.websarva.wings.android.higengokun.utils.NavigationManager;

public class WeeknessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekness);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));

        //ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //アクションバーの文字変更
        //アクションバーにアイコンを表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.ab_welcome));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }
    }
}