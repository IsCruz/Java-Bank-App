package Models;

import Exceptions.RecordNotFoundException;
import MySql.MySqlConnection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

public class Movement {

    //objects
    private static PreparedStatement command;
    private static ResultSet result;

    public static PreparedStatement getCommand() {
        return command;
    }
    //attributes
    private int id;
    private Date date;
    private Concept concept;
    private double ammount;

    //getters & setters
    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Concept getConcept() {
        return concept;
    }

    public double getAmmount() {
        return ammount;
    }
     public String getAmmountFormatted(){
        return NumberFormat.getCurrencyInstance().format(this.ammount);
    }
    public String getDateFormatted(){
        return new SimpleDateFormat("dd-MM-yyyy").format(this.date);
    }

    //constructors
    public Movement(int id) throws RecordNotFoundException {
        try {
            //query
            String query = "select mov_id, move_date, mov_id_concept, mov_ammount from movements where mov_id = ?";
            //prepare statement
            command = MySqlConnection.getConnection().prepareStatement(query);
            //parameters
            command.setInt(1, id);
            //execute query
            result = command.executeQuery();
            //check if found data
            result.first(); //go to first row
            if (result.getRow() > 0) {
                //read values, pass them to attributes
                this.id = result.getInt("mov_id");
                this.date = result.getDate("mov_date");
                this.concept = new Concept(result.getString("mov_id_concept"));
                this.ammount = result.getDouble("mov_ammount");
            } else {
                throw new RecordNotFoundException(this.getClass().getName(), String.valueOf(id));
            }
        } catch (SQLException ex) {
        }
    }

    public Movement(int id, Date date, Concept concept, double ammount) {
        this.id = id;
        this.date = date;
        this.concept = concept;
        this.ammount = ammount;
    }

    @Override
    public String toString() {
        return this.getDateFormatted() + "\t" + this.id + " : " + this.concept + "\t\t" + this.getAmmountFormatted();
    }

}
