package Arkanoid;

import java.awt.*;

public class MapGenerator {
    public int[][] allBricks;
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col, int[][] sourceMap) {
        allBricks = new int[row][col];
        for (int i = 0; i < allBricks.length; i++) {
            for (int j = 0; j < allBricks[0].length; j++) {
                allBricks[i][j] = sourceMap[i][j];
            }
        }

        brickWidth = 900 / col;
        brickHeight = 280 / row;
    }

    public Color colorDecision(int brickCode) {
        Color brickColor;

        switch (brickCode) {
            case 1:
                brickColor = new Color(255, 255, 255);
                break;
            case 2:
                brickColor = new Color(238,130,238);
                break;
            case 3:
                brickColor = new Color(0,0,255);
                break;
            case 4:
                brickColor = new Color(0,255,0);
                break;
            case 5:
                brickColor = new Color(255,165,0);
                break;
            case 999:
                brickColor = new Color(125,125,125);
                break;
            default:
                brickColor = new Color(0, 0, 0);
        }
        return brickColor;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < allBricks.length; i++) {
            for (int j = 0; j < allBricks[0].length; j++) {
                if (allBricks[i][j] > 0) {
                    //g.setColor(Color.white);
                    g.setColor(colorDecision(allBricks[i][j]));
                    g.fillRect(j * brickWidth + 60, i * brickHeight + 60, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 60, i * brickHeight + 60, brickWidth, brickHeight);
                }
            }
        }
    }

    public int hitBrick(int row, int col) {
        allBricks[row][col] = (allBricks[row][col]!=999) ? allBricks[row][col]-1 : allBricks[row][col];
        return (allBricks[row][col]==999) ? 0 : -1;
    }
}
