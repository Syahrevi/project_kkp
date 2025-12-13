package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class siswa_crud {
    private final IntegerProperty id_siswa;
    private final StringProperty nama_tim;
    private final StringProperty nama_siswa;
    private final StringProperty tanggal_lahir;
    // private final StringProperty kategori;

    public siswa_crud(Integer id_siswa, String nama_tim , String nama_siswa, String tanggal_lahir){
        this.id_siswa = new SimpleIntegerProperty(id_siswa);
        this.nama_tim = new SimpleStringProperty(nama_tim);
        this.nama_siswa = new SimpleStringProperty(nama_siswa);
        this.tanggal_lahir = new SimpleStringProperty(tanggal_lahir);
        // this.kategori = new SimpleStringProperty(kategori);
    }

    // getters (JavaBean style)
    public Integer getId_siswa(){
        return id_siswa.get();
    }
    public String getNama_tim(){
        return nama_tim.get();
    }
    public String getNama_siswa(){
        return nama_siswa.get();
    }
    public String getTanggal_lahir(){
        return tanggal_lahir.get();
    }
    // public String getKategori(){
    //     return kategori.get();
    // }

    // setters
    public void setId_siswa(Integer value){
        id_siswa.set(value);
    }
    public void setNama_tim(String value){
        nama_tim.set(value);
    }
    public void setNama_siswa(String value){
        nama_siswa.set(value);
    }
    public void setTanggal_lahir(String value){
        tanggal_lahir.set(value);
    }
    // public void setKategori(String value){
    //     kategori.set(value);
    // }
    
    public IntegerProperty id_siswaProperty(){
        return id_siswa;
    }
    public StringProperty nama_timProperty(){
        return nama_tim;
    }
    public StringProperty nama_siswaProperty(){
        return nama_siswa;
    }
    public StringProperty tanggal_lahirProperty(){
        return tanggal_lahir;
    }
    // public StringProperty kategoriProperty(){
    //     return kategori;
    // }   
}
