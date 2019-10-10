package bob.eve.walle.pojo;

public class User {
	int uid;
	public User() {
		super();
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUjingyan() {
		return ujingyan;
	}
	public void setUjingyan(int ujingyan) {
		this.ujingyan = ujingyan;
	}
	public String getUnicheng() {
		return unicheng;
	}
	public void setUnicheng(String unicheng) {
		this.unicheng = unicheng;
	}
	public String getUusername() {
		return uusername;
	}
	public void setUusername(String uusername) {
		this.uusername = uusername;
	}
	public String getUpassword() {
		return upassword;
	}
	public void setUpassword(String upassword) {
		this.upassword = upassword;
	}
	public String getUstype() {
		return ustype;
	}
	public void setUstype(String ustype) {
		this.ustype = ustype;
	}
	public String getUsname() {
		return usname;
	}
	public void setUsname(String usname) {
		this.usname = usname;
	}
	public String getUspassword() {
		return uspassword;
	}
	public void setUspassword(String uspassword) {
		this.uspassword = uspassword;
	}
	int ujingyan;
	String unicheng;
	String uusername;
	String upassword;
	String ustype;
	String usname;
	String uspassword;

	@Override
	public String toString() {
		return "" +
				"uid=" + uid +
				"&ujingyan=" + ujingyan +
				"&unicheng=" + unicheng +
				"&uusername=" + uusername +
				"&upassword=" + upassword +
				"&ustype=" + ustype +
				"&usname=" + usname +
				"&uspassword=" + uspassword ;
	}
}
