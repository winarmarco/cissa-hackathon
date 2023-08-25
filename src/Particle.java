import bagel.Image;
import bagel.util.Point;

public class Particle extends GameEntity {
    private static final String[] IMAGES = {"res/circle-particle.png", "res/rectangle-particle.png", "res/triangle-particle.png"};
    public static final int NUM_IMAGES = 3;

    public Particle(int particleType, Point point) {
        super(point, new Image(IMAGES[particleType]));
    }
}
