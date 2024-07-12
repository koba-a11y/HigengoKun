package com.websarva.wings.android.higengokun;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.websarva.wings.android.higengokun.utils.NavigationManager;

import java.time.temporal.WeekFields;

public class DescriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationManager navigationManager;
    private MediaPlayer mediaPlayer;

    TextView tvDescOverviewWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationManager = NavigationManager.getInstance();
        navigationManager.setDrawerLayout(findViewById(R.id.drawer_layout));

        // MediaPlayerのインスタンスを作成してBGMを再生
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm_main);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();


        //ナビゲーション(side)の設定
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // DrawerLayoutのトグル設定
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //アクションバーの文字変更
        //アイコンを表示
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.ab_desc));
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        //リンクを有効にする
        tvDescOverviewWeb = findViewById(R.id.tvDescOverviewWeb);
        tvDescOverviewWeb.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //サイドバーをタップした時の処理
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            navigationManager.navigateToActivity(this, MainActivity.class);
        } else if (id == R.id.nav_category) {
            navigationManager.navigateToActivity(this,CategoryActivity.class);
        } else if (id == R.id.nav_weakness) {
            navigationManager.navigateToActivity(this, WeaknessActivity.class);
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

    @Override
    protected void onPause(){
        super.onPause();
        // アクティビティ終了時にMediaPlayerとSoundPoolを解放
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // アクティビティ終了時にMediaPlayerとSoundPoolを解放
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }



}