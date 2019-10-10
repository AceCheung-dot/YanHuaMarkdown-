package bob.eve.walle.pojo;

import java.util.Arrays;

public class Gexing {
	int gid;
	int guser;
	String gjianjie;
	int gzhuti;
	byte[] gicon;

	String gsex;
	String gt1;
	String gt2;
	String gt3;
	String gt4;
	String gt5;
	public Gexing() {
		super();
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getGuser() {
		return guser;
	}
	public void setGuser(int guser) {
		this.guser = guser;
	}
	public String getGjianjie() {
		return gjianjie;
	}
	public void setGjianjie(String gjianjie) {
		this.gjianjie = gjianjie;
	}
	public int getGzhuti() {
		return gzhuti;
	}
	public void setGzhuti(int gzhuti) {
		this.gzhuti = gzhuti;
	}
	public byte[] getGicon() {
		return gicon;
	}
	public void setGicon(byte[] gicon) {
		this.gicon = gicon;
	}

	public String getGsex() {
		return gsex;
	}
	public void setGsex(String gsex) {
		this.gsex = gsex;
	}
	public String getGt1() {
		return gt1;
	}
	public void setGt1(String gt1) {
		this.gt1 = gt1;
	}
	public String getGt2() {
		return gt2;
	}
	public void setGt2(String gt2) {
		this.gt2 = gt2;
	}
	public String getGt3() {
		return gt3;
	}
	public void setGt3(String gt3) {
		this.gt3 = gt3;
	}
	public String getGt4() {
		return gt4;
	}
	public void setGt4(String gt4) {
		this.gt4 = gt4;
	}
	public String getGt5() {
		return gt5;
	}
	public void setGt5(String gt5) {
		this.gt5 = gt5;
	}

	@Override
	public String toString() {
		return
				"gid=" + gid +
				"&guser=" + guser +
				"&gjianjie=" + gjianjie +
				"&gzhuti=" + gzhuti +
				"&gicon=" + "null" +
				"&gsex=" + gsex +
				"&gt1=" + gt1 +
				"&gt2=" + gt2 +
				"&gt3=" + gt3 +
				"&gt4=" + gt4 +
				"&gt5=" + gt5 ;
	}
}
