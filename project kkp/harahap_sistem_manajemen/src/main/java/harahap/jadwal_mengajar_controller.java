package harahap;
import harahap.scene_switcher;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
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
    @FXML private TableColumn<jadwal_mengajar, Integer> col_nama_siswa;
    @FXML private TableColumn<jadwal_mengajar, Integer> col_nama_pelatih;
    @FXML private TableColumn<jadwal_mengajar, String> col_tanggal_waktu_awal;
    @FXML private TableColumn<jadwal_mengajar, String> col_tanggal_waktu_akhir;
    @FXML private TableColumn<jadwal_mengajar, String> col_kehadiran;
    @FXML private DatePicker datepicker_input_waktu_mulai;
    @FXML private DatePicker datepicker_input_waktu_selesai;
    @FXML private Spinner<Integer> spinner_start_hour;
    @FXML private Spinner<Integer> spinner_start_minute;
    @FXML private Spinner<Integer> spinner_end_hour;
    @FXML private Spinner<Integer> spinner_end_minute;
    @FXML private ComboBox<String> combobox_input_nama_pelatih;
    @FXML private ComboBox<String> combobox_input_nama_siswa;
    @FXML private TextField textfield_input_nama_pelatih;
    @FXML private TextField textfield_input_nama_siswa;
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

    public void edit_kehadiran(){
    }

    public void readDataJadwalMengajar() {
        // bind property ke kolom
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_siswa.setCellValueFactory(new PropertyValueFactory<>("nama_siswa"));
        col_tanggal_waktu_awal.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu_awal"));
        col_tanggal_waktu_akhir.setCellValueFactory(new PropertyValueFactory<>("tanggal_waktu_akhir"));
        col_kehadiran.setCellValueFactory(new PropertyValueFactory<>("kehadiran"));

        // load data dari DB
        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("select jadwal.id_jadwal, pelatih.nama_pelatih, siswa.nama_siswa, jadwal.tanggal_waktu_akhir, jadwal.tanggal_waktu_akhir, jadwal.kehadiran from jadwal, pelatih, siswa where jadwal.id_pelatih = pelatih.id_pelatih AND siswa.id_siswa = jadwal.id_siswa")) {
            while (rs.next()) {
                data.add(new jadwal_mengajar(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
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
        initializeTimeSpinners();
        loadPelatihData();
        loadSiswaData();
        setupAutosuggestion();
        readDataJadwalMengajar();
    }
    
    private void initializeTimeSpinners() {
        // Initialize start time spinners
        spinner_start_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinner_start_minute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        
        // Initialize end time spinners
        spinner_end_hour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        spinner_end_minute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
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
            
            if (datepicker_input_waktu_mulai.getValue() == null || datepicker_input_waktu_selesai.getValue() == null) {
                System.err.println("Error: Tanggal harus dipilih!");
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
            String tanggal_mulai = datepicker_input_waktu_mulai.getValue().toString();
            int jam_mulai = spinner_start_hour.getValue();
            int menit_mulai = spinner_start_minute.getValue();
            String tanggal_waktu_awal = tanggal_mulai + " " + String.format("%02d:%02d:00", jam_mulai, menit_mulai);
            
            String tanggal_selesai = datepicker_input_waktu_selesai.getValue().toString();
            int jam_selesai = spinner_end_hour.getValue();
            int menit_selesai = spinner_end_minute.getValue();
            String tanggal_waktu_akhir = tanggal_selesai + " " + String.format("%02d:%02d:00", jam_selesai, menit_selesai);
            
            // Insert into database
            String insertSQL = "INSERT INTO jadwal (id_pelatih, id_siswa, tanggal_waktu_awal, tanggal_waktu_akhir, kehadiran) VALUES (?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, id_pelatih);
                ps.setInt(2, id_siswa);
                ps.setString(3, tanggal_waktu_awal);
                ps.setString(4, tanggal_waktu_akhir);
                ps.setString(5, "TIDAK HADIR");
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
        datepicker_input_waktu_mulai.setValue(null);
        datepicker_input_waktu_selesai.setValue(null);
        spinner_start_hour.getValueFactory().setValue(0);
        spinner_start_minute.getValueFactory().setValue(0);
        spinner_end_hour.getValueFactory().setValue(0);
        spinner_end_minute.getValueFactory().setValue(0);
        System.out.println("Form reset!");
    }
}