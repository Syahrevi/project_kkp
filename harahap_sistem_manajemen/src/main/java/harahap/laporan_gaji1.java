package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class laporan_gaji1 {
    private IntegerProperty id_jadwal1;
    private StringProperty nama_pelatih1;
    private IntegerProperty total_jam1;
    private IntegerProperty total_gaji1;

    public laporan_gaji1(Integer id_jadwal1, String nama_pelatih1, Integer total_jam1, Integer total_gaji1){
        this.id_jadwal1 = new SimpleIntegerProperty(id_jadwal1);
        this.nama_pelatih1 = new SimpleStringProperty(nama_pelatih1);
        this.total_jam1 = new SimpleIntegerProperty(total_jam1);
        this.total_gaji1 = new SimpleIntegerProperty(total_gaji1);
    }

    //getter
    public int getId_jadwal1(){
        return id_jadwal1.get();
    }
    public String getNama_pelatih1(){
        return nama_pelatih1.get();
    }
    public int getTotal_jam1(){
        return total_jam1.get();
    }
    public int getTotal_gaji1(){
        return total_gaji1.get();
    }

    //setter
    public void setId_jadwal1(Integer value){
        id_jadwal1.set(value);
    }
    public void setNama_pelatih1(String value){
        nama_pelatih1.set(value);
    }
    public void setTotal_jam1(Integer value){
        total_jam1.set(value);
    }
    public void setTotal_gaji1(Integer value){
        total_gaji1.set(value);
    }

    //property
    public IntegerProperty id_jadwal1Property(){
        return id_jadwal1;
    }
    public StringProperty nama_pelatih1Property(){
        return nama_pelatih1;
    }
    public IntegerProperty total_jam1Property(){
        return total_jam1;
    }
    public IntegerProperty total_gaji1Property(){
        return total_gaji1;
    }
}
