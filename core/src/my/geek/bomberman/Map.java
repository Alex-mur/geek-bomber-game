package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public class Map {

    public enum Level {
        level_1("maps/level_1.dat", 1),
        level_2("maps/level_2.dat", 2),
        level_3("maps/level_3.dat", 3),
        level_4("maps/level_4.dat", 4);

        String mapPath;
        int mapID;

        private Level(String mapPath, int mapID) {
            this.mapPath = mapPath;
            this.mapID = mapID;
        }

        public static Level getLevelByID(int id) {
            for (Level l : values()) {
                if (l.mapID == id)
                    return l;
            }
            return level_1;
        }

        public String getMapPath() {
            return mapPath;
        }

    }

    public enum CellType {
        CELL_EMPTY(0),
        CELL_WALL(1),
        CELL_BOX(2),
        CELL_BOT(3),
        CELL_BOMB(4);

        private int id;

        CellType (int id) {
            this.id = id;
        }
    }

    private int mapWidth;
    private int mapHeight;
    private Vector2 startPosition;
    private Vector2 doorPosition;

    private int[][] data;
    private Texture textureGrass;
    private GameScreen gs;
    private MapEditorScreen mes;
    private Actor[][] mapActors;
    private DoorActor door;

    public Map(GameScreen gs, String mapName) {
        this.gs = gs;
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();
        loadMap(mapName);
    }

    public Map(MapEditorScreen mes, int cellsX, int cellsY) {
        this.mes = mes;
        this.mapWidth = cellsX;
        this.mapHeight = cellsY;
        data = new int[getMapWidth()][getMapHeight()];
        mapActors = new Actor[getMapWidth()][getMapHeight()];
        textureGrass = Assets.getInstance().getAtlas().findRegion("ground/grass").getTexture();
        createWalls();
    }

    private void createWalls() {
        for (int x = 0; x < mapWidth; x++) {
            setCellType(x, 0, CellType.CELL_WALL);
            setCellType(x, mapHeight - 1, CellType.CELL_WALL);
        }

        for (int y = 0; y < mapHeight; y++) {
            setCellType(0, y, CellType.CELL_WALL);
            setCellType(mapWidth - 1, y, CellType.CELL_WALL);
        }
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
                            ((BotActor)mapActors[j][i]).setActive();
                            break;
                        case 's':
                            startPosition = new Vector2(j * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, i * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
                            break;
                        case 'd':
                            doorPosition = new Vector2(j * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE, i * Mgmt.CELL_SIZE + Mgmt.CELL_HALF_SIZE);
                            setCellType(j, i, CellType.CELL_BOX);
                            door = new DoorActor(doorPosition);
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

    public void saveMap() {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(Gdx.files.local("custom_maps/test_dat").writer(false));
            bw.write(String.valueOf(mapWidth));
            bw.newLine();
            bw.write(String.valueOf(mapHeight));
            bw.newLine();

            for (int i = data.length - 1; i >= 0 ; i--) {
                for (int j = 0; j < data.length; j++) {
                    bw.write(String.valueOf(data[j][i]));
                }
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                batch.draw(textureGrass, i * Mgmt.CELL_SIZE, j * Mgmt.CELL_SIZE, 0, 0, 80, 80, 1.05f ,1.05f, 0, 0, 0, 160, 160, false, false);
            }
        }
    }

    public boolean isCellEmpty(int cellX, int cellY) {
        return data[cellX][cellY] == CellType.CELL_EMPTY.id;
    }

    public DoorActor getDoor() {
        return door;
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

    public Vector2 getStartPosition() {
        return startPosition;
    }

    public Vector2 getDoorPosition() {
        return doorPosition;
    }

    public void setCellType(int cellX, int cellY, CellType type) {
        if (type == CellType.CELL_WALL) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new WallActor(cellX * Mgmt.CELL_SIZE, cellY * Mgmt.CELL_SIZE, gs);
            data[cellX][cellY] = type.id;
        }

        if (type == CellType.CELL_BOX) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new BoxActor(cellX * Mgmt.CELL_SIZE, cellY * Mgmt.CELL_SIZE, gs);
            data[cellX][cellY] = type.id;
        }

        if (type == CellType.CELL_BOT) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            mapActors[cellX][cellY] = new BotActor(gs);
            ((BotActor)mapActors[cellX][cellY]).activate(cellX, cellY);
            ((BotActor)mapActors[cellX][cellY]).setInactive();
            data[cellX][cellY] = CellType.CELL_EMPTY.id;
        }

        if (type == CellType.CELL_BOMB) {
            data[cellX][cellY] = type.id;
        }

        if (type == CellType.CELL_EMPTY) {
            if (mapActors[cellX][cellY] != null) mapActors[cellX][cellY].removeFromActorsList();
            data[cellX][cellY] = type.id;
        }


    }

    public void clearCell (int cellX, int cellY) {
        data[cellX][cellY] = CellType.CELL_EMPTY.id;
    }
}
