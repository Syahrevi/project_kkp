package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class tim_siswa_crud {
    private final IntegerProperty id_tim;
    private final StringProperty nama_tim;
    private final StringProperty kategori_tim;

    public tim_siswa_crud(Integer id_tim, String nama_tim , String kategori_tim){
        this.id_tim = new SimpleIntegerProperty(id_tim);
        this.nama_tim = new SimpleStringProperty(nama_tim);
        this.kategori_tim = new SimpleStringProperty(kategori_tim);
    }

    // getters
    public Integer getId_tim(){
        return id_tim.get();
    }
    public String getNama_tim(){
        return nama_tim.get();
    }
    public String getKategori_tim(){
        return kategori_tim.get();
    }

    // setters
    public void setId_tim(Integer value){
        id_tim.set(value);
    }
    public void setNama_tim(String value){
        nama_tim.set(value);
    }
    public void setKategori_tim(String value){
        kategori_tim.set(value);
    }
    
    // property accessors
    public IntegerProperty id_timProperty(){
        return id_tim;
    }
    public StringProperty nama_timProperty(){
        return nama_tim;
    }
    public StringProperty kategori_timProperty(){
        return kategori_tim;
    }   
}
