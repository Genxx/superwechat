package cn.gen.fulicenter.bean;



import java.io.Serializable;

/**
 * Created by ucai001 on 2016/3/1.
 */
public class NewGoodBean implements Serializable {

    /**
     * id : 1
     * goodsId : 7672
     * catId : 0
     * goodsName : 趣味煮蛋模具
     * goodsEnglishName : Kotobuki
     * goodsBrief : 将煮好的鸡蛋…
     * shopPrice : ￥110
     * currencyPrice : ￥140
     * promotePrice : ￥0
     * rankPrice : ￥0
     * promote : false
     * goodsThumb : 201509/thumb_img/7672_thumb_G_14423845719.jpg
     * goodsImg : 201509/goods_img/7672_P_1442389445199.jpg
     * colorId : 4
     * colorName : 绿色
     * colorCode : #59d85c
     * colorUrl : 1
     * addTime : 1442389445
     */

    private int id;
    private int goodsId;
    private int catId;
    private String goodsName;
    private String goodsEnglishName;
    private String goodsBrief;
    private String shopPrice;
    private String currencyPrice;
    private String promotePrice;
    private String rankPrice;
    private boolean isPromote;
    private String goodsThumb;
    private String goodsImg;
    private int colorId;
    private String colorName;
    private String colorCode;
    private String colorUrl;
    private long addTime;

    public void setId(int id) {
        this.id = id;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setGoodsEnglishName(String goodsEnglishName) {
        this.goodsEnglishName = goodsEnglishName;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public void setCurrencyPrice(String currencyPrice) {
        this.currencyPrice = currencyPrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public void setRankPrice(String rankPrice) {
        this.rankPrice = rankPrice;
    }

    public void setIsPromote(boolean isPromote) {
        this.isPromote = isPromote;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
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

    public void setColorUrl(String colorUrl) {
        this.colorUrl = colorUrl;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public int getId() {
        return id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public int getCatId() {
        return catId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getGoodsEnglishName() {
        return goodsEnglishName;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public String getCurrencyPrice() {
        return currencyPrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public String getRankPrice() {
        return rankPrice;
    }

    public boolean isPromote() {
        return isPromote;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public String getGoodsImg() {
        return goodsImg;
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

    public String getColorUrl() {
        return colorUrl;
    }

    public long getAddTime() {
        return addTime;
    }

    @Override
    public String toString() {
        return "NewGoodBean{" +
                "id=" + id +
                ", goodsId=" + goodsId +
                ", catId=" + catId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsEnglishName='" + goodsEnglishName + '\'' +
                ", goodsBrief='" + goodsBrief + '\'' +
                ", shopPrice='" + shopPrice + '\'' +
                ", currencyPrice='" + currencyPrice + '\'' +
                ", promotePrice='" + promotePrice + '\'' +
                ", rankPrice='" + rankPrice + '\'' +
                ", isPromote=" + isPromote +
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", colorId=" + colorId +
                ", colorName='" + colorName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", colorUrl='" + colorUrl + '\'' +
                ", addTime=" + addTime +
                '}';
    }
}
