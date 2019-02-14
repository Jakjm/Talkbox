package testing;
import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import musicplayer.MusicPlayer;
public class MusicTest {
	/**
	 * Testing the opening of a music player for errors.
	 */
	@Test
	public void test1() {
		try {
			MusicPlayer player = new MusicPlayer("/testing/test.wav");
			//If the player is considered playing before starting, fail.
			if(player.isPlaying()) {
				fail();
			}
			player.play();
			while(player.isPlaying()) {
				
			}
		}
		catch(Exception e) {
			fail();
		}
	}
	/**
	 * Testing the music player's ability to stop on command
	 */
	@Test
	public void test2() {
		try {
			MusicPlayer player = new MusicPlayer(new File("src/testing/test.wav"));
			//Ensure the player is not considered playing yet
			if(player.isPlaying()) {
				fail();
			}
			player.play();
			//Ensure that the player must know that it is being played
			if(!player.isPlaying()) {
				fail();
			}
			Thread.sleep(500);
			//Ensure that the player still is considered playing
			if(!player.isPlaying()) {
				fail();
			}
			//Now stop the player
			player.stop();
			if(player.isPlaying()) {
				fail();
			}
		}
		catch(Exception e) {
			fail();
		}
	}
	/**
	 * Test the looping functionality of the music player
	 */
	@Test
	public void test3() {
		try {
			MusicPlayer player = new MusicPlayer("/testing/test2.wav");
			player.setMode(MusicPlayer.LOOP);
			player.play();
			//Have the track loop for 4 seconds
			long playTime = System.currentTimeMillis() + 4000;
			while(System.currentTimeMillis() < playTime) {
				if(!player.isPlaying()) {
					fail();
				}
			}
			player.stop();
			if(player.isPlaying()) {
				fail();
			}
		}
		catch(Exception e) {
			fail();
		}
	}
	/**
	 * Checking skip function, reset function, checking the frame position and track position
	 */
	@Test
	public void test4() {
		try {
			MusicPlayer player = new MusicPlayer("/testing/test4.wav");
			
			//Skipping 1 minute and 58 seconds
			player.skip("1:58");
			if(player.framePosition() <= 0) {
				fail();
			}
			//Ensuring the player records the right frame position
			if(player.currentTrackPosition().equals("1:58")) {
				//System.out.println("Track time:" + player.currentTrackPosition());
			}
			else {
				fail();
			}
			
			//Checking that the reset button resets the frame position and track
			//Position to zero
			player.reset();
			if(player.framePosition() == 0 && player.currentTrackPosition().equals("0:00")) {
				
			}
			else {
				fail();
			}
			
			
			//Trying to skip again
			player.skip(2,2);
			if(player.currentTrackPosition().equals("2:02")) {
				//System.out.println("Track time:" + player.currentTrackPosition());
			}
			else {
				fail();
			}
			
			//Playing - it should be starting from 2:02
			player.play();
			while(player.isPlaying()) {
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Testing that the reset method works properly
	 */
	@Test
	public void test5() {
		
		MusicPlayer player = new MusicPlayer(new File("src/testing/test4.wav"));
		//Skipping through the song
		player.skip(2,03);
		if(!player.currentTrackPosition().equals("2:03")) {
			fail();
		}
		
		//Playing for 4 seconds
		long playTime = System.currentTimeMillis() + 4000;
		player.play();
		while(System.currentTimeMillis() < playTime) {
			
		}
		
		//Resetting and then playign for five seconds.
		player.reset();
		if(!player.currentTrackPosition().equals("0:00")) {
			fail();
		}
		playTime = System.currentTimeMillis() + 5000;
		player.play();
		while(System.currentTimeMillis() < playTime) {
			
		}
	}
}
