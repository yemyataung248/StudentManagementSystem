package org.example.studentmanagementsystem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.studentmanagementsystem.dao.StudentDAO;
import org.example.studentmanagementsystem.database.DatabaseConnection;
import org.example.studentmanagementsystem.model.Student;
import org.kordamp.bootstrapfx.BootstrapFX;
import org.kordamp.bootstrapfx.scene.layout.*;
import org.kordamp.bootstrapfx.scene.layout.Panel;



public class HelloApplication extends Application {

    private StudentDAO studentDAO = new StudentDAO();
    private TableView<Student> tableView = new TableView<>();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    private TextField nameField = new TextField();
    private TextField emailField = new TextField();
    private TextField ageField = new TextField();
    private TextField majorField = new TextField();
    private TextField searchField = new TextField();

    private Button addButton = new Button("Add Student");
    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private Button clearButton = new Button("Clear");
    private Button searchButton = new Button("Search");
    private Button refreshButton = new Button("Refresh");

    private Student selectedStudent = null;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Student Management System");

        //Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        VBox headerBox = createHeader();
        mainLayout.setTop(headerBox);

        VBox contentBox = new VBox(20);
        contentBox.setPadding(new Insets(20,0,0,0));

        Panel formPanel = createFormPanel();

        Panel tablePanel = createTablePanel();

        contentBox.getChildren().addAll(formPanel,tablePanel);
        mainLayout.setCenter(contentBox);

        loadStudent();
        setupEventHandlers();

