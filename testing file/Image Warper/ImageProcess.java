package ntou.cs.java.imagewarper;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.Math;


import static ntou.cs.java.imagewarper.PdfProcess.getDateTime;


public class ImageProcess {
    //How to use:
    //ImageProcess.saveImage(this.getApplicationContext(),bitmap);
    //Default saves to /storage/emulated/0/Pictures/  with DateTime.jpg
    static void saveImage(Context context, Bitmap srcBitmap){
        try{
            Log.d("PATH",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+File.separator+getDateTime()+".jpg");
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+File.separator+getDateTime()+".jpg";
            OutputStream output = new FileOutputStream(path);
            srcBitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
            output.flush();
            output.close();

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,Uri.fromFile(new File(path))));
        }catch(Exception e){
            Log.d("EXC",e.toString());
        }
    }
    public static void saveImage_re(Bitmap srcBitmap, Activity activity){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String imageName="JPEG_"+getDateTime();
        String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), srcBitmap, imageName, null);
        Toast.makeText(activity.getApplicationContext(),"圖片已儲存!",Toast.LENGTH_SHORT).show();
    }

    public static Bitmap myWarpPerspective(Bitmap srcBitmap, Point lu, Point ru, Point rd, Point ld) {
        Mat src = new Mat(srcBitmap.getHeight(),srcBitmap.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(srcBitmap,src);
        double l = distance(lu,ld);
        double u = distance(lu,ru);
        double r = distance(ru,rd);
        double d = distance(ld,rd);


        double adjWid = l>r?(l-r)/l+1:(r-l)/r+1;
        double adjLen = u>d?(u-d)/u+1:(d-u)/d+1;

        if(adjWid>adjLen) {
            adjLen = 1;
        }else {
            adjWid = 1;
        }

        double maxLen = l>r?l*adjLen:r*adjLen;
        double maxWid = u>d?u*adjWid:d*adjWid;

        MatOfPoint2f dstMat = new MatOfPoint2f(new Point(0,0),new Point((int)maxWid,0),new Point((int)maxWid,(int)maxLen),new Point(0,(int)maxLen));
        MatOfPoint2f srcMat = new MatOfPoint2f(lu,ru,rd,ld);

        Mat perspective = Imgproc.getPerspectiveTransform(srcMat,dstMat);
        Mat resultMat = src.clone();
        Imgproc.warpPerspective(src, resultMat, perspective, new Size((int)maxWid,(int)maxLen));
        double maxLenth = Math.max(srcBitmap.getWidth(), srcBitmap.getHeight());
        double maxMat = Math.max(resultMat.width(),resultMat.height());
        Imgproc.resize(resultMat,resultMat,new Size(resultMat.width()*(maxLenth/maxMat),resultMat.height()*(maxLenth/maxMat)));

        Bitmap resultBitmap = Bitmap.createBitmap(resultMat.width(),resultMat.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(resultMat,resultBitmap);
        return resultBitmap;
    }

    public static Bitmap clearify(Bitmap srcBitmap) {
        Mat src = new Mat(srcBitmap.getHeight(),srcBitmap.getWidth(), CvType.CV_8UC3);
        Utils.bitmapToMat(srcBitmap,src);
        Mat srcMat = src.clone();
        Mat dst = src.clone();
        Imgproc.cvtColor(srcMat, srcMat,Imgproc.COLOR_BGR2GRAY, 0);
        Imgproc.cvtColor(dst, dst,Imgproc.COLOR_BGR2GRAY, 0);
        Imgproc.adaptiveThreshold(srcMat, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 23, 6);

        Bitmap resultBitmap = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,resultBitmap);
        return resultBitmap;
    }

    private static double distance(Point a,Point b) {
        return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
    }
}