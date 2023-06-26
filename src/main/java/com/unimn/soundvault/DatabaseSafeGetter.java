package com.unimn.soundvault;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseSafeGetter {
    public static DatabaseManager getDb() {

        DatabaseManager db;
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
            return null;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }
}
