package org.zoolu.sip.header;


/**
 * @author TuanHao
 * Support IMS 
 */
public class PCalledPartyIDHeader extends PHeader {

	protected static String P_INFO= "3GPP-UTRAN-TDD; utran-cell-id-3gpp=234151D0FCE11";
	
	public PCalledPartyIDHeader() {
		super(BaseSipHeaders.P_Access_Network_Info, P_INFO);
	}
	public PCalledPartyIDHeader(String hname, String hvalue) {
		super(hname,hvalue);
	}
	
	public PCalledPartyIDHeader(String hvalue) {
		super(BaseSipHeaders.P_Access_Network_Info, hvalue);
	}

	public PCalledPartyIDHeader(Header hd) {
		super(hd);
	}
}
