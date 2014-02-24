package users;
public class User {
        private Long userId;
        private String password;
    public User(String password, Long userId){
        this.password = password;
        this.userId= userId;
    }
    public Boolean rightPassword(String password){
        return this.password.equals(password);
    }
    public long getUserid(){
        return this.userId;
    }
}
