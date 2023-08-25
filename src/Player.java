import bagel.Image;
import bagel.util.Point;

public class Player extends GameEntity {
    int size = 0;

    public Player(Point point, Image image) {
        super(point, image);
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
    /* -------------------------------------------------------------- */

}
