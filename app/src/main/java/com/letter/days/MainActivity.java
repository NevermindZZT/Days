package com.letter.days;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.letter.days.activity.AddItemActivity;
import com.letter.days.activity.AnniversaryActivity;
import com.letter.days.anniversary.AnniAdapter;
import com.letter.days.anniversary.Anniversary;
import com.letter.days.common.SpacesItemDecoration;
import com.letter.days.service.NotifyService;

import org.litepal.LitePal;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AnniAdapter anniAdapter;

    List<Anniversary> anniversaryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setExpandedTitleColor(0xff1a1a1a);
        collapsingToolbar.setCollapsedTitleTextColor(0xff1a1a1a);
        setSupportActionBar(toolbar);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        anniversaryList = LitePal.findAll(Anniversary.class);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        anniAdapter = new AnniAdapter(anniversaryList);
        anniAdapter.setOnAnniItemClickListener((position) -> {
            Intent intent = new Intent(MainActivity.this, AnniversaryActivity.class);
            intent.putExtra("anniId", anniversaryList.get(position).getId());
            startActivityForResult(intent, 1);
        });
        recyclerView.setAdapter(anniAdapter);

        final FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            intent.putExtra("editType", AddItemActivity.ITEM_ADD);
            startActivityForResult(intent, 1);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(this, NotifyService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    anniversaryList.clear();
                    anniversaryList.addAll(LitePal.findAll(Anniversary.class));
                    anniAdapter.notifyDataSetChanged();
                }
                break;

            default:
                break;
        }
    }

}
