package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.pdf.HandWriteToPDF;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by andre on 2017/12/27 .
 */

public class FingerSignerData extends BaseData {

    private int pdfPageNumber;
    private int signX;
    private int signY;
    private int signWidth;
    private int signHeight;
    private String fileName = "esign.pdf";


    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPDFPath() {
        Log.d(TAG, "getPDFPath:" + FileInf.PDF + "/" + fileName);
        return FileInf.PDF + "/" + fileName;
    }

    public String getPDFSignPath() {
        Log.d(TAG, "getPDFSignPath:" + FileInf.PDF + "/" + AssitTool.getFileName(fileName) + "_sign.pdf");
        return FileInf.PDF + "/" + AssitTool.getFileName(fileName) + "_sign.pdf";
    }

    public String getSignImagePath() {
        Log.d(TAG, "getSignImagePath:" + FileInf.PDF + "/" + "signa.bmp");
        return FileInf.PDF + "/" + "signa.bmp";
    }

    public int getSignX() {
        return signX;
    }

    public void setSignX(int signX) {
        this.signX = signX;
    }

    public int getSignY() {
        return signY;
    }

    public void setSignY(int signY) {
        this.signY = signY;
    }

    public int getSignWidth() {
        return signWidth;
    }

    public void setSignWidth(int signWidth) {
        this.signWidth = signWidth;
    }

    public int getSignHeight() {
        return signHeight;
    }

    public void setSignHeight(int signHeight) {
        this.signHeight = signHeight;
    }

    public int getPdfPageNumber() {
        if (pdfPageNumber <= 0)
            pdfPageNumber = 1;
        return pdfPageNumber;
    }

    public void setPdfPageNumber(int pdfPageNumber) {
        this.pdfPageNumber = pdfPageNumber;
    }

    private static FingerSignerData mInstance;

    public static FingerSignerData getInstance() {
        if (mInstance == null) {
            mInstance = new FingerSignerData();
        }
        return mInstance;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_SF.getBytes(), cmd)) {
            pos = 2;
            pdfPageNumber = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "pdfPageNumber:" + pdfPageNumber);

            pos = pos + 4;
            signX = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "signX:" + signX);

            pos = pos + 4;
            signY = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "signY:" + signY);

            pos = pos + 4;
            signWidth = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "signWidth:" + signWidth);

            pos = pos + 4;
            signHeight = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "signHeight:" + signHeight);

            signaturePdf();
            ResposeFingerSignerData.getInstance().setSignPdfData();
            ResposeFingerSignerData.getInstance().MakeResposeData();

            sendConfirmCode(Cmds.CMD_SF);
        }

    }

    void signaturePdf() {
        //creat signature bitmap
        int width = getSignWidth(), height = getSignHeight();

        File dir = new File(FileInf.PDF);
        if (!dir.exists()) {
            dir.mkdirs();
        }


        //sign to pdf
        String InPdfFilePath = getPDFPath();
        String OutPdfFilePath = getPDFSignPath();
        String InPicFilePath = getSignImagePath();
        HandWriteToPDF handWriteToPDF = new HandWriteToPDF(InPdfFilePath, OutPdfFilePath, InPicFilePath);
        Log.e("bill", "width:" + width);
        Log.e("bill", "height:" + height);
        handWriteToPDF.addText(getPdfPageNumber(), width,
                height,
                getSignX(), getSignY());

    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = backCmd + BackCode.CODE_00;
        backData(backCode.getBytes());
    }


}
