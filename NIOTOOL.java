package com.topsstrace.project.trace;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class NIOTOOL {



    public void writeNIO(String fileName,String fileString,String encode) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(fileName));
            FileChannel channel = fos.getChannel();
            ByteBuffer src = Charset.forName("utf-8").encode(fileString);
            // 字节缓冲的容量和limit会随着数据长度变化，不是固定不变的
            channel.write(src);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Test
    public void test(){

        String FileName = "D:\\ftpservice\\DLRTraceInfo_20181205000516.CSV";
        String writFileName = "D:\\ftpservice\\test.CSV";
//        readNIO(FileName);
        String file = readNIO(FileName,"UTF-8");
        writeNIO(writFileName,file,"utf-8");
//        System.out.println(file);

    }

    public  String readNIO(String FileName,String encoding) {
        FileInputStream fin = null;
        StringBuffer stringBuffer = new StringBuffer();
        String result = "";
        try {
            Charset charset = Charset.forName(encoding);
            CharsetDecoder decoder = charset.newDecoder();
            RandomAccessFile file = new RandomAccessFile(FileName, "rw");
            FileChannel fileChannel = file.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            CharBuffer charBuffer = CharBuffer.allocate(1024);
            int position = -1;
            while (-1 != fileChannel.read(byteBuffer)) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    if (position == byteBuffer.position()){
                        byteBuffer.position(byteBuffer.position() + 1);
                    }
                    decoder.decode(byteBuffer, charBuffer, false);
                    charBuffer.flip();
                    stringBuffer.append(charBuffer.toString());
                    position = byteBuffer.position();
                }
                byteBuffer.clear();
                charBuffer.clear();
            }
            fileChannel.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        result = stringBuffer.toString();
        return result;
    }
}
