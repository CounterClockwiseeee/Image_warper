package ntou.cs.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

//import org.opencv.core.Point;
import org.opencv.osgi.OpenCVNativeLoader;
import org.opencv.ximgproc.ContourFitting;

import android.util.Log;
import android.graphics.Point;

public class MainActivity extends Activity {
    ImageView imageView;
    //private ZoomImageView zoomImageView;
    boolean clicked = false;
    private ImageView ic[] = new ImageView[4];
    private int count = 0;
    Bitmap bitmap;
    Bitmap bitmap1, bitmapic;
    private float x, y;    // 原本圖片存在的X,Y軸位置
    private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
    private int screenWidth, screenHeight;

    //private float x, y;    // 原本圖片存在的X,Y軸位置
    //private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
    //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(21,19);
    private class ViewPoint {
        int x;
        int y;
    }

    int[] posXY = new int[2];
    ViewPoint topleft = new ViewPoint();
    ViewPoint topright = new ViewPoint();
    ViewPoint bottomleft = new ViewPoint();
    ViewPoint bottomright = new ViewPoint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OpenCVNativeLoader loader = new OpenCVNativeLoader();
        loader.init();
        ic[0] = (ImageView) findViewById(R.id.icon1);
        ic[1] = (ImageView) findViewById(R.id.icon2);
        ic[2] = (ImageView) findViewById(R.id.icon3);
        ic[3] = (ImageView) findViewById(R.id.icon4);
        //ic.invalidate();
        //BitmapDrawable drawable1 = (BitmapDrawable) ic.getDrawable();
        //bitmapic = drawable1.getBitmap();
        //img.setOnTouchListener(this);//觸控時監聽
        //ImageView ic = (ImageView)findViewById(R.id.imageView1);
        //zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
        imageView = (ImageView) findViewById(R.id.imgV);
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        //imageView.setImageBitmap(bitmap);
        TouchListen imgListener = new TouchListen();
        for (int i = 0; i < 4; i++) {
            ic[i].setOnTouchListener(imgListener);
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        Log.e("screenWidth", "" + screenWidth);
        Log.e("screenHeight", "" + screenHeight);
    }

    private class TouchListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.d("View", v.toString());
            switch (event.getAction()) {          //判斷觸控的動作

                case MotionEvent.ACTION_DOWN:// 按下圖片時
                    x = event.getX();                  //觸控的X軸位置
                    y = event.getY();                  //觸控的Y軸位置
                    break;
                case MotionEvent.ACTION_MOVE:// 移動圖片時

                    //getX()：是獲取當前控件(View)的座標
                    Log.d("axis", x + " " + y);
                    //getRawX()：是獲取相對顯示螢幕左上角的座標
                    mx = (int) (event.getRawX() - x);
                    my = (int) (event.getRawY() - y) - 60;
                    Log.d("getRaw", mx + " " + my);
                    if (mx < screenWidth / 2 + imageView.getWidth() / 2 && mx > screenWidth / 2 - imageView.getWidth() / 2 && my < screenHeight / 2 + imageView.getHeight() / 2 && my > screenHeight / 2 - imageView.getHeight() / 2) {
                        v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("source", "width: " + imageView.getWidth() + " height: " + imageView.getHeight());
                    imageView.getLocationOnScreen(posXY);
                    Log.e("getLocationOnScreen", "width: " + posXY[0] + " height: " + posXY[1]);
                    if (v == ic[0]) {

                        topleft.x = mx;
                        topleft.y = my;
                        Log.e("ic[0]", "width: " + topleft.x + " height: " + topleft.y);
                    }
                    break;
            }
            //Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
            return true;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void Comput(ImageView imageview) {


    }


    //zoomImageView.setImageBitmap(bitmapic);
    //bitmap1 = ImageProcess.myWarpPerspective(bitmap,new Point(1145,821),new Point(2977,463),new Point(3000,2413),new Point(1216,1910));






/*
    public void trans(View view) {

        if (!clicked) {
            imageView.setImageBitmap(bitmap1);
            clicked = !clicked;
        } else {
            imageView.setImageBitmap(bitmap);
            clicked = !clicked;
        }
    }
*/

}
