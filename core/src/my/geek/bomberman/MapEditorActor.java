package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.*;

public class MapEditorActor extends Actor {

    private Map.CellType currentType;
    private MapEditorScreen mes;
    private float speed;
    private TextureRegion currentFrame;

    private ArrayList <StringBuilder> guiMessages;
    private BitmapFont font;

    public MapEditorActor(MapEditorScreen mes) {
        this.mes = mes;
        currentType = Map.CellType.CELL_WALL;
        currentFrame = Assets.getInstance().getAtlas().findRegion("man/man_stay_down");
        position = new Vector2(120, 120);
        collider = new Rectangle(position.x - Mgmt.CELL_HALF_SIZE, position.y - Mgmt.CELL_HALF_SIZE, Mgmt.CELL_SIZE / 1.8f, Mgmt.CELL_SIZE / 1.8f);

        speed = Mgmt.GAME_SPEED * 3f;
        velocity = new Vector2(0, 0);
        font = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);

        guiMessages = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            guiMessages.add(new StringBuilder());
        }
        activate();
    }

    public void render(SpriteBatch batch) {
        batch.draw(currentFrame, position.x, position.y);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        for (StringBuilder s : guiMessages) {
            s.setLength(0);
        }
        guiMessages.get(0).append("Current block type: " + currentType.toString());
        font.draw(batch, guiMessages.get(0), 20, 700);
    }

    public void update(float dt) {
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
            mes.getMap().setCellType((int) (Gdx.input.getX() / Mgmt.CELL_SIZE), (int) (((Map.MAP_CELLS_HEIGHT * Mgmt.CELL_SIZE) - Gdx.input.getY()) / Mgmt.CELL_SIZE), currentType);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            velocity.set(speed, 0);
            position.mulAdd(velocity, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.set(0, speed);
            position.mulAdd(velocity, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            velocity.set(-speed, 0);
            position.mulAdd(velocity, dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.set(0, -speed);
            position.mulAdd(velocity, dt);
        }
    }

}
