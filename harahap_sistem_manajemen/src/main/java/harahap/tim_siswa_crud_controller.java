package harahap;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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

public class tim_siswa_crud_controller implements Initializable {
    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;
    @FXML private Button button_crud_absensi;

    @FXML private TableView<tim_siswa_crud> table_siswa;
    @FXML private TableColumn<tim_siswa_crud, Integer> col_id_tim;
    @FXML private TableColumn<tim_siswa_crud, String> col_nama_tim;
    @FXML private TableColumn<tim_siswa_crud, String> col_kategori_tim;

    @FXML private TextField textfield_nama_tim;
    @FXML private ComboBox<String> combobox_kategori_tim;

    @FXML private Button button_hapus;
    @FXML private Button button_edit;
    @FXML private Button button_reset;
    @FXML private Button button_submit;
    @FXML private ObservableList<tim_siswa_crud> data = FXCollections.observableArrayList();

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
    public void switch_to_crud_absensi(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_absensi(e);
    }
    public void switch_to_laporan_gaji(ActionEvent e) throws Exception{
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_gaji(e);
    }
    private void readDataTimSiswa(ActionEvent e){
        col_id_tim.setCellValueFactory(new PropertyValueFactory<>("id_tim"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_kategori_tim.setCellValueFactory(new PropertyValueFactory<>("kategori_tim"));
        
        data.clear();
        String DB_URL = "jdbc:sqlite:database/harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tim_siswa;")) {
            while (rs.next()) {
                data.add(new tim_siswa_crud(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3)
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_siswa.setItems(data);
    }

    public void submit(ActionEvent e){
        String DB_URL = "jdbc:sqlite:database/harahap.db";
        
        String insertQuery = "INSERT INTO tim_siswa (nama_tim, kategori_tim) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, textfield_nama_tim.getText());
            pstmt.setString(2, combobox_kategori_tim.getValue().toString());
            pstmt.executeUpdate();
            System.out.println("Siswa berhasil ditambahkan!");
        } catch (SQLException er) {
            System.err.println("Error inserting siswa: ");
            er.printStackTrace();
            return;
        }
        readDataTimSiswa(e);
        reset(e);
    }

    public void loadComboBoxNamakategoriTim(ActionEvent e){
        ObservableList<String> kategoriList = FXCollections.observableArrayList();
        kategoriList.add("Pemula");
        kategoriList.add("Sedang");
        kategoriList.add("Tinggi");
        combobox_kategori_tim.setItems(kategoriList);
    }
    
    public void reset(ActionEvent e){
        textfield_nama_tim.clear();
        combobox_kategori_tim.setValue(null);
    }
    @FXML 
    public void delete(ActionEvent e){
        tim_siswa_crud selectedSiswa = table_siswa.getSelectionModel().getSelectedItem();
        if (selectedSiswa != null) {
            String DB_URL = "jdbc:sqlite:database/harahap.db";
            String deleteQuery = "DELETE FROM tim_siswa WHERE id_tim = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, selectedSiswa.getId_tim());
                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataTimSiswa(e);
        }
    }
    @FXML 
    public void edit(ActionEvent e){
        String DB_URL = "jdbc:sqlite:database/harahap.db";
        tim_siswa_crud selectedSiswa = table_siswa.getSelectionModel().getSelectedItem();
        String updateQuery = "UPDATE tim_siswa SET nama_tim = ?, kategori_tim = ? WHERE id_tim = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setString(1, textfield_nama_tim.getText());
                pstmt.setString(2, combobox_kategori_tim.getValue().toString());
                pstmt.setInt(3, selectedSiswa.getId_tim());
                pstmt.executeUpdate();
                System.out.println("Siswa berhasil diperbarui!");
            } catch (SQLException er) {
                System.err.println("Error updating siswa: ");
                er.printStackTrace();
                return;
            }
            readDataTimSiswa(e);
            reset(e);
        }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataTimSiswa(null);
        loadComboBoxNamakategoriTim(null);
    }
}
