package bob.eve.walle.pojo;

public class Xingji {
	int  xid;
	int  xuser;
	int xmarker;
	int xfenzhi;
	public int getXid() {
		return xid;
	}
	public void setXid(int xid) {
		this.xid = xid;
	}
	public int getXuser() {
		return xuser;
	}
	public void setXuser(int xuser) {
		this.xuser = xuser;
	}
	public int getXmarker() {
		return xmarker;
	}
	public void setXmarker(int xmarker) {
		this.xmarker = xmarker;
	}
	public int getXfenzhi() {
		return xfenzhi;
	}
	public void setXfenzhi(int xfenzhi) {
		this.xfenzhi = xfenzhi;
	}

	@Override
	public String toString() {
		return "" +
				"&xid=" + xid +
				"&xuser=" + xuser +
				"&xmarker=" + xmarker +
				"&xfenzhi=" + xfenzhi
				;
	}
}
