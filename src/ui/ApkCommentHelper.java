
package ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.ZipFile;

public class ApkCommentHelper {

    public static boolean isApkFile(String path) {
        boolean isApkFile = false;
        if (!IoUtils.isEmpty(path) && path.endsWith(".apk")) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                isApkFile = true;
            }

        }

        return isApkFile;

    }

    public static String getFileName(String path) {
        if (isApkFile(path)) {
            return path.substring(path.lastIndexOf(File.separator) + 1, path.length() - 4);
        }
        return null;
    }

    public static boolean copyFile(String oldPath, String newPath) {
        boolean isCopySuccess = false;
        File oldfile = new File(oldPath);
        FileOutputStream fs = null;
        InputStream inStream = null;
        try {
            int bytesum = 0;
            int byteread = 0;
            if (oldfile.exists()) { // 文件存在时
                inStream = new FileInputStream(oldPath); // 读入原文件
                System.out.println(newPath);
                if (!IoUtils.isFileExists(newPath)) {
                    if (IoUtils.createFile(newPath)) {// 如果失败，再试一次
                        IoUtils.createFile(newPath);
                    }
                }
                if (IoUtils.isFileExists(newPath)) {
                    fs = new FileOutputStream(newPath);
                    byte[] buffer = new byte[1444];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        bytesum += byteread; // 字节数 文件大小
                        System.out.println(bytesum);
                        fs.write(buffer, 0, byteread);
                    }
                    isCopySuccess = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(fs);
            IoUtils.close(inStream);
        }

        return isCopySuccess;
    }

    public static boolean writeApk(String path, String comment) {
        File file = new File(path);
        return writeApk(file, comment);
    }

    public static boolean writeApk(File file, String comment) {
        boolean isWriteSuccess = false;
        ZipFile zipFile = null;
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile accessFile = null;
        try {
            zipFile = new ZipFile(file);
            String zipComment = zipFile.getComment();
            if (zipComment == null) {
                byte[] byteComment = comment.getBytes();
                outputStream = new ByteArrayOutputStream();
                outputStream.write(byteComment);
                outputStream.write(short2Stream((short) byteComment.length));
                byte[] data = outputStream.toByteArray();
                accessFile = new RandomAccessFile(file, "rw");
                accessFile.seek(file.length() - 2);
                accessFile.write(short2Stream((short) data.length));
                accessFile.write(data);
                isWriteSuccess = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IoUtils.close(zipFile);
            IoUtils.close(outputStream);
            IoUtils.close(accessFile);
        }
        return isWriteSuccess;
    }

    private static byte[] short2Stream(short data) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putShort(data);
        buffer.flip();
        return buffer.array();
    }
}
