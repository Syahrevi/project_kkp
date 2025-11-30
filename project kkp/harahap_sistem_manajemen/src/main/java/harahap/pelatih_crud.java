package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class pelatih_crud {
    private final IntegerProperty id_pelatih;
    private final StringProperty nama_pelatih;
    private final StringProperty tanggal_lahir;
    private final StringProperty nama_keahlian;

    public pelatih_crud(Integer id_pelatih, String nama_pelatih, String tanggal_lahir, String nama_keahlian){
        this.id_pelatih = new SimpleIntegerProperty(id_pelatih);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.tanggal_lahir = new SimpleStringProperty(tanggal_lahir);
        this.nama_keahlian = new SimpleStringProperty(nama_keahlian);
    }

    // getters (JavaBean style)
    public Integer getId_pelatih(){
        return id_pelatih.get();
    }
    public String getNama_pelatih(){
        return nama_pelatih.get();
    }
    public String getTanggal_lahir(){
        return tanggal_lahir.get();
    }
    public String getNama_keahlian(){
        return nama_keahlian.get();
    }

    // setters
    public void setId_pelatih(Integer value){
        id_pelatih.set(value);
    }
    public void setNama_pelatih(String value){
        nama_pelatih.set(value);
    }
    public void setTanggal_lahir(String value){
        tanggal_lahir.set(value);
    }
    public void setNama_keahlian(String value){
        nama_keahlian.set(value);
    }
    
    // property accessors — MUST be lowercase start to match PropertyValueFactory("id_pelatih")
    public IntegerProperty id_pelatihProperty(){
        return id_pelatih;
    }
    public StringProperty nama_pelatihProperty(){
        return nama_pelatih;
    }
    public StringProperty tanggal_lahirProperty(){
        return tanggal_lahir;
    }
    public StringProperty nama_keahlianProperty(){
        return nama_keahlian;
    }   
}