package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {
    private int transferId;
    private int transferTypeId;
    private int transferStatusId;
    private long accountFrom;
    private long accountTo;
    private BigDecimal amount;
    private String transferType;
    private String transferStatus;
    private long userFrom;
    private long userTo;
    private String username;
    private String userFromName;
    private String userToName;

    public int getTransferId() { return transferId; }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() { return transferTypeId; }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() { return transferStatusId; }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public long getAccountFrom() { return accountFrom; }

    public void setAccountFrom(long accountFrom) {
        this.accountFrom = accountFrom;
    }

    public long getAccountTo() { return accountTo; }

    public void setAccountTo(long accountTo) { this.accountTo = accountTo; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTransferType() { return transferType; }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() { return transferStatus; }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getUserFrom() { return userFrom; }

    public void setUserFrom(long userFrom) {
        this.userFrom = userFrom;
    }

    public long getUserTo() { return userTo; }

    public void setUserTo(long userTo) { this.userTo = userTo; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserFromName() {
        return userFromName;
    }

    public void setUserFromName(String userFromName) {
        this.userFromName = userFromName;
    }

    public String getUserToName() {
        return userToName;
    }

    public void setUserToName(String userToName) {
        this.userToName = userToName;
    }
}
