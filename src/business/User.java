package business;

import pages.Page;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class User {
    private String firstName,lastName, password, role, username;
    private  boolean logged;
    public User(){}
    public User(String firstName, String lastName, String username, String role, String password){
        setFirstName(firstName);
        setLastName(lastName);
        setPassword(password);
        setRole(role);
        setUsername(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public boolean isLogged() {
        return logged;
    }

    @Override
    public String toString() {
        return firstName+" "+lastName+" - "+username;
    }

    public void save(){
        List<User> users = readUsers();
        users.add(this);
        try (FileWriter writer = new FileWriter("users.dat")){
            for (User user: users){
                writer.write(user.desc()+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<User> readUsers(){
        List<User> users = new ArrayList<User>();
        try (FileReader reader = new FileReader("users.dat")){
            Scanner in = new Scanner(reader);
            while (in.hasNextLine()){
                String line = in.nextLine();
                String details[] = line.split("::::");
               User user = new User(details[0], details[1],
                       details[2], details[3], details[4]);
               users.add(user);
            }
        }catch (IOException e){}
        return users;
    }

    public static User login(String username, String password){
        for (User user: readUsers()){
            if (user.getUsername().equals(username)){
                if (user.getPassword().equals(password)){
                    user.setLogged(true);
                    return user;
                }else {
                    Page.error("Authentication failed","Incorrect password.\nPlease check your password and try again");
                    return null;
                }
            }
        }
        Page.error("Unknown user","No such user in the system");
        return null;
    }
    public String desc(){
        return ""+firstName+"::::"+lastName+"::::"+
                username+"::::"+role+"::::"+password;
    }
}

