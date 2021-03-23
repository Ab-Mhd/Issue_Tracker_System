package pages;

import business.Issue;
import business.User;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;


public class IssueDetails  extends Page {
    private  Issue issue;
    public IssueDetails(User user, Issue issue) {
        super(user);
        this.issue = issue;
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

        VBox root = new VBox();
        root.setSpacing(20);
        root.setPadding(new Insets(20,10,10,10));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Issue title :");
        TextField titleField = new TextField();
        GridPane.setConstraints(titleLabel, 0,0);
        GridPane.setConstraints(titleField, 1,0);

        Label descLabel = new Label("Issue Description :");
        TextArea issueDescField = new TextArea();
        GridPane.setConstraints(descLabel, 0,1);
        GridPane.setConstraints(issueDescField, 1,1);

        Label statusLabel = new Label("Issue Status :");
        TextField statusField = new TextField();
        statusField.setEditable(false);
        GridPane.setConstraints(statusLabel, 0,2);
        GridPane.setConstraints(statusField, 1,2);
        gridPane.getChildren().addAll(titleLabel,titleField, descLabel,
                issueDescField, statusLabel, statusField);

        HBox buttons = new HBox();
        Button cancel = new Button("Cancel");
        buttons.setSpacing(20);
        cancel.setOnAction(e->{
            back();
        });
        buttons.getChildren().add(cancel);



        if (issue == null){
            statusField.setText("New");
            Button submit = new Button("Submit");
            submit.setOnAction(e->{
                String title = titleField.getText().trim(),
                description = issueDescField.getText().trim();
                if (title.equals("")){
                    error("Field required", "Please enter the title of the issue.");
                    return;
                }
                if (description.equals("")){
                    error("Field required", "Please enter the description of the issue.");
                    return;
                }

                Issue newIssue = new Issue(title, description, "New");
                newIssue.save();
                back();
            });
            buttons.getChildren().add(submit);
        }else {
            titleField.setText(issue.getTitle());
            titleField.setEditable(false);

            issueDescField.setText(issue.getIssue());
            issueDescField.setEditable(false);

            statusField.setText(issue.getStatus());
            if (getUser().getRole().equals("Manager")){
                if (issue.getStatus().equals("New") || issue.getStatus().equals("Rejected")){
                    Label assignLabel = new Label("Assign to :");
                    List<User> users = User.readUsers();
                    List<User> developers = new ArrayList<User>();

                    for (User user: users){
                        if (user.getRole().equals("Developer")){
                            developers.add(user);
                        }
                    }
                    users = null;
                    ComboBox developersCombo = new ComboBox(FXCollections.observableArrayList(developers));
                    GridPane.setConstraints(assignLabel, 0,3);
                    GridPane.setConstraints(developersCombo, 1,3);

                    gridPane.getChildren().addAll(assignLabel, developersCombo);

                    Button assign = new Button("Assign");
                    assign.setOnAction(e->{
                        User dev = (User) developersCombo.getSelectionModel().getSelectedItem();
                        issue.updateDev(dev.getUsername());
                        issue.updateStatus("Assigned");
                        back();
                    });
                    buttons.getChildren().add(assign);



                }

                if (issue.getStatus().equals("Validated") || issue.getStatus().equals("Rejected")){
                    Button close = new Button("Close");
                    buttons.getChildren().add(close);
                    close.setOnAction(e->{
                        issue.updateStatus("Closed");
                        back();
                    });
                }

                if (issue.getStatus().equals("Resolved")){
                    Button validate = new Button("Validate");
                    buttons.getChildren().add(validate);
                    validate.setOnAction(e->{
                        issue.updateStatus("Validated");
                        back();
                    });
                    Button fail = new Button("Fail");
                    buttons.getChildren().add(fail);
                    fail.setOnAction(e->{
                        issue.updateStatus("Failed");
                        back();
                    });
                }
            }else if (getUser().getRole().equals("Developer")){
                if (issue.getStatus().equals("Assigned")){
                    Button reject = new Button("Reject");
                    reject.setOnAction(e->{
                        issue.updateStatus("Rejected");
                        issue.updateDev(null);
                        back();
                    });


                    Button  open = new Button("Open");
                    open.setOnAction(e->{
                        issue.updateStatus("Opened");
                        back();
                    });
                    buttons.getChildren().add(open);
                    buttons.getChildren().add(reject);
                }
                if (issue.getStatus().equals("Opened")){
                    Button resolve = new Button("Resolve");
                    resolve.setOnAction(e->{
                        issue.updateStatus("Resolved");
                        back();
                    });
                    buttons.getChildren().add(resolve);
                }

            }
        }


        setTitle("Issue Details.");
        root.getChildren().addAll(gridPane, buttons);
        Scene scene = new Scene(root, 650,450);
        setScene(scene);
        show();
    }


}
