package cn.gen.fulicenter.bean;

import android.widget.ProgressBar;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/6/13.
 */
public class GoodDetailsBean implements Serializable {
    /**
     * id : 280
     * goodsId : 7677
     * catId : 291
     * goodsName : 双层分格饭盒 绿色
     * goodsEnglishName : Monbento
     * goodsBrief : PP食品级材质，轻巧、易清洗、蠕变性小，不易变形，可置于微波炉加热，可方巾洗碗机清洗。双层色彩可以随意组合，轻巧方便。
     * shopPrice : ￥253
     * currencyPrice : ￥293
     * promotePrice : ￥0
     * rankPrice : ￥293
     * goodsThumb : 201509/thumb_img/7677_thumb_G_1442391216339.png
     * goodsImg : 201509/thumb_img/7677_thumb_G_1442391216339.png
     * addTime : 1442419200000
     * shareUrl : http://m.fulishe.com/item/7677
     * properties : [{"id":9529,"goodsId":0,"colorId":7,"colorName":"白色","colorCode":"#ffffff","colorImg":"","colorUrl":"https://detail.tmall.com/item.htm?spm=a1z10.5-b.w4011-3609973698.66.6PtkVY&id=520971761592&rn=5ddf7aff64dbe1a24da0eaf7409e3389&abbucket=15&skuId=3104519239252","albums":[{"pid":7677,"imgId":28296,"imgUrl":"201509/goods_img/7677_P_1442391216432.png","thumbUrl":"no_picture.gif"}]}]
     * promote : false
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
    private String goodsThumb;
    private String goodsImg;
    private long addTime;
    private String shareUrl;
    private boolean isPromote;
    /**
     * id : 9529
     * goodsId : 0
     * colorId : 7
     * colorName : 白色
     * colorCode : #ffffff
     * colorImg :
     * colorUrl : https://detail.tmall.com/item.htm?spm=a1z10.5-b.w4011-3609973698.66.6PtkVY&id=520971761592&rn=5ddf7aff64dbe1a24da0eaf7409e3389&abbucket=15&skuId=3104519239252
     * albums : [{"pid":7677,"imgId":28296,"imgUrl":"201509/goods_img/7677_P_1442391216432.png","thumbUrl":"no_picture.gif"}]
     */

    private ProgressBar[] properties;
    private AlbumBean[] albums;

    public AlbumBean[] getAlbums() {
        return albums;
    }

    public void setAlbums(AlbumBean[] albums) {
        this.albums = albums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getCatId() {
        return catId;
    }

    public void setCatId(int catId) {
        this.catId = catId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsEnglishName() {
        return goodsEnglishName;
    }

    public void setGoodsEnglishName(String goodsEnglishName) {
        this.goodsEnglishName = goodsEnglishName;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    public String getCurrencyPrice() {
        return currencyPrice;
    }

    public void setCurrencyPrice(String currencyPrice) {
        this.currencyPrice = currencyPrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public String getRankPrice() {
        return rankPrice;
    }

    public void setRankPrice(String rankPrice) {
        this.rankPrice = rankPrice;
    }

    public String getGoodsThumb() {
        return goodsThumb;
    }

    public void setGoodsThumb(String goodsThumb) {
        this.goodsThumb = goodsThumb;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public boolean isPromote() {
        return isPromote;
    }

    public void setPromote(boolean promote) {
        isPromote = promote;
    }

    public ProgressBar[] getProperties() {
        return properties;
    }

    public void setProperties(ProgressBar[] properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GoodDetailsBean)) return false;

        GoodDetailsBean that = (GoodDetailsBean) o;

        if (getId() != that.getId()) return false;
        if (getGoodsId() != that.getGoodsId()) return false;
        if (getCatId() != that.getCatId()) return false;
        if (getAddTime() != that.getAddTime()) return false;
        if (isPromote() != that.isPromote()) return false;
        if (!getGoodsName().equals(that.getGoodsName())) return false;
        if (!getGoodsEnglishName().equals(that.getGoodsEnglishName())) return false;
        if (!getGoodsBrief().equals(that.getGoodsBrief())) return false;
        if (!getShopPrice().equals(that.getShopPrice())) return false;
        if (!getCurrencyPrice().equals(that.getCurrencyPrice())) return false;
        if (!getPromotePrice().equals(that.getPromotePrice())) return false;
        if (!getRankPrice().equals(that.getRankPrice())) return false;
        if (!getGoodsThumb().equals(that.getGoodsThumb())) return false;
        if (!getGoodsImg().equals(that.getGoodsImg())) return false;
        if (!getShareUrl().equals(that.getShareUrl())) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getProperties(), that.getProperties())) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(getAlbums(), that.getAlbums());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getGoodsId();
        result = 31 * result + getCatId();
        result = 31 * result + getGoodsName().hashCode();
        result = 31 * result + getGoodsEnglishName().hashCode();
        result = 31 * result + getGoodsBrief().hashCode();
        result = 31 * result + getShopPrice().hashCode();
        result = 31 * result + getCurrencyPrice().hashCode();
        result = 31 * result + getPromotePrice().hashCode();
        result = 31 * result + getRankPrice().hashCode();
        result = 31 * result + getGoodsThumb().hashCode();
        result = 31 * result + getGoodsImg().hashCode();
        result = 31 * result + (int) (getAddTime() ^ (getAddTime() >>> 32));
        result = 31 * result + getShareUrl().hashCode();
        result = 31 * result + (isPromote() ? 1 : 0);
        result = 31 * result + Arrays.hashCode(getProperties());
        result = 31 * result + Arrays.hashCode(getAlbums());
        return result;
    }

    @Override
    public String toString() {
        return "GoodDetailsBean{" +
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
                ", goodsThumb='" + goodsThumb + '\'' +
                ", goodsImg='" + goodsImg + '\'' +
                ", addTime=" + addTime +
                ", shareUrl='" + shareUrl + '\'' +
                ", isPromote=" + isPromote +
                ", properties=" + Arrays.toString(properties) +
                ", albums=" + Arrays.toString(albums) +
                '}';
    }
}
