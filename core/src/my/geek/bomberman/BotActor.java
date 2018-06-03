package my.geek.bomberman;


import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Vector;

public class BotActor extends Actor {

    public enum Direction {
        LEFT(-1, 0, 2), RIGHT(1, 0, 0), UP(0, 1, 1), DOWN(0, -1, 3);

        private int dx;
        private int dy;
        int directionID;

        Direction(int dx, int dy, int directionID) {
            this.dx = dx;
            this.dy = dy;
            this.directionID = directionID;
        }
    }


    private float speed;
    private boolean isMoving;
    private float currentDistance;
    private int currentDirection;
    private Vector<Integer> solidElements;

    private Animation<TextureRegion> walk_left, walk_right, walk_up, walk_down;
    private TextureRegion currentFrame, stay_right, stay_up, stay_left, stay_down;
    private float animationSpeed;
    private float stateTime;
    boolean isActive;

    public BotActor(GameScreen gs) {
        
        this.gs = gs;
        currentFrame = Assets.getInstance().getAtlas().findRegion("alien/walk_down", 0);
        position = new Vector2(-200, -200);
        collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE / 1.8f, Mgmt.CELL_SIZE / 1.8f);

        isActive = false;

        speed = Mgmt.GAME_SPEED * 1.4f;
        velocity = new Vector2(0, 0);
        animationSpeed = 0.07f;
        stateTime = 0f;
        solidElements = new Vector<>();
        solidElements.add(1); //walls
        solidElements.add(2); //boxes
        solidElements.add(4); //bombs
        currentHealth = 100;

        walk_right = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("alien/walk_right"), Animation.PlayMode.LOOP);
        walk_up = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("alien/walk_up"), Animation.PlayMode.LOOP);
        walk_left = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("alien/walk_left"), Animation.PlayMode.LOOP);
        walk_down = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("alien/walk_down"), Animation.PlayMode.LOOP);

        stay_right = Assets.getInstance().getAtlas().findRegion("alien/walk_right", 0);
        stay_up = Assets.getInstance().getAtlas().findRegion("alien/walk_up", 0);
        stay_left = Assets.getInstance().getAtlas().findRegion("alien/walk_left", 0);
        stay_down = Assets.getInstance().getAtlas().findRegion("alien/walk_down", 0);

        isMoving = false;
        currentDistance = 0;
        currentDirection = 3; //0 - right, 1 - up, 2 - left, 3 - down
    }

    public void activate(int cellX, int cellY) {
        currentHealth = 100;
        activate();
        position.set(cellX * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, cellY * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
        collider.setPosition(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);
        isActive = true;
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE - 5, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1.10f, 1.10f, 0);
    }


    public void update(float dt) {
        if (!isActive) {
            currentFrame = stay_down;
            return;
        }

        if (currentHealth <= 0) {
            dying();
        }

        if (!isMoving) {
            if (currentDirection == 0)
                currentFrame = stay_right;

            if (currentDirection == 1)
                currentFrame = stay_up;

            if (currentDirection == 2)
                currentFrame = stay_left;

            if (currentDirection == 3)
                currentFrame = stay_down;

            Direction direction = Direction.values()[MathUtils.random(0, 3)];
            if (!solidElements.contains(gs.getMap().getCellType(getCellX() + direction.dx, getCellY() + direction.dy))) {
                velocity.set(direction.dx * speed, direction.dy * speed);
                currentDirection = direction.directionID;
                isMoving = true;
            }

        } else {
            stateTime += dt;
            position.mulAdd(velocity, dt);
            currentDistance += velocity.len() * dt;
            collider.setPosition(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);

            if (currentDirection == 0) {
                currentFrame = walk_right.getKeyFrame(stateTime);
            }
            if (currentDirection == 1) {
                currentFrame = walk_up.getKeyFrame(stateTime);
            }
            if (currentDirection == 2) {
                currentFrame = walk_left.getKeyFrame(stateTime);
            }
            if (currentDirection == 3) {
                currentFrame = walk_down.getKeyFrame(stateTime);
            }

            if (currentDistance >= Mgmt.CELL_SIZE) {
                position.x = getCellX() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                position.y = getCellY() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                collider.setPosition(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);
                isMoving = false;
                currentDistance = 0;
                stateTime = 0;
            }
        }
        checkCollisions();
    }

    public void dying() {
        removeFromActorsList();
        isActive = false;
        AnimationEmitter.emitter.createAnimation(position.x, position.y, AnimationEmitter.AnimationType.ALIEN_BLAST);
    }

    public void setInactive() {
        isActive = false;
    }

    public void setActive() {
        isActive = true;
    }

    @Override
    protected void collideWithManActorAction(ManActor a) {
        a.addDamage(1);
    }
}
