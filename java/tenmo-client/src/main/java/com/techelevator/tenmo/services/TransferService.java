package com.techelevator.tenmo.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.techelevator.view.ConsoleService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;




public class TransferService {

    private final String BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        url = BASE_URL;
    }


    public void sendMoney(AuthenticatedUser user) {
        Scanner scanner = new Scanner(System.in);
        AccountService as = new AccountService(BASE_URL, user);
        as.getAllUsers(user);
        System.out.println("Enter ID of user you are sending to (press 0 to cancel)");
       try {
           Long accountFrom = as.getAccountByUserId(currentUser.getUser().getId());
           BigDecimal accountFromBalance = as.getBalance(currentUser.getUser().getId());
           Integer userTo = Integer.parseInt(scanner.nextLine());
           Long accountTo = 0L;
           if(userTo == 0) {
               return;
           }else if (userTo != 0) {
               accountTo = as.getAccountByUserId(userTo);
           }
           if(accountTo == 0) {
               return;
           }else if (!accountFrom.equals(accountTo)) {
               System.out.println("Enter Amount");
               BigDecimal amount = (new BigDecimal(Double.parseDouble(scanner.nextLine())));
               if(amount.compareTo(BigDecimal.ZERO) == 1) {
                   if (amount.compareTo(accountFromBalance) == -1 || amount.compareTo(accountFromBalance) == 0) {
                       Transfer newTransfer = new Transfer();
                       newTransfer.setAccountFrom(accountFrom);
                       newTransfer.setAccountTo(accountTo);
                       newTransfer.setAmount(amount);
                       transferMoney(newTransfer);
                   } else {
                       System.out.println("Insufficient funds to complete that transfer");
                   }
               }else{
                   System.out.println("Transfer must be greater than $0.00");
               }
           } else {
               System.out.println("Wouldn't it be great if you could send money to yourself?");
           }
       } catch (NumberFormatException ex) {
           System.out.println("Invalid user input");
       }
    }
    //this method probably could have used a while loop instead of six if statements

    public void transferMoney(Transfer transfer){
        try {
            restTemplate.exchange(BASE_URL + "transfers", HttpMethod.POST, makeTransferEntity(transfer), String.class).getBody();
            System.out.println("Transfer successful!");
        } catch(RestClientException e){
            System.out.println("Not a valid user ID, please try again");
        }
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;

    }


    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}


