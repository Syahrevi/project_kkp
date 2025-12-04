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

public class siswa_crud_controller implements Initializable {
    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;
    @FXML private Button button_crud_absensi;

    @FXML private TableView<siswa_crud> table_siswa;
    @FXML private TableColumn<siswa_crud, Integer> col_id_siswa;
    @FXML private TableColumn<siswa_crud, String> col_nama_tim;
    @FXML private TableColumn<siswa_crud, String> col_nama_siswa;
    @FXML private TableColumn<siswa_crud, String> col_tanggal_lahir;
    @FXML private TableColumn<siswa_crud, String> col_kategori;

    // @FXML private TextField textfield_id_siswa;
    @FXML private ComboBox<String> combobox_nama_tim;
    @FXML private TextField textfield_nama_siswa;
    @FXML private DatePicker datepicker_tanggal_lahir;
    @FXML private ComboBox<String> combobox_kategori;

    @FXML private Button button_hapus;
    @FXML private Button button_edit;
    @FXML private Button button_reset;
    @FXML private Button button_submit;
    @FXML private ObservableList<siswa_crud> data = FXCollections.observableArrayList();

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
    private void readDataSiswa(ActionEvent e){
        col_id_siswa.setCellValueFactory(new PropertyValueFactory<>("id_siswa"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal_lahir.setCellValueFactory(new PropertyValueFactory<>("tanggal_lahir"));
        col_kategori.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        
        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("Select siswa.id_siswa, nama_tim, nama_siswa, tanggal_lahir, kategori from siswa, tim_siswa where siswa.id_tim = tim_siswa.id_tim")) {
            while (rs.next()) {
                data.add(new siswa_crud(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5)
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_siswa.setItems(data);
    }

    public void submit(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        
        // Validate inputs
        if (combobox_nama_tim.getValue() == null || combobox_nama_tim.getValue().isEmpty()) {
            System.err.println("Error: Pilih nama tim!");
            return;
        }
        if (textfield_nama_siswa.getText().isEmpty()) {
            System.err.println("Error: Masukkan nama siswa!");
            return;
        }
        if (datepicker_tanggal_lahir.getValue() == null) {
            System.err.println("Error: Pilih tanggal lahir!");
            return;
        }
        if (combobox_kategori.getValue() == null || combobox_kategori.getValue().isEmpty()) {
            System.err.println("Error: Pilih kategori!");
            return;
        }
        
        // Get id_tim from nama_tim
        Integer id_tim = null;
        String getIdTimQuery = "SELECT id_tim FROM tim_siswa WHERE nama_tim = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(getIdTimQuery)) {
            pstmt.setString(1, combobox_nama_tim.getValue().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id_tim = rs.getInt("id_tim");
            }
        } catch (SQLException er) {
            er.printStackTrace();
            return;
        }
        
        if (id_tim == null) {
            System.err.println("Error: Tim tidak ditemukan!");
            return;
        }
        
        // Insert siswa
        String insertQuery = "INSERT INTO siswa (id_tim, nama_siswa, tanggal_lahir, kategori) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, id_tim);
            pstmt.setString(2, textfield_nama_siswa.getText());
            pstmt.setString(3, datepicker_tanggal_lahir.getValue().toString());
            pstmt.setString(4, combobox_kategori.getValue().toString());
            pstmt.executeUpdate();
            System.out.println("Siswa berhasil ditambahkan!");
        } catch (SQLException er) {
            System.err.println("Error inserting siswa: ");
            er.printStackTrace();
            return;
        }
        readDataSiswa(e);
        reset(e);
    }

    public void loadComboBoxNamakategori(ActionEvent e){
        ObservableList<String> kategoriList = FXCollections.observableArrayList();
        kategoriList.add("Pemula");
        kategoriList.add("Sedang");
        kategoriList.add("Tinggi");
        combobox_kategori.setItems(kategoriList);
    }
    
    public void loadComboBoxNamaTim(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        ObservableList<String> timList = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT nama_tim FROM siswa, tim_siswa WHERE siswa.id_tim = tim_siswa.id_tim";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                timList.add(rs.getString("nama_tim"));
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        combobox_nama_tim.setItems(timList);
    }
    public void reset(ActionEvent e){
        textfield_nama_siswa.clear();
        datepicker_tanggal_lahir.setValue(null);
        combobox_kategori.setValue(null);
        combobox_nama_tim.setValue(null);
    }
    @FXML 
    public void delete(ActionEvent e){
        siswa_crud selectedSiswa = table_siswa.getSelectionModel().getSelectedItem();
        if (selectedSiswa != null) {
            String DB_URL = "jdbc:sqlite:harahap.db";
            String deleteQuery = "DELETE FROM siswa WHERE id_siswa = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, selectedSiswa.getId_siswa());
                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataSiswa(e);
        }
    }
    @FXML 
    public void edit(ActionEvent e){
        siswa_crud selectedSiswa = table_siswa.getSelectionModel().getSelectedItem();
        if (selectedSiswa != null) {
            // Validate inputs
            if (combobox_nama_tim.getValue() == null || combobox_nama_tim.getValue().isEmpty()) {
                System.err.println("Error: Pilih nama tim!");
                return;
            }
            if (textfield_nama_siswa.getText().isEmpty()) {
                System.err.println("Error: Masukkan nama siswa!");
                return;
            }
            if (datepicker_tanggal_lahir.getValue() == null) {
                System.err.println("Error: Pilih tanggal lahir!");
                return;
            }
            if (combobox_kategori.getValue() == null || combobox_kategori.getValue().isEmpty()) {
                System.err.println("Error: Pilih kategori!");
                return;
            }
            
            String DB_URL = "jdbc:sqlite:harahap.db";
            
            // Get id_tim from nama_tim
            Integer id_tim = null;
            String getIdTimQuery = "SELECT id_tim FROM tim_siswa WHERE nama_tim = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(getIdTimQuery)) {
                pstmt.setString(1, combobox_nama_tim.getValue().toString());
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    id_tim = rs.getInt("id_tim");
                }
            } catch (SQLException er) {
                er.printStackTrace();
                return;
            }
            
            if (id_tim == null) {
                System.err.println("Error: Tim tidak ditemukan!");
                return;
            }
            
            // Update siswa
            String updateQuery = "UPDATE siswa SET id_tim = ?, nama_siswa = ?, tanggal_lahir = ?, kategori = ? WHERE id_siswa = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                pstmt.setInt(1, id_tim);
                pstmt.setString(2, textfield_nama_siswa.getText());
                pstmt.setString(3, datepicker_tanggal_lahir.getValue().toString());
                pstmt.setString(4, combobox_kategori.getValue().toString());
                pstmt.setInt(5, selectedSiswa.getId_siswa());
                pstmt.executeUpdate();
                System.out.println("Siswa berhasil diperbarui!");
            } catch (SQLException er) {
                System.err.println("Error updating siswa: ");
                er.printStackTrace();
                return;
            }
            readDataSiswa(e);
            reset(e);
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataSiswa(null);
        loadComboBoxNamakategori(null);
        loadComboBoxNamaTim(null);
    }
}
