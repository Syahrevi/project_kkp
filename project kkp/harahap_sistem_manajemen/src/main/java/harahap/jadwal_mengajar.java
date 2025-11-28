package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class jadwal_mengajar {
    private IntegerProperty id_jadwal;
    private StringProperty tanggal_waktu;
    private IntegerProperty id_tim;
    private IntegerProperty id_pelatih;

    public jadwal_mengajar(Integer id_jadwal, String tanggal_waktu, Integer id_tim, Integer id_pelatih) {
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.tanggal_waktu = new SimpleStringProperty(tanggal_waktu);
        this.id_tim = new SimpleIntegerProperty(id_tim);
        this.id_pelatih = new SimpleIntegerProperty(id_pelatih);
    }

    //getter
    public String getId_jadwal(){
        return id_jadwal.get()+"";
    }
    public String getTanggal_waktu(){
        return tanggal_waktu.get()+"";
    }
    public String getId_tim(){
        return id_tim.get()+"";
    }
    public String getId_pelatih(){
        return id_pelatih.get()+"";
    }

    //setter
    public void setId_jadwal(Integer value){
        id_jadwal.set(value);
    }
    public void setTanggal_waktu(String value){
        tanggal_waktu.set(value);
    }
    public void setId_tim(Integer value){
        id_tim.set(value);
    }
    public void setId_pelatih(Integer value){
        id_pelatih.set(value);
    }

    public IntegerProperty Id_jadwalProperty(){
        return id_jadwal;
    }
    public StringProperty Tanggal_waktuProperty(){
        return tanggal_waktu;
    }
    public IntegerProperty Id_timProperty(){
        return id_tim;
    }
    public IntegerProperty Id_pelatihProperty(){
        return id_pelatih;
    }
}
