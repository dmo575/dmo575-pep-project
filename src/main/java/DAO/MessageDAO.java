package DAO;

import Model.Message;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import Util.ConnectionUtil;

public class MessageDAO {
    
    public Message insertMessage(Message message) {
        // get a connection to the database trough the ConnectionUtil class
        Connection connection = ConnectionUtil.getConnection();

        //attempt to insert new record into the database
        try {

            // query template
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";

            // get a prepared statement from the connection, set the values on the query template
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            // execute the query
            ps.executeUpdate();

            // get the generated keys of the inserted record, which is the message_id
            ResultSet rs = ps.getGeneratedKeys();

            // if there is a record (there should be one if the INSERT succeeded)
            if(rs.next()) {
                // get the generated id, which is an int on the database
                int message_id = rs.getInt("message_id");

                // return the message to the caller, containing the correct message id
                return new Message(
                    message_id,
                    message.posted_by,
                    message.message_text,
                    message.time_posted_epoch
                );
            }
            
        } catch (Exception e) {
            // print to the console the error message of any potential exception that might have surfaced on the try block
            System.out.println("MessageDAO::insertMessage: " + e.getMessage() + "\n");
        }

        return null;
    }
}
