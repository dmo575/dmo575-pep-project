package Controller;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private ObjectMapper om = new ObjectMapper();
    private AccountService accountService = new AccountService();
    private MessageService messageService = new MessageService();

    // To be used with Jackson. Since Jackson needs of a class that has the same structure as the
    // JSON it tries to parse.
    private static class MessageText {
        public String message_text;
    };

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesOfAccount);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    // Handler for a POST request on route /register.
    // Registers a new user on the database
    private void registerHandler(Context context) {

        try {

            // attempt to extract the json out of the response body and parse its data into an account object
            Account account = om.readValue(context.body(), Account.class);

            // ask the service class to add thew new account (this throws an exception if the account data is not valid)
            Account new_account = accountService.addAccount(account);

            context.json(new_account);
            
        } catch (Exception e) {

            context.status(400);
        }
    }

    // Handler for a POST request on route /login.
    // checks if the user can login or not
    private void loginHandler(Context context) {

        try {

            // get account object from the response body of the HTTP request
            Account account = om.readValue(context.body(), Account.class);

            // ask the service class to add thew new account (this throws an exception if the account data is not valid)
            Account validated_account = accountService.validateAccount(account);

            // return validated account
            context.json(validated_account);
            
        } catch (Exception e) {

            context.status(401);
        }
    }

    private void postMessageHandler(Context context) {

        try {

            // get the message object
            Message message = om.readValue(context.body(), Message.class);

            // ask the service class to add thew new account (this throws an exception if the account data is not valid)
            Message new_message = messageService.addMessage(message);

            // return validated account
            context.json(new_message);
            
        } catch (Exception e) {

            context.status(400);
        }
    }

    private void getMessagesHandler(Context context) {

        try {
            // retrieve messages
            ArrayList<Message> messages = messageService.getAllMessages();
    
            // send messages as a json
            context.json(messages).status(200);
            return;

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        context.status(400);
    }

    private void getMessageByIdHandler(Context context) {

        try {
            // get id from URL params
            String paramMessageId = context.pathParam("message_id");

            // retrieve message from database
            Message message = messageService.getMessageById(Integer.parseInt(paramMessageId));
    
            // if the message exists, return it 200 OK
            if(message != null) {
                context.json(message).status(200);
                return;
            }

            // the response will still be 200 OK even if the message is not found.
            context.status(200);
            return;

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        context.status(400);
    }

    private void deleteMessageByIdHandler(Context context) {

        try {
            // get id from URL path
            String paramMessageId = context.pathParam("message_id");

            // request to delete the message
            Message message = messageService.deleteMessageById(Integer.parseInt(paramMessageId));
    
            // if we got a message back, means the message existed and was deleted
            if(message != null) {

                // return deleted message
                context.json(message).status(200);
                return;
            }

            // response will still be 200 OK if the message didnt exist.
            context.status(200);
            return;
            
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        context.status(400);
    }

    private void patchMessageByIdHandler(Context context) {

        try {
            // get id from URL param
            String paramMessageId = context.pathParam("message_id");

            // get message text from the HTTP body, using Jackson
            MessageText message_text = om.readValue(context.body(), MessageText.class);

            // request to update the message
            Message message = messageService.updateMessageById(Integer.parseInt(paramMessageId), message_text.message_text);
    
            // if we get an update message back, it means success
            if(message != null) {

                // return update message
                context.json(message).status(200);
                return;
            }
            
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        
        context.status(400);
    }

    private void getAllMessagesOfAccount(Context context) {

        try {

            // get account id from URL path
            String paramAccount_id = context.pathParam("account_id");

            // request to retrieve messages
            ArrayList<Message> messages = messageService.getAllMessagesOfAccount(Integer.parseInt(paramAccount_id));
    
            // if we get messages back, it means success
            if(messages != null) {

                // return messages as JSON
                context.json(messages).status(200);
                return;
            }
            
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        context.status(400);
    }
}