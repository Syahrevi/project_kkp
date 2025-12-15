package harahap;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;
import java.util.Locale;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;

public class jadwal_mengajar_controller implements Initializable {

    @FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;
    @FXML private Button button_crud_absensi;

    @FXML private TableView<jadwal_mengajar> table_jadwal_mengajar;
    @FXML private TableColumn<jadwal_mengajar, Integer> col_id_jadwal;
    @FXML private TableColumn<jadwal_mengajar, String> col_nama_pelatih;
    @FXML private TableColumn<jadwal_mengajar, String> col_nama_tim;
    @FXML private TableColumn<jadwal_mengajar, String> col_nama_kelas;
    @FXML private TableColumn<jadwal_mengajar, String> col_tanggal;
    @FXML private TableColumn<jadwal_mengajar, String> col_waktu_mulai;
    @FXML private TableColumn<jadwal_mengajar, String> col_waktu_selesai;
    @FXML private TableColumn<jadwal_mengajar, String> col_kehadiran_pelatih; // refactored
    @FXML private TableColumn<jadwal_mengajar, String> col_kehadiran_siswa;   // new

    @FXML private DatePicker datepicker_input_tanggal;
    @FXML private Spinner<Integer> spinner_start_hour;
    @FXML private Spinner<Integer> spinner_start_minute;
    @FXML private Spinner<Integer> spinner_end_hour;
    @FXML private Spinner<Integer> spinner_end_minute;
    @FXML private ComboBox<String> combobox_input_nama_pelatih;
    @FXML private ComboBox<TimData> combobox_input_nama_tim;
    // @FXML private TextField textfield_input_nama_pelatih;
    @FXML private TextField textfield_input_nama_siswa;
    @FXML private ComboBox<KelasData> combobox_kelas;

    @FXML private ToggleGroup progresifAtauTransisi;
    @FXML private RadioButton radio_semiProgresif;
    @FXML private RadioButton radio_transisi;

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
    ObservableList<TimData> timDataList = FXCollections.observableArrayList();
    ObservableList<KelasData> kelasDataList = FXCollections.observableArrayList();

    private static final String DB_URL = "jdbc:sqlite:database/harahap.db";

    // --- Scene switches ---
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

    // --- Initialization ---
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTimeSpinners();
        disableDatePickerRadio();
        loadPelatihData();
        loadSiswaData();
        loadTimData();
        loadKelasData();

        // When user picks a kelas, clear radio selection (do not mutate items)
        if (combobox_kelas != null) {
            combobox_kelas.valueProperty().addListener((obs, oldV, newV) -> {
                if (newV != null && progresifAtauTransisi != null) {
                    progresifAtauTransisi.selectToggle(null);
                }
            });
        }

        // If radios are clicked, ensure combobox is cleared
        if (radio_semiProgresif != null) {
            radio_semiProgresif.setOnAction(a -> {
                if (radio_semiProgresif.isSelected() && combobox_kelas != null) combobox_kelas.getSelectionModel().clearSelection();
            });
        }
        if (radio_transisi != null) {
            radio_transisi.setOnAction(a -> {
                if (radio_transisi.isSelected() && combobox_kelas != null) combobox_kelas.getSelectionModel().clearSelection();
            });
        }

