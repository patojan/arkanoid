package Arkanoid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    //VARIABLES AND CONSTANTS
    //Gameplay
    private boolean play = true;
    private int time = 0;
    private int remainingSeconds;
    private int numberOfLevels;
    private int currentLevel = 1;
    private boolean toEnd = false;

    //Map
    private int brickRows;
    private int brickColumns;
    private int totalBricks;
    private int[][] sourceMap;

    private Timer timer;
    private int delay = 20; //20

    //Paddle
    private int paddleWidth;
    private final int PADDLE_HEIGHT = 10;
    private final int PLAYER_X_START = 500;
    private int playerX = PLAYER_X_START;
    private final int PADDLE_SPEED = 4;
    private int paddleMove = 0;

    //Ball
    private final int BALL_SIZE = 25;
    //position
    private final double BALL_POS_X_START = 500;
    private final double BALL_POS_Y_START = 600;
    private double ballposX;
    private double ballposY;
    //direction
    private double ballXDirStart;
    private double ballYDirStart;
    private double ballXdir = ballXDirStart;
    private double ballYdir = ballYDirStart;

    private Arkanoid.MapGenerator map;
    private Arkanoid.CampaignLoader loader;

    //METHODS

    public void createLevel(){
        //parameters from constants
        ballposX =  BALL_POS_X_START;
        ballposY = BALL_POS_Y_START;
        playerX = PLAYER_X_START;

        //loaded parameters
        loader.loadLevel(currentLevel);
        remainingSeconds = loader.getSeconds();
        paddleWidth = loader.getPaddleWidth();
        ballXDirStart = loader.getXDirectionStart();
        ballXdir = ballXDirStart;
        ballYDirStart = loader.getYDirectionStart();
        ballYdir = ballYDirStart;
        brickRows = loader.getRows();
        brickColumns = loader.getColumns();
        totalBricks = loader.getActiveBricks();
        sourceMap = loader.getMap();
    }

    public boolean increaseLevelNumber(){
        if (currentLevel<numberOfLevels){
            currentLevel++;
            return true;
        }
        else return false;
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.black);
        g.fillRect(0, 0, 1024, 768);

        //draw map
        map.draw((Graphics2D) g);

        //remaining bricks
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString("Remaining: "+totalBricks, 835,30);

        //remaining time
        g.drawString("Time: "+remainingSeconds, 30,30);

        //current level
        g.drawString("Level: "+currentLevel, 470,30);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 700, paddleWidth, PADDLE_HEIGHT);
        playerX += paddleMove;
        if (playerX < 903) {

        } else {
            playerX = 903;
        }

        if (playerX > 5) {

        } else {
            playerX = 5;
        }

        //ball
        if (play) {
            g.setColor(Color.yellow);
            g.fillOval((int) ballposX, (int) ballposY, BALL_SIZE, BALL_SIZE);
        }

        if (totalBricks<=0||totalBricks==999){
            onWin(g);
            timer.stop();
        }

        if (ballposY>760){
            onGameOver(g);
        }

        if (remainingSeconds==0){
            onTimeOver(g);
        }

        g.dispose();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveRight();
        }
        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,false);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            moveLeft();
        }

        //Restart of game
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!toEnd) restartGame();
            else System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            //paddle bounce
            if (new Rectangle((int)ballposX, (int)ballposY, BALL_SIZE, BALL_SIZE).intersects(new Rectangle(playerX, 700, paddleWidth, PADDLE_HEIGHT))) {
                ballYdir = -ballYdir;
                ballXdir = paddleBounce();
            }

            A: for (int i = 0; i < map.allBricks.length; i++) {
                for (int j = 0; j < map.allBricks[0].length; j++) {
                    if (map.allBricks[i][j] > 0) {
                        int brickX = j * map.brickWidth + 60;
                        int brickY = i * map.brickHeight + 60;
                        int brickWidth = map.brickWidth+1;
                        int brickHeight = map.brickHeight;


                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle((int)ballposX, (int)ballposY, BALL_SIZE, BALL_SIZE);

                        if (ballRect.intersects(rect)) {
                            totalBricks = totalBricks + map.hitBrick(i, j);
                            if ((int)ballposX + 19 <= rect.x || (int)ballposX + 1 >= rect.x + rect.width) { //19
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }
        }

        ballposX += ballXdir;
        ballposY += ballYdir;
        //left border bounce
        if (ballposX < 1) {
            ballXdir = -ballXdir;
        }
        //top border bounce
        if (ballposY < 1) {
            ballYdir = -ballYdir;
        }
        //right border bounce
        if (ballposX > 1008 - BALL_SIZE) {
            ballXdir = -ballXdir;
        }
        repaint();
        time = time + delay;
        if (time>=1000 && play) {
            time = 0;
            remainingSeconds-=1;
        }

}


    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            stopMove();
            if (playerX < 903) {

            } else {
                playerX = 903;
            }
        }
        KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0,false);
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            stopMove();
            if (playerX > 5) {

            } else {
                playerX = 5;
            }
        }
    }

    public void moveRight() {
        if (play) {
            paddleMove = PADDLE_SPEED;
        }
    }

    public void moveLeft() {
        if (play){
            paddleMove = -PADDLE_SPEED;
        }
    }

    public void stopMove(){
        paddleMove = 0;
    }

    public double paddleBounce(){
        double hitPoint;
        hitPoint = ballposX-playerX+(BALL_SIZE/2)-(paddleWidth /2);
        if (hitPoint<0){
            ballXdir = ballXDirStart *(Math.abs(hitPoint)/(paddleWidth /2));
        }
        if (hitPoint>=0){
            ballXdir = (-ballXDirStart)*(Math.abs(hitPoint)/(paddleWidth /2));
        }
        return ballXdir;
    }

    public void onGameOver(Graphics g){
        play = false;
        ballXdir = 0;
        ballYdir = 0;

        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD,30));
        g.drawString("Game over.", 435,350);

        g.setFont(new Font("serif", Font.BOLD,20));
        g.drawString("Press enter to restart.", 415,400);
    }

    public void onTimeOver(Graphics g){
        play = false;
        ballXdir = 0;
        ballYdir = 0;

        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD,30));
        g.drawString("Time over.", 435,350);

        g.setFont(new Font("serif", Font.BOLD,20));
        g.drawString("Press enter to restart.", 415,400);
    }

    private void onWin(Graphics g) {
        play = false;
        ballXdir = 0;
        ballYdir = 0;
        if (increaseLevelNumber()) {
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won this round.", 385, 350);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press enter to continue.", 410, 400);

        }
        else {
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You won the campaign.", 390, 350);
            toEnd = true;
        }
    }

    public void restartGame() {
        if (!play){
            play = true;
            createLevel();
            map = new Arkanoid.MapGenerator(brickRows, brickColumns, sourceMap);
            repaint();
            timer.start();
        }
    }

    public Gameplay(int jumpTo) throws IOException{
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        loader = new Arkanoid.CampaignLoader();
        numberOfLevels = loader.getNumberOfLevels();
        currentLevel = (jumpTo!=0) ? jumpTo : currentLevel;
        createLevel();
        map = new Arkanoid.MapGenerator(brickRows, brickColumns, sourceMap);

        timer = new Timer(delay, this);
        timer.start();

    }
}
