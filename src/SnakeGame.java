import java.util.*;

public class SnakeGame {
    private Random random;
    private int[][] collision_array;
    private int field_width;
    private int field_height;
    public Deque<Position> snake_order = new ArrayDeque<>();
    private Position food;
    private Position head;
    private Direction direction = Direction.START;
    private boolean ateFoodLastRound = false;
    private volatile Direction nextDirection = Direction.START;
    // for the RL agents training
    private double reward = 0.0;

    public SnakeGame(int field_width, int field_height) {
        this.random = new Random();
        this.field_width = field_width;
        this.field_height = field_height;
        resetGame(field_width, field_height);
    }

    private void resetGame(int field_width, int field_height) {
        this.collision_array = new int[field_height][field_width];
        this.snake_order = new ArrayDeque<>();

        int startX = field_width/2;
        int startY = field_height/2;
        Position start_position = new Position(startX, startY);
        this.head = start_position;
        this.snake_order.add(start_position);
        this.collision_array[startY][startX] = 1;
        this.ateFoodLastRound = false;

        this.direction = Direction.START;
        this.nextDirection = Direction.START;

        this.getNewFood();
    }

    public void setNextDirection(Direction newDir) {
        this.nextDirection = newDir;
    }

    public Direction getDirection(){
        return this.direction;
    }

    /*
        This functions updates the gamestate of the snake game for one step.
        @returns boolean: if the game after the step is still active or if it is finished.
    */
    public boolean update() {
        // returns if the game is still going
        if (this.nextDirection != null && !this.direction.isOpposite(this.nextDirection)) {
            this.direction = this.nextDirection;
        }
        this.reward = -0.01;
        Position oldHeadPosition = this.snake_order.getLast();
        // get the new Head position of the snake after the update
        int newX = oldHeadPosition.x + this.direction.dx;
        int newY = oldHeadPosition.y + this.direction.dy;
        if (isCollision(newX, newY)) {
            // this.gameOver = true;
            this.reward = -1.0;
            return false;
        }
        Position pos = new Position(newX, newY);
        // update the parameters of the game
        this.head = pos;
        this.collision_array[pos.y][pos.x] = 1;       
        this.snake_order.add(pos);
        // snake ate the food (last round)
        if (this.ateFoodLastRound) {
            this.ateFoodLastRound = false;
        } else {
            Position snakeTail = this.snake_order.removeFirst();
            this.collision_array[snakeTail.y][snakeTail.x] = 0;
        }

        if (this.food.compare(pos)) {
            this.ateFoodLastRound = true;
            if (this.gameFieldFull()) { 
                return false; 
            }
            this.getNewFood();
            this.reward = 10;
        }
        return true;
    }

    /*
        Function for the RL agent training, which takes a input direction as an int
        and updates the gamestate of the Snake Game (inspired by Gym RL enviroments)
        @param int: a int value that maps to a direction (up, right, down, left)
        @return StepResult: returns the observation, reward and if the game is still running
        for the training of the RL agent
    */
    public StepResult step(int newDirectionInt) {
        Direction newDirection = null;
        switch (newDirectionInt) {
            case 0:
                newDirection = Direction.UP;
                break;
            case 1:
                newDirection = Direction.RIGHT;
                break;
            case 2:
                newDirection = Direction.DOWN;
                break;
            case 3:
                newDirection = Direction.LEFT;
                break;
            default:
                break;
        }
        setNextDirection(newDirection);
        boolean running = update();
        StepResult result = new StepResult(
            collisionArrayToString(), 
            reward, 
            !running
        );
        return result;
    }

    /*
        Resets the game state to  new game.
        @return String: returns the game board as a string.
    */
    public String reset() {
        resetGame(field_width, field_height);
        return collisionArrayToString();
    }

    /* 
        funtion to test if the next position of the snake results in a collision
        @param ints: x/y position of the new position of the snake
        @return boolean: returns if it is a collition or not
    */
    private boolean isCollision(int x, int y) {
        return x < 0 || x >= this.field_width ||
            y < 0 || y >= this.field_height ||
            this.collision_array[y][x] > 0;
    }

    /*
        Spawns a new food item somewhere on the game field
    */
    private void getNewFood(){
        do {
            int x = random.nextInt(this.field_width);
            int y = random.nextInt(this.field_height);
            this.food = new Position(x, y);
        } while (this.collision_array[this.food.y][this.food.x] != 0);
    }

    private boolean gameFieldFull(){
        int snakeLength = this.snake_order.size();
        return (snakeLength == this.field_width * this.field_height);
    }

    /*
        visualization of the game state.
    */
    public void drawGameState() {
        int[][] collision_array = this.updateCollisionArray();
        int height = collision_array.length;
        int width = collision_array[0].length;

        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (collision_array[i][j] == 3) {
                    System.out.print("x");
                }
                else if (collision_array[i][j] == 2) {
                    System.out.print("O");
                }
                else if (collision_array[i][j] == 1) {
                    System.out.print("o");
                }
                else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }

    /*
        updates the collision array to include the position of the food item
        as well as the position of the head of the snake.
        @return int[][]: a 2d matrix with ints that give the food/head/body position of the snake
    */
    private int[][] updateCollisionArray() {
        int[][] collision_array_coppy = new int[this.collision_array.length][];
        for (int i = 0; i < this.collision_array.length; i++) {
            collision_array_coppy[i] = Arrays.copyOf(this.collision_array[i], this.collision_array[i].length);
        }
        collision_array_coppy[this.food.y][this.food.x] = 3;
        collision_array_coppy[this.head.y][this.head.x] = 2;
        
        return collision_array_coppy;
    }

    /*
        Function to transform the collision array to a string in a reversable way.
        Is used to send information through the socket to the python implementation.
        @return String: the collision array as a string, split by ',' and ';'
    */
    public String collisionArrayToString() {
        int[][] collision_array_coppy = updateCollisionArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < collision_array_coppy.length; i++) {
            for (int j = 0; j < collision_array_coppy[0].length; j++) {
                sb.append(collision_array_coppy[i][j]);
                if (j < collision_array_coppy[i].length -1) {
                    sb.append(',');
                }
            }
            if (i < collision_array_coppy.length -1) {
                sb.append(';');
            }
        }
        return sb.toString();
    }

    /*
        A basic class that saves the position of the different parts on the game field.
    */
    public class Position {
        public int x, y;
    
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        public Position getPosition() {
            return this;
        }

        public boolean compare(Position pos) {
            return pos.x == this.x && pos.y == this.y;
        }
    }

    /*
        Enum that defines the possible directions of the snake.
    */
    public enum Direction {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0),
        START(0,0);

        private final int dx;
        private final int dy;

        Direction(int dx, int dy) {
            this.dx = dx;
            this.dy = dy;
        }

        private boolean isOpposite(Direction dir) {
            switch (dir) {
                case UP:
                    return this == Direction.DOWN;
                case RIGHT:
                    return this == Direction.LEFT;
                case DOWN:
                    return this == Direction.UP;
                case LEFT:
                    return this == Direction.RIGHT;
                default:
                    return false;
                }
        }
    }

}
