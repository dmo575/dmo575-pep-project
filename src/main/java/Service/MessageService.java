package Service;

import DAO.MessageDAO;
import Model.Message;
import java.util.ArrayList;

public class MessageService {
    private MessageDAO messageDAO = new MessageDAO();
    private AccountService accountUser = new AccountService();

    public Message addMessage(Message message) throws Exception {

        // validate message
        if(message.message_text == "" || 
        message.message_text.length() > 255 ||
        !accountUser.userExists(message.posted_by)) {
            
            throw new Exception("Message failed validity check");
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

        if(message != null) {
            messageDAO.deleteMessageById(id);
            return message;
        }
        
        return null;
    }
    
}
