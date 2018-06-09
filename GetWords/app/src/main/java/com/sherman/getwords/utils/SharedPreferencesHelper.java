package com.sherman.getwords.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * author: 李梦(<a href="mailto:limeng@danlu.com">limeng@danlu.com</a>)<br/>
 * version: $VERSION<br/>
 * since: 2018-05-21 10:33<br/>
 *
 * <p>
 * $DESCRIPTION
 * </p>
 */

public class SharedPreferencesHelper {

    private SharedPreferences prefer;
    private SharedPreferences.Editor editor;


    /**
     * 构造函数
     *
     * @param application 应用上下文
     */
    public SharedPreferencesHelper(Application application) {
        prefer = application.getSharedPreferences(application.getPackageName(), Context.MODE_PRIVATE);
        editor = prefer.edit();
    }

    /**
     * 存储 String 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 存储 boolean 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 存储 float 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 存储 int 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 存储 long 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 存储 对象
     *
     * @param key   键
     * @param value 值
     */
    public void save(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.apply();
    }

    /**
     * 获取 String 对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public String getString(String key, String defaultValue) {
        return prefer.getString(key, defaultValue);
    }

    /**
     * 获取 int 对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public int getInt(String key, int defaultValue) {
        return prefer.getInt(key, defaultValue);
    }

    /**
     * 获取 long 对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public long getLong(String key, long defaultValue) {
        return prefer.getLong(key, defaultValue);
    }

    /**
     * 获取 float 对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public float getFloat(String key, float defaultValue) {
        return prefer.getFloat(key, defaultValue);
    }

    /**
     * 获取 boolean 对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        return prefer.getBoolean(key, defaultValue);
    }

    /**
     * 获取  对象
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 与键值对应的数据
     */
    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return prefer.getStringSet(key, defaultValue);
    }

    /**
     * 删除与指定键值对应的数据
     *
     * @param key 键
     */
    public void remove(String key) {
        editor.remove(key);
        editor.apply();
    }
    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public <T> void saveList(String tag, List<T> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;

        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(tag, strJson);
        editor.commit();

    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public <T> List<T> getDataList(String tag) {
        List<T> datalist=new ArrayList<T>();
        String strJson = prefer.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return datalist;

    }


}