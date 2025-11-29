package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class jadwal_mengajar {
    private IntegerProperty id_jadwal;
    private IntegerProperty id_pelatih;
    private IntegerProperty id_siswa;
    private StringProperty tanggal_waktu_awal;
    private StringProperty tanggal_waktu_akhir;
    private StringProperty kehadiran;

    public jadwal_mengajar(Integer id_jadwal, Integer id_pelatih,  Integer id_siswa, String tanggal_waktu_awal, String tanggal_waktu_akhir, String kehadiran){
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.id_pelatih = new SimpleIntegerProperty(id_pelatih);
        this.id_siswa = new SimpleIntegerProperty(id_siswa);
        this.tanggal_waktu_awal = new SimpleStringProperty(tanggal_waktu_awal);
        this.tanggal_waktu_akhir = new SimpleStringProperty(tanggal_waktu_akhir);
        this.kehadiran = new SimpleStringProperty(kehadiran);
    }
    //getter
    public String getId_jadwal(){
        return id_jadwal.get()+"";
    }
    public String getId_pelatih(){
        return id_pelatih.get()+"";
    }
    public String getId_siswa(){
        return id_siswa.get()+"";
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
    public void setId_pelatih(Integer value){
        id_pelatih.set(value);
    }
    
    public void setId_siswa(Integer value){
        id_siswa.set(value);
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
    public IntegerProperty Id_pelatihProperty(){
        return id_pelatih;
    }
    public IntegerProperty Id_siswaProperty(){
        return id_siswa;
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
