package com.svnnotification.bean;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
/**
 * get and save preferences 
 * @author Natsume
 * @date 2017-01-17
 * @email cug_zg@163.com
 */
public class Activator extends AbstractUIPlugin {   
    private static IPreferenceStore store;
    
    /**
     * get stored preference as SVN object
     * @return
     */
    public SVN getPreference(){
        store =getPreferenceStore();
        SVN svn = new SVN();
        svn.setUrl(store.getString("url")==null||store.getString("url").equals("")?"http://localhost/svn/sample":store.getString("url"));
		svn.setUserName(store.getString("userName")==null||store.getString("userName").equals("")?"natsume":store.getString("userName"));
		svn.setPwd(store.getString("pwd")==null||store.getString("pwd").equals("")?"123456":store.getString("pwd"));
		svn.setFrequency(store.getString("freq")==null||store.getString("freq").equals("")?"10s":store.getString("freq"));
		svn.setOn_off(store.getString("on_off")==null||store.getString("on_off").equals("")?"0":store.getString("on_off"));
		svn.setRevision(store.getString("revision")==null||store.getString("revision").equals("")?"0":store.getString("revision"));
		return svn;
    }
    
    /**
     * save preference
     * @param svn
     */
    @SuppressWarnings("deprecation")
	public void setPreference(SVN svn) {
    	store = getPreferenceStore();
    	store.setValue("url", svn.getUrl());
    	store.setValue("userName", svn.getUserName());
    	store.setValue("pwd", svn.getPwd());
    	store.setValue("freq", svn.getFrequency());
    	store.setValue("on_off", svn.getOn_off());
    	savePreferenceStore();
    }
    
    /**
     * save latest revision 
     * @param revision
     */
    @SuppressWarnings("deprecation")
    public void setRevision(String revision) { 	
    	store = getPreferenceStore();
    	store.setValue("revision", revision);
    	savePreferenceStore();
    }
}
