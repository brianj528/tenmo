package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getAccountBalance(Long userId ) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return balance;
    }

    @Override
    public BigDecimal addToBalance(BigDecimal transferAmount, Long accountId){
        String sql = "UPDATE accounts " +
                "SET balance = balance + " + transferAmount +
                "WHERE account_id = " + accountId + ";";
        try{
           jdbcTemplate.update(sql);
        } catch (DataAccessException e){
            System.out.println("Failed to access data");
        }
        return getAccountBalance(accountId);
    }


    @Override
    public BigDecimal withdrawFromBalance(BigDecimal transferAmount, Long accountId){
        String sql = "UPDATE accounts " +
                "SET balance = balance - " + transferAmount +
                "WHERE account_id = " + accountId + ";";
        try{
            jdbcTemplate.update(sql);
        } catch (DataAccessException e){
            System.out.println("Failed to access data");
        }
        return getAccountBalance(accountId);
    }
    @Override
    public Account getAccountByUserId(Long userId){
        Account account = null;
        SqlRowSet output;
        String sql = "SELECT * FROM accounts WHERE user_id = ?;";
        try{
            output = jdbcTemplate.queryForRowSet(sql, userId);
            if (output.next()){
                account = mapRowToAccount(output);
            }
        }catch(DataAccessException e){
            System.out.println("Error accessing data");
        }
        return account;
    }

    @Override
    public Long getAccountIdByUserId(Long userId){
        Long accountId = null;
        String sql = "SELECT account_id FROM accounts WHERE user_id = ?;";
            accountId = jdbcTemplate.queryForObject(sql, Long.class, userId);
        return accountId;
    }


    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getLong("account_id"));
        account.setUserId(rs.getLong("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}


