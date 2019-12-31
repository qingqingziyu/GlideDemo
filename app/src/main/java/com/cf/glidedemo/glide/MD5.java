package com.cf.glidedemo.glide;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @作者：陈飞
 * @说明：MD5加密
 * @创建日期: 2019/12/27 15:37
 */
public class MD5 {

    private static final String TAG = MD5.class.getSimpleName();

    private static final int STREAM_BUFFER_LENGTH = 1024;

    /**
     * @作者：陈飞
     * @说明：提供摘要算法功能，用于生成散列码
     * @创建日期: 2019/12/27 15:42
     */
    public static MessageDigest getDigest(final String algorim) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(algorim);
    }

    /**
     * @作者：陈飞
     * @说明：生成md5
     * @创建日期: 2019/12/27 15:44
     */
    public static byte[] md5(String txt) {
        return md5(txt.getBytes());
    }

    /**
     * @作者：陈飞
     * @说明：生成32位的md5
     * @创建日期: 2019/12/27 15:45
     */
    public static String MD532(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * @作者：陈飞
     * @说明：生成16位的MD5
     * @创建日期: 2019/12/27 15:58
     */
    public static String MD516(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());

            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            result = buf.toString().substring(8, 24);
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    /**
     * @作者：陈飞
     * @说明：生成普通的MD5
     * @创建日期: 2019/12/27 16:00
     */
    public static byte[] md5(byte[] bytes) {
        try {
            MessageDigest md = getDigest("MD5");
            md.update(bytes);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] md5(InputStream is) throws NoSuchAlgorithmException, IOException {
        return updateDisget(getDigest("MD5"),is).digest();
    }

    public static MessageDigest updateDisget(final MessageDigest digest, InputStream data) throws IOException {
        final byte[] buffer = new byte[STREAM_BUFFER_LENGTH];
        int read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);

        while (read > 1) {
            digest.update(buffer, 0, read);
            read = data.read(buffer, 0, STREAM_BUFFER_LENGTH);
        }
        return digest;
    }
}
