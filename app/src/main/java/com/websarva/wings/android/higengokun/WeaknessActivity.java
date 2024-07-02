package com.websarva.wings.android.higengokun;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.websarva.wings.android.higengokun.enums.ScreenType;
import com.websarva.wings.android.higengokun.utils.NavigationManager;

public class WeaknessActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weakness);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));

        //ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // DrawerLayoutのトグル設定
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //アクションバーの文字変更
        //アクションバーにアイコンを表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.menu_weakness));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }
    }

    public void onClickWeaknessStart(View view) {
        Intent intent = new Intent(WeaknessActivity.this, QuestionActivity.class);
        intent.putExtra("Type", ScreenType.WeaknessActivity.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_optiion_2,menu);
        return true;
    }

    //サイドバーをタップした時の処理
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_desc) {
            navigationManager.navigateToActivity(this,DescriptionActivity.class);
        } else if (id == R.id.nav_category) {
            navigationManager.navigateToActivity(this,CategoryActivity.class);
            //navigateToActivity(CategoryActivity.class);
        } else if (id == R.id.nav_home) {
            navigationManager.navigateToActivity(this, MainActivity.class);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //アイコンをタップした時の処理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}