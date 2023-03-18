package info.lostred.ruler.util;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.logging.Logger;

/**
 * 类路径扫描工具
 *
 * @author lostred
 */
public final class ClassPathScanUtils {
    private static final Logger logger = Logger.getLogger(ClassPathScanUtils.class.getName());

    /**
     * 找到包扫描路径下的所有类对象集合
     *
     * @param scanBasePackage 包扫描路径
     * @return 类对象集合
     */
    public static Set<Class<?>> getClasses(String scanBasePackage) {
        Set<Class<?>> classes = new HashSet<>();
        String relativePath = scanBasePackage.replaceAll("\\.", "/");
        try {
            Enumeration<URL> enumeration = Thread.currentThread().getContextClassLoader().getResources(relativePath);
            while (enumeration.hasMoreElements()) {
                URL url = enumeration.nextElement();
                List<File> files = new ArrayList<>();
                if ("file".equals(url.getProtocol())) {
                    collectFiles(new File(url.getFile()), files);
                    for (File file : files) {
                        String absolutePath = file.getAbsolutePath().replaceAll("\\\\", "/");
                        if (absolutePath.lastIndexOf(relativePath) != -1) {
                            String className = getClassName(absolutePath, relativePath);
                            loadClass(className).ifPresent(classes::add);
                        }
                    }
                } else if ("jar".equals(url.getProtocol())) {
                    JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                    Enumeration<JarEntry> entries = urlConnection.getJarFile().entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        if (entry.getName().endsWith(".class")) {
                            String absolutePath = entry.getName().replaceAll("\\\\", "/");
                            if (absolutePath.lastIndexOf(relativePath) != -1) {
                                String className = getClassName(absolutePath, relativePath);
                                loadClass(className).ifPresent(classes::add);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
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
     * @param absolutePath 文件绝对路径
     * @param relativePath 包扫描文件夹相对路径
     * @return 全限定类名
     */
    private static String getClassName(String absolutePath, String relativePath) {
        String path = absolutePath.substring(absolutePath.lastIndexOf(relativePath));
        return path.replace(".class", "").replaceAll("/", ".");
    }

    /**
     * 加载类
     *
     * @param className 全限定类名
     * @return 类对象
     */
    public static Optional<Class<?>> loadClass(String className) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            return Optional.of(classLoader.loadClass(className));
        } catch (ClassNotFoundException ignored) {
            logger.warning("Unable to find class: " + className);
        }
        return Optional.empty();
    }
}
