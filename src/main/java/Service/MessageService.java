package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.ArrayList;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();
    private AccountService accountUser = new AccountService();

    public Message addMessage(Message message) {

        // validate message data
        if(message.message_text == "" || 
        message.message_text.length() > 255 ||
        !accountUser.userExists(message.posted_by)) {
            
            return null;
        }

        // asks dao to send message to the database
        return messageDAO.insertMessage(message);
    }

    public ArrayList<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) {

        Message message = getMessageById(id);

        // if we got a message back, request a deletion and return it to caller
        if(message != null) {
            messageDAO.deleteMessageById(id);
            return message;
        }

        return null;
    }

    public Message updateMessageById(int id, String message_text) {


        // validate message data
        if(message_text.length() < 1 ||
        message_text.length() > 255) {
            return null;
        }

        // get current message by its id
        Message message = getMessageById(id);
        
        // if the message exists, then we update it and send back
        // the new version
        if(message != null) {
            messageDAO.updateMessageById(id, message_text);
            return new Message(id, message.posted_by, message_text, message.time_posted_epoch);
        }
        
        return null;
    }

    public ArrayList<Message> getAllMessagesOfAccount(int account_id) {

        return messageDAO.getAllMessagesOfAccount(account_id);
    }
    
}
