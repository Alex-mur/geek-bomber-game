package my.geek.bomberman;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class PickUpActor extends Actor {

    public enum Type {
        HP_100(1);

        private int id;

        private Type (int id) {
            this.id = id;
        }
    }

    private Type currentType;

    public PickUpActor(int cellX, int cellY, Type type) {
        super();
        this.currentType = type;
        this.position = new Vector2(cellX * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, cellY * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
        this.collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE, Mgmt.CELL_SIZE);
    }


}
