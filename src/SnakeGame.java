import java.util.*;

public class SnakeGame {
    private Random random;
    private int[][] collision_array;
    private int field_width;
    private int field_height;
    public Deque<Position> snake_order = new ArrayDeque<>();
    private Position food;
    private Position head;
    private Direction direction = Direction.DOWN;
    // private boolean gameOver = false;
    // private int score = 0;
    private boolean ateFoodLastRound = false;
    private volatile Direction nextDirection = Direction.DOWN;

    private float reward = 0;

    public SnakeGame(int field_width, int field_height) {
        this.random = new Random();
        this.field_width = field_width;
        this.field_height = field_height;
        resetGame(field_width, field_height);
    }
    
    public void setNextDirection(Direction newDir) {
        this.nextDirection = newDir;
    }

    private void resetGame(int field_width, int field_height) {
        this.collision_array = new int[field_width][field_height];
        this.snake_order = new ArrayDeque<>();

        int startX = field_width/2;
        int startY = field_height/2;
        Position start_position = new Position(startX, startY);
        this.head = start_position;
        this.snake_order.add(start_position);
        this.collision_array[startX][startY] = 1;

        this.getNewFood();
    }

    public boolean update() {
        // returns if the game is still going
        if (this.nextDirection != null && !this.direction.isOpposite(this.nextDirection)) {
            this.direction = this.nextDirection;
        }
        this.reward = 0;
        Position oldHeadPosition = this.snake_order.getLast();
        int newX = -1;
        int newY = -1;
        // get the new Head position of the snake after the update
        switch (this.direction) {
            case RIGHT:
                newX = oldHeadPosition.x + 1;
                newY = oldHeadPosition.y;
                break;
            case DOWN:
                newX = oldHeadPosition.x;
                newY = oldHeadPosition.y + 1;
                break;
            case LEFT:
                newX = oldHeadPosition.x - 1;
                newY = oldHeadPosition.y;
                break;
            case UP:
                newX = oldHeadPosition.x;
                newY = oldHeadPosition.y - 1;
                break;
            default:
                break;
        }
        if (isCollision(newX, newY)) {
            // this.gameOver = true;
            this.reward = -1;
            return false;
        }
        Position pos = new Position(newX, newY);
        this.head = pos;
        this.collision_array[pos.y][pos.x] = 1;       
        this.snake_order.add(pos);
        // snake ate the food
        if (this.ateFoodLastRound) {
            this.ateFoodLastRound = false;
        } else {
            Position snakeTail = this.snake_order.removeFirst();
            this.collision_array[snakeTail.y][snakeTail.x] = 0;
        }

        if (this.food.compare(pos)) {
            this.ateFoodLastRound = true;
            if (this.gameFieldFull()) { 
                // this.gameOver = true;
                return false; 
            }
            this.getNewFood();
            // this.score += 1;
            this.reward = 1;
        }
        
        return true;
    }

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

    public String reset() {
        resetGame(field_width, field_height);
        return collisionArrayToString();
    }

    private boolean isCollision(int x, int y) {
        return x < 0 || x >= this.field_width ||
            y < 0 || y >= this.field_height ||
            this.collision_array[x][y] > 0;
    }

    private void getNewFood(){
        do {
            int x = random.nextInt(this.field_width);
            int y = random.nextInt(this.field_height);
            this.food = new Position(x, y);
        } while (this.collision_array[this.food.x][this.food.y] != 0);
    }

    private boolean gameFieldFull(){
        int snakeLength = this.snake_order.size();
        return (snakeLength == this.field_width * this.field_height);
    }

    public void drawGameState() {
        int[][] collision_array = this.updateCollisionArray();
        int height = collision_array.length;
        int width = collision_array[0].length;

        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (collision_array[j][i] == 3) {
                    System.out.print("x");
                }
                else if (collision_array[j][i] == 2) {
                    System.out.print("O");
                }
                else if (collision_array[j][i] == 1) {
                    System.out.print("o");
                }
                else {
                    System.out.print("-");
                }
            }
            System.out.println();
        }
    }

    private int[][] updateCollisionArray() {
        int[][] collision_array_coppy = new int[this.collision_array.length][];
        for (int i = 0; i < this.collision_array.length; i++) {
            collision_array_coppy[i] = Arrays.copyOf(this.collision_array[i], this.collision_array[i].length);
        }
        collision_array_coppy[this.head.y][this.head.x] = 2;
        collision_array_coppy[this.food.y][this.food.x] = 3;
        
        return collision_array_coppy;
    }

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

    public enum Direction {
        UP(0, 1),
        RIGHT(1, 0),
        DOWN(0, -1),
        LEFT(-1, 0);

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
