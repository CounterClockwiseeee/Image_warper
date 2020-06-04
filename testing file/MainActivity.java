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
import android.widget.Button;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import org.opencv.core.Point;
import org.opencv.osgi.OpenCVNativeLoader;
import org.opencv.ximgproc.ContourFitting;

import android.util.Log;
//import android.graphics.Point;

public class MainActivity extends Activity {
    ImageView imageView;
    //private ZoomImageView zoomImageView;
    boolean clicked = false;
    private ImageView ic[] = new ImageView[4];
    private int count = 0;
    Bitmap bitmap;
    Bitmap bitmap1, bitmapic;
    private float x, y, rateWidth, rateHeight;    // 原本圖片存在的X,Y軸位置
    private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
    private int screenWidth, screenHeight;
    private boolean flag = false;

    //private float x, y;    // 原本圖片存在的X,Y軸位置
    //private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
    //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(21,19);
    private class ViewPoint {
        int x;
        int y;
    }

    /*int[] topLeftXY = new int[2];
    int[] topRightXY = new int[2];
    int[] bottomLeftXY = new int[2];
    int[] bottomRightXY = new int[2];*/
    ViewPoint topLeft = new ViewPoint();
    ViewPoint topRight = new ViewPoint();
    ViewPoint bottomLeft = new ViewPoint();
    ViewPoint bottomRight = new ViewPoint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        OpenCVNativeLoader loader = new OpenCVNativeLoader();
        loader.init();
        ic[0] = (ImageView) findViewById(R.id.icon1);//左上
        ic[1] = (ImageView) findViewById(R.id.icon2);//右上
        ic[2] = (ImageView) findViewById(R.id.icon3);//右下
        ic[3] = (ImageView) findViewById(R.id.icon4);//左下
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

        Display display = getWindowManager().getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        Log.e("screenWidth", "" + screenWidth);
        Log.e("screenHeight", "" + screenHeight);
        if (screenWidth > screenHeight) flag = false; //橫的
        else if (screenWidth < screenHeight) flag = true;//直的
        Log.e("flag", ""+flag);
        init();

        TouchListen imgListener = new TouchListen();
        for (int i = 0; i < 4; i++) {
            ic[i].setOnTouchListener(imgListener);
        }
        final Button button = findViewById(R.id.btn_test);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    rateWidth = (float) 1920 / imageView.getWidth();
                    rateHeight = (float) 1440 / imageView.getHeight();
                } catch (ArithmeticException e) {
                    Log.e("source", "width: " + imageView.getWidth() + " height: " + imageView.getHeight());
                    System.exit(1);
                }
                if (flag) {
                    topLeft.y -= screenHeight / 2 - imageView.getHeight() / 2 - 50;
                    topRight.y-= screenHeight / 2 - imageView.getHeight() / 2 - 50;
                    bottomLeft.y -= screenHeight / 2 - imageView.getHeight() / 2 - 50; //直的
                    bottomRight.y -= screenHeight / 2 - imageView.getHeight() / 2 - 50;
                }
                else {
                    topLeft.x -= screenWidth / 2 - imageView.getWidth() / 2 - 20;//橫的
                    topRight.x -= screenWidth / 2 - imageView.getWidth() / 2 - 20;//橫的
                    bottomLeft.x -= screenWidth / 2 - imageView.getWidth() / 2 - 20;//橫的
                    bottomRight.x -= screenWidth / 2 - imageView.getWidth() / 2 - 20;//橫的
                }
                Log.e("flag", ""+flag);
                Log.e("rate", "width: " + rateWidth + " height: " + rateHeight);
                Log.e("imageView", "width: " + imageView.getWidth() + " height: " + imageView.getHeight());
                Log.e("screenWidth", "" + screenWidth);
                Log.e("screenHeight", "" + screenHeight);
                Log.e("topLeft", "width: " + topLeft.x + " height: " + topLeft.y);
                Log.e("topRight", "width: " + topRight.x + " height: " + topRight.y);
                Log.e("bottomLeft", "width: " + bottomLeft.x + " height: " + bottomLeft.y);
                Log.e("bottomRight", "width: " + bottomRight.x + " height: " + bottomRight.y);
                bitmap1 = ImageProcess.myWarpPerspective(bitmap, new Point(topLeft.x * rateWidth, topLeft.y * rateHeight), new Point(topRight.x * rateWidth, topRight.y * rateHeight), new Point(bottomRight.x * rateWidth, bottomRight.y * rateHeight), new Point(bottomLeft.x * rateWidth, bottomLeft.y * rateHeight));

                imageView.setImageBitmap(bitmap1);
                for (int i = 0; i < 4; i++) {
                    ic[i].setVisibility(View.INVISIBLE);
                }
            }
        });
        final Button button_Ret = findViewById(R.id.btn_return);
        button_Ret.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imageView.setImageBitmap(bitmap);
                init();
            }
        });
    }
    private void init() {
        for (int i = 0; i < 4; i++) {
            ic[i].setVisibility(View.VISIBLE);
        }
        topLeft.x = screenWidth / 2 - 50;//row
        topLeft.y = screenHeight / 2 - 50;//column
        topRight.x = screenWidth / 2 + 50;
        topRight.y = screenHeight / 2 - 50;
        bottomLeft.x = screenWidth / 2 - 50;
        bottomLeft.y = screenHeight / 2 + 50;
        bottomRight.x = screenWidth / 2 + 50;
        bottomRight.y = screenHeight / 2 + 50;
        Log.e("topLeft", "width: " + topLeft.x + " height: " + topLeft.y);
        Log.e("topRight", "width: " + topRight.x + " height: " + topRight.y);
        Log.e("bottomLeft", "width: " + bottomLeft.x + " height: " + bottomLeft.y);
        Log.e("bottomRight", "width: " + bottomRight.x + " height: " + bottomRight.y);
    }
    private class TouchListen implements OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
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
                    if (mx < screenWidth / 2 + imageView.getWidth() / 2 - 20 && mx > screenWidth / 2 - imageView.getWidth() / 2 - 20 && my < screenHeight / 2 + imageView.getHeight() / 2 - 50 && my > screenHeight / 2 - imageView.getHeight() / 2 - 50) {
                        if (v == ic[0]) {
                            if (mx < topRight.x && my < bottomLeft.y) {
                                v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                                topLeft.x = mx + v.getWidth() / 2;
                                topLeft.y = my + v.getHeight() / 2;
                            }
                        } else if (v == ic[1]) {
                            if (mx > topLeft.x && my < bottomRight.y) {
                                v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                                topRight.x = mx + v.getWidth() / 2;
                                topRight.y = my + v.getHeight() / 2;
                            }
                        } else if (v == ic[2]) {
                            if (mx >  bottomLeft.x && my > topRight.y) {
                                v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                                bottomRight.x = mx + v.getWidth() / 2;
                                bottomRight.y = my + v.getHeight() / 2;
                            }
                        } else if (v == ic[3]) {
                            if (mx < bottomRight.x && my > topLeft.y) {
                                v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                                bottomLeft.x = mx + v.getWidth() / 2;
                                bottomLeft.y = my + v.getHeight() / 2;
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    Log.e("imageView", "width: " + imageView.getWidth() + " height: " + imageView.getHeight());
                    break;
            }
            return true;

        }
    }
    public void trans(View view) {

        if (!clicked) {
            imageView.setImageBitmap(bitmap1);
            clicked = !clicked;
        } else {
            imageView.setImageBitmap(bitmap);
            clicked = !clicked;
        }
    }


}
