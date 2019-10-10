package bob.eve.walle.pojo;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class QunLiao {
    int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMessage() {
        return Message;
    }
    public void setMessage(String message) {
        Message = message;
    }
    public int getWho() {
        return who;
    }
    public void setWho(int who) {
        this.who = who;
    }
    public QunLiao(String message, int who) {
        super();
        Message = message;
        this.who = who;
    }
    public QunLiao(int id, String message, int who) {
        super();
        this.id = id;
        Message = message;
        this.who = who;
    }
    public QunLiao(String s) {
        super();
        StringTokenizer token=new StringTokenizer(s,",");
        LinkedList<String> temp=new LinkedList<String>();
        while(token.hasMoreTokens())
        {
            temp.add(token.nextToken());
        }

        this.id = new Integer(temp.get(0)).intValue();
        Message = temp.get(1);
        this.who = new Integer(temp.get(2)).intValue();
    }
    @Override
    public String toString() {
        return "" + id + "," + Message + "," + who + "";
    }

    String Message;
    int who;

}
