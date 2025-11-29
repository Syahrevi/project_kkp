package harahap;

public class SiswaData {
    private int id_siswa;
    private String nama_siswa;
    private String nama_tim;

    public SiswaData(int id_siswa, String nama_siswa, String nama_tim) {
        this.id_siswa = id_siswa;
        this.nama_siswa = nama_siswa;
        this.nama_tim = nama_tim;
    }

    public int getId_siswa() {
        return id_siswa;
    }

    public String getNama_siswa() {
        return nama_siswa;
    }

    public String getNama_tim() {
        return nama_tim;
    }

    @Override
    public String toString() {
        return nama_siswa + " (" + nama_tim + ")";
    }
}
