package dataSet;

public class UserDataSet {
    private long id;
    private String login;

    public UserDataSet(long id, String name){
        this.id = id;
        this.login = name;
    }

    public String getLogin() {
        return login;
    }
    public long getId() {
        return id;
    }
}
