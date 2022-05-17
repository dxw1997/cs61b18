package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 11111;//2873123;
    private static final Random RANDOM = new Random(SEED);


    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.MOUNTAIN;
            case 3: return Tileset.SAND;
            case 4: return Tileset.TREE;
            default: return Tileset.MOUNTAIN;
        }
    }
    public static void addHexagonRow(TETile[][] hexTiles, int c, int r, int len, TETile type){
        if(r+len >= HEIGHT || r < 0 || c < 0 || c >= WIDTH) return;
        for(int ridx = r;ridx < r+len;++ridx)
            hexTiles[ridx][c] = type;
    }

    public static void addHexagon(TETile[][] hexTiles, int r, int c, int len, TETile type){
        for(int i = 0;i < len;++i ){
            addHexagonRow(hexTiles, c-i, r-i, len+2*i, type);
        }
        for(int i = len-1, j = 0;i >= 0;--i,++j ){
            addHexagonRow(hexTiles, c-len-j, r-i, len+2*i, type);
        }
    }

    public static void addTesslateHexagon(TETile[][] hexTiles, int r, int c, int tlen, int len, TETile type){
        if(tlen == 1){
            addHexagon(hexTiles, r, c, len, randomTile());//type may need to be changed.
            return;
        }
        int rDist = len+(len-1);
        for(int i = 0;i < tlen;++i ){
            addHexagon(hexTiles, r-rDist*i, c-len*i, len, randomTile());
            if(i > 0) addHexagon(hexTiles, r+rDist*i, c-len*i, len, randomTile());
        }
        for(int i = 0;i < tlen-1;++i){
            addHexagon(hexTiles, r-rDist*(tlen-1), c-len*(tlen-1)-len*2*(i+1), len, randomTile());
            addHexagon(hexTiles, r+rDist*(tlen-1), c-len*(tlen-1)-len*2*(i+1), len, randomTile());
        }
        for(int i = 0;i < tlen-1;++i ){
            addHexagon(hexTiles, r-rDist*(tlen-1)+(i+1)*rDist, c-len*(tlen-1)-len*2*(tlen-1)-len*(i+1), len, randomTile());
            if(i < tlen-2) addHexagon(hexTiles, r+rDist*(tlen-1)-(i+1)*rDist, c-len*(tlen-1)-len*2*(tlen-1)-len*(i+1), len, randomTile());
        }
        addTesslateHexagon(hexTiles, r,c-2*len,tlen-1,len,type);
    }

    public static void main(String[] args){
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexTiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexTiles[x][y] = Tileset.NOTHING;
            }
        }
        //addHexagon(hexTiles, 10,10,3,Tileset.GRASS);
        //addHexagonCol(hexTiles, 5, 20, 6, Tileset.GRASS);
        //addTesslateHexagon(hexTiles, 25,40,3,4,Tileset.FLOWER);
        addTesslateHexagon(hexTiles, 25,46,4,3,Tileset.FLOWER);
        ter.renderFrame(hexTiles);

    }
}
