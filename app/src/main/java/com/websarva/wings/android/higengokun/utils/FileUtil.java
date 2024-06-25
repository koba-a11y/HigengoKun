package com.websarva.wings.android.higengokun.utils;


import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class FileUtil {
    public static boolean readFile(Context context, String fileName, String date) {
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, StandardCharsets.UTF_8));
            String lineBuffer;
            while ((lineBuffer = br.readLine()) != null) {
                System.out.println("LineBuffer:" + lineBuffer);
                if (lineBuffer.equals(date)) {
                    br.close();
                    return true;
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void saveFile(Context context, String fileName, String date) {
        if (!readFile(context, fileName, date)) { // 既に日付が存在するかチェック
            date = date + "\n";
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE | Context.MODE_APPEND);
                fileOutputStream.write(date.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
