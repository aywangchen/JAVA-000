package cn.mioffice.mier.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author wangc
 * @date 2020/10/21 14:25
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        HelloClassLoader helloClassLoader = new HelloClassLoader();
        Class<?> helloClass = helloClassLoader.findClass("Hello");
        Method helloMethod = helloClass.getMethod("hello");
        helloMethod.invoke(helloClass.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String filePath = "/Users/wangchen/Downloads/Hello.xlass";

        byte[] content = null;
        try {
            content = getContent(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (content == null) {
            throw new ClassNotFoundException(name);
        }

        return defineClass(name, content, 0, content.length);
    }

    public byte[] getContent(String filePath) throws IOException {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
                && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset != buffer.length) {
            throw new IOException("Could not completely read file "
                    + file.getName());
        }

        byte[] newBuffer = new byte[buffer.length];
        for (int i = 0; i < buffer.length; i++) {
            newBuffer[i] = (byte) (255 - buffer[i]);
        }

        fi.close();
        return newBuffer;
    }
}
