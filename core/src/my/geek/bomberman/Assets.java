package my.geek.bomberman;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;


public class Assets {
    private static Assets ourInstance = new Assets();

    public static Assets getInstance() {
        return ourInstance;
    }

    private AssetManager assetManager;
    private TextureAtlas atlas;

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    public void makeLinks() {
        atlas = assetManager.get("atlas/bomberman_atlas.atlas", TextureAtlas.class);
    }

    private Assets() {
        this.assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type) {
        switch (type) {
            case MENU:
                assetManager.load("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                createStdFont(32);
                createStdFont(96);
                assetManager.finishLoading();
                atlas = assetManager.get("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                break;
            case GAME:
                assetManager.load("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                createStdFont(32);
                createStdFont(16);
                assetManager.finishLoading();
                atlas = assetManager.get("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                break;
            case GAME_OVER:
                assetManager.load("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                createStdFont(96);
                assetManager.finishLoading();
                atlas = assetManager.get("atlas/bomberman_atlas.atlas", TextureAtlas.class);
                break;

        }
    }

    public void createStdFont(int size) {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "gomarice.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.borderWidth = 1;
        fontParameter.fontParameters.borderColor = Color.BLACK;
        fontParameter.fontParameters.shadowOffsetX = 3;
        fontParameter.fontParameters.shadowOffsetY = 3;
        fontParameter.fontParameters.shadowColor = Color.BLACK;
        assetManager.load("gomarice" + size + ".ttf", BitmapFont.class, fontParameter);
    }

    public void clear() {
        assetManager.clear();
    }
}
