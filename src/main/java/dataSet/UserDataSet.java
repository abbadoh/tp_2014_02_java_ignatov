package dataSet;

public class UserDataSet {
    private long id;
    private String login;
    private String password;


    public UserDataSet(long id, String name){
        this.id = id;
        this.login = name;
    }

    public UserDataSet(String name, String password){
        this.login = name;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }
    public String getPassword() { return password; }
    public long getId() {
        return id;
    }
}
