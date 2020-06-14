package ntou.cs.java.imagewarper;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;



public class PdfConvertActivity extends AppCompatActivity {
    private ArrayList<Uri> imageData;//add this to the top as var
    private boolean selectedPicture=false;
    public ArrayList<Bitmap> bitmaps = new ArrayList<>();

    private MyHandler myHandler;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myHandler.removeCallbacksAndMessages(null);
    }
    static class MyHandler extends Handler {
        // WeakReference to the outer class's instance.
        private WeakReference<PdfConvertActivity> mOuter;

        MyHandler(PdfConvertActivity activity) {
            mOuter = new WeakReference<PdfConvertActivity>(activity);
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage( Message msg) {
            final PdfConvertActivity outer = mOuter.get();
            if (outer != null) {
                super.handleMessage(msg);
                AlertDialog.Builder builder = new AlertDialog.Builder(this.mOuter.get());
                builder.setCancelable(false);
                builder.setMessage(msg.what==1?"成功建立PDF!":"發生不明錯誤，請再試一次");
                builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        outer.finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_convert_layout);
        myHandler = new MyHandler(this);
        selectImageAndCreatePdf();
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
            else{
                finish();
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
            PdfProcess2.createPdfOfImagesWithUserInput(this,myHandler,bitmaps);
        }
    }
}