package com.dcch.sharebike.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Administrator on 2017/3/21 0021.
 * 自定义TextView
 */

@SuppressLint("AppCompatCustomView")
public class MyBorderEditText extends EditText {
    Paint paint = new Paint();
    public MyBorderEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setColor(android.graphics.Color.YELLOW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);

        //1、横坐标0到this.getWidth()-1，纵坐标0到0

        canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);

        //2、横坐标0到0，纵坐标0到this.getHeight()-1

        canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);

        //3、横坐标this.getWidth()-1到this.getWidth()-1，纵坐标0到this.getHeight()-1

        canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);

        //4、横坐标0到this.getWidth()-1，纵坐标this.getHeight()-1到this.getHeight()-1


    }

}
