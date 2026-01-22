package com.example.myapplication.renshi.entity;

import com.google.gson.annotations.SerializedName;

public class YhRenShiKaoQinDaKa {

    private int id;
    private String year;
    private String moth;
    private String name;

    @SerializedName("E")
    private String e;  // 1号考勤状态

    @SerializedName("F")
    private String f;  // 2号

    @SerializedName("G")
    private String g;  // 3号

    @SerializedName("H")
    private String h;  // 4号

    @SerializedName("I")
    private String i;  // 5号

    @SerializedName("J")
    private String j;  // 6号

    @SerializedName("K")
    private String k;  // 7号

    @SerializedName("L")
    private String l;  // 8号

    @SerializedName("M")
    private String m;  // 9号

    @SerializedName("N")
    private String n;  // 10号

    @SerializedName("O")
    private String o;  // 11号

    @SerializedName("P")
    private String p;  // 12号

    @SerializedName("Q")
    private String q;  // 13号

    @SerializedName("R")
    private String r;  // 14号

    @SerializedName("S")
    private String s;  // 15号

    @SerializedName("T")
    private String t;  // 16号

    @SerializedName("U")
    private String u;  // 17号

    @SerializedName("V")
    private String v;  // 18号

    @SerializedName("W")
    private String w;  // 19号

    @SerializedName("X")
    private String x;  // 20号

    @SerializedName("Y")
    private String y;  // 21号

    @SerializedName("Z")
    private String z;  // 22号

    @SerializedName("AA")
    private String aa;  // 23号

    @SerializedName("AB")
    private String ab;  // 24号

    @SerializedName("AC")
    private String ac;  // 25号

    @SerializedName("AD")
    private String ad;  // 26号

    @SerializedName("AE")
    private String ae;  // 27号

    @SerializedName("AF")
    private String af;  // 28号

    @SerializedName("AG")
    private String ag;  // 29号

    @SerializedName("AH")
    private String ah;  // 30号

    @SerializedName("AI")
    private String ai;  // 31号

    @SerializedName("AJ")
    private String aj;  // 全勤天数

    @SerializedName("AK")
    private String ak;  // 实际天数

    @SerializedName("AL")
    private String al;  // 请假/小时

    @SerializedName("AM")
    private String am;  // 加班/小时

    @SerializedName("AN")
    private String an;  // 迟到天数

    @SerializedName("AO")
    private String ao;  // 公司名

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return moth;
    }

    public void setMonth(String moth) {
        this.moth = moth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getF() {
        return f;
    }

    public void setF(String f) {
        this.f = f;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getH() {
        return h;
    }

    public void setH(String h) {
        this.h = h;
    }

    public String getI() {
        return i;
    }

    public void setI(String i) {
        this.i = i;
    }

    public String getJ() {
        return j;
    }

    public void setJ(String j) {
        this.j = j;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getO() {
        return o;
    }

    public void setO(String o) {
        this.o = o;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getW() {
        return w;
    }

    public void setW(String w) {
        this.w = w;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getAa() {
        return aa;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public String getAb() {
        return ab;
    }

    public void setAb(String ab) {
        this.ab = ab;
    }

    public String getAc() {
        return ac;
    }

    public void setAc(String ac) {
        this.ac = ac;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getAe() {
        return ae;
    }

    public void setAe(String ae) {
        this.ae = ae;
    }

    public String getAf() {
        return af;
    }

    public void setAf(String af) {
        this.af = af;
    }

    public String getAg() {
        return ag;
    }

    public void setAg(String ag) {
        this.ag = ag;
    }

    public String getAh() {
        return ah;
    }

    public void setAh(String ah) {
        this.ah = ah;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getAj() {
        return aj;
    }

    public void setAj(String aj) {
        this.aj = aj;
    }

    public String getAk() {
        return ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getAl() {
        return al;
    }

    public void setAl(String al) {
        this.al = al;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getAo() {
        return ao;
    }

    public void setAo(String ao) {
        this.ao = ao;
    }

    // 根据日期获取考勤状态
    public String getDayStatus(int day) {
        switch (day) {
            case 1: return e;
            case 2: return f;
            case 3: return g;
            case 4: return h;
            case 5: return i;
            case 6: return j;
            case 7: return k;
            case 8: return l;
            case 9: return m;
            case 10: return n;
            case 11: return o;
            case 12: return p;
            case 13: return q;
            case 14: return r;
            case 15: return s;
            case 16: return t;
            case 17: return u;
            case 18: return v;
            case 19: return w;
            case 20: return x;
            case 21: return y;
            case 22: return z;
            case 23: return aa;
            case 24: return ab;
            case 25: return ac;
            case 26: return ad;
            case 27: return ae;
            case 28: return af;
            case 29: return ag;
            case 30: return ah;
            case 31: return ai;
            default: return "";
        }
    }

    // 设置某天的考勤状态
    public void setDayStatus(int day, String status) {
        switch (day) {
            case 1: e = status; break;
            case 2: f = status; break;
            case 3: g = status; break;
            case 4: h = status; break;
            case 5: i = status; break;
            case 6: j = status; break;
            case 7: k = status; break;
            case 8: l = status; break;
            case 9: m = status; break;
            case 10: n = status; break;
            case 11: o = status; break;
            case 12: p = status; break;
            case 13: q = status; break;
            case 14: r = status; break;
            case 15: s = status; break;
            case 16: t = status; break;
            case 17: u = status; break;
            case 18: v = status; break;
            case 19: w = status; break;
            case 20: x = status; break;
            case 21: y = status; break;
            case 22: z = status; break;
            case 23: aa = status; break;
            case 24: ab = status; break;
            case 25: ac = status; break;
            case 26: ad = status; break;
            case 27: ae = status; break;
            case 28: af = status; break;
            case 29: ag = status; break;
            case 30: ah = status; break;
            case 31: ai = status; break;
        }
    }
}