package com.crashhandler;

public class ExceptionInfo {
	// 时间
	private String timestamp;
	// 产品ID
	private String productId;
	// 设备类型
	private String device;
	// 设备操作系统
	private String os;
	private String reason;
	// 异常名称
	private String name;
	// 用户信息
	private String userinfo;
	// 异常堆栈
	private String callstack;
	// 手机系统版本号
	private String deviceModel;
	// 系统平台版本号
	private String Platform;
	private String versionName;
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(String userinfo) {
		this.userinfo = userinfo;
	}

	public String getCallstack() {
		return callstack;
	}

	public void setCallstack(String callstack) {
		this.callstack = callstack;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getPlatform() {
		return Platform;
	}

	public void setPlatform(String platform) {
		Platform = platform;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
