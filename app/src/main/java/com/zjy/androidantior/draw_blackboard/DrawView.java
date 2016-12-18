package com.zjy.androidantior.draw_blackboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 绘制简易的涂鸦板（画板）
 */
public class DrawView extends View {
    private  int view_width = 0 ; //屏幕的宽度
    private  int view_height= 0 ; //屏幕的高度
    private  float preX;  //起始点的x，y坐标
    private  float preY;
    private Path path; //路径
    public Paint paint; //画笔
    Bitmap cacheBitmap = null;  //定义一个内存中的图片，该图片作为缓冲区
    Canvas cacheCanvas = null; //定义cacheBitmap上的canva对象

    public DrawView(Context context) {
        super(context);

        view_width=context.getResources().getDisplayMetrics().widthPixels; //获取屏幕的宽度
        view_height = context.getResources().getDisplayMetrics().heightPixels; //获取屏幕的高度
        //创建一个与该view相同大小的缓存区
        cacheBitmap = Bitmap.createBitmap(view_width,view_height, Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(); //创建一个新的画布
        path = new Path();
        cacheCanvas.setBitmap(cacheBitmap); //在画布上绘制cachebitmap
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.RED);  //设置默认的画笔
        //设置画笔的风格
        paint.setStyle(Paint.Style.STROKE);  //设置填充方式为描边
        paint.setStrokeJoin(Paint.Join.ROUND);  //设置笔刷的图形样式
        paint.setStrokeCap(Paint.Cap.ROUND); //设置画笔转弯处的连接风格
        paint.setStrokeWidth(1); //设置画笔的笔触宽度
        paint.setAntiAlias(true);
        paint.setDither(true); //使用抖动效果
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画布的背景颜色
        canvas.drawColor(0xFFFFFFFF);
        Paint bmpPaint = new Paint();
        //绘制cacheBitmap
        canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
        //绘制路径
        canvas.drawPath(path, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG); //保存canvas的状态
        canvas.restore(); //回复canvas之前保存的状态，防止保存后对canvas执行的操作对后续的绘制有影响

    }

    /**
     *重写此方法，为该视图添加触摸事件监听器
     * 先获取触摸事件发生的位置，然后应用switch语句对事件的不同状态添加响应代码，最后使用incalidate方法更新视图
     */
    public boolean onTouchEvent(MotionEvent event) {
        //获取触摸事件发生的位置
        float x = event.getX();
        float y  = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y); //将绘图的起始点移到（x.y）坐标点的位置
                preX = x;
                preY = y ;
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x-preX);
                float dy = Math.abs(y-preY);
                //判断是否在允许的范围
                if (dx >= 5 || dy >= 5){
                    path.quadTo(preX,preY,(x+preX)/2,(y+preY)/2);
                    preX = x;
                    preX = y;
                }
                break;
            case MotionEvent.ACTION_UP:
                cacheCanvas.drawPath(path,paint); //绘制路径
                path.reset();
                break;
        }
        //更新视图
        invalidate();
        return true;  //返回true，表明处理方法已经处理该事件
    }

    /**
     * 编写此方法，用于实现擦橡皮功能
     */
    public  void  clear(){
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR)); //设置图像重叠时的处理方法
        paint.setStrokeWidth(50); //设置笔触的宽度
    }

    /**
     * 编写此方法，用于保存当前绘图的方法
     */
    public  void  save(){

    }

    /**
     * 保存绘制好的位图
     * 在该方法中，首先在sd卡上创建一个文件，然后创建一个文件输出流对象，并调用bitmap类的compress方法
     * 将绘图内容压缩为png格式输出到刚创建的文件输出流对象中，最后将缓冲区的数据全部写出到输出流中
     * 并关闭文件输出流对象
     */
    public  void  saveBitmap(String fileName) throws IOException{
        File file = new File("/sdcard/pictures/"+fileName+".png"); //创建文件对象
        file.createNewFile();  //创建一个新文件
        FileOutputStream fileOS = new FileOutputStream(file); //创建一个文件输出流对象
        //将绘图内容压缩为png格式输出到输出流对象中
        cacheBitmap.compress(Bitmap.CompressFormat.PNG,100,fileOS);
        fileOS.flush(); //将缓冲区中的数据全部写出到输出流中
        fileOS.close(); //关闭文件输出流对象
    }
}
