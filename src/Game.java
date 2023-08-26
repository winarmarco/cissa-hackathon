import bagel.*;
import bagel.Font;
import bagel.Image;
import bagel.Window;
import bagel.util.Point;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Game extends AbstractGame {
    public static final int WIDTH = 1440;
    public static final int HEIGHT = 920;

    private static final int MAX_NUM_PARTICLE = 50;
    private static final double X_PADDING = 45;
    private static final double Y_PADDING = 100;
    private static final int GAME_LENGTH_SECONDS = 90;

    private Particle[] particles = new Particle[MAX_NUM_PARTICLE];
    private int numParticle = 0;
    private Particle p1;
    private Particle p2;
    private Font textOutput; // Outputting texts

    private Point player1SpawnPoint = new Point(X_PADDING, HEIGHT - Y_PADDING);
    private Point player2SpawnPoint = new Point(WIDTH - X_PADDING, Y_PADDING);
    private Player player1;
    private Player player2;
    private DrawOptions drawOptions; // Scale the other visuals than players
    private Image backgroundImage;
    private Image homeImage;
    private Font titleText;
    private Font timerText;
    private Font subtitleText;
    private Font subtitleText2;
    private boolean hasStarted = false;
    private boolean toPlay = false;
    private Timer countdownTimer;
    private int timer = 30;
    private boolean isGameOver = false;


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

    public String formatTime(int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;

        return String.format("%02d:%02d", min, sec);
    }

    public void reinitializeSettings() {
        numParticle = 0;
        for (int i = 0; i < MAX_NUM_PARTICLE; i++) {
            particles[numParticle] = randomParticle();
            numParticle++;
        }

        player1 = new Player(player1SpawnPoint, new Image("res/player1.png"));
        player2 = new Player(player2SpawnPoint, new Image("res/player2.png"));

        isGameOver = false;
        hasStarted = false;
        toPlay = false;

        timer = GAME_LENGTH_SECONDS;
    }

    /**
     * Constructors for the game
     */
    public Game() {
        super(WIDTH, HEIGHT, "BattleSphere");
        p1 = new Particle(1, new Point(100, 200));
        p2 = new Particle(2, new Point(200, 200));

        textOutput = new Font("res/chary.ttf", 65);
        timerText = new Font("res/chary.ttf", 64);
        backgroundImage = new Image("res/backgroundblack.jpeg");
        homeImage = new Image("res/homescreen.jpeg");
        titleText = new Font("res/arcadeclassic.ttf", 70); // Adjust the font path and size
        subtitleText = new Font("res/chary.ttf", 40);
        subtitleText2 = new Font("res/conformable.otf", 40);
        drawOptions = new DrawOptions();

        reinitializeSettings();

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

        // if already exceed time, then its gameOver
        if (!isGameOver && toPlay && timer <= 0) {
            isGameOver = true;
        }


        // render if not GameOver
        // - if hasStarted,
        //      - and it has already playing, render the playground
        //      - not played, draw instruction
        // - not startedYet
        //      - render homeScreen
        if (!isGameOver) {
            if (hasStarted) {
                if (toPlay) {
                    // Placement of BG image
                    backgroundImage.draw(0, 0, drawOptions.setScale(2, 2));

                    // Players' movement
                    player1.movement1(player1, input);
                    player2.movement2(player2, input);

                    // Checking, hiding and drawing particles
                    for (int i = 0;i < numParticle;i++) {
                        Particle particle = particles[i];
                        if (particle.getHidden()) continue;

                        if (player1.isIntersect(particle, 70)) {
                            player1.particleIntersectBehaviour(particle);
                            particles[i] = randomParticle();
                        } else if (player2.isIntersect(particle, 70)) {
                            player2.particleIntersectBehaviour(particle);
                            particles[i] = randomParticle();
                        }
                        particle.draw();
                    }

                    // Checking for player intersecting player
                    if (player1.isIntersect(player2, 60) && player1.getScaleSize() > player2.getScaleSize()) {
                        player1.playerIntersectBehaviour(player2);
                        player1.respawn();
                        player2.respawn();
                    } else if (player2.isIntersect(player1, 60) && player2.getScaleSize() > player1.getScaleSize()) {
                        player2.playerIntersectBehaviour(player1);
                        player1.respawn();
                        player2.respawn();
                    } else {

                        // if player1's score is higher, than it should be at top of player 2
                        if (player1.getScaleSize() > player2.getScaleSize()) {
                            player2.draw();
                            player1.draw();
                        } else {
                            player1.draw();
                            player2.draw();
                        }
                    }

                    /* Output all the visuals */
                    double timerTextWidth = timerText.getWidth(formatTime(timer));
                    timerText.drawString(formatTime(timer),WIDTH / 2 - (timerTextWidth / 2), 100);
                    textOutput.drawString("Score 1 : " + player1.score, X_PADDING, Y_PADDING);
                    textOutput.drawString("Score 2 : " + player2.score, X_PADDING, Y_PADDING * 2);
                } else {
                    homeImage.draw(Window.getWidth() / 2.0,Window.getHeight() / 2.0, drawOptions.setScale(1.5, 1.5));
                    titleText.drawString("How to play", 2 * X_PADDING, 2 * Y_PADDING);
                    subtitleText.drawString("PRESS SPACE TO CONTINUE", 2 * X_PADDING, 3 * Y_PADDING);
                    subtitleText.drawString("When you start, you'll control a small cell" +
                            "\n\n  Player 1 can move their cell using the key 'W', 'A', 'S', D' " +
                            "\n  and Player 2 with 'UP', 'LEFT', 'RIGHT', 'DOWN'" +
                            "\n\n  Both players can eliminate each other by consuming them" +
                            "\n  This gives them 3 additional points" +
                            "\n\n  Once the timer runs out, the player with the highest score" +
                            "\n  wins", 2 * X_PADDING, 4 * Y_PADDING);

                    if (input.wasPressed(Keys.SPACE)) {
                        toPlay = true;
                        countdownTimer = new Timer();
                        countdownTimer.scheduleAtFixedRate(new TimerTask() {
                            @Override
                            public void run() {
                                if (timer > 0) {
                                    timer--;
                                } else {
                                    // Timer reach zero
                                    countdownTimer.cancel();
                                }
                            }
                        }, 1000, 1000);
                    }
                }

            } else if (!hasStarted) {
                double titleX = Window.getWidth() / 2.0 - titleText.getWidth("BattleSphere") / 2;
                double titleY = Window.getHeight() / 2.0 + 70.0 / 2.0;

                double subtitleX = Window.getWidth() / 2.0 - subtitleText2.getWidth("PRESS ENTER TO PLAY") / 2;
                double subtitleY = Window.getHeight() / 2.0 + 40.0 / 2.0 + 55;

                homeImage.draw(Window.getWidth() / 2.0,Window.getHeight() / 2.0, drawOptions.setScale(1.5, 1.5));
                titleText.drawString("BattleSphere", titleX, titleY);
                subtitleText2.drawString("PRESS ENTER TO PLAY", subtitleX, subtitleY);
                if (input.wasPressed(Keys.ENTER)) {
                    hasStarted = true;
                }
            }
        }

        if (isGameOver) {
            double titleX = Window.getWidth() / 2.0 - titleText.getWidth("GAME OVER") / 2;
            double titleY = Window.getHeight() / 2.0 + 70.0 / 2.0;
            homeImage.draw(Window.getWidth() / 2.0, Window.getHeight() / 2.0, drawOptions.setScale(1.5, 1.5));
            titleText.drawString("GAME OVER", titleX, titleY);

            String playerWinString = "";
            // if player 1 wins
            if (player1.score > player2.score) {
                playerWinString += "Player 1 wins!";
            } else if (player2.score > player1.score) {
                playerWinString += "Player 2 wins!";
            } else {
                playerWinString += "Tie!";

            }
            double endgameX = Window.getWidth() / 2.0 - subtitleText.getWidth(playerWinString) / 2;
            double endgameY = Window.getHeight() / 2.0 + 70.0 / 2.0;
            subtitleText.drawString(playerWinString, endgameX, endgameY + 45.0);

            double instructionX = Window.getWidth() / 2.0 - subtitleText.getWidth("Press Enter to Restart\nPress ESC to exit") / 2;
            double instructionY = Window.getHeight() / 2.0 + 200;
            subtitleText.drawString("Press Enter to Restart\nPress ESC to exit", instructionX, instructionY);

            if (input.wasPressed(Keys.ENTER)) {
                reinitializeSettings();
            }
        }

        if (input.wasPressed(Keys.ESCAPE)) {
            if (countdownTimer != null) countdownTimer.cancel();
            Window.close();
        }
    }
}
