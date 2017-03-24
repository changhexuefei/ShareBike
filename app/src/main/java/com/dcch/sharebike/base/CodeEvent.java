package com.dcch.sharebike.base;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class CodeEvent {
    private String bikeNo;


    public String getBikeNo() {
        return bikeNo;
    }

    public void setBikeNo(String bikeNo) {
        this.bikeNo = bikeNo;
    }

    public CodeEvent(String bikeNo) {
        this.bikeNo = bikeNo;
    }
}
