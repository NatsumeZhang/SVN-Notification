package com.svnnotification.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.svnnotification.bean.Activator;
import com.svnnotification.bean.SVN;


/**
 * main handler
 * 
 * @author Natsume
 * @date 2018-01-05
 * @email cug_zg@163.com
 */
public class MainHandler extends AbstractHandler {
	static Text url;
	static Label urlLabel;
	static Text userName;
	static Label userNameLabel;
	static Text pwd;
	static Label pwdLabel;
	static Combo combo;
	static Label comboLabel;
	static Label checkLabel; 
	static Group checkGroup;
	static Button onButton;
	static Button offButton;
	static Button configButton;
	static MessageBox messageBox;
	static int widht = 340;
	static int heght = 210;

	/*
	 * get main window and show input and comfirm
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// main window center in parent shell
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		//full screen
		int x = Display.getCurrent().getClientArea().width;
		int y = Display.getCurrent().getClientArea().height;
		shell.setSize(widht, heght);
		shell.setLocation((x-widht)/2, (y-heght)/2);
		shell.setText("Config SVN");
		shell.open();
		// inputs
		SVN svn =new Activator().getPreference();
		
		createPanel(shell, svn);

		shell.layout();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return null;
	}
	
	/**
	 * create panel
	 * @param shell
	 * @param svn
	 */
	public void createPanel(Shell shell, SVN svn) {
		urlLabel = new Label(shell, SWT.NONE);
		urlLabel.setBounds(5, 8, 30, 20);
		urlLabel.setText("URL");
		url = new Text(shell, SWT.BORDER);
		url.setBounds(40, 8, widht-70, 20);
		url.setText(svn.getUrl()==null?"":svn.getUrl());

		userNameLabel = new Label(shell, SWT.NONE);
		userNameLabel.setBounds(5, 38, 30, 20);
		userNameLabel.setText("User");
		userName = new Text(shell, SWT.BORDER);
		userName.setBounds(40, 38, widht-70, 20);
		userName.setText(svn.getUserName()==null?"":svn.getUserName());

		pwdLabel = new Label(shell, SWT.NONE);
		pwdLabel.setBounds(5, 68, 30, 20);
		pwdLabel.setText("Pwd");
		pwd = new Text(shell, SWT.PASSWORD);
		pwd.setBounds(40, 68, widht-70, 20);
		pwd.setText(svn.getPwd()==null?"":svn.getPwd());
		
		//combo
		comboLabel = new Label(shell, SWT.NONE);
		comboLabel.setBounds(5, 98, 30, 20);
		comboLabel.setText("Freq");
		combo = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setItems(new String[] {"10s","30s","60s","300s"});
		combo.setBounds(40, 98, 60, 20);
		combo.setText(svn.getFrequency()==null?"10s":svn.getFrequency());
	
		//switch on or off
		checkLabel = new Label(shell, SWT.NONE);
		checkLabel.setBounds(120, 98, 40, 20);
		checkLabel.setText("Switch");
		checkGroup = new Group(shell, SWT.NONE);
		checkGroup.setBounds(160, 88, 150, 35);
		onButton = new Button(checkGroup, SWT.RADIO);
		onButton.setText("On");
		onButton.setBounds(10, 10, 40, 20);
		
		offButton = new Button(checkGroup, SWT.RADIO);
		offButton.setText("Off");
		offButton.setBounds(70, 10, 40, 20);
		//defalut 
		if(svn.getOn_off().equals("1")) {
			onButton.setSelection(true);
		}else {
			offButton.setSelection(true);
		}
		
		// button
		configButton = new Button(shell, SWT.NONE);
		configButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				SVNHandler svnHandler = new SVNHandler();
				// initial style
				messageBox = new MessageBox(shell, SWT.ICON_WARNING);
				if (url.getText() == null || url.getText() == "") {
					messageBox.setMessage("Please input right SVN url!");
				} else if (userName.getText() == null || userName.getText() == "") {
					messageBox.setMessage("Please input username!");
				} else if (pwd.getText() == null || pwd.getText() == "") {
					messageBox.setMessage("Please input password!");
				} else if (combo.getText() == null || combo.getText() == "") {
					messageBox.setMessage("Please choose frequency!");
				} else if(svnHandler.createSVNRepository(url.getText(), userName.getText(), pwd.getText()) == -1) {
					messageBox.setMessage("Inputs not correct, please check!");
				} else {
					messageBox = new MessageBox(shell, SWT.OK);
					messageBox.setMessage("Success!");
					svn.setUrl(url.getText());
					svn.setUserName(userName.getText());
					svn.setPwd(pwd.getText());
					svn.setFrequency(combo.getText());
					svn.setOn_off(onButton.getSelection()?"1":"0");
					new Activator().setPreference(svn);
					svnHandler.close();// clean verify operation
				}
				messageBox.open();
				if ("Success!".equals(messageBox.getMessage())) {
					shell.close();// success close
					svnHandler.run();
				}else {
					shell.update();// fail update
				}
			}
		});
		configButton.setText("Comfirm");
		configButton.setBounds(30, 128, widht-70, 30);
	}
}
