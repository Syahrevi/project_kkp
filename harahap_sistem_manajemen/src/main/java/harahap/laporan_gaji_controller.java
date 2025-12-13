package harahap;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.converter.IntegerStringConverter;
import javafx.collections.transformation.FilteredList;
import java.time.YearMonth;


public class laporan_gaji_controller implements javafx.fxml.Initializable {
    public static laporan_gaji_controller INSTANCE;
    // Navigation buttons
    @FXML private Button button_jadwal_mengajar1;
    @FXML private Button button_laporan_gaji;
    @FXML private Button button_absensi;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;

    // Tab Hari
    @FXML private Tab tab_hari;
    @FXML private TableView<laporan_gaji1> table_jadwal_laporan1;
    @FXML private TableColumn<laporan_gaji1, Integer> col_id_jadwal1;
    @FXML private TableColumn<laporan_gaji1, String> col_nama_pelatih1;
    @FXML private TableColumn<laporan_gaji1, Integer> col_total_jam1;
    @FXML private TableColumn<laporan_gaji1, Integer> col_total_gaji1;
    @FXML private ObservableList<laporan_gaji1> data1 = FXCollections.observableArrayList();

    // Tab Bulan
    @FXML private Tab tab_bulan;
    @FXML private TableView<laporan_gaji2> table_jadwal_laporan2;
    @FXML private TableColumn<laporan_gaji2, Integer> col_id_jadwal2;
    @FXML private TableColumn<laporan_gaji2, String> col_nama_pelatih2;
    @FXML private TableColumn<laporan_gaji2, Integer> col_total_jam2;
    @FXML private TableColumn<laporan_gaji2, Integer> col_total_gaji2;
    @FXML private ObservableList<laporan_gaji2> data2 = FXCollections.observableArrayList();

    // Tab Tahun
    @FXML private Tab tab_tahun;
    @FXML private TableView<laporan_gaji3> table_jadwal_laporan3;
    @FXML private TableColumn<laporan_gaji3, Integer> col_id_jadwal3;
    @FXML private TableColumn<laporan_gaji3, String> col_nama_pelatih3;
    @FXML private TableColumn<laporan_gaji3, Integer> col_total_jam3;
    @FXML private TableColumn<laporan_gaji3, Integer> col_total_gaji3;
    @FXML private ObservableList<laporan_gaji3> data3 = FXCollections.observableArrayList();

    // Per-jadwal detail table
    @FXML private TableView<GajiPerJadwal> gaji_per_jadwal;
    @FXML private TableColumn<GajiPerJadwal,Integer> col_id_jadwal_gaji;
    @FXML private TableColumn<GajiPerJadwal,String> col_nama_pelatih_gaji;
    @FXML private TableColumn<GajiPerJadwal,String> col_grade_keahlian_gaji;
    @FXML private TableColumn<GajiPerJadwal,String> col_nama_kelas_gaji;
    @FXML private TableColumn<GajiPerJadwal,String> col_tanggal_gaji;
    @FXML private TableColumn<GajiPerJadwal,Integer> col_jam_gaji;
    @FXML private TableColumn<GajiPerJadwal,Integer> col_total_gaji;
    @FXML private TableColumn<GajiPerJadwal,Integer> col_harga_per_jam;
    @FXML private ObservableList<GajiPerJadwal> dataGajiPerJadwal = FXCollections.observableArrayList();
    @FXML private FilteredList<GajiPerJadwal> filteredGajiPerJadwal;

    @FXML private javafx.scene.control.DatePicker dari_tanggal;
    @FXML private javafx.scene.control.DatePicker sampai_tanggal;
    @FXML private Button tanggal_sekarang;
    @FXML private Button reset_tanggal;

    @FXML private Button print_laporan;

    // DB URL
    private static final String DB_URL = "jdbc:sqlite:database/harahap.db";

