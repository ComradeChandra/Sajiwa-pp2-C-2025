package id.ac.unpas.sajiwa.model;

public class Anggota {
    // ===== ATRIBUT =====
    private int idAnggota;
    private String nim;
    private String namaMahasiswa;
    private String programStudi;
    private String statusAnggota;

    // ===== CONSTRUCTOR =====

    // Constructor kosong (dibutuhkan untuk beberapa proses seperti load data)
    public Anggota() {
    }

    // Constructor tanpa id (untuk insert data baru)
    public Anggota(String nim, String namaMahasiswa, String programStudi, String statusAnggota) {
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.programStudi = programStudi;
        this.statusAnggota = statusAnggota;
    }

    // Constructor lengkap (untuk ambil data dari database)
    public Anggota(int idAnggota, String nim, String namaMahasiswa,
                   String programStudi, String statusAnggota) {
        this.idAnggota = idAnggota;
        this.nim = nim;
        this.namaMahasiswa = namaMahasiswa;
        this.programStudi = programStudi;
        this.statusAnggota = statusAnggota;
    }

    // ===== GETTER & SETTER =====

    public int getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNamaMahasiswa() {
        return namaMahasiswa;
    }

    public void setNamaMahasiswa(String namaMahasiswa) {
        this.namaMahasiswa = namaMahasiswa;
    }

    public String getProgramStudi() {
        return programStudi;
    }

    public void setProgramStudi(String programStudi) {
        this.programStudi = programStudi;
    }

    public String getStatusAnggota() {
        return statusAnggota;
    }

    public void setStatusAnggota(String statusAnggota) {
        this.statusAnggota = statusAnggota;
    }
}
