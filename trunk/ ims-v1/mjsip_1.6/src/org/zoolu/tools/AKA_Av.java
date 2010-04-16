package org.zoolu.tools;

/**  Support_IMS SIM
 * @author sub
 *
 */
public class AKA_Av {
	private int[] IK;
	private int[] CK;
	private int[] AUTN;
	private int[] RAND;
	private int[] XRES;
	
	public AKA_Av() {
		// TODO Auto-generated constructor stub
		IK = new int[16];
		CK = new int[16];
		AUTN = new int[16];
		RAND = new int[16];
		XRES = new int[16];
	}

	public int[] getIK() {
		return IK;
	}

	public void setIK(int[] ik) {
		IK = ik;
	}

	public int[] getCK() {
		return CK;
	}

	public void setCK(int[] ck) {
		CK = ck;
	}

	public int[] getAUTN() {
		return AUTN;
	}

	public void setAUTN(int[] autn) {
		AUTN = autn;
	}

	public int[] getRAND() {
		return RAND;
	}

	public void setRAND(int[] rand) {
		RAND = rand;
	}

	public int getXRES(int index) {
		return XRES[index];
	}
	
	public int[] getXRES() {
		return XRES;
	}


	public void setXRES(int[] xres) {
		XRES = xres;
	}
	
}
