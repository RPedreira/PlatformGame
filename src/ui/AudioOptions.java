package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.Gamestate;
import main.Game;
import utilz.Constants.UI.PauseButtons;
import utilz.Constants.UI.VolumeButtons;

public class AudioOptions {
	private SoundButton musicButton, sfxButton;
	private VolumeButton volumeB;
	private Game game;
	
	public AudioOptions(Game game) {
		this.game = game;
		createSoundButtons();
		createVolumeButton();
	}
	
	private void createVolumeButton() {
		int vX = (int)(309 * Game.SCALE);
		int vY = (int)(278 * Game.SCALE);
		volumeB = new VolumeButton(vX, vY, VolumeButtons.SLIDER_WIDTH, VolumeButtons.VOLUME_HEIGHT);
	}
	
	private void createSoundButtons() {
		int soundX = (int)(450 * Game.SCALE);
		int musicY = (int)(140 * Game.SCALE);
		int sfxY = (int)(186 * Game.SCALE);
		musicButton = new SoundButton(soundX, musicY, PauseButtons.SOUND_SIZE, PauseButtons.SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, PauseButtons.SOUND_SIZE, PauseButtons.SOUND_SIZE);
	}
	
	public void update() {
		musicButton.update();
		sfxButton.update();
		volumeB.update();
	}
	
	public void draw(Graphics g) {
		musicButton.draw(g);
		sfxButton.draw(g);
		volumeB.draw(g);
	}
	
	public void mouseDragged(MouseEvent e) {
		if(volumeB.isMousePressed()) {
			float valueBefore = volumeB.getFloatValue();
			volumeB.changeX(e.getX());
			float valueAfter = volumeB.getFloatValue();
			if(valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		if(isIn(e, musicButton))
			musicButton.setMousePressed(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMousePressed(true);
		else if(isIn(e, volumeB))
			volumeB.setMousePressed(true);
	}

	public void mouseReleased(MouseEvent e) {
		if(isIn(e, musicButton)) {
			if(musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
				game.getAudioPlayer().toggleSongMute();
			}
		}else if(isIn(e, sfxButton)) {
			if(sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
				game.getAudioPlayer().toggleEffectMute();
			}
		}
		musicButton.resetBools();
		sfxButton.resetBools();
		volumeB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volumeB.setMouseOver(false);
		if(isIn(e, musicButton))
			musicButton.setMouseOver(true);
		else if(isIn(e, sfxButton))
			sfxButton.setMouseOver(true);
		else if(isIn(e, volumeB))
			volumeB.setMouseOver(true);
	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return b.getBounds().contains(e.getX(), e.getY());
	}
}
