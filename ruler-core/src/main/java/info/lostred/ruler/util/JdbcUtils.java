package info.lostred.ruler.util;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * jdbc工具
 *
 * @author lostred
 */
public final class JdbcUtils {
    /**
     * 执行sql语句
     *
     * @param dataSource 数据源
     * @param sql        sql语句
     */
    public static void execute(DataSource dataSource, String sql) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 执行sql语句
     *
     * @param dataSource 数据源
     * @param sql        sql语句
     * @param args       参数
     * @return 数据集
     */
    public static ResultSet executeQuery(DataSource dataSource, String sql, Object... args) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询并将结果集包装成集合
     *
     * @param dataSource   数据源
     * @param sql          sql语句
     * @param elementClass 集合元素类型的类对象
     * @param args         参数
     * @param <E>          集合元素类型
     * @return 结果集合
     */
    public static <E> List<E> query(DataSource dataSource, String sql, Class<E> elementClass, Object... args) {
        assert elementClass != null;
        Field[] fields = elementClass.getDeclaredFields();
        try {
            ResultSet resultSet = executeQuery(dataSource, sql, args);
            List<E> list = new ArrayList<>();
            while (resultSet.next()) {
                E e = elementClass.newInstance();
                for (Field field : fields) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    Object value = getValue(resultSet, fieldName);
                    if (value != null) {
                        field.set(e, value);
                    }
                }
                list.add(e);
            }
            return list;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取值
     *
     * @param resultSet 结果集
     * @param fieldName 字段名
     * @return 值
     */
    public static Object getValue(ResultSet resultSet, String fieldName) {
        try {
            return resultSet.getObject(fieldName);
        } catch (SQLException e) {
            String snakeFieldName = camelToSnake(fieldName);
            try {
                return resultSet.getObject(snakeFieldName);
            } catch (SQLException ex) {
                return null;
            }
        }
    }

    /**
     * 解析sql
     *
     * @param filename sql文件名
     * @return sql
     */
    public static String parseSql(String filename) {
        try (InputStream is = JdbcUtils.class.getClassLoader().getResourceAsStream("sql/" + filename + ".sql")) {
            if (is != null) {
                OutputStream os = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
                return os.toString();
            }
            throw new NullPointerException("Unable to find this resource.");
        } catch (IOException e) {
            throw new RuntimeException("Unable to parse this sql.", e);
        }
    }

    /**
     * 解析sql
     *
     * @param filename    sql文件名
     * @param targetName  替换目标表名
     * @param replaceName 替换后的表名
     * @return sql
     */
    public static String parseSql(String filename, String targetName, String replaceName) {
        return parseSql(filename).replace(targetName, replaceName);
    }

    /**
     * 驼峰命名转蛇形命名
     *
     * @param string 驼峰命名
     * @return 蛇形命名
     */
    public static String camelToSnake(String string) {
        Matcher matcher = Pattern.compile("[A-Z]").matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0));
        }
        matcher.appendTail(sb);
        return sb.toString().toLowerCase();
    }

    /**
     * 蛇形命名转驼峰命名
     *
     * @param string 蛇形命名
     * @return 驼峰命名
     */
    public static String snakeToCamel(String string) {
        string = string.toLowerCase();
        Matcher matcher = Pattern.compile("_(\\w)").matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }
}
