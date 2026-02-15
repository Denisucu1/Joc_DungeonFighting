package mygame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl fc;// Putem stoca până la 30 de sunete

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/adventure.wav"); // Muzica de fundal
        soundURL[1] = getClass().getResource("/sound/Coin.wav");           // Sunet cheie
        soundURL[2] = getClass().getResource("/sound/chest.wav");        // Sunet cufăr
        soundURL[3] = getClass().getResource("/sound/unlocking.wav");         // Sunet ușă
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setVolume(float volume) {
        fc.setValue(volume);
    }
    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}