package entities;

import static utilz.Constants.PlayerConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Playing;
import utilz.Constants;
import utilz.HelpMethods;
import main.Game;
import utilz.LoadSave;
import utilz.Constants.EnemyConstants;

public class Player extends Entity{
	private BufferedImage[][] animations;
	private boolean moving = false, attacking = false;
	private boolean left, right, jump;
	private int[][] lvlData;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private BufferedImage statusBarImg;
	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);
	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);
	private int healthWidth = healthBarWidth;
	private int flipX = 0;
	private int flipW = 1;
	private boolean attackChecked;
	private Playing playing;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = Game.SCALE * 1.0f;
		this.xDrawOffset = 45 * Game.SCALE;
		this.yDrawOffset = 42 * Game.SCALE;
		loadAnimations();
		initHitbox(19, 37);
		initAttackBox();
	}
	
	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y,(int)(40 * Game.SCALE), (int)(25 * Game.SCALE));
	}

	public void update() {
		updateHealthBar();
		if(currentHealth <= 0) {
			if(state != DYING) {
				state = DYING;
				animationTick = 0;
				animationIndex = 0;
				playing.setPlayerDying(true);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			}else if(animationIndex == GetSpriteAmount(DYING) - 1 && animationTick >= Constants.ANIMATION_SPEED - 1) {
				playing.setGameOver(true);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			}else
				updateAnimationTick();
			return;
		}
		updateAttackBox();
		updatePos();
		if(attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}
	
	private void checkAttack() {
		if(attackChecked || animationIndex != 1)
			return ;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);
		playing.getGame().getAudioPlayer().playAttackSound();
	}

	private void updateAttackBox() {
		if(right)
			attackBox.x = hitbox.x + hitbox.width + (int)(Game.SCALE * 10);
		else if(left)
			attackBox.x = hitbox.x - hitbox.width - (int)(Game.SCALE * 10);
		attackBox.y = hitbox.y + (Game.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int)((currentHealth / (float)maxHealth) * healthBarWidth);
	}

	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[state][animationIndex], (int)(hitbox.x - xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y - yDrawOffset), width * flipW, height, null);
		drawUI(g);
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}

	private void updateAnimationTick() {
		animationTick++;
		if(animationTick >= Constants.ANIMATION_SPEED) {
			animationTick = 0;
			animationIndex++;
			if(animationIndex >= GetSpriteAmount(state)) {
				animationIndex = 0;
				attacking = false;
				attackChecked = false;
			}
		}
	}
	
	private void setAnimation() {
		int startAnimation = state;
		if(moving)
			state = RUNNING;
		else
			state = IDLE;
		if(inAir) {
			if(airSpeed < 0)
				state = JUMP;
			else
				state = FALLING;
		}
		if(attacking)
			state = ATTACK_1;
		if(startAnimation != state)
			resetAnimationTick();
	}
	
	private void resetAnimationTick() {
		animationTick = 0;
		animationIndex = 0;
	}

	private void updatePos() {
		moving = false;
		
		if(jump)
			jump();
		if(!inAir)
			if((!left && !right) || (left && right))
				return;
		
		float xSpeed = 0;
		
		if(left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if(right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		
		if(!inAir)
			if(!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
				inAir = true;
		if(inAir) {
			if(HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += Constants.GRAVITY;
				updateXPos(xSpeed);
			}else {
				hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}
			// Check if fall in abbys
			if((int)((hitbox.y + hitbox.height) / Game.TILES_SIZE) + 1 == Game.TILES_IN_HEIGHT)
				changeHealth(-maxHealth);
		}else
			updateXPos(xSpeed);
		
		moving = true;
	}
	
	private void jump() {
		if(inAir)
			return;
		playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpeed) {
		if(HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else
			hitbox.x = HelpMethods.GetEntityXPosNextToWall(hitbox, xSpeed);
	}
	
	public void changeHealth(int value) {
		currentHealth += value;
		if(currentHealth <= 0)
			currentHealth = 0;
		else if(currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][10];
		for(int i = 0; i < animations.length; i++)
			for(int j = 0; j < animations[i].length; j++)
				animations[i][j] = img.getSubimage(j*120, i*80, 120, 80);
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
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

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}
	
	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		if(!HelpMethods.IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
}
