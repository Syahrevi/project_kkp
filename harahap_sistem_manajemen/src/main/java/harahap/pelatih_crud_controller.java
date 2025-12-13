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
    @FXML private Button button_crud_absensi;

    @FXML private TableView<pelatih_crud> table_pelatih;
    @FXML private TableColumn<pelatih_crud, Integer> col_id_pelatih;
    @FXML private TableColumn<pelatih_crud, String> col_nama_pelatih;
    @FXML private TableColumn<pelatih_crud, String> col_tanggal_lahir;
    @FXML private TableColumn<pelatih_crud, String> col_nama_grade_keahlian; // updated
    @FXML private TableColumn<pelatih_crud, String> col_status_honor; // NEW
    

    @FXML private TextField textfield_id_pelatih;
    @FXML private TextField textfield_nama_pelatih;
    @FXML private ComboBox<GradeData> combobox_nama_grade_keahlian; // changed type
    @FXML private DatePicker datepicker_tanggal_lahir;
    @FXML private Button button_hapus;
    @FXML private Button button_edit;
    @FXML private Button button_reset;
    @FXML private Button button_submit;
    @FXML private ObservableList<pelatih_crud> data = FXCollections.observableArrayList();

    // DB URL (single place)
    private static final String DB_URL = "jdbc:sqlite:database/harahap.db";

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

    // Read all pelatih and include grade name via LEFT JOIN
    private void readDataPelatih(ActionEvent e){
    col_id_pelatih.setCellValueFactory(new PropertyValueFactory<>("id_pelatih"));
    col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
    col_tanggal_lahir.setCellValueFactory(new PropertyValueFactory<>("tanggal_lahir"));
    col_nama_grade_keahlian.setCellValueFactory(new PropertyValueFactory<>("nama_grade_keahlian"));
    col_status_honor.setCellValueFactory(new PropertyValueFactory<>("honor")); // bind to model honor

    data.clear();
    String sql = "SELECT p.id_pelatih, p.nama_pelatih, p.tanggal_lahir, g.nama_grade_keahlian, p.honor "
               + "FROM pelatih p "
               + "LEFT JOIN grade_keahlian g ON p.id_grade_keahlian = g.id_grade_keahlian "
               + "ORDER BY p.nama_pelatih ASC";

    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            String honorValue = rs.getString("honor");
            // Normalize DB values to "Ya"/"Tidak"
            String honorDisplay = (honorValue != null && (honorValue.equalsIgnoreCase("ya") || honorValue.equalsIgnoreCase("true") || honorValue.equalsIgnoreCase("yes"))) ? "Ya" : "Tidak";

            data.add(new pelatih_crud(
                rs.getInt("id_pelatih"),
                rs.getString("nama_pelatih"),
                rs.getString("tanggal_lahir"),
                rs.getString("nama_grade_keahlian"),
                honorDisplay
            ));
        }
    } catch (Exception er) {
        er.printStackTrace();
    }
    table_pelatih.setItems(data);

    // After populating, ensure the clickable cell factory is set up
    setupHonorColumnCellFactory();
    }
    private void setupHonorColumnCellFactory() {
        if (col_status_honor == null) return;

            col_status_honor.setCellFactory(col -> {
            javafx.scene.control.TableCell<pelatih_crud, String> cell = new javafx.scene.control.TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                    setStyle(""); // reset style
                }
            };
        cell.setOnMouseClicked(evt -> {
            if (cell.isEmpty()) return;
                pelatih_crud row = cell.getTableRow().getItem();
            if (row == null) return;
                String current = row.getHonor();
                String next = "Ya".equalsIgnoreCase(current) ? "Tidak" : "Ya";

            // Optimistically update UI
                row.setHonor(next);
                table_pelatih.refresh();

            // Persist change in background
                javafx.concurrent.Task<Void> dbTask = new javafx.concurrent.Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        String updateSQL = "UPDATE pelatih SET honor = ? WHERE id_pelatih = ?";
                        try (Connection conn = DriverManager.getConnection(DB_URL);
                        PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                            ps.setString(1, next); // store "Ya" or "Tidak" in DB
                            ps.setInt(2, row.getId_pelatih());
                            ps.executeUpdate();
                        }
                        return null;
                    }
                    @Override
                    protected void failed() {
                        // revert UI change on failure
                        javafx.application.Platform.runLater(() -> {
                            row.setHonor(current);
                            table_pelatih.refresh();
                        });
                        getException().printStackTrace();
                    }
                };
                new Thread(dbTask).start();
            });

            return cell;
        });
    }

    // Insert: set id_grade_keahlian from selected GradeData
    public void submit(ActionEvent e){
        GradeData selectedGrade = combobox_nama_grade_keahlian == null ? null : combobox_nama_grade_keahlian.getValue();
        Integer idGrade = selectedGrade == null ? null : selectedGrade.getId_grade_keahlian();

        String insertQuery = "INSERT INTO pelatih (id_grade_keahlian, nama_pelatih, tanggal_lahir) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            if (idGrade != null) pstmt.setInt(1, idGrade); else pstmt.setNull(1, java.sql.Types.INTEGER);
            pstmt.setString(2, textfield_nama_pelatih.getText());
            pstmt.setString(3, datepicker_tanggal_lahir.getValue() == null ? null : datepicker_tanggal_lahir.getValue().toString());
            pstmt.executeUpdate();
        } catch (SQLException er) {
            er.printStackTrace();
        }
        readDataPelatih(e);
    }

    // Load grade list into combobox (ComboBox<GradeData>)
    public void loadComboBoxNamaGradeKeahlian(ActionEvent e){
        ObservableList<GradeData> gradeList = FXCollections.observableArrayList();
        String query = "SELECT id_grade_keahlian, nama_grade_keahlian FROM grade_keahlian ORDER BY nama_grade_keahlian ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                gradeList.add(new GradeData(rs.getInt("id_grade_keahlian"), rs.getString("nama_grade_keahlian")));
            }
        } catch (SQLException er) {
            er.printStackTrace();
        }
        if (combobox_nama_grade_keahlian != null) combobox_nama_grade_keahlian.setItems(gradeList);
    }

    public void reset(ActionEvent e){
        textfield_nama_pelatih.clear();
        datepicker_tanggal_lahir.setValue(null);
        if (combobox_nama_grade_keahlian != null) combobox_nama_grade_keahlian.setValue(null);
    }

    @FXML 
    public void delete(ActionEvent e){
        pelatih_crud selectedPelatih = table_pelatih.getSelectionModel().getSelectedItem();
        if (selectedPelatih != null) {
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
    }

    @FXML 
    public void edit(ActionEvent e){
        pelatih_crud selectedPelatih = table_pelatih.getSelectionModel().getSelectedItem();
        if (selectedPelatih != null) {
            GradeData selectedGrade = combobox_nama_grade_keahlian == null ? null : combobox_nama_grade_keahlian.getValue();
            Integer idGrade = selectedGrade == null ? null : selectedGrade.getId_grade_keahlian();

            String updateQuery = "UPDATE pelatih SET id_grade_keahlian = ?, nama_pelatih = ?, tanggal_lahir = ?, honor = ? WHERE id_pelatih = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                if (idGrade != null) pstmt.setInt(1, idGrade); else pstmt.setNull(1, java.sql.Types.INTEGER);
                pstmt.setString(2, textfield_nama_pelatih.getText());
                pstmt.setString(3, datepicker_tanggal_lahir.getValue() == null ? null : datepicker_tanggal_lahir.getValue().toString());
                pstmt.setString(4, textfield_nama_pelatih.getText());
                pstmt.setInt(5, selectedPelatih.getId_pelatih());

                pstmt.executeUpdate();
            } catch (SQLException er) {
                er.printStackTrace();
            }
            readDataPelatih(e);
        }
    }
    private void setupSelectionListener() {
        table_pelatih.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                // populate fields from selected row
                // textfield_id_pelatih.setText(String.valueOf(newSel.getId_pelatih()));
                textfield_nama_pelatih.setText(newSel.getNama_pelatih());

                // safely set date
                if (newSel.getTanggal_lahir() != null && !newSel.getTanggal_lahir().isEmpty()) {
                    datepicker_tanggal_lahir.setValue(java.time.LocalDate.parse(newSel.getTanggal_lahir()));
                } else {
                    datepicker_tanggal_lahir.setValue(null);
                }

                // safely match grade
                if (combobox_nama_grade_keahlian != null && combobox_nama_grade_keahlian.getItems() != null) {
                    GradeData match = null;
                    String gradeName = newSel.getNama_grade_keahlian();

                    for (GradeData g : combobox_nama_grade_keahlian.getItems()) {
                        if (gradeName != null && gradeName.equals(g.getNama_grade_keahlian())) {
                            match = g;
                            break;
                        }
                    }
                    combobox_nama_grade_keahlian.setValue(match);
                }
            }
        });
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // load grade list first so combobox is ready
        loadComboBoxNamaGradeKeahlian(null);
        readDataPelatih(null);
        setupSelectionListener();
    }
}
