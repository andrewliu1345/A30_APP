package com.joesmate.bin.keyBoard;

import com.joesmate.AssitTool;
import com.joesmate.CMD;
import com.joesmate.frames.BaseSerialFrames;

/**
 * Created by zwzhu on 2016/1/15.
 * 向密码密码键盘发送命令
 */
public class SerialRequestFrame extends BaseSerialFrames {

    private byte[] SData;
    private byte[] SCommand;


    public byte[] getSData() {
        return SData;
    }

    public void setSData(byte[] SData) {
        this.SData = SData;
    }

    public byte[] getSCommand() {
        return SCommand;
    }

    public void setSCommand(byte[] SCommand) {
        this.SCommand = SCommand;
    }

    public int getCommandLen()
    {
        return 4;
    }

    @Override
    public byte[] makePackage() {
        int length = CMD.SERIAL_HEAD.length
                +CMD.SERIAL_RESERVE.length
                +CMD.SERIAL_COMMAND_BASE.length+CMD.SERIAL_COMMAND_SM2PK.length
                +CMD.SERIAL_TAIL.length;
        int copylen = 0;
        byte[] writeByte = new byte[length];
        System.arraycopy(CMD.SERIAL_HEAD,0,writeByte,0,CMD.SERIAL_HEAD.length);
        copylen += CMD.SERIAL_HEAD.length;
        System.arraycopy(CMD.SERIAL_RESERVE,0,writeByte,copylen,CMD.SERIAL_RESERVE.length);
        copylen += CMD.SERIAL_RESERVE.length;
        System.arraycopy(CMD.SERIAL_COMMAND_BASE,0,writeByte,copylen,CMD.SERIAL_COMMAND_BASE.length);
        copylen += CMD.SERIAL_COMMAND_BASE.length;
        System.arraycopy(CMD.SERIAL_COMMAND_SM2PK,0,writeByte,copylen,CMD.SERIAL_COMMAND_SM2PK.length);
        copylen += CMD.SERIAL_COMMAND_SM2PK.length;
        System.arraycopy(CMD.SERIAL_TAIL,0,writeByte,copylen,CMD.SERIAL_TAIL.length);
        return writeByte;
    }

    public byte[] makePackage(byte[] serialCmd) {
    	
        int length = CMD.SERIAL_HEAD.length
                +CMD.SERIAL_RESERVE.length
                +CMD.SERIAL_COMMAND_BASE.length+serialCmd.length
                +CMD.SERIAL_TAIL.length;
        int copylen = 0;
        byte[] writeByte = new byte[length];
        System.arraycopy(CMD.SERIAL_HEAD,0,writeByte,0,CMD.SERIAL_HEAD.length);
        copylen += CMD.SERIAL_HEAD.length;
        System.arraycopy(CMD.SERIAL_RESERVE,0,writeByte,copylen,CMD.SERIAL_RESERVE.length);
        copylen += CMD.SERIAL_RESERVE.length;
        System.arraycopy(CMD.SERIAL_COMMAND_BASE,0,writeByte,copylen,CMD.SERIAL_COMMAND_BASE.length);
        copylen += CMD.SERIAL_COMMAND_BASE.length;
        System.arraycopy(serialCmd,0,writeByte,copylen,serialCmd.length);
        copylen += serialCmd.length;
        System.arraycopy(CMD.SERIAL_TAIL,0,writeByte,copylen,CMD.SERIAL_TAIL.length);
        return writeByte;
    } 
    
    public byte[] makePackage(byte[] serialCmd, byte[] data) {
    	final int SPLIT = 0x30;
        int length = CMD.SERIAL_HEAD.length
                +CMD.SERIAL_RESERVE.length
                +CMD.SERIAL_COMMAND_BASE.length+serialCmd.length
                +data.length+data.length
                +CMD.SERIAL_TAIL.length;
        int copylen = 0;
        byte[] writeByte = new byte[length];
        System.arraycopy(CMD.SERIAL_HEAD,0,writeByte,0,CMD.SERIAL_HEAD.length);
        copylen += CMD.SERIAL_HEAD.length;
        System.arraycopy(CMD.SERIAL_RESERVE,0,writeByte,copylen,CMD.SERIAL_RESERVE.length);
        copylen += CMD.SERIAL_RESERVE.length;
        System.arraycopy(CMD.SERIAL_COMMAND_BASE,0,writeByte,copylen,CMD.SERIAL_COMMAND_BASE.length);
        copylen += CMD.SERIAL_COMMAND_BASE.length;
        System.arraycopy(serialCmd,0,writeByte,copylen,serialCmd.length);
        copylen += serialCmd.length;
        byte[] splitData = AssitTool.splitBuffer(data, SPLIT);
        System.arraycopy(splitData,0,writeByte,copylen,splitData.length);
        copylen += splitData.length;
        System.arraycopy(CMD.SERIAL_TAIL,0,writeByte,copylen,CMD.SERIAL_TAIL.length);
        return writeByte;
    }



