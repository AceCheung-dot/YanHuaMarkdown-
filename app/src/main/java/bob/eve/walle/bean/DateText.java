package bob.eve.walle.bean;

public class DateText {
	private String date;
	private String text;
	String person;
	String place;
	int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	byte[] icon;

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	private int imgsrc;
	public DateText() {

	}

	public DateText(String date, String text, String person, String place, byte[] icon, int id) {
		this.date = date;
		this.text = text;
		this.person = person;
		this.place = place;
		this.icon = icon;
		this.id = id;
	}

	public DateText(String date, String text, int imgsrc) {
		super();
		this.date = date;
		this.text = text;
		this.imgsrc=imgsrc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(int imgsrc) {
		this.imgsrc = imgsrc;
	}

}
