package com.elitetechnologies.watsapppromoter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.InputStream;
import java.util.Iterator;

class ProcessHelper {
    private Context mContext;
     ProcessHelper(Context context) {
        this.mContext=context;
    }

    public void readExcelFileFromAssets() {
        try {
            InputStream myInput;
            // initialize asset manager
            AssetManager assetManager = mContext.getAssets();
            //  open excel sheet
            myInput = assetManager.open("EliteData.xls");
            // Create a POI File System object
            POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            // Get the first sheet from workbook
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);
            // We now need something to iterate through the cells.
            Iterator<Row> rowIter = mySheet.rowIterator();
            int rowno = 0;
            while (rowIter.hasNext()) {
                HSSFRow myRow = (HSSFRow) rowIter.next();
                if (rowno != 0) {
                    Iterator<Cell> cellIter = myRow.cellIterator();
                    int colno = 0;
                    String strNumber = "";
                    while (cellIter.hasNext()) {
                        HSSFCell myCell = (HSSFCell) cellIter.next();
                        if (colno == 0) {
                            strNumber = myCell.toString();
                        }
                        colno++;
                    }
                    sendMessage(strNumber.replace("+91", "") + "");
                }
                rowno++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(String strMobileNo) {
        try {
            if (strMobileNo != null && !strMobileNo.equalsIgnoreCase("")) {
                String strWhatsAppNo = "91" + strMobileNo; // E164 format without '+' sign
                System.out.println("strWhatsAppNo: "+strWhatsAppNo);
                Intent intent = new Intent(Intent.ACTION_SEND);
                Bitmap picBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                        R.drawable.promoting_image);
                if (picBitmap != null) {
                    String url = MediaStore.Images.Media.insertImage(mContext.getContentResolver(), picBitmap, "", "");
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(url));
                } else {
                    intent.setType("text/plain");
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(Intent.EXTRA_TEXT, "Sent By Whatsapp Broadcaster");
                intent.putExtra("jid", strWhatsAppNo + "@s.whatsapp.net"); //phone number without "+" prefix
                intent.setPackage("com.whatsapp");
                mContext.startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
