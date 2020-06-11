package ntou.cs.java.projects;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import java.io.IOException;
import java.util.ArrayList;
import static ntou.cs.java.projects.PdfProcess.*;


public class PdfConvertActivity extends AppCompatActivity {
    private ArrayList<Uri> imageData;//add this to the top as var

    public ArrayList<Bitmap> bitmaps = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_convert_layout);
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
        }

        if(imageData!=null){
            for(Uri temp:imageData){
                try {
                    bitmaps.add(MediaStore.Images.Media.getBitmap(this.getContentResolver(), temp));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            createPdfOfImagesWithUserInput(this,bitmaps);
        }
    }
}