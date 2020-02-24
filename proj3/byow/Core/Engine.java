package byow.Core;

import byow.Core.InputSource.InputSource;
import byow.Core.InputSource.KeyboardInputSource;
import byow.Core.InputSource.StringInputSource;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.*;
import java.util.Random;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private StringBuilder record = new StringBuilder();
    private Player player;
    private TETile[][] world;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        player = new Player(new Position(10, 10));
        world = new TETile[80][40];
        ter.initialize(WIDTH, HEIGHT);
        // draw initial GUI
        drawStartMenu();
        // enter game loop
        InputSource inputSource = new KeyboardInputSource();

        while(true){
            if (record.length() > 0) {
                ter.renderFrame(world);
                tileInfo(new Position((int)StdDraw.mouseX(), (int)StdDraw.mouseY()), world);
            }
            if (!StdDraw.hasNextKeyTyped()) continue;
            char key = inputSource.getNextKey();
            record.append(key);
            takeAction(key, inputSource);
        }
    }
    private String load(){
        File f = new File("./sava_data.txt");
        if (f.exists()){
            try{
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                String recordString = (String)os.readObject();
                recordString = recordString.substring(recordString.indexOf("N"), recordString.length() - 1);
                return recordString;
            }catch (FileNotFoundException e){
                System.out.println("File not found");
                System.exit(0);
            }   catch (IOException e){
                System.out.println(e);
                System.exit(0);
            }   catch (ClassNotFoundException e){
                System.out.println(e);
                System.exit(0);
            }
        }
        return null;
    }

    private void save(){
        File f = new File("./sava_data.txt");
        try{
            if (!f.exists()) f.createNewFile();
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            String recordString = record.toString();
            os.writeObject(recordString);
        } catch (FileNotFoundException e){
            System.out.println("File not found");
            System.exit(0);

        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void drawSeed(String s) {
        StdDraw.clear(StdDraw.BLACK);
        // Draw instruction.
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 6 / 10, "Please type a seed, press 'S' to confirm");
        // Display seed typed.
        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, s);
        // Reset font size to TeRenderer's default size.
        font = new Font("Monaco", Font.BOLD, 16 - 2);
        StdDraw.setFont(font);
        StdDraw.show();

    }
    public void drawStartMenu(){

        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();

        StdDraw.clear();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(font);
        StdDraw.text(WIDTH / 2, HEIGHT * 9 / 10, "CS61B: Project3");

        Font smallFont = new Font("Monaca", Font.BOLD, 20);
        StdDraw.setFont(smallFont);
        StdDraw.text(WIDTH / 2,  HEIGHT * 6 / 10, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT * 5 / 10, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT * 4 / 10, "Quit (Q)");
        StdDraw.show();
    }
    public static void main(String args[]){
        /*
        Engine e = new Engine();
        e.drawStartMenu();*/
    }

    public TETile[][] createNewWorld(long seed){
        MapGenerator mg = new MapGenerator(WIDTH, HEIGHT, seed);
        return mg.getWorld();
    }

    private long readSeed(InputSource inputSource){
        long seed = 0;
        while(true){
            char k = inputSource.getNextKey();
            record.append(k);
            if (k == 'S') break;
            seed = seed * 10 + Character.getNumericValue(k);
            if (inputSource.getClass().equals(KeyboardInputSource.class))
                drawSeed(String.valueOf(seed));
        }
        return seed;
    }

    private void tileInfo(Position mousePos, TETile[][] world){
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.textLeft(1, HEIGHT - 1,
                        world[mousePos.getX()][mousePos.getY()].description());
        StdDraw.show();
    }


    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        InputSource inputSource = new StringInputSource(input);
        while(inputSource.possibleNextInput()){
            takeAction(inputSource.getNextKey(), inputSource);
        }
        return world;
    }

    public void takeAction(char key, InputSource inputSource){
        switch (key) {
            case 'N': // Create a new world
                long seed = readSeed(inputSource);
                world = createNewWorld(seed);
                player = Player.generatePlayer(world, new Random(seed));
                if (inputSource.getClass().equals(KeyboardInputSource.class))
                    ter.renderFrame(world);
                break;
            case 'W':
                player.move(Move.UP, world);
                if (inputSource.getClass().equals(KeyboardInputSource.class))
                    ter.renderFrame(world);
                break;
            case 'S':
                player.move(Move.DOWN, world);
                if (inputSource.getClass().equals(KeyboardInputSource.class))
                    ter.renderFrame(world);
                break;
            case 'A':
                player.move(Move.LEFT, world);
                if (inputSource.getClass().equals(KeyboardInputSource.class))
                    ter.renderFrame(world);
                break;
            case 'D':
                player.move(Move.RIGHT, world);
                if (inputSource.getClass().equals(KeyboardInputSource.class))
                    ter.renderFrame(world);
                break;
            case ':': // save and quit
                char k = inputSource.getNextKey();
                record.deleteCharAt(record.length() - 1);
                if (k == 'Q') {
                    record.append(k);
                    save();
                    System.exit(0);
                }
                break;
            case 'L':
                String recordString = load();
                record = new StringBuilder(recordString);
                interactWithInputString(recordString);
            default:
                record.deleteCharAt(record.length() - 1);
        }
    }
}
