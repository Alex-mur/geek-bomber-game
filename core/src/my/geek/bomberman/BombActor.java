package my.geek.bomberman;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BombActor extends Actor{

    public enum BombType {
        BOMB(3, 200, 999, AnimationEmitter.AnimationType.BOMB_BLAST, false, 3),
        NUKE(10, 400, 999, AnimationEmitter.AnimationType.NUKE_BLAST, true, -1);

        private int blastRadius;
        private int damage;
        private int startAmmoCount;
        private AnimationEmitter.AnimationType sfx;
        private boolean isRemoteDetonatable;
        private int timer;

        BombType(int blastRadius, int damage, int startAmmoCount, AnimationEmitter.AnimationType sfx, boolean isRemoteDetonatable, int timer) {
            this.blastRadius = blastRadius;
            this.damage = damage;
            this.startAmmoCount = startAmmoCount;
            this.sfx = sfx;
            this.isRemoteDetonatable = isRemoteDetonatable;
            this.timer = timer;
        }

        public int getStartAmmoCount() {
            return startAmmoCount;
        }
    }
    private static Queue<BombActor> detonateQueue;
    public BombType currentType;
    private boolean isActive;
    private Animation<TextureRegion> bomb_stay;
    private float stateTime;
    private ManActor owner;
    ArrayList<Actor> checkList;

    public static Queue<BombActor> getDetonateQueue() {
        return detonateQueue;
    }

    public BombActor(BombType type, GameScreen gs){
        this.gs = gs;
        position = new Vector2(-300, -300);
        collider = new Rectangle(position.x, position.y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        bomb_stay = new Animation(0.007f, Assets.getInstance().getAtlas().findRegions("bomb/bomb_stay"), Animation.PlayMode.LOOP);
        stateTime = 0;
        currentType = type;
        isActive = false;
        detonateQueue = new LinkedList<>();
    }

    public void activate(float x, float y, ManActor owner) {
        isActive = true;
        this.owner = owner;
        Actor.activeActorsList.add(this);
        setPosition(x, y);
        gs.getMap().setCellType(getCellX(), getCellY(), Map.CellType.CELL_BOMB);
        if (currentType.isRemoteDetonatable) {
            detonateQueue.add(this);
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(bomb_stay.getKeyFrame(stateTime), position.x + 5, position.y + 5, 0, 0, 80, 80, 0.8f, 0.8f, 0);
    }

    public void update(float dt) {
        if (isActive) {
            stateTime += dt;
            if (!currentType.isRemoteDetonatable && stateTime >= currentType.timer)
                detonate();
        }
    }

    public void detonate() {
        removeFromActorsList();
        gs.getMap().clearCell(getCellX(), getCellY());
        isActive = false;
        stateTime = 0;
        detonateQueue.remove(this);
        AnimationEmitter.emitter.createAnimation(position.x, position.y, currentType.sfx);
        checkCollisions();
        //blast up
        for (int i = 1; i <= currentType.blastRadius; i++) {
            setColliderCell(getCellX(), getCellY() + i);
            checkList = checkCollisions();
            if (!checkList.isEmpty()) {
                if (!(checkList.get(0) instanceof BotActor || checkList.get(0) instanceof ManActor)) break;
            } else {
                AnimationEmitter.emitter.createAnimation(position.x, position.y + (i * Mgmt.CELL_SIZE), currentType.sfx);
            }
        }

        //blast down
        for (int i = 1; i <= currentType.blastRadius; i++) {
            setColliderCell(getCellX(), getCellY() - i);
            checkList = checkCollisions();
            if (!checkList.isEmpty()) {
                if (!(checkList.get(0) instanceof BotActor || checkList.get(0) instanceof ManActor)) break;
            } else {
                AnimationEmitter.emitter.createAnimation(position.x, position.y - (i * Mgmt.CELL_SIZE), currentType.sfx);
            }
        }

        //blast left
        for (int i = 1; i <= currentType.blastRadius; i++) {
            setColliderCell(getCellX() - i, getCellY());
            checkList = checkCollisions();
            if (!checkList.isEmpty()) {
                if (!(checkList.get(0) instanceof BotActor || checkList.get(0) instanceof ManActor)) break;
            } else {
                AnimationEmitter.emitter.createAnimation(position.x - (i * Mgmt.CELL_SIZE), position.y, currentType.sfx);
            }
        }

        //blast right
        for (int i = 1; i <= currentType.blastRadius; i++) {
            setColliderCell(getCellX() + i, getCellY());
            checkList = checkCollisions();
            if (!checkList.isEmpty()) {
                if (!(checkList.get(0) instanceof BotActor || checkList.get(0) instanceof ManActor)) break;
            } else {
                AnimationEmitter.emitter.createAnimation(position.x + (i * Mgmt.CELL_SIZE), position.y, currentType.sfx);
            }
        }
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    protected void collideWithBoxActorAction(BoxActor a) {
        a.addDamage(currentType.damage);
        AnimationEmitter.emitter.createAnimation(a.position.x, a.position.y, currentType.sfx);
        owner.addScore(100);
    }

    @Override
    protected void collideWithBombActorAction(BombActor a) {
        a.detonate();
    }

    @Override
    protected void collideWithManActorAction(ManActor a) {
        a.addDamage(currentType.damage);
        AnimationEmitter.emitter.createAnimation(a.getCellX() * Mgmt.CELL_SIZE, a.getCellY() * Mgmt.CELL_SIZE, currentType.sfx);
    }

    @Override
    protected void collideWithBotActor(BotActor a) {
        a.addDamage(currentType.damage);
        AnimationEmitter.emitter.createAnimation(a.getCellX() * Mgmt.CELL_SIZE, a.getCellY() * Mgmt.CELL_SIZE, currentType.sfx);
        owner.addScore(250);
    }
}
