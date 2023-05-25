/*
 * Copyright (c) 2023-2023 x
 */

package com.danny.xtool;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 对象复制
 *
 * @author x
 * @since 2023-05-25
 */
public class XClone {
    private Class[] clz = new Class[] {String.class, Byte.class, Short.class, Integer.class
            , Long.class, Float.class, Double.class, Boolean.class, Character.class, Void.class
            , Class.class, Object.class};

    private Object createNewObject(Object c) {
        try {
            return c.getClass().newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是简单类，不需要clone
     * @param c 类
     * @return 是否clone
     */
    private Boolean isPrimitive(Class c) {
        if (c.isPrimitive()) {
            return true;
        }
        for (Class c1 : clz) {
            if (c.equals(c1)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 深拷贝
     *
     * @param o
     * @param <T>
     * @return
     */
    public < T extends Object>T deep(Object o) {
        try {
            return (T) clone(o, -1);
        } catch (Exception e) {
            Log.e("", e.toString());
        }
        return null;
    }

    /**
     * 深拷贝
     *
     * @param o
     * @return
     */
    public Object deepClone(Object o) throws IllegalAccessException, InstantiationException {
        return clone(o, -1);
    }

    /**
     * 浅拷贝
     *
     * @param o
     * @return
     */
    public Object clone(Object o) throws IllegalAccessException, InstantiationException {
        return clone(o, 1);
    }

    /**
     *
     * @param value 对象
     * @param level 深度
     * @return 对象
     */
    private Object clone(Object value, int level) throws InstantiationException, IllegalAccessException {
        if (value == null) {
            return null;
        }
        if (level == 0) {
            return value;
        }

        Class c = value.getClass();
        if (isPrimitive(c)) {
            return value;
        }

        level--;
        if (value instanceof Collection) {
            Collection collection = (Collection) c.newInstance();
            for (Object o : (Collection)value) {
                collection.add(clone(o, level));// 深度复制
            }
            value = collection;
        } else if (value instanceof Map) {
            Map tmp = (Map) c.newInstance();
            Map org = (Map) value;
            Iterator<Map.Entry<Object, Object>> iterator = (Iterator<Map.Entry<Object, Object>>) org.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Object, Object> entry = iterator.next();
                Object key = entry.getKey();
                tmp.put(key, clone(entry.getValue(), level));// 深度复制
            }
            value = tmp;
        } else if (c.isArray()) {
            if (c.equals(int[].class)) {
                int[] old = (int[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(short[].class)) {
                short[] old = (short[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(long[].class)) {
                long[] old = (long[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(byte[].class)) {
                byte[] old = (byte[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(char[].class)) {
                char[] old = (char[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(boolean[].class)) {
                boolean[] old = (boolean[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(float[].class)) {
                float[] old = (float[]) value;
                value = Arrays.copyOf(old, old.length);
            } else if (c.equals(double[].class)) {
                double[] old = (double[]) value;
                value = Arrays.copyOf(old, old.length);
            } else {
                Object[] old = (Object[]) value;
                Object[] tmp = Arrays.copyOf(old, old.length, old.getClass());
                for (int i = 0; i < old.length; i++) {
                    tmp[i] = clone(old[i], level);
                }
                value = tmp;
            }
        } else {
            Object tmp = createNewObject(value);
            if (tmp == null) {
                return value;
            }

            Set<Field> fields = new HashSet<>();
            while (c != null && !c.equals(Object.class)) {
                fields.addAll(Arrays.asList(c.getDeclaredFields()));
                c = c.getSuperclass();
            }

            for (final Field field: fields) {
                if (!Modifier.isFinal(field.getModifiers())) {// 非final
                    AccessController.doPrivileged(new PrivilegedAction<Object>() {
                        @Override
                        public Object run() {
                            field.setAccessible(true);
                            return null;
                        }
                    });
                    field.set(tmp, clone(field.get(value), level));// 深度复制
                }
            }
            value = tmp;
        }
        return value;
    }
}
