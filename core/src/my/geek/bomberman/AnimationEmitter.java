package my.geek.bomberman;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class AnimationEmitter {
    public enum AnimationType {
        NUKE_BLAST(-170f, -90f, 0.5f, 0.5f, 0.06f, "bomb/nuke_blast"),
        BOMB_BLAST(0,0,1f,1f,0.07f, "bomb/bomb_blast"),
        ALIEN_BLAST(-100,-80,0.4f,0.4f,0.06f, "alien/alien_blast"),
        BOX_DESTRUCT(-220,-230,1f,1f,0.03f,"box/box_destruct");

        private Vector2 shift;
        private Vector2 scale;
        float frameDuration;
        String regionName;

        AnimationType (float shiftX, float shiftY, float scaleX, float scaleY, float frameDuration, String regionName) {
            this.shift = new Vector2(shiftX, shiftY);
            this.scale = new Vector2(scaleX, scaleY);
            this.frameDuration = frameDuration;
            this.regionName = regionName;
        }
    }
    public static AnimationEmitter emitter;
    private Map<AnimationType, Array<? extends TextureRegion>> keyFramesCollection;
    private SFXAnimation[] sfxAnimations;

    public AnimationEmitter() {
        keyFramesCollection = new HashMap<>();
        for (int i = 0; i < AnimationType.values().length; i++) {
            AnimationType current = AnimationType.values()[i];
            keyFramesCollection.put(current, Assets.getInstance().getAtlas().findRegions(current.regionName));
        }

        sfxAnimations = new SFXAnimation[Mgmt.SFX_QUANTITY];
        for (int i = 0; i < sfxAnimations.length; i++) {
            sfxAnimations[i] = new SFXAnimation();
        }

        emitter = this;

    }

    public void createAnimation(float x, float y, AnimationType type) {
        for (int i = 0; i < sfxAnimations.length; i++) {
            if (!sfxAnimations[i].isActive()) {
                sfxAnimations[i].activate(x + type.shift.x , y + type.shift.y, keyFramesCollection.get(type), type.frameDuration, type.scale.x  , type.scale.y);
                break;
            }
        }
    }

    public void createRandomAnimation(float x, float y) {
        AnimationType[] typesArr = AnimationType.values();
        AnimationType type = typesArr[MathUtils.random(0, typesArr.length)];
        createAnimation(x, y, type);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < sfxAnimations.length; i++) {
            if (sfxAnimations[i].isActive()) {
                sfxAnimations[i].render(batch);
            }
        }
    }

    public void update(float dt) {
        for (int i = 0; i < sfxAnimations.length; i++) {
            if (sfxAnimations[i].isActive()) {
                sfxAnimations[i].update(dt);
            }
        }
    }
}