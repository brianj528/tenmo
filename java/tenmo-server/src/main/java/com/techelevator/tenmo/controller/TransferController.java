package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransfersDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransfersDao transfersDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransfersDao transfersDao, AccountDao accountDao, UserDao userDao){
        this.transfersDao = transfersDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
    public Transfers[] getMyTransfers (@Valid @PathVariable int id){
        List<Transfers> output = transfersDao.getAllTransfers(id);
        Transfers[] transferArray = new Transfers[output.size()];
        transferArray = output.toArray(transferArray);
        return transferArray;
    }

    @RequestMapping(path = "transfers/details/{transferId}", method =RequestMethod.GET)
    public Transfers getSelectedTransfer (@Valid @PathVariable int transferId){
        Transfers selectedTransfer = transfersDao.getTransferById(transferId);
        return selectedTransfer;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "transfers", method = RequestMethod.POST)
    public void sendTransfer (@RequestBody Transfers outgoingTransfer){
        Transfers thisTransfer = outgoingTransfer;
        try {
            transfersDao.sendMoney(thisTransfer.getAccountFrom(), thisTransfer.getAccountTo(), thisTransfer.getAmount());
            System.out.println("Transfer succeeds");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }



}
