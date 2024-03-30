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

            // attempt to extrac the json out of the response body and parse its data into an account object
            Account account = om.readValue(context.body(), Account.class);
            //System.out.print("received acc object:\n");
            //System.out.print(account);



            // ask the service class to add thew new account (this throws an exception if the account data is not valid)
            Account new_account = accountService.addAccount(account);

            //System.out.print("final acc object:\n");
            //System.out.println(new_account);

            context.json(new_account);
            
        } catch (Exception e) {

            context.status(404);
        }
    }
}