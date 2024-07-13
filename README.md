# Price Pharmacy Products

Una piattaforma per gestire la ricerca di prodotti venduti nelle varie farmacie in modo efficiente, utilizzando il metodo del simplesso attraverso la libreria OR-Tools.

## Sommario
- [Descrizione](#descrizione)
- [Installazione](#installazione)
  - [Utilizzo di Docker](#utilizzo-di-docker)
  - [Installazione manuale](#installazione-manuale)
- [Utilizzo](#utilizzo)

## Descrizione

Price Pharmacy Products è un'applicazione web che utilizza il metodo del simplesso per ricercare il prodotto con il prezzo più conveniente, dalle farmacie che vendono tale prodotto. 
L'API è corredata di interfaccia grafica, utilizzando Thymeleaf, che permette di registrare Prodotti e Farmacie, e simulare una vendita trovando la/le farmacie migliori nella quale acquistare i prodotti 

## Installazione

L'API può essere utilizzata sia via Docker, sia attraverso la configurazione manuale.

### Utilizzo di Docker

#### Prerequisiti

- [Docker Desktop](https://www.docker.com/products/docker-desktop)

#### Passaggi

1. Clona il repository:

    ```bash
    git clone https://github.com/tuo-username/tuo-repository.git
    cd tuo-repository
    ```
2. Assicurati che Docker Desktop sia attivo

3. Costruisci ed esegui i container Docker:

   Esegui nella repository in cui si trova il programma, il comando:

    ```bash
    docker-compose up --build
    ```

4. L'applicazione sarà disponibile su `http://localhost:8080/sales/home`.

5. Per terminare l'applicazione digitare

    ```bash
    docker-compose up --build
    ```

### Installazione manuale

#### Prerequisiti

- [JDK 17](https://adoptium.net/)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)

#### Configurazione di PostgreSQL

1. Installa PostgreSQL e crea un nuovo database:

    ```sql
    CREATE DATABASE PharmacyDb;
    CREATE USER pharmacyuser WITH ENCRYPTED PASSWORD 'password';
    GRANT ALL PRIVILEGES ON DATABASE pharmacydb TO pharmacyuser;
    ```

2. Modifica il file `application.properties` per configurare la connessione al database:

    ```properties
    datasource:
      url: jdbc:postgresql://localhost:5432/PharmacyDb
      username: postgres
      password: admin
    ```

#### Passaggi

1. Clona il repository:

    ```bash
    git clone https://github.com/tuo-username/tuo-repository.git
    cd tuo-repository
    ```

2. Compila il progetto con Maven:

    ```bash
    ./mvnw clean install
    ```

3. Esegui l'applicazione:

    ```bash
    java -jar target/your-app.jar
    ```

4. L'applicazione sarà disponibile su `http://localhost:8080/sales/home`.

## Utilizzo

### Aggiungere i Prodotti

1. Accedi all'applicazione.
2. Vai alla sezione "Product".
3. Aggiungi i prodotti che desideri mettere disponibili per le varie Farmacie tramite il pulsante 'Add Product'.


### Aggiungere le Farmacie

1. Vai alla sezione "Pharmacies".
2. Clicca sul pulsante 'Add Pharmacy' per aggiungere una Farmacia .
3. Nel Form di aggiunta della Farmacia clicca su 'Add Sale' per aggiungere i prodotti in vendita nella Farmacia.

### Simula l'acquisto di un Prodotto

1. Torna nella sezione "Home".
2. Aggiungi al carrello i prodotti che desideri Acquistare, uno per volta.
3. Clicca sul pulsante Buy.
