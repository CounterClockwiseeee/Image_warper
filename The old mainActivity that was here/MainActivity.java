package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.opencv.core.Point;
import org.opencv.osgi.OpenCVNativeLoader;


public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    boolean clicked = false;

    Bitmap bitmap;
    Bitmap bitmap1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        OpenCVNativeLoader loader = new OpenCVNativeLoader();
        loader.init();


        imageView = (ImageView)findViewById(R.id.imgV);
        imageView.invalidate();
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = drawable.getBitmap();
        bitmap1 = ImageProcess.myWarpPerspective(bitmap,new Point(1145,821),new Point(2977,463),new Point(3000,2413),new Point(1216,1910));

        imageView.setImageBitmap(bitmap);
    }

    public void trans(View view) {

        if(!clicked){
            imageView.setImageBitmap(bitmap1);
            clicked = !clicked;
        }else{
            imageView.setImageBitmap(bitmap);
            clicked = !clicked;
        }
    }


}
