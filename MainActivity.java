package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.TextView;

import com.example.myapplication.ImageProcess;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList<Uri> imageData;//add this to the top as var

    public ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * CODE HERE
        * CODE HERE
        * CODE HERE
        * CODE HERE
        * */

        //call below to activate image selection and create pdf
        //selectImageAndCreatePdf();
        Button bt2 = (Button)findViewById(R.id.button2);
        bt2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                selectImageAndCreatePdf();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void selectImageAndCreatePdf(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,1);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                imageData=new ArrayList<>();
                if(data.getClipData() != null){
                    int dataCount = data.getClipData().getItemCount();
                    for(int i=0;i<dataCount;i++){
                        imageData.add(data.getClipData().getItemAt(i).getUri());
                    }
                }
                else if(data.getData()!=null){
                    imageData.add(data.getData());
                }
            }
        }

        if(imageData!=null){
            for(Uri temp:imageData){
                try {
                    bitmaps.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), temp));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            PdfProcess.createPdfOfImagesWithUserInput(this,bitmaps);
        }

    }
    public ArrayList<Uri> getImageLocation(){
        return imageData;//return a ArrayList of Uri, so please consider there may be multiple data
    }
}
