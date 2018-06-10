package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.TextArea;

public class PickUpActor extends Actor {

    private TextureRegion currentRegion;

    public enum Type {
        HP_100
    }

    private Type currentType;

    public PickUpActor(int cellX, int cellY, Type type) {
        super();
        currentRegion = Assets.getInstance().getAtlas().findRegion("pickUP");
        this.currentType = type;
        this.position = new Vector2(cellX * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, cellY * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
        this.collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentRegion, position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, 0, 0, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE, 1, 1, 0);
    }

    public void addBonusToMan(ManActor a) {
        switch (currentType) {
            case HP_100:
                a.addHP(100);
        }
    }


}
