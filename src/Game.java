import bagel.*;
import bagel.util.Point;

import java.util.Random;


public class Game extends AbstractGame {
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;
    private static final int MAX_NUM_PARTICLE = 50;
    private static final double X_PADDING = 25;
    private static final double Y_PADDING = 50;
    private Particle[] particles = new Particle[MAX_NUM_PARTICLE];
    private int numParticle = 0;
    
    
    private Particle p1;
    private Particle p2;


    /* CHANGED BY PETER */
    private Font textOutput;
    private Player player1;
    private Player player2;
    private int score = 0;
    private double size1;
    private double size2;

    // For scaling the player
    private DrawOptions drawOptions;

    // Default player movement speed
    private double STEP_SIZE = 3.5;

    // Default particle size
    private final int BASE = 20;

    // TEST ONLY
    private Image playerImg1;
    private Image playerImg2;
    private Image backgroundImage;
    private Image homeImage;
    private Font titleText;
    private Font subtitleText;
    private boolean hasStarted = false;
    /* --------------------------------------------------------------- */

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

        /* CHANGED BY PETER */
        playerImg1 = new Image("res/playerBlue.png");
        playerImg2 = new Image("res/playerRed.png");
        textOutput = new Font("res/conformable.otf", 120);
        backgroundImage = new Image("res/backgroundblack.jpeg");
        homeImage = new Image("res/homescreen.jpeg");
        titleText = new Font("res/arcadeclassic.ttf", 70); // Adjust the font path and size
        subtitleText = new Font("res/conformable.otf", 40);
        drawOptions = new DrawOptions();
        player1 = new Player(new Point(200, 350), new Image("res/player1.png"));
        player2 = new Player(new Point(1900, 50), new Image("res/player2.png"));
        size1 = 1;
        size2 = 1;
        /* ---------------------------- */

        for (int i = 0; i < MAX_NUM_PARTICLE; i++) {
            particles[numParticle] = randomParticle();
            numParticle++;
        }
    }

    /* CHANGED BY PETER */
    /**
     * Movement of the player with - out of bounds - checker
     */
    public void movement(Player player, Input input) {
        if (player.getPoint().x > 20 + size1 && input.isDown(Keys.LEFT)) {
            player.moveLeft(STEP_SIZE);
        }
        if (player.getPoint().x < 1900 + size1 && input.isDown(Keys.RIGHT)) {
            player.moveRight(STEP_SIZE);
        }
        if (player.getPoint().y > 20  + size1 && input.isDown(Keys.UP)) {
            player.moveUp(STEP_SIZE);
        }
        if (player.getPoint().y < 1060 + size1 && input.isDown(Keys.DOWN)) {
            player.moveDown(STEP_SIZE);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    public void movement2(Player player, Input input) {
        if (player.getPoint().x > 20 + size2 && input.isDown(Keys.A)) {
            player.moveLeft(STEP_SIZE);
        }
        if (player.getPoint().x < 1900 + size2 && input.isDown(Keys.D)) {
            player.moveRight(STEP_SIZE);
        }
        if (player.getPoint().y > 20  + size2 && input.isDown(Keys.W)) {
            player.moveUp(STEP_SIZE);
        }
        if (player.getPoint().y < 1060 + size2 && input.isDown(Keys.S)) {
            player.moveDown(STEP_SIZE);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    /**
     * Calculate the Euclidean distance between 2 points
     */
    public int distance(Point point1, Point point2) {
        return (int) Math.abs(
                Math.sqrt(Math.pow((point1.x - point2.x), 2)
                        + Math.pow((point1.y - point2.y), 2)));
    }

    /**
     * Check for objects intersection
     */
    public void intersection(Player player, Particle particle) {
        if (distance(player.getPoint(), particle.getPoint()) <= BASE * size1) {
            // add score, since intersects
            // ... remove particle ..
            particle.toggleHidden();
            size1 += 0.5;
            score++;
            // check every 5 score needs to be slower for player's speed
            if ((score + 1) % 5 == 0) {
                STEP_SIZE -= 0.25;
            }
        }
    }

    public void intersection2(Player player, Particle particle) {
        if (distance(player.getPoint(), particle.getPoint()) <= BASE * size2) {
            // add score, since intersects
            // ... remove particle ..
            particle.toggleHidden();
            size2 += 0.5;
            score++;
            // check every 5 score needs to be slower for player's speed
            if ((score + 1) % 5 == 0) {
                STEP_SIZE -= 0.25;
            }
        }
    }

    /* --------------------------------------------------------- */

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
        if (hasStarted) {
            backgroundImage.draw(0,0, drawOptions.setScale(2,2));
            /* CHANGED BY PETER */
            // Movement of player
            movement(player1, input);
            movement2(player2, input);
            /* ---------------- */

            for (Particle particle : particles) {
                /* CHANGED BY PETER */
                if (particle.getHidden()) continue;

                intersection(player1, particle);
                intersection2(player2, particle);
                /* ------------------------------------- */

                particle.draw();
            }

            /* CHANGED BY PETER */
            // manual override of player image
            playerImg1.draw(player1.getPoint().x, player1.getPoint().y, drawOptions.setScale(size1, size1));
            playerImg2.draw(player2.getPoint().x, player2.getPoint().y, drawOptions.setScale(size2, size2));
            textOutput.drawString("Score : " + score, 45, 100);
            /* --------------------------------------------------------------- */
        } else {
            double titleX = Window.getWidth() / 2.0 - titleText.getWidth("GAME TITLE") / 2;
            double titleY = Window.getHeight() / 2.0 + 70.0 / 2.0;

            double subtitleX = Window.getWidth() / 2.0 - subtitleText.getWidth("PRESS ENTER TO PLAY") / 2;
            double subtitleY = Window.getHeight() / 2.0 + 40.0 / 2.0 + 55;

            homeImage.draw(Window.getWidth() / 2.0,Window.getHeight() / 2.0, drawOptions.setScale(1.5, 1.5));
            titleText.drawString("GAME TITLE", titleX, titleY);
            subtitleText.drawString("PRESS ENTER TO PLAY", subtitleX, subtitleY);
            if (input.wasPressed(Keys.ENTER)) {
                hasStarted = true;
            }
        }
    }
}
