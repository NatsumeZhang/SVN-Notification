package com.svnnotification.handlers;

import com.svnnotification.bean.SVN;
import com.svnnotification.utils.ConfigUtil;

/**
 * Config SVN and write it into local file
 * @author Natsume
 * @date 2018-01-05
 * @e-mail cug_zg@163.com
 *
 */
public class ConfigHandler {
	/**
	 * write svn config
	 * @param svn
	 */
	public static void writeConfig(SVN svn) {
		ConfigUtil.writeProperties("url", svn.getUrl());
		ConfigUtil.writeProperties("userName", svn.getUserName());
		ConfigUtil.writeProperties("pwd", svn.getPwd());
		ConfigUtil.writeProperties("freq", svn.getFrequency());
		ConfigUtil.writeProperties("on_off", svn.getOn_off());
	}
	
	/**
	 * write svn config
	 * @param svn
	 */
	public static void updateConfig(SVN svn) {
		ConfigUtil.updateProperties("url", svn.getUrl());
		ConfigUtil.updateProperties("userName", svn.getUserName());
		ConfigUtil.updateProperties("pwd", svn.getPwd());
		ConfigUtil.updateProperties("freq", svn.getFrequency());
		ConfigUtil.updateProperties("on_off", svn.getOn_off());
	}
	
	/**
	 * get svn config
	 * @return
	 */
	public static SVN getConfig() {
		SVN svn = new SVN();
		svn.setUrl(ConfigUtil.getStringValue("url"));
		svn.setUserName(ConfigUtil.getStringValue("userName"));
		svn.setPwd(ConfigUtil.getStringValue("pwd"));
		svn.setRevision(ConfigUtil.getStringValue("revision"));
		svn.setFrequency(ConfigUtil.getStringValue("freq"));
		svn.setOn_off(ConfigUtil.getStringValue("on_off"));
		return svn;
	}
}
