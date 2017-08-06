package com.jiuzhang.guojing.awesomeresume;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public abstract class EditBaseActivity<T> extends AppCompatActivity {

    private T data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the content of the activity to the layout we just created
        setContentView(getLayoutId());

        //noinspection ConstantConditions
        // 以下当结论记：
        // we add “back button” to the Activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = initializeData();
        if (data != null) {
            setupUIForEdit(data);
        } else {
            setupUIForCreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // this function will get called when you click back button
    // onOptionsItemSelected: callback函数：当menueItem被点击时被调用（可能是返回号，也可能是✔,具体要看function是怎样实现的）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 孩子干预父亲的方法：
        // android.R.id.home：系统级别的id，是home的id
        if (item.getItemId() == android.R.id.home) {
            finish();       // finish the current Activity
            return true;
        } else if (item.getItemId() == R.id.ic_save) {
            saveAndExit(data);
            return true;    // return true: 这个按钮由开发者处理，其他人不用管
        }
        // 当孩子从来没有干预过方法，就按照父亲的方法
        return super.onOptionsItemSelected(item);
    }

    protected abstract int getLayoutId();

    protected abstract void setupUIForCreate();

    protected abstract void setupUIForEdit(@NonNull T data);

    protected abstract void saveAndExit(@Nullable T data);

    protected abstract T initializeData();

}

