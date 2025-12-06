package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class jadwal_mengajar {
    private IntegerProperty id_jadwal;
    private StringProperty nama_pelatih;
    private StringProperty nama_siswa;
    private StringProperty tanggal;
    private StringProperty waktu_mulai;
    private StringProperty waktu_selesai;
    private IntegerProperty hari_berulang;
    private StringProperty kehadiran;

    public jadwal_mengajar(Integer id_jadwal, String nama_pelatih,  String nama_siswa, String tanggal, String waktu_mulai, String waktu_selesai, Integer hari_berulang, String kehadiran){
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.nama_siswa = new SimpleStringProperty(nama_siswa);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.waktu_mulai = new SimpleStringProperty(waktu_mulai);
        this.waktu_selesai = new SimpleStringProperty(waktu_selesai);
        this.hari_berulang = new SimpleIntegerProperty(hari_berulang != null ? hari_berulang : 0);
        this.kehadiran = new SimpleStringProperty(kehadiran);
    }
    //getter
    public int getId_jadwal(){
        return id_jadwal.get();
    }
    public String getNama_pelatih(){
        return nama_pelatih.get();
    }
    public String getNama_siswa(){
        return nama_siswa.get();
    }
    public String getTanggal(){
        return tanggal.get();
    }
    public String getWaktu_mulai(){
        return waktu_mulai.get();
    }
    public String getWaktu_selesai(){
        return waktu_selesai.get();
    }
    public Integer getHari_berulang(){
        return hari_berulang.get();
    }
    public String getKehadiran(){
        return kehadiran.get();
    }
    public int getId_jadwalInt() {
    return id_jadwal.get();
}
    //setter
    public void setId_jadwal(Integer value){
        id_jadwal.set(value);
    }
    public void setNama_pelatih(String value){
        nama_pelatih.set(value);
    }
    
    public void setNama_siswa(String value){
        nama_siswa.set(value);
    }
    
    public void setTanggal(String value){
        tanggal.set(value);
    }
    public void setWaktu_mulai(String value){
        waktu_mulai.set(value);
    }
    public void setWaktu_selesai(String value){
        waktu_selesai.set(value);
    }
    public void setHari_berulang(Integer value){
        hari_berulang.set(value);
    }
    public void setKehadiran(String value){
        kehadiran.set(value);
    }
    
    //property
    public IntegerProperty id_jadwalProperty(){
        return id_jadwal;
    }
    public StringProperty nama_pelatihProperty(){
        return nama_pelatih;
    }
    public StringProperty nama_siswaProperty(){
        return nama_siswa;
    }
    public StringProperty tanggalProperty(){
        return tanggal;
    }
    public StringProperty waktu_mulaiProperty(){
        return waktu_mulai;
    }
    public StringProperty waktu_selesaiProperty(){
        return waktu_selesai;
    }
    public IntegerProperty hari_berulangProperty(){
        return hari_berulang;
    }
    public StringProperty KehadiranProperty(){
        return kehadiran;
    }
}
