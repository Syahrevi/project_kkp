package harahap;

import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    private TableColumn<jadwal_mengajar, String> col_tanggal_waktu;

    @FXML
    private TableColumn<jadwal_mengajar, Integer> col_id_tim;

    @FXML
    private TableColumn<jadwal_mengajar, Integer> col_id_pelatih;

    
    ObservableList<jadwal_mengajar> data = FXCollections.observableArrayList();

    public void switch_to_jadwal_mengajar() throws Exception{
        App.setRoot("jadwal_mengajar");
    }

    public void readDataJadwalMengajar() {
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_tanggal_waktu.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu"));
        col_id_tim.setCellValueFactory(new PropertyValueFactory<>("id_tim"));
        col_id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
        try{
            var url = "jdbc:sqlite:harahap.db";
            var conn = DriverManager.getConnection(url);
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM jadwal;");
            while(rs.next()){
               data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getInt(4)
               ));
            }
            
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // table_jadwal_mengajar.setItems(data);
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
        col_id_tim.setCellValueFactory(new PropertyValueFactory<>("id_tim"));
        col_tanggal_waktu.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu"));

        table_jadwal_mengajar.setItems(null);
        table_jadwal_mengajar.setItems(data);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readDataJadwalMengajar();
    } 
}