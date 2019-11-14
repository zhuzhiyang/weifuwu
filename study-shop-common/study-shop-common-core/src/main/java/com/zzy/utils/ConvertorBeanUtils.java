package com.zzy.utils;

import org.springframework.beans.BeanUtils;

/**
 * Created by Sheep on 2019/11/11.
 */
public class ConvertorBeanUtils<T> {

    /**
     * dot 转换为Do 工具类
     *
     * @param object
     * @param tClass
     * @return
     */
    public static <T>  T  convert(Object object, Class<T> tClass) {
        // 判断dto是否为空!
        if (object == null) {
            return null;
        }
        // 判断DoClass 是否为空
        if (tClass == null) {
            return null;
        }
        try {
            T newInstance = tClass.newInstance();
            BeanUtils.copyProperties(object, newInstance);
            // Dto转换Do
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

}
