package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import edu.princeton.cs.introcs.StdDraw;

public class Game {
    private class DrawableObj{
        public int x, y;//bottom-left point, inner
        public int w, h;//width and height, inner
        public DrawableObj(int x_, int y_, int h_, int w_){
            this.x = x_;
            this.y = y_;
            this.h = h_;
            this.w = w_;
        }

        public void draw(TETile[][] finalWorldFrame){
            for(int i = x-1;i <= x+w;++i ){
                for(int j = y-1;j <= y+h;++j ){
                    if(i == x-1 || i==x+w || j==y-1 || j==y+h){
                        if(finalWorldFrame[i][j] == Tileset.NOTHING)
                            finalWorldFrame[i][j] = Tileset.WALL;
                    }else{
                        finalWorldFrame[i][j] = Tileset.FLOOR;
                    }
                }//for
            }//for
        }
    }

    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private Random rand;
    private TETile[][] finalWorldFrame;
    private boolean[][] vis;

    /*TETiles types*/
//    private TETile wall;
//    private TETile floor;
//    private TETile door;
//    private TETile nothing;

    private class compPos implements Comparator<DrawableObj>{
        @Override
        public int compare(DrawableObj o1, DrawableObj o2){
            if(o1.x == o2.x) return o1.y-o2.y;
            return o1.x - o2.x;
        }
    }

    private class Pos{
        public int x, y;
        public int dist;
    }



    private void dfs(DrawableObj o, int x, int y, Pos p){
        if(x < 0 || x >= WIDTH || y < 0 || y >= HEIGHT || finalWorldFrame[x][y] != Tileset.FLOOR || vis[x][y]) return;
        if(x >= o.x && x < o.x+o.w && y >= o.y && y < o.y+o.h){
            p.dist = 0;
            return;
        }
        if(p.dist == 0) return;
        int ndist = Math.min(Math.abs(x-o.x), Math.abs(x-o.x-o.w))+Math.min(Math.abs(y-o.y),Math.abs(y-o.y-o.h));
        if(ndist < p.dist){
            p.dist = ndist;
            p.x = x;
            p.y = y;
        }
        vis[x][y] = true;
        //System.out.printf("dfs-x:%d, dfs-y:%d\n", x, y);
        dfs(o, x+1, y, p);
        dfs(o, x-1, y, p);
        dfs(o, x, y+1, p);
        dfs(o, x, y-1, p);
    }

    private void connect(ArrayList<DrawableObj> rooms, ArrayList<DrawableObj> hallways){
        ArrayList<DrawableObj> arr = new ArrayList<>();
        for(int i = 0;i < rooms.size();++i ) arr.add(rooms.get(i));
        for(int i = 0;i < hallways.size();++i ) arr.add(hallways.get(i));
        Collections.sort(arr, new compPos());
        //for(int i = 0;i < arr.size();++i ){
        //    System.out.printf("x: %d, y: %d\n", arr.get(i).x, arr.get(i).y);
        //}

        for(int i = 1;i < arr.size();++i ){///i < arr.size()
            Pos p = new Pos();
            p.x = p.y = -1;
            p.dist = 10000;
            vis = new boolean[WIDTH][HEIGHT];
            dfs(arr.get(i-1), arr.get(i).x, arr.get(i).y, p);
            System.out.printf("p.x:%d, p.y:%d, p.dist:%d \n", p.x, p.y, p.dist);
            if(p.dist != 0 && p.dist != 10000){
                ///draw the hallway
                int nx, ny;
                DrawableObj od = arr.get(i-1);
                if(p.x > od.x-1 && p.x < od.x+od.w) nx = p.x;
                else if(p.x <= od.x-1) nx = od.x;
                else nx = od.x+od.w-1;
                ///make two points clear
                if(p.y > od.y-1 && p.y < od.y+od.h) ny = p.y;
                else if(p.y <= od.y-1) ny = od.y;
                else ny = od.y+od.h-1;

                ///connect these two points.
                if(nx == p.x){
                    DrawableObj tobj = new DrawableObj(nx, Math.min(ny, p.y), Math.abs(ny-p.y)+1, 1);
                    tobj.draw(finalWorldFrame);
                }
                else if(ny == p.y){
                    DrawableObj tobj = new DrawableObj( Math.min(p.x, nx), ny, 1, Math.abs(nx-p.x)+1);
                    tobj.draw(finalWorldFrame);
                }
                else{
                    int tx = Math.min(p.x, nx), ty = Math.min(p.y, ny);
                    DrawableObj tobj1 = null, tobj2 = null;
                    if(tx == nx && ty == ny){
                        tobj1 = new DrawableObj(nx, ny, 1, p.x-nx+1);
                        tobj2 = new DrawableObj(p.x, ny, p.y-ny+1, 1);
                    } else if(tx == p.x && ty == p.y){
                        tobj1 = new DrawableObj(p.x, p.y, 1, nx-p.x+1);
                        tobj2 = new DrawableObj(nx, p.y, ny-p.y+1, 1);
                    } else{
                        if(p.x == tx){
                            tobj1 = new DrawableObj(tx, ty, p.y-ty+1, 1);
                            tobj2 = new DrawableObj(tx, ty, 1, nx-tx+1);
                        }
                        else{
                            tobj1 = new DrawableObj(tx, ty, ny-ty+1, 1);
                            tobj2 = new DrawableObj(tx, ty, 1, p.x-tx+1);
                        }
                    }
                    tobj1.draw(finalWorldFrame);
                    tobj2.draw(finalWorldFrame);
                }///else
            }///if
        }///for
    }

