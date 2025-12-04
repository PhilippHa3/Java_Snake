from stable_baselines3 import PPO
from stable_baselines3.common.env_util import make_vec_env
from SnakeEnviroment import JavaSnakeEnv

if __name__ == '__main__':
    env = JavaSnakeEnv(board_size=9)

    model = PPO(
        policy="MlpPolicy",
        env=env,
        learning_rate=0.0003,
        n_steps=2048,
        batch_size=64,
        n_epochs=10,
        gamma=0.99,
        verbose=1,
        seed=42,
        tensorboard_log='C:/root/code_repositories/private_projects/Java_Snake/RL_Agent/logs/snake_ppo/'
    )

    print('Training Started!')
    model.learn(100_000)
    model.save('ppo_snake_agent')
    print('Traingin complete')