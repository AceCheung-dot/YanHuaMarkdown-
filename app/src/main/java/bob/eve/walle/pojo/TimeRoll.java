package bob.eve.walle.pojo;

import java.sql.Timestamp;


public class TimeRoll {
	public int getTid() {
		return tid;
	}
	public void setTid(int tid) {
		this.tid = tid;
	}
	public Timestamp getTtime() {
		return ttime;
	}
	public void setTtime(Timestamp ttime) {
		this.ttime = ttime;
	}
	public byte[] getTicon() {
		return ticon;
	}
	public void setTicon(byte[] ticon) {
		this.ticon = ticon;
	}
	public String getTcontent() {
		return tcontent;
	}
	public void setTcontent(String tcontent) {
		this.tcontent = tcontent;
	}
	public int getTuser() {
		return tuser;
	}
	public void setTuser(int tuser) {
		this.tuser = tuser;
	}
	public int getTfangwen() {
		return tfangwen;
	}
	public void setTfangwen(int tfangwen) {
		this.tfangwen = tfangwen;
	}
	public int getTmarkdown() {
		return tmarkdown;
	}
	public void setTmarkdown(int tmarkdown) {
		this.tmarkdown = tmarkdown;
	}
	public String getTt1() {
		return tt1;
	}
	public void setTt1(String tt1) {
		this.tt1 = tt1;
	}
	public String getTt2() {
		return tt2;
	}
	public void setTt2(String tt2) {
		this.tt2 = tt2;
	}
	public String getTt3() {
		return tt3;
	}
	public void setTt3(String tt3) {
		this.tt3 = tt3;
	}
	public String getTt4() {
		return tt4;
	}
	public void setTt4(String tt4) {
		this.tt4 = tt4;
	}
	public String getTt5() {
		return tt5;
	}
	public void setTt5(String tt5) {
		this.tt5 = tt5;
	}
	int tid;
	Timestamp ttime;
	byte[] ticon;
	String tcontent;
	int tuser;
	int tfangwen;
	int tmarkdown;
	String tt1;
	String tt2;
	String tt3;
	String tt4;
	String tt5;
	public TimeRoll() {
		super();
	}

}