        Scene scene = new Scene(mainLayout,1100,750);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event ->{
                    DatabaseConnection.closeConnection();
                });
    }

    private VBox createHeader(){
        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.getStyleClass().add("header");

        Label titleLabel = new Label("Student Management System");
        titleLabel.getStyleClass().addAll("h1","text-primary");
        Label subTitleLabel = new Label("Manage student rexords efficiently");
        subTitleLabel.getStyleClass().addAll("h2","text-muted");
        headerBox.getChildren().addAll(titleLabel,subTitleLabel);
        return headerBox;
    }

   private Panel createFormPanel(){
        Panel panel = new Panel("Student Information");
        panel.getStyleClass().add("panel-primary");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));

        //Name
       Label nameLabel = new Label("Name:");
       nameLabel.getStyleClass().add("form-label");
       nameField.setPromptText("Enter student name");
       nameField.getStyleClass().add("form-control");
       nameField.setPrefWidth(250);

       //Email
       Label emailLabel = new Label("Email:");
       emailLabel.getStyleClass().add("form-label");
       emailField.setPromptText("Enter email");
       emailField.getStyleClass().add("form-control");
       emailField.setPrefWidth(250);

       //Age
       Label ageLabel = new Label("Age:");
       ageLabel.getStyleClass().add("form-label");
       ageField.setPromptText("Enter age");
       ageField.getStyleClass().add("form-control");
       ageField.setPrefWidth(250);

       //major
       Label majorLabel = new Label("Major:");
       majorLabel.getStyleClass().add("form-label");
       majorField.setPromptText("Enter Major");
       majorField.getStyleClass().add("form-control");
       majorField.setPrefWidth(250);

       gridPane.add(nameLabel,0,0);
       gridPane.add(nameField,1,0);
       gridPane.add(emailLabel,2,0);
       gridPane.add(emailField,3,0);

       gridPane.add(ageLabel,0,1);
       gridPane.add(ageField,1,1);
       gridPane.add(majorLabel,2,1);
       gridPane.add(majorField,3,1);

       HBox buttonBox = new HBox(10);
       buttonBox.setAlignment(Pos.CENTER);
       buttonBox.setPadding(new Insets(10,0,0,0));

       //Add icons ro buttons

       FontAwesomeIconView addIcon = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
       addIcon.setSize("14");
       addButton.setGraphic(addIcon);
       addButton.getStyleClass().addAll("btn","btn-success");

       FontAwesomeIconView updateIcon = new FontAwesomeIconView(FontAwesomeIcon.EDIT);
       addIcon.setSize("14");
       updateButton.setGraphic(updateIcon);
       updateButton.getStyleClass().addAll("btn","btn-primary");
       updateButton.setDisable(true);

       FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);
       addIcon.setSize("14");
       deleteButton.setGraphic(deleteIcon);
       deleteButton.getStyleClass().addAll("btn","btn-danger");
       deleteButton.setDisable(true);

       FontAwesomeIconView clearIcon = new FontAwesomeIconView(FontAwesomeIcon.TIMES);
       addIcon.setSize("14");
       clearButton.setGraphic(clearIcon);
       clearButton.getStyleClass().addAll("btn","btn-default");

       buttonBox.getChildren().addAll(addButton,updateButton,deleteButton,clearButton);
       VBox contentBox = new VBox(15);
       contentBox.getChildren().addAll(gridPane,buttonBox);
       panel.setBody(contentBox);
       return panel;
   }
   private Panel createTablePanel(){
        Panel panel = new Panel("Student Records");
        panel.getStyleClass().add("panel-info");

        VBox contentBox = new VBox(15);
        contentBox.setPadding(new Insets(15));

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label searchLabel = new Label("Search:");
        searchLabel.getStyleClass().add("form-label");
        searchField.setPromptText("Search by name,email, or major ...");
        searchField.getStyleClass().add("form-control");
        searchField.setPrefWidth(400);

       FontAwesomeIconView searchIcon = new FontAwesomeIconView(FontAwesomeIcon.SEARCH);
       searchIcon.setSize("14");
       searchButton.setGraphic(searchIcon);
       searchButton.getStyleClass().addAll("btn","btn-info");

       FontAwesomeIconView refreshIcon = new FontAwesomeIconView(FontAwesomeIcon.REFRESH);
       refreshIcon.setSize("14");
       refreshButton.setGraphic(refreshIcon);
       refreshButton.getStyleClass().addAll("btn","btn-default");

       Region spacer = new Region();
       HBox.setHgrow(spacer,Priority.ALWAYS);

       Label countLabel = new Label("Total: ");
       countLabel.getStyleClass().add("text-muted");
       Label countValue = new Label("0");
       countValue.getStyleClass().addAll("badge","badge-primary");

       studentList.addListener((ListChangeListener.Change<? extends Student> c) -> {
           countValue.setText(String.valueOf(studentList.size()));
       });

       setupTable();
       searchBox.getChildren().addAll(searchLabel,searchField,searchButton,refreshButton,spacer,countLabel,countValue);

       contentBox.getChildren().addAll(searchBox,tableView);
       VBox.setVgrow(tableView,Priority.ALWAYS);
       panel.setBody(contentBox);
        return panel;
   }

   private void setupTable(){
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Student,Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setMaxWidth(80);
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Student,String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        nameColumn.setPrefWidth(200);

       TableColumn<Student,String> emailColumn = new TableColumn<>("Email");
       emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
       emailColumn.setPrefWidth(250);

       TableColumn<Student,Integer> ageColumn = new TableColumn<>("Age");
       ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asObject());
       ageColumn.setMaxWidth(100);
       ageColumn.setStyle("-fx-alignment: CENTER;");

       TableColumn<Student,String> majorColumn = new TableColumn<>("Major");
       majorColumn.setCellValueFactory(cellData -> cellData.getValue().majorProperty());
       majorColumn.setPrefWidth(200);

        tableView.getColumns().addAll(idColumn,nameColumn,emailColumn,ageColumn,majorColumn);
        tableView.setItems(studentList);
        tableView.getStyleClass().add("table-striped");
   }

   private void setupEventHandlers(){
        addButton.setOnAction(e ->addStudent());
        updateButton.setOnAction(e ->updateStudent());
        deleteButton.setOnAction(e ->deleteStudent());
        clearButton.setOnAction(e ->clearForm());
        searchButton.setOnAction(e ->searchStudent());
        refreshButton.setOnAction(e ->loadStudent());
        searchButton.setOnAction(e ->searchStudent());//search on enter
       tableView.getSelectionModel().selectedItemProperty().addListener((obs,oldSelection,newSelection) ->
               {
                   if(newSelection != null){
                       selectedStudent = newSelection;
                       populateForm(newSelection);
                       updateButton.setDisable(false);
                       deleteButton.setDisable(false);
                       addButton.setDisable(true);
                   }
               }
               );
   }

   private void addStudent(){
        if(validateInput()){
            Student student = new Student(
                    nameField.getText().trim(),
                    emailField.getText().trim(),
                    Integer.parseInt(ageField.getText().trim()),
                    majorField.getText().trim()
            );
            if(studentDAO.addStudent(student)){
                showAlert(Alert.AlertType.INFORMATION,"Success","Student added successfully","btn-success");
                loadStudent();
                clearForm();
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","Failed to add student.Email might already exist.","btn-danger");
            }
        }
   }

   private void updateStudent(){
        if(selectedStudent != null && validateInput()){
            selectedStudent.setName(nameField.getText().trim());
            selectedStudent.setEmail(emailField.getText().trim());
            selectedStudent.setAge(Integer.parseInt(ageField.getText().trim()));
            selectedStudent.setMajor(majorField.getText().trim());
            if(studentDAO.updateStudent(selectedStudent)){
                showAlert(Alert.AlertType.INFORMATION,"Success","Student updated successfully","btn-success");
                loadStudent();
                clearForm();
            }else{
                showAlert(Alert.AlertType.ERROR,"Error","Failed to update student.","btn-danger");
            }
        }
   }

   private void deleteStudent(){
        if(selectedStudent != null){
            Alert comfirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            comfirmAlert.setTitle("Confirm Delete");
            comfirmAlert.setHeaderText("Delete student");
            comfirmAlert.setContentText("Are you sure you want to delete "+ selectedStudent.getName()+ "?");
            comfirmAlert.getDialogPane().getStyleClass().add(BootstrapFX.bootstrapFXStylesheet());
            comfirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (studentDAO.deleteStudent(selectedStudent.getId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Student updated successfully", "btn-success");
                        loadStudent();
                        clearForm();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to update student.", "btn-danger");
                    }
                }
            });
        }
   }

   private void populateForm(Student student){
        nameField.setText(student.getName());
        emailField.setText(student.getEmail());
        ageField.setText(String.valueOf(student.getAge()));
        majorField.setText(student.getMajor());
    }

    private void searchStudent(){
        String keyword = searchField.getText().trim();
        if(keyword.isEmpty()){
            loadStudent();
        }else {
            studentList.clear();
            studentList.addAll(studentDAO.searchStudents(keyword));
        }
    }

    private void loadStudent(){
        studentList.clear();
        studentList.addAll(studentDAO.getAllStudents());
        searchField.clear();
    }

   private void clearForm(){
        nameField.clear();
        emailField.clear();
        ageField.clear();
        majorField.clear();
        selectedStudent = null;
        tableView.getSelectionModel().clearSelection();
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        addButton.setDisable(false);
   }

   private boolean validateInput(){
        if(nameField.getText().trim().isEmpty()){
            showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter student name.","btn-warning");
            return false;
        }
       if(emailField.getText().trim().isEmpty()){
           showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter email address.","btn-warning");
           return false;
       }
       if(emailField.getText().matches("^[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$")){
           showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter a valid email address.","btn-warning");
       }
       if(majorField.getText().trim().isEmpty()){
           showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter major.","btn-warning");
           return false;
       }
       if(ageField.getText().trim().isEmpty()){
           showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter age.","btn-warning");
           return false;
       }
       try{
           int age = Integer.parseInt(ageField.getText().trim());
           if(age<1 || age>150){
               showAlert(Alert.AlertType.WARNING,"Validation Error","Please enter a valid age (1-150).","btn-warning");
               return false;
           }
       }catch(NumberFormatException e){
           showAlert(Alert.AlertType.WARNING,"Validation Error","Age must be a number.","btn-warning");
           return false;
       }
        return true;
   }

   private void showAlert(Alert.AlertType type,String title,String content,String styleClass){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().getStyleClass().add(BootstrapFX.bootstrapFXStylesheet());
        alert.showAndWait();
   }
   /*
   ^ start of String
   $ end of String
   . any character
   * 0 or more
   + 1 or more
   ? optional
   \d digit
   \w word(a-z,A-Z,0-9,_)
   \s whitespace

   [] character classes [abc],[a-z]
   {} quantifiers {3,}
    */
}