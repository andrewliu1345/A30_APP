package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.FileInf;

import java.util.Arrays;

public class AddFileData extends BaseData {


    private int fileType;
    private String fileUrl;
    public static final int MIN_LEN = 18;
    private int curIndex, totalNum;
    private boolean boolAppend = false;
    private byte[] dataBuffer;
    private String fileName;
    private static AddFileData addFileData;

    public static AddFileData getInstance() {
        if (addFileData == null) {
            addFileData = new AddFileData();
        }
        return addFileData;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getCurIndex() {
        return curIndex;
    }

    public void setCurIndex(int curIndex) {
        this.curIndex = curIndex;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public byte[] getDataBuffer() {
        return dataBuffer;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        if (Arrays.equals(CMD.AF, cmd)) {
            int bufLen = buffer.length;
            if (bufLen < MIN_LEN) {
                sendConfirmCode(BackCode.CODE_12);
                return;
            }
            fileType = AssitTool.getArrayCount(new byte[]{buffer[2], buffer[3]});
            setFileType(fileType);
            int fileNameLen = AssitTool.getArrayCount(new byte[]{buffer[4], buffer[5]});
            Log.d(TAG, "fileNameLen：" + fileNameLen);
            byte[] arrayFileName = new byte[fileNameLen];
            System.arraycopy(buffer, 6, arrayFileName, 0, fileNameLen);
            String fileName = AssitTool.getString(arrayFileName, AssitTool.UTF_8);
//            if (FileInf.getFileType(AssitTool.fileType(fileName)) != fileType) {
//                setFileType(FileInf.getFileType(AssitTool.fileType(fileName)));
//            }
//			if (fileType!=1){
//				fileType=FileInf.getFileType(AssitTool.fileType(fileName)) ;//获取文件类型
//				setFileType(fileType);
//			}
            Log.d(TAG, "fileName：" + fileName);
            setFileName(fileName);

            int index = 6 + fileNameLen;
            Log.d(TAG, "index:" + index);
            Log.d(TAG, "" + buffer[index] + "," + buffer[index + 1] + "," + buffer[index + 2] + "," + buffer[index + 3]);
            curIndex = AssitTool.getArrayCount(new byte[]{buffer[index], buffer[index + 1], buffer[index + 2], buffer[index + 3]});

            totalNum = AssitTool.getArrayCount(new byte[]{buffer[index + 4], buffer[index + 5], buffer[index + 6], buffer[index + 7]});
            Log.d(TAG, "fileType：" + fileType);
            Log.d(TAG, "curIndex：" + curIndex);
            Log.d(TAG, "totalNum：" + totalNum);
            int dataLen = AssitTool.getArrayCount(new byte[]{buffer[index + 8], buffer[index + 9], buffer[index + 10], buffer[index + 11]});
            Log.d(TAG, "dataLen：" + dataLen);
            Log.d(TAG, "bufLen：" + bufLen);
            if (dataLen + MIN_LEN + fileNameLen != bufLen) {
                Log.w(TAG, "len illegal");
                sendConfirmCode(BackCode.CODE_12);
                return;
            }
            byte[] receiveBuf = new byte[dataLen];
            System.arraycopy(buffer, index + 12, receiveBuf, 0, receiveBuf.length);
            if (totalNum == curIndex) {
                //包序号采用倒序,序号与总包个数相同则是新包
                fileDatas.clear();
            } else if (totalNum < curIndex) {
                byte[] header = new byte[24];
                System.arraycopy(buffer, 0, header, 0, header.length);
                Log.w(TAG, "error: totalNum < curIndex");
                AssitTool.setProgressbarState(totalNum, 1);
                fileDatas.clear();
                sendConfirmCode(BackCode.CODE_12);
                return;
            }
            FileData fileData = new FileData(receiveBuf.length);
            fileData.setBuffer(receiveBuf);
            fileDatas.add(fileData);
            if (curIndex == 1 || (totalNum + 1 - curIndex) % 1000 == 0) {
                dataBuffer = getTotalFileData(fileDatas);
                fileDatas.clear();
                Log.d(TAG, "文件写入");
                new AssitTool.AddFile(FileInf.getFilePath(fileType), fileName, dataBuffer, boolAppend);
                boolAppend = true;
                if (curIndex == 1)
                    boolAppend = false;
            }
            sendConfirmCode(BackCode.CODE_00);
            AssitTool.setProgressbarState(totalNum, curIndex);
        }
    }

    private void sendConfirmCode(String code) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = Cmds.CMD_AF + code;
        backData(backCode.getBytes());
    }
}
