import java.util.*;

public class SnakeGame {
    private Random random;
    private int[][] collision_array;
    private int field_width;
    private int field_height;
    private Deque<Position> snake_order = new ArrayDeque<>();
    private Position food;
    private Direction direction = Direction.DOWN;
    private boolean gameOver = false;
    private int score = 0;
    private boolean ateFoodLastRound = false;

    public SnakeGame(int field_height, int field_width) {
        this.random = new Random();
        this.field_width = field_width;
        this.field_height = field_height;
        this.collision_array = new int[field_width][field_height];

        int startX = field_width/2;
        int startY = field_height/2;
        Position start_position = new Position(startX, startY);
        this.snake_order.add(start_position);
        this.collision_array[startX][startY] = 1;

        this.getNewFood();
    }
    
    // returns if the game is still going
    public boolean update(Direction newDirection) {
        if (newDirection != null && !this.direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
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
            this.gameOver = true;
            return false;
        }
        Position pos = new Position(newX, newY);
        this.collision_array[pos.x][pos.y] = 1;       
        this.snake_order.add(pos);
        // snake ate the food
        if (this.ateFoodLastRound) {
            this.ateFoodLastRound = false;
        } else {
            Position snakeTail = this.snake_order.removeFirst();
            this.collision_array[snakeTail.x][snakeTail.y] = 0;
        }

        if (this.food.compare(pos)) {
            System.out.println("SNAKE ATE THE FOOD!");
            this.ateFoodLastRound = true;
            if (this.gameFieldFull()) { 
                this.gameOver = true;
                return false; 
            }
            this.getNewFood();
            this.score += 1;
        }
    
        return true;
    }

    private boolean isCollision(int x, int y) {
        return x < 0 || x > this.field_width ||
            y < 0 || y > this.field_height ||
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

    public GameState getGameState() {
        List<Position> snake = new ArrayList<Position>(this.snake_order);
        Position food = new Position(this.food.x, this.food.y);

        return new GameState(
            snake, 
            this.collision_array,
            food, 
            this.score, 
            this.gameOver, 
            this.field_width, 
            this.field_height, 
            this.direction
        );
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

    public class GameState {
        private List<Position> snake;
        public int[][] field_grid;
        public Position food;
        private int score;
        private boolean gameOver;
        public int field_width;
        public int field_height;
        private List<Integer> direction;

        public GameState(List<Position> snake, int[][] field_grid, Position food, int score, boolean gameOver,
                int field_width, int field_height, Direction direction) {
                    this.snake = snake;
                    this.field_grid = field_grid;
                    this.food = food;
                    this.score = score;
                    this.gameOver = gameOver;
                    this.field_width = field_width;
                    this.field_height = field_height;
                    this.direction = Arrays.asList(direction.dx, direction.dy);
                }
    }
}
