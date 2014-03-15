package dataSet;

public final class UserDataSet {
    private final long id;
    private final String login;
    private final String password;


    public UserDataSet(long id, String name){
        this.id = id;
        this.login = name;
        this.password = "";
    }

    public UserDataSet(String name, String password){
        this.id = -1;
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
