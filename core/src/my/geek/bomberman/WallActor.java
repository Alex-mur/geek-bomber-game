package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WallActor extends Actor{

    private TextureRegion currentRegion;

    public WallActor(float x, float y, GameScreen gs) {
        this.gs = gs;
        currentRegion = Assets.getInstance().getAtlas().findRegion("wall/wall_stone");
        position = new Vector2(x, y);
        collider = new Rectangle(x, y, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentRegion, position.x, position.y, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1f, 1f, 0);
    }
}
