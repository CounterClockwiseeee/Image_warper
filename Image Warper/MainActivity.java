package ntou.cs.java.imagewarper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import org.opencv.osgi.OpenCVNativeLoader;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyStoragePermissions(MainActivity.this);
        setContentView(R.layout.activity_main);
        OpenCVNativeLoader loader=new OpenCVNativeLoader();
        loader.init();
    }
    public void cButtonAction(View view){
        Intent pictureIntent=new Intent (this,TakingPictureActivity.class);
        startActivity(pictureIntent);
    }
    public void pButtonAction(View view){
        Intent pdfIntent=new Intent(this,PdfConvertActivity.class);
        startActivity(pdfIntent);
    }
    public void iButtonAction(View view){
        Intent imageIntent=new Intent(this,GetImageActivity.class);
        startActivity(imageIntent);
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}