package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransfersDao implements TransfersDao {

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserDao userDao;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate, AccountDao accountDao, UserDao userDao){
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @Override
    public List<Transfers> getAllTransfers(int userId) {
        List<Transfers> list = new ArrayList<>();
        String sql = "SELECT t.transfer_id, 'To: '||u.username AS username, t.amount"
        +" FROM transfers t"
        +" INNER JOIN accounts a ON t.account_to = a.account_id"
        +" INNER JOIN users u ON a.user_id = u.user_id"
        +" WHERE t.account_from = ?"
        +" UNION"
        +" SELECT t.transfer_id, 'From: '||u.username AS username, t.amount"
        +" FROM transfers t"
        +" INNER JOIN accounts a ON t.account_from = a.account_id"
        +" INNER JOIN users u ON a.user_id = u.user_id"
        +" WHERE t.account_to = ?"
        +" ORDER BY transfer_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next() ) {
            Transfers transfers = mapRowTorTransferList(results);
            list.add(transfers);
        }
        return list;
    }

    @Override
    public Transfers getTransferById(int transferId) {
        Transfers transfer = new Transfers();
        String sql = "SELECT t.transfer_id, t.amount, tt.transfer_type_desc, ts.transfer_status_desc, ua.username AS user_from, ub.username AS user_to " +
        "FROM transfers t " +
        "INNER JOIN transfer_statuses ts " +
        "ON ts.transfer_status_id = t.transfer_status_id " +
        "INNER JOIN transfer_types tt " +
        "ON t.transfer_type_id = tt.transfer_type_id " +
        "INNER JOIN accounts a ON t.account_from = a.account_id " +
        "INNER JOIN accounts b ON t.account_to = b.account_id " +
        "INNER JOIN users ua ON ua.user_id = a.user_id " +
        "INNER JOIN users ub ON ub.user_id = b.user_id " +
        "WHERE t.transfer_id = ?;";
        SqlRowSet output;
        try{
            output = jdbcTemplate.queryForRowSet(sql, transferId);
            while (output.next()){
                transfer = mapRowForTransferId(output);
            }
        }catch(DataAccessException e){
            System.out.println("Error accessing data");
        }
        return transfer;
    }

    @Override
    public void sendMoney(long accountFrom, long accountTo, BigDecimal amount) {
            String sql = "INSERT INTO transfers (transfer_type_id, transfer_status_id, account_from, account_to, amount)" +
                    "VALUES (2, 2, ?, ?, ?)";
            jdbcTemplate.update(sql, accountFrom, accountTo, amount);
            accountDao.addToBalance(amount, accountTo);
            accountDao.withdrawFromBalance(amount, accountFrom);
        }

    private Transfers mapRowTorTransferList(SqlRowSet results){
        Transfers transfers = new Transfers();
        transfers.setTransferId(results.getInt("transfer_id"));
        transfers.setUsername(results.getString("username"));
        transfers.setAmount(results.getBigDecimal("amount"));
        return transfers;
    }

    private Transfers mapRowForTransferId(SqlRowSet results){
        Transfers requestedTransfer = new Transfers();
        requestedTransfer.setTransferId(results.getInt("transfer_id"));
        requestedTransfer.setUserFromName(results.getString("user_from"));
        requestedTransfer.setUserToName(results.getString("user_to"));
        requestedTransfer.setTransferStatus(results.getString("transfer_status_desc"));
        requestedTransfer.setTransferType(results.getString("transfer_type_desc"));
        requestedTransfer.setAmount(results.getBigDecimal("amount"));

        return requestedTransfer;
    }


    private Transfers mapRowToTransfers(SqlRowSet results) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(results.getInt("transfer_id"));
        transfers.setTransferTypeId(results.getInt("transfer_type_id"));
        transfers.setTransferStatusId(results.getInt("transfer_status_id"));
        transfers.setAccountFrom(results.getLong("account_From"));
        transfers.setAccountTo(results.getLong("account_to"));
        transfers.setAmount(results.getBigDecimal("amount"));
        try{
            transfers.setUserFrom(results.getLong("userFrom"));
            transfers.setUserTo(results.getLong("userTo"));
        }catch (Exception e){
            System.out.println("Could not determine user credentials");
        }
        try{
            transfers.setTransferStatus(results.getString("transferStatus"));
            transfers.setTransferType(results.getString("transferType"));
        } catch (Exception e){
            System.out.println("Could not determine transfer status");
        }
        return transfers;
    }


}
