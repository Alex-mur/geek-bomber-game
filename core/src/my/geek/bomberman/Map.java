package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.IOException;

public class Map {
    public static final int MAP_CELLS_WIDTH = 16;
    public static final int MAP_CELLS_HEIGHT = 9;

    public enum CellType {
        CELL_EMPTY(0),
        CELL_WALL(1),
        CELL_BOX(2),
        CELL_BOT(3),
        CELL_BOMB(4);

        private int id;

        private CellType (int id) {
            this.id = id;
        }
    }

    private int mapWidth;
    private int mapHeight;
    private Vector2 startPosition;

    private int[][] data;
    private Texture textureGrass;
    private GameScreen gs;
    private MapEditorScreen mes;
    private Actor[][] mapActors;

    public Map(GameScreen gs, String mapName) {
        this.gs = gs;
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();
        loadMap(mapName);
    }

    public Map(MapEditorScreen mes, int width, int height) {
        this.mes = mes;
        this.mapWidth = width;
        this.mapHeight = height;
        data = new int[getMapWidth()][getMapHeight()];
        mapActors = new Actor[getMapWidth()][getMapHeight()];
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();
    }

    public void loadMap(String mapName) {
        BufferedReader br = null;
        try {
            br = Gdx.files.internal(mapName).reader(8192);
            String str;
            mapWidth = Integer.parseInt(br.readLine());
            mapHeight = Integer.parseInt(br.readLine());
            data = new int[mapWidth][mapHeight];
            mapActors = new Actor[mapWidth][mapHeight];
            for (int i = mapHeight - 1; i >= 0; i--) {
                str = br.readLine();
                for (int j = 0; j < mapWidth; j++) {
                    char c = str.charAt(j);
                    switch (c) {
                        case '0':
                            setCellType(j, i, CellType.CELL_EMPTY);
                            break;
                        case '1':
                            setCellType(j, i, CellType.CELL_WALL);
                            break;
                        case '2':
                            setCellType(j, i, CellType.CELL_BOX);
                            break;
                        case '3':
                            setCellType(j, i, CellType.CELL_BOT);
                            break;
                        case 's':
                            startPosition = new Vector2(j * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, i * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
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

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
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
