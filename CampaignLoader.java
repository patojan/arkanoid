package Arkanoid;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CampaignLoader {
    private int numberOfLevels;
    private int seconds;
    private int paddleWidth;
    private double xDirectionStart;
    private double yDirectionStart;
    private int rows;
    private int columns;
    private int activeBricks;
    private int[][] map;

    public CampaignLoader() throws IOException{
        BufferedReader campaignFile = null;
        try {
            campaignFile = new BufferedReader(new FileReader("src/campaign.txt"));
            String inputString;
            inputString = campaignFile.readLine();
            numberOfLevels = Integer.parseInt(inputString);
        }
        catch (IOException e){
            System.out.println("Files are corrupted.");
            System.exit(1);
        }
        finally {
            campaignFile.close();
        }
    }

    public void countActiveBricks(){
        int totalPlaces = rows*columns;
        int countedActiveBricks = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                countedActiveBricks += (map[r][c]!=999) ? map[r][c] : 0;
            }
        }
        activeBricks = countedActiveBricks;
    }

    private int[][] loadMap(int rows,int columns, BufferedReader levelFile) throws IOException, NumberFormatException{
        int[][] map = new int[rows][columns];
        try {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    map[r][c] = Integer.parseInt(levelFile.readLine());
                }
            }
        }
        catch (NumberFormatException e){
            System.out.println("Files are corrupted.");
            System.exit(1);
        }
        return map;
    }

    public boolean loadLevel(int levelNumber){
        BufferedReader levelFile = null;
        String levelFileName = "src/level"+levelNumber+".txt";

        try {
            levelFile = new BufferedReader(new FileReader(levelFileName));
            seconds = Integer.parseInt(levelFile.readLine());
            paddleWidth = Integer.parseInt(levelFile.readLine());
            xDirectionStart = Double.parseDouble(levelFile.readLine());
            yDirectionStart = Double.parseDouble(levelFile.readLine());
            rows = Integer.parseInt(levelFile.readLine());
            columns = Integer.parseInt(levelFile.readLine());
            map = new int[rows][columns];
            map = loadMap(rows,columns,levelFile);
            countActiveBricks();
        }
        catch (IOException e){
            System.out.println("Files are corrupted.");
            System.exit(1);
        }

        return true;
    }

    public int getNumberOfLevels(){
        return numberOfLevels;
    }

    public int getSeconds(){
        return seconds;
    }

    public int getPaddleWidth(){
        return paddleWidth;
    }

    public double getXDirectionStart(){
        return xDirectionStart;
    }

    public double getYDirectionStart(){
        return yDirectionStart;
    }

    public int getRows(){
        return rows;
    }

    public int getColumns(){
        return columns;
    }

    public int[][] getMap(){
        return map;
    }

    public int getActiveBricks(){
        return activeBricks;
    }

}
