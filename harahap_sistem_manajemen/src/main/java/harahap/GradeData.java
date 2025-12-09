package harahap;

public class GradeData {
    private final int id_grade_keahlian;
    private final String nama_grade_keahlian;

    public GradeData(int id_grade_keahlian, String nama_grade_keahlian) {
        this.id_grade_keahlian = id_grade_keahlian;
        this.nama_grade_keahlian = nama_grade_keahlian;
    }

    public int getId_grade_keahlian() { return id_grade_keahlian; }
    public String getNama_grade_keahlian() { return nama_grade_keahlian; }

    @Override
    public String toString() {
        return nama_grade_keahlian;
    }
}
