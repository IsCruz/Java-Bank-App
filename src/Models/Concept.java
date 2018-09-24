package Models;

import Enumerators.ConceptType;
import MySql.MySqlConnection;
import Exceptions.RecordNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Concept implements Catalog {

    //objects 
    private static PreparedStatement command;
    private static ResultSet result;
    //attributes
    private String id;
    private String description;
    private ConceptType type;

    public ConceptType getType() {
        return type;
    }

    public void setType(ConceptType type) {
        this.type = type;
    }

    public static PreparedStatement getCommand() {
        return command;
    }

    public static void setCommand(PreparedStatement command) {
        Concept.command = command;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Concept() {
        this.id = "";
        this.description = "";
        this.type = ConceptType.DEPOSIT;
    }

    public Concept(String id, String description, ConceptType type) {
        this.id = id;
        this.description = description;
        this.type = type;
    }

    public Concept(String id) throws RecordNotFoundException {
        try {
            //query
            String query = "select con_id,con_description,con_type from concepts where con_id=?";
            //prepare command
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.setString(1, id);
            result = command.executeQuery();
            result.first();
            if (result.getRow() > 0) {
                this.id = result.getString(1);
                this.description = result.getString(2);
                this.type = ConceptType.values()[result.getInt(3)];

            } else {
                throw new RecordNotFoundException(this.getClass().getName(), id);
            }
        } catch (SQLException ex) {
        }

    }

    @Override
    public boolean add() {
        boolean result = false;
        try {
            String query = "insert into Concepts (con_id,con_description,con_type)values(?,?,?)";
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.setString(1, this.id);
            command.setString(2, this.description);
            command.setInt(3, this.type.ordinal());
            if (command.executeUpdate() > 0) {
                result = true;
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public boolean edit() {
        boolean result = false;
        try {
            String query = "update Concepts set con_description = ? , con_type = ? where con_id = ?";
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.setString(1, this.description);
            command.setInt(2, this.type.ordinal());
            command.setString(3, this.id);
            if (command.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public boolean delete() {
        boolean result = false;
        try {
            String query = "delete Concepts where con_id = ?";
            command = MySqlConnection.getConnection().prepareStatement(query);
            command.setString(1, this.id);
            if (command.executeUpdate() > 0) {
                result = true;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    @Override
    public String toString() {
        return this.id + ":" + this.description;
    }

    public static ArrayList<Concept> getAll() {

        ArrayList<Concept> list = new ArrayList<Concept>();
        String query = "select con_id,con_description, con_type from concepts order by con_id";
        try {
            command = MySqlConnection.getConnection().prepareStatement(query);
            result = command.executeQuery();
            while (result.next()) {
                list.add(new Concept(result.getString(1), result.getString(2), ConceptType.values()[result.getInt(3)]));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return list;
    }
}
