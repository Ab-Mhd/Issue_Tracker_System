import business.User;
import com.apple.eawt.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import pages.IssueList;
import pages.Page;

public class IssueTrackerSystem extends Application {
    private User  rootUser;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        rootUser = new User("root", "root",
                "root", "Manager", "root");
        GridPane root = new GridPane();
        root.setVgap(20);
        root.setHgap(20);
        root.setPadding(new Insets(30,30,
                30,30));

        Label usernameLabel = new Label("Username :");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password :");
        PasswordField passwordField = new PasswordField();

        Button login = new Button("Login");
        login.setOnAction(e->{
            String username = usernameField.getText().trim(),
                    password = passwordField.getText().trim();
            if (username.equals("")){
                Page.error("Field required",
                        "Please enter username.");
                return;
            }
            if (password.equals("")){
                Page.error("Field required",
                        "Please enter password.");
                return;
            }
            if (username.equals(rootUser.getUsername()) &&
                    password.equals(rootUser.getPassword())){
                rootUser.setLogged(true);
                new IssueList(rootUser);
                stage.close();
            }else {
                User user = User.login(username, password);
                if (user != null){
                    new IssueList(user);
                    stage.close();
                }
            }



        });

        GridPane.setConstraints(usernameLabel, 0,0);
        GridPane.setConstraints(usernameField, 1,0);
        GridPane.setConstraints(passwordLabel, 0,1);
        GridPane.setConstraints(passwordField, 1,1);
        GridPane.setConstraints(login, 0,2);

        root.getChildren().addAll(usernameLabel, usernameField,
                passwordLabel, passwordField, login);

        Scene scene = new Scene(root, 500,250);
        stage.setScene(scene);
        stage.setTitle("Login page");
        stage.setResizable(false);
        stage.show();


    }
}
