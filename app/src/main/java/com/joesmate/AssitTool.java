package com.joesmate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joesmate.FileInf.FileType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class AssitTool {
    public static final String TAG = "AssitTool";
    public static final String UTF_8 = "utf-8";
    public static final String GB2312 = "gb2312";
    public static final String GBK = "GBK";
    public static final String SPLIT_LINE = "&";
    public static final String SPLIT_LINE_NEW = "\\|";
    public static final byte SPLIT_COMMA = 44;

    public static final String KEY_3DES = "DESede";
    public static final String KEY_DES = "DES";

    public static final int PAGE_VIEW_HTML = 0;
    public static final int PAGE_VIEW_PDF = 1;
    public static final int PAGE_VIEW_INPUTPASSWORD = 2;
    public static final int PAGE_VIEW_STATUSAREA = 3;
    public static final int PAGE_VIEW_ReadPin = 9;

    public static final int PAGE_VIEW_SERIAL_INPUT = 20;
    public static final int PAGE_VIEW_SERIAL_INPUT_AGAIN = 21;
    // add sdcs
    public static final int PAGE_VIEW_SDCS_SHOW_HTML = 4;
    public static final int PAGE_VIEW_SDCS_GetUserOperateRstHtml = 22;


    public static final int PAGE_PDF_NOSIGN = 5;
    public static final int PAGE_PDF_SIGN = 6;
    public static final int PAGE_PDF_SHOWSIGN = 7;


    public static final int VOLUME_MUTE = 0;
    public static final int VOLUME_LITTLE = 8;
    public static final int VOLUME_MEDIUM = 12;
    public static final int VOLUME_LARGE = 15;


    public static final String JS_SCRIPT = "<script type=\"text/javascript\" src=\"jquery.js\"></script>\n" +
            "<script type=\"text/javascript\">\n" +
            "    $(document).ready(function () {\n" +
            "      \n" +
            "\n" +
            "        $(\":button\").click(\n" +
            "            function ()\n" +
            "            {\n" +
            "\t\t\t    setInputValue(\"name2\",\"21\");\n" +
            "                if (\"\" != getAllinputData())\n" +
            "                {\n" +
            "                    TlrClient.submit(getAllinputData() + \"&&\" +\"BUTTON=\"+$(this).attr(\"name\"));  \n" +
            "                }\n" +
            "                else\n" +
            "                {\n" +
            "                    TlrClient.submit(\"BUTTON=\" + $(this).attr(\"name\"));\n" +
            "                }\n" +
            "            }\n" +
            "            )\n" +
            "\t\t$(\":text\").focus(\n" +
            "\t\t   function ()\n" +
            "\t\t   {\n" +
            "\t\t   \t  //alert($(this).attr(\"name\"))\n" +
            "\t\t\t  $(this).attr(\"disabled\",true);\n" +
            "\t\t\t  TlrClient.inputchar($(this).attr(\"name\"));\n" +
            "\t\t   }\n" +
            "\t\t)\n" +
            "\n" +
            "\t\t$(\":text:first\").attr(\"disabled\", true);\n" +
            "\t\tTlrClient.inputchar($(\":text:first\").attr(\"name\"));\n" +
            "\n" +
            "\t\t\n" +
            "    });\n" +
            "    function getReturnData() {\n" +
            "        TlrClient.submit(getAllinputData());\n" +
            "    }\n" +
            "    function getAllinputData()\n" +
            "    {\n" +
            "        var valArr = new Array;\n" +
            "        $(\":text\").each(function (i) {\n" +
            "            valArr[i] = $(this).attr(\"name\") + \"=\" + $(this).val();\n" +
            "        });\n" +
            "        var allinput = valArr.join('&');\n" +
            "        return allinput;\n" +
            "    }\n" +
            "\tfunction setInputValue(inputname , value)\n" +
            "\t{\n" +
            "\t  var select = \"[name=\"+inputname+\"]\" ;\n" +
            "\t  $(select).val( $(select).val() + value);\n" +
            "\t}\n" +
            "\tfunction ClearInputValue(inputname)\n" +
            "\t{\n" +
            "\t  var select = \"[name=\"+inputname+\"]\" ;\n" +
            "\t  $(select).val(\"\");\n" +
            "\t}\n" +
            "</script>";


    public static int GetVolumeByLevel(int level) {
        if (level == 0) {
            return VOLUME_MUTE;
        } else if (level == 1) {
            return VOLUME_LITTLE;
        } else if (level == 2) {
            return VOLUME_MEDIUM;
        } else if (level == 3) {
            return VOLUME_LARGE;
        } else {
            return VOLUME_MEDIUM;
        }
    }


    /**
     * 加密
     *
     * @param datasource byte[]
     * @param password   String
     * @return byte[]
     */
    public static byte[] encrypt(byte[] datasource, String password, String key) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            //创建一个密匙工厂，然后用它把DESKeySpec转换成
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(key);
            SecretKey securekey = keyFactory.generateSecret(desKey);
            //Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(key);
            //用密匙初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
            //现在，获取数据并加密
            //正式执行加密操作
            return cipher.doFinal(datasource);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param src      byte[]
     * @param password String
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password, String key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom random = new SecureRandom();
        // 创建一个DESKeySpec对象
        DESKeySpec desKey = new DESKeySpec(password.getBytes());
        // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(key);
        // 将DESKeySpec对象转换成SecretKey对象
        SecretKey securekey = keyFactory.generateSecret(desKey);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(key);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, random);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    public static int getIntenter(String value) {
        int iv = 0;
        try {
            iv = Integer.parseInt(value);
        } catch (Exception e) {
            iv = -1;
        }
        return iv;
    }

    public static byte parseByte(String value) {
        byte bv = 0;
        try {
            bv = Byte.parseByte(value);
        } catch (Exception e) {
            bv = -1;
        }
        return bv;
    }

    public static String combinationArray(byte[] arrays) {
        String comValues = "";
        for (int i = 0; i < arrays.length; i++) {
            comValues += "" + arrays[i];
            if (i != arrays.length - 1) {
                comValues += "|";
            }
        }
        return comValues;
    }

    public static String getQueryFile(String dirPath, FileType fileType) {
        String queryFile = "";
        File[] files = getFileUrls(dirPath);
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            if (fileType != fileType(files[i].getName())) {
                continue;
            }
            queryFile += files[i].getName();
            if (i != files.length - 1) {
                queryFile += SPLIT_LINE;
            }
        }
        return queryFile;
    }

    public static String getQueryFileImg(String dirPath) {
        String queryFile = "";
        File[] files = getFileUrls(dirPath);
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            queryFile += files[i].getName();
            if (i != files.length - 1) {
                queryFile += SPLIT_LINE;
            }
        }
        return queryFile;
    }

    public static File[] getDeleteFiles(int fileType, String files) {
        String filePath = FileInf.getFilePath(fileType);
        if (filePath == null) {
            return null;
        }
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            return null;
        }

        if (files.equals("") || files == null) {
            return fileDir.listFiles();
        }
        String[] fileStrs = getSplit(files, SPLIT_LINE);
        if (fileStrs == null || fileStrs.length == 0) {
            return null;
        }
        File[] files2 = new File[fileStrs.length];
        for (int i = 0; i < fileStrs.length; i++) {
            files2[i] = new File(fileDir, fileStrs[i]);
        }
        return files2;

    }

    // 安装apk
    public static void installApk(File file) {
        App.getInstance().setNavigation(true);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        App.getInstance().startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static String getAppVersion(Context context, String archiveFilePath) {
        if (context == null || archiveFilePath == null) {
            return null;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            return info.versionName;
        }
        return null;
    }

    public static class AddFile {
        public static interface OnAddFileListener {
            public void complete();
        }

        OnAddFileListener operationFileListener;
        FileOutputStream fos;
        byte[] data;

        public AddFile(String path, String name, byte[] data) {
            this.data = data;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fos = null;
            }
            new Thread(runnable).start();

        }

        public AddFile(String path, String name, byte[] data, boolean bAppend) {
            this.data = data;
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fos = new FileOutputStream(file, bAppend);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fos = null;
            }
            new Thread(runnable).start();

        }

        public AddFile(String filePath, byte[] data) {
            this.data = data;
            Log.d(TAG, "AddFile filePath:" + filePath);
            String path = filePath.substring(0, filePath.lastIndexOf("/"));
            String name = filePath.substring(filePath.lastIndexOf("/") + 1);
            Log.d(TAG, "AddFile path:" + path);
            Log.d(TAG, "AddFile name:" + name);
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            final File file = new File(dir, name);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                fos = null;
            }
            new Thread(runnable).start();

        }

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (fos != null) {
                    try {
                        fos.write(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                        closeFos();
                    } finally {
                        if (operationFileListener != null) {
                            operationFileListener.complete();
                        }
                        closeFos();
                    }
                }

            }
        };

        public void setOnOperationFileListener(
                OnAddFileListener operationFileListener) {
            this.operationFileListener = operationFileListener;
        }

        private void closeFos() {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fos = null;
            }
        }
    }

    public static class DeleteFile {
        File[] files;

        public DeleteFile(File[] files) {
            this.files = files;
            new Thread(runnable).start();
        }

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                for (File file : files) {
                    if (file == null || !file.exists()) {
                        continue;
                    }
                    if (file.isDirectory()) {
                        File[] filearray = file.listFiles();
                        new AssitTool.DeleteFile(filearray);
                    }
                    String fileInf = file.getAbsolutePath();
                    boolean isDelete = file.delete();
                    Log.d(TAG, "delete:" + fileInf + "is " + isDelete);
                }
            }
        };
    }

    public static String getString(byte[] data, String charset) {
        String str = null;
        try {
            str = new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static String getString(String data, String charset) {

        String str = null;
        try {
            str = new String(data.getBytes(UTF_8), charset);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    /**
     * "|"
     *
     * @param options
     * @return
     */
    public static String[] getSplit(byte[] options, String split) {
        String str = getString(options, UTF_8);
        Log.d(TAG, "splite str:" + str);
        return str.split(split);
    }

    /**
     * "|"
     *
     * @param options
     * @return
     */
    public static String[] getSplit(String options, String split) {
        Log.d(TAG, "splite str:" + options);
        return options.split(split);
    }

    /**
     * @param value
     * @param split
     * @param isLast 取标记的后面的值
     * @return
     */
    public static String getSplit(String value, byte split, boolean isLast) {
        if (value == null) {
            return null;
        }
        int index = value.indexOf(split);
        if (isLast) {
            if (index != -1) {
                return value.substring(index + 1, value.length());
            }
        } else {
            if (index != -1) {
                return value.substring(0, index);
            } else {
                return value;
            }
        }
        return null;
    }

    /**
     * 检测是否有资源可以播放\且update资源
     *
     * @return
     */
    public static boolean isPlay() {
        File[] fileUrls = AssitTool.getFileUrls(FileInf.RES);
        if (fileUrls == null) {
            Log.d(TAG, "res file not exist");
            // 文件夹不存在或空文件夹
            return false;
        }
        // 检测文件夹文件是否有可播放的
        int erroNum = 0;
        for (File file : fileUrls) {
            if (file == null || !file.exists()) {
                erroNum++;
                continue;
            }
            FileType fileType = AssitTool.fileType(file.getAbsolutePath());
            if (fileType == FileType.OTHER) {
                erroNum++;
            }
        }
        if (erroNum >= fileUrls.length) {
            Log.d(TAG, "res file not legal");
            return false;
        }
        //Log.d(TAG, "res file can play");
        return true;
    }


    /**
     * 检测是否有资源可以播放\且update资源
     *
     * @return
     */
    public static boolean isImgCanPlay() {
        File[] fileUrls = AssitTool.getFileUrls(FileInf.IMAGE);
        if (fileUrls == null) {
            Log.d(TAG, "res file not exist");
            // 文件夹不存在或空文件夹
            return false;
        }
        // 检测文件夹文件是否有可播放的
        int okNum = 0;
        for (File file : fileUrls) {
            if (file != null && file.exists()) {
                FileType fileType = AssitTool.fileType(file.getAbsolutePath());
                if (fileType == FileType.IMG) {
                    okNum = okNum + 1;
                }
                continue;
            }
        }
        if (okNum > 0) {
            Log.d(TAG, "res  img  file can  play");
            return true;
        } else {
            Log.d(TAG, "res file is not legal");
            return false;
        }
    }


    /**
     * 检测是否有资源可以播放\且update资源
     *
     * @return
     */
    public static boolean isVideoCanPlay() {
        File[] fileUrls = AssitTool.getFileUrls(FileInf.VIDEO);
        if (fileUrls == null) {
            Log.d(TAG, "res file not exist");
            // 文件夹不存在或空文件夹
            return false;
        }
        // 检测文件夹文件是否有可播放的
        int okNum = 0;
        for (File file : fileUrls) {
            if (file != null && file.exists()) {
                FileType fileType = AssitTool.fileType(file.getAbsolutePath());
                if (fileType == FileType.VIDEO) {
                    okNum = okNum + 1;
                }
                continue;
            }
        }
        if (okNum > 0) {
            Log.d(TAG, "res  video  file can  play");
            return true;
        } else {
            Log.d(TAG, "res video is not legal");
            return false;
        }
    }


    public static File[] getFileUrls(String path) {
        if (path == null) {
            return null;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        return files;
    }

    public static ArrayList<File> getFileUrls_IMG(String path) {
        if (path == null) {
            return null;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        ArrayList<File> files_img = new ArrayList<File>();
        for (int i = 0; i < files.length; ++i) {
            FileType fileType = fileType(files[i].getAbsolutePath());
            if (fileType == FileType.IMG) {
                files_img.add(files[i]);
            }
        }

        return files_img;
    }

    public static ArrayList<File> getFileUrls_VIDEO(String path) {
        if (path == null) {
            return null;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            return null;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        ArrayList<File> files_video = new ArrayList<File>();
        for (int i = 0; i < files.length; ++i) {
            FileType fileType = fileType(files[i].getAbsolutePath());
            if (fileType == FileType.VIDEO) {
                files_video.add(files[i]);
            }
        }

        return files_video;
    }


    public static String getPostfix(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastPointIndex = fileName.lastIndexOf(".");
        if (lastPointIndex == -1) {
            return null;
        }
        if (lastPointIndex + 1 > fileName.length()) {
            return null;
        }
        return fileName.substring(lastPointIndex + 1);
    }

    /// 去掉文件后缀名
    public static String getFileName(String fileName) {
        if (fileName == null) {
            return null;
        }
        int lastPointIndex = fileName.lastIndexOf(".");
        if (lastPointIndex == -1) {
            return null;
        }
        if (lastPointIndex + 1 > fileName.length()) {
            return null;
        }
        return fileName.substring(0, lastPointIndex);
    }


    /**
     * @param fileUrl
     * @return 1 img; 0 video
     */
    public static FileType fileType(String fileUrl) {
        fileUrl = getPostfix(fileUrl);
        if (fileUrl == null) {
            return FileType.OTHER;
        }
        for (String img : FileInf.PROVIDE_IMG) {
            if (fileUrl.toLowerCase().contains(img)) {
                return FileType.IMG;
            }
        }
        for (String video : FileInf.PROVIDE_VIDEO) {
            if (fileUrl.toLowerCase().contains(video)) {
                return FileType.VIDEO;
            }
        }
        for (String audio : FileInf.PROVIDE_AUDIO) {
            if (fileUrl.toLowerCase().contains(audio)) {
                return FileType.AUDIO;
            }
        }
        return FileType.OTHER;
    }

    public static LinearLayout.LayoutParams getLinearParams(int width,
                                                            int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    public static String arrayToString(byte[] buffer, String charsetName) {
        String str = null;

        try {
            str = new String(buffer, charsetName);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            str = null;
        }
        return str;
    }

    public static int getArrayCount(byte[] buffer) {
        int num = 0;
        if (buffer == null) {
            Log.e(TAG, "getArrayCount buffer is null");
            return -1;
        }
        for (int i = 0; i < buffer.length; i++) {
            int n = (char) buffer[i] - 48;
            //Log.d(TAG, "n:" + n);
            if (n <= -1 || n >= 10) {
                Log.e(TAG, "getArrayCount num is illegal");
                return -1;
            }

            num += (i == (buffer.length - 1) ? n : n
                    * Math.pow(10, buffer.length - i - 1));
        }
        return num;
    }

    public static byte getAscall(int value) {
        return (byte) (0x30 ^ value);
    }

    public static boolean isFileExist(String url) {
        File file = new File(url);
        if (file != null && file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 拆分数据
     *
     * @param buffer
     * @return
     */
    public static byte[] splitBuffer(byte[] buffer, int tag) {
        if (buffer == null || buffer.length <= 0) {
            return null;
        }
        byte[] splitBuf = new byte[buffer.length * 2];
        for (int i = 0; i < buffer.length; i++) {

            byte hight = (byte) ((buffer[i] & 0xf0) >> 4);
            byte low = (byte) (buffer[i] & 0x0f);

            splitBuf[i * 2] = (byte) (hight + tag);
            splitBuf[(i * 2) + 1] = (byte) (low + tag);
        }
        return splitBuf;
    }

    /**
     * 合并拆分数据
     *
     * @param buffer
     * @return
     */
    /*
     * public static byte[] merge(byte[] buffer, int tag) { if (buffer == null
	 * || buffer.length <= 0) { return null; }
	 * 
	 * byte[] mergeBuf = new byte[buffer.length / 2]; for (int i = 0; i <
	 * mergeBuf.length; i++) { byte hight = (byte) (buffer[i * 2] - tag); byte
	 * low = (byte) (buffer[(i * 2) + 1] - tag); // 高低位合并 mergeBuf[i] = (byte)
	 * (((hight << 4) & 0xf0) | low & 0x0f); } return mergeBuf; }
	 * 
	 * public static int arrayToInteger(byte[] buffer) { byte hight = (byte)
	 * ((buffer[0] << 8) & 0xff00); byte low = (byte) (buffer[1] & 0x00ff);
	 * 
	 * return hight | low; }
	 */
    public static byte[] merge(byte[] buffer, int tag) {
        if (buffer == null || buffer.length <= 0) {
            return null;
        }

        byte[] mergeBuf = new byte[buffer.length / 2];
        for (int i = 0; i < mergeBuf.length; i++) {
            int hight = (buffer[i * 2] - tag);
            int low = (buffer[(i * 2) + 1] - tag);
            // 高低位合并
            mergeBuf[i] = (byte) (((hight << 4) & 0xf0) | low & 0x0f);
        }
        return mergeBuf;
    }

    public static int arrayToInteger(byte[] buffer) {
        int hight = ((buffer[0] << 8) & 0xff00);
        int low = (buffer[1] & 0x00ff);

        return hight | low;
    }

    /**
     * 由于长度是2个字节，所以最高位与次高位用不到
     *
     * @param value
     * @return
     */
    public static byte[] integerToArray(int value) {
        byte[] array = new byte[2];
        array[0] = (byte) ((value & 0xff00) >> 8);
        array[1] = (byte) (value & 0x00ff);
        return array;
    }

    public static byte[] getCount6N(int length) {
        byte[] data = new byte[6];
        data[0] = getAscall((length / 100000) % 10);
        data[1] = getAscall((length / 10000) % 10);
        data[2] = getAscall((length / 1000) % 10);
        data[3] = getAscall(length / 100 % 10);
        data[4] = getAscall(length / 10 % 10);
        data[5] = getAscall(length % 10);
        return data;
    }


    public static byte[] getCount4N(int length) {
        byte[] data = new byte[4];
        data[0] = getAscall((length / 1000) % 10);
        data[1] = getAscall((length / 100) % 10);
        data[2] = getAscall((length / 10) % 10);
        data[3] = getAscall(length % 10);
        return data;
    }

    public static byte[] getCount43(int length) {
        byte[] data = new byte[3];
        data[0] = getAscall((length / 100) % 10);
        data[1] = getAscall((length / 10) % 10);
        data[2] = getAscall(length % 10);
        return data;
    }

    public static byte[] getCount42(int length) {
        byte[] data = new byte[2];
        data[0] = getAscall((length / 10) % 10);
        data[1] = getAscall(length % 10);
        return data;
    }


    public static byte[] getCount41(int length) {
        byte[] data = new byte[1];
        data[0] = getAscall(length % 10);
        return data;
    }


/*	public static byte[] getCount42(int length) {
        byte[] data = new byte[3];
		data[1] = getAscall((length / 10) % 10);
		data[2] = getAscall(length % 10);
		return data;
	}*/


    // html url 编码
    public static String URLEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // html url 解码
    public static String URLDecode(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 删除单个文件
     *
     * @param filePath 被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }


    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }


    public static void setProgressbarState(int totalNum, int curIndex) {
        int frameNum = totalNum - curIndex;
        int curProgress = (frameNum + 2) * 100 / totalNum;
        Intent intent;
        if (1 == curIndex) {
            intent = new Intent(AppAction.ACTION_BROADCAST_CLOSE_PROGRESSBAR);
        } else if ((totalNum == curIndex) && curIndex != 1) {
            intent = new Intent(AppAction.ACTION_BROADCAST_SHOW_PROGRESSBAR);
        } else {
            intent = new Intent(AppAction.ACTION_BROADCAST_UPDATE_PROGRESSBAR);
        }
        //intent.putExtra(AppAction.KEY_BROADCAST_PROGRESS, curProgress + "%,  " + frameNum*4+"KB" + "/" +totalNum*4+"KB");
        intent.putExtra(AppAction.KEY_BROADCAST_PROGRESS, curProgress + "%");
        App.getInstance().sendBroadcast(intent);
    }


    public static void SetSystemVolume(int volume) {
        AudioManager mAudioManager = (AudioManager) App.getInstance().getSystemService(App.getInstance().AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        Log.d("volume", "current volume:" + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
    }


    public static ViewGroup.LayoutParams getLayoutParams() {
        return AssitTool.getLinearParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
    }


    public static void copyJqueryToSdcard(Context context, String strOutFileName) throws IOException {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open("jquery-2.2.0.min.js");
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while (length > 0) {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }
        myOutput.flush();
        myInput.close();
        myOutput.close();
    }


    /**
     * 追加文件：使用FileOutputStream，在构造FileOutputStream时，把第二个参数设为true
     *
     * @param fileName
     * @param content
     */
    public static void addScripttoIndexHtml(String file, String conent) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static Typeface getFonts(int font) {
        File file = new File("/system/fonts/" + font + ".ttf");
        if (file.exists()) {
            return Typeface.createFromFile("/system/fonts/" + font + ".ttf");
        } else {
            return null;
        }
    }

    public static byte getBytesCrc(byte[] data) {

        if (data.length >= 2) {
            byte result = data[0];
            for (int i = 1; i < data.length; ++i) {
                result = (byte) (result ^ data[i]);
            }
            return result;
        } else {
            return data[0];
        }
    }

    public static byte[] StringToBytes(String str) {
        byte[] data = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; ++i) {
            String str1 = str.substring(2 * i, 2 * i + 2);
            data[i] = (byte) Integer.parseInt(str1, 16);
        }
        return data;
    }

}
