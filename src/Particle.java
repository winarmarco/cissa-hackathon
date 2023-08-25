import bagel.Image;
import bagel.util.Point;

import java.util.Random;

public class Particle extends GameEntity {
    private static final String[] IMAGES = {"res/circle-particle.png", "res/rectangle-particle.png", "res/triangle-particle.png"};
    private static final Double[] PROBABILITY_DISTRIBUTION = {0.3, 0.1, 0.6};
    public static final int NUM_IMAGES = 3;

    public boolean hidden;

    public Particle(int particleType, Point point) {
        super(point, new Image(IMAGES[particleType]));
        this.hidden = false;
    }

    public static int generateRandomParticleType() {
        Random rand = new Random();
        double probabilitySum = 0;
        double randomNumber = rand.nextDouble();
        int particleType = 0;

        // search for where the randomNumber lies on the
        // probability distribution
        for (int i = 0; i < NUM_IMAGES; i++) {
            probabilitySum += PROBABILITY_DISTRIBUTION[i];
            if (randomNumber < probabilitySum) {
                particleType = i;
                break;
            }
        }

        return particleType;
    }

    public void toggleHidden() {
        this.hidden = !this.hidden;
    }

    public boolean getHidden() {
        return this.hidden;
    }
}
