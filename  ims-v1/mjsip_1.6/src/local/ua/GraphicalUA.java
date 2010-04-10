/*
 * Copyright (C) 2005 Luca Veltri - University of Parma - Italy
 * 
 * This source code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This source code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this source code; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * Author(s):
 * Luca Veltri (luca.veltri@unipr.it)
 */

package local.ua;

import static java.awt.BorderLayout.CENTER;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ComboBoxEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicComboBoxEditor;

import org.zoolu.net.SocketAddress;
import org.zoolu.sip.address.NameAddress;
import org.zoolu.sip.address.SipURL;
import org.zoolu.sip.provider.SipParser;
import org.zoolu.sip.provider.SipProvider;
import org.zoolu.sip.provider.SipStack;
import org.zoolu.tools.Log;
import org.zoolu.tools.LogLevel;

/** Simple GUI-based SIP user agent (UA). */
public class GraphicalUA extends JFrame implements UserAgentListener, RegisterAgentListener {

	/** This application */
	final String app_name = "DCN SIP Phone";

	/** Event logger. */
	Log log;

	/** User Agent */
	UserAgent ua;

	/** Register Agent */
	RegisterAgent ra;

	/** UserAgentProfile */
	UserAgentProfile user_profile;

	/** Title */
	// String user_name=app_name;
	/** Recent contacts */
	protected static final int NMAX_CONTACTS = 10;

	/** Recent contacts */
	StringList contact_list;

	private static final int W_Width = 320; // window width
	private static final int W_Height = 90; // window height
	private static final int C_Height = 30; // buttons and combobox height
	// (total)

	/** Media file path */
	final String MEDIA_PATH = "mjsip_1.6/media/local/ua/";

	final String CALL_GIF = MEDIA_PATH + "call.gif";
	final String HANGUP_GIF = MEDIA_PATH + "hangup.gif";

	// Icon icon_call;
	// Icon icon_hangup;
	Icon icon_call = new ImageIcon(MEDIA_PATH + "call.gif");
	Icon icon_hangup = new ImageIcon(MEDIA_PATH + "hangup.gif");

	JPanel jPanel1 = new JPanel();
	JPanel jPanel2 = new JPanel();
	JPanel jPanel3 = new JPanel();
	JPanel jPanel4 = new JPanel();
	JComboBox jComboBox1 = new JComboBox();
	BorderLayout borderLayout1 = new BorderLayout();
	BorderLayout borderLayout2 = new BorderLayout();
	JPanel jPanel5 = new JPanel();
	GridLayout gridLayout2 = new GridLayout();
	GridLayout gridLayout3 = new GridLayout();
	JButton CallButton = new JButton();
	JButton HangupButton = new JButton();
	ComboBoxEditor comboBoxEditor1 = new BasicComboBoxEditor();
	BorderLayout borderLayout3 = new BorderLayout();

	JTextField display = new JTextField();

	/** Creates a new GraphicalUA */
	public GraphicalUA(SipProvider sip_provider, UserAgentProfile user_profile) {
		log = sip_provider.getLog();
		this.user_profile = user_profile;

		ua = new UserAgent(sip_provider, user_profile, this);
		ua.listen();
		ra = new RegisterAgent(sip_provider, user_profile.from_url,
				user_profile.contact_url, user_profile.username,
				user_profile.realm, user_profile.passwd, this);

		//String jar_file = UserAgentProfile.ua_jar;

		contact_list = new StringList(UserAgentProfile.contacts_file);
		jComboBox1 = new JComboBox(contact_list.getElements());

		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		run();
	}

