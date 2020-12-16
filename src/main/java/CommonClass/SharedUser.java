package CommonClass;

import java.io.Serializable;

public class SharedUser implements Serializable {
    private static final long serialVersionUID = 985214563257895L;
    private String userName;
    private String role;
    private String password;

    public SharedUser() {
    }

    public SharedUser(String userName, String role, String password) {
        this.userName = userName;
        this.role = role;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
