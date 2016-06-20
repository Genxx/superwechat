package cn.gen.fulicenter.bean;

import java.io.Serializable;

/**
 * Created by ucai001 on 2016/3/1.
 */
public class ColorBean implements Serializable {

    /**
     * catId : 262
     * colorId : 1
     * colorName : 灰色
     * colorCode : #959595
     * colorImg : http://121.197.1.20/images/201309/1380064809234134935.jpg
     */

    private int catId;
    private int colorId;
    private String colorName;
    private String colorCode;
    private String colorImg;

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public void setColorImg(String colorImg) {
        this.colorImg = colorImg;
    }

    public int getCatId() {
        return catId;
    }

    public int getColorId() {
        return colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorCode() {
        return colorCode;
    }

    public String getColorImg() {
        return colorImg;
    }

    @Override
    public String toString() {
        return "ColorBean{" +
                "catId=" + catId +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorImg='" + colorImg + '\'' +
                '}';
    }
}
