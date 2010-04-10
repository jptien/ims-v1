package org.zoolu.sip.header;


/**
 * @author TuanHao
 * Support IMS 
 */
public class PAccessNetworkInfoHeader extends PHeader {

	protected static String P_INFO= "3GPP-UTRAN-TDD; utran-cell-id-3gpp=234151D0FCE11";
	
	public PAccessNetworkInfoHeader() {
		super(BaseSipHeaders.P_Access_Network_Info, P_INFO);
	}
	public PAccessNetworkInfoHeader(String hname, String hvalue) {
		super(hname,hvalue);
	}
	
	public PAccessNetworkInfoHeader(String hvalue) {
		super(BaseSipHeaders.P_Access_Network_Info, hvalue);
	}

	public PAccessNetworkInfoHeader(Header hd) {
		super(hd);
	}
}
