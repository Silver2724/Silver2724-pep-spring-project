package com.example.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.Optional;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

 @RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        //make sure that there's not a user with the same name
        if(accountService.existsByUsername(account.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        //register a user and make sure their account is created
        Optional<Account> registeredUser = accountService.register(account);
        if(registeredUser.isPresent()) {
            return ResponseEntity.ok(registeredUser.get());
        } else {
            return ResponseEntity.badRequest().body("Invalid registration details.");
        }      
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> existingAccount = accountService.login(account);

        //check if login was successful
        if(existingAccount.isPresent()) {
            return ResponseEntity.ok(existingAccount.get());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }  

    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        Optional<Message> createdMessage = messageService.createMessage(message);

        //check if message was successfully created
        //response status should be 200 if successful
        //and 400 if unsuccessful
        if(createdMessage.isPresent()) {
            return ResponseEntity.ok(createdMessage.get());
        } else {
            return ResponseEntity.status(400).body("Client error.");
        }  
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getAllMessages() { 
        //get a list containing all messages retrieved from the database
        //list should be empty if there are no messages
        List<Message> allMessages = messageService.getAllMessages();

        //response status shouls always be 200
        return ResponseEntity.ok(allMessages);
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessagesByID(@PathVariable Integer messageId) { 
        //the response body contains the JSON representation of the message defined by the message_id
        Optional<Message> message = messageService.getMessageByID(messageId);
        
        //the response status should always return 200
        //should be empty if there's no message
        return ResponseEntity.ok(message.orElse(null));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<?> deleteMessages(@PathVariable Integer messageId) { 
        //If the message existed, the response body should contain the number of rows updated (1). 
        //The response status should be 200, which is the default.
        //If the message did not exist, the response status should be 200, but the response body should be empty. 
        boolean deleted = messageService.deleteMessageById(messageId);
        if(deleted) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<?> updateMessage(@PathVariable Integer messageId, @RequestBody Message message) { 
        //updated text
        String updatedText = message.getMessageText();

        //if update successful, response body should contain the number of rows updated (1)
        //and response status 200
        //if unsuccessful, the response status should be 400
        boolean wasUpdated = messageService.updateMessage(messageId, updatedText);
        if(wasUpdated) {
            return ResponseEntity.ok(1);
        } else {
            return ResponseEntity.status(400).body("Client error.");
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<?> getMessagesByUser(@PathVariable Integer accountId) { 
        //get list containing all messages by particular user
        List<Message> messagesByUser = messageService.getMessagesByUser(accountId);

        //expected for list to be empty if there are no messages
        //response status should always be 200
        return ResponseEntity.ok(messagesByUser);  
    }

}
