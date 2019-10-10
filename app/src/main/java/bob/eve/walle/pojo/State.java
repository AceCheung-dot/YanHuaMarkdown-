package bob.eve.walle.pojo;

import java.util.Arrays;

public class State {
	int sid;
	int suser;
	byte[] sicon;
	String stitle;
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getSuser() {
		return suser;
	}
	public void setSuser(int suser) {
		this.suser = suser;
	}
	public byte[] getSicon() {
		return sicon;
	}
	public void setSicon(byte[] sicon) {
		this.sicon = sicon;
	}
	public String getStitle() {
		return stitle;
	}
	public void setStitle(String stitle) {
		this.stitle = stitle;
	}
	public State() {
		super();
	}

	@Override
	public String toString() {
		return
				"sid=" + sid +
				"&suser=" + suser +
				"&sicon=" + Arrays.toString(sicon) +
				"&stitle=" + stitle ;
	}
}
