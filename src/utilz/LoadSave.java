package utilz;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.awt.Color;
import main.Game;

public class LoadSave {
	public static final String PLAYER_ATLAS = "knight_merged_2.png";
	public static final String LEVEL_ATLAS = "teste.png";
	public static final String LEVEL_ATLAS_NEW = "Front_tiles_merged_new.png";
	public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String BACKGROUND_LAYER_ONE = "layer_1.png";
	public static final String BACKGROUND_LAYER_TWO = "layer_2.png";
	public static final String PLAYING_BACKGROUND = "playing_background.png";
	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static int[][] GetLevelData(){
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] levelData = new int[img.getHeight()][img.getWidth()];
		for(int i = 0; i < img.getHeight(); i++)
			for(int j = 0; j < img.getWidth(); j++) {
				Color color = new Color(img.getRGB(j, i));
				int value = color.getRed();
				if(value > 64 || value == 63)
					value = 72;
				levelData[i][j] = value; 
			}
		return levelData;
	}
}
