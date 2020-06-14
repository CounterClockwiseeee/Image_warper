package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import org.bytedeco.javacpp.opencv_core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PdfReader {
    private int pageCount;
    ArrayList<Bitmap> pages;

    public int getPageCount() {
        return pageCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PdfReader(Activity activity, final String filePath){
        this.pageCount = 0;
        final ProgressDialog dialog = ProgressDialog.show(activity,"正在開啟PDF...","請稍後",false);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
                        if (renderer.getPageCount() > 0) {
                            for (int i = 0; i < renderer.getPageCount(); i++) {
                                PdfRenderer.Page page = renderer.openPage(i);
                                Bitmap temp = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                                page.render(temp, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                                Log.d("DONE", "" + i);
                                pages.add(temp);
                            }
                        }
                    }catch(Exception e){
                        e.getStackTrace();
                    }finally{
                        dialog.dismiss();
                    }
                }
            }).start();

    }

    public static void deletePdf(String filePath){
        File pdf = new File(filePath);
        pdf.deleteOnExit();
    }

    public Bitmap readPdf(int pageCount){
        return pages.get(pageCount);
    }
}
