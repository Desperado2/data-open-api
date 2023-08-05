package com.github.desperado2.data.open.api.datasource.manage.enums;

/**
 * 数据源状态枚举
 *
 * @author tu nan
 * @date 2023/3/9
 **/
public enum DataSourceTypeEnum {
    MYSQL(0,"MySQL","com.github.desperado2.open.data.platform.datasource.manage.factory.MySQLDriver", "mysql.jpg", "jdbc:mysql://localhost:3306/test"),
    DB2(1,"DB2", "com.github.desperado2.open.data.platform.datasource.manage.factory.DB2Driver","db2.jpg", ""),
    ORACLE(2,"ORACLE","com.github.desperado2.open.data.platform.datasource.manage.factory.OracleDriver", "oracle.jpg", "jdbc:oracle:thin:@localhost:1521/test"),
    POSTGRE_SQL(3,"PostgreSQL", "com.github.desperado2.open.data.platform.datasource.manage.factory.PostgreSQLDriver","pg.jpg", "jdbc:postgresql://localhost:5432/postgres"),
    PRESTO(4,"Presto","com.github.desperado2.open.data.platform.datasource.manage.factory.PrestoDriver", "presto.jpg", "jdbc:presto://127.0.0.1:8443/hive"),
    SQLSERVER(5,"SQLServer","com.github.desperado2.open.data.platform.datasource.manage.factory.SQLServerDriver", "sqlServer.jpg", "jdbc:sqlserver://localhost:1433;database=test"),
   // MongoDB(6,"MongoDB","com.github.desperado2.open.data.platform.datasource.manage.factory.MongoDriver", "mongo.jpg", "mongodb://localhost:27017/test"),
   // REDIS(7,"Redis", "com.github.desperado2.open.data.platform.datasource.manage.factory.MongoDriver", "redis.jpg", "jdbc:sqlserver://localhost:1433;database=test"),
    CLICKHOUSE(8,"ClickHouse","com.github.desperado2.open.data.platform.datasource.manage.factory.ClickHouseDriver", "clickhouse.jpg", "jdbc:clickhouse://localhost:8123"),
    STARROCKS(9,"Starrocks","com.github.desperado2.open.data.platform.datasource.manage.factory.StarRocksDriver", "StarRocks.jpg", "jdbc:mysql://localhost:3306/test"),
    DORIS(10,"Doris","com.github.desperado2.open.data.platform.datasource.manage.factory.DorisDriver", "ds_doris.jpg", "jdbc:mysql://localhost:3306/test"),
    ELASTICSEARCH(11,"ElasticSearch","com.github.desperado2.open.data.platform.datasource.manage.factory.ElasticSearchDriver", "es.jpg", "127.0.0.1:9200,127.0.0.2:9200");

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 名称
     */
    private final String name;

    /**
     * 驱动
     */
    private final String driver;

    /**
     * 图标
     */
    private final String icon;

    /**
     * 连接格式
     */
    private final String format;


    DataSourceTypeEnum(Integer code, String name, String driver, String icon, String format) {
        this.code = code;
        this.name = name;
        this.driver = driver;
        this.icon = icon;
        this.format = format;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getFormat() {
        return format;
    }

    public String getDriver() {
        return driver;
    }

    public static DataSourceTypeEnum getByCode(Integer code){
        for (DataSourceTypeEnum dataSourceTypeEnum : DataSourceTypeEnum.values()) {
            if(dataSourceTypeEnum.code.equals(code)){
                return dataSourceTypeEnum;
            }
        }
        return null;
    }
}