    private void generateWorld(TETile[][] finalWorldFrame){
        int roomNumber = RandomUtils.uniform(this.rand, 6,9);///for testing
        int hallwayNum = RandomUtils.uniform(this.rand, 8,12);///for testing
        ArrayList<DrawableObj> rooms = new ArrayList<>();
        ArrayList<DrawableObj> hallways = new ArrayList<>();
        for(int i = 0;i < roomNumber;++i ){
            int w = RandomUtils.uniform(this.rand, 2, 8);
            int h = RandomUtils.uniform(this.rand, 2, 8);
            int x = RandomUtils.uniform(this.rand, 2, WIDTH-1-8);
            int y = RandomUtils.uniform(this.rand, 2, HEIGHT-1-8);
            rooms.add(new DrawableObj(x,y,h,w));
        }
        for(int i = 0;i < hallwayNum;++i ){
            int w,h;
            if(RandomUtils.uniform(this.rand,10) % 2 == 1){
                w = 1;
                h = RandomUtils.uniform(this.rand, 1, 8);
            }else{
                h = 1;
                w = RandomUtils.uniform(this.rand, 1, 8);
            }
            int x = RandomUtils.uniform(this.rand, 1, WIDTH-1-8);
            int y = RandomUtils.uniform(this.rand, 1, HEIGHT-1-8);
            hallways.add(new DrawableObj(x,y,h,w));
            if(RandomUtils.uniform(this.rand, 10)%2 == 1){
                hallways.add(new DrawableObj(x,y,w,h));
            }
        }
        for(int i = 0;i < roomNumber;++i ) rooms.get(i).draw(finalWorldFrame);
        for(int i = 0;i < hallways.size();++i ) hallways.get(i).draw(finalWorldFrame);
        ///TODO:, connect them.

        connect(rooms, hallways);
    }


    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // TODO: Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        //ter.initialize(WIDTH, HEIGHT);

        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        //initialize finalWorldFrame
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }

        //parsing commands
        for(int i = 0;i < input.length();++i ){
            switch(input.charAt(i)){
                case 'N':
                case 'n':
                    long seed = 0;
                    for(;i < input.length() && input.charAt(i) != 's' && input.charAt(i) != 'S';i++ ){
                        seed = seed * 10 + input.charAt(i)-'0';
                    }
                    ///there could exist an invalid input string.
                    ///if(i == input.length() || input.charAt(i) != ('s'  'S') )
                    this.rand = new Random(seed);
                    generateWorld(finalWorldFrame);
                    //generating the world
                    break;
                case 'L':
                case 'l':
                    break;
                case ':'://expecting 'Q' or 'q'
                    break;
                default:
                    //do nothing
            }
        }
        //ter.renderFrame(finalWorldFrame);

        return finalWorldFrame;
    }

/*    private long parseSeed(String input){
        return Long.parseLong(input);
    }*/
}