    // Navigation helpers
    public void switch_to_jadwal_mengajar(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_jadwal_mengajar(e);
    }
    public void switch_to_crud_pelatih(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_pelatih(e);
    }
    public void switch_to_crud_siswa(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_siswa(e);
    }
    public void switch_to_crud_tim_siswa(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_tim_siswa(e);
    }
    public void switch_to_crud_absensi(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_crud_absensi(e);
    }
    public void switch_to_laporan_gaji(ActionEvent e) throws Exception {
        scene_switcher switcher = new scene_switcher();
        switcher.switch_to_laporan_gaji(e);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hari table bindings
        col_id_jadwal1.setCellValueFactory(new PropertyValueFactory<>("id_jadwal1"));
        col_nama_pelatih1.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih1"));
        col_total_jam1.setCellValueFactory(new PropertyValueFactory<>("total_jam1"));
        col_total_gaji1.setCellValueFactory(new PropertyValueFactory<>("total_gaji1"));

        // Bulan table bindings
        col_id_jadwal2.setCellValueFactory(new PropertyValueFactory<>("id_jadwal2"));
        col_nama_pelatih2.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih2"));
        col_total_jam2.setCellValueFactory(new PropertyValueFactory<>("total_jam2"));
        col_total_gaji2.setCellValueFactory(new PropertyValueFactory<>("total_gaji2"));

        // Tahun table bindings
        col_id_jadwal3.setCellValueFactory(new PropertyValueFactory<>("id_jadwal3"));
        col_nama_pelatih3.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih3"));
        col_total_jam3.setCellValueFactory(new PropertyValueFactory<>("total_jam3"));
        col_total_gaji3.setCellValueFactory(new PropertyValueFactory<>("total_gaji3"));

        // Gaji per jadwal bindings
        col_id_jadwal_gaji.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        col_nama_pelatih_gaji.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_grade_keahlian_gaji.setCellValueFactory(new PropertyValueFactory<>("nama_grade_keahlian"));
        col_nama_kelas_gaji.setCellValueFactory(new PropertyValueFactory<>("nama_kelas"));
        col_tanggal_gaji.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_jam_gaji.setCellValueFactory(new PropertyValueFactory<>("jam"));
        col_total_gaji.setCellValueFactory(new PropertyValueFactory<>("total_gaji"));
        col_harga_per_jam.setCellValueFactory(new PropertyValueFactory<>("harga_per_jam"));

        // Tab listeners
        tab_hari.setOnSelectionChanged(e -> {
            if (tab_hari.isSelected()) {
                applyFilterHariIni();
                readDataLaporanGajiHariIni();
            }
        });

        tab_bulan.setOnSelectionChanged(e -> {
            if (tab_bulan.isSelected()) {
                applyFilterBulanIni();
                readDataLaporanGajiBulan();
            }
        });

        tab_tahun.setOnSelectionChanged(e -> {
            if (tab_tahun.isSelected()) {
                applyFilterTahunIni();
                readDataLaporanGajiTahun();
            }
        });

        INSTANCE = this;

        setupDateControls();
        loadGajiPerJadwal();
        // -------------------------
        // FilteredList for gaji_per_jadwal
        // -------------------------
        filteredGajiPerJadwal = new FilteredList<>(dataGajiPerJadwal, g -> true);
        gaji_per_jadwal.setItems(filteredGajiPerJadwal);

        
        // enable editing on the TableView
        if (gaji_per_jadwal != null) gaji_per_jadwal.setEditable(true);

        // integer converter for text field cells
        IntegerStringConverter intConverter = new IntegerStringConverter();

        // Jam column editable: only update jam, do NOT recalc total_gaji
        if (col_jam_gaji != null) {
            col_jam_gaji.setCellFactory(TextFieldTableCell.forTableColumn(intConverter));
            col_jam_gaji.setEditable(true);
            col_jam_gaji.setOnEditCommit(evt -> {
                GajiPerJadwal row = evt.getRowValue();
                Integer newJam = evt.getNewValue();
                if (newJam == null || newJam < 0) {
                    gaji_per_jadwal.refresh();
                    return;
                }

                String kehadiran = row.getKehadiran_pelatih();
                int hargaPerJam = row.getHarga_per_jam();
                int newTotal;

                if (kehadiran != null && kehadiran.equalsIgnoreCase("alfa")) {
                    // Alfa: total always zero
                    newTotal = 0;
                } else {
                    // normal calculation; if honor applies, apply 50% reduction
                    double raw = newJam * (double) hargaPerJam;
                    // If you store honor in the model, apply it here. For now we assume honor already applied in DB-derived total.
                    newTotal = (int) Math.round(raw);
                }

                // update model and UI
                row.setJam(newJam);
                row.setTotal_gaji(newTotal);
                gaji_per_jadwal.refresh();

                // persist both values in background
                updateJamAndTotalInDatabase(row.getId_jadwal(), newJam, newTotal);
            });
        }

        // Total Gaji column editable: update total_gaji directly
        if (col_total_gaji != null) {
            col_total_gaji.setCellFactory(TextFieldTableCell.forTableColumn(intConverter));
            col_total_gaji.setEditable(true);
            col_total_gaji.setOnEditCommit(evt -> {
                GajiPerJadwal row = evt.getRowValue();
                Integer newTotal = evt.getNewValue();
                if (newTotal == null || newTotal < 0) {
                    gaji_per_jadwal.refresh();
                    return;
                }

                // optimistic UI update
                int oldTotal = row.getTotal_gaji();
                row.setTotal_gaji(newTotal);
                gaji_per_jadwal.refresh();

                // persist total_gaji; on success refresh per-jadwal and aggregates
                updateTotalInDatabaseWithRefresh(row.getId_jadwal(), newTotal, oldTotal);
            });
        }
        
    }
    // -------------------------
// gaji_per_jadwal filters
// -------------------------

private void applyFilterHariIni() {
    LocalDate today = LocalDate.now();
    filteredGajiPerJadwal.setPredicate(g -> {
        try {
            return LocalDate.parse(g.getTanggal()).isEqual(today);
        } catch (Exception e) {
            return false;
        }
    });
}

private void applyFilterBulanIni() {
    YearMonth now = YearMonth.now();
    filteredGajiPerJadwal.setPredicate(g -> {
        try {
            LocalDate d = LocalDate.parse(g.getTanggal());
            return YearMonth.from(d).equals(now);
        } catch (Exception e) {
            return false;
        }
    });
}

