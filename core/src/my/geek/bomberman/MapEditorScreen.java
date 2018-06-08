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
        ActorsKeeper.getInstance().updateActiveActors(dt);
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
        menuGroup.setPosition(0, 0);
        Table menuTable = new Table();
        menuTable.setFillParent(true);
        menuTable.top();
        Button grassBlock = new Button(skin.getDrawable("ground/grass"));
        Button wallBlock = new Button(skin.getDrawable("wall/wall_stone"));
        Button boxBlock = new Button(skin.getDrawable("box/box_stay"));
        Button botBlock = new Button(skin.getDrawable("box/box_stay"));

        TextButton hideMenu = new TextButton("hide", skin, "simpleSkin");

        menuTable.add(grassBlock);
        menuTable.add();
        menuTable.add(wallBlock);
        menuTable.add();
        menuTable.add(boxBlock);
        menuTable.row();
        menuTable.add(botBlock);
        menuTable.row();
        menuTable.add(hideMenu);

        menuGroup.addActor(menuTable);

        grassBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapEditor.setCurrentType(Map.CellType.CELL_EMPTY);
            }
        });
        wallBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapEditor.setCurrentType(Map.CellType.CELL_WALL);
            }
        });
        boxBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapEditor.setCurrentType(Map.CellType.CELL_BOX);
            }
        });
        botBlock.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapEditor.setCurrentType(Map.CellType.CELL_BOT);
            }
        });

        hideMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isMenuHide) {
                    menuGroup.setPosition(100, 300);
                } else {
                    menuGroup.setPosition(100, 1100);
                }
            }
        });

        stage.addActor(menuGroup);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

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
