# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

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
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Server Design diagram

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco67F8H5LCBALnAWspqig5QIAePKwvuh6ouisTYgmhgumGbpkhSBq0uWo4mkS4YWhyMDcryBqCsKMCvmIrrSkml6weUtHaI6zpsU68owOgOaQjAYHvKyeCGCgaLACqWEarhLLlJ63pBgGQYhsx5qRpR0YwLGwZcXJrFnECJYoTy2a5pg-4gjBJRXAMxGjAuk59NOs7NuOrZ9N+V62YU2Q9jA-aDr0jkjs5XnVlOQYefOUVtsuq7eH4gReCg6B7gevjMMe6SZJggUXkU1DXtIACiu7lfU5XNC0D6qE+3TuY26Dtr+JmpuULVztZUEdnZ4IwAh9g5ch2W+mhGKYXK2EEgppIwOSYD6WpdatWgpFMm6FHlNRtrTnRYQ9W1mkRoN-GHYZs3yWReFGCg3CZKtJ2baGd0sYUloPU9hhXfI3GJsm-VpihOWWQgeYg52P5XL5pX+V2ORgH2A5DkunDJeugSQrau7QjAADio6snlp6FeezB2dehPVXV9ijs1sUbe1bI2d1zO9TZbJYfB0LE6MqjIfzJNTRhgNzTIC3uktFIvZz6BbWaEZfZR+0wP9wBCsdCtoO920sRdcEa+p12wbdBuLcgsQC2oRGUAAciR+vK7tVE8raDOC9rDEdE74pGcDpmgyLgsQ1Dwcw35VxTF7ajjJU-RxwAktICcAIy9gAzAALE8J6ZAaFYTF8OgIKADZF8BJdPHH-twX0PkwI08PHMZRUo8FaO9LHJMJxUSejqnGfZ3nUwF-qTn3I3TxlxXVfOTXvejPXJd7M3GMrp4KUbtgPhQNg3DwLqmRE6OKT5WeyM89HlS1A09OM8EutDnXo6twNf4gxz61zq-o710gpHI2SkT4oFtrCOAYDbZiyxBLC2yt8IrVUq9JW5FtJ7Q9ibOM2gfavRdugkB2CDIAyMvND6i1lKZAgTceusw36jDQTtDBx8vSn1tjAAAQiKP259zhLQAaOeBxl2asL1DAtQVlubGVhqWZeKBh7lEzrnGAH9EYd1RqFHo8jFEwGUTnVRZhMbb2xgESwj0ELJBgAAKQgDyM+oxAhzxAA2Cm19qZpmqJSO8LQ45M1-ugIch9gDmKgHACACEoD0KHtINRbNv7ELikE8uoTwmROiaMVOQDUw33YjAAAVnYtAEDbE8gkWiaawjyGWxlsteWAS3pnTdurTWeDdYEOYUQzWVSpYUPdMtCBKdpBMM+uyTBvI44+yGR0w2vEhpx3jDdIhAA1SgSAABmYRgmpIidAGAMQYCKmVD0s65Q-BaGoaOe2UA6EiRSZQNJ0AMkKOGTMrSqsznYAuYYSZyQMipBgCAGSBZ9AwEhBUpEtoQC7KgMIoOXUbFFIkTmSGfVgEeLhqzdulNNHoyMVvNcqUAheBCV2L0sBgDYEPoQeIiQL7kw7rk+ylQKpVRqnVYwaKckgJANwPA0hOGQL5VAAVsCZrmxwn0pSwrYQjPeWMsleA9LKl6VrTk3hhgyE4XCr+wdyiIHJVqmA6YUUR25Ri0scTsXI1xb0TemAgA


