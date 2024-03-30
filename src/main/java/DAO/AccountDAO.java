package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    public Account insertAccount(Account account) {

        // get a connection to the database trough the ConnectionUtil class
        Connection connection = ConnectionUtil.getConnection();

        //attempt to insert new record into the database
        try {

            // query template
            String sql = "INSERT INTO Account(username, password) VALUES(?, ?)";

            // get a prepared statement from the connection, set the values on the query template
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());

            // execute the query
            ps.executeUpdate();

            // get the generated keys of the inserted record, which is the account_id that is set to autogenerate and autoincrement on the database side.
            ResultSet rs = ps.getGeneratedKeys();

            // if there is a record (there should be one if the INSERT succeeded)
            if(rs.next()) {
                // get the generated id, which is an int on the database
                int new_account_id = rs.getInt("account_id");

                // return the account to the called, containing the correct account id
                return new Account(
                    new_account_id, 
                    account.username,
                    account.password
                );
            }
            
        } catch (Exception e) {
            // print to the console the error message of any potential exception that might have surfaced on the try block
            System.out.println("AccountDAO::insertAccount: " + e.getMessage() + "\n");
        }

        return null;
    }

    public ArrayList<Account> getAllAccounts(){

        // get a connection to the database trough the ConnectionUtil class
        Connection connection = ConnectionUtil.getConnection();

        try {

            ArrayList<Account> accs = new ArrayList<Account>();
            
            // prepare query
            String sql = "SELECT * FROM Account";

            // prepare statement
            PreparedStatement ps = connection.prepareStatement(sql);

            // execute statement
            ps.executeQuery();

            // retrieve results 
            ResultSet rs = ps.getResultSet();

            // loop trough records and add them to array list accs
            while(rs.next()) {
                accs.add(new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password"))
                );
            }

            // return array list accs
            return accs;

        } catch (Exception e) {
            // print to the console the error message of any potential exception that might have surfaced on the try block
            System.out.println("AccountDAO::getAllAccounts: " + e.getMessage() + "\n");
        }

        return null;
    }

    public Account getAccountByUsername(String username) {

        // get a connection to the database trough the ConnectionUtil class
        Connection connection = ConnectionUtil.getConnection();

        try {

            // prepare query
            String sql = "SELECT * FROM Account WHERE username=?";

            // prepare statement
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);

            // execute statement
            ps.executeQuery();

            // retrieve result
            ResultSet rs = ps.getResultSet();

            if(rs.next()) {
                return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
                );
            }

        } catch (Exception e) {
            // print to the console the error message of any potential exception that might have surfaced on the try block
            System.out.println("AccountDAO::getAccountByUsername: " + e.getMessage() + "\n");
        }

        return null;
    }
}
