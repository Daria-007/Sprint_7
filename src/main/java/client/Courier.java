package client;

public class Courier {
    private final String login;
    private final String password;
    private final String firstName;

    public static Courier create(String login, String password, String firstName){
        return new Courier(login,password, firstName);
    }
    private Courier(String login, String password, String firstName){
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }
    public String getFirstName(){
        return firstName;
    }
}