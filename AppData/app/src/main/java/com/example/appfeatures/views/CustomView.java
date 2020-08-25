package com.example.appfeatures.views;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.appfeatures.MainActivity;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CustomView extends View{

    boolean hasPath;
    ArrayList<Float []> points;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    int x = 500;
    int y = 500;

    int [][] path = {{500, 500},{-17, -253},{-101, -1140},{-1, -36},{462, -26},{562, -33},{307, -19},{463, -26},{177, -9},{-21, -213},{-250, 14},{-25, -332}};

    private void init(@Nullable AttributeSet set){

        hasPath = true;
        points = new ArrayList<>();
        String data = "[135.491760, 34.691897],[135.492170, 34.692110],[135.492637, 34.691416],[135.492693, 34.691342],[135.493085, 34.690820],[135.493194, 34.690690],[135.494163, 34.691731],[135.494345, 34.691870],[135.494690, 34.692070],[135.495114, 34.692228],[135.495341, 34.692301],[135.495359, 34.692305],";
        getCoordinates(data);
    }

    public void drawPath(){
        if (points.size() > 0){
            hasPath = true;
            invalidate();
            postInvalidate();
        }
    }

    public void getCoordinates(String data){
        String values [] = data.split("],");
        points = new ArrayList<>();
        float x;
        float y;
        String tempS[];
        for(int i = 0; i < values.length;i++){
            tempS = values[i].split(",");
            x = Float.parseFloat(tempS[0].substring(1));
            y = Float.parseFloat(tempS[1].substring(1));
            Float tempD[] = {x,y};
            points.add(tempD);
        }
        DecimalFormat df = new DecimalFormat("#.######");

        for (int i = points.size()-1;i>0;i--){
            x = Float.parseFloat(df.format((points.get(i)[0]-points.get(i-1)[0])*200000));
            y = Float.parseFloat(df.format((points.get(i)[1]-points.get(i-1)[1])*200000));
            Float tempD[] = {x,y};
            points.set(i,tempD);
        }

        Float[] temp = new Float[]{200.0f, 270.0f};
        points.set(0,temp);

        for (int i = 1;i<points.size();i++){
            x = (float) Math.round(points.get(i)[0]+points.get(i-1)[0]);
            y = (float)Math.round(points.get(i-1)[1]-points.get(i)[1]);
            Float tempD[] = {x,y};
            points.set(i,tempD);
        }

    }



    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        Paint borderPaint = new Paint();
        borderPaint.setColor( Color.WHITE );
        borderPaint.setStrokeWidth( 25 );
        borderPaint.setStyle( Paint.Style.STROKE );
        canvas.drawRect(0, 0, getWidth(), getHeight(), borderPaint);

        if(hasPath){
            Paint pathPaint = new Paint();
            pathPaint.setColor(Color.WHITE);
            pathPaint.setStrokeWidth( 5 );
            pathPaint.setStyle( Paint.Style.STROKE );

            Paint northPaint = new Paint();
            northPaint.setColor(Color.RED);
            northPaint.setStrokeWidth( 5 );
            northPaint.setStyle( Paint.Style.STROKE );

            Paint userPaint = new Paint();
            userPaint.setColor(Color.BLUE);

            canvas.drawLine(100,50, 100,150,northPaint);
            canvas.drawLine(75,90, 125,90,northPaint);
            canvas.drawLine(80,70, 100,50,northPaint);
            canvas.drawLine(120,70, 100,50,northPaint);

            for(int i = 0; i < points.size()-1;i++){
                canvas.drawLine(points.get(i)[0],points.get(i)[1],points.get(i+1)[0],points.get(i+1)[1],pathPaint);
            }
            canvas.drawCircle(points.get(0)[0],points.get(0)[1],20,userPaint);

        }



    }
}
