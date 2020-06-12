package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PdfProcess {
    /*
    by calling this method with param fileName = ""
    the fileName is default set to current time by "yyyy-MM-dd-HH-mm-ss.pdf"
    */

    //path to save the pdf: internalStorage/Documents

    //How to use this method:
    /*
    ArrayList<Bitmap> m = new ArrayList<Bitmap>();
    m.add(bitmap1);
    m.add(bitmap);
    PdfProcess.createPdfOfImagesWithUserInput(MainActivity.this,m);

    */

    /*Add this to the 'BOTTOM' of the xml file that you want to create pdf
    <TextView
        android:id="@+id/progress_msg"
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:layout_gravity="center"
        android:layout_margin="50dp"
        android:layout_marginTop="40dp"
        android:background="#FFFFFF"
        android:paddingLeft="40dp"
        android:paddingTop="40dp"
        android:paddingRight="40dp"
        android:textColor="#03DAC5"
        android:textSize="18sp"
        android:visibility="invisible" />

    <ProgressBar
        android:id="@+id/progress_bar"
        style="?android:attr/progressBarStyleHorizontal"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:layout_margin="60dp"
        android:indeterminate="false"
        android:max="100"
        android:paddingTop="10dp"
        android:progress="0"
        android:visibility="invisible" />

        */

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static void createPdfOfImages(final Activity activity, final ArrayList<Bitmap> bitmaps, String fileName){
        final ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.progress_bar);
        final TextView textView = (TextView)activity.findViewById(R.id.progress_msg);

        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        textView.setText(String.format("%s              %d/%d","正在產生PDF......",0,bitmaps.size()));
        textView.setVisibility(View.VISIBLE);

        fileName = fileName.equals("") ? getDateTime() + ".pdf" : fileName;
        fileName = fileName.endsWith(".pdf") ? fileName : fileName + ".pdf";
        final String finalFileName = fileName;
        new Thread(new Runnable() {
            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                boolean isSuccess=false;
                try {
                    Document doc = new Document();
                    float maxHeight = 0;
                    float maxWidth = 0;
                    for (Bitmap b : bitmaps) {
                        maxHeight = maxHeight > b.getHeight() ? maxHeight : b.getHeight();
                        maxWidth = maxWidth > b.getWidth() ? maxWidth : b.getWidth();
                    }
                    doc.setPageSize(new RectangleReadOnly((float) (maxWidth * 1), (float) (maxHeight * 1)));
                    doc.setMargins(0, 0, 0, 0);
                    PdfWriter.getInstance(doc, new FileOutputStream(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath() +  File.separator + finalFileName));
                    doc.open();

                    for (int i=0;i<bitmaps.size();i++) {
                        Bitmap b = bitmaps.get(i);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, output);
                        Image img = Image.getInstance(output.toByteArray());
                        img.setAbsolutePosition((maxWidth - b.getWidth()) / 2, (maxHeight - b.getHeight()) / 2);
                        doc.add(img);
                        doc.newPage();
                        output.close();
                        progressBar.setProgress((int)(((i +1)*100)/bitmaps.size()));
                        //Log.d("PROC",String.format("%d",(int)(((i +1)*100)/bitmaps.size())));
                        textView.setText(String.format("%s              %d/%d","正在產生PDF......",i+1,bitmaps.size()));
                    }
                    doc.close();
                    isSuccess=true;
                } catch (Exception e) {
                    Log.d("EXC", e.toString());
                }finally {
                    progressBar.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);

                    Looper.prepare();
                    final boolean finalIsSuccess = isSuccess;
                    @SuppressLint("HandlerLeak") Handler handler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage(finalIsSuccess ?"成功建立PDF!":"發生不明錯誤，請再試一次");
                            builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        }
                    };
                    handler.sendEmptyMessage(0);
                    Looper.loop();
                }
            }
        }).start();
    }


    public static void createPdfOfImagesWithUserInput(final Activity activity, final ArrayList<Bitmap> bitmaps){
        AlertDialog.Builder inputFileName = new AlertDialog.Builder(activity);
        inputFileName.setCancelable(false);
        inputFileName.setTitle("請輸入PDF名稱:");

        final LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        final EditText input = new EditText(activity);
        input.setHint("檔案名稱");

        layout.addView(input, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.setPadding(50,10,50,10);
        inputFileName.setView(layout);

        inputFileName.setPositiveButton("確定", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PdfProcess2.createPdfOfImages(activity,bitmaps, input.getText().toString());
            }
        });
        AlertDialog alertDialog = inputFileName.create();
        alertDialog.show();
    }


    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
