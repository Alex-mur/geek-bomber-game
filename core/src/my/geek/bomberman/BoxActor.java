package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoxActor extends Actor {

        
    public BoxActor(float x, float y, GameScreen gs) {
        this.gs = gs;
        texture = Assets.getInstance().getAtlas().findRegion("box/box_stay").getTexture();
        position = new Vector2();
        collider = new Rectangle(x, y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        setPosition(x, y);
        currentHealth = 50;
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1, 1, 0, 0, 0, 160, 160, false, false);
    }

    public void update(float dt) {
        if (currentHealth <= 0)
            destruct();
    }

    public void destruct() {
        if (gs.getMap().getDoorPosition().x - Mgmt.CELL_HALF_SIZE == position.x && gs.getMap().getDoorPosition().y - Mgmt.CELL_HALF_SIZE == position.y) {
            gs.getMap().getDoor().activate();
        }
        removeFromActorsList();
        gs.getMap().clearCell(getCellX(), getCellY());
        AnimationEmitter.emitter.createAnimation(position.x, position.y, AnimationEmitter.AnimationType.BOX_DESTRUCT);
    }
}
