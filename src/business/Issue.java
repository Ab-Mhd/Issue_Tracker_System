package business;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Issue {
    private String issue, status, developer, title;
    public Issue(String title,String issue, String status){
        this.title = title;
        this.issue = issue;
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void updateStatus(String status) {
        this.status = status;
        int index = 0;
        List<Issue> issues = readIssues();
        for (Issue issue: issues ){
            if (getTitle().equals(issue.getTitle())){
                issues.set(index, this);
                break;
            }
            index++;
        }
        saveIssues(issues);
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void updateDev(String developer) {
        this.developer = developer;
        int index = 0;
        List<Issue> issues = readIssues();
        for (Issue issue: issues ){
            if (getTitle().equals(issue.getTitle())){
                issues.set(index, this);
                break;
            }
            index++;
        }
        saveIssues(issues);
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getIssue() {
        return issue;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        if (developer == null){
            return getTitle()+"::::"+getIssue()+"::::"+getStatus();
        }
        return getTitle()+"::::"+getIssue()+"::::"+getStatus()+"::::"+getDeveloper();
    }

    public void save(){
        List<Issue> issues = readIssues();
        issues.add(this);
        saveIssues(issues);
    }

    private static void saveIssues(List<Issue> issues){
        try (FileWriter fileWriter = new FileWriter("issues.dat")){
            for (Issue issue: issues){
                fileWriter.write(issue.toString()+"\n");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static List<Issue> readIssues(){
        List issues = new ArrayList<Issue>();
        try {
            FileReader fileReader = new FileReader("issues.dat");
            Scanner in = new Scanner(fileReader);
            while (in.hasNextLine()){
                String line = in.nextLine();
                String details[] = line.split("::::");
                Issue issue = new Issue(details[0], details[1], details[2]);
                if (details.length == 4){
                    issue.setDeveloper(details[3]);
                }

                issues.add(issue);
            }
        } catch (IOException e) { }

        return issues;
    }

    public static List<Issue> getAssigned(User developer){
        List<Issue> issues = readIssues(), assigned = new ArrayList<Issue>();
        for (Issue issue: issues){
            if (issue.getDeveloper() != null){
                if (issue.getDeveloper().equals(developer.getUsername()) && !issue.getStatus().equals("Closed")){
                    assigned.add(issue);
                }
            }
        }
        return assigned;
    }

    public static List<Issue> getUnclosedIssues(){
        List<Issue> issues = readIssues(), open = new ArrayList<Issue>();
        for (Issue issue: issues){
            if (!issue.getStatus().equals("Closed")){
                open.add(issue);
            }
        }
        issues = null;
        return open;
    }
}