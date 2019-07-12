package com.letter.days.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haibin.calendarview.Calendar;
import com.letter.days.R;
import com.letter.days.anniversary.AnniUtils;
import com.letter.days.anniversary.Anniversary;
import com.letter.days.widget.ColorPane;
import com.letter.days.widget.ColorPickerDialog;

import org.litepal.LitePal;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ITEM_ADD = 0;
    public static final int ITEM_EDIT = 1;

    private int editType;

    private Calendar calendar = new Calendar();

    private TextView textTime;

    private EditText editTextName;

    private LinearLayout dateChoose;
    private LinearLayout typeLayout;
    private LinearLayout lunarLayout;
    private LinearLayout colorLayout;

    private TextView textType;
    private Anniversary anniversary;
    private CheckBox lunarCheck;

    private ColorPane colorPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            actionBar.setTitle("");
        }

        Intent intent = getIntent();
        editType = intent.getIntExtra("editType", ITEM_ADD);
        if (editType == ITEM_EDIT) {
            anniversary = LitePal.find(Anniversary.class, intent.getIntExtra("anniId", 0));
        } else {
            anniversary = new Anniversary();
        }

        editTextName = findViewById(R.id.text_name);
        editTextName.setText(anniversary.getText());

        dateChoose = findViewById(R.id.date_choose);
        AnniUtils.setCalendarTime(calendar, anniversary.getTime());
        freshDate();

        dateChoose.setOnClickListener(this);

        textType = findViewById(R.id.type);
        textType.setText(Anniversary.typeText[anniversary.getType()]);

        typeLayout = findViewById(R.id.type_layout);
        typeLayout.setOnClickListener(this);

        lunarLayout = findViewById(R.id.lunar_layout);
        lunarLayout.setOnClickListener(this);
        lunarCheck = findViewById(R.id.lunar_check);
        lunarCheck.setChecked(anniversary.isLunar());

        colorLayout = findViewById(R.id.color_layout);
        colorLayout.setOnClickListener(this);
        colorPane = findViewById(R.id.theme_color);
        colorPane.setColor(anniversary.getColor());
    }

    @Override
    public void onClick(View v) {
        if (v == dateChoose) {
            DateDialog dialog = new DateDialog.Builder(AddItemActivity.this)
                    .setOnDateSetListener((year, month, day) -> {
                        calendar.setYear(year);
                        calendar.setMonth(month);
                        calendar.setDay(day);
                        anniversary.setTime(calendar.getTimeInMillis());
                        freshDate();
                    })
                    .create();
            dialog.show();
        } else if (v == typeLayout) {
            AlertDialog dialog = new AlertDialog.Builder(this).setTitle("纪念日类型")
                    .setItems(Anniversary.typeText, (dialogInterface, i) -> {
                            textType.setText(Anniversary.typeText[i]);
                            anniversary.setType(i);
                        }).create();
            dialog.show();
        } else if (v == lunarLayout) {
            lunarCheck.setChecked(!lunarCheck.isChecked());
            anniversary.setLunar(lunarCheck.isChecked());
            freshDate();
        } else if (v == colorLayout) {
            ColorPickerDialog dialog = new ColorPickerDialog.Builder(AddItemActivity.this)
                    .setOnColorSelectListener((color) -> {
                        colorPane.setColor(color);
                        anniversary.setColor(color);
                    })
                    .setColor(anniversary.getColor())
                    .create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;

            case R.id.save:
                EditText editText = findViewById(R.id.text_name);
                if (editText.getText().toString().equals("")) {
                    Toast.makeText(AddItemActivity.this, "纪念日内容不能为空", Toast.LENGTH_SHORT).show();
                    break;
                }
                anniversary.setText(editText.getText().toString());
                if (editType == ITEM_EDIT) {
//                    anniversary.setTime(anniversary.getTime() + 1);       //可解决不能修改时间为当前日期的bug
                    anniversary.update(anniversary.getId());
                } else {
                    anniversary.save();
                }
                Intent broadIntent  = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
                getApplicationContext().sendBroadcast(broadIntent);
                Toast.makeText(AddItemActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK, intent);
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void freshDate() {
        textTime = findViewById(R.id.text_time);

        textTime.setText(AnniUtils.getFormatDate(getString(R.string.date_format),
                getString(R.string.date_lunar_format),
                calendar, anniversary.isLunar()));

    }
}
