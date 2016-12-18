package com.zjy.androidantior.draw_blackboard;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 为选项菜单解析菜单项目
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.toolsmenu,menu);  //解析菜单文件
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 设置选项菜单项目中的点击事件
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        //获取自定义的视图对象
        DrawView dv = (DrawView) findViewById(R.id.drawview1);

        dv.paint.setXfermode(null); //取消擦除效果
        dv.paint.setStrokeWidth(1); //初始化画笔的宽度

        switch (item.getItemId()){  //选项菜单的点击事件
            case R.id.red:
                dv.paint.setColor(Color.RED); //设置画笔的颜色为红色
                item.setChecked(true);
                break;
            case R.id.green:
                dv.paint.setColor(Color.GREEN); //设置画笔的颜色为绿色
                item.setChecked(true);
                break;
            case R.id.blue:
                dv.paint.setColor(Color.BLUE); //设置画笔的颜色为蓝色
                item.setChecked(true);
                break;
            case R.id.width_1:
                dv.paint.setStrokeWidth(1); //设置笔触的宽度为1像素
                break;
            case R.id.width_2:
                dv.paint.setStrokeWidth(5); //设置笔触的宽度为5像素
                break;
            case R.id.width_3:
                dv.paint.setStrokeWidth(10); //设置笔触的宽度为10像素
                break;
            case  R.id.clear:
                dv.clear(); //擦除绘图，橡皮擦的功能
                break;
            case R.id.save:
                dv.save(); //保存绘图
                break;
        }
        return true;
    }
}
