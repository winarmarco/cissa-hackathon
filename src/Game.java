import bagel.*;

public class Game extends AbstractGame {
    private Image smiley;
    private Image bagel;
    private double x = 100;
    private double y = 100;

    public Game() {
        super(800, 600, "Hello World");
        bagel = new Image("res/bagel.png");
        smiley = new Image("res/smiley.png");
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }

    /**
     * Performs a state update. This simple example shows an image that can be controlled with the arrow keys, and
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    public void update(Input input) {
        double speed = 0.5;

        if (input.isDown(Keys.LEFT)) {
            x -= speed;
        }
        if (input.isDown(Keys.RIGHT)) {
            x += speed;
        }
        if (input.isDown(Keys.UP)) {
            y -= speed;
        }
        if (input.isDown(Keys.DOWN)) {
            y += speed;
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }


        bagel.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0);
        smiley.draw(x, y);
    }
}
