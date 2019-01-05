package com.letter.days.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.letter.days.R;
import com.letter.days.anniversary.Anniversary;

import org.litepal.LitePal;

import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity {

    public static final int ITEM_ADD = 0;
    public static final int ITEM_EDIT = 1;

    private int editType;

    private Calendar selectCalender = Calendar.getInstance();

    private TextView textYear;
    private TextView textMouth;
    private TextView textDay;

    private ActionBar actionBar;

    private EditText editTextName;

    private LinearLayout dateChoose;

    private DatePickerDialog datePickerDialog;

    private AlertDialog.Builder typeDialog;

    private LinearLayout typeLayout;

    private TextView textType;

    private Anniversary anniversary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        actionBar = getSupportActionBar();
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
        selectCalender.setTimeInMillis(anniversary.getTime());
        freshDate(selectCalender);

        datePickerDialog = new DatePickerDialog(AddItemActivity.this);
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int mouth, int day) {
                selectCalender.set(year, mouth, day);
                anniversary.setTime(selectCalender.getTimeInMillis());
                freshDate(selectCalender);
            }
        });

        dateChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        textType = findViewById(R.id.type);
        textType.setText(Anniversary.typeText[anniversary.getType()]);

        typeDialog = new AlertDialog.Builder(this).setTitle("纪念日类型")
                .setItems(Anniversary.typeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textType.setText(Anniversary.typeText[i]);
                        anniversary.setType(i);
                    }
                });


        typeLayout = findViewById(R.id.type_layout);
        typeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeDialog.show();
            }
        });

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

    private void freshDate(Calendar calendar) {
        textYear = findViewById(R.id.text_year);
        textMouth = findViewById(R.id.text_mouth);
        textDay = findViewById(R.id.text_day);

        textYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        textMouth.setText(String.valueOf(calendar.get(Calendar.MONTH) + 1));
        textDay.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
    }
}
