package com.example.myapplication;

import android.graphics.Bitmap;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
/*TESTING DON'T USE*/


public class RectDetection {
    public Mat img;

    public RectDetection(Bitmap src) {
        int height = src.getHeight();
        int width = src.getWidth();
        Mat temp = new Mat(height,width, CvType.CV_8UC3);
        Utils.bitmapToMat(src,temp);
        this.img = temp.clone();
    }

    public Point[] detectRectangle(){
        Imgproc.resize(this.img,this.img,new Size((int)(this.img.width()/3),(int)(this.img.height()/3)));
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Mat grayImg = new Mat();
        Mat binImg = new Mat();
        Mat cannyImg = new Mat();
        Mat result = new Mat(this.img.size(),CvType.CV_8UC1);

        Imgproc.cvtColor(this.img,grayImg, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(grayImg,grayImg,7);

        //Imgproc.adaptiveThreshold(grayImg,binImg,255,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,21,8);
        Imgproc.threshold(grayImg,binImg,100,255,Imgproc.THRESH_BINARY);
        Imgproc.Canny(binImg,cannyImg,100,200,3,true);

        Imgproc.findContours(cannyImg,contours,hierarchy,Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);

        //ArrayList<MatOfPoint2f> poly = new ArrayList<>(contours.size());
        MatOfPoint2f poly [] =new MatOfPoint2f[contours.size()];
        for(int i=0;i<contours.size();i++){
            poly[i] = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()),poly[i],10,true);
        }
        int max = 0;


        ArrayList<Rect> rect = new ArrayList<>();
        for(int i=0;i<contours.size();i++){
            rect.add(Imgproc.boundingRect(new MatOfPoint(contours.get(i).toArray())));
        }
        double min=this.img.width()*this.img.height()*100;
        int minIndex=0;
        for(int i=0;i<contours.size();i++){
            if(poly[i].size().width*poly[i].size().height>8){
                if(distanceFromCenter(center(rect.get(i).tl(),rect.get(i).br()))<min){
                    min=distanceFromCenter(center(rect.get(i).tl(),rect.get(i).br()));
                    minIndex = i;
                    //Imgproc.drawContours(result,contours,i,new Scalar(255,255,0),10);
                    //Log.d("DIS",String.format("%d %f ",i,distanceFromCenter(center(rect.get(i).tl(),rect.get(i).br()))));
                    //Imgproc.rectangle(result,rect.get(i),new Scalar(255,255,0));
                }
                Log.d("DIS",String.format("%d %f",i,distanceFromCenter(center(rect.get(i).tl(),rect.get(i).br()))));
                //Imgproc.drawContours(result,contours,i,new Scalar(255,255,0),10);
            }
        }
        //Log.d("DIS",String.format("%d %f %f",minIndex,poly[minIndex].size().width,poly[minIndex].size().height));
        Imgproc.drawContours(result,contours,minIndex,new Scalar(255,255,0),1);

        //Imgproc.drawContours(result,contours,383,new Scalar(255,255,0),10);

        MatOfInt corner = new MatOfInt();
        Imgproc.convexHull(contours.get(minIndex),corner,true);


        MatOfPoint points = convertIndexesToPoints(contours.get(minIndex),corner);
        //Imgproc.drawContours(result,contours,-1,new Scalar(255,255,0));
        Point[] points1 = contours.get(minIndex).toArray();
        Point center = new Point((double) this.img.width()/2,(double) this.img.height()/2);
        Point lu = center.clone();
        Point ru = center.clone();
        Point rd = center.clone();
        Point ld = center.clone();
        Log.d("center",String.format("5 %f %f",center.x*3,center.y*3));
        for(int i=0;i<points1.length;i++){
            Point temp = points1[i];
            Log.d("Point",String.format("5 %f %f",temp.x*3,temp.y*3));
            if(temp.x<lu.x && temp.y<lu.y){//左上
                lu = temp.clone();
            }else if(temp.x>ru.x && temp.y<ru.y){//右上
                ru = temp.clone();
            }else if(temp.x>rd.x && temp.y>rd.y){//右下
                rd = temp.clone();
            }else if(temp.x<ld.x && temp.y>ld.y){//左下
                ld = temp.clone();
            }
        }

        //Log.d("Rect",String.format("%d %d ",(int)biggest.toArray()[2].x,(int)biggest.toArray()[2].y));
        //Log.d("Rect",String.format("%f %f",lu.x*3,lu.y*3));
        Point[] resultPoint = {lu,ru,rd,ld};
        for (Point temp: resultPoint) {
            temp.x = temp.x*3;
            temp.y = temp.y*3;
        }

        Bitmap resultBit = Bitmap.createBitmap(result.width(),result.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(result,resultBit);

        return resultPoint;
    }

    private Point center(Point p1,Point p2){
        return new Point((int)((p1.x+p2.x)/2),(int)((p1.y+p2.y)/2));
    }

    private double distanceFromCenter(Point p){
        double centerX = (double)this.img.width()/2;
        double centerY = (double)this.img.height()/2;
        return Math.sqrt((p.x-centerX)*(p.x-centerX) + (p.y-centerY)*(p.y-centerY));
    }

    public static MatOfPoint convertIndexesToPoints(MatOfPoint contour, MatOfInt indexes) {
        int[] arrIndex = indexes.toArray();
        Point[] arrContour = contour.toArray();
        Point[] arrPoints = new Point[arrIndex.length];

        for (int i=0;i<arrIndex.length;i++) {
            arrPoints[i] = arrContour[arrIndex[i]];
        }
        MatOfPoint hull = new MatOfPoint();
        hull.fromArray(arrPoints);
        return hull;
    }


}
