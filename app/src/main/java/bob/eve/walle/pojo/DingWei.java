package bob.eve.walle.pojo;

import java.sql.Timestamp;

public class DingWei {
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getDuser() {
		return duser;
	}
	public void setDuser(int duser) {
		this.duser = duser;
	}
	public Timestamp getDtime() {
		return dtime;
	}
	public void setDtime(Timestamp dtime) {
		this.dtime = dtime;
	}
	public double getDlongtitude() {
		return dlongtitude;
	}
	public void setDlongtitude(double dlongtitude) {
		this.dlongtitude = dlongtitude;
	}
	public double getDlatitude() {
		return dlatitude;
	}
	public void setDlatitude(double dlatitude) {
		this.dlatitude = dlatitude;
	}
	
	int did;//定位编号
	int duser;//定位用户
	Timestamp dtime;//时间
	public DingWei() {
		super();
	}
	double dlongtitude;//精度
	double dlatitude;//纬度
	String dplace;//地点
	public String getDplace() {
		return dplace;
	}
	public void setDplace(String dplace) {
		this.dplace = dplace;
	}
	public DingWei(int did, int duser, Timestamp dtime, double dlongtitude, double dlatitude, String dplace) {
		super();
		this.did = did;
		this.duser = duser;
		this.dtime = dtime;
		this.dlongtitude = dlongtitude;
		this.dlatitude = dlatitude;
		this.dplace = dplace;
	}

	@Override
	public String toString() {
		return "" +
				"&did=" + did +
				"&duser=" + duser +
				"&dtime=" + dtime +
				"&dlongtitude=" + dlongtitude +
				"&dlatitude=" + dlatitude +
				"&dplace=" + dplace + '\'' +
				'}';
	}
}
