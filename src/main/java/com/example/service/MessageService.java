package com.example.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;
import java.util.List;

@Service
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Optional<Message> createMessage(Message message) {
        //check if user posting the message exists
        Optional<Account> user = accountRepository.findById(message.getPostedBy());
        if(user.isEmpty()) { return Optional.empty(); }

        //The creation of the message will be successful if and only if the message_text is not blank, 
        //is under 255 characters, and posted_by refers to a real, existing user. 
        String messageText = message.getMessageText();

         if(message == null || messageText == null || messageText.trim().isEmpty() || messageText.length() > 255) {
            return Optional.empty();
        }

        //save the message
        Message savedMessage = messageRepository.save(message);
        return Optional.of(savedMessage);
    }

    public List<Message> getAllMessages() {
        //get all messages from the database
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageByID(Integer messageId) {
        //return the message b its id
        return messageRepository.findById(messageId);
    }

    public boolean deleteMessageById(Integer messageId) {
        //check if id exists
        //if so, delete message by its id
        if(messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }

        return false;
    }

    public boolean updateMessage(Integer messageId, String text) {
        //check if updated text is valid
        if(text == null || text.trim().isEmpty() || text.length() > 255) { return false; }

        //make sure message exists
        Optional<Message> updatedText = messageRepository.findById(messageId);
        if(updatedText.isEmpty()) { return false; }

        //make sure the updated text is saved to the respository
        Message existingMessage = updatedText.get();
        existingMessage.setMessageText(text);
        messageRepository.save(existingMessage);

        return true;
    }

    public List<Message> getMessagesByUser(Integer accountId) {
        //find all messages by user give accountId
        return messageRepository.findByPostedBy(accountId);
    }
}
