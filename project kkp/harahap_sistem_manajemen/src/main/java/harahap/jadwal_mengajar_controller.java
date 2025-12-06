package harahap;
import harahap.scene_switcher;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class jadwal_mengajar_controller implements javafx.fxml.Initializable {

    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;
    @FXML private Button button_crud_absensi;
    
    @FXML private TableView<jadwal_mengajar> table_jadwal_mengajar;
    @FXML private TableColumn<jadwal_mengajar, Integer> col_id_jadwal;
    @FXML private TableColumn<jadwal_mengajar, String> col_nama_siswa;
    @FXML private TableColumn<jadwal_mengajar, String> col_nama_pelatih;
    @FXML private TableColumn<jadwal_mengajar, String> col_tanggal;
    @FXML private TableColumn<jadwal_mengajar, String> col_waktu_mulai;
    @FXML private TableColumn<jadwal_mengajar, String> col_waktu_selesai;
    @FXML private TableColumn<jadwal_mengajar, String> col_kehadiran;

    @FXML private DatePicker datepicker_input_tanggal;
    @FXML private Spinner<Integer> spinner_start_hour;
    @FXML private Spinner<Integer> spinner_start_minute;
    @FXML private Spinner<Integer> spinner_end_hour;
    @FXML private Spinner<Integer> spinner_end_minute;
    @FXML private ComboBox<String> combobox_input_nama_pelatih;
    @FXML private ComboBox<String> combobox_input_nama_siswa;
    @FXML private TextField textfield_input_nama_pelatih;
    @FXML private TextField textfield_input_nama_siswa;

    @FXML private ToggleGroup hari;
    @FXML private RadioButton input_senin;
    @FXML private RadioButton input_selasa;
    @FXML private RadioButton input_rabu;
    @FXML private RadioButton input_kamis;
    @FXML private RadioButton input_jumat;
    @FXML private RadioButton input_sabtu;
    @FXML private RadioButton input_minggu;
    @FXML private Button input_batal;

    @FXML private Button button_input_submit;
    @FXML private Button button_input_reset;
    @FXML private Button button_input_edit;
    @FXML private Button button_input_delete;

    @FXML DatePicker datepicker_sort_daftar_absensi_setting;
    @FXML DatePicker datepicker_dari_tanggal_setting;
    @FXML DatePicker datepicker_sampai_tanggal_setting;
    @FXML Button button_tanggal_sekarang_setting;
    @FXML Button button_reset_setting;
    @FXML Button button_setting_print_laporan;

    @FXML Button button_show_all;


    ObservableList<jadwal_mengajar> data = FXCollections.observableArrayList();
    ObservableList<PelatihData> pelatihDataList = FXCollections.observableArrayList();
    ObservableList<SiswaData> siswaDataList = FXCollections.observableArrayList();
    private static final String DB_URL = "jdbc:sqlite:harahap.db";

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
    public void disableDatePickerRadio() {
        // Disable DatePicker whenever any radio is selected
        hari.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean anySelected = newToggle != null;
            datepicker_input_tanggal.setDisable(anySelected);
            
            // Update preview when day is selected
            if (anySelected) {
                String selectedDayName = getSelectedDayName();
                if (selectedDayName != null) {
                    LocalDate dateForDay = calculateDateForDay(selectedDayName);
                    String displayText = selectedDayName + " (" + dateForDay.toString() + ")";
                    System.out.println("Selected: " + displayText);
                }
            }
        });

        // When "Batal" button is pressed: clear radio selection and enable DatePicker
        input_batal.setOnAction(e -> {
            // clear selection so no radio is selected
            hari.selectToggle(null);
            // ensure DatePicker is enabled
            datepicker_input_tanggal.setDisable(false);
        });
    }
    public void edit_kehadiran(){

    }
    public void delete(ActionEvent e){
    System.out.println("Delete button clicked");
    jadwal_mengajar selectedJadwal = table_jadwal_mengajar.getSelectionModel().getSelectedItem();
    if (selectedJadwal == null) {
        System.out.println("No row selected to delete.");
        return;
    }

    String DB_URL = "jdbc:sqlite:harahap.db";

    String selectSql = "SELECT hari_berulang FROM jadwal WHERE id_jadwal = ?";
    String deleteByIdSql = "DELETE FROM jadwal WHERE id_jadwal = ?";
    String deleteRepeatingSql = "DELETE FROM jadwal WHERE hari_berulang = ?";

    try (Connection conn = DriverManager.getConnection(DB_URL)) {
        conn.setAutoCommit(false); // begin transaction

        String hariBerulangText = null;
        try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
            psSelect.setInt(1, selectedJadwal.getId_jadwalInt());
            try (ResultSet rs = psSelect.executeQuery()) {
                if (rs.next()) {
                    hariBerulangText = rs.getString("hari_berulang"); // returns null if DB value is NULL
                    if (hariBerulangText != null) {
                        hariBerulangText = hariBerulangText.trim();
                        if (hariBerulangText.isEmpty()) {
                            hariBerulangText = null;
                        }
                    }
                }
            }
        }

        // Delete the selected row by id
        try (PreparedStatement psDeleteId = conn.prepareStatement(deleteByIdSql)) {
            psDeleteId.setInt(1, selectedJadwal.getId_jadwalInt());
            int deleted = psDeleteId.executeUpdate();
            System.out.println("Deleted rows by id: " + deleted);
        }

        // If hari_berulang is present, delete all rows that share the same hari_berulang text
        if (hariBerulangText != null) {
            try (PreparedStatement psDeleteRepeat = conn.prepareStatement(deleteRepeatingSql)) {
                psDeleteRepeat.setString(1, hariBerulangText);
                int affected = psDeleteRepeat.executeUpdate();
                System.out.println("Also deleted " + affected + " rows with hari_berulang = '" + hariBerulangText + "'");
            }
        }

        conn.commit();
        System.out.println("Delete committed.");
    } catch (SQLException ex) {
        ex.printStackTrace();
        System.err.println("Delete failed.");
    }

        // Refresh table data after deletion
        readDataJadwalMengajar();
        System.out.println("Delete button finished");
    }

    public void edit(ActionEvent e){
        jadwal_mengajar selectedJadwal = table_jadwal_mengajar.getSelectionModel().getSelectedItem();
        if (selectedJadwal != null) {
            try {
                // Validate inputs
                String namaPelatih = textfield_input_nama_pelatih.getText().trim();
                String namaSiswa = textfield_input_nama_siswa.getText().trim();
                
                if (namaPelatih.isEmpty() || namaSiswa.isEmpty()) {
                    System.err.println("Error: Nama pelatih dan siswa harus diisi!");
                    return;
                }
                
                // Check if a day radio button is selected or datepicker is filled
                String selectedDayName = getSelectedDayName();
                String tanggal;
                Integer hari_berulang = null;
                
                if (selectedDayName != null) {
                    // Use day-based scheduling
                    LocalDate dateForDay = calculateDateForDay(selectedDayName);
                    tanggal = dateForDay.toString();
                    hari_berulang = dateForDay.getDayOfWeek().getValue();
                    System.out.println("Using day-based schedule: " + selectedDayName + " -> " + tanggal + " (hari_berulang: " + hari_berulang + ")");
                } else if (datepicker_input_tanggal.getValue() != null) {
                    // Use date picker (one-time schedule)
                    tanggal = datepicker_input_tanggal.getValue().toString();
                    hari_berulang = null;
                    System.out.println("Using date picker (one-time): " + tanggal);
                } else {
                    System.err.println("Error: Pilih tanggal atau hari!");
                    return;
                }
                
                // Get id_pelatih from pelatih name
                Integer id_pelatih = null;
                for (PelatihData p : pelatihDataList) {
                    if (p.toString().equals(namaPelatih)) {
                        id_pelatih = p.getId_pelatih();
                        break;
                    }
                }
                
                if (id_pelatih == null) {
                    System.err.println("Error: Pelatih tidak ditemukan!");
                    return;
                }
                
                // Get id_siswa from siswa name
                Integer id_siswa = null;
                for (SiswaData s : siswaDataList) {
                    if (s.toString().equals(namaSiswa)) {
                        id_siswa = s.getId_siswa();
                        break;
                    }
                }
                
                if (id_siswa == null) {
                    System.err.println("Error: Siswa tidak ditemukan!");
                    return;
                }
                
                // Build datetime strings
                int jam_mulai = spinner_start_hour.getValue();
                int menit_mulai = spinner_start_minute.getValue();
                String waktu_mulai = String.format("%02d:%02d:00", jam_mulai, menit_mulai);
                
                int jam_selesai = spinner_end_hour.getValue();
                int menit_selesai = spinner_end_minute.getValue();
                String waktu_selesai = String.format("%02d:%02d:00", jam_selesai, menit_selesai);
                
                // Update database
                String updateSQL = "UPDATE jadwal SET id_pelatih = ?, id_siswa = ?, tanggal = ?, waktu_mulai = ?, waktu_selesai = ?, hari_berulang = ? WHERE id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                     PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                    ps.setInt(1, id_pelatih);
                    ps.setInt(2, id_siswa);
                    ps.setString(3, tanggal);
                    ps.setString(4, waktu_mulai);
                    ps.setString(5, waktu_selesai);
                    if (hari_berulang != null) {
                        ps.setInt(6, hari_berulang);
                    } else {
                        ps.setNull(6, java.sql.Types.INTEGER);
                    }
                    ps.setInt(7, selectedJadwal.getId_jadwalInt());
                    ps.executeUpdate();
                    System.out.println("Jadwal berhasil diperbarui!");
                    
                    // Refresh table
                    readDataJadwalMengajar();
                    resetJadwalForm();
                }
            } catch (Exception er) {
                System.err.println("Error updating jadwal: ");
                er.printStackTrace();
            }
        }
    }

    public void setTanggalSekarangSetting(ActionEvent e){
        LocalDate today = LocalDate.now();
        datepicker_sort_daftar_absensi_setting.setValue(today);
        System.out.println("Set current date for setting: " + today);
    }
    
    public void readJadwalByDate(ActionEvent e){
        if (datepicker_sort_daftar_absensi_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal untuk filter!");
            return;
        }

        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // Custom cell factory for col_tanggal to display day name
        col_tanggal.setCellFactory(col -> {
            return new TableCell<jadwal_mengajar, String>() {
                @Override
                protected void updateItem(String tanggal, boolean empty) {
                    super.updateItem(tanggal, empty);
                    if (empty || tanggal == null) {
                        setText(null);
                    } else {
                        String dayName = getDayNameFromDate(tanggal);
                        setText(dayName + " (" + tanggal + ")");
                    }
                }
            };
        });

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        LocalDate selectedDate = datepicker_sort_daftar_absensi_setting.getValue();
        String selectedDateStr = selectedDate.toString();
        int selectedDayOfWeek = selectedDate.getDayOfWeek().getValue();
        
        // Load schedules that match the selected date or are repeating on this day of week
        String query = "SELECT id_jadwal, nama_pelatih, nama_siswa, tanggal, waktu_mulai, waktu_selesai, hari_berulang, kehadiran FROM jadwal, pelatih, siswa WHERE pelatih.id_pelatih = jadwal.id_pelatih AND siswa.id_siswa = jadwal.id_siswa AND (tanggal = ? OR hari_berulang = ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedDateStr);
            pstmt.setInt(2, selectedDayOfWeek);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer hari_berulang = rs.getObject(7) != null ? rs.getInt(7) : null;
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    hari_berulang,
                    rs.getString(8)
                ));
            }
            System.out.println("Loaded " + data.size() + " records for date: " + selectedDateStr);
        } catch (Exception er) {
            er.printStackTrace();
        }
        
        // Replace all tanggal with the selected date for repeating schedules
        ObservableList<jadwal_mengajar> adjustedData = FXCollections.observableArrayList();
        for (jadwal_mengajar schedule : data) {
            if (schedule.getHari_berulang() != null && schedule.getHari_berulang() > 0) {
                // For repeating schedules, show them on the selected date
                jadwal_mengajar adjustedSchedule = new jadwal_mengajar(
                    schedule.getId_jadwalInt(),
                    schedule.getNama_pelatih(),
                    schedule.getNama_siswa(),
                    selectedDateStr,
                    schedule.getWaktu_mulai(),
                    schedule.getWaktu_selesai(),
                    schedule.getHari_berulang(),
                    schedule.getKehadiran()
                );
                adjustedData.add(adjustedSchedule);
            } else {
                adjustedData.add(schedule);
            }
        }
        
        data.clear();
        data.addAll(adjustedData);
        
        table_jadwal_mengajar.setItems(data);
    }
    public void readJadwalByDateRange() {
        if (datepicker_dari_tanggal_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal dari!");
            return;
        }
        if (datepicker_sampai_tanggal_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal sampai!");
            return;
        }

        // Bind absensi columns (use the distinct column variables)
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // Custom cell factory for col_tanggal to display day name
        col_tanggal.setCellFactory(col -> {
            return new TableCell<jadwal_mengajar, String>() {
                @Override
                protected void updateItem(String tanggal, boolean empty) {
                    super.updateItem(tanggal, empty);
                    if (empty || tanggal == null) {
                        setText(null);
                    } else {
                        String dayName = getDayNameFromDate(tanggal);
                        setText(dayName + " (" + tanggal + ")");
                    }
                }
            };
        });

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        LocalDate dari_tanggal_obj = datepicker_dari_tanggal_setting.getValue();
        LocalDate sampai_tanggal_obj = datepicker_sampai_tanggal_setting.getValue();
        String dari_tanggal = dari_tanggal_obj.toString();
        String sampai_tanggal = sampai_tanggal_obj.toString();

        String query = "SELECT id_jadwal, nama_pelatih, nama_siswa, tanggal, waktu_mulai, waktu_selesai, hari_berulang, kehadiran FROM jadwal, siswa, pelatih WHERE pelatih.id_pelatih = jadwal.id_pelatih AND siswa.id_siswa = jadwal.id_siswa AND tanggal BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dari_tanggal);
            pstmt.setString(2, sampai_tanggal);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer hari_berulang = rs.getObject(7) != null ? rs.getInt(7) : null;
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    hari_berulang,
                    rs.getString(8)
                ));
            }
            System.out.println("Loaded " + data.size() + " pelatih records for date range: " + dari_tanggal + " to " + sampai_tanggal);
        } catch (Exception er) {
            er.printStackTrace();
        }
        
        // Expand repeating schedules within the date range
        expandRepeatingSchedules(dari_tanggal_obj, sampai_tanggal_obj);
        
        table_jadwal_mengajar.setItems(data);
    }

        public void showAllDataSetting(ActionEvent e){
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // Custom cell factory for col_tanggal to display day name
        col_tanggal.setCellFactory(col -> {
            return new TableCell<jadwal_mengajar, String>() {
                @Override
                protected void updateItem(String tanggal, boolean empty) {
                    super.updateItem(tanggal, empty);
                    if (empty || tanggal == null) {
                        setText(null);
                    } else {
                        String dayName = getDayNameFromDate(tanggal);
                        setText(dayName + " (" + tanggal + ")");
                    }
                }
            };
        });

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_jadwal, nama_pelatih, nama_siswa, tanggal, waktu_mulai, waktu_selesai, hari_berulang, kehadiran FROM jadwal, pelatih, siswa WHERE pelatih.id_pelatih = jadwal.id_pelatih AND siswa.id_siswa = jadwal.id_siswa ORDER BY tanggal ASC")) {
            while (rs.next()) {
                Integer hari_berulang = rs.getObject(7) != null ? rs.getInt(7) : null;
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    hari_berulang,
                    rs.getString(8)
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        
        // Expand repeating schedules for all data
        expandRepeatingSchedules(null, null);
        
        table_jadwal_mengajar.setItems(data);
    }
    public void resetSetting(ActionEvent e){
        datepicker_sort_daftar_absensi_setting.setValue(null);
        readDataJadwalMengajar();
        System.out.println("Setting direset! Menampilkan semua data absensi.");
    }


    public void readDataJadwalMengajar() {
        // bind property ke kolom
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // Custom cell factory for col_tanggal to display day name
        col_tanggal.setCellFactory(col -> {
            return new TableCell<jadwal_mengajar, String>() {
                @Override
                protected void updateItem(String tanggal, boolean empty) {
                    super.updateItem(tanggal, empty);
                    if (empty || tanggal == null) {
                        setText(null);
                    } else {
                        String dayName = getDayNameFromDate(tanggal);
                        setText(dayName + " (" + tanggal + ")");
                    }
                }
            };
        });

        // load data dari DB
        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("SELECT id_jadwal, nama_pelatih, nama_siswa, tanggal, waktu_mulai, waktu_selesai, kehadiran FROM jadwal, pelatih, siswa WHERE pelatih.id_pelatih = jadwal.id_pelatih AND siswa.id_siswa = jadwal.id_siswa AND tanggal >= date('now') ORDER BY tanggal ASC")) {
            while (rs.next()) {
                Integer hari_berulang = rs.getObject(7) != null ? rs.getInt(7) : null;
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    hari_berulang,
                    rs.getString(8)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Expand repeating schedules for future matching days
        expandRepeatingSchedules(null, null);
        
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
    
    
    private void initializeTimeSpinners() {
        // Initialize start time spinners
        spinner_start_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinner_start_minute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        
        // Initialize end time spinners
        spinner_end_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinner_end_minute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
    }
    
    private String getSelectedDayName() {
        if (input_senin.isSelected()) return "Senin";
        if (input_selasa.isSelected()) return "Selasa";
        if (input_rabu.isSelected()) return "Rabu";
        if (input_kamis.isSelected()) return "Kamis";
        if (input_jumat.isSelected()) return "Jumat";
        if (input_sabtu.isSelected()) return "Sabtu";
        if (input_minggu.isSelected()) return "Minggu";
        return null;
    }
    
    private String getDayNameFromDate(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString);
            switch(date.getDayOfWeek()) {
                case MONDAY: return "Senin";
                case TUESDAY: return "Selasa";
                case WEDNESDAY: return "Rabu";
                case THURSDAY: return "Kamis";
                case FRIDAY: return "Jumat";
                case SATURDAY: return "Sabtu";
                case SUNDAY: return "Minggu";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }
    
    private LocalDate calculateDateForDay(String dayName) {
        LocalDate today = LocalDate.now();
        int targetDayOfWeek = 0;
        
        switch(dayName) {
            case "Senin": targetDayOfWeek = 1; break;      // Monday
            case "Selasa": targetDayOfWeek = 2; break;     // Tuesday
            case "Rabu": targetDayOfWeek = 3; break;       // Wednesday
            case "Kamis": targetDayOfWeek = 4; break;      // Thursday
            case "Jumat": targetDayOfWeek = 5; break;      // Friday
            case "Sabtu": targetDayOfWeek = 6; break;      // Saturday
            case "Minggu": targetDayOfWeek = 7; break;     // Sunday
        }
        
        int currentDayOfWeek = today.getDayOfWeek().getValue();
        int daysAhead = targetDayOfWeek - currentDayOfWeek;
        
        if (daysAhead <= 0) {
            daysAhead += 7;
        }
        
        return today.plusDays(daysAhead);
    }
    
    private void expandRepeatingSchedules(LocalDate fromDate, LocalDate toDate) {
        // Expand repeating schedules (hari_berulang > 0) to future matching days
        // Generate entries for next 365 days from today
        ObservableList<jadwal_mengajar> expandedData = FXCollections.observableArrayList();
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(365);
        
        for (jadwal_mengajar schedule : data) {
            // Add the original record first (from database)
            expandedData.add(schedule);
            
            // If hari_berulang is set (> 0), generate entries for future matching days
            if (schedule.getHari_berulang() > 0) {
                int targetDayOfWeek = schedule.getHari_berulang();
                LocalDate scheduleDate = LocalDate.parse(schedule.getTanggal());
                
                // Generate for each matching day from tomorrow until endDate
                for (LocalDate checkDate = scheduleDate.plusDays(1); checkDate.isBefore(endDate); checkDate = checkDate.plusDays(1)) {
                    if (checkDate.getDayOfWeek().getValue() == targetDayOfWeek) {
                        // Create a new entry for this matching day
                        jadwal_mengajar expandedSchedule = new jadwal_mengajar(
                            schedule.getId_jadwalInt(),
                            schedule.getNama_pelatih(),
                            schedule.getNama_siswa(),
                            checkDate.toString(),
                            schedule.getWaktu_mulai(),
                            schedule.getWaktu_selesai(),
                            schedule.getHari_berulang(),
                            "TIDAK HADIR"  // Default to not attended for future occurrences
                        );
                        
                        // Only add if within requested date range (if filtering by date)
                        if (fromDate == null || toDate == null || 
                            (!checkDate.isBefore(fromDate) && !checkDate.isAfter(toDate))) {
                            expandedData.add(expandedSchedule);
                        }
                    }
                }
            }
        }
        
        // Replace data with expanded list
        data.clear();
        data.addAll(expandedData);
    }
    
    private void loadPelatihData() {
        pelatihDataList.clear();
        String query = "SELECT id_pelatih, nama_pelatih, nama_keahlian FROM pelatih";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PelatihData p = new PelatihData(
                    rs.getInt("id_pelatih"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_keahlian")
                );
                pelatihDataList.add(p);
                System.out.println("Added pelatih: " + p.toString());
            }
            System.out.println("Total pelatih loaded: " + pelatihDataList.size());
        } catch (Exception e) {
            System.err.println("Error loading pelatih data: ");
            e.printStackTrace();
        }
    }
    
    private void loadSiswaData() {
        siswaDataList.clear();
        String query = "SELECT s.id_siswa, s.nama_siswa, t.nama_tim FROM siswa s JOIN tim_siswa t ON s.id_tim = t.id_tim";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SiswaData sw = new SiswaData(
                    rs.getInt("id_siswa"),
                    rs.getString("nama_siswa"),
                    rs.getString("nama_tim")
                );
                siswaDataList.add(sw);
                System.out.println("Added siswa: " + sw.toString());
            }
            System.out.println("Total siswa loaded: " + siswaDataList.size());
        } catch (Exception e) {
            System.err.println("Error loading siswa data: ");
            e.printStackTrace();
        }
    }
    
    private void setupAutosuggestion() {
        textfield_input_nama_pelatih.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String input = textfield_input_nama_pelatih.getText().toLowerCase().trim();
                System.out.println("=== Pelatih Key Released ===> Input: '" + input + "', Total pelatih available: " + pelatihDataList.size());
                
                ObservableList<String> suggestions = FXCollections.observableArrayList();
                
                if (!input.isEmpty()) {
                    for (PelatihData pelatih : pelatihDataList) {
                        String namaPelatih = pelatih.getNama_pelatih();
                        String namaKeahlian = pelatih.getNama_keahlian();
                        if (namaPelatih != null && namaKeahlian != null) {
                            if (namaPelatih.toLowerCase().contains(input) ||
                                namaKeahlian.toLowerCase().contains(input)) {
                                suggestions.add(pelatih.toString());
                                System.out.println("  -> Match: " + pelatih.toString());
                            }
                        }
                    }
                    System.out.println("Total suggestions: " + suggestions.size());
                    combobox_input_nama_pelatih.setItems(suggestions);
                    if (!suggestions.isEmpty()) {
                        combobox_input_nama_pelatih.show();
                        System.out.println("ComboBox shown");
                    }
                } else {
                    combobox_input_nama_pelatih.setItems(FXCollections.observableArrayList());
                }
            }
        });
        
        combobox_input_nama_pelatih.setOnAction(event -> {
            String selected = combobox_input_nama_pelatih.getValue();
            System.out.println("Pelatih ComboBox selection: " + selected);
            if (selected != null) {
                textfield_input_nama_pelatih.setText(selected);
                combobox_input_nama_pelatih.hide();
            }
        });
        
        // Siswa autosuggestion
        textfield_input_nama_siswa.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String input = textfield_input_nama_siswa.getText().toLowerCase().trim();
                System.out.println("=== Siswa Key Released ===> Input: '" + input + "', Total siswa available: " + siswaDataList.size());
                
                ObservableList<String> suggestions = FXCollections.observableArrayList();
                
                if (!input.isEmpty()) {
                    for (SiswaData siswa : siswaDataList) {
                        String namaSiswa = siswa.getNama_siswa();
                        String namaTim = siswa.getNama_tim();
                        if (namaSiswa != null && namaTim != null) {
                            if (namaSiswa.toLowerCase().contains(input) ||
                                namaTim.toLowerCase().contains(input)) {
                                suggestions.add(siswa.toString());
                                System.out.println("  -> Match: " + siswa.toString());
                            }
                        }
                    }
                    System.out.println("Total suggestions: " + suggestions.size());
                    combobox_input_nama_siswa.setItems(suggestions);
                    if (!suggestions.isEmpty()) {
                        combobox_input_nama_siswa.show();
                        System.out.println("Siswa ComboBox shown");
                    }
                } else {
                    combobox_input_nama_siswa.setItems(FXCollections.observableArrayList());
                }
            }
        });
        
        combobox_input_nama_siswa.setOnAction(event -> {
            String selected = combobox_input_nama_siswa.getValue();
            System.out.println("Siswa ComboBox selection: " + selected);
            if (selected != null) {
                textfield_input_nama_siswa.setText(selected);
                combobox_input_nama_siswa.hide();
            }
        });
    }
    
    public void submitJadwalForm() {
        try {
            // Validate inputs
            String namaPelatih = textfield_input_nama_pelatih.getText().trim();
            String namaSiswa = textfield_input_nama_siswa.getText().trim();
            
            if (namaPelatih.isEmpty() || namaSiswa.isEmpty()) {
                System.err.println("Error: Nama pelatih dan siswa harus diisi!");
                return;
            }
            
            // Check if a day radio button is selected or datepicker is filled
            String selectedDayName = getSelectedDayName();
            String tanggal;
            Integer hari_berulang = null;
            
            if (selectedDayName != null) {
                // Use day-based scheduling
                LocalDate dateForDay = calculateDateForDay(selectedDayName);
                tanggal = dateForDay.toString();
                hari_berulang = dateForDay.getDayOfWeek().getValue();
                System.out.println("Using day-based schedule: " + selectedDayName + " -> " + tanggal + " (hari_berulang: " + hari_berulang + ")");
            } else if (datepicker_input_tanggal.getValue() != null) {
                // Use date picker (one-time schedule)
                tanggal = datepicker_input_tanggal.getValue().toString();
                hari_berulang = null;
                System.out.println("Using date picker (one-time): " + tanggal);
            } else {
                System.err.println("Error: Pilih tanggal atau hari!");
                return;
            }
            
            // Get id_pelatih from pelatih name
            Integer id_pelatih = null;
            for (PelatihData p : pelatihDataList) {
                if (p.toString().equals(namaPelatih)) {
                    id_pelatih = p.getId_pelatih();
                    break;
                }
            }
            
            if (id_pelatih == null) {
                System.err.println("Error: Pelatih tidak ditemukan!");
                return;
            }
            
            // Get id_siswa from siswa name
            Integer id_siswa = null;
            for (SiswaData s : siswaDataList) {
                if (s.toString().equals(namaSiswa)) {
                    id_siswa = s.getId_siswa();
                    break;
                }
            }
            
            if (id_siswa == null) {
                System.err.println("Error: Siswa tidak ditemukan!");
                return;
            }
            
            // Build datetime strings
            int jam_mulai = spinner_start_hour.getValue();
            int menit_mulai = spinner_start_minute.getValue();
            String waktu_mulai = String.format("%02d:%02d:00", jam_mulai, menit_mulai);
            
            int jam_selesai = spinner_end_hour.getValue();
            int menit_selesai = spinner_end_minute.getValue();
            String waktu_selesai = String.format("%02d:%02d:00", jam_selesai, menit_selesai);
            
            // Insert into database
            String insertSQL = "INSERT INTO jadwal (id_pelatih, id_siswa, tanggal, waktu_mulai, waktu_selesai, hari_berulang, kehadiran) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, id_pelatih);
                ps.setInt(2, id_siswa);
                ps.setString(3, tanggal);
                ps.setString(4, waktu_mulai);
                ps.setString(5, waktu_selesai);
                if (hari_berulang != null) {
                    ps.setInt(6, hari_berulang);
                } else {
                    ps.setNull(6, java.sql.Types.INTEGER);
                }
                ps.setString(7, "TIDAK HADIR");
                ps.executeUpdate();
                System.out.println("Jadwal berhasil ditambahkan!");
                
                // Refresh table
                readDataJadwalMengajar();
                resetJadwalForm();
            }
        } catch (Exception e) {
            System.err.println("Error submitting form: ");
            e.printStackTrace();
        }
    }
    
    public void resetJadwalForm() {
        textfield_input_nama_pelatih.clear();
        textfield_input_nama_siswa.clear();
        combobox_input_nama_pelatih.getItems().clear();
        combobox_input_nama_siswa.getItems().clear();
        datepicker_input_tanggal.setValue(null);
        spinner_start_hour.getValueFactory().setValue(0);
        spinner_start_minute.getValueFactory().setValue(0);
        spinner_end_hour.getValueFactory().setValue(0);
        spinner_end_minute.getValueFactory().setValue(0);
        System.out.println("Form reset!");
    }
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initializeTimeSpinners();
        loadPelatihData();
        loadSiswaData();
        setupAutosuggestion();
        readDataJadwalMengajar();
        disableDatePickerRadio();
    }
}