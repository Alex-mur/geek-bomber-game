package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BoxActor extends Actor {

    public BoxActor(float x, float y) {
        texture = Assets.getInstance().getAtlas().findRegion("box/box_stay").getTexture();
        position = new Vector2();
        collider = new Rectangle(x, y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        setPosition(x, y);
        currentHealth = 50;
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 0, 0, 80, 80, 1, 1, 0, 0, 0, 160, 160, false, false);
    }

    public void update(float dt) {
        if (currentHealth <= 0)
            destruct();
    }

    public void destruct() {
        removeFromActorsList();
        Map.map.clearCell(getCellX(), getCellY());
        AnimationEmitter.emitter.createAnimation(position.x, position.y, AnimationEmitter.AnimationType.BOX_DESTRUCT);
    }

}
