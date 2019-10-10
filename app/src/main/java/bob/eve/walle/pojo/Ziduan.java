package bob.eve.walle.pojo;

public class Ziduan {
	int zid;
	int zstate;
	String zname;
	String zt1;
	String zt2;
	String zt3;
	public Ziduan() {
		super();
	}
	public int getZid() {
		return zid;
	}
	public void setZid(int zid) {
		this.zid = zid;
	}
	public int getZstate() {
		return zstate;
	}
	public void setZstate(int zstate) {
		this.zstate = zstate;
	}
	public String getZname() {
		return zname;
	}
	public void setZname(String zname) {
		this.zname = zname;
	}
	public String getZt1() {
		return zt1;
	}
	public void setZt1(String zt1) {
		this.zt1 = zt1;
	}
	public String getZt2() {
		return zt2;
	}
	public void setZt2(String zt2) {
		this.zt2 = zt2;
	}
	public String getZt3() {
		return zt3;
	}
	public void setZt3(String zt3) {
		this.zt3 = zt3;
	}

	@Override
	public String toString() {
		return
				"zid=" + zid +
				"&zstate=" + zstate +
				"&zname='" + zname +
				"&zt1=" + zt1 +
				"&zt2=" + zt2 +
				"&zt3='" + zt3
				;
	}
}
