package levels;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Goblin;
import main.Game;
import utilz.HelpMethods;

public class Level {
	private BufferedImage img;
	private int[][] levelData;
	private ArrayList<Goblin> goblins;
	private int lvlTilesWide;
	private int maxTilesOffset;
	private int maxLvlOffsetX;
	private Point playerSpawn;
	
	public Level(BufferedImage img) {
		this.img = img;
		createLevelData();
		createEnemies();
		calcLvlOffsets();
		calcPlayerSpawn();
	}
	
	private void calcPlayerSpawn() {
		playerSpawn = HelpMethods.GetPlayerSpawn(img);
	}

	private void calcLvlOffsets() {
		lvlTilesWide = img.getWidth();
		maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
		maxLvlOffsetX = Game.TILES_SIZE * maxTilesOffset;
	}

	private void createEnemies() {
		goblins = HelpMethods.GetGoblins(img);
	}

	private void createLevelData() {
		levelData = HelpMethods.GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return levelData[y][x];
	}
	
	public int[][] getLevelData(){
		return levelData;
	}
	
	public int getLvlOffset() {
		return maxLvlOffsetX;
	}
	
	public ArrayList<Goblin> getGoblins(){
		return goblins;
	}
	
	public Point getPlayerSpawn() {
		return playerSpawn;
	}
}
