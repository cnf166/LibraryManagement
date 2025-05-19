package librarymanagement;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

public class dashboardAdminController implements Initializable {

    @FXML
    private Button addBook_btn;

    @FXML
    private AnchorPane addBook_form;

    @FXML
    private Button addingBooks_btn;

    @FXML
    private Label adminId_label;

    @FXML
    private Button arrow_btn;

    @FXML
    private Button arrow_btn1;

    @FXML
    private TextField author_field;

    @FXML
    private Button bars_btn;

    @FXML
    private Button bars_btn1;

    @FXML
    private TextField bookTitle_field;

    @FXML
    private TextField bookType_field;

    @FXML
    private Button browseImage_btn;

    @FXML
    private Circle circle_image;

    @FXML
    private Button close;

    @FXML
    private Label currentForm_label;

    @FXML
    private Button edit_btn;

    @FXML
    private FontAwesomeIcon edit_icon;

    @FXML
    private TextField imagePath_field;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane mainCenter_form;

    @FXML
    private Button minimize;

    @FXML
    private AnchorPane nav_form;

    @FXML
    private AnchorPane halfNav_form;

    @FXML
    private Label status_label;

    @FXML
    private ComboBox<String> deleteBookComboBox;

    @FXML
    private Button deleteBook_btn;

    private Connection connect;
    private PreparedStatement prepare;

    private double x = 0;
    private double y = 0;

    private Image image;

    private String selectedImagePath; // Lưu đường dẫn ảnh đã chọn