    private void applyFilterTahunIni() {
        int year = LocalDate.now().getYear();
        filteredGajiPerJadwal.setPredicate(g -> {
            try {
                return LocalDate.parse(g.getTanggal()).getYear() == year;
            } catch (Exception e) {
                return false;
            }
        });
    }

    private void clearGajiFilter() {
        filteredGajiPerJadwal.setPredicate(g -> true);
    }

    private void updateTotalInDatabaseWithRefresh(int idJadwal, int totalGajiValue, int oldTotal) {
        Task<Void> t = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String sql = "UPDATE jadwal SET total_gaji = ? WHERE id_jadwal = ?";
                try (Connection conn = DriverManager.getConnection(DB_URL);
                    PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, totalGajiValue);
                    ps.setInt(2, idJadwal);
                    ps.executeUpdate();
                }
                return null;
            }

            @Override
            protected void succeeded() {
                // refresh UI on FX thread
                Platform.runLater(() -> {
                    // reload per-jadwal list (preserves current date filter)
                    loadGajiPerJadwal();

                    // refresh aggregates for currently visible tabs
                    // call the aggregate loaders so table_jadwal_laporan1/2/3 reflect the change
                    readDataLaporanGajiHariIni();
                    readDataLaporanGajiBulan();
                    readDataLaporanGajiTahun();
                });
            }

            @Override
            protected void failed() {
                // revert optimistic UI change by reloading from DB
                Platform.runLater(() -> {
                    loadGajiPerJadwal();
                    readDataLaporanGajiHariIni();
                    readDataLaporanGajiBulan();
                    readDataLaporanGajiTahun();
                });
                getException().printStackTrace();
            }
        };
        new Thread(t).start();
    }
        private void updateJamAndTotalInDatabase(int idJadwal, int jamValue, int totalGajiValue) {
            Task<Void> t = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    String sql = "UPDATE jadwal SET total_jam = ?, total_gaji = ? WHERE id_jadwal = ?";
                    try (Connection conn = DriverManager.getConnection(DB_URL);
                        PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, jamValue);
                        ps.setInt(2, totalGajiValue);
                        ps.setInt(3, idJadwal);
                        ps.executeUpdate();
                    }
                    return null;
                }

                @Override
                protected void succeeded() {
                    // Refresh UI on the JavaFX Application Thread so the change is visible immediately
                    Platform.runLater(() -> {
                        // reload per-jadwal list (preserves current date filter)
                        loadGajiPerJadwal();

                        // refresh aggregates so day/month/year tables reflect the updated values
                        readDataLaporanGajiHariIni();
                        readDataLaporanGajiBulan();
                        readDataLaporanGajiTahun();
                    });
                }

                @Override
                protected void failed() {
                    Platform.runLater(() -> {
                        // reload to revert UI to DB state
                        loadGajiPerJadwal();
                        readDataLaporanGajiHariIni();
                        readDataLaporanGajiBulan();
                        readDataLaporanGajiTahun();
                    });
                    getException().printStackTrace();
                }
            };
            new Thread(t).start();
        }


    // -------------------------
    // Per-jadwal calculation
    // -------------------------
    // call this from initialize() after column bindings: loadGajiPerJadwal();
    public void loadGajiPerJadwal() {
        List<GajiPerJadwal> rows = calculateGajiPerJadwal(null, null);
        dataGajiPerJadwal.setAll(rows);
        if (gaji_per_jadwal != null) gaji_per_jadwal.setItems(dataGajiPerJadwal);
    }

    public void loadGajiPerJadwal(LocalDate from, LocalDate to) {
        List<GajiPerJadwal> rows = calculateGajiPerJadwal(from, to);
        dataGajiPerJadwal.setAll(rows);
        if (gaji_per_jadwal != null) gaji_per_jadwal.setItems(dataGajiPerJadwal);
    }

    public List<GajiPerJadwal> calculateGajiPerJadwal(LocalDate from, LocalDate to) {
        List<GajiPerJadwal> result = new ArrayList<>();

        String baseSql = ""
            + "SELECT j.id_jadwal, j.id_pelatih, p.nama_pelatih, p.id_grade_keahlian, p.honor, "
            + "       j.id_kelas, k.nama_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, "
            + "       j.total_gaji, j.total_jam, lb.harga, g.nama_grade_keahlian, j.kehadiran_pelatih, j.kehadiran_siswa "
            + "FROM jadwal j "
            + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
            + "LEFT JOIN kelas k ON j.id_kelas = k.id_kelas "
            + "LEFT JOIN list_biaya lb ON lb.id_kelas = j.id_kelas AND lb.id_nama_grade_keahlian = p.id_grade_keahlian "
            + "LEFT JOIN grade_keahlian g ON p.id_grade_keahlian = g.id_grade_keahlian ";

        String whereClause = "";
        if (from != null && to != null) {
            whereClause = "WHERE j.tanggal BETWEEN ? AND ? ";
        } else if (from != null) {
            whereClause = "WHERE j.tanggal = ? ";
        } else if (to != null) {
            whereClause = "WHERE j.tanggal = ? ";
        }

        String orderClause = "ORDER BY j.id_jadwal ASC";
        String sql = baseSql + whereClause + orderClause;

        DateTimeFormatter[] timeParsers = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm")
        };

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            if (from != null && to != null) {
                ps.setString(1, from.toString());
                ps.setString(2, to.toString());
            } else if (from != null) {
                ps.setString(1, from.toString());
            } else if (to != null) {
                ps.setString(1, to.toString());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idJadwal = rs.getInt("id_jadwal");
                    String namaPelatih = rs.getString("nama_pelatih");
                    String namaKelas = rs.getString("nama_kelas");
                    String namaGrade = rs.getString("nama_grade_keahlian");
                    String waktuMulaiStr = rs.getString("waktu_mulai");
                    String waktuSelesaiStr = rs.getString("waktu_selesai");
                    Integer hargaObj = rs.getObject("harga") != null ? rs.getInt("harga") : 0;
                    String honor = rs.getString("honor");
                    String kehadiran = rs.getString("kehadiran_pelatih");
                    String kehadiranSiswa = rs.getString("kehadiran_siswa");

                    Integer dbTotalGajiObj = rs.getObject("total_gaji") != null ? rs.getInt("total_gaji") : null;
                    Integer dbTotalJamObj  = rs.getObject("total_jam")  != null ? rs.getInt("total_jam")  : null;

                    LocalTime start = null;
                    LocalTime end = null;
                    for (DateTimeFormatter fmt : timeParsers) {
                        try { if (start == null && waktuMulaiStr != null) start = LocalTime.parse(waktuMulaiStr, fmt); } catch (Exception ex) {}
                        try { if (end == null && waktuSelesaiStr != null) end = LocalTime.parse(waktuSelesaiStr, fmt); } catch (Exception ex) {}
                    }

                    double durationHours = 0.0;
                        if (start != null && end != null) {
                            Duration dur = Duration.between(start, end);
                            if (dur.isNegative() || dur.isZero()) dur = Duration.between(start, end.plusHours(24));
                            durationHours = dur.toMinutes() / 60.0;
                        }

                    int fullHours = (int) Math.floor(durationHours);
                    int jamRounded;
                    int totalGajiRounded;

                    if (dbTotalGajiObj != null) {
                        // Use DB-stored authoritative values when present
                        totalGajiRounded = dbTotalGajiObj;
                        jamRounded = dbTotalJamObj != null ? dbTotalJamObj : fullHours;
                    } else {
                        double hargaPerHour = hargaObj != null ? hargaObj.doubleValue() : 0.0;
                        double rawTotal = fullHours * hargaPerHour; // use fullHours
                        boolean honorYes = honor != null && honor.equalsIgnoreCase("ya");
                        double finalTotal = honorYes ? rawTotal * 0.5 : rawTotal;

                        boolean siswaTidakHadir = kehadiranSiswa != null &&
                            (kehadiranSiswa.equalsIgnoreCase("TIDAK_HADIR") || kehadiranSiswa.equalsIgnoreCase("ALFA"));

                        if (kehadiran != null && kehadiran.equalsIgnoreCase("alfa")) {
                            finalTotal = 0.0;
                        } else if (siswaTidakHadir && "HADIR".equalsIgnoreCase(kehadiran)) {
                            finalTotal = 25000.0;
                        }

                        totalGajiRounded = (int) Math.round(finalTotal);
                        jamRounded = fullHours;
                    }

                    String tanggal = rs.getString("tanggal");

                    GajiPerJadwal row = new GajiPerJadwal(
                        idJadwal,
                        namaPelatih != null ? namaPelatih : "",
                        namaGrade != null ? namaGrade : "",
                        namaKelas != null ? namaKelas : "",
                        tanggal != null ? tanggal : "",
                        jamRounded,
                        totalGajiRounded,
                        hargaObj != null ? hargaObj.intValue() : 0,
                        kehadiran != null ? kehadiran : "",
                        kehadiranSiswa != null ? kehadiranSiswa : ""
                    );
                    result.add(row);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }
    private void setupDateControls() {
        // Button: set both datepickers to today
        if (tanggal_sekarang != null) {
            tanggal_sekarang.setOnAction(e -> {
                LocalDate today = LocalDate.now();
                if (dari_tanggal != null) dari_tanggal.setValue(today);
                if (sampai_tanggal != null) sampai_tanggal.setValue(today);
                loadGajiPerJadwal(today, today);
            });
        }

        // Button: reset both datepickers to null
        if (reset_tanggal != null) {
            reset_tanggal.setOnAction(e -> {
                if (dari_tanggal != null) dari_tanggal.setValue(null);
                if (sampai_tanggal != null) sampai_tanggal.setValue(null);

                // clear all filters → show all data
                if (filteredGajiPerJadwal != null) {
                    filteredGajiPerJadwal.setPredicate(g -> true);
                }
            });
        }

        // When either datepicker changes, reload table with appropriate range
        if (dari_tanggal != null) {
            dari_tanggal.valueProperty().addListener((obs, oldV, newV) -> {
                LocalDate from = newV;
                LocalDate to = (sampai_tanggal != null) ? sampai_tanggal.getValue() : null;
                applyDateFilterAndReload(from, to);
            });
        }
        if (sampai_tanggal != null) {
            sampai_tanggal.valueProperty().addListener((obs, oldV, newV) -> {
                LocalDate to = newV;
                LocalDate from = (dari_tanggal != null) ? dari_tanggal.getValue() : null;
                applyDateFilterAndReload(from, to);
            });
        }
    }
    private void applyDateFilterAndReload(LocalDate from, LocalDate to) {
        if (from != null && to != null) {
            // ensure from <= to
            if (from.isAfter(to)) {
                // swap so range is valid
                LocalDate tmp = from;
                from = to;
                to = tmp;
            }
            loadGajiPerJadwal(from, to);
        } else if (from != null) {
            // only from selected -> treat as single-day filter
            loadGajiPerJadwal(from, from);
        } else if (to != null) {
            // only to selected -> treat as single-day filter
            loadGajiPerJadwal(to, to);
        } else {
            // no filter
            loadGajiPerJadwal();
        }
    }

    // -------------------------
    // Hari ini (per-pelatih aggregate)
    // -------------------------
    public void readDataLaporanGajiHariIni() {
        readDataLaporanGajiHariIni(LocalDate.now().toString());
    }

    public void readDataLaporanGajiHariIni(String dateYYYYMMDD) {
        data1.clear();

        String sql = ""
            + "SELECT j.id_jadwal, j.id_pelatih, p.nama_pelatih, p.id_grade_keahlian, p.honor, "
            + "       j.id_kelas, k.nama_kelas, j.waktu_mulai, j.waktu_selesai, "
            + "       lb.harga AS harga_perjam, j.kehadiran_pelatih, j.kehadiran_siswa, "
            + "       j.total_gaji, j.total_jam "
            + "FROM jadwal j "
            + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
            + "LEFT JOIN kelas k ON j.id_kelas = k.id_kelas "
            + "LEFT JOIN list_biaya lb ON lb.id_kelas = j.id_kelas AND lb.id_nama_grade_keahlian = p.id_grade_keahlian "
            + "WHERE j.tanggal = ? "
            + "ORDER BY p.nama_pelatih, j.id_jadwal;";

        class Acc { double totalHours = 0.0; double totalGaji = 0.0; String namaPelatih = ""; }
        java.util.Map<Integer, Acc> map = new java.util.LinkedHashMap<>();

        DateTimeFormatter[] timeParsers = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm")
        };

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dateYYYYMMDD);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPelatih = rs.getInt("id_pelatih");
                    String namaPelatih = rs.getString("nama_pelatih");
                    String honor = rs.getString("honor");
                    String waktuMulaiStr = rs.getString("waktu_mulai");
                    String waktuSelesaiStr = rs.getString("waktu_selesai");
                    int hargaPerJam = rs.getObject("harga_perjam") != null ? rs.getInt("harga_perjam") : 0;

                    // read attendance values
                    String kehadiranPelatih = rs.getString("kehadiran_pelatih");
                    String kehadiranSiswa = rs.getString("kehadiran_siswa");

                    // read DB-stored totals if present
                    Integer dbTotalGaji = rs.getObject("total_gaji") != null ? rs.getInt("total_gaji") : null;
                    Integer dbTotalJam  = rs.getObject("total_jam")  != null ? rs.getInt("total_jam")  : null;

                    LocalTime start = null;
                    LocalTime end = null;
                    for (DateTimeFormatter fmt : timeParsers) {
                        try { if (start == null && waktuMulaiStr != null) start = LocalTime.parse(waktuMulaiStr, fmt); } catch (Exception ex) {}
                        try { if (end == null && waktuSelesaiStr != null) end = LocalTime.parse(waktuSelesaiStr, fmt); } catch (Exception ex) {}
                    }

                    double durationHours = 0.0;
                    if (start != null && end != null) {
                        Duration dur = Duration.between(start, end);
                        if (dur.isNegative() || dur.isZero()) dur = Duration.between(start, end.plusHours(24));
                        durationHours = dur.toMinutes() / 60.0;
                    }

                    double rowTotal;
                    int jamForRow;

                    if (dbTotalGaji != null) {
                        // Use DB-stored authoritative values when present
                        rowTotal = dbTotalGaji.doubleValue();
                        jamForRow = dbTotalJam != null ? dbTotalJam : (int) Math.round(durationHours);
                    } else {
                        rowTotal = durationHours * (double) hargaPerJam;
                        if (honor != null && honor.equalsIgnoreCase("ya")) rowTotal *= 0.5;

                        // interpret siswa absence: treat TIDAK_HADIR or ALFA as "siswa tidak hadir"
                        boolean siswaTidakHadir = kehadiranSiswa != null &&
                            (kehadiranSiswa.equalsIgnoreCase("TIDAK_HADIR") || kehadiranSiswa.equalsIgnoreCase("ALFA"));

                        // Pelatih ALFA => zero (highest precedence)
                        if (kehadiranPelatih != null && kehadiranPelatih.equalsIgnoreCase("ALFA")) {
                            rowTotal = 0.0;
                        }
                        // Override rule: siswa tidak hadir but pelatih hadir => fixed 25000
                        else if (siswaTidakHadir && "HADIR".equalsIgnoreCase(kehadiranPelatih)) {
                            rowTotal = 25000.0;
                        }

                        jamForRow = (int) Math.round(durationHours);
                    }

                    Acc acc = map.get(idPelatih);
                    if (acc == null) {
                        acc = new Acc();
                        acc.namaPelatih = namaPelatih != null ? namaPelatih : "";
                        map.put(idPelatih, acc);
                    }
                    acc.totalHours += jamForRow;
                    acc.totalGaji += rowTotal;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (java.util.Map.Entry<Integer, Acc> e : map.entrySet()) {
            int idPelatih = e.getKey();
            Acc acc = e.getValue();
            int totalJamRounded = (int) Math.round(acc.totalHours);
            int totalGajiRounded = (int) Math.round(acc.totalGaji);

            data1.add(new laporan_gaji1(
                idPelatih,
                acc.namaPelatih,
                totalJamRounded,
                totalGajiRounded
            ));
        }

        table_jadwal_laporan1.setItems(data1);
    }


    // -------------------------
    // Bulan (per-pelatih aggregate)
    // -------------------------
    public void readDataLaporanGajiBulan() {
    String ym = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        readDataLaporanGajiBulan(ym);
    }
    public void readDataLaporanGajiTahun() {
    String year = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
    readDataLaporanGajiTahun(year);
    }
    public void readDataLaporanGajiBulan(String yearMonth) {
        data2.clear();

        String sql = ""
            + "SELECT j.id_jadwal, j.id_pelatih, p.nama_pelatih, p.id_grade_keahlian, p.honor, "
            + "       j.id_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, "
            + "       lb.harga AS harga_perjam, j.kehadiran_pelatih, j.kehadiran_siswa, "
            + "       j.total_gaji, j.total_jam "
            + "FROM jadwal j "
            + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
            + "LEFT JOIN list_biaya lb ON lb.id_kelas = j.id_kelas AND lb.id_nama_grade_keahlian = p.id_grade_keahlian "
            + "WHERE strftime('%Y-%m', j.tanggal) = ? "
            + "ORDER BY p.nama_pelatih, j.id_jadwal;";

        class Acc { double totalHours = 0.0; double totalGaji = 0.0; String namaPelatih = ""; }
        java.util.Map<Integer, Acc> accum = new java.util.LinkedHashMap<>();

        DateTimeFormatter[] timeParsers = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm")
        };

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, yearMonth);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPelatih = rs.getInt("id_pelatih");
                    String namaPelatih = rs.getString("nama_pelatih");
                    String honor = rs.getString("honor");
                    String waktuMulaiStr = rs.getString("waktu_mulai");
                    String waktuSelesaiStr = rs.getString("waktu_selesai");
                    int hargaPerJam = rs.getObject("harga_perjam") != null ? rs.getInt("harga_perjam") : 0;

                    // read attendance values
                    String kehadiranPelatih = rs.getString("kehadiran_pelatih");
                    String kehadiranSiswa = rs.getString("kehadiran_siswa");

                    // read DB-stored totals if present
                    Integer dbTotalGaji = rs.getObject("total_gaji") != null ? rs.getInt("total_gaji") : null;
                    Integer dbTotalJam  = rs.getObject("total_jam")  != null ? rs.getInt("total_jam")  : null;

                    LocalTime start = null;
                    LocalTime end = null;
                    for (DateTimeFormatter fmt : timeParsers) {
                        try { if (start == null && waktuMulaiStr != null) start = LocalTime.parse(waktuMulaiStr, fmt); } catch (Exception ex) {}
                        try { if (end == null && waktuSelesaiStr != null) end = LocalTime.parse(waktuSelesaiStr, fmt); } catch (Exception ex) {}
                    }

                    double durationHours = 0.0;
                        if (start != null && end != null) {
                            Duration dur = Duration.between(start, end);
                            if (dur.isNegative() || dur.isZero()) dur = Duration.between(start, end.plusHours(24));
                            durationHours = dur.toMinutes() / 60.0;
                        }

                    int fullHours = (int) Math.floor(durationHours);
                    double rowTotal;
                    int jamForRow;

                    if (dbTotalGaji != null) {
                        rowTotal = dbTotalGaji.doubleValue();
                        jamForRow = dbTotalJam != null ? dbTotalJam : fullHours;
                    } else {
                        double raw = fullHours * (double) hargaPerJam;
                        boolean honorYes = honor != null && honor.equalsIgnoreCase("ya");
                        double finalTotal = honorYes ? raw * 0.5 : raw;

                        boolean siswaTidakHadir = kehadiranSiswa != null &&
                            (kehadiranSiswa.equalsIgnoreCase("TIDAK_HADIR") || kehadiranSiswa.equalsIgnoreCase("ALFA"));

                        if (kehadiranPelatih != null && kehadiranPelatih.equalsIgnoreCase("alfa")) {
                            finalTotal = 0.0;
                        } else if (siswaTidakHadir && "HADIR".equalsIgnoreCase(kehadiranPelatih)) {
                            finalTotal = 25000.0;
                        }

                        rowTotal = finalTotal;
                        jamForRow = fullHours;
                    }

                    Acc a = accum.get(idPelatih);
                    if (a == null) {
                        a = new Acc();
                        a.namaPelatih = namaPelatih != null ? namaPelatih : "";
                        accum.put(idPelatih, a);
                    }
                    a.totalHours += jamForRow;
                    a.totalGaji += rowTotal;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (java.util.Map.Entry<Integer, Acc> e : accum.entrySet()) {
            int idPelatih = e.getKey();
            Acc a = e.getValue();
            int totalJamRounded = (int) Math.round(a.totalHours);
            int totalGajiRounded = (int) Math.round(a.totalGaji);

            data2.add(new laporan_gaji2(
                idPelatih,
                a.namaPelatih,
                yearMonth,
                totalJamRounded,
                totalGajiRounded
            ));
        }

        table_jadwal_laporan2.setItems(data2);
    }

    public void readDataLaporanGajiTahun(String year) {
        data3.clear();

        String sql = ""
            + "SELECT j.id_jadwal, j.id_pelatih, p.nama_pelatih, p.id_grade_keahlian, p.honor, "
            + "       j.id_kelas, j.tanggal, j.waktu_mulai, j.waktu_selesai, "
            + "       lb.harga AS harga_perjam, j.kehadiran_pelatih, j.kehadiran_siswa, "
            + "       j.total_gaji, j.total_jam "
            + "FROM jadwal j "
            + "JOIN pelatih p ON j.id_pelatih = p.id_pelatih "
            + "LEFT JOIN list_biaya lb ON lb.id_kelas = j.id_kelas AND lb.id_nama_grade_keahlian = p.id_grade_keahlian "
            + "WHERE strftime('%Y', j.tanggal) = ? "
            + "ORDER BY p.nama_pelatih, j.id_jadwal;";

        class Acc { double totalHours = 0.0; double totalGaji = 0.0; String namaPelatih = ""; }
        java.util.Map<Integer, Acc> accum = new java.util.LinkedHashMap<>();

        DateTimeFormatter[] timeParsers = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss"),
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm")
        };

        try (Connection conn = DriverManager.getConnection(DB_URL);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int idPelatih = rs.getInt("id_pelatih");
                    String namaPelatih = rs.getString("nama_pelatih");
                    String honor = rs.getString("honor");
                    String waktuMulaiStr = rs.getString("waktu_mulai");
                    String waktuSelesaiStr = rs.getString("waktu_selesai");
                    int hargaPerJam = rs.getObject("harga_perjam") != null ? rs.getInt("harga_perjam") : 0;

                    // read attendance values
                    String kehadiranPelatih = rs.getString("kehadiran_pelatih");
                    String kehadiranSiswa = rs.getString("kehadiran_siswa");

                    // read DB-stored totals if present
                    Integer dbTotalGaji = rs.getObject("total_gaji") != null ? rs.getInt("total_gaji") : null;
                    Integer dbTotalJam  = rs.getObject("total_jam")  != null ? rs.getInt("total_jam")  : null;

                    LocalTime start = null;
                    LocalTime end = null;
                    for (DateTimeFormatter fmt : timeParsers) {
                        try { if (start == null && waktuMulaiStr != null) start = LocalTime.parse(waktuMulaiStr, fmt); } catch (Exception ex) {}
                        try { if (end == null && waktuSelesaiStr != null) end = LocalTime.parse(waktuSelesaiStr, fmt); } catch (Exception ex) {}
                    }

                    double durationHours = 0.0;
                        if (start != null && end != null) {
                            Duration dur = Duration.between(start, end);
                            if (dur.isNegative() || dur.isZero()) dur = Duration.between(start, end.plusHours(24));
                            durationHours = dur.toMinutes() / 60.0;
                        }
                    
                    int fullHours = (int) Math.floor(durationHours);
                    double rowTotal;
                    int jamForRow;

                    if (dbTotalGaji != null) {
                        rowTotal = dbTotalGaji.doubleValue();
                        jamForRow = dbTotalJam != null ? dbTotalJam : fullHours;
                    } else {
                        double raw = fullHours * (double) hargaPerJam;
                        boolean honorYes = honor != null && honor.equalsIgnoreCase("ya");
                        double finalTotal = honorYes ? raw * 0.5 : raw;

                        boolean siswaTidakHadir = kehadiranSiswa != null &&
                            (kehadiranSiswa.equalsIgnoreCase("TIDAK_HADIR") || kehadiranSiswa.equalsIgnoreCase("ALFA"));

                        if (kehadiranPelatih != null && kehadiranPelatih.equalsIgnoreCase("alfa")) {
                            finalTotal = 0.0;
                        } else if (siswaTidakHadir && "HADIR".equalsIgnoreCase(kehadiranPelatih)) {
                            finalTotal = 25000.0;
                        }

                        rowTotal = finalTotal;
                        jamForRow = fullHours;
                    }

                    Acc a = accum.get(idPelatih);
                    if (a == null) {
                        a = new Acc();
                        a.namaPelatih = namaPelatih != null ? namaPelatih : "";
                        accum.put(idPelatih, a);
                    }
                    a.totalHours += jamForRow;
                    a.totalGaji += rowTotal;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (java.util.Map.Entry<Integer, Acc> e : accum.entrySet()) {
            int idPelatih = e.getKey();
            Acc a = e.getValue();
            int totalJamRounded = (int) Math.round(a.totalHours);
            int totalGajiRounded = (int) Math.round(a.totalGaji);
            String tahunLabel = year;

            data3.add(new laporan_gaji3(
                idPelatih,
                a.namaPelatih,
                tahunLabel,
                totalJamRounded,
                totalGajiRounded
            ));
        }

        table_jadwal_laporan3.setItems(data3);
    }

    // -------------------------
    // Excel export
    // -------------------------
    @FXML
    private void handlePrintLaporan(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Report As");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("Excel Workbook (*.xlsx)", "*.xlsx"));

        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
        String suggestedName = "laporan_gaji_" + dateStr;
        if (tab_hari.isSelected()) suggestedName += "_hari";
        else if (tab_bulan.isSelected()) suggestedName += "_bulan";
        else if (tab_tahun.isSelected()) suggestedName += "_tahun";
        fileChooser.setInitialFileName(suggestedName + ".xlsx");

        File file = fileChooser.showSaveDialog(print_laporan.getScene().getWindow());
        if (file == null) return;

        TableView<?> tableToExport;
        if (tab_hari.isSelected()) tableToExport = table_jadwal_laporan1;
        else if (tab_bulan.isSelected()) tableToExport = table_jadwal_laporan2;
        else tableToExport = table_jadwal_laporan3;

        try {
            exportTablesToExcel(
                tableToExport,
                gaji_per_jadwal,
                file
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void exportTablesToExcel(
        TableView<?> laporanTable,
        TableView<?> gajiPerJadwalTable,
        File file
        ) throws Exception {

            try (XSSFWorkbook workbook = new XSSFWorkbook()) {

                // Sheet 1: Laporan (Hari / Bulan / Tahun)
                XSSFSheet laporanSheet = workbook.createSheet("Laporan");
                writeTableToSheet(laporanTable, laporanSheet);

                // Sheet 2: Gaji Per Jadwal
                XSSFSheet gajiSheet = workbook.createSheet("Gaji Per Jadwal");
                writeTableToSheet(gajiPerJadwalTable, gajiSheet);

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }
            }
        }
        private void writeTableToSheet(TableView<?> table, XSSFSheet sheet) {
            // Header row
            Row headerRow = sheet.createRow(0);
            int colCount = table.getColumns().size();

            for (int c = 0; c < colCount; c++) {
                TableColumn<?, ?> col = table.getColumns().get(c);
                Cell cell = headerRow.createCell(c);
                cell.setCellValue(col.getText() != null ? col.getText() : "");
            }

            // Data rows
            int rowIndex = 1;
            for (int r = 0; r < table.getItems().size(); r++) {
                Row row = sheet.createRow(rowIndex++);
                for (int c = 0; c < colCount; c++) {
                    Object value = table.getColumns().get(c).getCellData(r);
                    Cell cell = row.createCell(c);

                    if (value == null) {
                        cell.setCellValue("");
                    } else if (value instanceof Number) {
                        cell.setCellValue(((Number) value).doubleValue());
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }

            // Auto-size columns
            for (int c = 0; c < colCount; c++) {
                sheet.autoSizeColumn(c);
            }
        }

    // Placeholder for print action (if you want to implement printing)
    public void printLaporanGaji(ActionEvent e) {
        // implement if needed
    }
}