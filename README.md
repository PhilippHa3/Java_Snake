# Java Snake Game with Python RL agent
This project implements the classic Snake Game in Java and uses a network connection to stream the game state to an external Python service for Deep Reinforcement Learning (DRL). The goal was to build a functional, multi-language application to practice the fundamentals of Java, socket programming, and multi-language communication.  

### Core Objectives:
- Mastering fundamental Java syntax and Object-Oriented Programming
- Implementing Socket communication between Java (Server) and Pyhon (Client)

## Features:
The implementation offers two different use cases:  
- **Playing the game**: The game is visualized in the Command Line.
- **Training RL agent**: The game state is sent to the python application and then a RL agent is trained.

## Components:
The application is split into two main components that communicate over a network socket:  
1. **Game Engine (Java)**:
- Core game logic: manages fundamental game mechanics, including snake movement, collision detection, food spawning, etc.
- Multithreaded Control: utilizes multiple Threads, one for the main game loop and another for the input handler. These threads are synchonised using a *CountDownLatch* to ensure the game begins only after the first player action has been received.
- Python Client Communiation: serializes the current game state (game display) and sends it to the Python Client. For that it follows the Gym api by implementing the core functions `step(action)` and `reset()`.
2. **RL Agent (Python)**:
- Game State Integration: receives the serialized Game State from the Java Application, which serves as the observation for the RL agent
- Custom Environment: defines a custom environment for the RL agent (using the `gym` library). The custom environment interfaces with the Java Engine via the network to handle actions and receive new states, rewards, and termination signals.
- Agent Implementation and Training: implements and trains the RL agent on the game using `Stable-Baseline3` for the agent implementation and training

<!-- ## Technology Stack: -->

## Demo:
The following shows a game of snake played by a human player:  
<img src="./images/SnakeGame_HumanPlayer.gif" alt="A snake game played by a Human" width="600"/>

<!-- This gif shows the game played by a Trained PPO agent:  
<!-- GIF -->

<!-- The training process of the PPO agent looks as follows:  
Picture of avg reward/episode length over training -->

<!-- ## Key Takeaways:
The project serves as successful exercise in:
- Inter-Process Communication: implementing a reliable data transfer between two distinct programs written in different languages using TCP sockets.
- Java OOP Refresher: Strengthening the understanding of the java syntax, class design, and proper data serialization in Java.  -->