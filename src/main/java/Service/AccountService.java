package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO = new AccountDAO();

    public Account addAccount(Account account) throws Exception{

        // validate account data
        if(accountDAO.getAccountByUsername(account.getUsername()) != null || 
        account.username.length() < 1 ||
        account.password.length() < 4) {
            throw new Exception("Account validation failed.");
        }

        // ask DAO to add new account to the database
        return accountDAO.insertAccount(account);
    }
}
