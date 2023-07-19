package com.jie.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {


    /**
     * 拷贝bean
     * @param source 源对象
     * @param clazz 目标对象
     * @return
     * @param <V> 泛型
     */
    public static <V> V copyBean(Object source,Class<V> clazz) {

        // 创建目标对象
        V target = null;
        try {
            target = clazz.newInstance();

            // 属性拷贝
            BeanUtils.copyProperties(source,target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return target;
    }

    /**
     *
     * @param List
     * @param clazz
     * @return
     * @param <O> 实体类
     * @param <T> 目标对象
     */
    public static <O,T> List<T> copyBeanList(List<O> List, Class<T> clazz){
        return  List.stream().map(o -> copyBean(o,clazz)).collect(Collectors.toList());
    }

}
