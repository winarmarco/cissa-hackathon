import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

public class GameEntity {

    private Point point;
    private Image image;

    public GameEntity(Point point, Image image) {
        this.point = point;
        this.image = image;
    }

    public void draw() {
        this.image.draw(this.point.x, this.point.y);
    }

    public Image getImage() {
        return this.image;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public boolean isIntersect(GameEntity gameEntity) {
        Rectangle r1 = this.image.getBoundingBoxAt(this.point);
        Rectangle r2 = gameEntity.getImage().getBoundingBoxAt(gameEntity.getPoint());

        return r1.intersects(r2);
    }
}
