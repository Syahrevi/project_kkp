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

    @FXML private TextField textfield_nama_tim;

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

        data.clear();
        String DB_URL = "jdbc:sqlite:database/harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM tim_siswa")) {

            while (rs.next()) {
                data.add(new tim_siswa_crud(
                    rs.getInt("id_tim"),
                    rs.getString("nama_tim")
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_siswa.setItems(data);
    }

    public void submit(ActionEvent e){
        String DB_URL = "jdbc:sqlite:database/harahap.db";

        if (textfield_nama_tim.getText().isEmpty()) {
            System.err.println("Error: Nama tim tidak boleh kosong!");
            return;
        }

        String insertQuery = "INSERT INTO tim_siswa (nama_tim) VALUES (?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, textfield_nama_tim.getText());
            pstmt.executeUpdate();
            System.out.println("Tim berhasil ditambahkan!");
        } catch (SQLException er) {
            System.err.println("Error inserting tim: ");
            er.printStackTrace();
            return;
        }
        readDataTimSiswa(e);
        reset(e);
    }

    public void reset(ActionEvent e){
        textfield_nama_tim.clear();
        table_siswa.getSelectionModel().clearSelection();
    }

    @FXML 
    public void delete(ActionEvent e){
        tim_siswa_crud selected = table_siswa.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String DB_URL = "jdbc:sqlite:database/harahap.db";
            String deleteQuery = "DELETE FROM tim_siswa WHERE id_tim = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
                pstmt.setInt(1, selected.getId_tim());
                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataTimSiswa(e);
            reset(e);
        }
    }

    @FXML 
    public void edit(ActionEvent e){
        tim_siswa_crud selected = table_siswa.getSelectionModel().getSelectedItem();
        if (selected == null) {
            System.err.println("Error: Pilih tim dulu!");
            return;
        }
        if (textfield_nama_tim.getText().isEmpty()) {
            System.err.println("Error: Nama tim tidak boleh kosong!");
            return;
        }

        String DB_URL = "jdbc:sqlite:database/harahap.db";
        String updateQuery = "UPDATE tim_siswa SET nama_tim = ? WHERE id_tim = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, textfield_nama_tim.getText());
            pstmt.setInt(2, selected.getId_tim());
            pstmt.executeUpdate();
            System.out.println("Tim berhasil diperbarui!");
        } catch (SQLException er) {
            System.err.println("Error updating tim: ");
            er.printStackTrace();
            return;
        }
        readDataTimSiswa(e);
        reset(e);
    }

    private void setupSelectionListener() {
        table_siswa.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                textfield_nama_tim.setText(newSel.getNama_tim());
            }
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataTimSiswa(null);
        setupSelectionListener();
    }
}
