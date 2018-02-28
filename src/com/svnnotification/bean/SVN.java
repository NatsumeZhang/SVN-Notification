package com.svnnotification.bean;
/**
 * SVN bean
 * @author Natsume
 * @date 2018-01-05
 * @email cug_zg@163.com
 */
public class SVN {
	private String url;      //SVN path
	private String userName; //account
	private String pwd;      //password
	private String revision; //revision
	private String frequency;//frequency
	private String on_off;   // 0 off 1 on

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getOn_off() {
		return on_off;
	}
	public void setOn_off(String on_off) {
		this.on_off = on_off;
	}

}