    public byte[] make_SD_Package(byte[] data) {
        final int SPLIT = 0x30;
        //           头2个字节  + 总数据长度2个字节*2 +数据长度*2 + CRC一个字节*2 + 尾2个字节
        int length = CMD.SD_CMD_HEAD.length + 4 + data.length*2 + 2 + CMD.SD_CMD_TAIL.length ;
        int copylen = 0;
        byte[] writeByte = new byte[length];

        //head
        System.arraycopy(CMD.SD_CMD_HEAD,0,writeByte,0,CMD.SD_CMD_HEAD.length);
        copylen += CMD.SD_CMD_HEAD.length;


        //total length
        int totalLength = data.length;
        byte[] totalLengthArray =AssitTool.splitBuffer(AssitTool.integerToArray(totalLength),SPLIT);
        System.arraycopy(totalLengthArray,0,writeByte,copylen,4);
        copylen += 4 ;

        //data
        byte[] splitData = AssitTool.splitBuffer(data, SPLIT);
        System.arraycopy(splitData,0,writeByte,copylen,splitData.length);
        copylen += splitData.length;


        //crc
        byte[] crcArray = AssitTool.splitBuffer(new byte[] {AssitTool.getBytesCrc(data)},SPLIT);
        System.arraycopy(crcArray,0,writeByte,copylen,2);
        copylen += 2 ;

        //tail
        System.arraycopy(CMD.SD_CMD_TAIL,0,writeByte,copylen,CMD.SD_CMD_TAIL.length);

        return writeByte;
    }
    public byte[] make_kb_Package(byte[] data) {
        final int SPLIT = 0x30;
        //           头2个字节  + 总数据长度2个字节*2 +数据长度*2 + CRC一个字节*2 + 尾2个字节
        int length = CMD.KB_CMD_HEAD.length  +4+ data.length*2 + 2 + CMD.SD_CMD_TAIL.length ;
        int copylen = 0;
        byte[] writeByte = new byte[length];

        //head
        System.arraycopy(CMD.KB_CMD_HEAD,0,writeByte,0,CMD.KB_CMD_HEAD.length);
        copylen += CMD.KB_CMD_HEAD.length;


        //total length
        int totalLength = data.length;
        byte[] totalLengthArray =AssitTool.splitBuffer(AssitTool.integerToArray(totalLength),SPLIT);
        System.arraycopy(totalLengthArray,0,writeByte,copylen,4);
        copylen += 4 ;

        //data
        byte[] splitData = AssitTool.splitBuffer(data, SPLIT);
        System.arraycopy(splitData,0,writeByte,copylen,splitData.length);
        copylen += splitData.length;


        //crc
        byte[] crcArray = AssitTool.splitBuffer(new byte[] {AssitTool.getBytesCrc(data)},SPLIT);
        System.arraycopy(crcArray,0,writeByte,copylen,2);
        copylen += 2 ;

        //tail
        System.arraycopy(CMD.SD_CMD_TAIL,0,writeByte,copylen,CMD.SD_CMD_TAIL.length);

        return writeByte;
    }
    public byte[] sendSerialCmd(byte[] serialCmd, byte[] pwData)
    {
        return makePackage(serialCmd,pwData);
    }
    
    public byte[] sendSingleSerialCmd(byte[] serialCmd)
    {
        return makePackage(serialCmd);
    }
    
    public byte[] sendGenerateSM2Key()
    {
        return makePackage();
    }
    @Override
    public byte[] extract() {
        return new byte[0];
    }
}
