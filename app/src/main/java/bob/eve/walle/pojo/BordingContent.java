package bob.eve.walle.pojo;

import java.sql.Timestamp;

public class BordingContent {
	//广播内容表，用于对每一次广播的信息进行记录
	int bcid;//广播内容的id
	String bcontent;//广播的内容
	int bcfromuid;//广播所来源的用户id,其中0代表系统
	int bctouid;//广播目标用户id，其中0代表广播系统从当前开始到未来一小时的在线人
	Timestamp bctime;//广播的上传时间
	int busing;//是否有效
	public int getBusing() {
		return busing;
	}
	public void setBusing(int busing) {
		this.busing = busing;
	}
	public int getBcid() {
		return bcid;
	}
	public void setBcid(int bcid) {
		this.bcid = bcid;
	}

	public int getBcfromuid() {
		return bcfromuid;
	}
	public void setBcfromuid(int bcfromuid) {
		this.bcfromuid = bcfromuid;
	}
	public int getBctouid() {
		return bctouid;
	}
	public void setBctouid(int bctouid) {
		this.bctouid = bctouid;
	}
	

	
	public BordingContent(int bcid, String bcontent, int bcfromuid, int bctouid, Timestamp bctime, int busing) {
		super();
		this.bcid = bcid;
		this.bcontent = bcontent;
		this.bcfromuid = bcfromuid;
		this.bctouid = bctouid;
		this.bctime = bctime;
		this.busing = busing;
	}
	@Override
	public String toString() {
		return "" +
				"bcid=" + bcid +
				"&bcontent=" + bcontent
				+ "&bcfromuid=" + bcfromuid +
				"&bctouid=" + bctouid +
				"&busing=" + busing+
				"&bctime=" + bctime

				+ "";
	}
	public String getBcontent() {
		return bcontent;
	}
	public void setBcontent(String bcontent) {
		this.bcontent = bcontent;
	}
	public Timestamp getBctime() {
		return bctime;
	}
	public void setBctime(Timestamp bctime) {
		this.bctime = bctime;
	}
	public BordingContent() {
		super();
	}
	
	

}
