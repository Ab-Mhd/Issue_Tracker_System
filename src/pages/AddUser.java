package pages;

import business.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class AddUser extends Page {
    public AddUser(User user) {
        super(user);
        init();
    }

    @Override
    protected void init() {
        if (getUser() == null){
            error("Authentication failed", "Please login first.");
            return;
        }
        if (!getUser().getRole().equals("Manager")){
            error("Authentication failed", "You are not allowed to access this page.");
            return;
        }

        GridPane root = new GridPane();
        root.setPadding(new Insets(20,20,20,20));
        root.setVgap(20);
        root.setHgap(20);

        Label firstNameLabel = new Label("First name :");
        TextField firstNameField = new TextField();
        GridPane.setConstraints(firstNameLabel, 0,0);
        GridPane.setConstraints(firstNameField, 1,0);

        Label lastNameLabel = new Label("Last name :");
        TextField lastNameField = new TextField();
        GridPane.setConstraints(lastNameLabel, 0,1);
        GridPane.setConstraints(lastNameField, 1,1);

        Label usernameLabel = new Label("Username :");
        TextField usernameField = new TextField();
        GridPane.setConstraints(usernameLabel, 0,2);
        GridPane.setConstraints(usernameField, 1,2);

        Label roleLabel = new Label("Role :");
        String roles[] = {"Manager", "Developer", "Analyst"};
        ComboBox rolesCombo = new ComboBox(FXCollections.observableArrayList(roles));
        GridPane.setConstraints(roleLabel, 0,3);
        GridPane.setConstraints(rolesCombo, 1,3);



        Label passwordLabel = new Label("Password :");
        PasswordField passwordField = new PasswordField();
        GridPane.setConstraints(passwordLabel, 0,4);
        GridPane.setConstraints(passwordField, 1,4);


        Button submit = new Button("Submit");
        submit.setOnAction(e->{
            String firstName = firstNameField.getText().trim(),
                    lastName = lastNameField.getText().trim(),
                    username = usernameField.getText().trim(),
                    role = (String) rolesCombo.getValue(),
                    password = passwordField.getText().trim();
            if (firstName.equals("")){
                error("Field required","Please enter the user's first name.");
                return;
            }
            if (lastName.equals("")){
                error("Field required","Please enter the user's last name.");
                return;
            }

            if (username.equals("")){
                error("Field required","Please enter the username.");
                return;
            }

            if (role == null){
                error("Field required","Please select the user's role.");
                return;
            }

            if (password.equals("") || password.length() < 4){
                error("Validation failed","Please pick a password of 4 or more characters.");
                return;
            }

            User newUser = new User(firstName, lastName, username, role, password);
            newUser.save();
            new IssueList(getUser());
            close();
        });
        GridPane.setConstraints(submit, 0,5);

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e->{
            new IssueList(getUser());
            close();
        });
        GridPane.setConstraints(cancel, 1,5);

        root.getChildren().addAll(firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,usernameLabel,
                usernameField, roleLabel, rolesCombo, passwordLabel,
                passwordField, submit, cancel
        );

        Scene scene = new Scene(root, 500,400);
        setScene(scene);
        setTitle("Add User");
        show();
    }

}
