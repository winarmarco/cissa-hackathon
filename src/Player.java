import bagel.*;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Player extends GameEntity {
    private Point spawnedPoint;
    private double scaleSize = 1.0;
    private double speed = 2.00;
    public int score = 0;
    public boolean hidden;

    public Player(Point point, Image image) {
        super(point, image);
        this.hidden = false;
        this.spawnedPoint = point;
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

    public double getScaleSize() {
        return scaleSize;
    }

    public void increaseScaleSize(double scaleSizeIncrease) {
        this.scaleSize += scaleSizeIncrease;
    }

    public int getScore() {return this.score;}
    public void increaseScore() {this.score += 1;}

    public double getSpeed() {
        return this.speed;
    }
    public void setSpeed(double newSpeed) {
        if (newSpeed > 0) this.speed = newSpeed;
    }

    public Rectangle getBoundingBox() {
        // first get the original *non-scaled* rectangle
        Rectangle playerRect = this.getImage().getBoundingBoxAt(this.getPoint());
        Point center = this.getPoint();

        // get the original *non-scaled* width and height (here it is dx and dy)
        double dx = playerRect.right() - playerRect.left();
        double dy = playerRect.bottom() - playerRect.top();

        // scale dx and dy, so it matches the currently scaled image rendered
        double playerWidth = dx * scaleSize;
        double playerHeight = dy * scaleSize;

        // create a rectangle on the scaled image render, using the scaled playerWidth and scaled playerHeihgt
        // and get the topLeft of the scaled rectangle as well
        Point scaledRectTopLeft = new Point(center.x - playerWidth / 2, center.y - playerHeight / 2);
        Rectangle scaledRectPlayer = new Rectangle(scaledRectTopLeft, playerHeight, playerWidth);

        return scaledRectPlayer;
    }

    public boolean isIntersect(GameEntity gameEntity, double percentageIntersect) {
        // convert percentageIntersect to decimal
        double decimalIntersect= percentageIntersect / 100.00;

        // get player and gameEntity bounding box rectangle and check whether it intersects or not
        Rectangle playerRect = this.getBoundingBox();
        Rectangle gameEntityRect = gameEntity.getImage().getBoundingBoxAt(gameEntity.getPoint());

        // Find center1 and center2
        Point center1 = playerRect.centre();
        Point center2 = gameEntityRect.centre();

        // find the radius of r1 and r2, by using width / 2;
        double r1 = (playerRect.right() - playerRect.left()) / 2.0 * (decimalIntersect);
        double r2 = (gameEntityRect.right() - gameEntityRect.left()) / 2.0;

        // Now, we have Circle1 and Circle2 with:
        // - center1 and center2 as the centerPoint
        // - and r1 and r2 as the radius

        // Now, we calculate whether Circle1 and Circle2 is intersecting
        // by using the formula: sqrt((x2 - x1)^2 + (y2 - y1)^2) <= r1 + r2
        double dx = center1.x - center2.x;
        double dy = center1.y - center2.y;

        return (Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0)) <= (r1 + r2));
    }

    public boolean isIntersect(GameEntity gameEntity) {
        // By default, if there is no percentageIntersect, it is 100%,
        // which means, the intersection will happen even when the gameEntity
        // touches the player real border.
        return isIntersect(gameEntity, 100);
    }

    public void particleIntersectBehaviour(Particle particle) {
        // if a player intersects with a particle, we
        // - increase the size of player
        // - hide the particle
        // - increase the player score
        // - decrease the speed for every 5 score it gets
        this.increaseScaleSize(0.5);
        particle.toggleHidden();
        this.increaseScore();

        if ((this.score + 1) % 5 == 0) {
            this.setSpeed(this.speed - 0.25);
        }

    }

    public void playerIntersectBehaviour(Player player) {
        player.toggleHidden();
        this.draw();
        this.score += 3;

    }

    public void respawn() {
        this.setPoint(spawnedPoint);
        this.scaleSize = 1.00;
        this.speed = 2.00;
    }


    public void draw() {
        DrawOptions drawOptions = new DrawOptions().setScale(this.scaleSize, this.scaleSize);
        Font sizeTxt = new Font("res/conformable.otf", 40);
        super.draw(drawOptions);


        double textWidth = sizeTxt.getWidth(String.valueOf(this.scaleSize));
        double textHeight = 40.0;
        sizeTxt.drawString(String.valueOf(this.scaleSize), this.getPoint().x - (textWidth / 2), this.getPoint().y + (textHeight / 2));
    }

    /**
     * Movement of the player with - out of bounds - checker
     */
    public void movement2(Player player, Input input) {
        if (player.getPoint().x > 20 + scaleSize && input.isDown(Keys.LEFT)) {
            player.moveLeft(speed);
        }
        if (player.getPoint().x < 1900 + scaleSize && input.isDown(Keys.RIGHT)) {
            player.moveRight(speed);
        }
        if (player.getPoint().y > 20  + scaleSize && input.isDown(Keys.UP)) {
            player.moveUp(speed);
        }
        if (player.getPoint().y < 1060 + scaleSize && input.isDown(Keys.DOWN)) {
            player.moveDown(speed);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

    public void movement1(Player player, Input input) {
        if (player.getPoint().x > 20 + scaleSize && input.isDown(Keys.A)) {
            player.moveLeft(speed);
        }
        if (player.getPoint().x < 1900 + scaleSize && input.isDown(Keys.D)) {
            player.moveRight(speed);
        }
        if (player.getPoint().y > 20  + scaleSize && input.isDown(Keys.W)) {
            player.moveUp(speed);
        }
        if (player.getPoint().y < 1060 + scaleSize && input.isDown(Keys.S)) {
            player.moveDown(speed);
        }
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        }
    }

}
