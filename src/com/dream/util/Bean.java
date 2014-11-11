package com.dream.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bean extends HashMap<Object, Object> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 构造体方法
     */
    public Bean() {
        super();
    }
    
    /**
     * 构造体方法，初始化数据存储
     * @param id    主键
     */
    public Bean(String id) {
        super();
    }

    /**
     * 构造体方法，直接初始化Bean
     * @param values    带数据信息
     */
    public Bean(HashMap<Object, Object> values) {
        super(values);
    }

    /**
     * 设置对象，支持级联设置
     * @param key   键值
     * @param obj   对象数据
     * @return this，当前Bean
     */
    public Bean set(Object key, Object obj) {
        put(key, obj);
        return this;
    }

    /**
     * 获取对象值，如果不存在则返回缺省对象
     * 
     * @param key 键值
     * @param def 缺省对象
     * @return 对象值
     */
    public final Object get(Object key, Object def) {
        if (containsKey(key)) {
            return get(key);
        } else {
            return def;
        }
    }

    /**
     * 获取对象值的字符串，如果不存在则返回缺省字符串
     * 
     * @param key 键值
     * @param def 缺省字符串
     * @return 对象值
     */
    public final String get(Object key, String def) {
    	Object value = get(key);
    	
    	if (null != value) {
    		return String.valueOf(value);
    	}
    	
        return def;
    }

    /**
     * 获取对象值的整数，如果不存在则返回缺省
     * 
     * @param key 键值
     * @param def 缺省值
     * @return 对象值
     */
    public final int get(Object key, int def) {
    	Object value = get(key);
    	
    	if (null != value) {
    		return Integer.parseInt(value.toString());
    	}
    	
        return def;
    }


    /**
     * 获取对象值的布尔，如果不存在则返回缺省
     * 
     * @param key 键值
     * @param def 缺省值
     * @return 对象值
     */
    public final boolean get(Object key, boolean def) {
    	Object obj = get(key);
    	if (obj != null) {
            if (obj instanceof Boolean) {
                return ((Boolean) obj).booleanValue();
            } else if (obj instanceof Integer) {
                return ((Integer) obj).intValue() == 1;
            } else if (obj instanceof Long) {
                return ((Long) obj).longValue() == 1;
            } else if (obj instanceof Double) {
                return ((Double) obj).doubleValue() == 1;
            } else if (obj instanceof Date) {
                return ((Date) obj).getTime() == 1;
            } else {
                String str = obj.toString().toUpperCase();
                return str.equalsIgnoreCase("TRUE") || str.equalsIgnoreCase("YES") || str.equals("1");
            }
        }
    	
        return def;
    }

    /**
     * 获取对象值(list)，如果不存在则返回缺省
     * 
     * @param <T> List实际存储的对象类型
     * @param key 键值
     * @param def 缺省值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <T> List<T> get(Object key, List<T> def) {
        if (contains(key)) {
            return (List<T>) get(key);
        } else {
            return def;
        }
    }
    
    /**
     * 获取对象值(Map)，如果不存在则返回缺省
     * 
     * @param <M> MAP键
     * @param <N> MAP值
     * @param key 键值
     * @param def 缺省值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <M, N> Map<M, N> get(Object key, Map<M, N> def) {
        if (contains(key)) {
            return (Map<M, N>) get(key);
        } else {
            return def;
        }
    }
    
    /**
     * 获取对象值(LinkedHashMap)，如果不存在则返回缺省
     * 
     * @param <M> LinkedHashMap键
     * @param <N> LinkedHashMap值
     * @param key 键值
     * @param def 缺省值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <M, N> LinkedHashMap<M, N> get(Object key, LinkedHashMap<M, N> def) {
        if (contains(key)) {
            return (LinkedHashMap<M, N>) get(key);
        } else {
            return def;
        }
    }
    
    /**
     * 获取对象值（字符串）
     * 
     * @param key 键值
     * @return 对象值
     */
    public final String getStr(Object key) {
        return get(key, "");
    }

    /**
     * 获取对象值（整数）
     * 
     * @param key 键值
     * @return 对象值
     */
    public final int getInt(Object key) {
        return get(key, 0);
    }



    /**
     * 获取对象值（布尔）
     * 
     * @param key 键值
     * @return 对象值
     */
    public final boolean getBoolean(Object key) {
        return get(key, false);
    }
    
    /**
     * 获取对象值（Bean）
     * @param key 键值
     * @return 对象值
     */
    public final Bean getBean(Object key) {
        if (contains(key)) {
            return (Bean) get(key);
        } else {
            return new Bean();
        }
    }
    
    /**
     * 获取对象值（list）
     * @param <T> list中对象类型
     * @param key 键值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <T> List<T> getList(Object key) {
        if (contains(key)) {
            return (List<T>) get(key);
        } else {
            return new ArrayList<T>();
        }
    }
    
    /**
     * 获取对象值（数组）
     * @param <T>   数组中对象类型
     * @param type  对象的类型，泛型数组初始化必须用到
     * @param key   键值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <T> T[] getArray(Class<T> type, Object key) {
        if (contains(key)) {
            return (T[]) get(key);
        } else {
            return (T[]) Array.newInstance(type, 0);
        }
    }

    /**
     * 获取对象值(Map)，如果不存在则返回空MAP
     * 
     * @param <M> MAP键
     * @param <N> MAP值
     * @param key 键值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <M, N> Map<M, N> getMap(Object key) {
        if (contains(key)) {
            return (Map<M, N>) get(key);
        } else {
            return new HashMap<M, N>();
        }
    }
    
    /**
     * 获取对象值(LinkedHashMap)，如果不存在则返回空LinkedHashMap
     * 
     * @param <M> LinkedHashMap键
     * @param <N> LinkedHashMap值
     * @param key 键值
     * @return 对象值
     */
    @SuppressWarnings("unchecked")
    public final <M, N> LinkedHashMap<M, N> getLinkedMap(Object key) {
        if (contains(key)) {
            return (LinkedHashMap<M, N>) get(key);
        } else {
            return new LinkedHashMap<M, N>();
        }
    }
    
    /**
     * 是否存在指定值
     * @param key 键值
     * @return 存在返回true，不存在返回false
     */
    public final boolean contains(Object key) {
        return containsKey(key);
    }

    /**
     * 是否存在指定值或者为空
     * @param key 键值
     * @return 存在且不为空字符串或数字0返回false，不存在返回或者为空返回true
     */
    public final boolean isEmpty(Object key) {
        boolean bEmpty = true;
        if (contains(key)) {
            Object value = get(key);
            if (value != null) {
                if (value instanceof Number) {
                    bEmpty = (((Number) value).intValue() == 0);
                } else if (value instanceof String) {
                    bEmpty = ((String) value).isEmpty();
                } else {
                    bEmpty = false;
                }
            }
        }
        return bEmpty;
    }

    /**
     * 指定键值是否不为空
     * @param key 键值
     * @return 存在且不为空字符串返回true，不存在返回false
     */
    public final boolean isNotEmpty(Object key) {
        return !isEmpty(key);
    }
    /**
     * 删除指定值
     * 
     * @param key 键值
     * @return 当前对象
     */
    public Bean remove(Object key) {
        if (containsKey(key)) {
            super.remove(key);
        }
        return this;
    }
  
    /**
    * 将在数组中所有的内容传递到另外一个bean
    * @return 复制出来的数据内容，
    */
   public Bean copyOf()  {
       return copyOf(null);
   }
    
    /**
    * 将在数组中设定属性键值的内容传递到另外一个bean
    * @param keys  键值数组 null表示传全部src中的数据
    * @return 复制出来的数据内容，
    */
   public Bean copyOf(Object[] keys)  {
       Bean tar = new Bean(); 
       if (keys != null) {
           for (Object key : keys) {
               tar.set(key, get(key));
           }
       } else {
           tar.putAll(this);
       }
       return tar;
   }
   
   /**
    * @param bean 将指定对象的数据复制到本对象中
    */
   public void copyFrom(Bean bean) {
       copyFrom(bean, null);
   }
   
   /**
    * @param bean 将指定对象的数据复制到本对象中
    * @param keys 指定键值
    */
   public void copyFrom(Bean bean, Object[] keys) {
       if (keys != null) {
           for (Object key : keys) {
               set(key, bean.get(key));
           }
       } else {
           putAll(bean);
       }
       
   }
}

