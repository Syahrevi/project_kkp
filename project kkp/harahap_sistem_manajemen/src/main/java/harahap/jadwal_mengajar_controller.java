package harahap;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
public class jadwal_mengajar_controller implements javafx.fxml.Initializable {
    @FXML
    private Button button_jadwal_mengajar;
    @FXML
    private TableView<jadwal_mengajar> table_jadwal_mengajar;
    @FXML
    private TableColumn<jadwal_mengajar, Integer> col_id_jadwal;
    @FXML
    private TableColumn<jadwal_mengajar, Integer> col_id_siswa;
    @FXML
    private TableColumn<jadwal_mengajar, Integer> col_id_pelatih;
    @FXML
    private TableColumn<jadwal_mengajar, String> col_tanggal_waktu_awal;
    @FXML
    private TableColumn<jadwal_mengajar, String> col_tanggal_waktu_akhir;
    @FXML
    private TableColumn<jadwal_mengajar, String> col_kehadiran;
    ObservableList<jadwal_mengajar> data = FXCollections.observableArrayList();

    public void switch_to_jadwal_mengajar() throws Exception{
        App.setRoot("jadwal_mengajar");
    }
    public void edit_kehadiran() throws Exception{
    }
    public void readDataJadwalMengajar() {
        // bind property ke kolom
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
        col_id_siswa.setCellValueFactory(new PropertyValueFactory<>("id_siswa"));
        col_tanggal_waktu_awal.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu_awal"));
        col_tanggal_waktu_akhir.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu_akhir"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // load data dari DB
        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db"; // sesuaikan dengan URL database Anda
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM jadwal;")) {
            while (rs.next()) {
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getInt(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table_jadwal_mengajar.setItems(data);

        // cellFactory untuk toggle kehadiran tanpa DAO
        col_kehadiran.setCellFactory(col -> {
            TableCell<jadwal_mengajar, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            cell.setOnMouseClicked(evt -> {
                if (!cell.isEmpty()) {
                    jadwal_mengajar row = cell.getTableRow().getItem();
                    if (row == null) return;

                    String current = row.getKehadiran();
                    String next = "HADIR".equalsIgnoreCase(current) ? "TIDAK HADIR" : "HADIR";

                    // update model segera agar UI responsif
                    row.setKehadiran(next);
                    table_jadwal_mengajar.refresh();

                    // update DB di background thread langsung dari controller
                    int idJadwal = row.getId_jadwalInt();
                    Task<Void> dbTask = new Task<>() {
                        @Override
                        protected Void call() throws Exception {
                            String sql = "UPDATE jadwal SET kehadiran = ? WHERE id_jadwal = ?";
                            try (Connection conn = DriverManager.getConnection(DB_URL);
                                 PreparedStatement ps = conn.prepareStatement(sql)) {
                                ps.setString(1, next);
                                ps.setInt(2, idJadwal);
                                ps.executeUpdate();
                            }
                            return null;
                        }

                        @Override
                        protected void failed() {
                            // rollback UI jika update DB gagal
                            Platform.runLater(() -> {
                                row.setKehadiran(current);
                                table_jadwal_mengajar.refresh();
                            });
                            getException().printStackTrace();
                        }
                    };
                    new Thread(dbTask).start();
                }
            });

            return cell;
        });
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataJadwalMengajar();
    } 
}