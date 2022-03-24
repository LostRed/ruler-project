package com.ylzinfo.ruler.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * sql工具
 *
 * @author dengluwei
 */
public final class SqlUtils {
    /**
     * 解析sql
     *
     * @param filename    sql文件名
     * @param targetName  替换目标表名
     * @param replaceName 替换后的表名
     * @return sql
     */
    public static String parseSql(String filename, String targetName, String replaceName) {
        try {
            InputStream is = SqlUtils.class.getClassLoader().getResourceAsStream("sql/" + filename + ".sql");
            if (is != null) {
                OutputStream os = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
                return os.toString().replace(targetName, replaceName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unable to parse this sql.");
    }
}
