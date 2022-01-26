package io.github.bw.boot.thrift.client.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Bean reflection util.
 *
 * @author yunan.zhang zyndev@gmail.com
 * @since 2017年12月26日 16点36分
 */
public class BeanReflectionUtil {

  public static Object getPrivatePropertyValue(Object obj, String propertyName) throws Exception {
    Class cls = obj.getClass();
    Field field = cls.getDeclaredField(propertyName);
    field.setAccessible(true);
    return field.get(obj);
  }


  /**
   * Gets static field value.
   *
   * @param className the class name
   * @param fieldName the field name
   * @return the static field value
   * @throws Exception the exception
   */
  public static Object getStaticFieldValue(String className, String fieldName) throws Exception {
    Class cls = Class.forName(className);
    Field field = cls.getField(fieldName);
    return field.get(cls);
  }


  /**
   * Gets field value.
   *
   * @param obj       the obj
   * @param fieldName the field name
   * @return the field value
   * @throws Exception the exception
   */
  public static Object getFieldValue(Object obj, String fieldName) throws Exception {
    Class cls = obj.getClass();
    Field field = cls.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(obj);
  }


  /**
   * Invoke method object.
   *
   * @param owner      the owner
   * @param methodName the method name
   * @param args       the args
   * @return the object
   * @throws Exception the exception
   */
  public Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
    Class cls = owner.getClass();
    Class[] argclass = new Class[args.length];
    for (int i = 0, j = argclass.length; i < j; i++) {
      argclass[i] = args[i].getClass();
    }
    @SuppressWarnings("unchecked")
    Method method = cls.getMethod(methodName, argclass);
    return method.invoke(owner, args);
  }


  /**
   * Invoke static method object.
   *
   * @param className  the class name
   * @param methodName the method name
   * @param args       the args
   * @return the object
   * @throws Exception the exception
   */
  public Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
    Class cls = Class.forName(className);
    Class[] argClass = new Class[args.length];
    for (int i = 0, j = argClass.length; i < j; i++) {
      argClass[i] = args[i].getClass();
    }
    @SuppressWarnings("unchecked")
    Method method = cls.getMethod(methodName, argClass);
    return method.invoke(null, args);
  }


  /**
   * New instance object.
   *
   * @param className the class name
   * @return the object
   * @throws Exception the exception
   */
  public static Object newInstance(String className) throws Exception {
    Class clazz = Class.forName(className);
    @SuppressWarnings("unchecked")
    java.lang.reflect.Constructor cons = clazz.getConstructor();
    return cons.newInstance();
  }

  /**
   * New instance object.
   *
   * @param clazz the clazz
   * @return the object
   * @throws Exception the exception
   */
  public static Object newInstance(Class clazz) throws Exception {
    @SuppressWarnings("unchecked")
    java.lang.reflect.Constructor cons = clazz.getConstructor();
    return cons.newInstance();
  }


  /**
   * Get bean declared fields field [ ].
   *
   * @param className the class name
   * @return the field [ ]
   * @throws ClassNotFoundException the class not found exception
   */
  public static Field[] getBeanDeclaredFields(String className) throws ClassNotFoundException {
    Class clazz = Class.forName(className);
    return clazz.getDeclaredFields();
  }


  /**
   * Get bean declared methods method [ ].
   *
   * @param className the class name
   * @return the method [ ]
   * @throws ClassNotFoundException the class not found exception
   */
  public static Method[] getBeanDeclaredMethods(String className) throws ClassNotFoundException {
    Class clazz = Class.forName(className);
    return clazz.getMethods();
  }


  /**
   * Copy fields.
   *
   * @param source the source
   * @param target the target
   */
  public static void copyFields(Object source, Object target) {
    try {
      List<Field> list = new ArrayList<>();
      Field[] sourceFields = getBeanDeclaredFields(source.getClass().getName());
      Field[] targetFields = getBeanDeclaredFields(target.getClass().getName());
      Map<String, Field> map = new HashMap<>(targetFields.length);
      for (Field field : targetFields) {
        map.put(field.getName(), field);
      }
      for (Field field : sourceFields) {
        if (map.get(field.getName()) != null) {
          list.add(field);
        }
      }
      for (Field field : list) {
        Field tg = map.get(field.getName());
        tg.setAccessible(true);
        tg.set(target, getFieldValue(source, field.getName()));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
