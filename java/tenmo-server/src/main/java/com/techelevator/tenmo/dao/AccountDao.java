package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public interface AccountDao {

    public BigDecimal getAccountBalance(Long userId);
    //transfer

    public BigDecimal addToBalance(BigDecimal transferAmount, Long userId);

    public BigDecimal withdrawFromBalance(BigDecimal transferAmount, Long userId);

    public Account getAccountByUserId(Long userId);

    public Long getAccountIdByUserId(Long userId);
}
