package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class laporan_gaji2 {
    private IntegerProperty id_jadwal2;
    private StringProperty nama_pelatih2;
    private StringProperty tanggal2;
    private IntegerProperty total_jam2;     // NEW
    private IntegerProperty total_gaji2;

    public laporan_gaji2(Integer id_jadwal2, String nama_pelatih2, String tanggal2, Integer total_jam2, Integer total_gaji2){
        this.id_jadwal2 = new SimpleIntegerProperty(id_jadwal2);
        this.nama_pelatih2 = new SimpleStringProperty(nama_pelatih2);
        this.tanggal2 = new SimpleStringProperty(tanggal2);
        this.total_jam2 = new SimpleIntegerProperty(total_jam2);
        this.total_gaji2 = new SimpleIntegerProperty(total_gaji2);
    }

    // getters
    public int getId_jadwal2(){ return id_jadwal2.get(); }
    public String getNama_pelatih2(){ return nama_pelatih2.get(); }
    public String getTanggal2(){ return tanggal2.get(); }
    public int getTotal_jam2(){ return total_jam2.get(); }   // NEW
    public int getTotal_gaji2(){ return total_gaji2.get(); }

    // setters
    public void setId_jadwal2(Integer value){ id_jadwal2.set(value); }
    public void setNama_pelatih2(String value){ nama_pelatih2.set(value); }
    public void setTanggal2(String value){ tanggal2.set(value); }
    public void setTotal_jam2(Integer value){ total_jam2.set(value); } // NEW
    public void setTotal_gaji2(Integer value){ total_gaji2.set(value); }

    // properties
    public IntegerProperty id_jadwal2Property(){ return id_jadwal2; }
    public StringProperty nama_pelatih2Property(){ return nama_pelatih2; }
    public StringProperty tanggal2Property(){ return tanggal2; }
    public IntegerProperty total_jam2Property(){ return total_jam2; } // NEW
    public IntegerProperty total_gaji2Property(){ return total_gaji2; }
}
