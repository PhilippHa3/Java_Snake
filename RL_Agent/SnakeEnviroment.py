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
            low=0, high=board_size, shape=(board_size * board_size * 2,), dtype=np.int32
        )

        self.board_size = board_size
        self.host = host
        self.port = port
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.connect((self.host, self.port))

    def _parseInput(self, input_str:str):
        obs_str, reward_str, done_str = input_str.strip().split('|')

        obs_list = [[int(x) for x in row.split(',')] for row in obs_str.split(';')]
        observation_board = np.array(obs_list, dtype=np.int32)
        food_position = np.argwhere(observation_board == 3).ravel()
        snake_head = np.argwhere(observation_board == 2).ravel()
        snake_body = np.argwhere(observation_board == 1).ravel()

        observation = np.zeros((self.board_size * self.board_size * 2))

        observation[0:len(food_position)] = food_position
        observation[2:2+len(snake_head)] = snake_head
        observation[4:4+len(snake_body)] = snake_body

        reward = float(reward_str)
        done = done_str.lower() == 'true'

        return observation, reward, done, {}

    def step(self, action):
        self.sock.sendall(f"{action}\n".encode('utf-8'))
        data = self.sock.recv(4096).decode('utf-8')
        obs, reward, done, info = self._parseInput(data)
        return obs, reward, False, done, info

    def reset(self, seed=None, options=None):
        self.sock.sendall("-1\n".encode('utf-8'))
        data = self.sock.recv(4096).decode('utf-8')

        obs, _, _, info = self._parseInput(data)
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