package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class laporan_pengecualian_data {
    private final IntegerProperty id_jadwal;
    private final StringProperty tanggal;
    private final StringProperty nama_pelatih;
    private final IntegerProperty gaji_yang_di_input;
    private final IntegerProperty gaji_yang_dikalkulasi;
    private final IntegerProperty selisih;

    public laporan_pengecualian_data(int id_jadwal, String tanggal, String nama_pelatih,
                                      int gaji_yang_di_input, int gaji_yang_dikalkulasi,
                                      int selisih) {
        this.id_jadwal = new SimpleIntegerProperty(id_jadwal);
        this.tanggal = new SimpleStringProperty(tanggal);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.gaji_yang_di_input = new SimpleIntegerProperty(gaji_yang_di_input);
        this.gaji_yang_dikalkulasi = new SimpleIntegerProperty(gaji_yang_dikalkulasi);
        this.selisih = new SimpleIntegerProperty(selisih);
    }

    // Getters (JavaBean style matching PropertyValueFactory keys)
    public int getId_jadwal() {
        return id_jadwal.get();
    }

    public String getTanggal() {
        return tanggal.get();
    }

    public String getNama_pelatih() {
        return nama_pelatih.get();
    }

    public int getGaji_yang_di_input() {
        return gaji_yang_di_input.get();
    }

    public int getGaji_yang_dikalkulasi() {
        return gaji_yang_dikalkulasi.get();
    }

    public int getSelisih() {
        return selisih.get();
    }

    // Setters
    public void setId_jadwal(int value) {
        id_jadwal.set(value);
    }

    public void setTanggal(String value) {
        tanggal.set(value);
    }

    public void setNama_pelatih(String value) {
        nama_pelatih.set(value);
    }

    public void setGaji_yang_di_input(int value) {
        gaji_yang_di_input.set(value);
    }

    public void setGaji_yang_dikalkulasi(int value) {
        gaji_yang_dikalkulasi.set(value);
    }

    public void setSelisih(int value) {
        selisih.set(value);
    }

    // Properties for binding
    public IntegerProperty id_jadwalProperty() {
        return id_jadwal;
    }

    public StringProperty tanggalProperty() {
        return tanggal;
    }

    public StringProperty nama_pelatihProperty() {
        return nama_pelatih;
    }

    public IntegerProperty gaji_yang_di_inputProperty() {
        return gaji_yang_di_input;
    }

    public IntegerProperty gaji_yang_dikalkulasiProperty() {
        return gaji_yang_dikalkulasi;
    }

    public IntegerProperty selisihProperty() {
        return selisih;
    }
}
