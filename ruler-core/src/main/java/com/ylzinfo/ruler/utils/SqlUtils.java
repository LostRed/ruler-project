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
    public static String parseSql(String filename, String tableName) {
        try {
            InputStream is = SqlUtils.class.getClassLoader().getResourceAsStream("sql/" + filename + ".sql");
            if (is != null) {
                OutputStream os = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
                return os.toString().replace("'tableName'", tableName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unable to parse this sql.");
    }
}
