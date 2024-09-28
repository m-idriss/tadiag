# tadiag

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Setting Up Environment Variables

To configure the environment variables for this project, you will need to create a `.env` file in the root directory of the project. This file will store sensitive information such as API keys, database credentials, and other configuration details that should not be hardcoded into your source code.

### Steps to Create and Complete the `.env` File

1. **Create the `.env` File**: In the root directory of your project, create a new file named `.env`.

2. **Add Environment Variables**: Open the `.env` file and add the necessary environment variables. Each variable should be on a new line in the format `KEY=VALUE`. Below is an example of what your `.env` file might look like:

```plaintext
# API variables from https://rapidapi.com/dpventures/api/wordsapi or other
WORDSAPI_URL=wordsapiv1.p.rapidapi.com
WORDSAPI_KEY=your key

#DB Config
DB_USER=your_db_user
DB_PASSWORD=your_db_password
DB_name=your_db_name

# Other configuration
DEBUG=true
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

``` Example shell script to create a postgres container
docker run --name tadiag-postgres -e POSTGRES_USER=quarkus -e POSTGRES_PASSWORD=quarkus -e POSTGRES_DB=tadiag -p 5432:5432 -d postgres
```

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_** Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/tadiag-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
