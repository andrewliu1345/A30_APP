package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;

import java.io.File;
import java.util.Arrays;

/**
 * Created by andre on 2017/12/13 .
 */

public class ExistFileData extends BaseData {
    private static ExistFileData existFileData;
    private int fileType;

    public static ExistFileData getInstance() {
        if (existFileData == null) {
            existFileData = new ExistFileData();
        }
        return existFileData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        if (Arrays.equals(Cmds.CMD_EF.getBytes(), cmd)) {
            int pos = 2;
            fileType = AssitTool.getArrayCount(new byte[]{buffer[pos++], buffer[pos++]});
            Log.d(TAG, "query fileType:" + fileType);
            int namelen = AssitTool.getArrayCount(new byte[]{buffer[pos++], buffer[pos++]});
            byte[] filenamebuff = new byte[namelen];
            System.arraycopy(buffer, pos, filenamebuff, 0, namelen);
            String filename = AssitTool.getString(filenamebuff, AssitTool.UTF_8);
            String filePath = FileInf.getFilePath(fileType);
            //String fileurl = filePath+"/" + filename+".*";
            File file = new File(filePath);
            File[] files = file.listFiles();
            boolean isExist = false;
            for (File f :
                    files) {
                String file_name = f.getName();
                isExist = file_name.equals(filename);
                if (isExist) {
                    break;
                }
//                String[] str_s = file_name.split("\\.");
//                if (str_s.length > 0) {
//                    isExist = str_s[0].equals(filename);
//                    if (isExist) {
//                        break;
//                    }
//                }
            }

            if (isExist) {
                sendConfirmCode(BackCode.CODE_00);
            } else {
                sendConfirmCode(BackCode.CODE_01);
            }
        }
    }

    public void sendConfirmCode(String backcode) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = Cmds.CMD_EF + backcode;
        backData(backCode.getBytes());
    }
}
