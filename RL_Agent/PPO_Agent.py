from stable_baselines3 import PPO, A2C, DQN
# from stable_baselines3.common.env_util import make_vec_env
from utils import visualizeAgentsPlay
from SnakeEnviroment import JavaSnakeEnv
import os, datetime

if __name__ == '__main__':

    env = JavaSnakeEnv(board_size=3)
    save_path = f'{os.getcwd()}/RL_Agent/logs/testing/'
    # save_path = f'{os.getcwd()}/RL_Agent/logs/training_progress/'
    timestamp = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
    
    model = PPO(
        policy="MlpPolicy",
        env=env,
        # learning_rate=1e-4,
        # n_steps=2048,
        # batch_size=64,
        # n_epochs=5,
        # gamma=0.99,
        # gae_lambda=0.95,
        # ent_coef=0.01,
        # clip_range=0.15,
        # seed=0,
        policy_kwargs=dict(normalize_images=False),
        verbose=0,
        tensorboard_log=save_path
    )

    # print('Training Started! PPO')
    # model.learn(total_timesteps=1_000_000)
    # model.save(f'{save_path}/models/ppo_snake_agent_{timestamp}')
    # print('Traingin complete! PPO')

    # # model.load('C:/root/code_repositories/private_projects/Java_Snake/RL_Agent/logs/snake_ppo/models/ppo_snake_agent_251208_125102.zip')
    # # for i in range(3):
    # #     visualizeAgentsPlay(model, env)
    # #     print('=' * 10)
    # # env.close()

    # model = A2C(
    #     policy="MlpPolicy",
    #     env=env,
    #     # learning_rate=7e-4,
    #     # n_steps=5,
    #     # gamma=0.99,
    #     # ent_coef=0.0,
    #     # vf_coef=0.5,
    #     verbose=0,
    #     policy_kwargs=dict(normalize_images=False),
    #     tensorboard_log=save_path
    # )

    # print('Training Started! A2C')
    # model.learn(total_timesteps=1_000_000)
    # model.save(f'{save_path}/models/dqn_snake_agent_{timestamp}')
    # print('Traingin complete! A2C')

    # model = DQN(
    #     'MlpPolicy',
    #     env,
    #     verbose=0,
    #     # buffer_size=50_000,
    #     # batch_size=32,
    #     # gamma=0.99,
    #     # target_update_interval=1000,
    #     # exploration_fraction=0.1,
    #     # exploration_final_eps=0.05,
    #     policy_kwargs=dict(normalize_images=False),
    #     tensorboard_log=save_path
    # )

    # print('Training Started! DQN')
    # model.learn(total_timesteps=1_000_000)
    # model.save(f'{save_path}/models/dqn_snake_agent_{timestamp}')
    # print('Traingin complete! DQN')

    # for _ in range(3): print()

    model.load('C:/root/code_repositories/private_projects/Java_Snake/RL_Agent/logs/testing/models/ppo_snake_agent_251211_163046.zip')
    for i in range(10):
        visualizeAgentsPlay(model, env)
        print('=' * 10)
    env.close()
