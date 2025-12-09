package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class laporan_gaji3 {
    private IntegerProperty id_jadwal3;
    private StringProperty nama_pelatih3;
    private StringProperty tanggal3;
    private IntegerProperty total_jam3;     // NEW
    private IntegerProperty total_gaji3;

    public laporan_gaji3(Integer id_jadwal3, String nama_pelatih3, String tanggal3, Integer total_jam3, Integer total_gaji3){
        this.id_jadwal3 = new SimpleIntegerProperty(id_jadwal3);
        this.nama_pelatih3 = new SimpleStringProperty(nama_pelatih3);
        this.tanggal3 = new SimpleStringProperty(tanggal3);
        this.total_jam3 = new SimpleIntegerProperty(total_jam3);
        this.total_gaji3 = new SimpleIntegerProperty(total_gaji3);
    }

    // getters
    public int getId_jadwal3(){ return id_jadwal3.get(); }
    public String getNama_pelatih3(){ return nama_pelatih3.get(); }
    public String getTanggal3(){ return tanggal3.get(); }
    public int getTotal_jam3(){ return total_jam3.get(); }   // NEW
    public int getTotal_gaji3(){ return total_gaji3.get(); }

    // setters
    public void setId_jadwal3(Integer value){ id_jadwal3.set(value); }
    public void setNama_pelatih3(String value){ nama_pelatih3.set(value); }
    public void setTanggal3(String value){ tanggal3.set(value); }
    public void setTotal_jam3(Integer value){ total_jam3.set(value); } // NEW
    public void setTotal_gaji3(Integer value){ total_gaji3.set(value); }

    // properties
    public IntegerProperty id_jadwal3Property(){ return id_jadwal3; }
    public StringProperty nama_pelatih3Property(){ return nama_pelatih3; }
    public StringProperty tanggal3Property(){ return tanggal3; }
    public IntegerProperty total_jam3Property(){ return total_jam3; } // NEW
    public IntegerProperty total_gaji3Property(){ return total_gaji3; }
}
