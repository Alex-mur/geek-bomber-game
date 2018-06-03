package my.geek.bomberman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Actor {

    protected Vector2 position;
    protected Vector2 velocity;
    protected Texture texture;
    protected Rectangle collider;
    protected int currentHealth;

    public static final Set<Actor> activeActorsList = ConcurrentHashMap.newKeySet();
    public static final Set<Actor> passiveActorsList = ConcurrentHashMap.newKeySet();

    public Actor() {
        Actor.passiveActorsList.add(this);
    }

    public void activate() {
        Actor.activeActorsList.add(this);
        Actor.passiveActorsList.remove(this);
    }

    public void deactivate() {
        Actor.passiveActorsList.add(this);
        Actor.activeActorsList.remove(this);
    }

    public void render(SpriteBatch batch){}

    public void update(float dt){}

    public boolean checkCollisions() {
        boolean isCollide = false;
        for (Actor a : Actor.activeActorsList) {
            if (this.equals(a)) continue;
            if (this.collider.overlaps(a.collider)) {
                isCollide = true;
                if (a instanceof BombActor) {
                    collideWithBombActorAction((BombActor) a);
                }

                if (a instanceof BoxActor) {
                    collideWithBoxActorAction((BoxActor) a);
                }

                if (a instanceof ManActor) {
                    collideWithManActorAction((ManActor) a);
                }

                if (a instanceof WallActor) {
                    collideWithWallActorAction((WallActor) a);
                }

                if (a instanceof BotActor) {
                    collideWithBotActor((BotActor) a);
                }
            }
        }
        return isCollide;
    }

    public int getCellX() {
        return (int) (position.x / Mgmt.CELL_SIZE);
    }

    public int getCellY() {
        return (int) (position.y / Mgmt.CELL_SIZE);
    }

    protected void collideWithBombActorAction(BombActor a) {}

    protected void collideWithBoxActorAction(BoxActor a) {}

    protected void collideWithManActorAction(ManActor a) {}

    protected void collideWithWallActorAction(WallActor a) {}

    protected void collideWithBotActor(BotActor a) {}

    protected void setPosition (float x, float y) {
        position.set(x, y);
        collider.setPosition(position.x, position.y);
    }

    protected void setColliderCell(int x, int y){
        collider.setPosition(x * Mgmt.CELL_SIZE, y * Mgmt.CELL_SIZE);
    }

    protected void addDamage(int damage) {
        if (currentHealth > 0) {
            currentHealth -= damage;
            if (currentHealth < 0)
                currentHealth = 0;
        }
    }

    public void removeFromActorsList() {
        Actor.activeActorsList.remove(this);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

}
