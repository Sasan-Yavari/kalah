# Kalah

Each of the two players has ​ **​ six pits​ ** ​ in front of him/her. To the right of the six pits, each player has a larger pit, his Kalah or house.

At the start of the game, six stones are put in each pit.

The player who begins picks up all the stones in any of their own pits, and sows the stones on to the right, one in each of the following pits, including his own Kalah. No stones are put in the opponent's' Kalah. If the players last stone lands in his own Kalah, he gets another turn. This can be repeated any number of times before it's the other player's turn.

When the last stone lands in an own empty pit, the player captures this stone and all stones in the opposite pit (the other players' pit) and puts them in his own Kalah.

The game is over as soon as one of the sides run out of stones. The player who still has stones in his/her pits keeps them and puts them in his/hers Kalah. The winner of the game is the player who has the most stones in his Kalah.

This program created based on [Backbase](https://backbase.com/) home assignment task for hiring process.

## Design

All the classes inside this program are created to be testable, readable and maintainable and the program itself is easy to deploy.
I am tried to follow `KISS` and `Clean Architecture` principles.

Followings are the packages and layers of the program:

- `game` package is the main package for game domain. We can access to a game using an implementation of `DataAccess` class.
This package contains `BoardEntity` class as the main entity of the game and the `Controller` class as the controller layer of the game.
Also the `DataAccess` interface is the database layer entry point. Any type of `DataAccess` can be implemented to handle the data storage process.
Users of `DataAccess` does not know anything about the implementation according to Clean Architecture principles.

- `ui` layer is the highest layer that depends on almost everything and we can change it easily because no layer is depended on it.
RESTFull api `RestUserInterface` implemented as the main interaction point with the game but we can implement other types of
user interfaces like graphical ones.  

### Design Patterns

- `InMemoryDataAccess` is implemented according to `Singleton` pattern.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need followings to build the program:
- Latest version of gradle
- JDK 1.8+

You need followings to run the program:
- JVM 1.8+ or JDK 1.8+

### Installing

First of all run following commands to build the project:
```
git clone https://github.com/Sasan-Yavari/kalah.git
cd kalah
gradle installDist
```

Now you have the final build inside the following directory:
```
kalah/build/install/kalah
```

Now you can run the project using following command:
```
cd build/install/kalah/bin
./run.sh
```

or simply
```
cd build/install/kalah/bin
java -jar kalah.jar
```

You can pass the configuration file path to the jar file like this:
```
java -jar kalah.jar PATH_TO_CONFIG_FILE
```

otherwise, the application will use the `Config.properties` file that exists inside the `conf` directory.

## Running the tests

In order to run tests, inside the root of the project, run the following command:
```
gradle test
```

## Deployment

Copy the `kalah` directory from `build/install` to where ever you want to deploy the build. This directory is the final runnable version of project.

## Built With

* [Gradle](https://gradle.org/) - Dependency Management

## Authors

* **Sasan Yavari** - *Initial work* - [Sasan-Yavari](https://github.com/Sasan-Yavari)