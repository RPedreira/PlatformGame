package entities;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import gamestates.Playing;
import levels.Level;
import utilz.HelpMethods;
import utilz.LoadSave;
import static utilz.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] goblinArr;
	private ArrayList<Goblin> goblins = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		goblins = level.getGoblins();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Goblin c : goblins)
			if(c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}
		if(!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawGoblins(g, xLvlOffset);
	}

	private void drawGoblins(Graphics g, int xLvlOffset) {
		for (Goblin c : goblins) 
			if(c.isActive()) {
				g.drawImage(goblinArr[c.getState()][c.getAniIndex()], (int)c.getHitbox().x - xLvlOffset - GOBLIN_DRAWOFFSET_X  + c.flipX(), (int)c.getHitbox().y - GOBLIN_DRAWOFFSET_Y, GOBLIN_SIZE * c.flipW(), GOBLIN_SIZE, null);
			}
	}
	
	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		for(Goblin c : goblins)
			if(c.isActive())
				if(attackBox.intersects(c.getHitbox())) {
					c.hurt(10);
					return;
				}
	}

	private void loadEnemyImgs() {
		goblinArr = new BufferedImage[5][8];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.GOBLIN_SPRITE);
		for (int i = 0; i < goblinArr.length; i++)
			for (int j = 0; j < goblinArr[i].length; j++)
				goblinArr[i][j] = temp.getSubimage(j * GOBLIN_SIZE_DEFAULT, i * GOBLIN_SIZE_DEFAULT, GOBLIN_SIZE_DEFAULT, GOBLIN_SIZE_DEFAULT);
	}

	public void resetAllEnemies() {
		for(Goblin c : goblins)
			c.resetEnemy();
	}
}