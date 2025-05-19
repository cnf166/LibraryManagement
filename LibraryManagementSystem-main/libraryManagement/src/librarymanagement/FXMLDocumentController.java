package librarymanagement;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane signin_form;

    @FXML
    private TextField signIn_studentNumber;

    @FXML
    private PasswordField signIn_password;

    @FXML
    private Button login_Btn;

    @FXML
    private Hyperlink signIn_newaccount;

    @FXML
    private Button minimize;

    @FXML
    private Button close;

    @FXML
    private AnchorPane signUp_form;

    @FXML
    private TextField signUp_StudentNumber;

    @FXML
    private TextField signUp_emailStudent;

    @FXML
    private PasswordField signUp_Password;

    @FXML
    private RadioButton typeStudentAccount;

    @FXML
    private RadioButton typeAdminAccount;

    @FXML
    private Button signUp_Btn;

    @FXML
    private Hyperlink signUp_already;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void minimize(ActionEvent event) {
        Stage stage = (Stage) minimize.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void numbersOnly(KeyEvent event) {
        String input = signIn_studentNumber.getText();
        if (!input.matches("\\d*")) {
            signIn_studentNumber.setText(input.replaceAll("[^\\d]", ""));
            signIn_studentNumber.positionCaret(input.length());
        }
    }

    @FXML
    void switchForm(ActionEvent event) {
        if (event.getSource() == signIn_newaccount) {
            signin_form.setVisible(false);
            signUp_form.setVisible(true);
        } else if (event.getSource() == signUp_already) {
            signin_form.setVisible(true);
            signUp_form.setVisible(false);
        }
    }

    public void login(ActionEvent event) {
        connect = Database.connectDB();

        try {
            Alert alert;
            String loginId = signIn_studentNumber.getText().trim();
            String password = signIn_password.getText().trim();

            if (loginId.isEmpty() || password.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập ID và mật khẩu!");
                alert.showAndWait();
                return;
            }

            String sql;
            if (typeStudentAccount.isSelected()) {
                sql = "SELECT * FROM student WHERE studentNumber = ? AND password = ?";
            } else if (typeAdminAccount.isSelected()) {
                sql = "SELECT * FROM admin WHERE adminId = ? AND password = ?";
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng chọn loại tài khoản (Student hoặc Admin)!");
                alert.showAndWait();
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, loginId);
            prepare.setString(2, password);
            result = prepare.executeQuery();

            if (result.next()) {
                if (typeStudentAccount.isSelected()) {
                    // Lưu thông tin student
                    getData.studentNumber = result.getString("studentNumber");
                    getData.path = result.getString("image");

                    // Chuyển đến dashboard cho Student
                    login_Btn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("../resources/dashboard.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                } else if (typeAdminAccount.isSelected()) {
                    // Lưu thông tin admin
                    getData.adminId = result.getString("adminId");
                    getData.path = result.getString("image");

                    // Chuyển đến dashboard cho Admin
                    login_Btn.getScene().getWindow().hide();

                    Parent root = FXMLLoader.load(getClass().getResource("../resources/dashboardAdmin.fxml"));
                    Stage stage = new Stage();
                    Scene scene = new Scene(root);

                    stage.setScene(scene);
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.show();
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText(typeStudentAccount.isSelected() ? "Sai Student Number hoặc mật khẩu!" : "Sai Admin ID hoặc mật khẩu!");
                alert.showAndWait();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi khi đăng nhập: " + e.getMessage());
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

    @FXML
    private void handleSignUp() {
        connect = Database.connectDB();

        try {
            Alert alert;

            // Kiểm tra loại tài khoản
            if (!typeStudentAccount.isSelected()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Chỉ hỗ trợ đăng ký cho tài khoản Student!");
                alert.showAndWait();
                return;
            }

            // Kiểm tra các trường nhập liệu
            if (signUp_StudentNumber.getText().isEmpty() || signUp_Password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng điền đầy đủ Student Number và mật khẩu!");
                alert.showAndWait();
                return;
            }

            // Kiểm tra xem studentNumber đã tồn tại chưa
            String checkSql = "SELECT studentNumber FROM student WHERE studentNumber = ?";
            prepare = connect.prepareStatement(checkSql);
            prepare.setString(1, signUp_StudentNumber.getText().trim());
            result = prepare.executeQuery();

            if (result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Thông báo");
                alert.setHeaderText(null);
                alert.setContentText("Student Number đã tồn tại!");
                alert.showAndWait();
                return;
            }

            // Thêm dữ liệu vào bảng student
            String sql = "INSERT INTO student (studentNumber, password, image) VALUES (?, ?, ?)";
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, signUp_StudentNumber.getText().trim());
            prepare.setString(2, signUp_Password.getText().trim());
            prepare.setString(3, ""); // Image mặc định rỗng

            prepare.executeUpdate();

            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đăng ký thành công!");
            alert.showAndWait();

            // Xóa các trường sau khi đăng ký thành công
            signUp_StudentNumber.setText("");
            signUp_Password.setText("");

            // Chuyển về trang đăng nhập sau khi đăng ký thành công
            signUp_form.setVisible(false);
            signin_form.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Lỗi khi đăng ký: " + e.getMessage());
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Toggle group cho RadioButton để chỉ chọn một loại tài khoản
        ToggleGroup accountTypeGroup = new ToggleGroup();
        typeStudentAccount.setToggleGroup(accountTypeGroup);
        typeAdminAccount.setToggleGroup(accountTypeGroup);
        typeStudentAccount.setSelected(true); // Mặc định chọn Student
    }
}