package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class jadwal_mengajar {
    private IntegerProperty id_jadwal;
    private StringProperty nama_pelatih;
    private StringProperty nama_tim;
    private StringProperty nama_kelas;
    private StringProperty tanggal;
    private StringProperty waktu_mulai;
    private StringProperty waktu_selesai;
    private IntegerProperty hari_berulang;
    private StringProperty kehadiran_pelatih; // NEW
    private StringProperty kehadiran_siswa;   // NEW

    public jadwal_mengajar(Integer id_jadwal, String nama_pelatih, String nama_tim, String nama_kelas, String tanggal,
                           String waktu_mulai, String waktu_selesai, Integer hari_berulang,
                           String kehadiran_pelatih, String kehadiran_siswa){
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.nama_tim = new SimpleStringProperty(nama_tim);
        this.nama_kelas = new SimpleStringProperty(nama_kelas != null ? nama_kelas : "");
        this.tanggal = new SimpleStringProperty(tanggal);
        this.waktu_mulai = new SimpleStringProperty(waktu_mulai);
        this.waktu_selesai = new SimpleStringProperty(waktu_selesai);
        this.hari_berulang = new SimpleIntegerProperty(hari_berulang != null ? hari_berulang : 0);
        this.kehadiran_pelatih = new SimpleStringProperty(kehadiran_pelatih != null ? kehadiran_pelatih : "ALFA");
        this.kehadiran_siswa = new SimpleStringProperty(kehadiran_siswa != null ? kehadiran_siswa : "ALFA");
    }

    // getters
    public int getId_jadwal(){ return id_jadwal.get(); }
    public String getNama_pelatih(){ return nama_pelatih.get(); }
    public String getNama_tim(){ return nama_tim.get(); }
    public String getNama_kelas(){ return nama_kelas.get(); }
    public String getTanggal(){ return tanggal.get(); }
    public String getWaktu_mulai(){ return waktu_mulai.get(); }
    public String getWaktu_selesai(){ return waktu_selesai.get(); }
    public Integer getHari_berulang(){ return hari_berulang.get(); }
    public String getKehadiran_pelatih(){ return kehadiran_pelatih.get(); } // NEW
    public String getKehadiran_siswa(){ return kehadiran_siswa.get(); }     // NEW
    public int getId_jadwalInt() { return id_jadwal.get(); }

    // setters
    public void setId_jadwal(Integer value){ id_jadwal.set(value); }
    public void setNama_pelatih(String value){ nama_pelatih.set(value); }
    public void setNama_tim(String value){ nama_tim.set(value); }
    public void setNama_kelas(String value){ nama_kelas.set(value); }
    public void setTanggal(String value){ tanggal.set(value); }
    public void setWaktu_mulai(String value){ waktu_mulai.set(value); }
    public void setWaktu_selesai(String value){ waktu_selesai.set(value); }
    public void setHari_berulang(Integer value){ hari_berulang.set(value); }
    public void setKehadiran_pelatih(String value){ kehadiran_pelatih.set(value); } // NEW
    public void setKehadiran_siswa(String value){ kehadiran_siswa.set(value); }     // NEW

    // properties
    public IntegerProperty id_jadwalProperty(){ return id_jadwal; }
    public StringProperty nama_pelatihProperty(){ return nama_pelatih; }
    public StringProperty nama_timProperty(){ return nama_tim; }
    public StringProperty nama_kelasProperty(){ return nama_kelas; }
    public StringProperty tanggalProperty(){ return tanggal; }
    public StringProperty waktu_mulaiProperty(){ return waktu_mulai; }
    public StringProperty waktu_selesaiProperty(){ return waktu_selesai; }
    public IntegerProperty hari_berulangProperty(){ return hari_berulang; }
    public StringProperty kehadiran_pelatihProperty(){ return kehadiran_pelatih; } // NEW
    public StringProperty kehadiran_siswaProperty(){ return kehadiran_siswa; }     // NEW
}
