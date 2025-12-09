package harahap;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class crud_absensi {
    private final IntegerProperty id_kehadiran;
    private final StringProperty nama_pelatih;
    private final StringProperty waktu_mulai;
    private final StringProperty waktu_selesai;
    private final StringProperty tanggal;
    private final IntegerProperty total_jam;

    public crud_absensi(Integer id_kehadiran, String nama_pelatih , String waktu_mulai, String waktu_selesai, String tanggal, Integer total_jam){
        this.id_kehadiran = new SimpleIntegerProperty(id_kehadiran);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.waktu_mulai = new SimpleStringProperty(waktu_mulai);
        this.waktu_selesai = new SimpleStringProperty(waktu_selesai);
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
    public String getWaktu_mulai(){
        return waktu_mulai.get();
    }
    public String getWaktu_selesai(){
        return waktu_selesai.get();
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
    public void setWaktu_mulai(String value){
        waktu_mulai.set(value);
    }
    public void setWaktu_selesai(String value){
        waktu_selesai.set(value);
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
    public StringProperty waktu_mulaiProperty(){
        return waktu_mulai;
    }
    public StringProperty waktu_selesaiProperty(){
        return waktu_selesai;
    }
    public StringProperty tanggalProperty(){
        return tanggal;
    }
    public IntegerProperty total_jamProperty(){
        return total_jam;
    }

}
