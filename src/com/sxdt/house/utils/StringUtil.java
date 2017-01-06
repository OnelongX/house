package com.sxdt.house.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huanglong on 2017/1/4.
 */
public class StringUtil {
    /**
     * 根据键值对填充字符串，如("hello ${name}")
     * 输出：
     *
     * @param content
     * @param name
     * @return String
     */
    public static String renderString(String content, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        return renderString(content, map);
    }

    /**
     * 根据键值对填充字符串，如("hello ${name}",{name:"xiaoming"})
     * 输出：
     *
     * @param content
     * @param map
     * @return String
     */
    public static String renderString(String content, Map<String, String> map) {
        Set<Map.Entry<String, String>> sets = map.entrySet();
        for (Map.Entry<String, String> entry : sets) {
            String regex = "\\$\\{" + entry.getKey() + "\\}";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            content = matcher.replaceAll(entry.getValue());
        }
        return content;
    }
}
