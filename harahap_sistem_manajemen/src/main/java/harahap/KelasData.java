package harahap;

public class KelasData {
    private final int id_kelas;
    private final String nama_kelas;

    public KelasData(int id_kelas, String nama_kelas) {
        this.id_kelas = id_kelas;
        this.nama_kelas = nama_kelas;
    }

    public int getId_kelas() { return id_kelas; }
    public String getNama_kelas() { return nama_kelas; }

    @Override
    public String toString() {
        return nama_kelas;
    }
}
