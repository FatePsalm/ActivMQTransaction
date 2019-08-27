package com.solace.transactioncommon.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author CG
 * @since 2019-08-23
 */
public class Money implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private Integer myMoney;

    private String userId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Integer getMyMoney() {
        return myMoney;
    }

    public void setMyMoney(Integer myMoney) {
        this.myMoney = myMoney;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Money{" +
        "id=" + id +
        ", myMoney=" + myMoney +
        ", userId=" + userId +
        "}";
    }
}
