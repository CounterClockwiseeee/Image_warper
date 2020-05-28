package com.example.myapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.ImageDecoder;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.RectangleReadOnly;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PdfProcess {
    public static void creatPdfOfImages(ArrayList<Bitmap> bitmaps,String filePath,String fileName) throws IOException, DocumentException {
        Document doc = new Document();
        PdfWriter.getInstance(doc,new FileOutputStream(filePath+"/"+fileName+".pdf"));
        doc.open();

        float maxHeight = 0;
        float maxWidth = 0;
        for(Bitmap b:bitmaps){
            maxHeight = maxHeight>b.getHeight()?maxHeight:b.getHeight();
            maxWidth = maxWidth>b.getWidth()?maxWidth:b.getWidth();
        }
        doc.setPageSize(new RectangleReadOnly(maxWidth,maxHeight));
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        for(Bitmap b:bitmaps){
            b.compress(Bitmap.CompressFormat.JPEG,100,output);
            Image img = Image.getInstance(output.toByteArray());
            doc.add(img);
        }

        doc.close();
    }

}
