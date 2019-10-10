package bob.eve.walle.pojo;

/**
 * Created by zhangxutong .
 * Date: 16/04/24
 */
public class SwipeBean {
    public String name;
    public String time;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SwipeBean(String name, String time, String title, String content) {
        this.name = name;
        this.time = time;
        this.title = title;
        this.content = content;
    }

    public String title;
    public String content;
}
