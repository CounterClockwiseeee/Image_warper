package ntou.cs.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

import org.opencv.core.Point;
import org.opencv.osgi.OpenCVNativeLoader;


public class MainActivity extends Activity {
    ImageView imageView ,ic;
    private ZoomImageView zoomImageView;
    boolean clicked = false;
    private ImageView img;
    Bitmap bitmap;
    Bitmap bitmap1,bitmapic;
    //private float x, y;    // 原本圖片存在的X,Y軸位置
    //private int mx, my; // 圖片被拖曳的X ,Y軸距離長度
    //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(21,19);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVNativeLoader loader = new OpenCVNativeLoader();
        loader.init();

        ic = (ImageView) findViewById(R.id.imageView1); //icon
        ic.invalidate();
        BitmapDrawable drawable1 = (BitmapDrawable) ic.getDrawable();
        bitmapic = drawable1.getBitmap();
        //img.setOnTouchListener(this);//觸控時監聽
        //ImageView ic = (ImageView)findViewById(R.id.imageView1);
        zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);

        imageView = (ImageView)findViewById(R.id.imgV);
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        //imageView.setImageBitmap(bitmap);
        zoomImageView.setImageBitmap(bitmap);
        //bitmap1 = ImageProcess.myWarpPerspective(bitmap,new Point(1145,821),new Point(2977,463),new Point(3000,2413),new Point(1216,1910));


    }

/*
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Log.e("View", v.toString());
        switch (event.getAction()) {          //判斷觸控的動作

            case MotionEvent.ACTION_DOWN:// 按下圖片時
                x = event.getX();                  //觸控的X軸位置
                y = event.getY();                  //觸控的Y軸位置
                params.leftMargin = x;

            case MotionEvent.ACTION_MOVE:// 移動圖片時

                //getX()：是獲取當前控件(View)的座標

                //getRawX()：是獲取相對顯示螢幕左上角的座標
                mx = (int) (event.getRawX() - x);
                my = (int) (event.getRawY() - y);
                v.layout(mx, my, mx + v.getWidth(), my + v.getHeight());
                break;
        }
        //Log.e("address", String.valueOf(mx) + "~~" + String.valueOf(my)); // 記錄目前位置
        return true;
    }
*/
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
