# Satori Backend

Satori Backend provides a REST API for retrieving insurance submissions.

## Local Development Environment
To set up the local development environment, ensure that you have a 
[PostgreSQL server](https://www.postgresql.org/download/) running on port 5432 
(configurable [here](src%2Fsatori_backend%2Fdiplomat%2Fdb%2Fconfig.clj)). 

1. Customize your database credentials in the [config.clj](src%2Fsatori_backend%2Fdiplomat%2Fdb%2Fconfig.clj) file.
2. Create a new database named `satori` by executing the following command 
   replacing `<USERNAME>` with the correct PostgreSQL user.
```bash
psql -U <USERNAME> -d postgres -c "CREATE DATABASE satori"
```
3. Create empty tables for the database
``` bash
psql -U <USERNAME> -d satori -a -f resources/init.sql/001_create_table.sql
```
4.Load the database with 10,000 mock `submission` rows
```bash
psql -U <USERNAME> -d satori -a -f resources/init.sql/002_insert_data.sql
```

## Starting the service
You will need to install `leiningen` in order to execute the Clojure project. You can install it via your favorite
[package manager](https://wiki.leiningen.org/Packaging) or [manually](https://leiningen.org/#install).

You may start the service with the following command
```bash
lein run
```

## Running tests
You may execute unit tests with
```bash
lein test
```

## Documentation
 - [API reference](doc%2Fapi_reference.md)
 - [Project Structure](doc%2Fproject_structure.md)
 - 