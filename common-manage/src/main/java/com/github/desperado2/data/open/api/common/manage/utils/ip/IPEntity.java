package com.github.desperado2.data.open.api.common.manage.utils.ip;


/**
 * IPEntity
 * @author tu nan
 * @date 2021/3/11
 **/
public class IPEntity {
	/**
	 * 国家:0级地址
	 */
	private String nation;

	/**
	 * 省:0级地址
	 */
	private String province;

	/**
	 * 市:1级地址
	 */
	private String city;

	/**
	 * 区:2级地址
	 */
	private String region;  


	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	@Override
	public String toString() {
		return "IPEntity [nation=" + nation + ", province=" + province + ", city=" + city + ", region=" + region + "]";
	}
	
}