	/**
	 * @throws Exception
	 * create layout for the sip program
	 */
	private void jbInit() throws Exception {
		// set frame dimensions
		this.setSize(W_Width, W_Height);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
			frameSize.height = screenSize.height;
		if (frameSize.width > screenSize.width)
			frameSize.width = screenSize.width;
		this.setLocation((screenSize.width - frameSize.width) / 2 - 40,
				(screenSize.height - frameSize.height) / 2 - 40);
		this.setResizable(false);

		this.setTitle(user_profile.contact_url);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				this_windowClosing(e);
			}
		});
		jPanel1.setLayout(borderLayout3);
		jPanel2.setLayout(borderLayout2);
		display.setBackground(Color.black);
		display.setForeground(Color.green);
		display.setEditable(false);
		display.setText(app_name);
		jPanel4.setLayout(borderLayout1);
		jPanel5.setLayout(gridLayout2);
		jPanel3.setLayout(gridLayout3);
		gridLayout3.setRows(2);
		gridLayout3.setColumns(1);
		if (icon_call != null && icon_call.getIconWidth() > 0)
			CallButton.setIcon(icon_call);
		else
			CallButton.setText("Call");
		CallButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CallButton_actionPerformed();
			}
		});
		CallButton.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				CallButton_actionPerformed();
			}
		});
		if (icon_hangup != null && icon_hangup.getIconWidth() > 0)
			HangupButton.setIcon(icon_hangup);
		else
			HangupButton.setText("Hungup");
		HangupButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				HangupButton_actionPerformed();
			}
		});
		HangupButton.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				HangupButton_actionPerformed();
			}
		});
		jComboBox1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jComboBox1_actionPerformed(e);
			}
		});
		comboBoxEditor1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				comboBoxEditor1_actionPerformed(e);
			}
		});
		HangupButton.setFont(new java.awt.Font("Dialog", 0, 10));
		CallButton.setFont(new java.awt.Font("Dialog", 0, 10));
		comboBoxEditor1.getEditorComponent().setBackground(Color.yellow);
		jComboBox1.setEditable(true);
		jComboBox1.setEditor(comboBoxEditor1);
		jComboBox1.setSelectedItem(null);
		jPanel3.setPreferredSize(new Dimension(0, C_Height));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jPanel2, CENTER);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);
		jPanel2.add(display, BorderLayout.CENTER);
		jPanel3.add(jPanel4, null);
		jPanel3.add(jPanel5, null);
		jPanel4.add(jComboBox1, BorderLayout.CENTER);
		jPanel5.add(CallButton, null);
		jPanel5.add(HangupButton, null);

		// show it
		this.setVisible(true);
	}

	/** Starts the UA */
	void run() {
		// Set the re-invite
		if (user_profile.re_invite_time > 0) {
			ua.reInvite(user_profile.contact_url, user_profile.re_invite_time);
		}

		// Set the transfer (REFER)
		if (user_profile.transfer_to != null && user_profile.transfer_time > 0) {
			ua.callTransfer(user_profile.transfer_to,
					user_profile.transfer_time);
		}

		if (user_profile.do_unregister_all)
		// ########## unregisters ALL contact URLs
		{
			ua.printLog("UNREGISTER ALL contact URLs");
			unregisterall();
		}

		if (user_profile.do_unregister)
		// unregisters the contact URL
		{
			ua.printLog("UNREGISTER the contact URL");
			unregister();
		}

		if (user_profile.do_register)
		// ########## registers the contact URL with the registrar server
		{
			ua.printLog("REGISTRATION");
			loopRegister(user_profile.expires, user_profile.expires / 2,
					user_profile.keepalive_time);
		}

		if (user_profile.call_to != null)
		// ########## make a call with the remote URL
		{
			ua.printLog("UAC: CALLING " + user_profile.call_to);
			jComboBox1.setSelectedItem(null);
			comboBoxEditor1.setItem(user_profile.call_to);
			ua.call(user_profile.call_to);
		}

		if (!user_profile.audio && !user_profile.video)
			ua.printLog("ONLY SIGNALING, NO MEDIA");
	}

	void this_windowClosing(WindowEvent e) {
		System.exit(0);
	}

	void CallButton_actionPerformed() {
		if (ua.statusIs(UserAgent.UA_IDLE)) {
			String url = (String) comboBoxEditor1.getItem();
			if (url != null && url.length() > 0) {
				ua.hangup();
				ua.call(url);
				display.setText("CALLING " + url);
			}
		} else if (ua.statusIs(UserAgent.UA_INCOMING_CALL)) {
			ua.accept();
			display.setText("ON CALL");
		}
	}

	void HangupButton_actionPerformed() {
		if (!ua.statusIs(UserAgent.UA_IDLE)) {
			ua.hangup();
			ua.listen();

			display.setText("HANGUP");
		}
	}

	void jComboBox1_actionPerformed(ActionEvent e) { // if the edited URL is
		// different from the
		// selected item, copy
		// the selected item in
		// the editor
		/*
		 * String edit_name=(String)comboBoxEditor1.getItem(); int
		 * index=jComboBox1.getSelectedIndex(); if (index>=0) { String
		 * selected_name=contact_list.elementAt(index); if
		 * (!selected_name.equals(edit_name))
		 * comboBoxEditor1.setItem(selected_name); }
		 */
	}

	void comboBoxEditor1_actionPerformed(ActionEvent e) { // if a new URL has
		// been typed,
		// insert it in the
		// contact_list and
		// make it selected
		// item
		// else, simply make the URL the selected item
		String name = (String) comboBoxEditor1.getItem();
		// parse separatly NameAddrresses or SipURLs
		if (name.indexOf("\"") >= 0 || name.indexOf("<") >= 0) { // try to
			// parse a
			// NameAddrress
			NameAddress nameaddr = (new SipParser(name)).getNameAddress();
			if (nameaddr != null)
				name = nameaddr.toString();
			else
				name = null;
		} else { // try to parse a SipURL
			SipURL url = new SipURL(name);
			if (url != null)
				name = url.toString();
			else
				name = null;
		}

		if (name == null) {
			System.out.println("DEBUG: No sip url recognized in: "
					+ (String) comboBoxEditor1.getItem());
			return;
		}

		// checks if the the URL is already present in the contact_list
		if (!contact_list.contains(name)) {
			jComboBox1.insertItemAt(name, 0);
			jComboBox1.setSelectedIndex(0);
			// limit the list size
			while (contact_list.getElements().size() > NMAX_CONTACTS)
				jComboBox1.removeItemAt(NMAX_CONTACTS);
			// save new contact list
			contact_list.save();
		} else {
			int index = contact_list.indexOf(name);
			jComboBox1.setSelectedIndex(index);
		}

	}

	/** Gets the UserAgent */
	/*
	 * protected UserAgent getUA() { return ua; }
	 */

	/**
	 * Register with the registrar server
	 * 
	 * @param expire_time
	 *            expiration time in seconds
	 */
	public void register(int expire_time) {
		if (ra.isRegistering())
			ra.halt();
		ra.register(expire_time);
	}

	/**
	 * Periodically registers the contact address with the registrar server.
	 * 
	 * @param expire_time
	 *            expiration time in seconds
	 * @param renew_time
	 *            renew time in seconds
	 * @param keepalive_time
	 *            keep-alive packet rate (inter-arrival time) in milliseconds
	 */
	public void loopRegister(int expire_time, int renew_time,
			long keepalive_time) {
		if (ra.isRegistering())
			ra.halt();
		ra.loopRegister(expire_time, renew_time, keepalive_time);
	}

	/** Unregister with the registrar server */
	public void unregister() {
		if (ra.isRegistering())
			ra.halt();
		ra.unregister();
	}

	/** Unregister all contacts with the registrar server */
	public void unregisterall() {
		if (ra.isRegistering())
			ra.halt();
		ra.unregisterall();
	}

	// ********************** UA callback functions **********************

	/** When a new call is incoming */
	public void onUaCallIncoming(UserAgent ua, NameAddress callee,
			NameAddress caller) {
		if (ua.user_profile.redirect_to != null) // redirect the call
		{
			display
					.setText("CALL redirected to "
							+ ua.user_profile.redirect_to);
			ua.redirect(ua.user_profile.redirect_to);
		} else if (ua.user_profile.accept_time >= 0) // automatically accept
		// the call
		{
			display.setText("ON CALL");
			jComboBox1.setSelectedItem(null);
			comboBoxEditor1.setItem(caller.toString());
			// ua.accept();
			ua.automaticAccept(ua.user_profile.accept_time);
		} else {
			display.setText("INCOMING CALL");
			jComboBox1.setSelectedItem(null);
			comboBoxEditor1.setItem(caller.toString());
		}
	}

	/** When an ougoing call is remotly ringing */
	public void onUaCallRinging(UserAgent ua) {
		display.setText("RINGING");
	}

	/** When an ougoing call has been accepted */
	public void onUaCallAccepted(UserAgent ua) {
		display.setText("ON CALL");
	}

	/** When an incoming call has been cancelled */
	public void onUaCallCancelled(UserAgent ua) {
		display.setText("CANCELLED");
		ua.listen();
	}

	/** When a call has been trasferred */
	public void onUaCallTrasferred(UserAgent ua) {
		display.setText("TRASFERRED");
		ua.listen();
	}

	/** When an ougoing call has been refused or timeout */
	public void onUaCallFailed(UserAgent ua) {
		display.setText("FAILED");
		ua.listen();
	}

	/** When a call has been locally or remotely closed */
	public void onUaCallClosed(UserAgent ua) {
		display.setText("BYE");
		ua.listen();
	}

	// ********************** RA callback functions **********************

	/** When a UA has been successfully (un)registered. */
	public void onUaRegistrationSuccess(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		ua.printLog("Registration success: " + result, LogLevel.HIGH);
		this.setTitle(user_profile.from_url);
	}

	/** When a UA failed on (un)registering. */
	public void onUaRegistrationFailure(RegisterAgent ra, NameAddress target,
			NameAddress contact, String result) {
		ua.printLog("Registration failure: " + result, LogLevel.HIGH);
		this.setTitle(user_profile.contact_url);
	}

	// ******************************** MAIN *******************************

	/** The main method. */
	public static void main(String[] args) {
		System.out.println("Graphical MJSIP UA 1.0");

		String file = null;
		boolean opt_regist = false;
		boolean opt_unregist = false;
		boolean opt_unregist_all = false;
		int opt_expires = -1;
		long opt_keepalive_time = -1;
		boolean opt_no_offer = false;
		String opt_call_to = null;
		int opt_accept_time = -1;
		int opt_hangup_time = -1;
		String opt_redirect_to = null;
		String opt_transfer_to = null;
		int opt_transfer_time = -1;
		int opt_re_invite_time = -1;
		boolean opt_audio = false;
		boolean opt_video = false;
		int opt_media_port = 0;
		boolean opt_recv_only = false;
		boolean opt_send_only = false;
		boolean opt_send_tone = false;
		String opt_send_file = null;
		String opt_recv_file = null;
		boolean opt_no_prompt = false;

		String opt_from_url = null;
		String opt_contact_url = null;
		String opt_username = null;
		String opt_realm = null;
		String opt_passwd = null;

		int opt_debug_level = -1;
		String opt_log_path = null;
		String opt_outbound_proxy = null;
		String opt_via_addr = SipProvider.AUTO_CONFIGURATION;
		int opt_host_port = SipStack.default_port;

		try {
			file = "config/mjsip.cfg";
			SipStack.init(file);
			if (opt_debug_level >= 0)
				SipStack.debug_level = opt_debug_level;
			if (opt_log_path != null)
				SipStack.log_path = opt_log_path;
			SipProvider sip_provider;
			if (file != null)
				sip_provider = new SipProvider(file);
			else
				sip_provider = new SipProvider(opt_via_addr, opt_host_port);
			if (opt_outbound_proxy != null)
				sip_provider.setOutboundProxy(new SocketAddress(
						opt_outbound_proxy));
			UserAgentProfile user_profile = new UserAgentProfile(file);

			if (opt_regist)
				user_profile.do_register = true;
			if (opt_unregist)
				user_profile.do_unregister = true;
			if (opt_unregist_all)
				user_profile.do_unregister_all = true;
			if (opt_expires > 0)
				user_profile.expires = opt_expires;
			if (opt_keepalive_time >= 0)
				user_profile.keepalive_time = opt_keepalive_time;
			if (opt_hangup_time > 0)
				user_profile.hangup_time = opt_hangup_time;
			if (opt_no_offer)
				user_profile.no_offer = true;
			if (opt_call_to != null)
				user_profile.call_to = opt_call_to;
			if (opt_accept_time >= 0)
				user_profile.accept_time = opt_accept_time;
			if (opt_redirect_to != null)
				user_profile.redirect_to = opt_redirect_to;
			if (opt_re_invite_time > 0)
				user_profile.re_invite_time = opt_re_invite_time;
			if (opt_transfer_to != null)
				user_profile.transfer_to = opt_transfer_to;
			if (opt_transfer_time > 0)
				user_profile.transfer_time = opt_transfer_time;
			if (opt_audio)
				user_profile.audio = true;
			if (opt_video)
				user_profile.video = true;
			if (opt_media_port > 0)
				user_profile.video_port = (user_profile.audio_port = opt_media_port) + 2;
			if (opt_from_url != null)
				user_profile.from_url = opt_from_url;
			if (opt_contact_url != null)
				user_profile.contact_url = opt_contact_url;
			if (opt_username != null)
				user_profile.username = opt_username;
			if (opt_realm != null)
				user_profile.realm = opt_realm;
			if (opt_passwd != null)
				user_profile.passwd = opt_passwd;
			if (opt_recv_only)
				user_profile.recv_only = true;
			if (opt_send_only)
				user_profile.send_only = true;
			if (opt_send_tone)
				user_profile.send_tone = true;
			if (opt_send_file != null)
				user_profile.send_file = opt_send_file;
			if (opt_recv_file != null)
				user_profile.recv_file = opt_recv_file;
			if (opt_no_prompt)
				user_profile.no_prompt = true;

			// use audio as default media in case of..
			if ((opt_recv_only || opt_send_only || opt_send_tone
					|| opt_send_file != null || opt_recv_file != null)
					&& !opt_video)
				user_profile.audio = true;

			new GraphicalUA(sip_provider, user_profile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ****************************** Logs *****************************

//	/** Adds a new string to the default Log */
//	private void printLog(String str) {
//		printLog(str, LogLevel.HIGH);
//	}
//
//	/** Adds a new string to the default Log */
//	private void printLog(String str, int level) {
//		if (log != null)
//			log.println("GraphicalUA: " + str, level + SipStack.LOG_LEVEL_UA);
//	}
//
//	/** Adds the Exception message to the default Log */
//	private void printException(Exception e, int level) {
//		if (log != null)
//			log.printException(e, level + SipStack.LOG_LEVEL_UA);
//	}
}