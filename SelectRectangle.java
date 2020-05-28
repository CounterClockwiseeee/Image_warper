package com.example.myapplication;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;



public class SelectRectangle {
    public Point lu;
    public Point ru;
    public Point rd;
    public Point ld;
    public Mat img;
    public Scalar green = new Scalar(0,0,0);


    public SelectRectangle(Bitmap src){
        int height = src.getHeight();
        int width = src.getWidth();
        Mat temp = new Mat(height,width, CvType.CV_8UC3);
        Utils.bitmapToMat(src,temp);
        this.img = temp.clone();
        lu = new Point((int)(width/4),(int)(height/4));
        ru = new Point((int)(width*3/4),(int)(height/4));
        rd = new Point((int)(width*3/4),(int)(height*3/4));
        ld = new Point((int)(width/4),(int)(height*3/4));
    }

    public Bitmap drawLines(){
        Mat temp = this.img.clone();
        Imgproc.line(temp,lu,ru,new Scalar(0,255,0),15);
        Imgproc.line(temp,ru,rd,new Scalar(0,255,0),15);
        Imgproc.line(temp,rd,ld,new Scalar(0,255,0),15);
        Imgproc.line(temp,ld,lu,new Scalar(0,255,0),15);
        Bitmap tempBitmap = Bitmap.createBitmap(this.img.width(),this.img.height(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(temp,tempBitmap);
        return tempBitmap;
    }

    public void setLeftUp(double x,double y){
        this.lu.x = (int)x;
        this.lu.y = (int)y;
    }

    public void setLeftDown(double x,double y){
        this.ld.x = (int)x;
        this.ld.y = (int)y;
    }

    public void setRightUp(double x,double y){
        this.ru.x = (int)x;
        this.ru.y = (int)y;
    }

    public void setRightDown(double x,double y){
        this.rd.x = (int)x;
        this.rd.y = (int)y;
    }
}
