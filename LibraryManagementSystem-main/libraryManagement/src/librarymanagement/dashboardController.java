package librarymanagement;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.io.File;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class dashboardController implements Initializable {

    @FXML
    private Button close;

    @FXML
    private Button minimize;

    @FXML
    private Button bars_btn;

    @FXML
    private Button arrow_btn;

    @FXML
    private AnchorPane nav_form;

    @FXML
    private Circle circle_image;

    @FXML
    private Label studentNumber_label;

    @FXML
    private Button availableBooks_btn;

    @FXML
    private Button issueBooks_btn;

    @FXML
    private Button returnBooks_btn;

    @FXML
    private Button savedBooks_btn;

    @FXML
    private Button edit_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane availableBooks_form;

    @FXML
    private TableView<availableBooks> availableBooks_tableView;

    @FXML
    private TableColumn<availableBooks, String> col_ab_bookTitle;

    @FXML
    private TableColumn<availableBooks, String> col_ab_author;

    @FXML
    private TableColumn<availableBooks, String> col_ab_bookType;

    @FXML
    private TableColumn<availableBooks, String> col_ab_publishedDate;

    @FXML
    private ImageView availableBooks_imageView;

    @FXML
    private Button save_btn;

    @FXML
    private Label availableBooks_title;

    @FXML
    private Button take_btn;

    @FXML
    private AnchorPane halfNav_form;

    @FXML
    private Circle smallCircle_image;

    @FXML
    private Button halfNav_availableBtn;

    @FXML
    private Button halfNav_takeBtn;

    @FXML
    private Button halfNav_returnBtn;

    @FXML
    private Button halfNav_saveBtn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private AnchorPane issue_form;

    @FXML
    private AnchorPane returnBook_form;

    @FXML
    private AnchorPane savedBook_form;

    @FXML
    private Label currentForm_label;

    @FXML
    private Label take_StudentNumber;

    @FXML
    private TextField take_FirstName;

    @FXML
    private TextField take_LastName;

    @FXML
    private ComboBox<?> take_Gender;

    @FXML
    private TextField take_BookTitle;

    @FXML
    private Label take_IssuedDate;

    @FXML
    private ImageView take_imageView;

    @FXML
    private Label take_titleLabel;

    @FXML
    private Label take_authorLabel;

    @FXML
    private Label take_genreLabel;

    @FXML
    private Label take_dateLabel;

    @FXML
    private Button take_clearBtn;

    @FXML
    private Button take_takeBtn;

    @FXML
    private TableView<returnBook> return_tableView;

    @FXML
    private TableColumn<returnBook, String> col_return_BookTitle;

    @FXML
    private TableColumn<returnBook, String> col_return_Author;

    @FXML
    private TableColumn<returnBook, String> col_return_bookType;

    @FXML
    private TableColumn<returnBook, String> col_return_dateIssue;

    @FXML
    private ImageView return_imageView;

    @FXML
    private Button return_button;

    @FXML
    private Label returnBookTitle;

    @FXML
    private TableView<saveBook> save_tableView;

    @FXML
    private TableColumn<saveBook, String> col_saveTitle;

    @FXML
    private TableColumn<saveBook, String> col_saveAuthor;

    @FXML
    private TableColumn<saveBook, String> col_saveGenre;

    @FXML
    private TableColumn<saveBook, String> col_saveDate;

    @FXML
    private ImageView save_imageView;

    @FXML
    private Button unsaveBtn;

    @FXML
    private FontAwesomeIcon edit_icon;

    private Image image;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private String comboBox[] = {"Male", "Female", "Others"};

    public void gender() {

        List<String> combo = new ArrayList<>();

        for (String data : comboBox) {

            combo.add(data);
        }

        ObservableList list = FXCollections.observableList(combo);

        take_Gender.setItems(list);

    }

    public void takeBook() {
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO take VALUES (?,?,?,?,?,?,?,?,?,?)";
        String checkSql = "SELECT bookTitle FROM book WHERE bookTitle = ? COLLATE utf8mb4_bin"; // Thêm COLLATE để so sánh chính xác

        connect = Database.connectDB();

        try {
            Alert alert;

            if (take_FirstName.getText().isEmpty()
                    || take_LastName.getText().isEmpty()
                    || take_Gender.getSelectionModel().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo quản trị");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin sinh viên");
                alert.showAndWait();
                return;
            } else if (take_titleLabel.getText().isEmpty() || take_titleLabel.getText().equals("Book is not available!")) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo quản trị");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn sách hợp lệ để mượn.");
                alert.showAndWait();
                return;
            }

            // Debug thêm
            String selectedBookTitle = take_titleLabel.getText().trim();
            System.out.println("Đang kiểm tra sách: " + selectedBookTitle);

            // Kiểm tra chặt chẽ hơn với COLLATE utf8mb4_bin
            prepare = connect.prepareStatement(checkSql);
            prepare.setString(1, selectedBookTitle);
            result = prepare.executeQuery();

            if (!result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo quản trị");
                alert.setHeaderText(null);
                alert.setContentText("Sách '" + selectedBookTitle + "' không tồn tại trong hệ thống hoặc tên sách không khớp chính xác.");
                alert.showAndWait();
                return;
            }

            // Debug: In ra giá trị đang được chèn
            System.out.println("Đang chèn dữ liệu vào bảng take:");
            System.out.println("studentNumber: " + take_StudentNumber.getText().trim());
            System.out.println("bookTitle: " + selectedBookTitle);
            System.out.println("image path: " + getData.path);

            // Thêm bản ghi vào bảng take
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, take_StudentNumber.getText().trim());
            prepare.setString(2, take_FirstName.getText().trim());
            prepare.setString(3, take_LastName.getText().trim());
            prepare.setString(4, (String) take_Gender.getSelectionModel().getSelectedItem());
            prepare.setString(5, selectedBookTitle);
            prepare.setString(6, take_authorLabel.getText().trim());
            prepare.setString(7, take_genreLabel.getText().trim());
            prepare.setString(8, getData.path != null ? getData.path : "");
            prepare.setDate(9, sqlDate);
            prepare.setString(10, "Not Return");

            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo quản trị");
            alert.setHeaderText(null);
            alert.setContentText("Mượn sách thành công!");
            alert.showAndWait();

            clearTakeData();

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo quản trị");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi khi mượn sách: " + e.getMessage() +
                    "\nChi tiết: " + e.getSQLState() +
                    "\nMã lỗi: " + e.getErrorCode());
            alert.showAndWait();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void findBook(ActionEvent event) {
        clearFindData();
        String sql = "SELECT * FROM book WHERE bookTitle LIKE ?";
        connect = Database.connectDB();

        try {
            Alert alert;
            String searchText = take_BookTitle.getText().trim();

            if (searchText.isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo quản trị");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập tên sách cần tìm.");
                alert.showAndWait();
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, "%" + searchText + "%"); // Tìm kiếm theo phần tên sách
            result = prepare.executeQuery();

            if (result.next()) {
                take_titleLabel.setText(result.getString("bookTitle"));
                take_authorLabel.setText(result.getString("author"));
                take_genreLabel.setText(result.getString("bookType"));
                take_dateLabel.setText(result.getString("date"));
                getData.path = result.getString("image");

                if (getData.path != null && !getData.path.isEmpty()) {
                    File file = new File(getData.path);
                    if (file.exists()) {
                        String uri = file.toURI().toURL().toString();
                        image = new Image(uri, 127, 162, false, true);
                        if (!image.isError()) {
                            take_imageView.setImage(image);
                        } else {
                            take_imageView.setImage(null);
                            System.err.println("Lỗi tải hình ảnh: " + uri);
                        }
                    } else {
                        take_imageView.setImage(null);
                        System.err.println("Tệp hình ảnh không tồn tại: " + getData.path);
                    }
                } else {
                    take_imageView.setImage(null);
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo quản trị");
                alert.setHeaderText(null);
                alert.setContentText("Không tìm thấy sách phù hợp với: '" + searchText + "'");
                alert.showAndWait();
                take_titleLabel.setText("");
                take_imageView.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Thông báo quản trị");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi khi tìm sách: " + e.getMessage());
            alert.showAndWait();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void studentNumberLabel() {
        take_StudentNumber.setText(getData.studentNumber);
    }

    public void clearTakeData() {
        take_BookTitle.setText(""); // Xóa nội dung TextField
        take_titleLabel.setText("");
        take_authorLabel.setText("");
        take_genreLabel.setText("");
        take_dateLabel.setText("");
        take_imageView.setImage(null);

    }

    public void clearFindData() {

        take_titleLabel.setText("");
        take_authorLabel.setText("");
        take_genreLabel.setText("");
        take_dateLabel.setText("");
        take_imageView.setImage(null);

    }

    public void displayDate() {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String date = format.format(new java.util.Date());

        take_IssuedDate.setText(date);
    }

    public ObservableList<returnBook> returnBook() {

        ObservableList<returnBook> bookReturnData = FXCollections.observableArrayList();

        String check = "Not Return";

        String sql = "SELECT * FROM take WHERE checkReturn = '" + check + "' and studentNumber = '"
                + getData.studentNumber + "'";

        connect = Database.connectDB();

        try {

            returnBook rBook;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                rBook = new returnBook(result.getString("bookTitle"), result.getString("author"),
                        result.getString("bookType"), result.getString("image"),
                        result.getDate("date"));
                bookReturnData.add(rBook);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bookReturnData;

    }

    public void returnBooks() {

        String sql = "UPDATE take SET checkReturn = 'Returned' WHERE bookTitle = '" + getData.takeBookTitle + "'";

        connect = Database.connectDB();

        Alert alert;

        try {

            if (return_imageView.getImage() == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the book you want to return");
                alert.showAndWait();

            } else {

                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully returned!");
                alert.showAndWait();

                showBookReturn();

                return_imageView.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ObservableList<returnBook> retBook;

    public void showBookReturn() {

        retBook = returnBook();

        col_return_BookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_return_Author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_return_bookType.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_return_dateIssue.setCellValueFactory(new PropertyValueFactory<>("date"));

        return_tableView.setItems(retBook);

    }

    public void selectReturnBook() {
        returnBook rBook = return_tableView.getSelectionModel().getSelectedItem();
        int num = return_tableView.getSelectionModel().getFocusedIndex();

        if ((num - 1) < -1 || rBook == null) {
            return;
        }

        String uri = "file:" + rBook.getImage();
        if (rBook.getImage() != null && !rBook.getImage().isEmpty()) {
            try {
                image = new Image(uri, 143, 181, false, true);
                if (!image.isError()) {
                    return_imageView.setImage(image);
                } else {
                    return_imageView.setImage(null);
                    System.err.println("Lỗi tải hình ảnh: " + uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return_imageView.setImage(null);
            }
        } else {
            return_imageView.setImage(null);
        }

        getData.takeBookTitle = rBook.getTitle();
    }

    public ObservableList<availableBooks> dataList() {

        ObservableList<availableBooks> listBooks = FXCollections.observableArrayList();

        String sql = "SELECT * FROM book";

        connect = Database.connectDB();

        try {

            availableBooks aBooks;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                aBooks = new availableBooks(result.getString("bookTitle"),
                        result.getString("author"), result.getString("bookType"),
                        result.getString("image"), result.getDate("date"));

                listBooks.add(aBooks);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listBooks;
    }

    //    SAVED BOOKS
    public ObservableList<saveBook> savedBooksData() {

        ObservableList<saveBook> listSaveData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM save WHERE studentNumber = '" + getData.studentNumber + "'";

        connect = Database.connectDB();

        try {
            saveBook sBook;

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()) {

                sBook = new saveBook(result.getString("bookTitle"), result.getString("author"),
                        result.getString("bookType"), result.getString("image"), result.getDate("date"));

                listSaveData.add(sBook);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listSaveData;
    }

    private ObservableList<saveBook> sBookList;

    public void showSavedBooks() {

        sBookList = savedBooksData();

        col_saveTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_saveAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_saveGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_saveDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        save_tableView.setItems(sBookList);

    }

    public void selectSavedBooks() {

        saveBook sBook = save_tableView.getSelectionModel().getSelectedItem();
        int num = save_tableView.getSelectionModel().getFocusedIndex();

        if ((num - 1) < -1 || sBook == null) {
            return;
        }

        String uri = "file:" + sBook.getImage();
        if (sBook.getImage() != null && !sBook.getImage().isEmpty()) {
            try {
                image = new Image(uri, 117, 148, false, true);
                if (!image.isError()) {
                    save_imageView.setImage(image);
                } else {
                    save_imageView.setImage(null);
                    System.err.println("Lỗi tải hình ảnh: " + uri);
                }
            } catch (Exception e) {
                e.printStackTrace();
                save_imageView.setImage(null);
            }
        } else {
            save_imageView.setImage(null);
        }

        getData.savedImage = sBook.getImage();
        getData.savedTitle = sBook.getTitle();

    }

    public void saveBooks() {

        String sql = "INSERT INTO save VALUES (?,?,?,?,?,?)";

        connect = Database.connectDB();

        try {

            Alert alert;

            if (availableBooks_title.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please select the book");
                alert.showAndWait();
            } else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, getData.studentNumber);
                prepare.setString(2, getData.savedTitle);
                prepare.setString(3, getData.savedAuthor);
                prepare.setString(4, getData.savedGenre);
                prepare.setString(5, getData.savedImage);
                prepare.setDate(6, getData.savedDate);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Saved.");
                alert.showAndWait();

                showSavedBooks();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void unsaveBooks() {

        String sql = "DELETE FROM save WHERE bookTitle = '" + getData.savedTitle + "'"
                + " and studentNumber = '" + getData.studentNumber + "'";

        connect = Database.connectDB();

        try {

            Alert alert;

            if (save_imageView.getImage() == null) {

                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Please Select the book you want to unsave");
                alert.showAndWait();

            } else {

                statement = connect.createStatement();
                statement.executeUpdate(sql);

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Admin Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully unsaved.");
                alert.showAndWait();

                showSavedBooks();

                save_imageView.setImage(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    SHOWING BOOKS DATA
    private ObservableList<availableBooks> listBook;

    public void showAvailableBooks() {

        listBook = dataList();

        col_ab_bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        col_ab_author.setCellValueFactory(new PropertyValueFactory<>("author"));
        col_ab_bookType.setCellValueFactory(new PropertyValueFactory<>("genre"));
        col_ab_publishedDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        availableBooks_tableView.setItems(listBook);

    }

    public void selectAvailableBooks() {
        availableBooks bookData = availableBooks_tableView.getSelectionModel().getSelectedItem();
        int num = availableBooks_tableView.getSelectionModel().getFocusedIndex();

        if ((num - 1) < -1 || bookData == null) {
            return;
        }

        availableBooks_title.setText(bookData.getTitle());

        String uri = "file:" + bookData.getImage();
        if (bookData.getImage() != null && !bookData.getImage().isEmpty()) {
            try {
                image = new Image(uri, 134, 171, false, true);
                if (!image.isError()) {
                    availableBooks_imageView.setImage(image);
                } else {
                    availableBooks_imageView.setImage(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                availableBooks_imageView.setImage(null);
            }
        } else {
            availableBooks_imageView.setImage(null);
        }

        getData.takeBookTitle = bookData.getTitle();
        getData.savedTitle = bookData.getTitle();
        getData.savedAuthor = bookData.getAuthor();
        getData.savedGenre = bookData.getGenre();
        getData.savedImage = bookData.getImage();
        getData.savedDate = bookData.getDate();
    }

    public void abTakeButton(ActionEvent event) {

        if (event.getSource() == take_btn) {
            issue_form.setVisible(true);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Issue Books");

        }
    }

    public void studentNumber() {
//        WE CAN DISPLAY THE STUDENT NUMBER THAT STUDENT USED
        studentNumber_label.setText(getData.studentNumber);
    }

    public void insertImage() {

        FileChooser open = new FileChooser();
        open.setTitle("Image File");
        open.getExtensionFilters().add(new ExtensionFilter("Image file", "*png", "*jpg"));
        Stage stage = (Stage) nav_form.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {

            image = new Image(file.toURI().toString(), 112, 84, false, true);
            circle_image.setFill(new ImagePattern(image));
            smallCircle_image.setFill(new ImagePattern(image));

            getData.path = file.getAbsolutePath();

            changeProfile();

        }
    }

    public void changeProfile() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE student SET image = '" + uri + "' WHERE studentNumber = '" + getData.studentNumber + "'";

        connect = Database.connectDB();

        try {

            statement = connect.createStatement();
            statement.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showProfile() {
        if (getData.path != null && !getData.path.isEmpty()) {
            try {
                String uri = "file:" + getData.path;
                image = new Image(uri, 112, 84, false, true);
                if (!image.isError()) {
                    circle_image.setFill(new ImagePattern(image));
                    smallCircle_image.setFill(new ImagePattern(image));
                } else {
                    // Gán hình ảnh mặc định nếu có lỗi
                    setDefaultProfileImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                setDefaultProfileImage();
            }
        } else {
            // Gán hình ảnh mặc định nếu getData.path là null
            setDefaultProfileImage();
        }

    }

    private void setDefaultProfileImage() {
        // Đường dẫn đến hình ảnh mặc định trong thư mục tài nguyên
        String defaultImagePath = "/image/default_profile.png"; // Thay bằng đường dẫn thực tế
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream(defaultImagePath), 112, 84, false, true);
            circle_image.setFill(new ImagePattern(defaultImage));
            smallCircle_image.setFill(new ImagePattern(defaultImage));
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu hình ảnh mặc định không tải được, để trống hoặc xử lý khác
            circle_image.setFill(null);
            smallCircle_image.setFill(null);
        }
    }


    public void designInserImage() {

        edit_btn.setVisible(false);

        circle_image.setOnMouseEntered((MouseEvent event) -> {

            edit_btn.setVisible(true);

        });

        circle_image.setOnMouseExited((MouseEvent event) -> {

            edit_btn.setVisible(false);

        });

        edit_btn.setOnMouseEntered((MouseEvent event) -> {

            edit_btn.setVisible(true);
            edit_icon.setFill(Color.valueOf("#fff"));

        });

        edit_btn.setOnMousePressed((MouseEvent event) -> {

            edit_btn.setVisible(true);
            edit_icon.setFill(Color.RED);

        });

        edit_btn.setOnMouseExited((MouseEvent event) -> {

            edit_btn.setVisible(false);

        });

    }

    public void sideNavButtonDesign(ActionEvent event) {

        if (event.getSource() == halfNav_availableBtn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(true);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Available Books");

        } else if (event.getSource() == halfNav_takeBtn) {

            issue_form.setVisible(true);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Issue Books");

        } else if (event.getSource() == halfNav_returnBtn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Return Books");

            showBookReturn();

        } else if (event.getSource() == halfNav_saveBtn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(true);
            returnBook_form.setVisible(false);

            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Saved Books");

        }

    }

    public void navButtonDesign(ActionEvent event) {

        if (event.getSource() == availableBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(true);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Available Books");

        } else if (event.getSource() == issueBooks_btn) {

            issue_form.setVisible(true);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(false);

            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Issue Books");

        } else if (event.getSource() == returnBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(false);
            returnBook_form.setVisible(true);

            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Return Books");

            showBookReturn();

        } else if (event.getSource() == savedBooks_btn) {

            issue_form.setVisible(false);
            availableBooks_form.setVisible(false);
            savedBook_form.setVisible(true);
            returnBook_form.setVisible(false);

            savedBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            availableBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            issueBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            returnBooks_btn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            halfNav_saveBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #46589a, #4278a7);");
            halfNav_takeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_returnBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");
            halfNav_availableBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #344275, #3a6389);");

            currentForm_label.setText("Saved Books");

            showSavedBooks();

        }
    }

    private double x = 0;
    private double y = 0;

    public void sliderArrow() {

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(.5));
        slide.setNode(nav_form);
        slide.setToX(-224);

        TranslateTransition slide1 = new TranslateTransition();

        slide1.setDuration(Duration.seconds(.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(-224 + 90);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(0);

        slide.setOnFinished((ActionEvent event) -> {

            arrow_btn.setVisible(false);
            bars_btn.setVisible(true);

        });

        slide2.play();
        slide1.play();
        slide.play();

    }

    public void sliderBars() {

        TranslateTransition slide = new TranslateTransition();

        slide.setDuration(Duration.seconds(.5));
        slide.setNode(nav_form);
        slide.setToX(0);

        TranslateTransition slide1 = new TranslateTransition();

        slide1.setDuration(Duration.seconds(.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(0);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(.5));
        slide2.setNode(halfNav_form);
        slide2.setToX(-77);

        slide.setOnFinished((ActionEvent event) -> {

            arrow_btn.setVisible(true);
            bars_btn.setVisible(false);

        });

        slide2.play();
        slide1.play();
        slide.play();
    }

    @FXML
    public void logout(ActionEvent event) {
        try {
            if (event.getSource() == logout_btn) {
                // DASHBOARD về login form
                Parent root = FXMLLoader.load(getClass().getResource("../resources/FXMLDocument.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent e) -> {
                    x = e.getSceneX();
                    y = e.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent e) -> {

                    stage.setX(e.getScreenX() - x);
                    stage.setY(e.getScreenY() - y);

                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();

                logout_btn.getScene().getWindow().hide();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {

        System.exit(0);

    }

    public void minimize() {

        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        designInserImage();
        showProfile();
        showAvailableBooks();
        studentNumber();
        studentNumberLabel();
        displayDate();
        gender();
        showSavedBooks();
        try {
            showBookReturn();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thêm sự kiện nếu muốn tìm kiếm khi nhập
        take_BookTitle.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && newValue.length() > 2) { // Tìm kiếm sau khi nhập ít nhất 3 ký tự
                findBook(new ActionEvent());
            }
        });
    }
}
