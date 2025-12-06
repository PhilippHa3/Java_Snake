from SnakeEnviroment import JavaSnakeEnv


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
    print("Reset")
    drawGameState(obs)
    print()
    done = False
    while not done:
        action, _ = model.predict(obs, deterministic=True)
        obs, reward, _, done, _ = env.step(action)
        print(f"Action: {actionToNumber(action)} | Reward: {reward}")
        drawGameState(obs)
        print()

