import bagel.Image;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.util.Point;

public class Player extends GameEntity {
    public double size = 1.00;
    public double STEP_SIZE = 2.00;
    public int score = 0;
    public boolean hidden;

    public Player(Point point, Image image) {
        super(point, image);
        this.hidden = false;
    }

    /* CHANGED BY PETER */
    public void moveLeft(double x) {
        Point currPoint = this.getPoint();
        Point newPoint = new Point(currPoint.x - x, currPoint.y);
        this.setPoint(newPoint);
    }

    public void moveRight(double x) {
        Point currPoint = this.getPoint();
        Point newPoint = new Point(currPoint.x + x, currPoint.y);
        this.setPoint(newPoint);
    }

    public void moveUp(double y) {
        Point currPoint = this.getPoint();
        Point newPoint = new Point(currPoint.x, currPoint.y - y);
        this.setPoint(newPoint);
    }

    public void moveDown(double y) {
        Point currPoint = this.getPoint();
        Point newPoint = new Point(currPoint.x, currPoint.y + y);
        this.setPoint(newPoint);
    }

    public void toggleHidden() {
        this.hidden = !this.hidden;
    }

    public boolean getHidden() {
        return this.hidden;
    }

    /**
     * Movement of the player with - out of bounds - checker
     */
    public void movement1(Player player, Input input) {
        if (player.getPoint().x > 20 + size && input.isDown(Keys.LEFT)) {
            player.moveLeft(STEP_SIZE);
        }
        if (player.getPoint().x < 1900 + size && input.isDown(Keys.RIGHT)) {
            player.moveRight(STEP_SIZE);
        }
        if (player.getPoint().y > 20  + size && input.isDown(Keys.UP)) {
            player.moveUp(STEP_SIZE);
        }
        if (player.getPoint().y < 1060 + size && input.isDown(Keys.DOWN)) {
            player.moveDown(STEP_SIZE);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    public void movement2(Player player, Input input) {
        if (player.getPoint().x > 20 + size && input.isDown(Keys.A)) {
            player.moveLeft(STEP_SIZE);
        }
        if (player.getPoint().x < 1900 + size && input.isDown(Keys.D)) {
            player.moveRight(STEP_SIZE);
        }
        if (player.getPoint().y > 20  + size && input.isDown(Keys.W)) {
            player.moveUp(STEP_SIZE);
        }
        if (player.getPoint().y < 1060 + size && input.isDown(Keys.S)) {
            player.moveDown(STEP_SIZE);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

}
