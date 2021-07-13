package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
@Component
public interface TransfersDao {

    public List<Transfers> getAllTransfers(int userId);
    //lists all transfers for currentUser

    public Transfers getTransferById (int transferId);
    //see details of transfer by transferId

    public void sendMoney(long userFrom, long userTo, BigDecimal amount);

    




}
