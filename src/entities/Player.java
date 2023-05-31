package entities;

import static utilz.Constants.PlayerConstants.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity{
	private BufferedImage[][] animations;
	private int animationTick, animationIndex, animationSpeed = 15;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false;
	private boolean left, up, right, down;
	private float playerSpeed = 2.0f;
	
	public Player(float x, float y) {
		super(x, y);
		loadAnimations();
	}
	
	public void update() {
		updateAnimationTick();
		setAnimation();
		updatePos();
	}
	
	public void render(Graphics g) {
		g.drawImage(animations[playerAction][animationIndex], (int)x, (int)y, 2*Game.TILES_SIZE, 2*Game.TILES_SIZE, null);
	}
	
	private void updateAnimationTick() {
		animationTick++;
		if(animationTick >= animationSpeed) {
			animationTick = 0;
			animationIndex++;
			if(animationIndex >= GetSpriteAmount(playerAction)) {
				animationIndex = 0;
				attacking = false;
			}
		}
	}
	
	private void setAnimation() {
		int startAnimation = playerAction;
		if(moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		if(attacking)
			playerAction = ATTACK_1;
		if(startAnimation != playerAction)
			resetAnimationTick();
	}
	
	private void resetAnimationTick() {
		animationTick = 0;
		animationIndex = 0;
	}

	private void updatePos() {
		moving = false;
		if(left && !right) {
			x -= playerSpeed;
			moving = true;
		}else if(right && !left) {
			x += playerSpeed;
			moving = true;
		}
		if(up && !down) {
			y -= playerSpeed;
			moving = true;
		}else if(down && !up) {
			y += playerSpeed;
			moving = true;
		}
	}
	
	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[8][10];
		for(int i = 0; i < animations.length; i++)
			for(int j = 0; j < animations[i].length; j++)
				animations[i][j] = img.getSubimage(j*120, i*80, 120, 80);
	}
	
	public void resetDirBooleans() {
		left = false;
		up = false;
		right = false;
		down = false;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	
	
	
}