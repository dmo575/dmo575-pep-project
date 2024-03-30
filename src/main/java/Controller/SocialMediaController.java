package Controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;
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

            //System.out.println("validated_account: ");
            //System.out.println(validated_account);

            context.json(validated_account);
            
        } catch (Exception e) {

            context.status(401);
        }
    }
}