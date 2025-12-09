package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GajiPerJadwal {
    private final IntegerProperty id_jadwal;
    private final StringProperty nama_pelatih;
    private final StringProperty nama_grade_keahlian;
    private final StringProperty nama_kelas;
    private final StringProperty tanggal;
    private final IntegerProperty jam;
    private final IntegerProperty total_gaji;
    private final IntegerProperty harga_per_jam; // NEW
    private final StringProperty kehadiran_pelatih; // NEW

    public GajiPerJadwal(int id_jadwal, String nama_pelatih, String nama_grade_keahlian, String nama_kelas, String tanggal, int jam, int total_gaji, int harga_per_jam, String kehadiran_pelatih) {
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.nama_grade_keahlian = new SimpleStringProperty(nama_grade_keahlian);
        this.nama_kelas = new SimpleStringProperty(nama_kelas);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.jam = new SimpleIntegerProperty(jam);
        this.total_gaji = new SimpleIntegerProperty(total_gaji);
        this.harga_per_jam = new SimpleIntegerProperty(harga_per_jam);
        this.kehadiran_pelatih = new SimpleStringProperty(kehadiran_pelatih);
    }

    // Getters (JavaBean style matching PropertyValueFactory keys)
    public int getId_jadwal() { 
        return id_jadwal.get(); 
    }
    public String getNama_pelatih() { 
        return nama_pelatih.get(); 
    }
    public String getNama_grade_keahlian() {
        return nama_grade_keahlian.get();
    }
    public String getNama_kelas() { 
        return nama_kelas.get(); 
    }
    public String getTanggal() { 
        return tanggal.get(); 
    }
    public int getJam() { 
        return jam.get(); 
    }
    public int getTotal_gaji() { 
        return total_gaji.get(); 
    }
    public int getHarga_per_jam() { 
        return harga_per_jam.get(); 
    }
    public String getKehadiran_pelatih() {
        return kehadiran_pelatih.get();
    }

    // Setters
    public void setId_jadwal(int value) {
         id_jadwal.set(value); 
    }
    public void setNama_pelatih(String value) {
         nama_pelatih.set(value); 
    }
    public void setNama_grade_keahlian(String value) {
         nama_grade_keahlian.set(value); 
    }
    public void setNama_kelas(String value) { 
        nama_kelas.set(value); 
    }
    public void setTanggal(String value) { 
        tanggal.set(value); 
    }
    public void setJam(int value) {
         jam.set(value); 
    }
    public void setTotal_gaji(int value) {
         total_gaji.set(value); 
    }
    public void setHarga_per_jam(int value) {
         harga_per_jam.set(value); 
    }
    public void setKehadiran_pelatih(String value) {
        kehadiran_pelatih.set(value);
    }

    // Property accessors
    public IntegerProperty id_jadwalProperty() {
         return id_jadwal; 
        }
    public StringProperty nama_pelatihProperty() {
         return nama_pelatih; 
        }
    public StringProperty nama_grade_keahlianProperty() {
         return nama_grade_keahlian; 
        }
    public StringProperty nama_kelasProperty() {
         return nama_kelas; 
        }
    public StringProperty tanggalProperty() {
         return tanggal; 
        }
    public IntegerProperty jamProperty() {
         return jam; 
        }
    public IntegerProperty total_gajiProperty() {
         return total_gaji; 
        }
    public IntegerProperty harga_per_jamProperty() {
         return harga_per_jam; 
        }
    public StringProperty kehadiran_pelatihProperty() {
         return kehadiran_pelatih; 
        }
}
