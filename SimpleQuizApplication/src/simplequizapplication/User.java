package simplequizapplication;

import javafx.beans.property.*;

public class User {
    private SimpleStringProperty name;
    private SimpleStringProperty username;
    private SimpleStringProperty contact;
    private SimpleStringProperty password;

    public User(String name, String username, String contact, String password) {
        this.name = new SimpleStringProperty(name);
        this.username = new SimpleStringProperty(username);
        this.contact = new SimpleStringProperty(contact);
        this.password = new SimpleStringProperty(password);
    }
    
    public String getName() {
        return name.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getContact() {
        return contact.get();
    }

    public String getPassword() {
        return password.get();
    }
    
}
