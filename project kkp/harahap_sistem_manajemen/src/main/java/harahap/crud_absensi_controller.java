package harahap;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class crud_absensi_controller implements Initializable
{
@FXML private Button button_jadwal_mengajar;
    @FXML private Button button_crud_pelatih;
    @FXML private Button button_crud_siswa;
    @FXML private Button button_crud_tim_siswa;
    @FXML private Button button_crud_absensi;

    @FXML private TableView<crud_absensi> table_kehadiran;
    @FXML private TableColumn<crud_absensi, String> col_id_kehadiran;
    @FXML private TableColumn<crud_absensi, Integer> col_tanggal;
    @FXML private TableColumn<crud_absensi, String> col_nama_pelatih;
    @FXML private TableColumn<crud_absensi, String> col_waktu_masuk;
    @FXML private TableColumn<crud_absensi, String> col_waktu_keluar;
    @FXML private TableColumn<crud_absensi, String> col_total_jam;
    @FXML private ObservableList<crud_absensi> data = FXCollections.observableArrayList();

    //absen masuk
    @FXML private DatePicker datepicker_tanggal_masuk;
    @FXML private Spinner<String> spinner_jam_masuk;
    @FXML private Spinner<String> spinner_menit_masuk;
    @FXML private Button button_tanggal_jam_sekarang_masuk;
    @FXML private ComboBox<String> combobox_nama_pelatih_masuk;
    @FXML private Button button_submit_masuk;
    @FXML private Button button_reset_masuk;

    //absen keluar
    @FXML private DatePicker datepicker_tanggal_keluar;
    @FXML private Spinner<String> spinner_jam_keluar;
    @FXML private Spinner<String> spinner_menit_keluar;
    @FXML private Button button_tanggal_jam_sekarang_keluar;
    @FXML private ComboBox<String> combobox_nama_pelatih_keluar;
    @FXML private Button button_submit_keluar;
    @FXML private Button button_reset_keluar;

    //setting
    @FXML DatePicker datepicker_sort_daftar_absensi_setting;
    @FXML Button button_tanggal_sekarang_setting;
    @FXML Button button_reset_setting;
    @FXML Button button_confirm_sort_setting;

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

    public void readDataAbsensi(ActionEvent e){
        col_id_kehadiran.setCellValueFactory(new PropertyValueFactory<>("id_kehadiran"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_waktu_masuk.setCellValueFactory(new PropertyValueFactory<>("waktu_masuk"));
        col_waktu_keluar.setCellValueFactory(new PropertyValueFactory<>("waktu_keluar"));
        col_total_jam.setCellValueFactory(new PropertyValueFactory<>("total_jam"));

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        try (Connection conn = DriverManager.getConnection(DB_URL);
            ResultSet rs = conn.createStatement().executeQuery("SELECT jk.id_kehadiran, p.nama_pelatih, jk.waktu_masuk, jk.waktu_keluar, jk.tanggal, jk.total_jam FROM jumlah_kehadiran jk JOIN pelatih p ON jk.id_pelatih = p.id_pelatih")) {
            while (rs.next()) {
                data.add(new crud_absensi(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6) 
                ));
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_kehadiran.setItems(data);
    }
    
    public void readDataAbsensiByDate(ActionEvent e){
        if (datepicker_sort_daftar_absensi_setting.getValue() == null) {
            System.err.println("Error: Pilih tanggal untuk filter!");
            return;
        }
        
        col_id_kehadiran.setCellValueFactory(new PropertyValueFactory<>("id_kehadiran"));
        col_tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        col_nama_pelatih.setCellValueFactory(new PropertyValueFactory<>("nama_pelatih"));
        col_waktu_masuk.setCellValueFactory(new PropertyValueFactory<>("waktu_masuk"));
        col_waktu_keluar.setCellValueFactory(new PropertyValueFactory<>("waktu_keluar"));
        col_total_jam.setCellValueFactory(new PropertyValueFactory<>("total_jam"));

        data.clear();
        String DB_URL = "jdbc:sqlite:harahap.db";
        String selectedDate = datepicker_sort_daftar_absensi_setting.getValue().toString();
        String query = "SELECT jk.id_kehadiran, p.nama_pelatih, jk.waktu_masuk, jk.waktu_keluar, jk.tanggal, jk.total_jam FROM jumlah_kehadiran jk JOIN pelatih p ON jk.id_pelatih = p.id_pelatih WHERE jk.tanggal = ?";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, selectedDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                data.add(new crud_absensi(
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getInt(6) 
                ));
            }
            System.out.println("Loaded " + data.size() + " records for date: " + selectedDate);
        } catch (Exception er) {
            er.printStackTrace();
        }
        table_kehadiran.setItems(data);
    }

    public void submitMasuk(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        
        // Validate inputs
        if (datepicker_tanggal_masuk.getValue() == null) {
            System.err.println("Error: Pilih tanggal masuk!");
            return;
        }
        if (spinner_jam_masuk.getValue() == null || spinner_jam_masuk.getValue().isEmpty()) {
            System.err.println("Error: Masukkan jam masuk!");
            return;
        }
        if (spinner_menit_masuk.getValue() == null || spinner_menit_masuk.getValue().isEmpty()) {
            System.err.println("Error: Masukkan menit masuk!");
            return;
        }
        if (combobox_nama_pelatih_masuk.getValue() == null || combobox_nama_pelatih_masuk.getValue().isEmpty()) {
            System.err.println("Error: Pilih nama pelatih!");
            return;
        }
        
        // Get id_pelatih from nama_pelatih
        Integer id_pelatih = null;
        String getIdPelatihQuery = "SELECT id_pelatih FROM pelatih WHERE nama_pelatih = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(getIdPelatihQuery)) {
            pstmt.setString(1, combobox_nama_pelatih_masuk.getValue().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id_pelatih = rs.getInt("id_pelatih");
            }
        } catch (Exception er) {
            er.printStackTrace();
            return;
        }
        
        if (id_pelatih == null) {
            System.err.println("Error: Pelatih tidak ditemukan!");
            return;
        }
        
        // Build datetime string
        String tanggal = datepicker_tanggal_masuk.getValue().toString();
        String jam = spinner_jam_masuk.getValue().toString().length() == 1 ? "0" + spinner_jam_masuk.getValue() : spinner_jam_masuk.getValue();
        String menit = spinner_menit_masuk.getValue().toString().length() == 1 ? "0" + spinner_menit_masuk.getValue() : spinner_menit_masuk.getValue();
        String waktu_masuk = tanggal + " " + jam + ":" + menit + ":00";
        
        // Calculate total_jam if waktu_keluar exists in the database for this pelatih and date
        int total_jam = 0;
        String getWaktuKeluarQuery = "SELECT waktu_keluar FROM jumlah_kehadiran WHERE id_pelatih = ? AND tanggal = ? AND waktu_keluar IS NOT NULL ORDER BY waktu_keluar DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(getWaktuKeluarQuery)) {
            pstmt.setInt(1, id_pelatih);
            pstmt.setString(2, tanggal);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String waktu_keluar = rs.getString("waktu_keluar");
                if (waktu_keluar != null && !waktu_keluar.isEmpty()) {
                    try {
                        String[] masukParts = waktu_masuk.split(" ");
                        String[] keluarParts = waktu_keluar.split(" ");
                        
                        String masukTime = masukParts[1];
                        String keluarTime = keluarParts[1];
                        
                        String[] masukTimeComponents = masukTime.split(":");
                        String[] keluarTimeComponents = keluarTime.split(":");
                        
                        int masukHour = Integer.parseInt(masukTimeComponents[0]);
                        int masukMinute = Integer.parseInt(masukTimeComponents[1]);
                        
                        int keluarHour = Integer.parseInt(keluarTimeComponents[0]);
                        int keluarMinute = Integer.parseInt(keluarTimeComponents[1]);
                        
                        int masukTotalMinutes = masukHour * 60 + masukMinute;
                        int keluarTotalMinutes = keluarHour * 60 + keluarMinute;
                        
                        int totalMinutes = keluarTotalMinutes - masukTotalMinutes;
                        total_jam = totalMinutes / 60;
                        
                        if (total_jam < 0) {
                            total_jam = 0;
                        }
                    } catch (Exception er) {
                        System.err.println("Error calculating total_jam: ");
                        er.printStackTrace();
                    }
                }
            }
        } catch (Exception er) {
            er.printStackTrace();
        }
        
        // Insert into jumlah_kehadiran table
        String insertQuery = "INSERT INTO jumlah_kehadiran (id_pelatih, waktu_masuk, waktu_keluar, tanggal, total_jam) VALUES (?, ?, NULL, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setInt(1, id_pelatih);
            pstmt.setString(2, waktu_masuk);
            pstmt.setString(3, tanggal);
            pstmt.setInt(4, total_jam);
            pstmt.executeUpdate();
            System.out.println("Absensi masuk berhasil dicatat! Total jam: " + total_jam + " jam");
        } catch (Exception er) {
            System.err.println("Error inserting absensi masuk: ");
            er.printStackTrace();
            return;
        }
        readDataAbsensi(e);
        resetMasuk(e);
    }
    
    public void resetMasuk(ActionEvent e){
        datepicker_tanggal_masuk.setValue(null);
        spinner_jam_masuk.getValueFactory().setValue("00");
        spinner_menit_masuk.getValueFactory().setValue("00");
        combobox_nama_pelatih_masuk.setValue(null);
        System.out.println("Form masuk direset!");
    }
    
    public void loadComboBoxNamaPelatihMasuk(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        ObservableList<String> pelatihList = FXCollections.observableArrayList();
        String query = "SELECT nama_pelatih FROM pelatih";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pelatihList.add(rs.getString("nama_pelatih"));
            }
            System.out.println("Loaded " + pelatihList.size() + " pelatih names");
        } catch (Exception er) {
            System.err.println("Error loading pelatih names: ");
            er.printStackTrace();
        }
        combobox_nama_pelatih_masuk.setItems(pelatihList);
    }
    
    public void setTanggalJamSekarangMasuk(ActionEvent e){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        datepicker_tanggal_masuk.setValue(today);
        String jam = String.format("%02d", now.getHour());
        String menit = String.format("%02d", now.getMinute());
        spinner_jam_masuk.getValueFactory().setValue(jam);
        spinner_menit_masuk.getValueFactory().setValue(menit);
        
        System.out.println("Set current date and time: " + today + " " + jam + ":" + menit);
    }
    
    public void submitKeluar(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        
        // Validate inputs
        if (datepicker_tanggal_keluar.getValue() == null) {
            System.err.println("Error: Pilih tanggal keluar!");
            return;
        }
        if (spinner_jam_keluar.getValue() == null || spinner_jam_keluar.getValue().isEmpty()) {
            System.err.println("Error: Masukkan jam keluar!");
            return;
        }
        if (spinner_menit_keluar.getValue() == null || spinner_menit_keluar.getValue().isEmpty()) {
            System.err.println("Error: Masukkan menit keluar!");
            return;
        }
        if (combobox_nama_pelatih_keluar.getValue() == null || combobox_nama_pelatih_keluar.getValue().isEmpty()) {
            System.err.println("Error: Pilih nama pelatih!");
            return;
        }
        
        // Get id_pelatih from nama_pelatih
        Integer id_pelatih = null;
        String getIdPelatihQuery = "SELECT id_pelatih FROM pelatih WHERE nama_pelatih = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(getIdPelatihQuery)) {
            pstmt.setString(1, combobox_nama_pelatih_keluar.getValue().toString());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                id_pelatih = rs.getInt("id_pelatih");
            }
        } catch (Exception er) {
            er.printStackTrace();
            return;
        }
        
        if (id_pelatih == null) {
            System.err.println("Error: Pelatih tidak ditemukan!");
            return;
        }
        
        // Build datetime string
        String tanggal = datepicker_tanggal_keluar.getValue().toString();
        String jam = spinner_jam_keluar.getValue().toString().length() == 1 ? "0" + spinner_jam_keluar.getValue() : spinner_jam_keluar.getValue();
        String menit = spinner_menit_keluar.getValue().toString().length() == 1 ? "0" + spinner_menit_keluar.getValue() : spinner_menit_keluar.getValue();
        String waktu_keluar = tanggal + " " + jam + ":" + menit + ":00";
        
        // Get waktu_masuk from the most recent entry without waktu_keluar (the one we're updating)
        String waktu_masuk = null;
        String getWaktuMasukQuery = "SELECT waktu_masuk FROM jumlah_kehadiran WHERE id_pelatih = ? AND tanggal = ? AND waktu_keluar IS NULL ORDER BY id_kehadiran DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(getWaktuMasukQuery)) {
            pstmt.setInt(1, id_pelatih);
            pstmt.setString(2, tanggal);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                waktu_masuk = rs.getString("waktu_masuk");
            }
        } catch (Exception er) {
            er.printStackTrace();
            return;
        }
        
        if (waktu_masuk == null) {
            System.err.println("Error: Waktu masuk tidak ditemukan!");
            return;
        }
        
        // Calculate total_jam (difference in hours between waktu_masuk and waktu_keluar)
        int total_jam = 0;
        try {
            String[] masukParts = waktu_masuk.split(" ");
            String[] keluarParts = waktu_keluar.split(" ");
            
            String masukTime = masukParts[1];
            String keluarTime = keluarParts[1];
            
            String[] masukTimeComponents = masukTime.split(":");
            String[] keluarTimeComponents = keluarTime.split(":");
            
            int masukHour = Integer.parseInt(masukTimeComponents[0]);
            int masukMinute = Integer.parseInt(masukTimeComponents[1]);
            
            int keluarHour = Integer.parseInt(keluarTimeComponents[0]);
            int keluarMinute = Integer.parseInt(keluarTimeComponents[1]);
            
            int masukTotalMinutes = masukHour * 60 + masukMinute;
            int keluarTotalMinutes = keluarHour * 60 + keluarMinute;
            
            int totalMinutes = keluarTotalMinutes - masukTotalMinutes;
            total_jam = totalMinutes / 60;
            
            if (total_jam < 0) {
                System.err.println("Error: Waktu keluar harus setelah waktu masuk!");
                return;
            }
        } catch (Exception er) {
            System.err.println("Error calculating total_jam: ");
            er.printStackTrace();
            return;
        }
        
        // Update jumlah_kehadiran table to set waktu_keluar and total_jam for the most recent entry without keluar
        String updateQuery = "UPDATE jumlah_kehadiran SET waktu_keluar = ?, total_jam = ? WHERE id_pelatih = ? AND tanggal = ? AND waktu_keluar IS NULL";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, waktu_keluar);
            pstmt.setInt(2, total_jam);
            pstmt.setInt(3, id_pelatih);
            pstmt.setString(4, tanggal);
            pstmt.executeUpdate();
            System.out.println("Absensi keluar berhasil dicatat! Total jam: " + total_jam + " jam");
        } catch (Exception er) {
            System.err.println("Error inserting absensi keluar: ");
            er.printStackTrace();
            return;
        }
        readDataAbsensi(e);
        resetKeluar(e);
    }
    
    public void resetKeluar(ActionEvent e){
        datepicker_tanggal_keluar.setValue(null);
        spinner_jam_keluar.getValueFactory().setValue("17");
        spinner_menit_keluar.getValueFactory().setValue("00");
        combobox_nama_pelatih_keluar.setValue(null);
        System.out.println("Form keluar direset!");
    }
    
    public void loadComboBoxNamaPelatihKeluar(ActionEvent e){
        String DB_URL = "jdbc:sqlite:harahap.db";
        ObservableList<String> pelatihList = FXCollections.observableArrayList();
        String query = "SELECT nama_pelatih FROM pelatih";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                pelatihList.add(rs.getString("nama_pelatih"));
            }
            System.out.println("Loaded " + pelatihList.size() + " pelatih names for keluar");
        } catch (Exception er) {
            System.err.println("Error loading pelatih names: ");
            er.printStackTrace();
        }
        combobox_nama_pelatih_keluar.setItems(pelatihList);
    }
    
    public void setTanggalJamSekarangKeluar(ActionEvent e){
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();
        
        datepicker_tanggal_keluar.setValue(today);
        String jam = String.format("%02d", now.getHour());
        String menit = String.format("%02d", now.getMinute());
        spinner_jam_keluar.getValueFactory().setValue(jam);
        spinner_menit_keluar.getValueFactory().setValue(menit);
        
        System.out.println("Set current date and time (keluar): " + today + " " + jam + ":" + menit);
    }
    
    public void setTanggalSekarangSetting(ActionEvent e){
        LocalDate today = LocalDate.now();
        datepicker_sort_daftar_absensi_setting.setValue(today);
        System.out.println("Set current date for setting: " + today);
    }
    
    public void resetSetting(ActionEvent e){
        datepicker_sort_daftar_absensi_setting.setValue(null);
        readDataAbsensi(e);
        System.out.println("Setting direset! Menampilkan semua data absensi.");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Initialize spinners for masuk (entry) - 24 hour format
        spinner_jam_masuk.setValueFactory(new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val - steps + 240) % 24;
                setValue(String.format("%02d", val));
            }
            @Override
            public void increment(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val + steps) % 24;
                setValue(String.format("%02d", val));
            }
        });
        spinner_jam_masuk.getValueFactory().setValue("08");
        
        spinner_menit_masuk.setValueFactory(new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val - steps + 600) % 60;
                setValue(String.format("%02d", val));
            }
            @Override
            public void increment(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val + steps) % 60;
                setValue(String.format("%02d", val));
            }
        });
        spinner_menit_masuk.getValueFactory().setValue("00");
        
        // Initialize spinners for keluar (exit) - 24 hour format
        spinner_jam_keluar.setValueFactory(new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val - steps + 240) % 24;
                setValue(String.format("%02d", val));
            }
            @Override
            public void increment(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val + steps) % 24;
                setValue(String.format("%02d", val));
            }
        });
        spinner_jam_keluar.getValueFactory().setValue("17");
        
        spinner_menit_keluar.setValueFactory(new SpinnerValueFactory<String>() {
            @Override
            public void decrement(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val - steps + 600) % 60;
                setValue(String.format("%02d", val));
            }
            @Override
            public void increment(int steps) {
                int val = Integer.parseInt(getValue());
                val = (val + steps) % 60;
                setValue(String.format("%02d", val));
            }
        });
        spinner_menit_keluar.getValueFactory().setValue("00");
        
        readDataAbsensi(new ActionEvent());
        loadComboBoxNamaPelatihMasuk(null);
        loadComboBoxNamaPelatihKeluar(null);
    }
}
