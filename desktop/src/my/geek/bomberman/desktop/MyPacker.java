package my.geek.bomberman.desktop;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class MyPacker {
    public static void main (String[] args) throws Exception {
        TexturePacker.process("AtlasInput", "AtlasOutput", "bomberman_atlas");
    }
}
