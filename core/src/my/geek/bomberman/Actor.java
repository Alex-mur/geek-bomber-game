package my.geek.bomberman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Actor {

    protected Vector2 position;
    protected Vector2 velocity;
    protected Texture texture;
    protected Rectangle collider;
    protected int currentHealth;
    protected GameScreen gs;

    public Actor() {
        ActorsKeeper.getInstance().getPassiveActorsList().add(this);
    }

    public void activate() {
        ActorsKeeper.getInstance().getActiveActorsList().add(this);
        ActorsKeeper.getInstance().getPassiveActorsList().remove(this);
    }

    public void deactivate() {
        ActorsKeeper.getInstance().getPassiveActorsList().add(this);
        ActorsKeeper.getInstance().getActiveActorsList().remove(this);
    }

    public void render(SpriteBatch batch){}

    public void update(float dt){}

    public ArrayList<Actor> checkCollisions() {
        //boolean isCollide = false;
        ArrayList<Actor> list = new ArrayList<>();
        for (Actor a : ActorsKeeper.getInstance().getActiveActorsList()) {
            if (this.equals(a)) continue;
            if (this.collider.overlaps(a.collider)) {

                if (a instanceof BombActor) {
                    collideWithBombActorAction((BombActor) a);
                    list.add(a);
                }

                if (a instanceof BoxActor) {
                    collideWithBoxActorAction((BoxActor) a);
                    list.add(a);
                }

                if (a instanceof ManActor) {
                    collideWithManActorAction((ManActor) a);
                    list.add(a);
                }

                if (a instanceof WallActor) {
                    collideWithWallActorAction((WallActor) a);
                    list.add(a);
                }

                if (a instanceof BotActor) {
                    collideWithBotActor((BotActor) a);
                    list.add(a);
                }

                if (a instanceof DoorActor) {
                    collideWithDoorActor((DoorActor) a);
                    list.add(a);
                }

                if (a instanceof PickUpActor) {
                    collideWithPickUpActor((PickUpActor) a);
                    list.add(a);
                }
            }
        }
        return list;
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

    protected void collideWithDoorActor(DoorActor a) {}

    protected void collideWithPickUpActor(PickUpActor a) {}

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
        ActorsKeeper.getInstance().getActiveActorsList().remove(this);
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public Vector2 getPosition() {
        return position;
    }
}
