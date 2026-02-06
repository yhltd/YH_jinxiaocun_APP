// WorkDetailDTO.java
package com.example.myapplication.scheduling.entity;

import java.io.Serializable;
import java.util.Date;

public class WorkDetailDTO implements Serializable {
    private Integer id;
    private String kaishishijian;
    private Integer charu;
    private String dingdanleixing;
    private String jiezhishijian;
    private Integer gongxushuliang;
    private String gongxumingcheng;
    private Double gongxuxiaolv;
    private String dingdanhao;
    private String dingdanmingcheng;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getKaishishijian() { return kaishishijian; }
    public void setKaishishijian(String kaishishijian) { this.kaishishijian = kaishishijian; }

    public Integer getCharu() { return charu; }
    public void setCharu(Integer charu) { this.charu = charu; }

    public String getDingdanleixing() { return dingdanleixing; }
    public void setDingdanleixing(String dingdanleixing) { this.dingdanleixing = dingdanleixing; }

    public String getJiezhishijian() { return jiezhishijian; }
    public void setJiezhishijian(String jiezhishijian) { this.jiezhishijian = jiezhishijian; }

    public Integer getGongxushuliang() { return gongxushuliang; }
    public void setGongxushuliang(Integer gongxushuliang) { this.gongxushuliang = gongxushuliang; }

    public String getGongxumingcheng() { return gongxumingcheng; }
    public void setGongxumingcheng(String gongxumingcheng) { this.gongxumingcheng = gongxumingcheng; }

    public Double getGongxuxiaolv() { return gongxuxiaolv; }
    public void setGongxuxiaolv(Double gongxuxiaolv) { this.gongxuxiaolv = gongxuxiaolv; }

    public String getDingdanhao() { return dingdanhao; }
    public void setDingdanhao(String dingdanhao) { this.dingdanhao = dingdanhao; }

    public String getDingdanmingcheng() { return dingdanmingcheng; }
    public void setDingdanmingcheng(String dingdanmingcheng) { this.dingdanmingcheng = dingdanmingcheng; }
}