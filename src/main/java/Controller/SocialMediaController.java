package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountRegisterHandler);
        app.post("/login", this::postAccountLoginHandler);
        app.post("/messages", this::postMessageMessagesHandler);
        app.get("/messages", this::getMessageMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessageByAccountIdHandler);

        return app;
    }

    private void postAccountRegisterHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount!=null){
            ctx.json(om.writeValueAsString(addedAccount));
        }else{
            ctx.status(400);
        }
    }

    private void postAccountLoginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Account account = om.readValue(ctx.body(), Account.class);
        Account accountCheck = accountService.checkAccount(account); //may need to change this with username,password
        if(accountCheck!=null){
            ctx.json(om.writeValueAsString(accountCheck));
        }else{
            ctx.status(401);
        }
    }

    private void postMessageMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage!=null){
            ctx.json(om.writeValueAsString(addedMessage));
        }else{
            ctx.status(400);
        }
    }
    
    private void getMessageMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }
    
    
    private void getMessageByIdHandler(Context ctx){
        String messageID = ctx.pathParam("message_id");
        if (messageService.getMessageById(Integer.parseInt(messageID)) == null){
            return;
        }
        ctx.json(messageService.getMessageById(Integer.parseInt(messageID)));
    }

    private void deleteMessageByIdHandler(Context ctx){
        String messageID = ctx.pathParam("message_id");
        if (messageService.getMessageById(Integer.parseInt(messageID)) == null){
            return;
        }
        ctx.json(messageService.deleteMessageById(Integer.parseInt(messageID)));
    }

    private void patchMessageByIdHandler(Context ctx)throws JsonProcessingException{
        ObjectMapper om = new ObjectMapper();
        Message message = om.readValue(ctx.body(), Message.class);
        String messageID = ctx.pathParam("message_id");

        Message newMessage = messageService.updateMessage(Integer.parseInt(messageID), message.getMessage_text());

        
        if(newMessage == null){
            ctx.status(400);
            return;
        }
        if(message.getMessage_text()==""){
            ctx.status(400);
            return;
        }
        if(message.getMessage_text().length() > 255){
            ctx.status(400);
            return;
        }
        else{
            ctx.json(newMessage);
        }
    }

    public void getMessageByAccountIdHandler(Context ctx){
        String accountId = ctx.pathParam("account_id");
        List<Message> messages = messageService.getMessageByAccountId(Integer.parseInt(accountId));
        ctx.json(messages);
    }
}