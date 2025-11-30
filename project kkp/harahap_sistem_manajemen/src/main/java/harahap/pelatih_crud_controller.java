package harahap;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class pelatih_crud_controller implements Initializable {
    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;

    @FXML private TableView<pelatih_crud> table_pelatih;
    @FXML private TableColumn<pelatih_crud, Integer> col_id_pelatih;
    @FXML private TableColumn<pelatih_crud, String> col_nama_pelatih;
    @FXML private TableColumn<pelatih_crud, String> col_tanggal_lahir;
    @FXML private TableColumn<pelatih_crud, String> col_nama_keahlian;

    @FXML private TextField textfield_id_pelatih;
    @FXML private TextField textfield_nama_pelatih;
    @FXML private ComboBox<String> combobox_nama_keahlian;
    @FXML private DatePicker datepicker_tanggal_lahir;
    @FXML private Button button_hapus;
    @FXML private Button button_edit;
    @FXML private Button button_reset;
    @FXML private Button button_submit;
    @FXML private ObservableList<pelatih_crud> data = FXCollections.observableArrayList();

    public void switch_to_jadwal_mengajar(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_jadwal_mengajar(e);
    }
    public void switch_to_crud_pelatih(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_pelatih(e);
    }
    public void switch_to_crud_siswa(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_siswa(e);
    }
    public void switch_to_crud_tim_siswa(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_tim_siswa(e);
    }
    private void readDataPelatih(ActionEvent e){
        col_id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_tanggal_lahir.setCellValueFactory(new PropertyValueFactory<>("tanggal_lahir"));
        col_nama_keahlian.setCellValueFactory(new PropertyValueFactory<>("nama_keahlian"));

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("Select * from pelatih")) {
            while (rs.next()) {
                data.add(new pelatih_crud(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4)
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_pelatih.setItems(data);
    }

    public void submit(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        String insertQuery = "INSERT INTO pelatih (nama_pelatih, tanggal_lahir, nama_keahlian) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, textfield_nama_pelatih.getText());
            pstmt.setString(2, datepicker_tanggal_lahir.getValue().toString());
            pstmt.setString(3, combobox_nama_keahlian.getValue().toString());
            pstmt.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
        readDataPelatih(e);
    }

    public void loadComboBoxNamaKeahlian(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        ObservableList<String> keahlianList = FXCollections.observableArrayList();
        String query = "SELECT nama_keahlian FROM keahlian";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                keahlianList.add(rs.getString("nama_keahlian"));
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        combobox_nama_keahlian.setItems(keahlianList);
    }
    public void reset(ActionEvent e){
        textfield_nama_pelatih.clear();
        datepicker_tanggal_lahir.setValue(null);
        combobox_nama_keahlian.setValue(null);
    }
    @FXML 
    public void delete(ActionEvent e){
        System.out.println("Delete button clicked");
        pelatih_crud selectedPelatih = table_pelatih.getSelectionModel().getSelectedItem();
        if (selectedPelatih != null) {
            String DB_URL = "jdbc:sqlite:harahap.db";
            String deleteQuery = "DELETE FROM pelatih WHERE id_pelatih = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, selectedPelatih.getId_pelatih());
                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataPelatih(e);
        }
        System.out.println("Delete button clicked2");
    }
    @FXML 
    public void edit(ActionEvent e){
        pelatih_crud selectedPelatih = table_pelatih.getSelectionModel().getSelectedItem();
        if (selectedPelatih != null) {
            String DB_URL = "jdbc:sqlite:harahap.db";
            String updateQuery = "UPDATE pelatih SET nama_pelatih = ?, tanggal_lahir = ?, nama_keahlian = ? WHERE id_pelatih = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, textfield_nama_pelatih.getText());
                pstmt.setString(2, datepicker_tanggal_lahir.getValue().toString());
                pstmt.setString(3, combobox_nama_keahlian.getValue().toString());
                pstmt.setInt(4, selectedPelatih.getId_pelatih());
                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataPelatih(e);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataPelatih(null);
        loadComboBoxNamaKeahlian(null);
    }
}
