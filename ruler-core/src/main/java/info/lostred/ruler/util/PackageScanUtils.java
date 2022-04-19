package info.lostred.ruler.util;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.logging.Logger;

/**
 * 包扫描工具
 *
 * @author lostred
 */
public final class PackageScanUtils {
    private static final Logger logger = Logger.getLogger(PackageScanUtils.class.getName());

    /**
     * 找到包扫描路径下的所有全限定类名
     *
     * @param scanBasePackage 包扫描路径
     * @return 全限定类名集合
     * @throws IOException IO异常
     */
    public static Set<String> findClassNames(String scanBasePackage) throws IOException {
        Set<String> result = new HashSet<>();
        scanBasePackage = scanBasePackage.replaceAll("\\.", "/");
        Enumeration<URL> enumeration = Thread.currentThread().getContextClassLoader().getResources(scanBasePackage);
        while (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            List<File> files = new ArrayList<>();
            if ("file".equals(url.getProtocol())) {
                collectFiles(new File(url.getFile()), files);
                for (File file : files) {
                    String className = getClassName(file.getAbsolutePath(), scanBasePackage);
                    if (className != null) {
                        result.add(className);
                    }
                }
            } else if ("jar".equals(url.getProtocol())) {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                Enumeration<JarEntry> entries = urlConnection.getJarFile().entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = getClassName(entry.getName(), scanBasePackage);
                        if (className != null) {
                            result.add(className);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 收集文件夹路径下的所有文件
     *
     * @param dir  根文件夹路径
     * @param list 文件集合
     */
    private static void collectFiles(File dir, List<File> list) {
        if (!dir.exists()) {
            return;
        }
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    collectFiles(file, list);
                } else {
                    list.add(file);
                }
            }
        } else {
            if (dir.getName().endsWith(".class")) {
                list.add(dir);
            }
        }
    }

    /**
     * 获取全限定类名
     *
     * @param path     文件绝对路径
     * @param scanPath 包扫描文件夹相对路径
     * @return 全限定类名
     */
    private static String getClassName(String path, String scanPath) {
        path = path.replaceAll("\\\\", "/");
        if (path.lastIndexOf(scanPath) != -1) {
            path = path.substring(path.lastIndexOf(scanPath));
            return path.replace(".class", "").replaceAll("/", ".");
        }
        return null;
    }

    /**
     * 加载类
     *
     * @param className 全限定类名
     * @return 类对象
     */
    public static Class<?> loadClass(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException ignored) {
            logger.warning("Unable to find class: " + className);
        }
        return null;
    }
}
