package my.geek.bomberman;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import org.omg.PortableInterceptor.ACTIVE;

import java.io.BufferedWriter;

public class MapEditorScreen implements Screen {

    public enum Status {
        EDIT, MENU
    }

    private SpriteBatch batch;
    private Map map;
    private MapEditorActor mapEditor;
    private BitmapFont font32;
    private int cellsX;
    private int cellsY;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private boolean isMenuHide;
    private Status status;

    public MapEditorScreen(SpriteBatch batch, Camera camera, int cellsX, int cellsY) {
        this.batch = batch;
        this.cellsX = cellsX;
        this.cellsY = cellsY;
        this.camera = camera;
    }
    public Map getMap() {
        return map;
    }


    @Override
    public void show() {
        map = new Map(this, cellsX, cellsY);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        isMenuHide = true;
        createGUI();
        mapEditor = new MapEditorActor(this, camera);
        font32 = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        status = Status.EDIT;
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.render(batch);
        ActorsKeeper.getInstance().renderActiveActors(batch);
        ScreenManager.getInstance().resetCamera();
        mapEditor.renderGUI(batch, font32);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        cameraUpdate();
        if (status == Status.EDIT) {
            ActorsKeeper.getInstance().updateActiveActors(dt);
        }
    }

    public void cameraUpdate() {
        camera.position.set(mapEditor.getPosition().x, mapEditor.getPosition().y, 0);
        camera.update();
    }

    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Assets.getInstance().getAtlas());
        skin.add("font32", font32);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("controls/btn_menu");
        textButtonStyle.font = font32;
        skin.add("simpleSkin", textButtonStyle);

        final Group menuGroup = new Group();
        Button grassBlock = new Button(skin.getDrawable("ground/grass"));
        Button wallBlock = new Button(skin.getDrawable("wall/wall_stone"));
        Button boxBlock = new Button(skin.getDrawable("box/box_stay"));
        Button botBlock = new Button(skin.getDrawable("box/box_stay"));
        Button hideMenu = new Button(skin.getDrawable("controls/btn_right"));
        menuGroup.addActor(grassBlock);
        menuGroup.addActor(wallBlock);
        menuGroup.addActor(boxBlock);
        menuGroup.addActor(botBlock);
        menuGroup.addActor(hideMenu);
        menuGroup.setPosition(0, 300);
        grassBlock.setPosition(0,0);
        wallBlock.setPosition(0, 120);
        boxBlock.setPosition(120, 0);
        botBlock.setPosition(120,120);
        hideMenu.setPosition(260, 100);


        grassBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((x > 0) && (y > 0) && (x < map.getMapWidth()) && (y < map.getMapHeight())) {
                    mapEditor.setCurrentType(Map.CellType.CELL_EMPTY);
                    menuGroup.setPosition(0, 300);
                    isMenuHide = true;
                    status = Status.EDIT;
                }
            }
        });
        wallBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((x > 0) && (y > 0) && (x < map.getMapWidth()) && (y < map.getMapHeight())) {
                    mapEditor.setCurrentType(Map.CellType.CELL_WALL);
                    menuGroup.setPosition(0, 300);
                    isMenuHide = true;
                    status = Status.EDIT;
                }
            }
        });
        boxBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((x > 0) && (y > 0) && (x < map.getMapWidth()) && (y < map.getMapHeight())) {
                    mapEditor.setCurrentType(Map.CellType.CELL_BOX);
                    menuGroup.setPosition(0, 300);
                    isMenuHide = true;
                    status = Status.EDIT;
                }
            }
        });
        botBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if ((x > 0) && (y > 0) && (x < map.getMapWidth()) && (y < map.getMapHeight())) {
                    mapEditor.setCurrentType(Map.CellType.CELL_BOT);
                    menuGroup.setPosition(0, 300);
                    isMenuHide = true;
                    status = Status.EDIT;
                }
            }
        });

        hideMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isMenuHide) {
                    menuGroup.setPosition(400, 300);
                    status = Status.MENU;
                } else {
                    menuGroup.setPosition(0, 300);
                    isMenuHide = true;
                    status = Status.EDIT;
                }
            }
        });

        stage.addActor(menuGroup);
    }

    @Override
    public void dispose() {}

    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
