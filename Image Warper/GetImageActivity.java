package com.example.myapplication;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


@SuppressLint("Registered")
public class GetImageActivity extends AppCompatActivity {
    private boolean selectedPicture=false;
    private final static int REQUEST_FILE_CODE=1234;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_image_layout);
        getImageData();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void getImageData(){
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imageIntent.setType("image/*");
        imageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(imageIntent,REQUEST_FILE_CODE);
    }
    @Override
    public void onResume(){
        super.onResume();
        if(selectedPicture){
            finish();
        }
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==REQUEST_FILE_CODE){
            if(resultCode==RESULT_OK&&data.getData()!=null){
                Intent cordIntent=new Intent(this,GetCoordActivity.class);
                cordIntent.putExtra("imagePath",data.getData());
                selectedPicture=true;
                startActivity(cordIntent);
            }
            else{
                finish();
            }
        }
    }
}