package com.letter.days.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.letter.days.R;
import com.letter.days.anniversary.AnniPagerTransformer;
import com.letter.days.anniversary.Anniversary;
import com.letter.days.anniversary.AnniversaryFragment;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class AnniversaryActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private ViewPager viewPager;

    private int anniId;
    private boolean isEdited = false;

    List<Anniversary> anniversaryList;
    List<AnniversaryFragment> anniversaryFragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anniversary);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle("");
        }

        anniId = getIntent().getIntExtra("anniId", 0);

        viewPager = findViewById(R.id.view_pager);

    }

    @Override
    protected void onResume() {
        super.onResume();

        anniversaryList = LitePal.findAll(Anniversary.class);
        anniversaryFragments = new ArrayList<>();
        for (Anniversary anniversary: anniversaryList) {
            anniversaryFragments.add(AnniversaryFragment.newInstance(anniversary.getId()));
        }
        AnniFragmentPagerAdapter<AnniversaryFragment> adapter =
                new AnniFragmentPagerAdapter<>(getSupportFragmentManager(), anniversaryFragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(getFragmentPositionById(anniId), true);
        viewPager.setPageTransformer(false, new AnniPagerTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                anniId = anniversaryFragments.get(viewPager.getCurrentItem()).getAnniId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.anniversary_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isEdited) {
                    intent = new Intent();
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;

            case R.id.delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(AnniversaryActivity.this);
                dialog.setMessage("确认删除这个纪念日？");
                dialog.setPositiveButton("确认", (dialogInterface, i) -> {
                        LitePal.delete(Anniversary.class, anniId);
                        Intent intent1 = new Intent();
                        setResult(RESULT_OK, intent1);
                        Toast.makeText(AnniversaryActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                dialog.setNegativeButton("取消", (dialogInterface, i) -> {});
                dialog.show();
                break;

            case R.id.edit:
                intent = new Intent(AnniversaryActivity.this, AddItemActivity.class);
                intent.putExtra("editType", AddItemActivity.ITEM_EDIT);
                intent.putExtra("anniId", anniId);
                startActivityForResult(intent, 1);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isEdited) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    isEdited = true;
                }
                break;

            default:
                break;
        }
    }

    private int getFragmentPositionById(int id) {
        for (int i = 0; i < anniversaryList.size(); i++) {
            if (anniversaryList.get(i).getId() == id) {
                return i;
            }
        }
        return 0;
    }

}
