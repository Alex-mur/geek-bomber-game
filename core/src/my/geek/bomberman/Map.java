package my.geek.bomberman;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    public static final int MAP_CELLS_WIDTH = 16;
    public static final int MAP_CELLS_HEIGHT = 9;

    public enum CellType {
        CELL_EMPTY(0),
        CELL_WALL(1),
        CELL_BOX(2),
        CELL_BOMB(3),
        CELL_BOT(4);

        private int id;

        private CellType (int id) {
            this.id = id;
        }
    }


    private int[][] data;
    private Texture textureGrass;
    private GameScreen gs;
    private Actor[][] mapActors;

    public Map(GameScreen gs) {
        this.gs = gs;
        data = new int[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        mapActors = new Actor[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();


        for (int i = 0; i < MAP_CELLS_WIDTH; i++) {
            data[i][0] = CellType.CELL_WALL.id;
            mapActors[i][0] = new WallActor(-100, -100, gs);
            data[i][MAP_CELLS_HEIGHT - 1] = CellType.CELL_WALL.id;
            mapActors[i][MAP_CELLS_HEIGHT - 1] = new WallActor(-100, -100, gs);
        }
        for (int i = 0; i < MAP_CELLS_HEIGHT; i++) {
            data[0][i] = CellType.CELL_WALL.id;
            mapActors[0][i] = new WallActor(-100, -100, gs);
            data[MAP_CELLS_WIDTH - 1][i] = CellType.CELL_WALL.id;
            mapActors[MAP_CELLS_WIDTH - 1][i] = new WallActor(-100,-100, gs);
        }
        for (int i = 0; i < MAP_CELLS_WIDTH; i++) {
            for (int j = 0; j < MAP_CELLS_HEIGHT; j++) {
                if(i % 2 == 0 && j % 2 == 0 && data[i][j] == CellType.CELL_EMPTY.id) {
                    data[i][j] = CellType.CELL_BOX.id;
                    mapActors[i][j] = new BoxActor(-100, -100, gs);
                }
            }
        }
    }

    public Map(int type) {
        data = new int[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        mapActors = new Actor[MAP_CELLS_WIDTH][MAP_CELLS_HEIGHT];
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < MAP_CELLS_WIDTH; i++) {
            for (int j = 0; j < MAP_CELLS_HEIGHT; j++) {
                batch.draw(textureGrass, i * Mgmt.CELL_SIZE, j * Mgmt.CELL_SIZE, 0, 0, 80, 80, 1.05f ,1.05f, 0, 0, 0, 160, 160, false, false);
                if (data[i][j] == CellType.CELL_WALL.id) {
                    mapActors[i][j].setPosition(i * Mgmt.CELL_SIZE,j * Mgmt.CELL_SIZE);
                }
                if (data[i][j] == CellType.CELL_BOX.id) {
                    mapActors[i][j].setPosition(i * Mgmt.CELL_SIZE,j * Mgmt.CELL_SIZE);
                }
            }
        }
    }

    public boolean isCellEmpty(int cellX, int cellY) {
        return data[cellX][cellY] == CellType.CELL_EMPTY.id;
    }

    public int getCellType(int cellX, int cellY) {
        return data[cellX][cellY];
    }

    public void setCellType(int cellX, int cellY, CellType type) {
        if (type == CellType.CELL_WALL) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new WallActor(cellX * Mgmt.CELL_SIZE, cellY * Mgmt.CELL_SIZE, gs);
        }

        if (type == CellType.CELL_BOX) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new BoxActor(cellX * Mgmt.CELL_SIZE, cellY * Mgmt.CELL_SIZE, gs);
        }

        if (type == CellType.CELL_BOT) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new BotActor(gs);
            ((BotActor)mapActors[cellX][cellY]).activate(cellX, cellY);
            ((BotActor)mapActors[cellX][cellY]).setPassive();
        }

        if (type == CellType.CELL_EMPTY) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
        }

        data[cellX][cellY] = type.id;
    }

    public void clearCell (int cellX, int cellY) {
        data[cellX][cellY] = CellType.CELL_EMPTY.id;
    }
}
