package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public Account addAccount(Account account) {

        // validate account data
        if(accountDAO.getAccountByUsername(account.getUsername()) != null || 
        account.username.length() < 1 ||
        account.password.length() < 4) {
            return null;
        }

        // ask DAO to add new account to the database
        return accountDAO.insertAccount(account);
    }

    // returns true of the username and password of the account are valid
    public Account validateAccount(Account account) {

        // request to get an account by username
        Account validated_account = accountDAO.getAccountByUsername(account.username);

        // if we managed to get an account and its password matches the one provided to this method's account argument, the data is valid
        if(validated_account != null && validated_account.password.equals(account.password)) {
            return validated_account;
        }

        return null;
    }

    // check if a user id exists
    public boolean userExists(int id) {

        return accountDAO.getAccountById(id) != null;
    }
}
