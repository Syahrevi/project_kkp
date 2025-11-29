package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class jadwal_mengajar {
    private IntegerProperty id_jadwal;
    private StringProperty nama_pelatih;
    private StringProperty nama_siswa;
    private StringProperty tanggal_waktu_awal;
    private StringProperty tanggal_waktu_akhir;
    private StringProperty kehadiran;

    public jadwal_mengajar(Integer id_jadwal, String nama_pelatih,  String nama_siswa, String tanggal_waktu_awal, String tanggal_waktu_akhir, String kehadiran){
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.nama_siswa = new SimpleStringProperty(nama_siswa);
        this.tanggal_waktu_awal = new SimpleStringProperty(tanggal_waktu_awal);
        this.tanggal_waktu_akhir = new SimpleStringProperty(tanggal_waktu_akhir);
        this.kehadiran = new SimpleStringProperty(kehadiran);
    }
    //getter
    public String getId_jadwal(){
        return id_jadwal.get()+"";
    }
    public String getnama_pelatih(){
        return nama_pelatih.get()+"";
    }
    public String getnama_siswa(){
        return nama_siswa.get()+"";
    }
    public String getTanggal_waktu_awal(){
        return tanggal_waktu_awal.get()+"";
    }
    public String getTanggal_waktu_akhir(){
        return tanggal_waktu_akhir.get()+"";
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
    public void setnama_pelatih(String value){
        nama_pelatih.set(value);
    }
    
    public void setnama_siswa(String value){
        nama_siswa.set(value);
    }
    
    public void setTanggal_waktu_awal(String value){
        tanggal_waktu_awal.set(value);
    }
    public void setTanggal_waktu_akhir(String value){
        tanggal_waktu_akhir.set(value);
    }
    public void setKehadiran(String value){
        kehadiran.set(value);
    }
    

    public IntegerProperty Id_jadwalProperty(){
        return id_jadwal;
    }
    public StringProperty nama_pelatihProperty(){
        return nama_pelatih;
    }
    public StringProperty nama_siswaProperty(){
        return nama_siswa;
    }
    public StringProperty Tanggal_waktu_awalProperty(){
        return tanggal_waktu_awal;
    }
    public StringProperty Tanggal_waktu_akhirProperty(){
        return tanggal_waktu_akhir;
    }
    public StringProperty KehadiranProperty(){
        return kehadiran;
    }
}
