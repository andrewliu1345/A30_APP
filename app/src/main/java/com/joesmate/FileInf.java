package com.joesmate;

public class FileInf {

    public static final int FILE_TYPE_MEDIA = 0;
    public static final int FILE_TYPE_PHOTO = 1;
    public static final int FILE_TYPE_VEDIO = 2;
    public static final int FILE_TYPE_AUDIO = 3;
    public static final int FILE_TYPE_IMG = 4;
    public static final int FILE_TYPE_HTML = 5;
    public static final int FILE_TYPE_PDF = 6;
    public static final int FILE_TYPE_OTHER = 7;
    public static final int FILE_TYPE_SIGNA = 8;

    public static final String MEDIA = "/mnt/sdcard/MEDIA";
    public static final String RES = MEDIA + "/res";
    public static final String IMAGE = MEDIA + "/image";
    public static final String VIDEO = MEDIA + "/video";
    public static final String HEAD = MEDIA + "/head";
    public static final String AUDIO = MEDIA + "/audio";
    public static final String APK = MEDIA + "/apk";
    public static final String HTML = MEDIA + "/html";
    public static final String PDF = MEDIA + "/pdf";
    public static final String OTHER = MEDIA + "/other";
    public static final String HTML_HOME_PAGE = "home.html";
    public static final String APK_NAME = "InhuaSoft_ICBC.apk";
    public static final String KEYS_PATH = MEDIA + "/key";
    public static final String KEY_NAME = "key.xml";
    public static final String SIGNA = MEDIA + "/signa";
    public static final String SYSTEM_LOG = MEDIA + "/log";
    public static final String LOG_NAME = "log.txt";


    //public static final String[] PROVIDE_IMG = {"jpg","png","bmp","gif"/*gif需另做需求*/};
    public static final String[] PROVIDE_IMG = {"jpg", "png", "bmp"/*gif需另做需求*/};
    public static final String[] PROVIDE_VIDEO = {"mp4", "avi", "mov", "asf", "wmv",
            ".3gp", ".rmvb"};
    public static final String[] PROVIDE_AUDIO = {"mp3", "wma", "aac", "ape", "ogg", "wav"};
    public static final String[] PROVIDE_PDF = {"pdf"};
    public static final String[] PROVIDE_HTML = {"html", "htm"};

    public static enum FileType {
        IMG, VIDEO, AUDIO, HTML, PDF, OTHER, SIGNA
    }

    public static int getFileType(FileType fileType) {
        switch (fileType) {
            case IMG:
                return FILE_TYPE_IMG;
            case PDF:
                return FILE_TYPE_PDF;
            case HTML:
                return FILE_TYPE_HTML;
            case AUDIO:
                return FILE_TYPE_AUDIO;

            case VIDEO:
                return FILE_TYPE_VEDIO;
            case SIGNA:
                return FILE_TYPE_SIGNA;
            case OTHER:

            default:
                return FILE_TYPE_OTHER;


        }
    }

    public static String getFilePath(int fileType) {
        if (fileType == FILE_TYPE_MEDIA) {
            return MEDIA;
        } else if (fileType == FILE_TYPE_PHOTO) {
            return HEAD;
        } else if (fileType == FILE_TYPE_VEDIO) {
            return VIDEO;
        } else if (fileType == FILE_TYPE_AUDIO) {
            return AUDIO;
        } else if (fileType == FILE_TYPE_IMG) {
            return IMAGE;
        } else if (fileType == FILE_TYPE_PDF) {
            return PDF;
        } else if (fileType == FILE_TYPE_HTML) {
            return HTML;
        } else if (fileType == FILE_TYPE_SIGNA) {
            return SIGNA;
        } else if (fileType == FILE_TYPE_OTHER) {
            return OTHER;
        }
        return OTHER;
    }

    public static FileType getFileType(int fileType) {
        if (fileType == FILE_TYPE_PHOTO) {
            return FileType.IMG;
        } else if (fileType == FILE_TYPE_VEDIO) {
            return FileType.VIDEO;
        } else if (fileType == FILE_TYPE_AUDIO) {
            return FileType.AUDIO;
        } else if (fileType == FILE_TYPE_IMG) {
            return FileType.IMG;
        } else if (fileType == FILE_TYPE_HTML) {
            return FileType.HTML;
        } else if (fileType == FILE_TYPE_PDF) {
            return FileType.PDF;
        }
        return FileType.OTHER;
    }
}
