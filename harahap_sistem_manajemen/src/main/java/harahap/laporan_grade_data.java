package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class laporan_grade_data {
    private final IntegerProperty id_grade_keahlian;
    private final StringProperty nama_grade_keahlian;
    private final IntegerProperty harga;
    private final IntegerProperty total;
    private final IntegerProperty jumlah_sesi;

    public laporan_grade_data(int id_grade_keahlian, String nama_grade_keahlian, 
                              int harga, int total, int jumlah_sesi) {
        this.id_grade_keahlian = new SimpleIntegerProperty(id_grade_keahlian);
        this.nama_grade_keahlian = new SimpleStringProperty(nama_grade_keahlian);
        this.harga = new SimpleIntegerProperty(harga);
        this.total = new SimpleIntegerProperty(total);
        this.jumlah_sesi = new SimpleIntegerProperty(jumlah_sesi);
    }

    // Getters (JavaBean style matching PropertyValueFactory keys)
    public int getId_grade_keahlian() {
        return id_grade_keahlian.get();
    }

    public String getNama_grade_keahlian() {
        return nama_grade_keahlian.get();
    }

    public int getHarga() {
        return harga.get();
    }

    public int getTotal() {
        return total.get();
    }

    public int getJumlah_sesi() {
        return jumlah_sesi.get();
    }

    // Setters
    public void setId_grade_keahlian(int value) {
        id_grade_keahlian.set(value);
    }

    public void setNama_grade_keahlian(String value) {
        nama_grade_keahlian.set(value);
    }

    public void setHarga(int value) {
        harga.set(value);
    }

    public void setTotal(int value) {
        total.set(value);
    }

    public void setJumlah_sesi(int value) {
        jumlah_sesi.set(value);
    }

    // Properties for binding
    public IntegerProperty id_grade_keahlianProperty() {
        return id_grade_keahlian;
    }

    public StringProperty nama_grade_keahlianProperty() {
        return nama_grade_keahlian;
    }

    public IntegerProperty hargaProperty() {
        return harga;
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public IntegerProperty jumlah_sesiProperty() {
        return jumlah_sesi;
    }
}
