package com.github.desperado2.data.open.api.datasource.manage.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表结构解析
 * @author tu nan
 * @date 2023/3/18
 **/
public class SqlUtils {

    /**
     * 通过表达式获取信息
     * @param sql sql
     * @param pattern 表达式
     * @param group 组
     * @return 数据
     */
    public static String getByPattern(String sql, String pattern, int group) {
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(sql);
        while (matcher.find()) {
            return matcher.group(group);
        }
        return null;
    }

    /**
     * 获取sql中的列
     * @param sql SQL语句
     * @return 列
     */
    public static List<String> getColumnSqls(String sql) {
        List<String> lines = new ArrayList<>();
        Scanner scanner = new Scanner(sql);
        boolean start = false;
        while (scanner.hasNextLine()) {
            String nextLine = scanner.nextLine();
            if (nextLine.indexOf("CREATE TABLE") != -1) {
                start = true;
                continue;
            }
            if (nextLine.indexOf("KEY") != -1 || nextLine.indexOf("ENGINE=") != -1) {
                start = false;
                continue;
            }
            if (start) {
                lines.add(nextLine);
            }
        }
        return lines;
    }
}
