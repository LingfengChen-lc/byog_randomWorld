package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private String currentEncouragement;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        int seed = Integer.parseInt(args[0]);
        MemoryGame game = new MemoryGame(60, 60, seed);
        game.startGame();
    }

    /**
     * Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
     * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
     * @param width width of the canvas
     * @param height height of the canvas
     * @param seed random seed
     */
    public MemoryGame(int width, int height, int seed) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setPenColor(new Color(13, 123, 222));
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        rand = new Random(seed);
    }

    /**
     * generates a random string with length n
     * @param n length
     * @return the generated string
     */
    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            sb.append(CHARACTERS[rand.nextInt(CHARACTERS.length)]);
            n--;
        }
        return sb.toString();
    }

    /**
     * draws one frame with argument string
     * @param s string to draw in the center of the screen
     */
    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        StdDraw.setPenColor(new Color(13, 123, 222));
        StdDraw.text(width /  2.0, height / 2.0, s);
        if (!gameOver) {
            StdDraw.setPenColor(new Color(255, 255, 255));
            StdDraw.text(5, height - 2, "Round: " + round);
            if (playerTurn) {
                StdDraw.text(width / 2.0, height - 2, "Type!");
            } else {
                StdDraw.text(width / 2.0, height - 2, "Watch!");
            }
            StdDraw.text(width - 10, height - 2, currentEncouragement);
            StdDraw.line(0, height - 5, width, height - 5);
        }
        StdDraw.show();
    }

    /**
     * shows one character of the argument letters at a time on the screen
     * @param letters the letters wish to show on the screen
     */
    public void flashSequence(String letters) {
        playerTurn = false;
        for (char c : letters.toCharArray()) {
            drawFrame(String.valueOf(c));
            StdDraw.pause(750);
            drawFrame(" ");
            StdDraw.pause(750);
        }
        playerTurn = true;
        drawFrame(" ");
    }

    /**
     * solicit n key inputs from user, wait until user input all n inputs
     * @param n number of inputs ask from user
     * @return the final string typed by user
     */
    public String solicitNCharsInput(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            if (StdDraw.hasNextKeyTyped()) {
                n--;
                sb.append(StdDraw.nextKeyTyped());
                drawFrame(sb.toString());
            }
        }
        return sb.toString();
    }

    /**
     * engine to start the game
     */
    public void startGame() {
        round = 1;
        while (!gameOver) {
            currentEncouragement = ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)];
            drawFrame("Round " + round);
            StdDraw.pause(1500);
            String s = generateRandomString(round);
            flashSequence(s);
            String input = solicitNCharsInput(round);
            if (!s.equals(input)) {
                gameOver = true;
                drawFrame("GAME OVER. You made it to round " + round);
                StdDraw.pause(1000);
            } else {
                drawFrame("Well Down!");
                StdDraw.pause(1000);
                round++;
            }
        }
        System.exit(0);
    }
}
