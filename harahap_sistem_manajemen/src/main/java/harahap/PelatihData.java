package harahap;

public class PelatihData {
    private int id_pelatih;
    private String nama_pelatih;
    private String nama_keahlian;

    public PelatihData(int id_pelatih, String nama_pelatih, String nama_keahlian) {
        this.id_pelatih = id_pelatih;
        this.nama_pelatih = nama_pelatih;
        this.nama_keahlian = nama_keahlian;
    }

    public int getId_pelatih() {
        return id_pelatih;
    }

    public String getNama_pelatih() {
        return nama_pelatih;
    }

    public String getNama_keahlian() {
        return nama_keahlian;
    }

    @Override
    public String toString() {
        return nama_pelatih + " (" + nama_keahlian + ")";
    }
}
