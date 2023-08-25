import bagel.Image;
import bagel.util.Point;

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
}
