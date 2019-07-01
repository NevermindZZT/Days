package com.letter.days.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.letter.days.R;
import com.letter.days.anniversary.Anniversary;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;

public class AnniversaryActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Anniversary anniversary;

    private TextView anniDate;
    private TextView anniDays;
    private TextView anniText;
    private TextView anniType;

    private int anniId;
    private int isEdited = 0;

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
        anniversary = LitePal.find(Anniversary.class, anniId);

        if (anniversary == null) {
            Toast.makeText(AnniversaryActivity.this, "发生错误", Toast.LENGTH_SHORT).show();
        } else {
            anniversary = new Anniversary();
        }

        anniText = findViewById(R.id.anni_text);
        anniDate = findViewById(R.id.anni_date);
        anniDays = findViewById(R.id.anni_days);
        anniType = findViewById(R.id.anni_type);

        freshData();
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
                if (isEdited == 1) {
                    intent = new Intent();
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;

            case R.id.delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(AnniversaryActivity.this);
                dialog.setMessage("确认删除这个纪念日？");
                dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LitePal.delete(Anniversary.class, anniversary.getId());
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        Toast.makeText(AnniversaryActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                break;

            case R.id.edit:
                intent = new Intent(AnniversaryActivity.this, AddItemActivity.class);
                intent.putExtra("editType", AddItemActivity.ITEM_EDIT);
                intent.putExtra("anniId", anniversary.getId());
                startActivityForResult(intent, 1);
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isEdited == 1) {
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
                    isEdited = 1;
                    freshData();
                }
                break;

            default:
                break;
        }
    }

    private void freshData() {
        anniversary = LitePal.find(Anniversary.class, anniId);
        anniText.setText(anniversary.getText());
        anniDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(anniversary.getTime()));
        anniDays.setText(anniversary.getDaysText());
        anniType.setText(anniversary.getTypeText());
    }
}
