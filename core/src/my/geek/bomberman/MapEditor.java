package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class MapEditor {
    private Map.CellType currentType;

    public MapEditor() {
       currentType = Map.CellType.CELL_WALL;
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            currentType = Map.CellType.CELL_WALL;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            currentType = Map.CellType.CELL_BOX;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
            currentType = Map.CellType.CELL_BOT;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
            currentType = Map.CellType.CELL_EMPTY;
        }

        if (Gdx.input.justTouched()) {
            Map.map.setCellType((int)(Gdx.input.getX() / Mgmt.CELL_SIZE), (int)(((Map.MAP_CELLS_HEIGHT * Mgmt.CELL_SIZE) - Gdx.input.getY()) / Mgmt.CELL_SIZE), currentType);
        }
    }
}
