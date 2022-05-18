package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import java.io.*;

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



    private TETile mousePos;///previous typed position.
    private int enx, eny;

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

    private void saveConf(){
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("./config.txt"));
            objectOutputStream.writeObject(finalWorldFrame);
            objectOutputStream.writeObject(rand);
            objectOutputStream.writeObject(mousePos);
            objectOutputStream.writeObject(enx);
            objectOutputStream.writeObject(eny);
            objectOutputStream.close();
        } catch (IOException e) {
            System.out.print("save Config failed.\n");
            e.printStackTrace();
        }
        System.out.print("have saved config.\n");
    }

    private void loadConf(){
        try{
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("./config.txt"));
            finalWorldFrame = (TETile[][]) objectInputStream.readObject();
            rand = (Random) objectInputStream.readObject();
            mousePos = (TETile) objectInputStream.readObject();
            enx = (int) objectInputStream.readObject();
            eny = (int) objectInputStream.readObject();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        System.out.print("have loaded config.\n");
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
//            System.out.printf("p.x:%d, p.y:%d, p.dist:%d \n", p.x, p.y, p.dist);
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
        this.enx = rooms.get(0).x;///initialize the position of entity.
        this.eny = rooms.get(0).y;
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

    private char readKeyBoard(){
        int i = 0;
        while(i < 10 && !StdDraw.hasNextKeyTyped()){
            StdDraw.pause(100);
            i++;
        }
        if(StdDraw.hasNextKeyTyped()) return StdDraw.nextKeyTyped();
        else return '#';
    }

    private void showFont(){
        ter.initialize(WIDTH, HEIGHT);
        //initFrame();
        //ter.renderFrame(finalWorldFrame);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        Font font = new Font("Monaco", Font.BOLD, 43);
        StdDraw.setFont(font);
        StdDraw.text(39,25,"CS61B: THE GAME");
        font = new Font("Monaco", Font.BOLD, 26);
        StdDraw.setFont(font);
        StdDraw.text(39, 13,"New Game (N)");
        StdDraw.text(39, 10,"Load Game (L)");
        StdDraw.text(39, 7,"Quit (Q)");
        StdDraw.show();
    }

    private void setRandSeed(long seed){
        this.rand = new Random(seed);
    }

    private long readSeed(){
        long seed = 0;
        char c = readKeyBoard();
        while(c != 'S' && c != 's'){///supposing input only consists of digits.
            seed = seed*10 + c-'0';
            c = readKeyBoard();
        }
        return seed;
    }

    private void showWorld(){
        //finalWorldFrame[enx][eny] = Tileset.TREE;
        ter.renderFrame(finalWorldFrame);
        StdDraw.setPenColor(StdDraw.WHITE);
        if(mousePos.equals(Tileset.FLOOR)){
            StdDraw.text(6, 28,"FLOOR");
        }else if(mousePos.equals(Tileset.NOTHING)){
            StdDraw.text(6, 28,"NOTHING");
        }else if(mousePos.equals(Tileset.WALL)){
            StdDraw.text(6, 28,"WALL");
        }else if(mousePos.equals(Tileset.TREE)){
            StdDraw.text(6, 28,"Entity");
        }
        StdDraw.show();
        //finalWorldFrame[enx][eny] = Tileset.FLOOR;
    }

    private void moveEW(){
        if(finalWorldFrame[enx][eny+1].equals(Tileset.FLOOR)){
            finalWorldFrame[enx][eny++] = Tileset.FLOOR;
            finalWorldFrame[enx][eny] = Tileset.TREE;
        }
    }
    private void moveEA(){
        if(finalWorldFrame[enx-1][eny].equals(Tileset.FLOOR)){
            finalWorldFrame[enx--][eny] = Tileset.FLOOR;
            finalWorldFrame[enx][eny] = Tileset.TREE;
        }
    }
    private void moveES(){
        if(finalWorldFrame[enx][eny-1].equals(Tileset.FLOOR)){
            finalWorldFrame[enx][eny--] = Tileset.FLOOR;
            finalWorldFrame[enx][eny] = Tileset.TREE;
        }
    }
    private void moveED(){
        if(finalWorldFrame[enx+1][eny].equals(Tileset.FLOOR)){
            finalWorldFrame[enx++][eny] = Tileset.FLOOR;
            finalWorldFrame[enx][eny] = Tileset.TREE;
        }
    }



    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        initFrame();
        showFont();
        mousePos = Tileset.NOTHING;
        char command;
        while(true){
            command = readKeyBoard();
            switch(command){
                case 'N':
                case 'n':
                    setRandSeed(readSeed());
                    generateWorld(finalWorldFrame);
                    finalWorldFrame[enx][eny] = Tileset.TREE;
                    showWorld();
                    break;
                case 'L':
                case 'l':
                    loadConf();
                    showWorld();
                    break;
                case 'Q':
                case 'q':
                    saveConf();
                    return;
                default:
                    StdDraw.pause(200);
            }
            if(command == 'N' || command == 'n' || command == 'L' || command == 'l') break;
            ///check Mouse pos.
        }
        ///dealing with commands
        while(true){
            command = readKeyBoard();
            System.out.print(command+"\n");
            switch(command){
                case 'W':
                case 'w':
                    moveEW();
                    showWorld();
                    break;
                case 'A':
                case 'a':
                    moveEA();
                    showWorld();
                    break;
                case 'S':
                case 's':
                    moveES();
                    showWorld();
                    break;
                case 'D':
                case 'd':
                    moveED();
                    showWorld();
                    break;
                case ':':
                    char c = readKeyBoard();
                    if(c == 'q' || c == 'Q'){
                        //save and quit
                        saveConf();
                        return;
                    }else{
                        StdDraw.pause(50);
                    }
                    break;
                default:
                    StdDraw.pause(50);
            }
            if(StdDraw.isMousePressed()){
                int x = (int)StdDraw.mouseX(), y = (int)StdDraw.mouseY();
                if(x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) mousePos = finalWorldFrame[x][y];
                //System.out.print("x:"+x+",y:"+y+"\n");
                showWorld();
            }
            StdDraw.pause(50);
        }
        //accept command: 'N', 'L', 'Q'
        //       and handling mouse presses.
        //set the initial position of some entity.
        //show the world and accept different command.
    }

    private void initFrame(){
        finalWorldFrame = new TETile[WIDTH][HEIGHT];
        //initialize finalWorldFrame
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                finalWorldFrame[x][y] = Tileset.NOTHING;
            }
        }
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

        initFrame();
        mousePos = Tileset.NOTHING;
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
                    //this.rand = new Random(seed);
                    setRandSeed(seed);
                    generateWorld(finalWorldFrame);
                    //generating the world
                    finalWorldFrame[enx][eny] = Tileset.TREE;
                    break;
                case 'L':
                case 'l':
                    loadConf();
                    break;
                case ':'://expecting 'Q' or 'q'
                    if(i+1 < input.length() && input.charAt(i+1) == 'q' || input.charAt(i+1) == 'Q'){
                        saveConf();
                        return finalWorldFrame;
                    }
                    break;
                case 'W':
                case 'w':
                    moveEW();
                    break;
                case 'A':
                case 'a':
                    moveEA();
                    break;
                case 'S':
                case 's':
                    moveES();
                    break;
                case 'D':
                case 'd':
                    moveED();
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
