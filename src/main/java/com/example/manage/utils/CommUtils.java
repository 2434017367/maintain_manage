package com.example.manage.utils;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @email 2434017367@qq.com
 * @author: zhy
 * @date: 2019/12/15
 * @time: 13:02
 */
public class CommUtils {

    public static String getUuid(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
        uuid = uuid.replaceAll("-", "");
        System.out.println(uuid);
        return uuid;
    }

    public static boolean isEmpty(String s){
        return StringUtils.isEmpty(s) || "null".equals(s);
    }

    public static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }

    public static String getVCode(){
        int s = (int)((Math.random()*9+1)*100000);
        return String.valueOf(s);
    }

    public static String getOrderNumber(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddss");
        String format = simpleDateFormat.format(new Date());
        int i = (int)((Math.random()*9+1)*10);
        return format + String.valueOf(i);
    }


    public static String fileUpload(MultipartFile file, String filePath){
        if (file == null || file.isEmpty()) {
            return null;
        }

        String filename = file.getOriginalFilename();
        String suffix = filename.substring(filename.indexOf("."), filename.length());
        String fileName = getUuid() + suffix;

        File path = new File(filePath);
        if(!path.exists()){
            path.mkdirs();
        }

        File dest = new File(filePath + fileName);
        try {
            file.transferTo(dest);
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    public static void main(String[] args) {
        String filename = "1.jsp";
        String suffix = filename.substring(filename.indexOf("."), filename.length());

        System.out.println(suffix);
    }


}
