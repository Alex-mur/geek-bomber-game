package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoxActor extends Actor {

    private TextureRegion currentRegion;

    public BoxActor(float x, float y, GameScreen gs) {
        this.gs = gs;
        currentRegion = Assets.getInstance().getAtlas().findRegion("box/box_stay");
        position = new Vector2();
        collider = new Rectangle(x, y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        setPosition(x, y);
        currentHealth = 50;
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentRegion, position.x, position.y, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1, 1, 0);
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
