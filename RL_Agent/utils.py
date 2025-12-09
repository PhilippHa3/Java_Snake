from SnakeEnviroment import JavaSnakeEnv
import numpy as np


def drawGameState(obs):
    for i in range(len(obs)):
        for j in range(len(obs[0])):
            if obs[i][j] == 1:
                print('o', end='', flush=True)
            elif obs[i][j] == 2:
                print('O', end='', flush=True)
            elif obs[i][j] == 3:
                print('x', end='', flush=True)
            else:
                print('-', end='', flush=True)
        print()

def createGameBoard(obs):
    board_size = int(np.sqrt(len(obs)/2))
    board_state = np.zeros((board_size, board_size))
    obs = np.array(obs, dtype=np.int16)
    board_state[obs[0]][obs[1]] = 3
    board_state[obs[2]][obs[3]] = 2
    for i in range(0,board_size,2):
        if obs[4+i] != 0 and obs[4+i+1] != 0:
            board_state[obs[4+i]][obs[4+i+1]] = 1
    return board_state

def actionToNumber(act_int):
    if act_int == 0:
        return 'UP'
    elif act_int == 1:
        return 'RIGHT'
    elif act_int == 2:
        return "DOWN"
    elif act_int == 3:
        return "LEFT"

def visualizeAgentsPlay(model, env:JavaSnakeEnv):
    obs, _ = env.reset()
    obs_draw = createGameBoard(obs)
    print("Reset")
    drawGameState(obs_draw)
    print()
    done = False
    while not done:
        action, _ = model.predict(obs, deterministic=True)
        obs, reward, _, done, _ = env.step(action)
        obs_draw = createGameBoard(obs)
        print(f"Action: {actionToNumber(action)} | Reward: {reward}")
        drawGameState(obs_draw)
        print()

