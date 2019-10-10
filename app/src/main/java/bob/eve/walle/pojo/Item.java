package bob.eve.walle.pojo;

public class Item {
    String key;
    String value;
    int id=0;
    int type=0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Item(String key, String value, int id) {
        this.key = key;
        this.value=value;
        this.id=id;
    }

    public int getType() {
        return type;
    }

    public Item(String key, String value, int id, int type) {
        this.key = key;
        this.value = value;
        this.id = id;
        this.type = type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
