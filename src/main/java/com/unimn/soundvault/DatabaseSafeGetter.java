package com.unimn.soundvault;

import java.sql.SQLException;

public class DatabaseSafeGetter {
    public static DatabaseManager main() throws SQLException {

        DatabaseManager db = null;
        try {
            System.out.print(Utilities.debHelp() + "> Connecting to database...");
            db = new DatabaseManager();
            System.out.println("OK!");

        }
        catch (SQLException e)
        {
            System.out.print(Utilities.debHelp() + "> Something went wrong ---> Restoring database...\n");
            e.printStackTrace();
            //  db.restore();
        }

        return db;
    }
}
