package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.Game;
import utilz.LoadSave;

public class LevelManager {
	private Game game;
	private BufferedImage[] levelSprite;
	private Level levelOne;
	
	public LevelManager(Game game) {
		this.game = game;
		importOutsideSprites();
		levelOne = new Level(LoadSave.GetLevelData());
	}
	
	private void importOutsideSprites() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[72];
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 8; j++) {
				int index = i*8 + j;
				levelSprite[index] = img.getSubimage(j*32, i*32, 32, 32);
			}
	}

	public void draw(Graphics g) {
		for(int i = 0; i < Game.TILES_IN_HEIGHT; i++)
			for(int j = 0; j < Game.TILES_IN_WIDTH; j++) {
				int index = levelOne.getSpriteIndex(j, i);
				g.drawImage(levelSprite[index], j*Game.TILES_SIZE, i*Game.TILES_SIZE, Game.TILES_SIZE, Game.TILES_SIZE, null);
			}
	}
	
	public void update() {
		
	}
}