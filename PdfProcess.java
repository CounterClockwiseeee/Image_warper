package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PdfProcess {
    /*
    by calling this method with param fileName = ""
    the fileName is default set to current time by "yyyy-MM-dd-HH-mm-ss.pdf"
    */

    //path to save the pdf: internalStorage/

    //How to use this method:
    /*
    ArrayList<Bitmap> m = new ArrayList<Bitmap>();
    m.add(bitmap1);
    m.add(bitmap);
    PdfProcess.createPdfOfImages(MainActivity.this,m,"test.pdf");

    */

    public static void createPdfOfImages(final Activity activity, final ArrayList<Bitmap> bitmaps, String fileName){
        final ProgressDialog dialog = ProgressDialog.show(activity, "正在產生PDF...", "請稍後", true);
        fileName = fileName.equals("") ? getDateTime() + ".pdf" : fileName;
        fileName = fileName.endsWith(".pdf") ? fileName : fileName + ".pdf";
        final String finalFileName = fileName;
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    PdfWriter.getInstance(doc, new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + finalFileName));
                    doc.open();
                    for (Bitmap b : bitmaps) {
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        b.compress(Bitmap.CompressFormat.PNG, 100, output);
                        Image img = Image.getInstance(output.toByteArray());
                        img.setAbsolutePosition((maxWidth - b.getWidth()) / 2, (maxHeight - b.getHeight()) / 2);
                        doc.add(img);
                        doc.newPage();
                        output.close();
                    }
                    doc.close();
                } catch (Exception e) {
                    Log.d("EXC", e.toString());
                }finally {
                    dialog.dismiss();
                    Looper.prepare();
                    @SuppressLint("HandlerLeak") Handler handler = new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setMessage("成功建立PDF!");
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

    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }


}
