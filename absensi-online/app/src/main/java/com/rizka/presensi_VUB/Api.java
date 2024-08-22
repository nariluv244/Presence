package com.rizka.presensi_VUB;

public class Api {

    private static final String ROOT_URL = "http://192.168.178.2/AbsensiOnline/v1/Api.php?apicall=";

    public static final String URL_CREATE_ABSEN = ROOT_URL + "createabsen";
    public static final String URL_READ_ABSEN = ROOT_URL + "getabsen";
    public static final String URL_UPDATE_ABSEN = ROOT_URL + "updateabsen";
    public static final String URL_DELETE_ABSEN = ROOT_URL + "deleteabsen&id=";
}
