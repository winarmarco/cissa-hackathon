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
    private Font textOutput; // Outputting texts
    private Player player1;
    private Player player2;
    private DrawOptions drawOptions; // Scale the other visuals than players
    private final int BASE = 20; // Default particle's size in pixel
    private Image backgroundImage;
    private Image homeImage;
    private Font titleText;
    private Font subtitleText;
    private boolean hasStarted = false;


    /**
     * Randomizing particles' placement
     */
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

    /**
     * Constructors for the game
     */
    public Game() {
        super(WIDTH, HEIGHT, "Game Title");
        p1 = new Particle(1, new Point(100, 200));
        p2 = new Particle(2, new Point(200, 200));

        /* CHANGED BY PETER */
        textOutput = new Font("res/conformable.otf", 120);
        backgroundImage = new Image("res/backgroundblack.jpeg");
        homeImage = new Image("res/homescreen.jpeg");
        titleText = new Font("res/arcadeclassic.ttf", 70); // Adjust the font path and size
        subtitleText = new Font("res/conformable.otf", 40);
        drawOptions = new DrawOptions();
        player1 = new Player(new Point(200, 350), new Image("res/playerBlue.png"));
        player2 = new Player(new Point(1900, 50), new Image("res/playerRed.png"));
        /* ---------------------------- */

        for (int i = 0; i < MAX_NUM_PARTICLE; i++) {
            particles[numParticle] = randomParticle();
            numParticle++;
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
        if (distance(player.getPoint(), particle.getPoint()) <= BASE * player.size) {
            // remove particle
            particle.toggleHidden();
            player.size += 0.5;
            player.score++;
            // check every 5 score needs to be slower for player's speed
            if ((player.score + 1) % 5 == 0) {
                player.STEP_SIZE -= 0.25;
            }
        }
    }

    /**
     * Check for players' intersection between one and another
     */
    public int isPlayerIntersect(Player player1, Player player2) {
        if ((distance(player1.getPoint(), player2.getPoint()) <= BASE * player1.size) && (player1.size > player2.size)) {
            // remove player 2
            player2.toggleHidden();
            return 2;
        }
        else if ((distance(player2.getPoint(), player1.getPoint()) <= BASE * player2.size) && (player2.size > player1.size)) {
            // remove player 1
            player1.toggleHidden();
            return 1;
        }
        return 0;
    }

    /**
     * Implementation for players' intersection
     */
    public void playersIntersection (Player player1, Player player2) {
        // If both do not intersect then draw all players
        if (isPlayerIntersect(player1, player2) == 0) {
            player1.draw(player1.size, player1.size);
            player2.draw(player2.size, player2.size);
        }
        // If player 1 get eaten
        else if (isPlayerIntersect(player1, player2) == 1) {
            player2.draw(player2.size, player2.size);
        }
        // If player 2 get eaten
        else if (isPlayerIntersect(player1, player2) == 2) {
            player1.draw(player1.size, player1.size);
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
        if (hasStarted) {
            // Placement of BG image
            backgroundImage.draw(0,0, drawOptions.setScale(2,2));

            // Players' movement
            player1.movement1(player1, input);
            player2.movement2(player2, input);

            // Checking, hiding and drawing particles
            for (Particle particle : particles) {
                if (particle.getHidden()) continue;
                intersection(player1, particle);
                intersection(player2, particle);
                particle.draw();
            }

            // Checking for player intersections
            playersIntersection(player1, player2);

            /* Output all the visuals */
            textOutput.drawString("Score1 : " + player1.score, 45, 100);
            textOutput.drawString("Score2 : " + player2.score, 45, 200);

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
