package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static utilz.Constants.PlayerConstants.*;
import static utilz.Constants.Directions.*;

public class GamePanel extends JPanel {
	private MouseInputs mouseInputs;
	private float xDelta = 100f, yDelta = 100f;
	private BufferedImage img;
	private BufferedImage[][] animations;
	private int animationTick, animationIndex, animationSpeed = 15;
	private int playerAction = IDLE;
	private int playerDirection = -1;
	private boolean moving = false;
	
	public GamePanel() {
		mouseInputs = new MouseInputs(this);
		importImg();
		loadAnimations();
		setPanelSize();
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}
	
	private void loadAnimations() {
		animations = new BufferedImage[8][10];
		for(int i = 0; i < animations.length; i++)
			for(int j = 0; j < animations[i].length; j++) {
				animations[i][j] = img.getSubimage(j*120, i*80, 120, 80);
			}
	}
	
	private void importImg() {
		InputStream is = getClass().getResourceAsStream("/knight_merged_2.png");
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
	}

	public void setPanelSize() {
		Dimension size = new Dimension(1280, 800);
		setPreferredSize(size);
	}
	
	public void setDirection(int direction) {
		this.playerDirection = direction;
		moving = true;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	private void updateAnimationTick() {
		animationTick++;
		if(animationTick >= animationSpeed) {
			animationTick = 0;
			animationIndex++;
			if(animationIndex >= GetSpriteAmount(playerAction))
				animationIndex = 0;
		}
	}
	
	private void setAnimation() {
		if(moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
	}
	
	private void updatePos() {
		if(moving) {
			switch(playerDirection) {
			case LEFT:
				xDelta -= 5;
				break;
			case UP:
				yDelta -= 5;
				break;
			case RIGHT:
				xDelta += 5;
				break;
			case DOWN:
				yDelta += 5;
				break;
			}
		}
	}
	
	public void updateGame() {
		updateAnimationTick();
		setAnimation();
		updatePos();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(animations[playerAction][animationIndex], (int)xDelta, (int)yDelta, 128, 80, null);
	}
}
