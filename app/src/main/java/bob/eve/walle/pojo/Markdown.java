package bob.eve.walle.pojo;

import java.sql.Timestamp;

public class Markdown {
int mid;
Timestamp mtime;
byte[] micon;
int mtype;
int muser;
String mshenhe;
String mplace;
double mlongtitude;
double mlatitude;
String mtitle;
int msousuo;
String mt1;
String mt2;
String mt3;
String mt4;
String mt5;
public int getMid() {
	return mid;
}
public void setMid(int mid) {
	this.mid = mid;
}
public Timestamp getMtime() {
	return mtime;
}
public void setMtime(Timestamp mtime) {
	this.mtime = mtime;
}
public byte[] getMicon() {
	return micon;
}
public void setMicon(byte[] micon) {
	this.micon = micon;
}
public int getMtype() {
	return mtype;
}
public void setMtype(int mtype) {
	this.mtype = mtype;
}
public int getMuser() {
	return muser;
}
public void setMuser(int muser) {
	this.muser = muser;
}
public String getMshenhe() {
	return mshenhe;
}
public void setMshenhe(String mshenhe) {
	this.mshenhe = mshenhe;
}
public String getMplace() {
	return mplace;
}
public void setMplace(String mplace) {
	this.mplace = mplace;
}
public double getMlongtitude() {
	return mlongtitude;
}
public void setMlongtitude(double mlongtitude) {
	this.mlongtitude = mlongtitude;
}
public double getMlatitude() {
	return mlatitude;
}
public void setMlatitude(double mlatitude) {
	this.mlatitude = mlatitude;
}
public String getMtitle() {
	return mtitle;
}
public void setMtitle(String mtitle) {
	this.mtitle = mtitle;
}
public int getMsousuo() {
	return msousuo;
}
public void setMsousuo(int msousuo) {
	this.msousuo = msousuo;
}
public String getMt1() {
	return mt1;
}
public void setMt1(String mt1) {
	this.mt1 = mt1;
}
public String getMt2() {
	return mt2;
}
public void setMt2(String mt2) {
	this.mt2 = mt2;
}
public String getMt3() {
	return mt3;
}
public void setMt3(String mt3) {
	this.mt3 = mt3;
}
public String getMt4() {
	return mt4;
}
public void setMt4(String mt4) {
	this.mt4 = mt4;
}
public String getMt5() {
	return mt5;
}
public void setMt5(String mt5) {
	this.mt5 = mt5;
}
public Markdown() {
	super();
}

	@Override
	public String toString() {

		return "" +
				"mid=" + mid +
				"&mtime=" + mtime +
				"&micon=" + new String(micon) +
				"&mtype=" + mtype +
				"&muser=" + muser +
				"&mshenhe=" + mshenhe  +
				"&mplace=" + mplace  +
				"&mlongtitude=" + mlongtitude +
				"&mlatitude=" + mlatitude +
				"&mtitle=" + mtitle  +
				"&msousuo=" + msousuo +
				"&mt1=" + mt1 +
				"&mt2=" + mt2 +
				"&mt3=" + mt3 +
				"&mt4=" + mt4 +
				"&mt5=" + mt5
				;
	}
}
