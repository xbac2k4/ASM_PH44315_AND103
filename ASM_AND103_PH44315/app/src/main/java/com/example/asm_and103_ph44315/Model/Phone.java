package com.example.asm_and103_ph44315.Model;

public class Phone {
    private String _id;
    private String hinhanh;
    private String ten;
    private String hang;
    private Double gia;
    private int soluong;

    public Phone() {
    }

    public Phone(String _id, String hinhanh, String ten, String hang, Double gia, int soluong) {
        this._id = _id;
        this.hinhanh = hinhanh;
        this.ten = ten;
        this.hang = hang;
        this.gia = gia;
        this.soluong = soluong;
    }

    public Phone(String hinhanh, String ten, String hang, Double gia, int soluong) {
        this.hinhanh = hinhanh;
        this.ten = ten;
        this.hang = hang;
        this.gia = gia;
        this.soluong = soluong;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHang() {
        return hang;
    }

    public void setHang(String hang) {
        this.hang = hang;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
