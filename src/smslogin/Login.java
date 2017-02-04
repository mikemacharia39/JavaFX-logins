package login;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {

	Stage primaryStage;
	Button btnclose;
	Text space1;
	Label userName;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage1) {
    	
    	primaryStage= primaryStage1;
    	primaryStage= new Stage(StageStyle.TRANSPARENT);
    	primaryStage.setResizable(false);
        primaryStage.setTitle("JavaFX Welcome");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        //grid.setPadding(new Insets(25, 25, 25, 25));

        userName = new Label("User Name:");
        grid.add(userName, 0, 0);
        grid.setPadding(new Insets(170,0,0,0));
        TextField userTextField = new TextField();
        userTextField.setPromptText("Your account username");
        grid.add(userTextField, 1, 0);
        userTextField.setMinWidth(280);
        userTextField.setMinHeight(30);
        Label pw = new Label("Password:");
        grid.add(pw, 0, 1);

        PasswordField pwBox = new PasswordField();
        pwBox.setMinHeight(30);
        pwBox.setPromptText("Your account password");
        grid.add(pwBox, 1, 1);

        Button btn = new Button("Sign in");
        
        btnclose= new Button("Close");
        btnclose.setId("red");
        btnclose.setOnAction(e -> {
        	primaryStage.close();
        });
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btn, btnclose);
        grid.add(hbBtn, 1, 2);

        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 3);
        actiontarget.setId("actiontarget");

        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                actiontarget.setText("Sign in button pressed");
            }
        });

        Scene scene = new Scene(grid, 640, 420);
        primaryStage.setScene(scene);
        scene.getStylesheets().add(Login.class.getResource("Login.css").toExternalForm());
        primaryStage.show();
    }
}