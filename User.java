public class User {
    public String username;
    User() {
        username = "";
    }
    User(String username) {
        this.username = username;
    }
    public String toString() {
        return username;
    }
}