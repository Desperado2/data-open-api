package com.github.desperado2.data.open.api.datasource.manage.utils;


/**
 * Mapper生成
 * @author tu nan
 * @date 2023/3/14
 **/
public class MapperUtils {

    /**
     * XML前缀
     */
    private final static String PREFIX ="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \n" +
            "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
            "<mapper namespace=\"com.github.desperado2.open.data.platform.datasource.manage.mapper.%s\">\n";

    /**
     * XML后缀
     */
    private final static String SUFFIX = "</mapper>";


    /**
     * 生成XML
     * @param sql SQL语句
     * @return XML
     */
    public static String generatorQueryList(String sql, String uuid){
        String queryListSql =  "\t<select id=\"selectList\" parameterType=\"java.util.Map\" resultType=\"java.util.Map\">\n" +
                "\t\t"+ sql +" \n" +
                "\t</select>\n";
        return String.format(PREFIX, uuid) + queryListSql + SUFFIX;
    }

}
