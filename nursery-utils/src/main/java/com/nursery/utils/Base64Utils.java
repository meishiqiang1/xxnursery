package com.nursery.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


//import it.sauronsoftware.base64.Base64;

/** */
/**
 * <p>
 * BASE64编码解码工具包
 * </p>
 * <p>
 * 依赖javabase64-1.3.1.jar
 * </p>
 *
 * @author IceWee
 * @date 2012-5-19
 * @version 1.0
 */
public class Base64Utils {

    /** */
    /**
     * 文件读取缓冲区大小
     */
    private static final int CACHE_SIZE = 1024;

    /** */
    /**
     * <p>
     * BASE64字符串解码为二进制数据
     * </p>
     *
     * @param base64
     * @return
     * @throws Exception
     */
    public static byte[] decode(String base64) throws Exception {
        return base64.getBytes(StandardCharsets.UTF_8);
//        return Base64.decode(base64.getBytes());
    }

    /** */
    /**
     * <p>
     * 二进制数据编码为BASE64字符串
     * </p>
     * @param bytes
     * @return
     * @throws Exception
     */
    public static String encode(byte[] bytes) throws Exception {
//        return new String(Base64.encode(bytes));
        return "";
    }

    /**
     * 将文件编码为BASE64字符串
     * 大文件慎用，可能会导致内存溢出
     * @param filePath 文件绝对路径
     * @return
     * @throws Exception
     */
    public static String encodeFile(String filePath) throws Exception {
        byte[] bytes = fileToByte(filePath);
        return encode(bytes);
    }

    /**
     * BASE64字符串转回文件
     * @param filePath 文件绝对路径
     * @param base64 编码字符串
     * @throws Exception
     */
    public static void decodeToFile(String filePath, String base64) throws Exception {
//        byte[] bytes = decode(base64);
        byte[] bytes = base64ToByte(base64);
        byteArrayToFile(bytes, filePath);
    }

    //先将base64转成字节数在
    public static byte[] base64ToByte(String imageBase64) {
        byte[] b = null;
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            if (imageBase64.indexOf("data:image/jpeg;base64,") != -1) {
                b = decoder.decode(imageBase64.replaceAll("data:image/jpeg;base64,", ""));
            } else {
                if (imageBase64.indexOf("data:image/png;base64,") != -1) {
                    b = decoder.decode(imageBase64.replaceAll("data:image/png;base64,", ""));
                } else {
                    b = decoder.decode(imageBase64.replaceAll("data:image/jpg;base64,", ""));
                }
            }
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * {@code 功能描述: 将二进制转换为图片
     *
     * @param b        二进制
     * @param userName 用户名
     * @author hzj
     * @since 2019-7-23 17:53:21
     * @return 图片路径 如果是null 就是保存失败
     */
    public static void filePath(String filePath,byte[] b) {
        try {
            //做转码,否则存的图片名字会乱码
//            resourcesPath = URLDecoder.decode(resourcesPath, "UTF-8");
            //如果文件夹不存在则创建
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            //将二进制在转换为图片
            OutputStream out = new FileOutputStream(filePath);
            out.write(b);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件转换为二进制数组
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static byte[] fileToByte(String filePath) throws Exception {
        byte[] data = new byte[0];
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
            byte[] cache = new byte[CACHE_SIZE];
            int nRead = 0;
            while ((nRead = in.read(cache)) != -1) {
                out.write(cache, 0, nRead);
                out.flush();
            }
            out.close();
            in.close();
            data = out.toByteArray();
        }
        return data;
    }

    /**
     * 二进制数据写文件
     * @param bytes 二进制数据
     * @param filePath 文件生成目录
     */
    public static void byteArrayToFile(byte[] bytes, String filePath) throws Exception {
        InputStream in = new ByteArrayInputStream(bytes);
        File destFile = new File(filePath);
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }
        destFile.createNewFile();
        OutputStream out = new FileOutputStream(destFile);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            out.write(cache, 0, nRead);
            out.flush();
        }
        out.close();
        in.close();
    }

}