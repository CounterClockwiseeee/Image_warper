package com.example.myapplication;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.opencv.core.Point;
import org.opencv.osgi.OpenCVNativeLoader;




public class GetCoordActivity extends AppCompatActivity {
    public class ViewPoint{

        private int rawX;//what the user sees (Basically the green dots)
        private int rawY;
        private int pureX;//Actual coordinates of the point in the picture (before changing ratio)
        private int pureY;
        private static final int UNIVERSAL_SPACE=60;
        private ImageView greenDotImage;
        public ViewPoint(int rawX,int rawY,int pureX,int pureY,ImageView i){
            this.rawX=rawX;
            this.rawY=rawY;
            this.pureX=pureX;
            this.pureY=pureY;
            this.greenDotImage=i;
        }
        public int getRawX(){
            return rawX;
        }
        public int getRawY(){
            return rawY;
        }
        public int getPureX(){
            return pureX;
        }
        public int getPureY(){
            return pureY;
        }
        public void setRawX(int rawX){
            if(targ==0){
                if(rawX+UNIVERSAL_SPACE>=greenIcon[1].getRawX()) return;
                else{
                    if(rawX<=viewRawLeft) this.rawX=viewRawLeft;
                    else if(rawX>=viewRawLeft+imageView.getWidth()) this.rawX=viewRawLeft+imageView.getWidth();
                    else this.rawX=rawX;
                }
            }
            else if(targ==3){
                if(rawX+UNIVERSAL_SPACE>=greenIcon[2].getRawX()) return;
                else{
                    if(rawX<=viewRawLeft) this.rawX=viewRawLeft;
                    else if(rawX>=viewRawLeft+imageView.getWidth()) this.rawX=viewRawLeft+imageView.getWidth();
                    else this.rawX=rawX;
                }
            }
            else if(targ==1){
                if(rawX<=UNIVERSAL_SPACE+greenIcon[0].getRawX()) return;
                else{
                    if(rawX<=viewRawLeft) this.rawX=viewRawLeft;
                    else if(rawX>=viewRawLeft+imageView.getWidth()) this.rawX=viewRawLeft+imageView.getWidth();
                    else this.rawX=rawX;
                }
            }
            else{
                if(rawX<=UNIVERSAL_SPACE+greenIcon[3].getRawX()) return;
                else{
                    if(rawX<=viewRawLeft) this.rawX=viewRawLeft;
                    else if(rawX>=viewRawLeft+imageView.getWidth()) this.rawX=viewRawLeft+imageView.getWidth();
                    else this.rawX=rawX;
                }
            }
        }
        public void setRawY(int rawY){
            if(targ==0){
                if(rawY+UNIVERSAL_SPACE>=greenIcon[3].getRawY()) return;
                else{
                    if(rawY<=viewRawTop) this.rawY=viewRawTop;
                    else if(rawY>=viewRawTop+imageView.getHeight()) this.rawY=viewRawTop+imageView.getHeight();
                    else this.rawY=rawY;
                }
            }
            else if(targ==1){
                if(rawY+UNIVERSAL_SPACE>=greenIcon[2].getRawY()) return;
                else{
                    if(rawY<=viewRawTop) this.rawY=viewRawTop;
                    else if(rawY>=viewRawTop+imageView.getHeight()) this.rawY=viewRawTop+imageView.getHeight();
                    else this.rawY=rawY;
                }
            }
            else if(targ==2){
                if(rawY<=UNIVERSAL_SPACE+greenIcon[1].getRawY()) return;
                else{
                    if(rawY<=viewRawTop) this.rawY=viewRawTop;
                    else if(rawY>=viewRawTop+imageView.getHeight()) this.rawY=viewRawTop+imageView.getHeight();
                    else this.rawY=rawY;
                }
            }
            else{
                if(rawY<=UNIVERSAL_SPACE+greenIcon[0].getRawY()) return;
                else{
                    if(rawY<=viewRawTop) this.rawY=viewRawTop;
                    else if(rawY>=viewRawTop+imageView.getHeight()) this.rawY=viewRawTop+imageView.getHeight();
                    else this.rawY=rawY;
                }
            }
        }
        public void setPureX(int pureX){
            if(targ==0){
                if(pureX+UNIVERSAL_SPACE>=greenIcon[1].getPureX()) return;
                else{
                    if(pureX<=0) this.pureX=0;
                    else if(pureX>=imageView.getWidth()) this.pureX=imageView.getWidth();
                    else this.pureX=pureX;
                }
            }
            else if(targ==3){
                if(pureX+UNIVERSAL_SPACE>=greenIcon[2].getPureX()) return;
                else{
                    if(pureX<=0) this.pureX=0;
                    else if(pureX>=imageView.getWidth()) this.pureX=imageView.getWidth();
                    else this.pureX=pureX;
                }
            }
            else if(targ==1){
                if(pureX<=UNIVERSAL_SPACE+greenIcon[0].getPureX()) return;
                else{
                    if(pureX<=0) this.pureX=0;
                    else if(pureX>=imageView.getWidth()) this.pureX=imageView.getWidth();
                    else this.pureX=pureX;
                }
            }
            else{
                if(pureX<=UNIVERSAL_SPACE+greenIcon[3].getPureX()) return;
                else{
                    if(pureX<=0) this.pureX=0;
                    else if(pureX>=imageView.getWidth()) this.pureX=imageView.getWidth();
                    else this.pureX=pureX;
                }
            }

        }
        public void setPureY(int pureY){
            if(targ==0){
                if(pureY+UNIVERSAL_SPACE>=greenIcon[3].getPureY()) return;
                else{
                    if(pureY<=0) this.pureY=0;
                    else if(pureY>=imageView.getHeight()) this.pureY=imageView.getHeight();
                    else this.pureY=pureY;
                }
            }
            else if(targ==1){
                if(pureY+UNIVERSAL_SPACE>=greenIcon[2].getPureY()) return;
                else{
                    if(pureY<=0) this.pureY=0;
                    else if(pureY>=imageView.getHeight()) this.pureY=imageView.getHeight();
                    else this.pureY=pureY;
                }
            }
            else if(targ==2){
                if(pureY<=UNIVERSAL_SPACE+greenIcon[1].getPureY()) return;
                else{
                    if(pureY<=0) this.pureY=0;
                    else if(pureY>=imageView.getHeight()) this.pureY=imageView.getHeight();
                    else this.pureY=pureY;
                }
            }
            else{
                if(pureY<=UNIVERSAL_SPACE+greenIcon[0].getPureY()) return;
                else{
                    if(pureY<=0) this.pureY=0;
                    else if(pureY>=imageView.getHeight()) this.pureY=imageView.getHeight();
                    else this.pureY=pureY;
                }
            }
        }
        public ImageView getGreenDotImage(){
            return greenDotImage;
        }
        public boolean isTouching(int x,int y){
            if(x<=rawX+(greenDotImage.getWidth()/2)+25&&x>=rawX-greenDotImage.getWidth()/2-25){
                if(y<=rawY+greenDotImage.getHeight()/2+25&&y>=rawY-greenDotImage.getHeight()/2-25){
                    return true;
                }
            }
//            Log.e("test","no");
            return false;
        }
        public void moveTo(int RawX,int RawY){
            this.setRawX(RawX);
            this.setRawY(RawY);
            this.getGreenDotImage().layout(this.getLayoutCoords(0),this.getLayoutCoords(1),this.getLayoutCoords(2),this.getLayoutCoords(3));
        }
        public int getParentHeight(){
            return ((View)greenDotImage.getParent()).getHeight();
        }
        public int getParentWidth(){
            return ((View)greenDotImage.getParent()).getWidth();
        }
        public int getCorrectCoords(char axis){
            if(axis=='x')return rawX-((int)(greenDotImage.getWidth()/2));
            else return rawY-((int)(greenDotImage.getHeight()/2));
        }
        public int getLayoutCoords(int dir){
            if(dir==0)return this.getCorrectCoords('x')-(viewRawLeft-imageView.getLeft());
            else if(dir==1)return this.getCorrectCoords('y')-(viewRawTop-imageView.getTop());
            else if(dir==2)return this.getCorrectCoords('x')-(viewRawLeft-imageView.getLeft())+greenDotImage.getWidth();
            else if (dir==3)return this.getCorrectCoords('y')-(viewRawTop-imageView.getTop())+greenDotImage.getHeight();
            return 0;
        }
    }
    private int coord[]=new int[2],viewRawLeft,viewRawTop,targ=-1;
    private ImageView imageView;
    private ViewPoint greenIcon[] = new ViewPoint[4];
    private float sizeRatioX,sizeRatioY;
    private Bitmap bitmapImage,newBitmap;
    private boolean notSetup=true,pictureWarped=false;
    private Uri imagePath;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        OpenCVNativeLoader loader =new OpenCVNativeLoader();
        loader.init();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_coord_activity_layout);
        imageView = (ImageView) findViewById(R.id.imgV);
        imagePath = getIntent().getParcelableExtra("imagePath");
        try
        {
            bitmapImage = MediaStore.Images.Media.getBitmap(this.getContentResolver() , imagePath);
            imageView.setImageBitmap(bitmapImage);
        }
        catch (Exception e)
        {
            Log.e("flag","e happend");
        }
        imageView.setOnTouchListener(new View.OnTouchListener(){
                                         @Override
                                         public boolean onTouch(View v, MotionEvent event){
                                             if(notSetup){
                                                 notSetup=false;
                                                 setup();
                                             }
                                             targ=-1;
                                             //Log.e("Test","X:" +(int)event.getRawX()+" Y:"+(int)event.getRawY());
                                             for(int i=0;i<4;i++){
                                                 if(greenIcon[i].isTouching((int)event.getRawX(),(int)event.getRawY())){
                                                     targ=i;
                                                     break;
                                                 }
                                             }
                                             if(targ==-1)return false;
                                             if(event.getAction()==MotionEvent.ACTION_DOWN||event.getAction()==MotionEvent.ACTION_MOVE) {
                                                 greenIcon[targ].setPureX((int)event.getX());
                                                 greenIcon[targ].setPureY((int)event.getY());
                                                 greenIcon[targ].moveTo((int)event.getRawX(),(int)event.getRawY());
                                             }
                                             return true;
                                         }
                                     }
        );
    }
    protected void setup(){
        Log.e("test","setup ran.");
        findViewById(R.id.btn_test).setEnabled(true);
        findViewById(R.id.textView2).setVisibility(View.INVISIBLE);
        imageView.getLocationOnScreen(coord);
        int adjX = imageView.getWidth()/3;
        int adjY = imageView.getHeight()/3;

        int viewWidth=imageView.getWidth(),
                viewHeight=imageView.getHeight();
        viewRawLeft= coord[0];
        viewRawTop = coord[1];
        greenIcon[0] =new ViewPoint(viewRawLeft+adjX,viewRawTop+adjY,0+adjX,0+adjY,(ImageView) findViewById(R.id.icon1));//左上
        greenIcon[1] =new ViewPoint(viewRawLeft+viewWidth-adjX,viewRawTop+adjY,viewWidth-adjX,0+adjY,(ImageView) findViewById(R.id.icon2));//右上
        greenIcon[2] =new ViewPoint( viewRawLeft+viewWidth-adjX,viewRawTop+viewHeight-adjY,viewWidth-adjX,viewHeight-adjY,(ImageView) findViewById(R.id.icon3));//右下
        greenIcon[3] =new ViewPoint( viewRawLeft+adjX,viewRawTop+viewHeight-adjY,0+adjX,viewHeight-adjY,(ImageView) findViewById(R.id.icon4));//左下
        for(ViewPoint icon:greenIcon){
            icon.greenDotImage.setVisibility(View.VISIBLE);
            icon.getGreenDotImage().layout(icon.getLayoutCoords(0),icon.getLayoutCoords(1),icon.getLayoutCoords(2),icon.getLayoutCoords(3));
        }
        sizeRatioX=(float) bitmapImage.getWidth() / imageView.getWidth();
        sizeRatioY=(float) bitmapImage.getHeight() / imageView.getHeight();
    }

    public void clear(View view){
        ImageView imageView = findViewById(R.id.imgV);
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        imageView.setImageBitmap(ImageProcess.clearify(drawable.getBitmap()));
        newBitmap = ImageProcess.clearify(drawable.getBitmap());
    }

    public void rotate(View view){
        ImageView imageView = findViewById(R.id.imgV);
        BitmapDrawable drawable = (BitmapDrawable)imageView.getDrawable();
        imageView.setImageBitmap(ImageProcess.rotate90(drawable.getBitmap()));
        newBitmap = ImageProcess.rotate90(drawable.getBitmap());
    }
    public void tryButtonAction(View view){
        if(pictureWarped){
            ImageProcess.saveImage_re(newBitmap,this);
            finish();
        }
        for(ViewPoint icon:greenIcon){
            icon.greenDotImage.setVisibility(View.INVISIBLE);
        }
        findViewById(R.id.textView2).setVisibility(View.VISIBLE);
        ((TextView)findViewById(R.id.textView2)).setText("修正完成!");
        ((Button)findViewById(R.id.btn_test)).setText("儲存");
        newBitmap=ImageProcess.myWarpPerspective(bitmapImage,new Point((int)(greenIcon[0].getPureX()*sizeRatioX),(int)(greenIcon[0].getPureY()*sizeRatioY)),new Point((int)(greenIcon[1].getPureX()*sizeRatioX),(int)(greenIcon[1].getPureY()*sizeRatioY)),new Point((int)(greenIcon[2].getPureX()*sizeRatioX),(int)(greenIcon[2].getPureY()*sizeRatioY)),new Point((int)(greenIcon[3].getPureX()*sizeRatioX),(int)(greenIcon[3].getPureY()*sizeRatioY)));
        imageView.setImageBitmap(newBitmap);
        pictureWarped=true;
    }
}