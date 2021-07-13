package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {

    private UserDao userDao;
    private AccountDao accountDao;

    public AccountController(UserDao userDao, AccountDao accountDao){
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "account/{userId}/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@PathVariable Long userId) {
        return accountDao.getAccountBalance(userId);
    }

    @RequestMapping(value = "account/{userId}", method = RequestMethod.GET)
    public Long getAccountId (@PathVariable Long userId) {return accountDao.getAccountIdByUserId(userId);}

    @RequestMapping(value = "users", method = RequestMethod.GET)
    public User[] listAllUsers() {
        List<User> allUsers = userDao.findAll();
        User[] userArray = new User[allUsers.size()];
        userArray = allUsers.toArray(userArray);
        return userArray;
    }

}
