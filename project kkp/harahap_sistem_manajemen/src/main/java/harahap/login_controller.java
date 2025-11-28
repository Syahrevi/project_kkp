package harahap;


import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class login_controller {

    @FXML
    private TextField textfield_username;
    @FXML
    private TextField textfield_nama;
    @FXML
    private TextField textfield_password;

    public void handle_login(ActionEvent e) throws Exception{
        var url = "jdbc:sqlite:harahap.db";
        var sql = "SELECT * FROM user WHERE username = ? AND nama = ? AND password = ?;";
        
        var username = textfield_username.getText();
        var nama = textfield_nama.getText();
        var password = textfield_password.getText();

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, nama);
            pstmt.setString(3, password);

            var rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful!");
                scene_switcher switcher = new scene_switcher();
                switcher.switch_to_jadwal_mengajar(e);
            } else {
                System.out.println("Invalid credentials.");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
