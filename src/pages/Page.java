package pages;

import business.User;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public abstract class Page extends Stage {
    private User user;

    public Page(User user){
        setUser(user);
        setResizable(false);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    protected abstract void init();
    protected  void back(){
        new IssueList(getUser());
        close();
    }

    public static void error(String title, String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

