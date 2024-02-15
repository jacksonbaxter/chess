# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

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


[Phase 2: Chess Server Design](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDAEooDmSAzmFMARDQVqhFHXyFiwUgBF+wAIIgQKLl0wATeQCNgXFDA3bMmdlAgBXbDADEaYFQCerDt178kg2wHcAFkjAxRFRSAFoAPnJKGigALhgAbQAFAHkyABUAXRgAegt9KAAdNABvfMp7AFsUABoYXDVvaA06lErgJAQAX0xhGJgIl04ePgEhaNF4qFceSgAKcqgq2vq9LiaoFpg2joQASkw2YfcxvtEByLkwRWVVLnj2FDAAVQKFguWDq5uVNQvDbTxMgAUQAMsC4OkYItljAAGbmSrQgqYb5KX5cAaDI5uUaecYiFTxNAWBAIQ4zE74s4qf5o25qeIgab8FCveYw4DVOoNdbNL7ydF3f5GeIASQAciCWFDOdzVo1mq12p0YJL0ilkbQcSMPIIaQZBvSMUyWYEFBYwL53hUuSgBdchX9BqK1VLgTKtUs7XVgJbfOkIABrdBujUwP1W1GChmY0LYyl4-UTIkR-2BkNoCnHJMEqjneORPqUeKRgPB9C9aKULGRYLoMDxABMAAYW8USmWM+geugNCZQJBYPWMOIGFJSMWoGPJEwYKCIJw86IZ4xpMxjXdNDo9AYjCYzJZrDZoNJHvPF0g0D5-IFMCPmIMp-Fkmksrl9Gp8R2AERylA-rEP4-nUP68hsGiAcBPRTv8C5Lgapb+g2hCsja3ryuB-KYPBV4GnSMYmjAjwvG8-48msEEOj8wouoC5BghCsofD6Cp8psMAAGIsCkACyXqYMACDMFwFixpgMCSTAm7OoMuHLqmiyGE8uzqFJF4ISmhqXIRdwPOglCshaVrdmg6GfBJUkyXGkSus8SQyAo6TAl6DFQl2FZoAAvBKwIAOoAPoagA0sCEowH5AASHouf+Xn-pZknWbWGl4VpSEmZ5iWpQphpPtWcQwK27alH+LHVFBIEwD+HmZpVPTqbBhbwMgDbNm2v7-pVoG1eg9WYCgCD6NC9j+tASAAF4oP26nJc18mITAJJktlC1aSlz4wAALC2ACMv7VGowCPIB1XAlA5iFRYo1WuNU2QQ1UlNYMD7xDt+2lYdXDHQB8Q-udl3xNdZZ3dNP69kNBiUJdq2XrlG0FfEACsHWfXcP2nf9F3QPEGiqMyxBjOD2XPXWrUYMjqMlD+X0Y39AM48pXAE9gRO9mg-Z9gO4DQC1IRYLgEhrpOBWrhOzDGb4Bpi3O1nbtcujDfupjmFYthmCgobwZYzA2H4ARBOTj5FojiQyIxznZDkn5cN+N2+HdepoPEAA8vVoOEVbUDWgyS4tV5UEJSBaIEplzO7Bx++tRq6YyxFPOyUAAEKOGHEfRo6sYivRILgpCXqwtxfGuVFMVpplmZee7gnCTAonibNsdxr7-qLQl6lR4S2nSU3UxtBANBp+mnkHI3mcYtn8T2Y5zmuSC7nD5XErPKCoIRdFLCxeVKDxdv2VzS3VqLfXGLZZ3+a0s1m3FSTBUpa9RVtgNkMjSDUCTdN+9Nyl5+TEtpLkg7q3aO+VvaFXegddGJ16bYyuvbUGD1b5gPvkbN6e1IFHWgWdWBQN4Hv3usTQaw1obQDPsAruCMwGUxKtTWmWCsaAyZizNmSD+jNQfijGhNMoG-WwYwvGzN36s3xIQjmA0xHcyHHzBsMt1xRDAbI0gABxO00tBbjllk3eWwBFZ7mMCrI8thpgaHnG4GAKjDo3gNveI2-xNoJCUcCd8OR2B2mKG-SaTtXbu09k1SIFiUCLWQDwAJXBw6L3QAcAJ+EY7jz0jAQOyAQ4oElkPCukSM40WdLZHOjF87-i4jxfiSlS6b3LuWSu1chIiTEhPUI5TTIJKDjNKy39mrRPSgXO02UOkUNiVk+48cwChNBG4OYo9WlxOyQCIEeSoSuOqGKGQdR9aBETssOo2gECgCDOs1iCyUASjtIU4uByv5TObv41RnSEjUwOUs06u0mwAGYtqgVWWybe3VqpbJ2XsiqQEqo-gOUcgF0FMg9OuX00BMR2pcIOVwU6tzgV2gefEJ5rz3m3k+baMFQLfkgF2V8wFoEQV2nqpkR6klSbSIpo-eFdpEUvjuaimQjyXlvOqh8-5v1gKgQJUS3FvKgVkrxV0Slz9hrAzGvg6aAwGmeRgAHZp5yBkpV6RfFAxIAGQuqDEmFJZtroLRpg3hDDGbSturKxBjU77sNQUaj6tCeGYwZnAjxBCIbENgbqwJICTZUJgJwjB316FutxvjIRLDbXIPtfzahIa6Z8MZgI5hIj2acwkYJHmw4jaKOYFOfN5ioWaqLXLEOOjdwAgPKrY82ALBQGwAgAwcAzQGACVYu8D47Gm1fBkK2Bz3Eys8WMbxESPa-lFcKmCdqrl6s6cyFArIAnhPSWgOoU6okloLDpC58REnByMuO1dFSMlzRybMvOzEhUnOKQUdeZd3ZV3HTXZg7smlJNVU6S5xb51dyBnvdSGqd09z3UqtABRhl2jmFOuo-4JlJTaRet00ooTqk1AcmAMG7SgpWIu-g0AeV7BgAANQUKCZ4wIyBYdg106oCHQNqvadu1M9yZC+v1QG2F9LJ2svZa8ql8i2EvQdcVXjiy2Xoo5V6gwugTHTAAI4WFUFgIDLG8pccNTtLhdCzXhpgHJmAinlM8GJjG4TZN41Gp0y6mBjDDPGZU4Ql+lqHbWvle+5Vn6x5McGMB1My1AFSX8xpoTWnjXOtNa6nBr9h2etYT7ETVmIEmtDXpmLrmEHOe9TDNTf7NWUO48G1LSbzWFVTVG9NCXYBxrakGqm3Cot2ZTZGwmVWuZc0kbzB8RbC3qNnHIkLZatEVt0dWgxasbCOEGggCA3gYAACkIBXl-QYGwAqbH8x7YG5IzxnGDrwSO-EY613hF-E24A02oBwAgLNqAUEopimcjkJOoIFBwGCiKvjUmBNezYXOv1-6YAACtltoBXe7DdrKeTbKuzdu7W78sgesvu5pR610ntMtRb9k8GJXrowYIud6aylJck+qptd30HpaYhi56r1MAaFRx6Ou6BlMl8CgQlATgQAA83DoBUNhiTDHz0zLx0xYirLb2S+qA+spbGvJseytUmXUM+c8DUupSZvmAeLSV1r4L6mCJgbWEgdgaAkiw8oIGFd5FVdLJh5dyg8PoAMfUiri7cPbu82pxGDmddZtvsDp0HRzbsoG9p0x5D08nIuUw-PGA3Lt45AM9swlPLd43tJ-bmQivWXh4jwfXXnST5bgj1rkLhXDU33L+ZxLlm6s14j0QgwIBvewCEiyDQzhriZhgLzLzwcC8G6L6txaJCasRgQF3nvwA++8wcygJTKnh8V6N1fU2O1nmJrDTFzvS7u8wF731LouRrM7-S-Z4A8ml8mbAGZ2v1K7VJbq1vi-0XGH7+v7Puqp+U-aff2a0KkX2X1M0E3Ui5ggJfkww0AgFUCWggGYBQHV1U0LzaT8wZ3-hWnL0rw30DQAJK13yvxv1APv3AKemfwbzpQIMizSw-0ZhALvwf0kg6xc0O3ug83HXA2py-SzmYyRwCx1Ty0BwKzwO4xS1oNK300y2tWYLCxqxf2oIi0azoKANwQ9TBhk22B9WEM43kITUIMvxa0ETa0EDkJpQ4Qa103oPK1a2ETMIzXEX7C61zX5l61Fn62FgLUcFmEqDUXoAG1kBGx3CVn0UPEmzMEuxaiXQ72wCbVQjGE7UNi2wNUKgSHNnBEtlyH3D8XIB8MCD8IXWbTwG+F0XGV6HyLaBiVZ2-T7kqAHhSTJETjCWxz4OQwyKcRciJwElHzIEqMKKB0WExBAGKOMQqN8OqMY1qKM37hoAUDJFCXKNF1dA6Nnm6LOV6P6L10ZRgBGJiM-j6ImJZymNjDqIaPmIQFSU8haMyRxzoniFWK6KKXKVuL4KfC2M6XdmGNGIOI+OhU00KhvgsNEyfi5iAA)

[![Sequence Diagram](Sequence%20Diagram.png)]