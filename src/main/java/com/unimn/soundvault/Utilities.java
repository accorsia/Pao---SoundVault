package com.unimn.soundvault;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Utilities {

    static StringBuilder reSb = new StringBuilder();

    public static void doBoth(String s) {
        System.out.print(s);    //  debug
        reSb.append(s);         //  function's sake
    }

    public static String printRs(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int nCols = md.getColumnCount();
        String s;

        doBoth("---\t" + md.getTableName(1) + "\t---\n");

        for (int i = 0; rs.next(); i++)    //  scroll rows
        {
            doBoth("[" + i + "]\t");

            for (int j = 1; j <= nCols; j++)     //  scroll columns
                doBoth(md.getColumnName(j) + " = " + rs.getObject(j).toString() + "\t\t|\t\t");

            doBoth("\n");   //  new record -> new line
        }

        return reSb.toString();
    }

    public static String debHelp() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        if (stackTrace.length >= 3) {
            int traceIndex = 2;

            String className = stackTrace[traceIndex].getClassName();
            String methodName = stackTrace[traceIndex].getMethodName();
            String fileName = stackTrace[traceIndex].getFileName();
            int lineNumber = stackTrace[traceIndex].getLineNumber();

            return "[" + fileName.replace(".java", "") + "." + methodName + "() - row " + lineNumber + "]\t";
        }

        return "[Utilis.debHelp()] --> ERROR\n";

    }
}
