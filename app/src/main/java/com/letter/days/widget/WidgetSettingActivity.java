package com.letter.days.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.letter.days.R;
import com.letter.days.anniversary.Anniversary;
import com.letter.days.anniversary.OnAnniItemClickListener;
import com.letter.days.common.SpacesItemDecoration;

import org.litepal.LitePal;

import java.util.List;

public class WidgetSettingActivity extends AppCompatActivity {

    private int widgetId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_widget_setting);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("选择纪念日");
        }

        Intent intent = getIntent();
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        final List<Anniversary> anniversaryList = LitePal.findAll(Anniversary.class);
        Anniversary closestAnni = new Anniversary();
        closestAnni.setText("最近的纪念日");
        anniversaryList.add(0, closestAnni);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(1));
        WidgetInfoAdapter widgetInfoAdapter = new WidgetInfoAdapter(anniversaryList);
        widgetInfoAdapter.setOnAnniItemClickListener(new OnAnniItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // TODO: 处理列表点击事件
                WidgetInfo widgetInfo = new WidgetInfo();
                if (position == 0) {
                    widgetInfo.setType(WidgetInfo.WIDGET_TYPE_CLOSEST);
                } else {
                    widgetInfo.setType(WidgetInfo.WIDGET_TYPE_NORMAL);
                    widgetInfo.setAnniId(anniversaryList.get(position).getId());
                }
                widgetInfo.setWidgetId(widgetId);
                widgetInfo.save();
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                AnniversaryWidget.updateAppWidget(getApplicationContext(), appWidgetManager, widgetId);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
        recyclerView.setAdapter(widgetInfoAdapter);
    }
}
