
package org.zoolu.sip.header;

/** SIP P-Header */

/**
 * @author TuanHao
 * 
 * Support IMS
 */
public abstract class PHeader extends Header {

	public PHeader(String hname, String hvalue) {
		super(hname, hvalue);
	}

	public PHeader(Header hd) {
		super(hd);
	}
}
