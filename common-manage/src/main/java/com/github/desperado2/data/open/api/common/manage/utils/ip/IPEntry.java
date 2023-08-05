package com.github.desperado2.data.open.api.common.manage.utils.ip;


/**
 * IPLocation
 * @author tu nan
 * @date 2021/3/11
 **/
public class IPEntry {
	public String beginIp;
    public String endIp;
    public String country;
    public String area;
    
    /**
     * 构造函数
     */
    public IPEntry() {
        beginIp = endIp = country = area = "";
    }

}
