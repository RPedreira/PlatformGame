package utilz;

import main.Game;

public class Constants {
	
	public static final float GRAVITY = 0.04f * Game.SCALE;
	public static final int ANIMATION_SPEED = 25;
	
	public static class EnemyConstants {
		public static final int GOBLIN = 100;
		public static final int BOSS = 101;
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		
		public static final int GOBLIN_SIZE_DEFAULT = 150;
		public static final int GOBLIN_SIZE = (int)(GOBLIN_SIZE_DEFAULT * Game.SCALE);
		public static final int GOBLIN_DRAWOFFSET_X = (int)(61 * Game.SCALE);
		public static final int GOBLIN_DRAWOFFSET_Y = (int)(70 * Game.SCALE);
		public static final int SKELETON_SIZE_DEFAULT = 79;
		public static final int SKELETON_SIZE = (int)(SKELETON_SIZE_DEFAULT * Game.SCALE);
		
		public static int GetSpriteAmount(int enemy_type, int enemy_state) {
			switch(enemy_type) {
			case GOBLIN:
				switch(enemy_state) {
				case ATTACK:
				case RUNNING:
					return 8;
				default:
					return 4;
				}
			case BOSS:
				switch(enemy_state) {
				case ATTACK:
					return 8;
				default:
					return 4;
				}
			}
			return 0;
		}
		
		public static int GetMaxHealth(int enemy_type) {
			switch(enemy_type) {
			case GOBLIN:
				return 30;
			default:
				return 1;
			}
		}
		
		public static int GetEnemyDmg(int enemy_type) {
			switch(enemy_type) {
			case GOBLIN:
				return 20;
			default:
				return 0;
			}
		}
	}
	
	public static class UI {
		public static class Buttons{
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int)(B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int)(B_HEIGHT_DEFAULT * Game.SCALE);
		}
		public static class PauseButtons{
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int)(SOUND_SIZE_DEFAULT * Game.SCALE);
		}
		public static class UrmButtons{
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int)(URM_DEFAULT_SIZE * Game.SCALE);
		}
		public static class VolumeButtons{
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;
			public static final int VOLUME_WIDTH = (int)(VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int)(VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int)(SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int	RIGHT = 2;
		public static final int	DOWN = 3;
	}
	
	public static class PlayerConstants{
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int ATTACK_1 = 4;
		public static final int HIT = 5;
		public static final int DYING = 6;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
			case RUNNING:
			case DYING:
				return 10;
			case ATTACK_1:
				return 4;
			case JUMP:
				return 3;
			case FALLING:
				return 2;
			default:
				return 1;
			}
		}
	}
}
