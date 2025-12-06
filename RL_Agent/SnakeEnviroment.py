import gymnasium as gym
from gymnasium import spaces
import socket, time
import numpy as np
import sys


class JavaSnakeEnv(gym.Env):
    
    def __init__(self, host='localhost', port=9000, board_size=12):
        super(JavaSnakeEnv, self).__init__()

        self.action_space = spaces.Discrete(4)

        self.observation_space = spaces.Box(
            low=0, high=3, shape=(board_size, board_size), dtype=np.int32
        )

        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.connect((self.host, self.port))

    def _parseInput(self, input_str:str):
        obs_str, reward_str, done_str = input_str.strip().split('|')

        obs_list = [[int(x) for x in row.split(',')] for row in obs_str.split(';')]
        observation = np.array(obs_list, dtype=np.int32)

        reward = float(reward_str)
        done = done_str.lower() == 'true'

        return observation, reward, done, {}

    def step(self, action):
        self.sock.sendall(f"{action}\n".encode('utf-8'))
        data = self.sock.recv(4096).decode('utf-8')
        obs, reward, done, info = self._parseInput(data)
        # print(self.actionToNumber(action), reward)
        # self.drawImage(obs)
        # time.sleep(2)
        # print()
        return obs, reward, False, done, info

    def reset(self, seed=None, options=None):
        self.sock.sendall("-1\n".encode('utf-8'))
        data = self.sock.recv(4096).decode('utf-8')

        obs, _, _, info = self._parseInput(data)
        # print('RESET')
        # self.drawImage(obs)
        # print()
        return obs, info
    
    def close(self):
        self.sock.close()


if __name__ == '__main__':
    env = JavaSnakeEnv(board_size=7)
    running = True
    obs, _ = env.reset()
    i = 0
    while running:
        # print((i%5) - 1)
        obs, rew, _, done, _ = env.step((i%5)-1)
        print(obs, '\n', rew, '\n', done)
        time.sleep(1)
        i += 1