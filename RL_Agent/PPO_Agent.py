from stable_baselines3 import PPO
# from stable_baselines3.common.env_util import make_vec_env
from utils import visualizeAgentsPlay
from SnakeEnviroment import JavaSnakeEnv
import os, datetime

if __name__ == '__main__':

    env = JavaSnakeEnv(board_size=5)
    save_path = f'{os.getcwd()}/RL_Agent/logs/snake_ppo/'
    timestamp = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    
    # if i == 0:
    #     policy = "MlpPolicy"
    # else:
    #     policy="CnnPolicy"

    model = PPO(
        policy="MlpPolicy",
        env=env,
        learning_rate=3e-4,
        n_steps=2048,
        batch_size=64,
        n_epochs=10,
        gamma=0.99,
        verbose=0,
        seed=0,
        tensorboard_log=save_path
    )

    # model.load('C:/root/code_repositories/private_projects/Java_Snake/RL_Agent/logs/snake_ppo/models/ppo_snake_agent_251205_103801.zip')
    # for i in range(10):
    #     visualizeAgentsPlay(model, env)
    #     print('=' * 10)
    # env.close()

    print('Training Started!')
    model.learn(total_timesteps=100_000_000)
    model.save(f'{save_path}/models/ppo_snake_agent_{timestamp}')
    print('Traingin complete')