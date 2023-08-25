import bagel.*;
import bagel.util.Point;

import java.util.Random;


public class Game extends AbstractGame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAX_NUM_PARTICLE = 50;
    private static final double X_PADDING = 25;
    private static final double Y_PADDING = 50;
    private Particle[] particles = new Particle[MAX_NUM_PARTICLE];
    private int numParticle = 0;
    
    
    private Particle p1;
    private Particle p2;

    private Particle randomParticle() {
        Random rand = new Random();
        boolean isIntersectingOtherParticle = true;
        Particle newParticle = null;

        // checks whether the particle intersects with other
        // particle that has been created
        do {
            // first create a new particle with a random coordinate,
            // and particleType
            double x = rand.nextDouble() * (WIDTH - (2  * X_PADDING)) + X_PADDING;
            double y = rand.nextDouble() * (HEIGHT - (2 * Y_PADDING)) + Y_PADDING;
            Point particlePoint = new Point(x, y);

            int particleType = Particle.generateRandomParticleType();

            newParticle = new Particle(particleType, particlePoint);

            // check whether it intersects with other particle
            isIntersectingOtherParticle = false;
            for (int j = 0; j < numParticle; j++) {
                // check if it intersects with other particle, if yes,
                // then continue creating a new particle, until we found one
                // that doesn't intersect with other particle
                if (particles[j].isIntersect(newParticle)) {
                    isIntersectingOtherParticle = true;
                    break;
                }
            }
        } while (isIntersectingOtherParticle);

        return newParticle;
    }

    public Game() {
        super(WIDTH, HEIGHT, "Game Title");
        p1 = new Particle(1, new Point(100, 200));
        p2 = new Particle(2, new Point(200, 200));


        for (int i = 0; i < MAX_NUM_PARTICLE; i++) {
            particles[numParticle] = randomParticle();
            numParticle++;
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

        Point curr = p1.getPoint();

        if (input.isDown(Keys.LEFT)) {
            p1.setPoint(new Point(curr.x - speed, curr.y));
        }
        if (input.isDown(Keys.RIGHT)) {
            p1.setPoint(new Point(curr.x + speed, curr.y));
        }
        if (input.isDown(Keys.UP)) {
            p1.setPoint(new Point(curr.x, curr.y - speed));
        }
        if (input.isDown(Keys.DOWN)) {
            p1.setPoint(new Point(curr.x, curr.y + speed));
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }

        for (Particle particle: particles) {
            particle.draw();
        }
    }
}
