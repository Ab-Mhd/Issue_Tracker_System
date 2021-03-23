package pages;

import business.Issue;
import business.User;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;


public class IssueList extends Page {
    private TableView list;


    public IssueList(User user) {
        super(user);
        init();
    }

    @Override
    protected void init(){
        if (getUser() == null){
            error("Authentication failed","Unauthorised access.");
            return;
        }

        if (!getUser().isLogged()){
            error("Authentication filed","Please login first.");
            return;
        }

        list = new TableView();

        TableColumn<String , Issue> titleColumn = new TableColumn<>("Issue tittle");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<String , Issue> statusColumn = new TableColumn<>("Issue status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        list.getColumns().add(titleColumn);
        list.getColumns().add(statusColumn);

        List<Issue> issues = null;

        if (getUser().getRole().equals("Manager")){
            issues = Issue.getUnclosedIssues();
        }else  if (getUser().getRole().equals("Developer")){
            issues = Issue.getAssigned(getUser());
        }else if (getUser().getRole().equals("Analyst")){
            issues = Issue.readIssues();
        }

        list.getItems().addAll(issues);


        HBox buttons = new HBox();
        buttons.setSpacing(15);
        Button newIssue = new Button("New Issue");
        newIssue.setOnAction(e->{
            close();
            new IssueDetails(getUser(), null);
        });
        Button details = new Button("See issue details");
        details.setOnAction(e->{
            Issue selected = (Issue) list.getSelectionModel().getSelectedItem();
            if (selected == null){
                error("No item selected","Please select an issue you want to view.");
                return;
            }
            close();
            new IssueDetails(getUser(), selected);
        });

        buttons.getChildren().addAll(newIssue, details);

        if (getUser().getRole().equals("Manager")){
            Button addUser = new Button("Add User");
            addUser.setOnAction(e->{
                new AddUser(getUser());
                close();
            });
            buttons.getChildren().add(addUser);
        }

        VBox root = new VBox();
        root.getChildren().addAll(list, buttons);
        root.setPadding(new Insets(20,20,20,20));
        root.setSpacing(20);


        Scene scene = new Scene(root, 400,400);
        setScene(scene);
        setTitle("Issues list");
        show();
    }
}
