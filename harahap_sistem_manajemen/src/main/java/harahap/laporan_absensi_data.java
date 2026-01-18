package harahap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class laporan_absensi_data {
    private final IntegerProperty id_pelatih;
    private final StringProperty nama_pelatih;
    private final IntegerProperty scheduled_sessions;
    private final DoubleProperty hadir_percent;
    private final IntegerProperty delivered_hours;

    public laporan_absensi_data(int id_pelatih, String nama_pelatih, int scheduled_sessions,
                                 double hadir_percent, int delivered_hours) {
        this.id_pelatih = new SimpleIntegerProperty(id_pelatih);
        this.nama_pelatih = new SimpleStringProperty(nama_pelatih);
        this.scheduled_sessions = new SimpleIntegerProperty(scheduled_sessions);
        this.hadir_percent = new SimpleDoubleProperty(hadir_percent);
        this.delivered_hours = new SimpleIntegerProperty(delivered_hours);
    }

    // Getters (JavaBean style matching PropertyValueFactory keys)
    public int getId_pelatih() {
        return id_pelatih.get();
    }

    public String getNama_pelatih() {
        return nama_pelatih.get();
    }

    public int getScheduled_sessions() {
        return scheduled_sessions.get();
    }

    public double getHadir_percent() {
        return hadir_percent.get();
    }

    public int getDelivered_hours() {
        return delivered_hours.get();
    }

    // Setters
    public void setId_pelatih(int value) {
        id_pelatih.set(value);
    }

    public void setNama_pelatih(String value) {
        nama_pelatih.set(value);
    }

    public void setScheduled_sessions(int value) {
        scheduled_sessions.set(value);
    }

    public void setHadir_percent(double value) {
        hadir_percent.set(value);
    }

    public void setDelivered_hours(int value) {
        delivered_hours.set(value);
    }

    // Properties for binding
    public IntegerProperty id_pelatihProperty() {
        return id_pelatih;
    }

    public StringProperty nama_pelatihProperty() {
        return nama_pelatih;
    }

    public IntegerProperty scheduled_sessionsProperty() {
        return scheduled_sessions;
    }

    public DoubleProperty hadir_percentProperty() {
        return hadir_percent;
    }

    public IntegerProperty delivered_hoursProperty() {
        return delivered_hours;
    }
}
