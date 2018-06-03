package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WallActor extends Actor{

    public WallActor(float x, float y) {
        texture = Assets.getInstance().getAtlas().findRegion("wall/wall_stone").getTexture();
        position = new Vector2();
        collider = new Rectangle(x, y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        setPosition(x, y);
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1.025f, 1.025f, 0, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, false, false);
    }
}
