package com.example.nasrul.presensi_VUB;

public class Absen {

    private int id;
    private String tanggal, kelas, mapel, nama, keterangan;

    public Absen(int id, String tanggal, String kelas, String mapel, String nama, String keterangan) {
        this.id = id;
        this.tanggal = tanggal;
        this.kelas = kelas;
        this.mapel = mapel;
        this.nama = nama;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getKelas() {
        return kelas;
    }

    public String getMapel() {
        return mapel;
    }

    public String getNama() {
        return nama;
    }

    public String getKeterangan() {
        return keterangan;
    }

}
