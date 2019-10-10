package bob.eve.walle.pojo;

import java.sql.Timestamp;
import java.util.Arrays;

public class Pinglun {
int pid;
int puser;
int pmarkdown;
String pcontent;
int pdafen;
Timestamp ptime;
byte[] picon;
int pdianzan;
String pt1;
String pt2;
String pt3;
String pt4;
String pt5;
public Pinglun() {
	super();
}
public int getPid() {
	return pid;
}
public void setPid(int pid) {
	this.pid = pid;
}
public int getPuser() {
	return puser;
}
public void setPuser(int puser) {
	this.puser = puser;
}
public int getPmarkdown() {
	return pmarkdown;
}
public void setPmarkdown(int pmarkdown) {
	this.pmarkdown = pmarkdown;
}
public String getPcontent() {
	return pcontent;
}
public void setPcontent(String pcontent) {
	this.pcontent = pcontent;
}
public int getPdafen() {
	return pdafen;
}
public void setPdafen(int pdafen) {
	this.pdafen = pdafen;
}
public Timestamp getPtime() {
	return ptime;
}
public void setPtime(Timestamp ptime) {
	this.ptime = ptime;
}
public byte[] getPicon() {
	return picon;
}
public void setPicon(byte[] picon) {
	this.picon = picon;
}
public int getPdianzan() {
	return pdianzan;
}
public void setPdianzan(int pdianzan) {
	this.pdianzan = pdianzan;
}
public String getPt1() {
	return pt1;
}
public void setPt1(String pt1) {
	this.pt1 = pt1;
}
public String getPt2() {
	return pt2;
}
public void setPt2(String pt2) {
	this.pt2 = pt2;
}
public String getPt3() {
	return pt3;
}
public void setPt3(String pt3) {
	this.pt3 = pt3;
}
public String getPt4() {
	return pt4;
}
public void setPt4(String pt4) {
	this.pt4 = pt4;
}
public String getPt5() {
	return pt5;
}
public void setPt5(String pt5) {
	this.pt5 = pt5;
}

	@Override
	public String toString() {
		return //去开头
				"&pid=" + pid +//把‘，’改成‘&’
				"&puser=" + puser +
				"&pmarkdown=" + pmarkdown +
				"&pcontent=" + pcontent  +//删除‘’，\'
				"&pdafen=" + pdafen +
				"&ptime=" + ptime +
				"&picon=" + new String(picon)+
				"&pdianzan=" + pdianzan +
				"&pt1=" + pt1 +
				"&pt2=" + pt2 +
				"&pt3=" + pt3 +
				"&pt4=" + pt4 +
				"&pt5=" + pt5 ;//去掉’}‘
	}
}
