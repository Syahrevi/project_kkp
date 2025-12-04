package harahap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class crud_absensi {
    private final IntegerProperty id_kehadiran;
    private final StringProperty nama_pelatih;
    private final StringProperty waktu_masuk;
    private final StringProperty waktu_keluar;
    private final StringProperty tanggal;
    private final IntegerProperty total_jam;

    public crud_absensi(Integer id_kehadiran, String nama_pelatih , String waktu_masuk, String waktu_keluar, String tanggal, Integer total_jam){
        this.id_kehadiran = new SimpleIntegerProperty(id_kehadiran);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.waktu_masuk = new SimpleStringProperty(waktu_masuk);
        this.waktu_keluar = new SimpleStringProperty(waktu_keluar);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.total_jam = new SimpleIntegerProperty(total_jam);
    }

    // getters (JavaBean style)
    public Integer getId_Kehadiran(){
        return id_kehadiran.get();
    }
    public String getNama_pelatih(){
        return nama_pelatih.get();
    }
    public String getWaktu_masuk(){
        return waktu_masuk.get();
    }
    public String getWaktu_keluar(){
        return waktu_keluar.get();
    }
    public String getTanggal(){
        return tanggal.get();
    }
    public Integer getTotal_jam(){
        return total_jam.get();
    }
    // setters
    public void setId_kehadiran(Integer value){
        id_kehadiran.set(value);
    }
    public void setNama_pelatih(String value){
        nama_pelatih.set(value);
    }
    public void setWaktu_masuk(String value){
        waktu_masuk.set(value);
    }
    public void setWaktu_keluar(String value){
        waktu_keluar.set(value);
    }
    public void setTanggal(String value){
        tanggal.set(value);
    }
    public void setTotal_jam(Integer value){
        total_jam.set(value);
    }
    //property accessors
    public IntegerProperty id_kehadiranProperty(){
        return id_kehadiran;
    }
    public StringProperty nama_pelatihProperty(){
        return nama_pelatih;
    }
    public StringProperty waktu_masukProperty(){
        return waktu_masuk;
    }
    public StringProperty waktu_keluarProperty(){
        return waktu_keluar;
    }
    public StringProperty tanggalProperty(){
        return tanggal;
    }
    public IntegerProperty total_jamProperty(){
        return total_jam;
    }

}
