package com.github.desperado2.data.open.api.common.manage.utils.ip;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * Ip地址解析类
 * @author tu nan
 * @date 2021/3/11
 **/
@Component
public class SplitAddress {

	public static File  FILE;
	public static IPSeeker IPSEEKER;
	private static final Integer ONE_LEVEL_ADDRESS = 1;
	private static final Integer TWO_LEVEL_ADDRESS = 2;
	private static final Integer THREE_LEVEL_ADDRESS = 3;


	/**
	 * 根据IP地址获取地区名称
	 * @param ipAddress ip
	 */
	public static IPEntity getPlaceFromIp(String ipAddress) throws Exception{
		if(IPSEEKER == null){
			if (FILE == null || !FILE.exists()) {
				setIpFile();
			}else{
				IPSEEKER = new IPSeeker(FILE);
			}
		}
		IPEntity ipentity = new IPEntity();
		String allAddress = IPSEEKER.getCountry(ipAddress);
		
		String[] part;
		//最多切成3段:辽宁省,盘锦市,双台子区;
		allAddress = allAddress.replaceAll("省", "省,").replaceAll("市", "市,").replaceAll("县", "县");
		part = allAddress.split(",");
		if(ONE_LEVEL_ADDRESS.equals(part.length)){
			//只有1级地址
			ipentity.setNation(part[0]);
			ipentity.setProvince(part[0]);
		}
		else if(TWO_LEVEL_ADDRESS.equals(part.length)){
			//有2级地址
			ipentity.setProvince(part[0]);
			ipentity.setCity(part[1]);
		}
		else if(THREE_LEVEL_ADDRESS.equals(part.length)){
			//有3级地址
			ipentity.setProvince(part[0]);
			ipentity.setCity(part[1]);
			ipentity.setRegion(part[2]);
		}
		return ipentity;
	}


	public static void setIpFile() throws Exception {
		ClassPathResource classPathResource = new ClassPathResource("qqwry.dat");
		InputStream inputStream = classPathResource.getInputStream();
		File somethingFile = new File("qqwry.dat");
		FileChannel fileChannel = new RandomAccessFile(somethingFile, "rw").getChannel();
		byte[] datas = FileCopyUtils.copyToByteArray(inputStream);
		ByteBuffer b = ByteBuffer.allocate(datas.length);
		b.put(datas);
		b.flip();
		fileChannel.write(b);
		FILE = new File("qqwry.dat");
		IPSEEKER = new IPSeeker(FILE);
	}


	public static String getIpPlaceByIpEntity(IPEntity entity) {
		String ipPlace = "";
		if (entity != null) {
			if (entity.getProvince() != null) {
				ipPlace = ipPlace + entity.getProvince();
			}
			if (entity.getCity() != null) {
				ipPlace = ipPlace + entity.getCity();
			}
			if (entity.getRegion() != null) {
				ipPlace = ipPlace + entity.getRegion();
			}
		}
		return ipPlace;
	}

}
