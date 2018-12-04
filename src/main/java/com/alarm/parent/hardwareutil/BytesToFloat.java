package com.alarm.parent.hardwareutil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BytesToFloat {
    /**
     * @param args
     * @throws IOException 40370A3E
     */
    public static void main(String[] args) throws IOException {
        //float 类型值为123.456 以大端模式存储数据即高字节存于低地址，低字节存于高地址,小端模式反之
        byte bytes[]={(byte)0x40,(byte)0x37,(byte)0x0A,(byte)0x3E};
        //创建一个 ByteArrayInputStream，使用bytes作为其缓冲区数组
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        //再将bais 封装为DataInputStream类型
        DataInputStream dis=new DataInputStream(bais);
        float flt=dis.readFloat();
        dis.close();
      System.out.println(flt);
    }
}