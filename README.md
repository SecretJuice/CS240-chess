# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)]([https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGcnQOIEMC2MDKkBHAV0gDsBjGAERAwHMAnbAKA3OAHsG5wQzhmABwwNQ5EMNLBoACQykAJlAZIhIsRPnT8DAG4hKq4aIOap0KgEEA8qoUZgGAEYYk1AELMvjDkUHQAYidwEmgAJUg6ECRgSG4AgHd4MEgvZlhefmgAWgA+WXklOKQALgBtAAVrXAAVAF1oAHoiNwYAHVIACha40mxIABpoYRQErgUhyCwMEHAASmY5RWUkPKWilRKqSHIOBRgAVVboAFk9yHBOhLB4aH3d-e5ycFckBfWVvJ19QxKGSOisQYRzi3VaC2+BmQeSsthKdEgwBBDDBcQAdD0GH0cAtYatclR3CVoLgAKIAGVJsBq0CIIAU0AAYmFrCdaccOgB1GSksKk9m9frQAC80AA-ABuaAdVEMDGtbGQXE2VZfOI-ZAlUhEcDgZiQwwwlUlcj-ByQZGy5W2GFEgCSADkyWEaZjFUMRkgxgwJtApjNwB1HTVrAKGBKZZj5YKcEMo57vb6o-7ZtbVbkDZq6Qp9eqofi8SazbFLERgPBuvS07aSo7na76UMMGX4DUOABrMhBh0h6DN8vMPE5NV6fMlfvwXOjw1rQorcctxZz4qz5bFEqkijnaCl8vQaAAKVw1gd0CuNz9W8e0Ger3ey5U2TyGT4UhKACYAAyfjoAbwnAC+zBkDmzA+H4gRcPICLQOSHBRKQgRJCkXgvvwq4bKUlTVPUTRuCgIAcKQf5uv0HqvImQEfCuuTUZs2wPIcxxnPsep0fimalOA8EgKQlqYhCeaGgSxoIkirRWoOKo1mSlLUrS9JMiybKYlyPJ8mGioiuKHSWA6VDDBR4zaZKZ5RqRsZhmiCbjNWI4aqU2ZTg5RpwqakDmruFbZtWBL2k6vINr6E5tp2xGkMGoYThKUm2MOGZCZqE7OfmT60Q+pTJexGHzpujE7i2+5HieZ7XHuZCMU8LwoPea6Ps+mRvl+P6kP+LZAcBihgQwvj+AEjCQGQsHwb40iJMksSoY1wA5euZRUBSpI1KSDSNPhSCEeFIUdmQS51fi7FbDs24yB5168ZwBV7ixFx7Zh9ljiB+AEURnQToJ07QiJcKsYikBeW9La+YSWyLctSmsn2LZqby-LbWFJlSjKE5ovDZB2Qln2lLoHD0ilM7pftJQ43j2UNa+wAft+Xggd1vWBP8DLkoC6D9Kg40oek02zZsZRoEtq10P0HRo6QXiNI0Xn8AY5p3Z8hOYUd+WnRg51SBwV23DdbEZQ9vwTtL5Dms9G2ve9+NfYWYkA+beIyYt8nZhDbITjDGmi4jZ4o6LGOcSUTmcWlh1OXLNF+2i1u6pgOBIJ0H0ud9pSR+A0fIHHsUFkSslUjSABUzvQELOAxXbeuamURfIHUFvpodFds9X2UK7lV4wJdqeHsep4iEwACeof1bkaFNd+f71zHdQdbT4F9YIRAMIIUBwMWMAdxzk1cxTPNYVUtSC8LfQtqFXatZXDr9B1EtS1IMuTU3h0MSdZ1xNAF0a15pznDr+1l5lLaG8bZApsujm0DoneEiIbZAwzvbOSQUC6u1INyWGUNyzH0QqKUygNyyoyPjtUgvtEqOTxoHbe-s8YDwOhlJW253B7F7q-dWrMcCf1YpQ3+RYPKxFTp0Su8dUrgPcuaHhfCYF+VrAFF0hd+jnxYZFaRxczyVwjjI-ohCsbEkrnaKgNcg7UIUZAbR7Dyb8Cpi1X8AAiLRVALEdU6jmGegRe4XG4gkQ8uNEJr2CGwdsm90LNzmhUA4uFGiVxFngsKf5F4YGcQwWAHBuIMCGNYy+kt-43yNnfXWAT6LHX2LIZ+3A36a1YbdJufsDYZMAS9EB0CwFW0gS2bB8BgZZwdvA5kkNEHIPdhEoamCpTNNwWg-B6iHLkJzKQnJxDQLMAlgeDxzDUjlKISUAAVh4nh0TYnxMSck-o2i4xVi8PU40hdEQiIOVQVpJRs7yXzp0tkYSkHqX5NYxGMCOGVxOUQ1yjlBD2G4f0Xh-QyhYD2CAAAZnwBQdQbkHAqFYcGzyyQ0m8eQdsAB9bZL9MEDA6MhWI2KXixJMm7N5VzEYymUeirFOKkkGLRISyAxKYlxH2TgFROBtELE+ZjcZJNJm-LIYK4xQ9ppmJpl1Rx-V-gMIyB5bglhBB9WZVNLe0zygLUpMtVaCgnDsP5WOZ4irlWL1vptdOpy3JQBEMiWONyahhAOA6WAlhwaYhLtJDhorrWlBNSILyDqxEgydS6t14Nop8r9r635gjbUMFTsGu24iw2uvdRS4u0bVmxqxnoomoqybioppK+xXggA)https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=C4S2BsFMAIGEAtIGcnQOIEMC2MDKkBHAV0gDsBjGAERAwHMAnbAKA3OAHsG5wQzhmABwwNQ5EMNLBoACQykAJlAZIhIsRPnT8DAG4hKq4aIOap0KgEEA8qoUZgGAEYYk1AELMvjDkUHQAYidwEmgAJUg6ECRgSG4AgHd4MEgvZlhefmgAWgA+WXklOKQALgBtAAVrXAAVAF1oAHoiNwYAHVIACha40mxIABpoYRQErgUhyCwMEHAASmY5RWUkPKWilRKqSHIOBRgAVVboAFk9yHBOhLB4aH3d-e5ycFckBfWVvJ19QxKGSOisQYRzi3VaC2+BmQeSsthKdEgwBBDDBcQAdD0GH0cAtYatclR3CVoLgAKIAGVJsBq0CIIAU0AAYmFrCdaccOgB1GSksKk9m9frQAC80AA-ABuaAdVEMDGtbGQXE2VZfOI-ZAlUhEcDgZiQwwwlUlcj-ByQZGy5W2GFEgCSADkyWEaZjFUMRkgxgwJtApjNwB1HTVrAKGBKZZj5YKcEMo57vb6o-7ZtbVbkDZq6Qp9eqofi8SazbFLERgPBuvS07aSo7na76UMMGX4DUOABrMhBh0h6DN8vMPE5NV6fMlfvwXOjw1rQorcctxZz4qz5bFEqkijnaCl8vQaAAKVw1gd0CuNz9W8e0Ger3ey5U2TyGT4UhKACYAAyfjoAbwnAC+zBkDmzA+H4gRcPICLQOSHBRKQgRJCkXgvvwq4bKUlTVPUTRuCgIAcKQf5uv0HqvImQEfCuuTUZs2wPIcxxnPsep0fimalOA8EgKQlqYhCeaGgSxoIkirRWoOKo1mSlLUrS9JMiybKYlyPJ8mGioiuKHSWA6VDDBR4zaZKZ5RqRsZhmiCbjNWI4aqU2ZTg5RpwqakDmruFbZtWBL2k6vINr6E5tp2xGkMGoYThKUm2MOGZCZqE7OfmT60Q+pTJexGHzpujE7i2+5HieZ7XHuZCMU8LwoPea6Ps+mRvl+P6kP+LZAcBihgQwvj+AEjCQGQsHwb40iJMksSoY1wA5euZRUBSpI1KSDSNPhSCEeFIUdmQS51fi7FbDs24yB5168ZwBV7ixFx7Zh9ljiB+AEURnQToJ07QiJcKsYikBeW9La+YSWyLctSmsn2LZqby-LbWFJlSjKE5ovDZB2Qln2lLoHD0ilM7pftJQ43j2UNa+wAft+Xggd1vWBP8DLkoC6D9Kg40oek02zZsZRoEtq10P0HRo6QXiNI0Xn8AY5p3Z8hOYUd+WnRg51SBwV23DdbEZQ9vwTtL5Dms9G2ve9+NfYWYkA+beIyYt8nZhDbITjDGmi4jZ4o6LGOcSUTmcWlh1OXLNF+2i1u6pgOBIJ0H0ud9pSR+A0fIHHsUFkSslUjSABUzvQELOAxXbeuamURfIHUFvpodFds9X2UK7lV4wJdqeHsep4iEwACeof1bkaFNd+f71zHdQdbT4F9YIRAMIIUBwMWMAdxzk1cxTPNYVUtSC8LfQtqFXatZXDr9B1EtS1IMuTU3h0MSdZ1xNAF0a15pznDr+1l5lLaG8bZApsujm0DoneEiIbZAwzvbOSQUC6u1INyWGUNyzH0QqKUygNyyoyPjtUgvtEqOTxoHbe-s8YDwOhlJW253B7F7q-dWrMcCf1YpQ3+RYPKxFTp0Su8dUrgPcuaHhfCYF+VrAFF0hd+jnxYZFaRxczyVwjjI-ohCsbEkrnaKgNcg7UIUZAbR7Dyb8Cpi1X8AAiLRVALEdU6jmGegRe4XG4gkQ8uNEJr2CGwdsm90LNzmhUA4uFGiVxFngsKf5F4YGcQwWAHBuIMCGNYy+kt-43yNnfXWAT6LHX2LIZ+3A36a1YbdJufsDYZMAS9EB0CwFW0gS2bB8BgZZwdvA5kkNEHIPdhEoamCpTNNwWg-B6iHLkJzKQnJxDQLMAlgeDxzDUjlKISUAAVh4nh0TYnxMSck-o2i4xVi8PU40hdEQiIOVQVpJRs7yXzp0tkYSkHqX5NYxGMCOGVxOUQ1yjlBD2G4f0Xh-QyhYD2CAAAZnwBQdQbkHAqFYcGzyyQ0m8eQdsAB9bZL9MEDA6MhWI2KXixJMm7N5VzEYymUeirFOKkkGLRISyAxKYlxH2TgFROBtELE+ZjcZJNJm-LIYK4xQ9ppmJpl1Rx-V-gMIyB5bglhBB9WZVNLe0zygLUpMtVaCgnDsP5WOZ4irlWL1vptdOpy3JQBEMiWONyahhAOA6WAlhwaYhLtJDhorrWlBNSILyDqxEgydS6t14Nop8r9r635gjbUMFTsGu24iw2uvdRS4u0bVmxqxnoomoqybioppK+xXggA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared tests`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
