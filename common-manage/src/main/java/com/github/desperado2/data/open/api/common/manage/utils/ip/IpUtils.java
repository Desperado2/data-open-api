package com.github.desperado2.data.open.api.common.manage.utils.ip;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Ip工具类
 * @author liujianqiang
 * @date 2019年1月4日
 */
public class IpUtils {
	
	private static Logger logger = LoggerFactory.getLogger(IpUtils.class);

	
	/**
	 * 获取客户端ip
	 * @author liujianqiang
	 * @data 2019年1月4日
	 * @param request
	 * @return
	 */
	public static String getClientIpAddr(HttpServletRequest request) {
		String xip = request.getHeader("X-Real-IP");
		String xFor = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)){
			logger.info("ip==========X-Forwarded-For,XFor= " + xFor);
		   //多次反向代理后会有多个ip值，第一个ip才是真实ip
		   int index = xFor.indexOf(",");
		   if(index != -1){
			   return xFor.substring(0,index);
		   }else{
		       return xFor;
		   }
		}
		xFor = xip;
		if(StringUtils.isNotEmpty(xFor) && !"unKnown".equalsIgnoreCase(xFor)){
		   logger.info("ip==========X-Real-IP,XFor= " + xFor);
		   return xFor;
		}
		if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
			xFor = request.getHeader("Proxy-Client-IP");
		   logger.info("ip==========Proxy-Client-IP,XFor= " + xFor);
		}
		if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
			xFor = request.getHeader("WL-Proxy-Client-IP");
		   logger.info("ip==========WL-Proxy-Client-IP,XFor= " + xFor);
		}
		if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
			xFor = request.getHeader("HTTP_CLIENT_IP");
		   logger.info("ip==========HTTP_CLIENT_IP,XFor= " + xFor);
		}
		if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
			xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
		   logger.info("ip==========HTTP_X_FORWARDED_FOR,XFor= " + xFor);
		}
		if (StringUtils.isBlank(xFor) || "unknown".equalsIgnoreCase(xFor)) {
			xFor = request.getRemoteAddr();
		   logger.info("ip==========RemoteAddr,XFor= " + xFor);
		}
		return xFor;
	}
		
	
}
