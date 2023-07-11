# SoundVault

**SoundVault** è un'applicazione per la gestione di un archivio musicale.

## Requisiti di sistema

- Java 8 o versione successiva
- SQLite

## Configurazione del database

Il database utilizzato dall'applicazione è SQLite. Prima di avviare l'applicazione, assicurarsi che il file del database "`identifier.sqlite`" sia presente nella directory del progetto.

## Avvio dell'applicazione

Per avviare l'applicazione, eseguire il file "`Main.java`".

## Funzionalità

### Classe `DatabaseManager`

La classe `DatabaseManager` si occupa di gestire l'accesso al database e l'esecuzione di query SQL. Contiene i seguenti metodi:

- `DatabaseManager()`: Costruttore che stabilisce una connessione al database SQLite.
- `close()`: Chiude la connessione al database.
- `updateDb()`: Aggiorna il database eseguendo lo script "`GoldPlatUpdater.sql`".
- `executeSqlScript(String scriptFileName)`: Esegue uno script SQL specificato dal nome del file.
- `restore()`: Ripristina il database eliminando e ricreando le tabelle.
- `executeQuery(String query)`: Esegue una query di lettura sul database.
- `executeUpdate(String query)`: Esegue una query di aggiornamento sul database.
- `printRs(ResultSet rs)`: Restituisce una rappresentazione formattata di un oggetto `ResultSet`.
- `showMetadata()`: Mostra le informazioni sul database e sulle tabelle.

### Classe `DatabaseSafeGetter`

La classe `DatabaseSafeGetter` fornisce un metodo statico `getDb()` per ottenere un'istanza di `DatabaseManager` in modo sicuro. Controlla se il database è accessibile e aggiornato, altrimenti fornisce indicazioni su come ripristinarlo.

### Classe `Main`

La classe `Main` è l'entry point dell'applicazione. Si occupa di avviare l'interfaccia grafica e gestisce la chiusura del database quando la finestra viene chiusa.

### Classe `SearchController`

La classe `SearchController` gestisce le funzionalità di ricerca nel database. Permette di cercare artisti e album in base a diversi criteri e visualizza i risultati in una tabella. Mostra anche i metadati degli artisti e degli album selezionati.

### Classe `Utilities`

La classe `Utilities` contiene metodi di utilità per la formattazione e la gestione delle stringhe. In particolare, contiene il metodo `debHelp()` per ottenere informazioni di debug sul punto di esecuzione del codice.

## Caratteristiche

- Gestione di un archivio musicale.
- Accesso al database SQLite.
- Esecuzione di query SQL.
- Aggiornamento del database.
- Ripristino del database.
- Ricerca di artisti e album.
- Visualizzazione dei risultati in una tabella.
- Mostra metadati degli artisti e degli album.
- Metodi di utilità per la formattazione e la gestione delle stringhe.

## Licenza

Questo progetto è concesso in licenza secondo i termini della licenza MIT.
