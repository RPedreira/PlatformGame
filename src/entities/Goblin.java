package entities;

import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import main.Game;
import utilz.HelpMethods;
import utilz.Constants.Directions;
import utilz.Constants.EnemyConstants;

public class Goblin extends Enemy {
	private int attackBoxOffsetX;
	
	public Goblin(float x, float y) {
		super(x, y, GOBLIN_SIZE, GOBLIN_SIZE, GOBLIN);
		this.xDrawOffset = EnemyConstants.GOBLIN_DRAWOFFSET_X;
		this.yDrawOffset = EnemyConstants.GOBLIN_DRAWOFFSET_Y;
		initHitbox(28, 33);
		initAttackBox();
	}
	
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int)(60 * Game.SCALE), (int)(33 * Game.SCALE));
		attackBoxOffsetX =  (int)(16 * Game.SCALE);
	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if(firstUpdate)
			firstUpdateCheck(lvlData);
		if(inAir)
			updateInAir(lvlData);
		else {
			switch(state) {
			case EnemyConstants.IDLE:
				newState(EnemyConstants.RUNNING);
				break;
			case EnemyConstants.RUNNING:
				if(canSeePlayer(lvlData, player)) {
					turnTowardsPlayer(player);
					if(isPlayerCloseForAttack(player))
						newState(EnemyConstants.ATTACK);
				}
				move(lvlData);
				break;
			case EnemyConstants.ATTACK:
				if(animationIndex == 0)
					attackChecked = false;
				if(animationIndex == 6 && !attackChecked)
					checkPlayerHit(attackBox, player);
				break;
			case EnemyConstants.HIT:
				break;
			}
		}
	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}
	
	private void updateAttackBox() {
		attackBox.x = hitbox.x - attackBoxOffsetX;
		attackBox.y = hitbox.y;
	}

	public int flipX() {
		if(walkDir == Directions.LEFT)
			return width;
		else
			return 0;
	}
	
	public int flipW() {
		if(walkDir == Directions.LEFT)
			return -1;
		else
			return 1;
	}
}