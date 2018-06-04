package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


public class DoorActor extends Actor {

    private Animation<TextureRegion> animation;
    private float stateTime;

    public DoorActor(Vector2 doorPosition) {
        this.gs = gs;
        animation = new Animation(0.05f, Assets.getInstance().getAtlas().findRegions("door/vortex"), Animation.PlayMode.LOOP);
        position = doorPosition;
        collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        //activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(animation.getKeyFrame(stateTime), position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE,0, 0, 80, 80, 1, 1, 0);
    }

    public void update(float dt) {
        stateTime += dt;
    }

}
