package com.quickinstasaver.app;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.quickinstasaver.app.fragment.FragmentHome;
import com.quickinstasaver.app.util.AppLangSessionManager;
import com.quickinstasaver.app.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FrameLayout fl_side_bar;
    private DrawerLayout drawer;
    private ClipboardManager clipBoard;
    String CopyIntent;
    AppLangSessionManager appLangSessionManager;


    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
        navigationViewSetup();

        appLangSessionManager = new AppLangSessionManager(this);
        CopyIntent = getIntent().getStringExtra("CopyIntent");
    }


    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(fl_side_bar.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void navigationViewSetup() {
        drawer = findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        setFragment(new FragmentHome(CopyIntent));
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initView() {
        fl_side_bar = findViewById(R.id.fl_side_bar);
        navigationView = findViewById(R.id.nav_view);
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }
    }

    private boolean checkPermissions(int type) {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new
                            String[listPermissionsNeeded.size()]), type);
            return false;
        } else {

        }
        return true;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        switch (item.getItemId()){
            case R.id.nav_rateus:
                Utils.RateApp(this);
                break;
            case R.id.nav_share:
                Utils.ShareApp(this);
                break;
            case R.id.nav_language:
                showLanguageBottom();
                break;
            case R.id.nav_privacy:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.quickinstasaver.blogspot.com/p/privacy-policy.html"));
                startActivity(intent);
                break;
            case R.id.nav_htd:
                Intent it = new Intent(this,HowToActivity.class);
                startActivity(it);
                break;
            case R.id.nav_about_developer:
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://quickinstasaver.blogspot.com/"));
                startActivity(i);
                break;
        }
        return false;
    }

    private void showLanguageBottom() {
        final BottomSheetDialog dialogSortBy = new BottomSheetDialog(MainActivity.this, R.style.SheetDialog);
        dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSortBy.setContentView(R.layout.dialog_language);
        final TextView tv_english = dialogSortBy.findViewById(R.id.tv_english);
        final TextView tv_hindi = dialogSortBy.findViewById(R.id.tv_hindi);
        final TextView tv_cancel = dialogSortBy.findViewById(R.id.tv_cancel);
        dialogSortBy.show();
        tv_english.setOnClickListener(view -> {
            setLocale("en");
            appLangSessionManager.setLanguage("en");
        });
        tv_hindi.setOnClickListener(view -> {
            setLocale("hi");
            appLangSessionManager.setLanguage("hi");
        });
        tv_cancel.setOnClickListener(view -> dialogSortBy.dismiss());

    }

    public void setLocale(String lang) {

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(MainActivity.this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

}