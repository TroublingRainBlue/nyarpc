package io.nya.rpc.common.scanner;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 通用类扫描器
 */
public class ClassScanner {
    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";

    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";

    /**
     * class文件后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 扫描工程目录下的.class文件
     * @param packageName 包名
     * @param packagePath   包的绝对路径
     * @param recursive 是否递归
     * @param classNameList 类名集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
                                                         final boolean recursive, List<String> classNameList) {
        // 获取包目录
        File dir = new File(packagePath);

        //如果不存在或者不是目录直接返回
        if(!dir.exists() || !dir.isDirectory()) {
            return;
        }

        // 如果存在，扫描包下所有文件
        // 自定义过滤规则
        File[] dirfiles = dir.listFiles(file -> (recursive && file.isDirectory() || file.getName().endsWith(".class")));

        // 遍历所有文件
        if (dirfiles != null) {
            for(File file : dirfiles) {
                if(file.isDirectory()) {
                    findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(),
                            recursive, classNameList);
                } else {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    classNameList.add(packageName + "." + className);
                }
            }
        }
    }

    /**
     * 扫描jar包
     * @param packageName 扫描的包名
     * @param classNameList 类名集合
     * @param recursive 是否递归
     * @param packageDirName 当前包名目录名
     * @param url 包的url地址
     * @return 处理后的包名
     */
    private static String findAndAddClassesInPackageByJar(String packageName, List<String> classNameList, boolean recursive, String packageDirName, URL url) throws IOException {
        // 如果是jar包文件
        // 定义一个JarFile
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();

        Enumeration<JarEntry> entries = jar.entries();

        // 遍历
        while(entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            // 如果是以/开头
            if(name.charAt(0) == '/') {
                name = name.substring(1);
            }

            // 如果前半部分与定义的包名相同
            if(name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                // 如果是以/结尾的包，把/替换成.
                if(idx != -1) {
                    packageName = name.substring(0,idx).replace('/','.');
                }
                if(idx != -1 || recursive) {
                    if(name.endsWith(CLASS_FILE_SUFFIX) && !entry.isDirectory()) {
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        classNameList.add(packageName + "." + className);
                    }
                }
            }
        }
        return packageName;
    }

    public static List<String> getClassNameList(String packageName) throws IOException {
        List<String> classNameList = new ArrayList<>();

        boolean recursive = true;

        // 获取包名
        String packageDirName = packageName.replace('.', '/');
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);

        while(dirs.hasMoreElements()) {
            URL url = dirs.nextElement();
            // 得到协议名称
            String protocol = url.getProtocol();
            // 如果以文件形式保存在服务器上
            if(PROTOCOL_FILE.equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                // 以文件形式扫描包，将类名加入集合
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classNameList);
            } else if(PROTOCOL_JAR.equals(protocol)) {
                packageName = findAndAddClassesInPackageByJar(packageName, classNameList, recursive, packageDirName, url);
            }
        }
        return classNameList;
    }

}
