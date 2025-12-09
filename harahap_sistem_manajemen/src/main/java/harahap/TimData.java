package harahap;

public class TimData {
    private int id_tim;
    private String nama_tim;
    private String kategori_tim; // optional

    public TimData(int id_tim, String nama_tim, String kategori_tim) {
        this.id_tim = id_tim;
        this.nama_tim = nama_tim;
        this.kategori_tim = kategori_tim;
    }

    public int getId_tim() {
        return id_tim;
    }

    public String getNama_tim() {
        return nama_tim;
    }

    public String getKategori_tim() {
        return kategori_tim;
    }

    @Override
    public String toString() {
        return nama_tim;
    }
}   