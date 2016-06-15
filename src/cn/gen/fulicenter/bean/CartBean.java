package cn.gen.fulicenter.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/13.
 */
public class CartBean implements Serializable {

    /**
     * id : 7672
     * userName : 7672
     * goodsId : 7672
     *  count : 2
     *  checked : true
     * goods : GoodDetailsBean
     */

    private int id;
    private int userName;
    private int goodsId;
    private int count;
    private boolean checked;
    private String goods;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserName() {
        return userName;
    }

    public void setUserName(int userName) {
        this.userName = userName;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id=" + id +
                ", userName=" + userName +
                ", goodsId=" + goodsId +
                ", count=" + count +
                ", checked=" + checked +
                '}';
    }
}
