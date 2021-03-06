package ntou.cs.demo;


import android.graphics.Bitmap;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.osgi.OpenCVNativeLoader;
import org.opencv.android.Utils;
import java.lang.Math;



public class ImageProcess {

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
        Imgproc.adaptiveThreshold(srcMat, dst, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 11, 8);

        Bitmap resultBitmap = Bitmap.createBitmap(dst.width(),dst.height(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(dst,resultBitmap);
        return resultBitmap;
    }

    private static double distance(Point a,Point b) {
        return Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
    }
}
