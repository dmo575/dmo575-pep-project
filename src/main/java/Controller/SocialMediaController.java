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

            System.out.print("**given: ");
            System.out.print(message + "\n");
            System.out.print("**new_message: ");
            System.out.print(new_message + "\n");

            // return validated account
            context.json(new_message);
            
        } catch (Exception e) {

            context.status(400);
        }
    }

    private void getMessagesHandler(Context context) {

        ArrayList<Message> messages = messageService.getAllMessages();

        context.json(messages);
    }

    private void getMessageByIdHandler(Context context) {

        String paramMessageId = context.pathParam("message_id");
        Message message = messageService.getMessageById(Integer.parseInt(paramMessageId));

        if(message != null) {
            context.json(message);
        }
        context.status(200);
    }

    private void deleteMessageByIdHandler(Context context) {

        String paramMessageId = context.pathParam("message_id");
        Message message = messageService.deleteMessageById(Integer.parseInt(paramMessageId));

        System.out.print(message);
        if(message != null) {
            context.json(message);
        }
        context.status(200);
    }
}