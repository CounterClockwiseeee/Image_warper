package com.example.myapplication;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

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
    try {
        PdfProcess.createPdfOfImages(m,"test.pdf");
    } catch (IOException e) {
        e.printStackTrace();
    } catch (DocumentException e) {
        e.printStackTrace();
    }
    */

    public static void createPdfOfImages(ArrayList<Bitmap> bitmaps, String fileName) throws IOException, DocumentException {
        fileName = fileName.equals("")?getDateTime()+".pdf":fileName;
        fileName = fileName.endsWith(".pdf")?fileName:fileName+".pdf";
        Document doc = new Document();
        float maxHeight = 0;
        float maxWidth = 0;
        for(Bitmap b:bitmaps){
            maxHeight = maxHeight>b.getHeight()?maxHeight:b.getHeight();
            maxWidth = maxWidth>b.getWidth()?maxWidth:b.getWidth();
        }
        //doc.setPageSize(PageSize.A4);
        doc.setPageSize(new RectangleReadOnly((float)(maxWidth*1),(float)(maxHeight*1)));
        doc.setMargins(0,0,0,0);
        PdfWriter.getInstance(doc,new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+fileName));
        //Log.d("PDF",Environment.getExternalStorageDirectory()+"/"+fileName);
        doc.open();

        for(Bitmap b:bitmaps){
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            //Log.d("IMG",String.format("%d %d%n",b.getWidth(),b.getHeight()));
            b.compress(Bitmap.CompressFormat.PNG,100,output);
            Image img = Image.getInstance(output.toByteArray());
            img.setAbsolutePosition((maxWidth-b.getWidth())/2,(maxHeight-b.getHeight())/2);
            doc.add(img);
            doc.newPage();
        }
        doc.close();
    }

    private static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
        return sdf.format(new Date());
    }


}
