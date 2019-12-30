package com.example.manage.utils;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * @email 2434017367@qq.com
 * @author: gyh
 * @date: 2019/12/15
 * @time: 12:38
 */
public class FileUtlis {


    public static String base64Upload(String path, String fileName, String base64) throws Exception {
        if (StringUtils.isEmpty(path) || StringUtils.isEmpty(fileName) || StringUtils.isEmpty(base64)){
            return base64;
        }

        base64 = base64.replaceAll("\r\n", "");

        String suffix = null;
        // 判断格式是否为base64格式
        if(base64.contains("data:")){
            // 根据base64获取文件后缀
            suffix = "." + base64.substring(base64.indexOf("/") + 1, base64.indexOf(";"));
            base64 = base64.substring(base64.indexOf(",")+1);
        }else {
            return base64;
        }

        //创建文件目录
        File  dir =new File(path);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }

        BASE64Decoder decoder = new BASE64Decoder();
        byte[] bytes =  decoder.decodeBuffer(base64);

        File file = new File(path + fileName + suffix);
        FileOutputStream fos = new FileOutputStream(file);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        bos.write(bytes);

        bos.flush();
        bos.close();
        fos.flush();
        fos.close();

        return fileName + suffix;

    }

    public static void flow(InputStream inputStream, OutputStream outputStream) throws IOException {

        if (inputStream != null && outputStream != null){
            byte[] bytes = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, length);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

        }
    }

    public static void main(String[] args) {
        String s = "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/4gxYSUNDX1BST0ZJTEUAAQEAAAxITGl";

        String substring = s.substring(s.indexOf("/") + 1, s.indexOf(";"));

        System.out.println("substring = " + substring);

        System.out.println("s = " + s);

    }

}
