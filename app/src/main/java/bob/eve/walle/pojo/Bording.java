package bob.eve.walle.pojo;

public class Bording {
	//广播表，主要用于将用户的用户id和广播id对应起来
	int bid;//广播id
	public int getBid() {
		return bid;
	}
	public void setBid(int bid) {
		this.bid = bid;
	}
	public int getBuser() {
		return buser;
	}
	public void setBuser(int buser) {
		this.buser = buser;
	}

	public String getBtoken() {
		return btoken;
	}
	public void setBtoken(String btoken) {
		this.btoken = btoken;
	}

	int buser;//广播的用户
	String btoken;//广播的token
	int busing;
	public int getBusing() {
		return busing;
	}
	public void setBusing(int busing) {
		this.busing = busing;
	}
	
	
	public Bording(int bid, int buser, String btoken, int busing) {
		super();
		this.bid = bid;
		this.buser = buser;
		this.btoken = btoken;
		this.busing = busing;
	}
	@Override
	public String toString() {
		return "" +
				"bid=" + bid
				+ "&buser=" + buser
				+ "&btoken=" + btoken
				+ "&busing=" + busing
				+ "";
	}
	public Bording() {
		super();
	}
	
	

}
