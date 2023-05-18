package utilz;

public class Constants {
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
		public static final int DYING = 4;
		public static final int HIT = 5;
		public static final int ATTACK_1 = 6;
		public static final int ATTACK_COMBO = 7;
		
		public static int GetSpriteAmount(int player_action) {
			switch(player_action) {
			case IDLE:
			case RUNNING:
			case ATTACK_COMBO:
			case DYING:
				return 10;
			case JUMP:
				return 8;
			case ATTACK_1:
				return 4;
			case FALLING:
				return 3;
			default:
				return 1;
			}
		}
	}
}
