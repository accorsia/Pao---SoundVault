package com.unimn.soundvault;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseSafeGetter {
    public static DatabaseManager db;;

    public static DatabaseManager getDb() {

        try
        {
            db = new DatabaseManager();
            db.updateDb();

            return db;
        }
        catch (SQLException e)
        {
            System.out.print(Utilities.debHelp() + "> Something went wrong ---> You should restore the database...\n");
            e.printStackTrace();
            //  db.restore();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Errore durante l'accesso al file", e);
        }

        return null;
    }
}
