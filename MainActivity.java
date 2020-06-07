package ntou.cs.java.projects;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
public class MainActivity extends AppCompatActivity {
    private ArrayList<Uri> imageData;//add this to the top as var
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btnAction(View view){
        setImageData();
    }
    public void setTextView(){//just an example of manipulating list of Uris
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText("");
        for(Uri data:getImageLocation()){
            textView.append(data.toString()+"\n");
        }
    }
    public void setImageData(){
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent,1234);
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1234){
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
        setTextView();//This is where your code goes if you need to use the data,delete the setTextView and change it to your function
    }
    public ArrayList<Uri> getImageLocation(){
        return imageData;//return a ArrayList of Uri, so please consider there may be multiple data
    }
}