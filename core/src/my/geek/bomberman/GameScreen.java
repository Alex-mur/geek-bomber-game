package my.geek.bomberman;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameScreen implements Screen {



    private enum Status {
        PLAY, PAUSE;
    }

    private SpriteBatch batch;
    private Map map;
    private ManActor player;
    private AnimationEmitter animationEmitter;
    private BitmapFont guiFont;
    private BitmapFont infoFont;
    private Camera camera;
    private Stage stage;
    private Skin skin;
    private Group upgradeGroup;
    private Status currentStatus;
    private int levelID;


    public GameScreen(SpriteBatch batch, Camera camera, int levelID) {
        this.batch = batch;
        this.camera = camera;
        this.levelID = levelID;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public void show() {
        guiFont = Assets.getInstance().getAssetManager().get("gomarice32.ttf", BitmapFont.class);
        infoFont = Assets.getInstance().getAssetManager().get("gomarice16.ttf", BitmapFont.class);

        createGUI();
        map = new Map(this, Map.Level.getLevelByID(levelID).getMapPath());
        animationEmitter = new AnimationEmitter();
        player = new ManActor(this);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        map.render(batch);
        ActorsKeeper.getInstance().renderActiveActors(batch);
        animationEmitter.render(batch);
        ScreenManager.getInstance().resetCamera();
        player.renderGUI(batch, guiFont);
        batch.end();
        stage.draw();
    }

    public void update(float dt) {
        cameraUpdate();
        ActorsKeeper.getInstance().updateActiveActors(dt);
        animationEmitter.update(dt);
    }

    public void cameraUpdate() {
        camera.position.set(player.getPosition().x, player.getPosition().y, 0);
        camera.update();
    }

    public void changeMap(int id) {
        levelID = id;
        map = new Map(this, Map.Level.getLevelByID(id).getMapPath());
    }



    public void createGUI() {
        stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());
        //if (Gdx.app.getType() == Application.ApplicationType.Android) {
            final Group arrowGroup = new Group();
            Button buttonLeft = new Button(skin.getDrawable("controls/btn_left"));
            Button buttonRight = new Button(skin.getDrawable("controls/btn_right"));
            Button buttonUp = new Button(skin.getDrawable("controls/btn_up"));
            Button buttonDown = new Button(skin.getDrawable("controls/btn_down"));
            buttonLeft.setPosition(10, 100);
            buttonUp.setPosition(120, 210);
            buttonRight.setPosition(230, 100);
            buttonDown.setPosition(120, 10);
            arrowGroup.setPosition(50, 50);
            Button[] buttons = new Button[]{buttonRight, buttonUp, buttonLeft, buttonDown};
            for (int i = 0; i < buttons.length; i++) {
                final int innerI = i;
                arrowGroup.addActor(buttons[i]);
                buttons[i].addListener(new ClickListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        player.setCurrentDirectionButtonPressed((byte)innerI);
                        return true;
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        player.setCurrentDirectionButtonPressed((byte)9);
                    }
                });
            }
            Button buttonPutBomb = new Button(skin.getDrawable("controls/btn_put"));
            buttonPutBomb.setPosition(1000, 10);
            buttonPutBomb.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    player.setPutButtonPressed();
                    return true;
                }
            });
            arrowGroup.addActor(buttonPutBomb);

            Button buttonDetonateBomb = new Button(skin.getDrawable("controls/btn_detonate"));
            buttonDetonateBomb.setPosition(1080, 90);
            buttonDetonateBomb.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    player.setDetonateButtonPressed();
                    return true;
                }
            });
            arrowGroup.addActor(buttonDetonateBomb);

            // arrowGroup.setColor(1,1,1,1f);
            stage.addActor(arrowGroup);
        //}

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

