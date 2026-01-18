package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class GradePerSesi {
    private final IntegerProperty id_jadwal;
    private final StringProperty nama_grade_keahlian;
    private final IntegerProperty harga;
    private final StringProperty tanggal;
    private final IntegerProperty jumlah_sesi;
    private final IntegerProperty total_harga;
    private final StringProperty nama_kelas;
    private final StringProperty nama_pelatih;

    public GradePerSesi(int id_jadwal, String nama_grade_keahlian, int harga, String tanggal, 
                        int jumlah_sesi, int total_harga, String nama_kelas, String nama_pelatih) {
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.nama_grade_keahlian = new SimpleStringProperty(nama_grade_keahlian);
        this.harga = new SimpleIntegerProperty(harga);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.jumlah_sesi = new SimpleIntegerProperty(jumlah_sesi);
        this.total_harga = new SimpleIntegerProperty(total_harga);
        this.nama_kelas = new SimpleStringProperty(nama_kelas);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
    }

    // Getters (JavaBean style matching PropertyValueFactory keys)
    public int getId_jadwal() { 
        return id_jadwal.get(); 
    }
    
    public String getNama_grade_keahlian() { 
        return nama_grade_keahlian.get(); 
    }
    
    public int getHarga() { 
        return harga.get(); 
    }
    
    public String getTanggal() { 
        return tanggal.get(); 
    }
    
    public int getJumlah_sesi() { 
        return jumlah_sesi.get(); 
    }
    
    public int getTotal_harga() { 
        return total_harga.get(); 
    }
    
    public String getNama_kelas() { 
        return nama_kelas.get(); 
    }
    
    public String getNama_pelatih() { 
        return nama_pelatih.get(); 
    }

    // Setters
    public void setId_jadwal(int value) { 
        id_jadwal.set(value); 
    }
    
    public void setNama_grade_keahlian(String value) { 
        nama_grade_keahlian.set(value); 
    }
    
    public void setHarga(int value) { 
        harga.set(value); 
    }
    
    public void setTanggal(String value) { 
        tanggal.set(value); 
    }
    
    public void setJumlah_sesi(int value) { 
        jumlah_sesi.set(value); 
    }
    
    public void setTotal_harga(int value) { 
        total_harga.set(value); 
    }
    
    public void setNama_kelas(String value) { 
        nama_kelas.set(value); 
    }
    
    public void setNama_pelatih(String value) { 
        nama_pelatih.set(value); 
    }

    // Properties for binding
    public IntegerProperty id_jadwalProperty() { 
        return id_jadwal; 
    }
    
    public StringProperty nama_grade_keahlianProperty() { 
        return nama_grade_keahlian; 
    }
    
    public IntegerProperty hargaProperty() { 
        return harga; 
    }
    
    public StringProperty tanggalProperty() { 
        return tanggal; 
    }
    
    public IntegerProperty jumlah_sesiProperty() { 
        return jumlah_sesi; 
    }
    
    public IntegerProperty total_hargaProperty() { 
        return total_harga; 
    }
    
    public StringProperty nama_kelasProperty() { 
        return nama_kelas; 
    }
    
    public StringProperty nama_pelatihProperty() { 
        return nama_pelatih; 
    }
}
