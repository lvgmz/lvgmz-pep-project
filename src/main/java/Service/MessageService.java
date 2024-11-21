package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }
    
    // #3 create new message
    public Message addMessage(Message message){
        if (message.getMessage_text() == ""){
            return null;
        }

        //need to add other cases

        return messageDAO.insertMessage(message);
    }

    // #4 get all messages
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    // #5 get message by id
    public Message getMessageById(int id){
        return messageDAO.getMessageById(id);
    }

    // #6 delete message by id
    public Message deleteMessageById(int id){
        Message deletedMsg = getMessageById(id);
        messageDAO.deleteMessageById(id);
        return deletedMsg;
    }

    // #7 update message
    public Message updateMessage(int messageID, String messageText){
        messageDAO.updateMessage(messageID, messageText);
        Message updatedMessage = getMessageById(messageID);
        return updatedMessage;
    }

    // #8 get message by account id
    public List<Message> getMessageByAccountId(int accountId){
        return messageDAO.getMessagesByAccountId(accountId);
    }
}
