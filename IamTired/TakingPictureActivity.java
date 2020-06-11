package ntou.cs.java.projects;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TakingPictureActivity extends AppCompatActivity{
    private String currentPhotoPath;
    private Uri currentPhotoUri=null;
    private static final int TAKE_PICTURE_CODE=45678;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taking_picture_layout);
        takePhotoIntent();
    }
    public void takePhotoIntent(){
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            File photofile = null;
            try{
                photofile=createImageFile();
            }
            catch(IOException e){

            }
            if(photofile!=null){
                currentPhotoUri= FileProvider.getUriForFile(this,"ntou.cs.java.projects.fileprovider",photofile);
            }
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,currentPhotoUri);
        startActivityForResult(takePictureIntent,TAKE_PICTURE_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==TAKE_PICTURE_CODE){
            if(resultCode==RESULT_OK){
                Intent processIntent = new Intent (this,PictureCordActivity.class);
                processIntent.putExtra("imagePath",currentPhotoUri);
                startActivity(processIntent);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath=image.getAbsolutePath();
        //currentPhotoUri=Uri.fromFile(image);
        return image;
    }
}
