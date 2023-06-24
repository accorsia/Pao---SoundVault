package com.unimn.soundvault;

import java.sql.SQLException;

public class DatabaseConnect {
    public static void main(String[] args) throws SQLException {

        DatabaseManager db = null;
        try {
            System.out.print(Utilities.debHelp() + "> Creating database...");
            db = new DatabaseManager();
            System.out.println("OK!");

//            //  Database is valid --> create form and pass it
//            mainForm = new Form(db);
//            Form.main(new String[]{});


        }
        catch (SQLException e)
        {
            System.out.print(Utilities.debHelp() + "> Something went wrong ---> Restoring database...\n");
            e.printStackTrace();
            //  db.restore();
        }
        finally
        {
            System.out.print(Utilities.debHelp() + "> Closing database...\n");
            db.close();
        }
    }
}
