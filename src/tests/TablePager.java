package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Pagination;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class TablePager extends Application {

	Connection conn;
	Statement stmt;
	ObservableList<Person> data = FXCollections.observableArrayList();
	
	public void connect()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn=DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "");
			System.out.println("Connected...");
			stmt=conn.createStatement();
		} catch (SQLException e) {
			
			e.printStackTrace();
		} catch (InstantiationException e) {
			
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}
	public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
	public void showError(String title, String message)
	{
		Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        //alert.setHeaderText("No Person Selected");
        alert.setContentText(message);
        alert.showAndWait();
	}
	
	
	ResultSet rs;
	
	public ObservableList<Person> getAtt()
	{
    ObservableList<Person> data = FXCollections.observableArrayList();
    
    String sql="SELECT id, fname, lname FROM  pager";
    			try {
    		    	connect();
    				ResultSet rec = stmt.executeQuery(sql);
    				while((rec!=null) && (rec.next()))
    				{ 
    					String id = (rec.getString("id"));
    					String fname = (rec.getString("fname"));
    					String lname = (rec.getString("lname"));
    					
    					data.add(new Person(id,fname, lname));
    				}
    				rec.close();
    				} catch (SQLException ea) {
    				showError("Database error", ea.getMessage());
    				ea.printStackTrace();
    				}
    			catch (Exception e) {
    				showError("Launching Error", e.getMessage());
    				e.printStackTrace();
    				}
    
    
    return data;
	}
    //new Person("32", "Humphrey", "Bogart");
    
    	
    private Pagination pagination;

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public int itemsPerPage() {
        return 1;
    }

    public int rowsPerPage() {
        return 5;
    }

    public VBox createPage(int pageIndex) {
        int lastIndex = 0;
        int displace = data.size() % rowsPerPage();
        if (displace > 0) {
            lastIndex = data.size() / rowsPerPage();
        } else {
            lastIndex = data.size() / rowsPerPage() - 1;

        }

        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();

        for (int i = page; i < page + itemsPerPage(); i++) {
            TableView<Person> table = new TableView<Person>();
            TableColumn numCol = new TableColumn("ID");
            numCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("num"));

            numCol.setMinWidth(20);

            TableColumn firstNameCol = new TableColumn("First Name");
            firstNameCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("firstName"));


            firstNameCol.setMinWidth(160);

            TableColumn lastNameCol = new TableColumn("Last Name");
            lastNameCol.setCellValueFactory(
                    new PropertyValueFactory<Person, String>("lastName"));

            lastNameCol.setMinWidth(160);

            table.getColumns().addAll(numCol, firstNameCol, lastNameCol);
            table.setItems(getAtt());
            if (lastIndex == pageIndex) {
       //         table.setItems(FXCollections.observableArrayList(data.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
            		table.setItems(getAtt());
            //     } else {
                table.setItems(FXCollections.observableArrayList(data.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
           }


            box.getChildren().add(table);
        }
        return box;
    }

    @Override
    public void start(final Stage stage) throws Exception {

        pagination = new Pagination((data.size() / rowsPerPage() + 1), 0);
        //   pagination = new Pagination(20 , 0);
        pagination.setStyle("-fx-border-color:red;");
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                if (pageIndex > data.size() / rowsPerPage() + 1) {
                    return null;
                } else {
                    return createPage(pageIndex);
                }
            }
        });

        AnchorPane anchor = new AnchorPane();
        AnchorPane.setTopAnchor(pagination, 10.0);
        AnchorPane.setRightAnchor(pagination, 10.0);
        AnchorPane.setBottomAnchor(pagination, 10.0);
        AnchorPane.setLeftAnchor(pagination, 10.0);
        anchor.getChildren().addAll(pagination);
        Scene scene = new Scene(anchor, 400, 250);
        stage.setScene(scene);
        stage.setTitle("Table pager");
        stage.show();
    }

    public static class Person {

        private final SimpleStringProperty num;
        private final SimpleStringProperty firstName;
        private final SimpleStringProperty lastName;

        private Person(String id, String fName, String lName) {
            this.firstName = new SimpleStringProperty(fName);
            this.lastName = new SimpleStringProperty(lName);
            this.num = new SimpleStringProperty(id);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getLastName() {
            return lastName.get();
        }

        public void setLastName(String fName) {
            lastName.set(fName);
        }

        public String getNum() {
            return num.get();
        }

        public void setNum(String id) {
            num.set(id);
        }
    }
}   