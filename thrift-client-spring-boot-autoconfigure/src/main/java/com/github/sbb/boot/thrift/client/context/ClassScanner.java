package com.github.sbb.boot.thrift.client.context;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

/**
 * class 扫描器
 *
 * @author 张瑀楠 zyndev@gmail.com
 * @version 1.0
 * @since 2017/12/22 11:06
 */
public class ClassScanner {

  // 是否包括内部类
  private boolean excludeInner = true;
  private boolean checkInOrEx = true;
  // 根据类名过滤
  private List<String> classFilters = null;

  public ClassScanner() {
  }

  public ClassScanner(Boolean excludeInner, Boolean checkInOrEx, List<String> classFilters) {
    this.excludeInner = excludeInner;
    this.checkInOrEx = checkInOrEx;
    this.classFilters = classFilters;
  }

  public boolean isExcludeInner() {
    return excludeInner;
  }

  public void setExcludeInner(boolean excludeInner) {
    this.excludeInner = excludeInner;
  }

  public boolean isCheckInOrEx() {
    return checkInOrEx;
  }

  public void setCheckInOrEx(boolean checkInOrEx) {
    this.checkInOrEx = checkInOrEx;
  }

  public List<String> getClassFilters() {
    return classFilters;
  }

  public void setClassFilters(List<String> classFilters) {
    this.classFilters = classFilters;
  }

  /**
   * 获取包中的所有类
   *
   * @param basePackage
   * @param recursive
   * @return
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Set<Class<?>> getPackageAllClasses(String basePackage, boolean recursive)
      throws IOException, ClassNotFoundException {
    // 使用LinkedHashSet来存放扫描到的类
    Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
    String packageName = basePackage;
    // 如果最后一个字符是“.”，则去掉
    if (packageName.endsWith(".")) {
      packageName = packageName.substring(0, packageName.lastIndexOf('.'));
    }
    // 将包名中的“.”换成系统文件夹的“/”
    String package2Path = packageName.replace('.', '/');
    // 使用当前线程来加载文件夹
    Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(package2Path);
    while (dirs.hasMoreElements()) {
      URL url = dirs.nextElement();
      // 获取URL协议
      String protocol = url.getProtocol();
      if ("file".equals(protocol)) {
        String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
        // 扫描文件夹中的包和类
        doScanPackageClassesByFile(classes, packageName, filePath, recursive);
      } else if ("jar".equals(protocol)) {
        // 扫描jar包中的包和类
        doScanPackageClassesByJar(packageName, url, recursive, classes);
      }
    }
    return classes;

  }

  /**
   * 在jar包中扫描包和类
   *
   * @param basePackage 包名
   * @param url         类路径
   * @param recursive   是否递归
   * @param classes     传引用，返回的结果
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private void doScanPackageClassesByJar(String basePackage, URL url, final boolean recursive, Set<Class<?>> classes)
      throws IOException, ClassNotFoundException {
    // 包名
    String packageName = basePackage;
    // 获取文件路径
    String package2Path = packageName.replace('.', '/');
    // 转为jar包
    JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
    // 遍历jar包中的元素
    Enumeration<JarEntry> entries = jar.entries();
    while (entries.hasMoreElements()) {
      JarEntry entry = entries.nextElement();
      String name = entry.getName();
      // 如果路径不一致，或者是目录，则继续
      if (!name.startsWith(package2Path) || entry.isDirectory()) {
        continue;
      }
      // 判断是否递归搜索子包
      if (!recursive && name.lastIndexOf('/') != package2Path.length()) {
        continue;
      }
      // 判断是否过滤 inner class
      if (this.excludeInner && name.indexOf('$') != -1) {
        continue;
      }
      String classSimpleName = name.substring(name.lastIndexOf('/') + 1);
      // 判定是否符合过滤条件
      if (this.filterClassName(classSimpleName)) {
        String className = name.replace('/', '.');
        className = className.substring(0, className.length() - 6);
        // 用当前线程的类加载器加载类
        classes.add(Thread.currentThread().getContextClassLoader().loadClass(className));
      }
    }
  }

  /**
   * 在文件夹中扫描包和类
   *
   * @param classes
   * @param packageName
   * @param packagePath
   * @param recursive
   * @throws ClassNotFoundException
   */
  private void doScanPackageClassesByFile(Set<Class<?>> classes, String packageName, String packagePath,
      boolean recursive) throws ClassNotFoundException {
    // 转为文件
    File dir = new File(packagePath);
    if (!dir.exists() || !dir.isDirectory()) {
      return;
    }
    final boolean fileRecursive = recursive;
    // 列出文件，进行过滤
    File[] dirfiles = dir.listFiles(new FileFilter() {
      // 自定义文件过滤规则
      @Override
      public boolean accept(File file) {
        if (file.isDirectory()) {
          return fileRecursive;
        }
        String filename = file.getName();
        if (excludeInner && filename.indexOf('$') != -1) {
          return false;
        }
        return filterClassName(filename);
      }
    });
    for (File file : dirfiles) {
      if (file.isDirectory()) {
        // 如果是目录，则递归
        doScanPackageClassesByFile(classes, packageName + "." + file.getName(), file.getAbsolutePath(), recursive);
      } else {
        // 用当前类加载器加载
        String className = file.getName().substring(0, file.getName().length() - 6);
        classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));
      }
    }
  }

  /**
   * 过滤类文件
   *
   * @param className
   * @return
   */
  private boolean filterClassName(String className) {
    // 文件后缀为class
    if (!className.endsWith(".class")) {
      return false;
    }
    // 没有类过滤规则
    if (null == this.classFilters || this.classFilters.isEmpty()) {
      return true;
    }
    String tmpName = className.substring(0, className.length() - 6);
    boolean flag = false;
    for (String str : classFilters) {
      String tmpreg = "^" + str.replace("*", ".*") + "$";
      Pattern p = Pattern.compile(tmpreg);
      if (p.matcher(tmpName).find()) {
        flag = true;
        break;
      }
    }
    return (checkInOrEx && flag) || (!checkInOrEx && !flag);
  }
}
