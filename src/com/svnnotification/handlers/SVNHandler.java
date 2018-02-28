package com.svnnotification.handlers;

import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNRevisionProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.svnnotification.bean.Activator;
import com.svnnotification.bean.SVN;
import com.svnnotification.utils.CalendarUtil;

/**
 * Fetch SVN message and show in console
 * 
 * @author Natsume
 * @date 2018-01-05
 * @email cug_zg@163.com
 */
public class SVNHandler extends TimerTask {
	protected SVNRepository repository = null;
	protected ISVNAuthenticationManager authManager;
	protected SVNClientManager clientManager;
	private static String revision;
	private static String author;
	private static String date;
	private static String commitMessage;
	private static int freq = 10000; // default 10s
	private static int widht = 340;
	private static int heght = 190;
	private static Label msgLabel;
	private static Button configButton;
	private static Thread thread;
	/**
	 * multi-threading prevent from suspend
	 */
	Runnable runnable = new Runnable() {
		public void run() {
			while (true) {
				try {
					doGet();
					Thread.sleep(freq);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	public void run() {
		thread = new Thread(runnable);
		thread.start();
	}

	/**
	 * get latest commit info from SVN Repo
	 * 
	 * @throws Exception
	 */
	public void doGet() throws Exception {
		SVN svn = new Activator().getPreference();
		String on_off = svn.getOn_off();
		if ("1".equals(on_off)) {
			String url = svn.getUrl();
			String userName = svn.getUserName();
			String pwd = svn.getPwd();
			String revison = svn.getRevision();
			if (svn.getFrequency() != null && !svn.getFrequency().equals("")) {
				freq = Integer.parseInt(svn.getFrequency().replaceAll("[a-zA-Z]", "")) * 1000;
			}
			// create repo
			if (url != null && userName != null && pwd != null) {
				createSVNRepository(url, userName, pwd);
				if (repository != null) {
					long revision_ = getLatestRevision();
					if (!String.valueOf(revision_).equals(revison)) {
						author = repository.getRevisionPropertyValue(revision_, SVNRevisionProperty.AUTHOR).toString();
						commitMessage = repository.getRevisionPropertyValue(revision_, SVNRevisionProperty.LOG)
								.toString();
						date = CalendarUtil.UTCToCST(
								repository.getRevisionPropertyValue(revision_, SVNRevisionProperty.DATE).toString(),
								"yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
						revision = String.valueOf(revision_);
						String alertMsg = "revision:" + revision + "\nauthor:" + author + "\ncommitMessage:"
								+ commitMessage + "\ndate:" + date;
						// record the latest revision to decide whether it's necessary to flush
						new Activator().setRevision(revision);
						alert(alertMsg);
					}
				}
				close();
			}
		}
	}

	/**
	 * create SVN Repo
	 * 
	 * @param url
	 * @param userName
	 * @param pwd
	 */
	public int createSVNRepository(String url, String userName, String pwd) {
		int ret = 0;
		try {
			if (repository != null)
				System.out.println("Repositroy is already exists, please clean it!");
			try {
				// create connect
				SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
				// authenticate
				this.authManager = SVNWCUtil.createDefaultAuthenticationManager(userName, pwd.toCharArray());
				// create auth manager
				repository.setAuthenticationManager(authManager);
				this.repository = repository;
				getLatestRevision();
			} catch (Exception e) {
				return -1;//
			}
			ret = 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * close repo
	 */
	public void close() {
		this.repository.closeSession();
		this.repository = null;
	}

	/**
	 * get latest revision
	 * 
	 * @return
	 * @throws SVNException
	 */
	@SuppressWarnings("unchecked")
	public long getLatestRevision() throws SVNException {
		Collection<SVNDirEntry> entries = repository.getDir("", -1, null, (Collection<SVNDirEntry>) null);
		Iterator<SVNDirEntry> it = entries.iterator();
		long max = 0;
		while (it.hasNext()) {
			SVNDirEntry entry = it.next();
			if (entry.getRevision() > max) {
				max = entry.getRevision();
			}
		}
		return max;
	}

	/**
	 * alert message
	 * 
	 * @param svn
	 * @throws InterruptedException 
	 */
	public void alert(String msg) throws InterruptedException {
		Display display = new Display();
		Shell shell = new Shell(display);
		// full screen
		int x = Display.getCurrent().getClientArea().width;
		int y = Display.getCurrent().getClientArea().height;
		shell.setSize(widht, heght);
		shell.setLocation((x - widht) / 2, (y - heght) / 2);
		shell.setText("New Message");
		shell.open();
		
		msgLabel = new Label(shell, SWT.NONE);
		msgLabel.setBounds(5, 8, 230, 80);
		msgLabel.setText(msg);

		// button
		configButton = new Button(shell, SWT.NONE);
		configButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				shell.close();// success close
				display.close();
			}
		});
		configButton.setText("Comfirm");
		configButton.setBounds(30, 98, widht - 70, 30);
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
}