        readDataJadwalMengajar();
        table_jadwal_mengajar.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) populateFormFromSelected(newSelection);
            }
        );
    }
    //HELPER METHODS=========================================================
    private void populateFormFromSelected(jadwal_mengajar schedule) {

        /* =========================
        Pelatih
        ========================= */
        if (schedule.getNama_pelatih() != null) {
            for (PelatihData p : pelatihDataList) {
                String display =
                    p.getNama_pelatih() +
                    (p.getNama_keahlian() != null
                        ? " (" + p.getNama_keahlian() + ")"
                        : "");

                if (p.getNama_pelatih()
                        .equalsIgnoreCase(schedule.getNama_pelatih())) {
                    combobox_input_nama_pelatih.setValue(display);
                    break;
                }
            }
        }

        /* =========================
        Tim
        ========================= */
        if (schedule.getNama_tim() != null) {
            for (TimData t : timDataList) {
                if (schedule.getNama_tim().equals(t.getNama_tim())) {
                    combobox_input_nama_tim.getSelectionModel().select(t);
                    break;
                }
            }
        } else {
            combobox_input_nama_tim.setValue(null);
        }

        /* =========================
        Kelas
        ========================= */
        if (schedule.getNama_kelas() != null) {
            for (KelasData k : kelasDataList) {
                if (schedule.getNama_kelas().equals(k.getNama_kelas())) {
                    combobox_kelas.getSelectionModel().select(k);
                    break;
                }
            }
        } else {
            combobox_kelas.setValue(null);
        }

        /* =========================
        CLEAR DAY SELECTION
        ========================= */
        if (hari != null) {
            hari.selectToggle(null);
        }

        /* =========================
        HARI BERULANG (PRIMARY)
        ========================= */
        if (schedule.getHari_berulang() != null) {
            switch (schedule.getHari_berulang().toString().toUpperCase()) {
                case "1", "SENIN"  -> input_senin.setSelected(true);
                case "2", "SELASA" -> input_selasa.setSelected(true);
                case "3", "RABU"   -> input_rabu.setSelected(true);
                case "4", "KAMIS"  -> input_kamis.setSelected(true);
                case "5", "JUMAT"  -> input_jumat.setSelected(true);
                case "6", "SABTU"  -> input_sabtu.setSelected(true);
                case "7", "MINGGU" -> input_minggu.setSelected(true);
            }
        }

        /* =========================
        TANGGAL (SECONDARY)
        ========================= */
        try {
            if (schedule.getTanggal() != null) {
                datepicker_input_tanggal
                        .setValue(LocalDate.parse(schedule.getTanggal()));
            } else {
                datepicker_input_tanggal.setValue(null);
            }
        } catch (Exception e) {
            datepicker_input_tanggal.setValue(null);
        }

        /* =========================
        WAKTU
        ========================= */
        try {
            LocalTime start = LocalTime.parse(schedule.getWaktu_mulai());
            spinner_start_hour.getValueFactory().setValue(start.getHour());
            spinner_start_minute.getValueFactory().setValue(start.getMinute());
        } catch (Exception ignored) {}

        try {
            LocalTime end = LocalTime.parse(schedule.getWaktu_selesai());
            spinner_end_hour.getValueFactory().setValue(end.getHour());
            spinner_end_minute.getValueFactory().setValue(end.getMinute());
        } catch (Exception ignored) {}
    }
    private void updateAttendanceAndRecompute(int idJadwal, String newKehadiranPelatih, String newKehadiranSiswa, jadwal_mengajar row) {
        final String kp = newKehadiranPelatih == null ? null : newKehadiranPelatih.trim().toUpperCase();
        final String ks = newKehadiranSiswa == null ? null : newKehadiranSiswa.trim().toUpperCase();
        final String oldKp = row != null ? row.getKehadiran_pelatih() : null;
        final String oldKs = row != null ? row.getKehadiran_siswa() : null;

        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "UPDATE jadwal SET kehadiran_pelatih = ?, kehadiran_siswa = ? WHERE id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, kp);
                    ps.setString(2, ks);
                    ps.setInt(3, idJadwal);
                    ps.executeUpdate();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                // recompute authoritative totals and persist them
                recomputeAndPersistTotalForJadwal(idJadwal);

                // refresh UI on FX thread after recompute started
                Platform.runLater(() -> {
                    // update row model (already optimistic but ensure normalized)
                    if (row != null) {
                        row.setKehadiran_pelatih(kp);
                        row.setKehadiran_siswa(ks);
                    }
                    table_jadwal_mengajar.refresh();
                    if (laporan_gaji_controller.INSTANCE != null) {
                        laporan_gaji_controller.INSTANCE.loadGajiPerJadwal();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiHariIni();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiBulan();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiTahun();
                    }
                });
            }

            @Override
            protected void failed() {
                // revert optimistic UI change
                Platform.runLater(() -> {
                    if (row != null) {
                        row.setKehadiran_pelatih(oldKp);
                        row.setKehadiran_siswa(oldKs);
                    }
                    table_jadwal_mengajar.refresh();
                });
                getException().printStackTrace();
            }
        };
        new Thread(t).start();
    }
    
    private void recomputeAndPersistTotalForJadwal(int idJadwal) {
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String q = "SELECT j.waktu_mulai, j.waktu_selesai, j.kehadiran_pelatih, j.kehadiran_siswa, "
                        + "p.honor, lb.harga "
                        + "FROM jadwal j "
                        + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
                        + "LEFT JOIN list_biaya lb ON lb.id_kelas = j.id_kelas AND lb.id_nama_grade_keahlian = p.id_grade_keahlian "
                        + "WHERE j.id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = conn.prepareStatement(q)) {
                    ps.setInt(1, idJadwal);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) return null;
                        String waktuMulai = rs.getString("waktu_mulai");
                        String waktuSelesai = rs.getString("waktu_selesai");
                        String kehadiranPelatih = rs.getString("kehadiran_pelatih");
                        String kehadiranSiswa = rs.getString("kehadiran_siswa");
                        String honor = rs.getString("honor");
                        int harga = rs.getObject("harga") != null ? rs.getInt("harga") : 0;

                        java.time.format.DateTimeFormatter[] timeParsers = new java.time.format.DateTimeFormatter[] {
                            java.time.format.DateTimeFormatter.ofPattern("H:mm:ss"),
                            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"),
                            java.time.format.DateTimeFormatter.ofPattern("H:mm"),
                            java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        };

                        java.time.LocalTime start = null, end = null;
                        for (java.time.format.DateTimeFormatter fmt : timeParsers) {
                            try { if (start == null && waktuMulai != null) start = java.time.LocalTime.parse(waktuMulai, fmt); } catch (Exception ex) {}
                            try { if (end == null && waktuSelesai != null) end = java.time.LocalTime.parse(waktuSelesai, fmt); } catch (Exception ex) {}
                        }

                        double durationHours = 0.0;
                        if (start != null && end != null) {
                            java.time.Duration dur = java.time.Duration.between(start, end);
                            if (dur.isNegative() || dur.isZero()) dur = java.time.Duration.between(start, end.plusHours(24));
                            durationHours = dur.toMinutes() / 60.0;
                        }
                        int fullHours = (int) Math.floor(durationHours);

                        double raw = fullHours * (double) harga;
                        boolean honorYes = honor != null && honor.equalsIgnoreCase("ya");
                        double total = honorYes ? raw * 0.5 : raw;

                        // normalize and interpret siswa absence: treat TIDAK_HADIR or ALFA as "siswa tidak hadir"
                        boolean siswaTidakHadir = kehadiranSiswa != null &&
                            (kehadiranSiswa.equalsIgnoreCase("TIDAK_HADIR") || kehadiranSiswa.equalsIgnoreCase("ALFA"));

                        // Pelatih ALFA => zero (highest precedence)
                        if (kehadiranPelatih != null && kehadiranPelatih.equalsIgnoreCase("ALFA")) {
                            total = 0.0;
                        }
                        // Override rule: siswa tidak hadir but pelatih hadir => fixed 25000
                        else if (siswaTidakHadir && "HADIR".equalsIgnoreCase(kehadiranPelatih)) {
                            total = 25000.0;
                        }

                        int totalRounded = (int) Math.round(total);

                        // persist fullHours into total_jam (not rounded fractional hours)
                        String up = "UPDATE jadwal SET total_gaji = ?, total_jam = ? WHERE id_jadwal = ?";
                        try (PreparedStatement ups = conn.prepareStatement(up)) {
                            ups.setInt(1, totalRounded);
                            ups.setInt(2, fullHours);
                            ups.setInt(3, idJadwal);
                            ups.executeUpdate();
}
                    }
                }
                return null;
            }
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    readDataJadwalMengajar();
                    if (laporan_gaji_controller.INSTANCE != null) {
                        laporan_gaji_controller.INSTANCE.loadGajiPerJadwal();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiHariIni();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiBulan();
                        laporan_gaji_controller.INSTANCE.readDataLaporanGajiTahun();
                    }
                });
            }

            @Override
            protected void failed() {
                getException().printStackTrace();
            }
        };
        new Thread(t).start();
    }

    // Load pelatih list into pelatihDataList and combobox_input_nama_pelatih
    private void loadPelatihData() {
        pelatihDataList.clear();
        String sql = "SELECT p.id_pelatih, p.nama_pelatih, g.nama_grade_keahlian AS nama_keahlian "
                + "FROM pelatih p "
                + "LEFT JOIN grade_keahlian g ON p.id_grade_keahlian = g.id_grade_keahlian "
                + "ORDER BY p.nama_pelatih ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                pelatihDataList.add(new PelatihData(
                    rs.getInt("id_pelatih"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_keahlian") // may be null
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // populate combobox_input_nama_pelatih if present (strings used elsewhere in your code)
        if (combobox_input_nama_pelatih != null) {
            ObservableList<String> items = FXCollections.observableArrayList();
            for (PelatihData p : pelatihDataList) {
                items.add(
                    p.getNama_pelatih() +
                    (p.getNama_keahlian() != null
                        ? " (" + p.getNama_keahlian() + ")"
                        : "")
                );
            }
        combobox_input_nama_pelatih.setItems(items);
        }
    }

// Load siswa list into siswaDataList (and optionally a combobox if you add one)
private void loadSiswaData() {
    siswaDataList.clear();
    String sql = "SELECT s.id_siswa, s.nama_siswa, t.nama_tim "
               + "FROM siswa s "
               + "LEFT JOIN tim_siswa t ON s.id_tim = t.id_tim "
               + "ORDER BY s.nama_siswa ASC";
    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            siswaDataList.add(new SiswaData(
                rs.getInt("id_siswa"),
                rs.getString("nama_siswa"),
                rs.getString("nama_tim") // may be null
            ));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    // If you later add a combobox for siswa, set its items here.
}

private void loadTimData() {
    timDataList.clear();
    String sql = "SELECT id_tim, nama_tim FROM tim_siswa ORDER BY nama_tim ASC";
    try (Connection conn = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            timDataList.add(new TimData(
                rs.getInt("id_tim"),
                rs.getString("nama_tim")
            ));
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    if (combobox_input_nama_tim != null) combobox_input_nama_tim.setItems(timDataList);
}


    private void loadKelasData() {
        kelasDataList.clear();
        String sql = "SELECT id_kelas, nama_kelas FROM kelas ORDER BY nama_kelas ASC";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                kelasDataList.add(new KelasData(rs.getInt("id_kelas"), rs.getString("nama_kelas")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (combobox_kelas != null) combobox_kelas.setItems(kelasDataList);
    }

    // --- Disable DatePicker when radio selected ---
    public void disableDatePickerRadio() {
        if (hari == null || datepicker_input_tanggal == null) return;
        hari.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean anySelected = newToggle != null;
            datepicker_input_tanggal.setDisable(anySelected);
            if (anySelected) {
                String selectedDayName = getSelectedDayName();
                if (selectedDayName != null) {
                    LocalDate dateForDay = calculateDateForDay(selectedDayName);
                    String displayText = selectedDayName + " (" + dateForDay.toString() + ")";
                    System.out.println("Selected: " + displayText);
                }
            }
        });

        input_batal.setOnAction(e -> {
            hari.selectToggle(null);
            datepicker_input_tanggal.setDisable(false);
        });
    }

    // --- Read all upcoming jadwal (joins tim_siswa, kelas) ---
    public void readDataJadwalMengajar() {
        // set up columns
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_nama_kelas.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran_pelatih.setCellValueFactory(new PropertyValueFactory<>("kehadiran_pelatih"));
        col_kehadiran_siswa.setCellValueFactory(new PropertyValueFactory<>("kehadiran_siswa"));

        // custom tanggal cell to show day name
        col_tanggal.setCellFactory(col -> new TableCell<jadwal_mengajar, String>() {
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
        });

        data.clear();
        String sql = "SELECT j.id_jadwal, p.nama_pelatih, t.nama_tim, k.nama_kelas, "
                   + "j.tanggal, j.waktu_mulai, j.waktu_selesai, j.hari_berulang, "
                   + "j.kehadiran_pelatih, j.kehadiran_siswa "
                   + "FROM jadwal j "
                   + "JOIN pelatih p ON p.id_pelatih = j.id_pelatih "
                   + "LEFT JOIN tim_siswa t ON t.id_tim = j.id_tim "
                   + "LEFT JOIN kelas k ON k.id_kelas = j.id_kelas "
                //    + "WHERE j.tanggal >= date('now') "
                   + "ORDER BY j.tanggal desc";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Integer hari_berulang = rs.getObject("hari_berulang") != null ? rs.getInt("hari_berulang") : null;
                String kp = rs.getString("kehadiran_pelatih");
                String ks = rs.getString("kehadiran_siswa");
                kp = kp == null ? "ALFA" : kp;
                ks = ks == null ? "ALFA" : ks;

                data.add(new jadwal_mengajar(
                    rs.getInt("id_jadwal"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_tim"),
                    rs.getString("nama_kelas"),
                    rs.getString("tanggal"),
                    rs.getString("waktu_mulai"),
                    rs.getString("waktu_selesai"),
                    hari_berulang,
                    kp,
                    ks
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Expand repeating schedules for future matching days
        expandRepeatingSchedules(null, null);

        table_jadwal_mengajar.setItems(data);

        // Setup clickable cell factories for both kehadiran columns
        setupKehadiranPelatihCellFactory();
        setupKehadiranSiswaCellFactory();
    }

    // --- Read jadwal for a single date (joins tim_siswa) ---
    public void readJadwalByDate(ActionEvent e){
        if (datepicker_sort_daftar_absensi_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal untuk filter!");
            return;
        }

        // set columns (same as above)
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_nama_kelas.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran_pelatih.setCellValueFactory(new PropertyValueFactory<>("kehadiran_pelatih"));
        col_kehadiran_siswa.setCellValueFactory(new PropertyValueFactory<>("kehadiran_siswa"));

        col_tanggal.setCellFactory(col -> new TableCell<jadwal_mengajar, String>() {
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
        });

        data.clear();
        LocalDate selectedDate = datepicker_sort_daftar_absensi_setting.getValue();
        String selectedDateStr = selectedDate.toString();
        int selectedDayOfWeek = selectedDate.getDayOfWeek().getValue();

        String query = "SELECT j.id_jadwal, p.nama_pelatih, t.nama_tim, k.nama_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, j.hari_berulang, j.kehadiran_pelatih, j.kehadiran_siswa "
                     + "FROM jadwal j "
                     + "JOIN pelatih p ON p.id_pelatih = j.id_pelatih "
                     + "LEFT JOIN tim_siswa t ON t.id_tim = j.id_tim "
                     + "LEFT JOIN kelas k ON k.id_kelas = j.id_kelas "
                     + "WHERE (j.tanggal = ? OR j.hari_berulang = ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedDateStr);
            pstmt.setInt(2, selectedDayOfWeek);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer hari_berulang = rs.getObject("hari_berulang") != null ? rs.getInt("hari_berulang") : null;
                String kp = rs.getString("kehadiran_pelatih");
                String ks = rs.getString("kehadiran_siswa");
                kp = kp == null ? "ALFA" : kp;
                ks = ks == null ? "ALFA" : ks;

                data.add(new jadwal_mengajar(
                    rs.getInt("id_jadwal"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_tim"),
                    rs.getString("nama_kelas"),
                    rs.getString("tanggal"),
                    rs.getString("waktu_mulai"),
                    rs.getString("waktu_selesai"),
                    hari_berulang,
                    kp,
                    ks
                ));
            }
            System.out.println("Loaded " + data.size() + " records for date: " + selectedDateStr);
        } catch (Exception er) {
            er.printStackTrace();
        }

        // Replace tanggal for repeating schedules to show on selected date
        ObservableList<jadwal_mengajar> adjustedData = FXCollections.observableArrayList();
        for (jadwal_mengajar schedule : data) {
            if (schedule.getHari_berulang() != null && schedule.getHari_berulang() > 0) {
                jadwal_mengajar adjustedSchedule = new jadwal_mengajar(
                    schedule.getId_jadwalInt(),
                    schedule.getNama_pelatih(),
                    schedule.getNama_tim(),
                    schedule.getNama_kelas(),
                    selectedDateStr,
                    schedule.getWaktu_mulai(),
                    schedule.getWaktu_selesai(),
                    schedule.getHari_berulang(),
                    schedule.getKehadiran_pelatih(),
                    schedule.getKehadiran_siswa()
                );
                adjustedData.add(adjustedSchedule);
            } else {
                adjustedData.add(schedule);
            }
        }

        data.clear();
        data.addAll(adjustedData);
        table_jadwal_mengajar.setItems(data);

        setupKehadiranPelatihCellFactory();
        setupKehadiranSiswaCellFactory();
    }

    // --- Read jadwal in a date range (joins tim_siswa) ---
    public void readJadwalByDateRange() {
        if (datepicker_dari_tanggal_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal dari!");
            return;
        }
        if (datepicker_sampai_tanggal_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal sampai!");
            return;
        }

        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_nama_kelas.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran_pelatih.setCellValueFactory(new PropertyValueFactory<>("kehadiran_pelatih"));
        col_kehadiran_siswa.setCellValueFactory(new PropertyValueFactory<>("kehadiran_siswa"));

        col_tanggal.setCellFactory(col -> new TableCell<jadwal_mengajar, String>() {
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
        });

        data.clear();
        LocalDate dari_tanggal_obj = datepicker_dari_tanggal_setting.getValue();
        LocalDate sampai_tanggal_obj = datepicker_sampai_tanggal_setting.getValue();
        String dari_tanggal = dari_tanggal_obj.toString();
        String sampai_tanggal = sampai_tanggal_obj.toString();

        String query = "SELECT j.id_jadwal, p.nama_pelatih, t.nama_tim, k.nama_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, j.hari_berulang, j.kehadiran_pelatih, j.kehadiran_siswa "
                     + "FROM jadwal j "
                     + "JOIN pelatih p ON p.id_pelatih = j.id_pelatih "
                     + "LEFT JOIN tim_siswa t ON t.id_tim = j.id_tim "
                     + "LEFT JOIN kelas k ON k.id_kelas = j.id_kelas "
                     + "WHERE j.tanggal BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, dari_tanggal);
            pstmt.setString(2, sampai_tanggal);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Integer hari_berulang = rs.getObject("hari_berulang") != null ? rs.getInt("hari_berulang") : null;
                String kp = rs.getString("kehadiran_pelatih");
                String ks = rs.getString("kehadiran_siswa");
                kp = kp == null ? "ALFA" : kp;
                ks = ks == null ? "ALFA" : ks;

                data.add(new jadwal_mengajar(
                    rs.getInt("id_jadwal"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_tim"),
                    rs.getString("nama_kelas"),
                    rs.getString("tanggal"),
                    rs.getString("waktu_mulai"),
                    rs.getString("waktu_selesai"),
                    hari_berulang,
                    kp,
                    ks
                ));
            }
            System.out.println("Loaded " + data.size() + " records for date range: " + dari_tanggal + " to " + sampai_tanggal);
        } catch (Exception er) {
            er.printStackTrace();
        }

        // Expand repeating schedules within the date range
        expandRepeatingSchedules(dari_tanggal_obj, sampai_tanggal_obj);

        table_jadwal_mengajar.setItems(data);
        setupKehadiranPelatihCellFactory();
        setupKehadiranSiswaCellFactory();
    }

    // --- Show all data (joins tim_siswa) ---
    public void showAllDataSetting(ActionEvent e){
        col_id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_nama_tim.setCellValueFactory(new PropertyValueFactory<>("nama_tim"));
        col_nama_kelas.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_waktu_mulai.setCellValueFactory(new PropertyValueFactory<>("waktu_mulai"));
        col_waktu_selesai.setCellValueFactory(new PropertyValueFactory<>("waktu_selesai"));
        col_kehadiran_pelatih.setCellValueFactory(new PropertyValueFactory<>("kehadiran_pelatih"));
        col_kehadiran_siswa.setCellValueFactory(new PropertyValueFactory<>("kehadiran_siswa"));

        col_tanggal.setCellFactory(col -> new TableCell<jadwal_mengajar, String>() {
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
        });

        data.clear();
        String sql = "SELECT j.id_jadwal, p.nama_pelatih, t.nama_tim, k.nama_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, j.hari_berulang, j.kehadiran_pelatih, j.kehadiran_siswa "
                   + "FROM jadwal j "
                   + "JOIN pelatih p ON p.id_pelatih = j.id_pelatih "
                   + "LEFT JOIN tim_siswa t ON t.id_tim = j.id_tim "
                   + "LEFT JOIN kelas k ON k.id_kelas = j.id_kelas "
                   + "ORDER BY j.tanggal Desc";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             ResultSet rs = conn.createStatement().executeQuery(sql)) {
            while (rs.next()) {
                Integer hari_berulang = rs.getObject("hari_berulang") != null ? rs.getInt("hari_berulang") : null;
                String kp = rs.getString("kehadiran_pelatih");
                String ks = rs.getString("kehadiran_siswa");
                kp = kp == null ? "ALFA" : kp;
                ks = ks == null ? "ALFA" : ks;

                data.add(new jadwal_mengajar(
                    rs.getInt("id_jadwal"),
                    rs.getString("nama_pelatih"),
                    rs.getString("nama_tim"),
                    rs.getString("nama_kelas"),
                    rs.getString("tanggal"),
                    rs.getString("waktu_mulai"),
                    rs.getString("waktu_selesai"),
                    hari_berulang,
                    kp,
                    ks
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }

        expandRepeatingSchedules(null, null);
        table_jadwal_mengajar.setItems(data);
        setupKehadiranPelatihCellFactory();
        setupKehadiranSiswaCellFactory();
    }
    @FXML
public void editKehadiranPelatih(javafx.event.Event e) {
    jadwal_mengajar selected = table_jadwal_mengajar == null ? null : table_jadwal_mengajar.getSelectionModel().getSelectedItem();
    if (selected == null) return;
        String current = selected.getKehadiran_pelatih();
        String next = "HADIR".equalsIgnoreCase(current) ? "ALFA" : "HADIR";

        // optimistic UI update
        selected.setKehadiran_pelatih(next);
        table_jadwal_mengajar.refresh();

        // persist in background
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "UPDATE jadwal SET kehadiran_pelatih = ? WHERE id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, next);
                    ps.setInt(2, selected.getId_jadwalInt());
                    ps.executeUpdate();
                }
                return null;
            }
            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    // refresh UI and aggregates you already have
                    table_jadwal_mengajar.refresh();

                    // recompute and persist total_gaji for this jadwal
                    recomputeAndPersistTotalForJadwal(selected.getId_jadwalInt());

                    // refresh laporan gaji after recompute completes (you may call immediately; recompute runs in background)
                    if (harahap.laporan_gaji_controller.INSTANCE != null) {
                        harahap.laporan_gaji_controller.INSTANCE.loadGajiPerJadwal();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiHariIni();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiBulan();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiTahun();
                    }
                });
            }
            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    selected.setKehadiran_pelatih(current);
                    table_jadwal_mengajar.refresh();
                });
                getException().printStackTrace();
            }
        };
        new Thread(t).start();
        
    }  
    @FXML
    public void editKehadiranSiswa(javafx.event.Event e) {
        jadwal_mengajar selected = table_jadwal_mengajar == null ? null : table_jadwal_mengajar.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String current = selected.getKehadiran_siswa();
        String next = "HADIR".equalsIgnoreCase(current) ? "ALFA" : "HADIR";

        selected.setKehadiran_siswa(next);
        table_jadwal_mengajar.refresh();

        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "UPDATE jadwal SET kehadiran_siswa = ? WHERE id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, next);
                    ps.setInt(2, selected.getId_jadwalInt());
                    ps.executeUpdate();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                // run UI updates on FX thread
                Platform.runLater(() -> {
                    // keep optimistic UI (already set above) in sync
                    table_jadwal_mengajar.refresh();

                    // recompute and persist total_gaji for this jadwal (background task inside helper)
                    recomputeAndPersistTotalForJadwal(selected.getId_jadwalInt());

                    // refresh laporan gaji views (they will reload from DB)
                    if (harahap.laporan_gaji_controller.INSTANCE != null) {
                        harahap.laporan_gaji_controller.INSTANCE.loadGajiPerJadwal();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiHariIni();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiBulan();
                        harahap.laporan_gaji_controller.INSTANCE.readDataLaporanGajiTahun();
                    }
                });
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    // revert optimistic UI change on failure
                    selected.setKehadiran_siswa(current);
                    table_jadwal_mengajar.refresh();
                });
                getException().printStackTrace();
            }
        };
        new Thread(t).start();
    }


    // --- Reset setting ---
    public void resetSetting(ActionEvent e){
        datepicker_sort_daftar_absensi_setting.setValue(null);
        readDataJadwalMengajar();
        System.out.println("Setting direset! Menampilkan semua data absensi.");
    }

    // --- Delete ---
    public void delete(ActionEvent e){
        System.out.println("Delete button clicked");
        jadwal_mengajar selectedJadwal = table_jadwal_mengajar.getSelectionModel().getSelectedItem();
        if (selectedJadwal == null) {
            System.out.println("No row selected to delete.");
            return;
        }

        String selectSql = "SELECT hari_berulang FROM jadwal WHERE id_jadwal = ?";
        String deleteByIdSql = "DELETE FROM jadwal WHERE id_jadwal = ?";
        String deleteRepeatingSql = "DELETE FROM jadwal WHERE hari_berulang = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false);

            String hariBerulangText = null;
            try (PreparedStatement psSelect = conn.prepareStatement(selectSql)) {
                psSelect.setInt(1, selectedJadwal.getId_jadwalInt());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        hariBerulangText = rs.getString("hari_berulang");
                        if (hariBerulangText != null) {
                            hariBerulangText = hariBerulangText.trim();
                            if (hariBerulangText.isEmpty()) hariBerulangText = null;
                        }
                    }
                }
            }

            try (PreparedStatement psDeleteId = conn.prepareStatement(deleteByIdSql)) {
                psDeleteId.setInt(1, selectedJadwal.getId_jadwalInt());
                int deleted = psDeleteId.executeUpdate();
                System.out.println("Deleted rows by id: " + deleted);
            }

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

        readDataJadwalMengajar();
        System.out.println("Delete button finished");
    }
    @FXML
    public void submitJadwalForm(ActionEvent e) {
        try {
            // Validate pelatih and tim
            String namaPelatihText = combobox_input_nama_pelatih == null ? null : combobox_input_nama_pelatih.getValue();
            TimData selectedTim = combobox_input_nama_tim == null ? null : combobox_input_nama_tim.getValue();

            if (namaPelatihText == null || namaPelatihText.trim().isEmpty()) {
                System.err.println("Error: Pilih nama pelatih.");
                return;
            }
            if (selectedTim == null) {
                System.err.println("Error: Pilih tim.");
                return;
            }

            // Resolve id_pelatih from pelatihDataList by matching toString() (same approach used elsewhere)
            Integer idPelatih = null;
            for (PelatihData p : pelatihDataList) {
                if (p.toString().equals(namaPelatihText)) {
                    idPelatih = p.getId_pelatih();
                    break;
                }
            }
            if (idPelatih == null) {
                System.err.println("Error: Pelatih tidak ditemukan.");
                return;
            }

            // Determine tanggal and hari_berulang
            String tanggal;
            Integer hariBerulang = null;
            String selectedDayName = getSelectedDayName();
            if (selectedDayName != null) {
                LocalDate dateForDay = calculateDateForDay(selectedDayName);
                tanggal = dateForDay.toString();
                hariBerulang = dateForDay.getDayOfWeek().getValue();
            } else if (datepicker_input_tanggal != null && datepicker_input_tanggal.getValue() != null) {
                tanggal = datepicker_input_tanggal.getValue().toString();
                hariBerulang = null;
            } else {
                System.err.println("Error: Pilih tanggal atau centang hari.");
                return;
            }

            // Time strings from spinners
            int sh = spinner_start_hour == null ? 0 : spinner_start_hour.getValue();
            int sm = spinner_start_minute == null ? 0 : spinner_start_minute.getValue();
            int eh = spinner_end_hour == null ? 0 : spinner_end_hour.getValue();
            int em = spinner_end_minute == null ? 0 : spinner_end_minute.getValue();
            String waktuMulai = String.format("%02d:%02d:00", sh, sm);
            String waktuSelesai = String.format("%02d:%02d:00", eh, em);

            Integer idTim = selectedTim.getId_tim();
            Integer idKelas = getSelectedKelasId();

            // Insert into DB (kehadiran defaults to 'ALFA')
            String insertSQL = "INSERT INTO jadwal (id_pelatih, id_kelas, id_tim, tanggal, waktu_mulai, waktu_selesai, hari_berulang, kehadiran_pelatih, kehadiran_siswa) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, idPelatih);
                if (idKelas != null) ps.setInt(2, idKelas); else ps.setNull(2, java.sql.Types.INTEGER);
                    ps.setInt(3, idTim);
                    ps.setString(4, tanggal);
                    ps.setString(5, waktuMulai);
                    ps.setString(6, waktuSelesai);
                if (hariBerulang != null) ps.setInt(7, hariBerulang); else ps.setNull(7, java.sql.Types.INTEGER);
                    ps.setString(8, "ALFA");
                ps.setString(9, "ALFA");
                ps.executeUpdate();
            }

            // Refresh table and reset form
            showAllDataSetting(null);
            resetJadwalForm();
            System.out.println("Jadwal berhasil ditambahkan.");
            } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Gagal submit jadwal: " + ex.getMessage());
        }
    }

    // --- Edit (updates id_tim and id_kelas and optionally mode) ---
    public void edit(ActionEvent e) {
        jadwal_mengajar selectedJadwal = table_jadwal_mengajar.getSelectionModel().getSelectedItem();
        if (selectedJadwal == null) return;

        try {
            TimData selectedTim = combobox_input_nama_tim.getValue();
            String selectedText = combobox_input_nama_pelatih.getValue();
            String namaPelatih = selectedText.split("\\(")[0].trim();
            
            if (selectedText == null || selectedText.isBlank()) {
                System.err.println("Error: Pelatih harus dipilih!");
                return;
}

            if (namaPelatih.isEmpty() || selectedTim == null) {
                System.err.println("Error: Nama pelatih dan tim harus diisi!");
                return;
            }

            String selectedDayName = getSelectedDayName();
            String tanggal;
            Integer hari_berulang = null;
            if (selectedDayName != null) {
                LocalDate dateForDay = calculateDateForDay(selectedDayName);
                tanggal = dateForDay.toString();
                hari_berulang = dateForDay.getDayOfWeek().getValue();
            } else if (datepicker_input_tanggal.getValue() != null) {
                tanggal = datepicker_input_tanggal.getValue().toString();
                hari_berulang = null;
            } else {
                System.err.println("Error: Pilih tanggal atau hari!");
                return;
            }

            Integer id_pelatih = null;
                for (PelatihData p : pelatihDataList) {
                    if (p.getNama_pelatih().equalsIgnoreCase(namaPelatih)) {
                        id_pelatih = p.getId_pelatih();
                        break;
                    }
                }

                if (id_pelatih == null) {
                    System.err.println("Error: Pelatih tidak ditemukan!");
                    return;
                }

            Integer id_tim = selectedTim.getId_tim();

            String waktu_mulai = String.format("%02d:%02d:00", spinner_start_hour.getValue(), spinner_start_minute.getValue());
            String waktu_selesai = String.format("%02d:%02d:00", spinner_end_hour.getValue(), spinner_end_minute.getValue());

            String updateSQL = "UPDATE jadwal SET id_pelatih = ?, id_tim = ?, id_kelas = ?, tanggal = ?, waktu_mulai = ?, waktu_selesai = ?, hari_berulang = ? WHERE id_jadwal = ?";
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = conn.prepareStatement(updateSQL)) {
                ps.setInt(1, id_pelatih);
                ps.setInt(2, id_tim);
                Integer idKelas = getSelectedKelasId();
                if (idKelas != null) ps.setInt(3, idKelas); else ps.setNull(3, java.sql.Types.INTEGER);
                ps.setString(4, tanggal);
                ps.setString(5, waktu_mulai);
                ps.setString(6, waktu_selesai);
                if (hari_berulang != null) ps.setInt(7, hari_berulang);
                else ps.setNull(7, java.sql.Types.INTEGER);
                ps.setInt(8, selectedJadwal.getId_jadwalInt());
                ps.executeUpdate();
            }

            readDataJadwalMengajar();
            resetJadwalForm();
        } catch (Exception ex) {
            System.err.println("Error updating jadwal: ");
            ex.printStackTrace();
        }
    }
    public void resetJadwalForm(ActionEvent e) {
        resetJadwalForm(); // calls the helper you already have (if present)
    }   

    // --- Set current date in setting ---
    public void setTanggalSekarangSetting(ActionEvent e){
        LocalDate today = LocalDate.now();
        datepicker_sort_daftar_absensi_setting.setValue(today);
        System.out.println("Set current date for setting: " + today);
    }

    // --- Utility: initialize time spinners ---
    private boolean updatingEnd = false;
    private void initializeTimeSpinners() {
        // hour: 0-23, minute: 0-59
        SpinnerValueFactory.IntegerSpinnerValueFactory startHourFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 8);
        SpinnerValueFactory.IntegerSpinnerValueFactory startMinuteFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        SpinnerValueFactory.IntegerSpinnerValueFactory endHourFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 9);
        SpinnerValueFactory.IntegerSpinnerValueFactory endMinuteFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);

        spinner_start_hour.setValueFactory(startHourFactory);
        spinner_start_minute.setValueFactory(startMinuteFactory);
        spinner_end_hour.setValueFactory(endHourFactory);
        spinner_end_minute.setValueFactory(endMinuteFactory);

        spinner_start_hour.setEditable(true);
        spinner_start_minute.setEditable(true);
        spinner_end_hour.setEditable(true);
        spinner_end_minute.setEditable(true);

        // helper to update end spinners based on start spinners
        Runnable updateEndFromStart = () -> {
            if (updatingEnd) return;
            try {
                updatingEnd = true;
                int sh = spinner_start_hour.getValue();
                int sm = spinner_start_minute.getValue();
                java.time.LocalTime start = java.time.LocalTime.of(sh, sm);
                java.time.LocalTime end = start.plusHours(1);
                // set end values (use Platform.runLater to avoid UI thread conflicts)
                Platform.runLater(() -> {
                    spinner_end_hour.getValueFactory().setValue(end.getHour());
                    spinner_end_minute.getValueFactory().setValue(end.getMinute());
                });
            } finally {
                updatingEnd = false;
            }
        };

        // listeners for changes on start spinners
        spinner_start_hour.valueProperty().addListener((obs, oldV, newV) -> updateEndFromStart.run());
        spinner_start_minute.valueProperty().addListener((obs, oldV, newV) -> updateEndFromStart.run());

        // Optional: if user edits end manually, do not override until they change start again.
        // If you want to keep end locked to start always, you can also add listeners on end to revert.
    }

    // --- Helper: get selected kelas id ---
    private Integer getSelectedKelasId() {
        KelasData kd = combobox_kelas == null ? null : combobox_kelas.getValue();
        return kd == null ? null : kd.getId_kelas();
    }

    // --- Cell factories for kehadiran columns ---
        private void setupKehadiranPelatihCellFactory() {
        if (col_kehadiran_pelatih == null) return;
        col_kehadiran_pelatih.setCellFactory(col -> {
            TableCell<jadwal_mengajar, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            cell.setOnMouseClicked(evt -> {
                if (cell.isEmpty()) return;
                jadwal_mengajar row = cell.getTableRow().getItem();
                if (row == null) return;

                String current = row.getKehadiran_pelatih();
                String next = "HADIR".equalsIgnoreCase(current) ? "ALFA" : "HADIR";

                // optimistic UI update
                row.setKehadiran_pelatih(next);
                table_jadwal_mengajar.refresh();

                int id = row.getId_jadwalInt();
                // pass both values so DB has full attendance state
                updateAttendanceAndRecompute(id, next, row.getKehadiran_siswa(), row);
            });

            return cell;
        });
    }

    private void setupKehadiranSiswaCellFactory() {
        if (col_kehadiran_siswa == null) return;
        col_kehadiran_siswa.setCellFactory(col -> {
            TableCell<jadwal_mengajar, String> cell = new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item);
                }
            };

            cell.setOnMouseClicked(evt -> {
                if (cell.isEmpty()) return;
                jadwal_mengajar row = cell.getTableRow().getItem();
                if (row == null) return;

                String current = row.getKehadiran_siswa();
                String next = "HADIR".equalsIgnoreCase(current) ? "ALFA" : "HADIR";

                // optimistic UI update
                row.setKehadiran_siswa(next);
                table_jadwal_mengajar.refresh();

                int id = row.getId_jadwalInt();
                // pass both values so DB has full attendance state
                updateAttendanceAndRecompute(id, row.getKehadiran_pelatih(), next, row);
            });

            return cell;
        });
    }

    // --- Expand repeating schedules helper (must preserve both kehadiran fields) ---
    private void expandRepeatingSchedules(LocalDate from, LocalDate to) {
        // If you have existing logic to expand repeating schedules, ensure it creates
        // jadwal_mengajar with both kehadiran_pelatih and kehadiran_siswa preserved.
        // This method is a placeholder if you already have a more complex implementation.
        // For now we do nothing here because your existing code calls this after loading data.
    }

    // --- Reset form helper (example) ---
    private void resetJadwalForm() {

        /* =========================
        Pelatih ComboBox (String)
        ========================= */
        if (combobox_input_nama_pelatih != null) {
            combobox_input_nama_pelatih.getSelectionModel().clearSelection();
            combobox_input_nama_pelatih.setValue(null); // IMPORTANT for String ComboBox
        }

        /* =========================
        Tim ComboBox
        ========================= */
        if (combobox_input_nama_tim != null) {
            combobox_input_nama_tim.getSelectionModel().clearSelection();
            combobox_input_nama_tim.setValue(null);
        }

        /* =========================
        Kelas ComboBox
        ========================= */
        if (combobox_kelas != null) {
            combobox_kelas.getSelectionModel().clearSelection();
            combobox_kelas.setValue(null);
        }

        /* =========================
        DatePicker
        ========================= */
        if (datepicker_input_tanggal != null) {
            datepicker_input_tanggal.setValue(null);
        }

        /* =========================
        Hari RadioButtons
        ========================= */
        if (hari != null) {   // ToggleGroup for hari
            hari.selectToggle(null);
        }

        // /* =========================
        // Progresif / Transisi
        // ========================= */
        // if (progresifAtauTransisi != null) {
        //     progresifAtauTransisi.selectToggle(null);
        // }
    }

    // --- Utility: get day name from date string (yyyy-MM-dd) ---
    private String getDayNameFromDate(String dateStr) {
    if (dateStr == null || dateStr.isEmpty()) return "";
        try {
            LocalDate d = LocalDate.parse(dateStr); // expects yyyy-MM-dd
            Locale id = new Locale("id", "ID");
            String day = d.getDayOfWeek().getDisplayName(TextStyle.FULL, id); // "Senin", "Selasa", ...
            // Ensure first letter uppercase
            if (day.length() > 0) {
                return day.substring(0, 1).toUpperCase(id) + day.substring(1);
            }
            return day;
        } catch (Exception ex) {
            return "";
        }
    }

    // --- Utility: calculate date for selected day name (Mon..Sun) ---
    private LocalDate calculateDateForDay(String dayName) {
        if (dayName == null || dayName.isEmpty()) return LocalDate.now();
        String normalized = dayName.trim().toLowerCase();
        DayOfWeek dow;
        switch (normalized) {
            case "senin":    dow = DayOfWeek.MONDAY; break;
            case "selasa":   dow = DayOfWeek.TUESDAY; break;
            case "rabu":     dow = DayOfWeek.WEDNESDAY; break;
            case "kamis":    dow = DayOfWeek.THURSDAY; break;
            case "jumat":    dow = DayOfWeek.FRIDAY; break;
            case "sabtu":    dow = DayOfWeek.SATURDAY; break;
            case "minggu":   dow = DayOfWeek.SUNDAY; break;
            // also accept English names just in case
            case "monday":   dow = DayOfWeek.MONDAY; break;
            case "tuesday":  dow = DayOfWeek.TUESDAY; break;
            case "wednesday":dow = DayOfWeek.WEDNESDAY; break;
            case "thursday": dow = DayOfWeek.THURSDAY; break;
            case "friday":   dow = DayOfWeek.FRIDAY; break;
            case "saturday": dow = DayOfWeek.SATURDAY; break;
            case "sunday":   dow = DayOfWeek.SUNDAY; break;
            default:         return LocalDate.now();
        }
    LocalDate today = LocalDate.now();
    return today.with(TemporalAdjusters.nextOrSame(dow));
}

    // --- Helper: get selected day name from radio group (if any) ---
    private String getSelectedDayName() {
        if (input_senin != null && input_senin.isSelected()) return "Senin";
        if (input_selasa != null && input_selasa.isSelected()) return "Selasa";
        if (input_rabu != null && input_rabu.isSelected()) return "Rabu";
        if (input_kamis != null && input_kamis.isSelected()) return "Kamis";
        if (input_jumat != null && input_jumat.isSelected()) return "Jumat";
        if (input_sabtu != null && input_sabtu.isSelected()) return "Sabtu";
        if (input_minggu != null && input_minggu.isSelected()) return "Minggu";
        return null;
        }   
    }