    // Phương thức để tải danh sách tiêu đề sách vào ComboBox
    private void loadBookTitles() {
        ObservableList<String> bookTitles = FXCollections.observableArrayList();
        connect = Database.connectDB();

        try {
            String sql = "SELECT bookTitle FROM book";
            prepare = connect.prepareStatement(sql);
            ResultSet result = prepare.executeQuery();

            while (result.next()) {
                bookTitles.add(result.getString("bookTitle"));
            }

            deleteBookComboBox.setItems(bookTitles);

        } catch (SQLException e) {
            e.printStackTrace();
            status_label.setText("Lỗi khi tải danh sách sách: " + e.getMessage());
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh sách");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tệp ảnh", "*.png", "*.jpg"));
        Stage stage = (Stage) addBook_form.getScene().getWindow();

        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedImagePath = file.getAbsolutePath();
            imagePath_field.setText(selectedImagePath);
        }
    }

    public void addBook() {
        connect = Database.connectDB();

        try {
            Alert alert;

            // Kiểm tra các trường nhập liệu
            if (bookTitle_field.getText().isEmpty() || author_field.getText().isEmpty() || bookType_field.getText().isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng điền đầy đủ Tiêu đề sách, Tác giả và Thể loại!");
                alert.showAndWait();
                return;
            }

            // Kiểm tra xem bookTitle đã tồn tại chưa
            String checkSql = "SELECT bookTitle FROM book WHERE bookTitle = ? COLLATE utf8mb4_bin";
            prepare = connect.prepareStatement(checkSql);
            prepare.setString(1, bookTitle_field.getText().trim());
            ResultSet result = prepare.executeQuery();

            if (result.next()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Tiêu đề sách đã tồn tại!");
                alert.showAndWait();
                return;
            }

            // Kiểm tra xem ảnh đã được chọn chưa
            if (selectedImagePath == null || selectedImagePath.isEmpty()) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn hình ảnh cho sách!");
                alert.showAndWait();
                return;
            }

            // Lấy ngày hiện tại
            Date date = new Date();
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());

            // Chuẩn hóa đường dẫn ảnh
            String imagePathForDB = selectedImagePath.replace("\\", "\\\\");

            // Thêm sách vào bảng book
            String sql = "INSERT INTO book (bookTitle, author, bookType, image, date) VALUES (?, ?, ?, ?, ?)";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, bookTitle_field.getText().trim());
            prepare.setString(2, author_field.getText().trim());
            prepare.setString(3, bookType_field.getText().trim());
            prepare.setString(4, imagePathForDB);
            prepare.setDate(5, sqlDate);

            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Thêm sách thành công!");
            alert.showAndWait();

            // Xóa các trường sau khi thêm thành công
            bookTitle_field.setText("");
            author_field.setText("");
            bookType_field.setText("");
            imagePath_field.setText("");
            selectedImagePath = null;
            status_label.setText("");

            // Cập nhật danh sách sách trong ComboBox
            loadBookTitles();

        } catch (SQLException e) {
            e.printStackTrace();
            status_label.setText("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteBook() {
        connect = Database.connectDB();

        try {
            Alert alert;

            // Kiểm tra xem có tiêu đề sách được chọn không
            String selectedBookTitle = deleteBookComboBox.getSelectionModel().getSelectedItem();
            if (selectedBookTitle == null) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn một tiêu đề sách để xóa!");
                alert.showAndWait();
                return;
            }

            // Xóa sách khỏi bảng book
            String sql = "DELETE FROM book WHERE bookTitle = ? COLLATE utf8mb4_bin";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, selectedBookTitle);
            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Xóa sách thành công!");
            alert.showAndWait();

            // Cập nhật danh sách sách trong ComboBox
            loadBookTitles();

            // Xóa trạng thái
            status_label.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            status_label.setText("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Chọn tệp ảnh");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Tệp ảnh", "*.png", "*.jpg"));
        Stage stage = (Stage) nav_form.getScene().getWindow();

        File file = open.showOpenDialog(stage);

        if (file != null) {
            image = new Image(file.toURI().toString(), 112, 84, false, true);
            circle_image.setFill(new ImagePattern(image));

            getData.path = file.getAbsolutePath();

            changeProfile();
        }
    }

    public void changeProfile() {
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        String sql = "UPDATE admin SET image = ? WHERE adminId = ?";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, uri);
            prepare.setString(2, getData.adminId);
            prepare.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void showProfile() {
        if (getData.path != null && !getData.path.isEmpty()) {
            try {
                String uri = "file:" + getData.path;
                image = new Image(uri, 112, 84, false, true);
                if (!image.isError()) {
                    circle_image.setFill(new ImagePattern(image));
                } else {
                    setDefaultProfileImage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                setDefaultProfileImage();
            }
        } else {
            setDefaultProfileImage();
        }
    }

    private void setDefaultProfileImage() {
        String defaultImagePath = "/image/default_profile.png"; // Thay bằng đường dẫn thực tế
        try {
            Image defaultImage = new Image(getClass().getResourceAsStream(defaultImagePath), 112, 84, false, true);
            circle_image.setFill(new ImagePattern(defaultImage));
        } catch (Exception e) {
            e.printStackTrace();
            circle_image.setFill(null);
        }
    }

    public void designInsertImage() {
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

    @FXML
    public void logout(ActionEvent event) {
        try {
            if (event.getSource() == logout_btn) {
                // Chuyển về giao diện đăng nhập
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
        } catch (IOException e) {
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

    public void sliderArrow() {
        TranslateTransition slide = new TranslateTransition();
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(nav_form);
        slide.setToX(-224);

        TranslateTransition slide1 = new TranslateTransition();
        slide1.setDuration(Duration.seconds(0.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(-224 + 90);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(0.5));
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
        slide.setDuration(Duration.seconds(0.5));
        slide.setNode(nav_form);
        slide.setToX(0);

        TranslateTransition slide1 = new TranslateTransition();
        slide1.setDuration(Duration.seconds(0.5));
        slide1.setNode(mainCenter_form);
        slide1.setToX(0);

        TranslateTransition slide2 = new TranslateTransition();
        slide2.setDuration(Duration.seconds(0.5));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminId_label.setText(getData.adminId);
        showProfile();
        designInsertImage();
        loadBookTitles(); // Tải danh sách sách vào ComboBox khi khởi tạo
    }
}