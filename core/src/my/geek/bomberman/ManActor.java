package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class ManActor extends Actor {

    private class BombWeapon {
        private int ammoCount;
        private BombActor.BombType type;
        private BombActor[] bombs;
        private Sound emptySound;

        public BombWeapon(BombActor.BombType type) {
            this.type = type;

            bombs = new BombActor[Mgmt.BOMB_OBJECTS_QUANTITY];
            for (int i = 0; i < bombs.length; i++) {
                bombs[i] = new BombActor(type, gs);
            }

            ammoCount = type.getStartAmmoCount();
            emptySound = Gdx.audio.newSound(Gdx.files.internal("sounds/fart.ogg"));
        }

        public void putBomb(int x, int y, ManActor man) {
            if (ammoCount == 0) {
                emptySound.play();
                return;
            }

            for (int i = 0; i < bombs.length; i++) {
                if (!bombs[i].isActive()) {
                    bombs[i].activate(x * Mgmt.CELL_SIZE, y * Mgmt.CELL_SIZE, man);
                    ammoCount--;
                    break;
                }
            }
        }

        public void detonateBomb() {
            if (!BombActor.getDetonateQueue().isEmpty())
                BombActor.getDetonateQueue().poll().detonate();
        }
    }


    private int score;
    private float speed;
    private boolean isMoving;
    private int currentDistance;
    private int currentFrameDistance;
    private byte currentDirection;
    private Vector<Integer> solidElements;

    private ArrayList<BombWeapon> weaponInventory;
    private BombWeapon currentWeapon;

    private Animation<TextureRegion> walk_left, walk_right, walk_up, walk_down, idle;
    private TextureRegion currentFrame, stay_right, stay_up, stay_left, stay_down;
    private float animationSpeed;
    private float stateTime;
    private float idleStateTime;

    private ArrayList <StringBuilder> guiMessages;
    private BitmapFont font;

    public ManActor(GameScreen gs) {
        this.gs = gs;
        currentFrame = Assets.getInstance().getAtlas().findRegion("man/man_stay_down");
        position = gs.getMap().getStartPosition();
        collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE / 1.8f, Mgmt.CELL_SIZE / 1.8f);

        speed = Mgmt.GAME_SPEED * 1.8f;
        velocity = new Vector2(0, 0);
        animationSpeed = Mgmt.GAME_SPEED * 0.00045f;
        stateTime = 0f;
        idleStateTime = 0f;
        solidElements = new Vector<>();
        solidElements.add(1); //walls
        solidElements.add(2); //boxes
        solidElements.add(4); //bombs
        currentHealth = Mgmt.PLAYER_MAX_HEALTH;
        score = 0;
        font = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);

        //weapons
        weaponInventory = new ArrayList<>();
        weaponInventory.add(new BombWeapon(BombActor.BombType.BOMB));
        weaponInventory.add(new BombWeapon(BombActor.BombType.NUKE));
        currentWeapon = weaponInventory.get(0);

        guiMessages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            guiMessages.add(new StringBuilder());
        }



        walk_right = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("man/man_run_right"), Animation.PlayMode.LOOP);
        walk_up = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("man/man_run_up"), Animation.PlayMode.LOOP);
        walk_left = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("man/man_run_left"), Animation.PlayMode.LOOP);
        walk_down = new Animation(animationSpeed,Assets.getInstance().getAtlas().findRegions("man/man_run_down"), Animation.PlayMode.LOOP);
        idle = new Animation<TextureRegion>(animationSpeed,Assets.getInstance().getAtlas().findRegions("man/man_idle"), Animation.PlayMode.LOOP);

        stay_right = Assets.getInstance().getAtlas().findRegion("man/man_stay_right");
        stay_up = Assets.getInstance().getAtlas().findRegion("man/man_stay_up");
        stay_left = Assets.getInstance().getAtlas().findRegion("man/man_stay_left");
        stay_down = Assets.getInstance().getAtlas().findRegion("man/man_stay_down");

        isMoving = false;
        currentDistance = 0;
        currentFrameDistance = 0;
        currentDirection = 3; //0 - right, 1 - up, 2 - left, 3 - down
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        for (StringBuilder s : guiMessages) {
            s.setLength(0);
        }

        guiMessages.get(0).append("Health: " + currentHealth);
        guiMessages.get(1).append("Score: " + score);
        guiMessages.get(2).append("Weapon: " + currentWeapon.type + " -- " + currentWeapon.ammoCount);

        font.draw(batch, guiMessages.get(0), 20, 700);
        font.draw(batch, guiMessages.get(1), 210, 700);
        font.draw(batch, guiMessages.get(2), 400, 700);

    }

    public void update(float dt) {
        if (currentHealth <= 0) die();

        //change current weapon in cycle
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            for (int i = 0; i < weaponInventory.size(); i++) {
                if (i == weaponInventory.size() - 1) {
                    currentWeapon = weaponInventory.get(0);
                    break;
                }

                if (weaponInventory.get(i) == currentWeapon) {
                    currentWeapon = weaponInventory.get(i + 1);
                    break;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            currentWeapon.putBomb(getCellX(), getCellY(), this);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            currentWeapon.detonateBomb();
        }

        if (!isMoving) {
            stateTime = 0;
            idleStateTime += dt;
            if (idleStateTime >= 3) {
                currentFrame = idle.getKeyFrame(idleStateTime);
            } else {
                if (currentDirection == 0)
                    currentFrame = stay_right;

                if (currentDirection == 1)
                    currentFrame = stay_up;

                if (currentDirection == 2)
                    currentFrame = stay_left;

                if (currentDirection == 3)
                    currentFrame = stay_down;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D) && !(solidElements.contains(gs.getMap().getCellType(getCellX() + 1, getCellY())))) {
                currentDirection = 0;
                velocity.set(speed, 0);
                isMoving = true;
                idleStateTime = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) && !(solidElements.contains(gs.getMap().getCellType(getCellX(), getCellY() + 1)))) {
                currentDirection = 1;
                velocity.set(0, speed);
                isMoving = true;
                idleStateTime = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A) && !(solidElements.contains(gs.getMap().getCellType(getCellX() - 1, getCellY())))) {
                currentDirection = 2;
                velocity.set(-speed, 0);
                isMoving = true;
                idleStateTime = 0;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S) && !(solidElements.contains(gs.getMap().getCellType(getCellX(), getCellY() - 1)))) {
                currentDirection = 3;
                velocity.set(0, -speed);
                isMoving = true;
                idleStateTime = 0;
            }

        } else {
            stateTime += dt;

            if (currentDirection == 0) {
                currentFrameDistance = (int)(speed * dt);
                currentDistance += currentFrameDistance;
                if (currentDistance <= Mgmt.CELL_SIZE) {
                    position.x += currentFrameDistance;
                } else {
                    position.x = getCellX() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                    if (!(Gdx.input.isKeyPressed(Input.Keys.D) && !(solidElements.contains(gs.getMap().getCellType(getCellX() + 1, getCellY()))))) {
                        isMoving = false;
                    }
                    currentDistance = 0;
                    currentFrameDistance = 0;
                }
                currentFrame = walk_right.getKeyFrame(stateTime);
            }
            if (currentDirection == 1) {
                currentFrameDistance = (int)(speed * dt);
                currentDistance += currentFrameDistance;
                if (currentDistance <= Mgmt.CELL_SIZE) {
                    position.y += currentFrameDistance;
                } else {
                    position.y = getCellY() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                    if (!(Gdx.input.isKeyPressed(Input.Keys.W) && !(solidElements.contains(gs.getMap().getCellType(getCellX(), getCellY() + 1))))) {
                        isMoving = false;
                    }
                    currentDistance = 0;
                    currentFrameDistance = 0;
                }
                currentFrame = walk_up.getKeyFrame(stateTime);
            }
            if (currentDirection == 2) {
                currentFrameDistance = (int)(speed * dt);
                currentDistance += currentFrameDistance;
                if (currentDistance <= Mgmt.CELL_SIZE) {
                    position.x -= currentFrameDistance;
                } else {
                    position.x = getCellX() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                    if (!(Gdx.input.isKeyPressed(Input.Keys.A) && !(solidElements.contains(gs.getMap().getCellType(getCellX() - 1, getCellY()))))) {
                        isMoving = false;
                    }
                    currentDistance = 0;
                    currentFrameDistance = 0;
                }
                currentFrame = walk_left.getKeyFrame(stateTime);
            }
            if (currentDirection == 3) {
                currentFrameDistance = (int)(speed * dt);
                currentDistance += currentFrameDistance;
                if (currentDistance <= Mgmt.CELL_SIZE) {
                    position.y -= currentFrameDistance;
                } else {
                    position.y = getCellY() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
                    if (!(Gdx.input.isKeyPressed(Input.Keys.S) && !(solidElements.contains(gs.getMap().getCellType(getCellX(), getCellY() - 1))))) {
                        isMoving = false;
                    }
                    currentDistance = 0;
                    currentFrameDistance = 0;
                }
                currentFrame = walk_down.getKeyFrame(stateTime);
            }

            collider.setPosition(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);
        }
        checkCollisions();
    }

    private void centerAndStopActorInCurrentCell() {
        position.x = getCellX() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
        position.y = getCellY() * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE;
        collider.setPosition(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE);
        isMoving = false;
        currentDistance = 0;
        stateTime = 0;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void addHP(int hp) {
        currentHealth += hp;
    }

    public void die() {
        ScreenManager.getInstance().endGame();
    }

    @Override
    protected void collideWithDoorActor(DoorActor a) {
        a.removeFromActorsList();
        ScreenManager.getInstance().nextLevel();
    }
}
