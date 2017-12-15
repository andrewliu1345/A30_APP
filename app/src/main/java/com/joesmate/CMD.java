package com.joesmate;

public class CMD {

    public static final byte[] JH = {0x10, 0x31};
    public static final byte[] DF = {0x44, 0x46};
    public static final byte[] DR = {0x44, 0x52};
    public static final byte[] JF = {0x4A, 0x46};
    public static final byte[] FI = {0x46, 0x73};
    public static final byte[] QS = {0x10, 0x41};
    public static final byte[] PJ = {0x50, 0x4A};
    public static final byte[] PH = {0x10, 0x51};
    public static final byte[] IN = {0x10, 0x61};
    public static final byte[] DB = {0x44, 0x42};
    public static final byte[] BR = {0x42, 0x52};
    public static final byte[] SG = {0x53, 0x47};
    public static final byte[] SR = {0x53, 0x52};
    public static final byte[] AF = {0x41, 0x46};
    public static final byte[] SF = {0x53, 0x46};
    public static final byte[] RR = {0x52, 0x52};
    public static final byte[] UV = {0x55, 0x56};
    public static final byte[] VI = {0x56, 0x49};
    public static final byte[] ID = {'I', 'D'};

    public static final byte[] UART_STX = {0x02, 0x30, 0x30, 0x30, 0x30};
    public static final byte[] UART_ETX = {0x03};
    public static final byte[] MK = {0x33, 0x30, 0x30, 0x32};
    public static final byte[] MM = {0x33, 0x30, 0x30, 0x33};
    public static final byte[] MW = {0x33, 0x30, 0x30, 0x34};
    public static final byte[] MI = {0x33, 0x30, 0x30, 0x35};
    public static final byte[] MQ = {0x33, 0x30, 0x31, 0x47};
    public static final byte[] MC = {0x33, 0x30, 0x31, 0x49};
    public static final byte[] MD = {0x33, 0x30, 0x32, 0x33};

    public static final byte[] CODE_0000 = {0x48, 0x48};
    public static final byte[] CODE_00 = {0x48};
    public static final byte[] CODE_01 = {0x49};

    public static final byte[] CODE_0000H = {0x00, 0x00};
    public static final byte[] CODE_00H = {0x00};
    public static final byte[] CODE_01H = {0x01};
    public static final byte[] CODE_02H = {0x02};
    public static final byte[] CODE_12H = {0x0C};
    public static final byte[] CODE_80H = {(byte) 0x80};
    public static final byte[] CODE_1051H = {0x10, 0X51};

    public static final byte[] CODE_OK = {0x00, 0X00};
    public static final byte[] CODE_ER = {0x00, 0X01};
    //for serial
    public static final byte[] SERIAL_HEAD = {0x02};
    public static final byte[] SERIAL_TAIL = {0x03};

    //for serial request
    public static final byte[] SERIAL_RESERVE = {0x30, 0x30, 0x30, 0x30};

    public static final byte[] SERIAL_COMMAND_BASE = {0x33, 0x30};
    public static final byte[] SERIAL_COMMAND_SM2PK = {0x30, 0x32};
    public static final byte[] SERIAL_COMMAND_MASTERK = {0x30, 0x33};
    public static final byte[] SERIAL_COMMAND_WORKK = {0x30, 0x34};
    public static final byte[] SERIAL_COMMAND_INPUTPW = {0x30, 0x35};
    public static final byte[] SERIAL_COMMAND_CANCELPW = {0x31, 0x47};
    public static final byte[] SERIAL_COMMAND_GETECC = {0x31, 0x39};
    public static final byte[] SERIAL_COMMAND_CLEARK = {0x31, 0x53};

    //for serial response
    public static final byte[] SERIAL_RESPONSE_OK = {0x4f, 0x4b};//response successfully
    public static final byte[] SERIAL_RESPONSE_ERRROR = {0x45, 0x52};//response failed

    public static final byte[] SERIAL_RESPONSE_CHAR = {0x37, 0x31, 0x46};
    public static final byte[] SERIAL_RESPONSE_BACK = {0x35, 0x33, 0x46};
    public static final byte[] SERIAL_RESPONSE_ENTER = {0x35, 0x36, 0x46};
    public static final byte[] SERIAL_KEYBOARD_CHAR = {0x31, 0x32, 0x30, 0x32, 0x34, 0x3b};
    public static final byte[] SERIAL_KEYBOARD_ENTER = {0x36, 0x46, 0x0d};
    public static final byte[] SERIAL_KEYBOARD_CANCEL = {0x36, 0x46, 0x08};
    public static final byte[] SERIAL_KEYBOARD_NUMCHAR = {0x46};
    //Algorithm type
    public static final byte[] ALGORITHM_SM4 = {0x30, 0x31};// 01-SM4
    public static final byte[] ALGORITHM_128B3DES = {0x30, 0x32};//02-128Bit 3DES
    public static final byte[] ALGORITHM_192B3DES = {0x30, 0x33};//03-192bit 3DES

    //speech
    public static final byte[] SPEECH_FIRST = {0x30, 0x31, 0x30, 0x30};
    public static final byte[] SPEECH_SECOND = {0x30, 0x32, 0x30, 0x30};

    //key type
    public static final byte[] KEY_MASTER = {0x30, 0x31};
    public static final byte[] KEY_WORK = {0x30, 0x32};
    public static final byte[] KEY_MASTER_WORK = {0x30, 0x33};

    public static final byte[] KEY_KEYBOARD = {0x00};
    public static final byte[] KEY_SCREEN = {0x01};
    public static final byte[] KEY_PRIMARYPW = {0x00};
    public static final byte[] KEY_WORKPW = {0x01};


    //山东城市银行国密
    public static final byte[] SD_CMD_HEAD = {0x1b, 0x7a};
    public static final byte[] SD_CMD_TAIL = {0x0d, 0x0a};
    public static final byte[] SD_CMD_RESET = {(byte) 0xa0, 0x10};
    public static final byte[] SD_CMD_TYPE = {(byte) 0xa0, 0x05};
    public static final byte[] SD_CMD_LENGTH = {(byte) 0xa0, 0x06};
    public static final byte[] SD_CMD_UPDATE_MAIN_KEY = {(byte) 0Xb0, 0x07};
    public static final byte[] SD_CMD_UPDATE_WORK_KEY = {(byte) 0xa0, 0x08};
    public static final byte[] SD_CMD_ACTIVATE_WORK_KEY = {(byte) 0xa0, 0x09};
    public static final byte[] SD_CMD_READ_PIN = {(byte) 0xb0, 0x0a};

    public static final byte[] SD_CMD_CHECK_MK = {(byte) 0x48};
    public static final byte[] SD_CMD_CHECK_WK = {(byte) 0x4c};

    public static final byte[] KB_CMD_HEAD = {0x1b};
}
