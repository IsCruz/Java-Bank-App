package Models;

import Enumerators.ConceptType;
import Enumerators.MovementResult;
import Exceptions.RecordNotFoundException;
import MySql.MySqlConnection;
import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Account{
    //objects
    private static PreparedStatement command;
    private static ResultSet result;
    private CallableStatement procedure;
    //attributes
    private int id;
    private String firstName;
    private String lastName;
    private Date dateOpened;
    private double balance;
    //gets and sets

    public int getId() {
        return this.id;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public Date getDateOpened() {
        return this.dateOpened;
    }
    public double getBalance(){
        return this.balance;
    }
    public String getBalanceFormatted(){
        return NumberFormat.getCurrencyInstance().format(this.balance);
    }
    public String getDateOpenedFormatted(){
        return new SimpleDateFormat("dd-MM-yyyy").format(this.dateOpened);
    }
    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }
    public ArrayList<Movement> getMovements(){
        //List
        ArrayList<Movement> list = new ArrayList<Movement>();
        //query
        String query = "select mov_id, mov_date, mov_id_concept, con_description, con_type, mov_ammount from movements inner join concepts on mov_id_concept = con_id where mov_id_account = ?";
        try {
            //prepare statement
            command = MySqlConnection.getConnection().prepareStatement(query);
            //parameters
            command.setInt(1, this.id);
            //execute query
            result = command.executeQuery();
            //read movements
            while(result.next()){
                //read fields
                int movId = result.getInt("mov_id");
                Date date = result.getDate("mov_date");
                String conId = result.getString("mov_id_concept");
                String description = result.getString("con_description");
                ConceptType type = ConceptType.values()[result.getInt("con_type")];
                double ammount = result.getDouble("mov_ammount");
                //add movement to list
                list.add(new Movement(movId, date, new Concept(conId, description, type), ammount));
            }
        }
        catch(SQLException ex){
            ex.getMessage();
        }
        //return list
        return list;
    }

    //constructors
    
    public Account(int id) throws RecordNotFoundException {
        try {
            //query 
            String query = "select acc_id, acc_first_name, acc_last_name, acc_date_opened, acc_balance from accounts where acc_id = ?";
            //prepare statement
            command = MySqlConnection.getConnection().prepareStatement(query);
            //parameters
            command.setInt(1, id);
            //execute query
            result = command.executeQuery();
            //check if found data
            result.first();
            if (result.getRow() > 0){
                //read values, pass them to attributes
                this.id = result.getInt("acc_id");
                this.firstName = result.getString("acc_first_name");
                this.lastName = result.getString("acc_last_name");
                this.dateOpened = result.getDate("acc_date_opened");
                this.balance = result.getDouble("acc_balance");
            }
            else
                throw new RecordNotFoundException(this.getClass().getName(), String.valueOf(id));
        }
        catch(SQLException ex){
        }
    }
    
    public Account(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = 0; 
    }
    
    
    public MovementResult add(){
        int result = 0;
        
        try{
            String call = "call sp_new_account(?,?,?,?)";
            procedure = MySqlConnection.getConnection().prepareCall(call);
            //parameters
            procedure.setString(1, this.firstName);
            procedure.setString(2, this.lastName);
            procedure.setDouble(3, this.balance);
            //out parameter
            procedure.registerOutParameter(4, java.sql.Types.INTEGER);
            //execute
            procedure.execute();
            //readResult
            result = procedure.getInt(4);
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return MovementResult.values()[result];
    };
    
    public boolean edit(){ return true;};
    
    public boolean delete(){ return true;};
    
    
    public MovementResult deposit(double ammount){
        int result = 0;
        String concept = "02";
        try{
            String call = "call sp_movement(?,?,?,?)";
            procedure = MySqlConnection.getConnection().prepareCall(call);
            //parameters
            procedure.setInt(1, this.id);
            procedure.setString(2, concept);
            procedure.setDouble(3, ammount);
            //out parameter
            procedure.registerOutParameter(4, java.sql.Types.INTEGER);
            //execute
            procedure.execute();
            //readResult
            result = procedure.getInt(4);
            
            if(result == 0) this.balance += ammount;
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return MovementResult.values()[result];
    }
    
    public MovementResult withdrawal(double ammount){
        int result = 0;
        String concept = "51";
        try{
            String call = " call sp_movement(?,?,?,?)";
            procedure = MySqlConnection.getConnection().prepareCall(call);
            //parameters
            procedure.setInt(1, this.id);
            procedure.setString(2, concept);
            procedure.setDouble(3, ammount);
            //out parameter
            procedure.registerOutParameter(4, java.sql.Types.INTEGER);
            //execute
            procedure.execute();
            //readResult
            result = procedure.getInt(4);
            
            if(result == 0) this.balance -= ammount;
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return MovementResult.values()[result];
    }
}
