package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class AccountService {

    private final String BASE_URL = "http://localhost:8080/";
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    public BigDecimal balance = new BigDecimal(0);

    public AccountService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        url = BASE_URL;
    }

    public Long getAccountByUserId(int userId){
        Long account = 0L;
        try {
            account = restTemplate.exchange(BASE_URL + "account/" + userId, HttpMethod.GET, makeAuthEntity(), Long.class).getBody();
        } catch (RestClientException e){
            System.out.println("Invalid user ID!");
            return 0L;
        }
        return account;
    }


    public BigDecimal getBalance(int id) {
        Long accountId = getAccountByUserId(id);
        try {
            balance = restTemplate.exchange(BASE_URL + "account/" + accountId + "/balance",
                    HttpMethod.GET, makeAuthEntity(), BigDecimal.class).getBody();
        } catch (RestClientException eX) {
            System.out.println("Rest client exception error!");
        }
        return balance;
    }

    public void displayTransfers(AuthenticatedUser currentUser){
        Transfer[] transfers = null;
        System.out.println("--------------------------------------------------");
        System.out.println("Transfers                                         ");
        System.out.println("ID             From/To                    Amount  ");
        System.out.println("--------------------------------------------------");
        Long accountId = getAccountByUserId(currentUser.getUser().getId());
        transfers = restTemplate.exchange(BASE_URL + "transfers/" + accountId, HttpMethod.GET, makeAuthEntity(), Transfer[].class ).getBody();
        for (Transfer transfer: transfers){
            System.out.println(transfer.getTransferId() + "          " + transfer.getUsername() + "                " + transfer.getAmount());
        }
    }

    public void displayTransferInformation(AuthenticatedUser currentUser) {
        displayTransfers(currentUser);
        Transfer requestedTransfer = null;
        String currentUserName = (currentUser.getUser().getUsername());
        Scanner userInput = new Scanner(System.in);
        try {
            System.out.println("Enter ID of transfer to see additional details (0 to cancel)");
            Long transferId = Long.parseLong(userInput.next());
            if (transferId == 0){
                return;}
            requestedTransfer = restTemplate.exchange(BASE_URL + "transfers/details/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfer.class).getBody();
              if (requestedTransfer.getTransferId() == 0) {
                System.out.println();
                System.out.println("That is an invalid transfer ID!");
            } else if (requestedTransfer.getUserToName().equals(currentUserName) || requestedTransfer.getUserFromName().equals(currentUserName)) {
                System.out.println();
                System.out.println("--------------------------------------------------");
                System.out.println("Transfer Details                                  ");
                System.out.println("--------------------------------------------------");
                System.out.println("Id: " + requestedTransfer.getTransferId());
                System.out.println("From: " + requestedTransfer.getUserFromName());
                System.out.println("To: " + requestedTransfer.getUserToName());
                System.out.println("Type: " + requestedTransfer.getTransferType());
                System.out.println("Status: " + requestedTransfer.getTransferStatus());
                System.out.println("Amount: $" + requestedTransfer.getAmount());
            }
        } catch (NumberFormatException | NullPointerException eX) {
            System.out.println();
            System.out.println("That is an invalid transfer ID!");
        }
    }


    public void getAllUsers(AuthenticatedUser currentUser){
        User[] users = null;
        System.out.println("--------------------------------------------------");
        System.out.println("Users                                             ");
        System.out.println("ID             Name                               ");
        System.out.println("--------------------------------------------------");
        try {
            users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthEntity(), User[].class).getBody();
            for(User user: users) {
                if (user.getId() != (currentUser.getUser().getId())) {
                    System.out.println(user.getId() + "         " + user.getUsername());
                }
            }
            System.out.println();
        } catch (Exception e){
            System.out.println("This happened: " + e.getMessage() + " and " + e.getCause());
        }
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}







