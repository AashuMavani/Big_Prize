package com.app.bigprize.Async.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Big_Wallet_ListItem implements Serializable {

    @SerializedName("earning_type")
    private String earningType;
    @Expose
    private String entryDate;
    @Expose
    private String scratchedDate;
    @Expose
    private String id;
    @Expose
    private String image;
    @Expose
    private String points;
    @Expose
    private String settleAmount;
    @Expose
    private String title;
    @Expose
    private String type;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String referEarningPoint;
    @Expose
    private String isDeliverd;
    @Expose
    private String couponeCode;
    @Expose
    private String mobileNo;
    @Expose
    private String txnID;
    @Expose
    private String comment;
    @Expose
    private String icon;
    @Expose
    private String deliveryDate;
    @Expose
    private String icone;// task icon
    @Expose
    private String campaignName;
    @Expose
    private String taskTitle;// scratch card history
    @Expose
    private String scratchCardPoints;// scratch card history
    @Expose
    private String couponCode;// giveaway history
    @Expose
    private String raisedTicketId;
    @Expose
    private String withdraw_type;
    @Expose
    private String emailID;
    @Expose
    private String paymentFrom;
    @Expose
    private String amount;

    public void setScratchedDate(String scratchedDate) {
        this.scratchedDate = scratchedDate;
    }

    public void setIcone(String icone) {
        this.icone = icone;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public void setScratchCardPoints(String scratchCardPoints) {
        this.scratchCardPoints = scratchCardPoints;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentFrom() {
        return paymentFrom;
    }

    public void setPaymentFrom(String paymentFrom) {
        this.paymentFrom = paymentFrom;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getWithdraw_type() {
        return withdraw_type;
    }

    public void setWithdraw_type(String withdraw_type) {
        this.withdraw_type = withdraw_type;
    }

    public String getRaisedTicketId() {
        return raisedTicketId;
    }

    public void setRaisedTicketId(String raisedTicketId) {
        this.raisedTicketId = raisedTicketId;
    }

    public String getEarningType() {
        return earningType;
    }

    public void setEarningType(String earningType) {
        this.earningType = earningType;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(String settleAmount) {
        this.settleAmount = settleAmount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getReferEarningPoint() {
        return referEarningPoint;
    }

    public void setReferEarningPoint(String referEarningPoint) {
        this.referEarningPoint = referEarningPoint;
    }

    public String getIsDeliverd() {
        return isDeliverd;
    }

    public void setIsDeliverd(String isDeliverd) {
        this.isDeliverd = isDeliverd;
    }

    public String getCouponeCode() {
        return couponeCode;
    }

    public void setCouponeCode(String couponeCode) {
        this.couponeCode = couponeCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getTxnID() {
        return txnID;
    }

    public void setTxnID(String txnID) {
        this.txnID = txnID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getIcone() {
        return icone;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getScratchCardPoints() {
        return scratchCardPoints;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public String getScratchedDate() {
        return scratchedDate;
    }
}

