import bagel.*;
import bagel.util.Point;

import java.util.Random;


public class Game extends AbstractGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int NUM_PARTICLE = 50;
    private Particle[] particles = new Particle[NUM_PARTICLE];
    private Player player;

    public Game() {
        super(WIDTH, HEIGHT, "Game Title");
        Random rand = new Random();

        for (int i = 0; i < NUM_PARTICLE; i++) {
            double x = rand.nextDouble() * WIDTH;
            double y = rand.nextDouble() * HEIGHT;

            int imageNumber = rand.nextInt(Particle.NUM_IMAGES);

            Point particlePoint = new Point(x, y);

            this.particles[i] = new Particle(imageNumber, particlePoint);
        }
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

//        if (input.isDown(Keys.LEFT)) {
//            x -= speed;
//        }
//        if (input.isDown(Keys.RIGHT)) {
//            x += speed;
//        }
//        if (input.isDown(Keys.UP)) {
//            y -= speed;
//        }
//        if (input.isDown(Keys.DOWN)) {
//            y += speed;
//        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        for (Particle particle: particles) {
            particle.draw();
        }
    }
}
