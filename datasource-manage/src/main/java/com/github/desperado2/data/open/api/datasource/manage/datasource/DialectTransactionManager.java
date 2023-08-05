package com.github.desperado2.data.open.api.datasource.manage.datasource;

import org.springframework.transaction.PlatformTransactionManager;

/**
 * 事务管理器
 *
 * @author tu nan
 * @date 2023/3/9
 **/
public interface DialectTransactionManager {

    public PlatformTransactionManager getTransactionManager();
}
