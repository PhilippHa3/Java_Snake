# Java Snake Game with Python RL agent
This project implements the classic Snake Game in Java and uses a network connection to stream the game state to an external Python service for Deep Reinforcement Learning (DRL). The goal was to build a functional, multi-language application to practice the fundamentals of Java, socket programming, and multi-language communication. 
### Core Objectives:
- Mastering fundamental Java syntax and Object-Oriented Programming
- Implementing Socket communication between Java (Server) and Pyhon (Client)

## Features:
<!-- ????????????????? -->
The implementation offers two different use cases:  
- Playing the game: The game is visualized in the Command Line.
- Training RL agent: The game state is sent to the python application and then a RL agent is trained.

## Components:
The application is split into two main components that communicate over a network socket:  
1. **Game Engine (Java)**:
- handles core game logic: snake movement, collision detection, food spawning, etc.
- Serializes the current game state (game display) and sends it to the Python Client. For that it follows the Gym api implementing the functions `step(action)` and `reset()`.
2. **RL Agent (Python)**:
- Receives the Game State from the Java Application
- Creates a custom evnironment for the RL agent (using the `gym` library)
- Train a PPO agent on the given game using `Stable-Baseline3` for the agent implementation

<!-- ## Technology Stack: -->

## Demo:
The following shows a game of snake played by a human player:  
![A snake game played by a Human](./images/SnakeGame_HumanPlayer.gif)

<!-- This gif shows the game played by a Trained PPO agent:  
<!-- GIF -->

<!-- The training process of the PPO agent looks as follows:  
Picture of avg reward/episode length over training -->

## Key Takeaways:
The project serves as successful exercise in:
- Inter-Process Communication: implementing a reliable data transfer between two distinct programs written in different languages using TCP sockets.
- Java OOP Refresher: Strengthening the understanding of the java syntax, class design, and proper data serialization in Java. 