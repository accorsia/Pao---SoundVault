# SoundVault

**SoundVault** is a Java-based application for managing a music archive, allowing users to store and retrieve information about artists and albums through an SQLite database.

## System Requirements

- Java 8 or later
- SQLite

## Database Configuration

The application uses **SQLite** as its database. Before running the application, ensure that the database file **`identifier.sqlite`** is present in the project directory.

## Running the Application

To start the application, execute the `Main.java` file.

## Features

### `DatabaseManager` Class

The `DatabaseManager` class handles database access and SQL queries execution. It provides the following methods:

- `DatabaseManager()`: Establishes a connection to the SQLite database.
- `close()`: Closes the database connection.
- `updateDb()`: Updates the database by executing the `"GoldPlatUpdater.sql"` script.
- `executeSqlScript(String scriptFileName)`: Runs a specified SQL script file.
- `restore()`: Restores the database by deleting and recreating tables.
- `executeQuery(String query)`: Executes a read query on the database.
- `executeUpdate(String query)`: Executes an update query on the database.
- `printRs(ResultSet rs)`: Returns a formatted representation of a `ResultSet` object.
- `showMetadata()`: Displays database and table information.

### `DatabaseSafeGetter` Class

The `DatabaseSafeGetter` class provides a static method `getDb()` to obtain a safe instance of `DatabaseManager`. It checks whether the database is accessible and up-to-date, providing guidance if a restore is required.

### `Main` Class

The `Main` class serves as the application entry point. It launches the graphical interface and ensures the database connection is properly closed when the application is terminated.

### `SearchController` Class

The `SearchController` class manages search functionalities within the database, allowing users to search for **artists** and **albums** based on different criteria. It also displays metadata of the selected artists and albums.

### `Utilities` Class

The `Utilities` class provides utility functions for string formatting and debugging. It includes the `debHelp()` method to retrieve execution trace information.

## Project Structure

- `Main.java`: Entry point for launching the application.
- `DatabaseManager.java`: Handles database operations.
- `DatabaseSafeGetter.java`: Ensures safe database access.
- `SearchController.java`: Manages search functionality.
- `Utilities.java`: Provides utility functions.
- `resources/identifier.sqlite`: SQLite database file.
- `resources/GoldPlatUpdater.sql`: SQL script for updating the database.

## Key Functionalities

- Music archive management  
- SQLite database integration  
- SQL query execution  
- Database updates and restoration  
- Artist and album search  
- Table-based result visualization  
- Metadata display for artists and albums  
- Utility methods for data formatting  

## License

This project is licensed under the **MIT License**.
