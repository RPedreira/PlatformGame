package entities;

import utilz.Constants.EnemyConstants;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import main.Game;
import utilz.HelpMethods;
import utilz.Constants;
import utilz.Constants.Directions;
import utilz.Constants.Directions.*;
import utilz.Constants.EnemyConstants.*;

public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected float walkSpeed = 0.5f * Game.SCALE;
	protected int walkDir = Directions.LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected boolean active = true;
	protected boolean attackChecked;
	
	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		maxHealth = EnemyConstants.GetMaxHealth(enemyType);
		currentHealth = maxHealth;
		walkSpeed = Game.SCALE * 0.35f;
	}
	
	protected void firstUpdateCheck(int[][] lvlData) {
		if(!HelpMethods.IsEntityOnFloor(hitbox, lvlData)) 
			inAir = true;
		firstUpdate = false;
	}
	
	protected void updateInAir(int[][] lvlData) {
		if(HelpMethods.CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += Constants.GRAVITY;
		}else {
			inAir = false;
			hitbox.y = HelpMethods.GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int)(hitbox.y / Game.TILES_SIZE);
		}
	}
	
	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		if(walkDir == Directions.LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;
		if(HelpMethods.CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if(HelpMethods.IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}
		changeWalkDir();
	}
	
	protected void turnTowardsPlayer(Player player) {
		if(player.hitbox.x > hitbox.x)
			walkDir = Directions.RIGHT;
		else
			walkDir = Directions.LEFT;
	}
	
	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int)(player.getHitbox().y / Game.TILES_SIZE);
		if(playerTileY == tileY)
			if(isPlayerInRange(player))
				if(HelpMethods.IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
		return false;
	}

	protected boolean isPlayerInRange(Player player) {
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance * 5;
	}
	
	protected boolean isPlayerCloseForAttack(Player player) {
		int absValue = (int)Math.abs(player.hitbox.x - hitbox.x);
		return absValue <= attackDistance;
	}

	protected void newState(int enemyState) {
		this.state = enemyState;
		animationTick = 0;
		animationIndex = 0;
	}
	
	public void hurt(int damage) {
		currentHealth -= damage;
		if(currentHealth <= 0)
			newState(EnemyConstants.DEAD);
		else
			newState(EnemyConstants.HIT);
	}
	

	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if(attackBox.intersects(player.hitbox))
			player.changeHealth(-EnemyConstants.GetEnemyDmg(enemyType));
		attackChecked = true;
	}

	protected void updateAnimationTick() {
		animationTick++;
		if (animationTick >= Constants.ANIMATION_SPEED) {
			animationTick = 0;
			animationIndex++;
			if (animationIndex >= EnemyConstants.GetSpriteAmount(enemyType, state)) {
				animationIndex = 0;
				switch(state) {
				case EnemyConstants.ATTACK, EnemyConstants.HIT -> state = EnemyConstants.IDLE;
				case EnemyConstants.DEAD -> active = false;
				}
			}
		}
	}

	protected void changeWalkDir() {
		if(walkDir == Directions.LEFT)
			walkDir = Directions.RIGHT;
		else
			walkDir = Directions.LEFT;
	}
	
	public void resetEnemy() {
		hitbox.x = x + EnemyConstants.GOBLIN_DRAWOFFSET_X;
		hitbox.y = y + EnemyConstants.GOBLIN_DRAWOFFSET_Y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(EnemyConstants.IDLE);
		active = true;
		airSpeed = 0;
	}

	public boolean isActive() {
		return active;
	}
}