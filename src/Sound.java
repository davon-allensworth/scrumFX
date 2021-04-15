import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

public class Sound {
    MediaPlayer mediaPlayer;
    private boolean isMusic;

    public Sound(String filepath, boolean isMusic) {
        this(filepath, isMusic, false);
    }

    public Sound(String filepath, boolean isMusic, boolean loop) {
        Media sound = new Media(new File("src/" + filepath).toURI().toString());
        this.isMusic = isMusic;
        mediaPlayer = new MediaPlayer(sound);

        if(this.isMusic)
            mediaPlayer.setVolume(GameManager.getMusicVolume());
        else
            mediaPlayer.setVolume(GameManager.getSoundVolume());

        if (loop)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void play() {
        mediaPlayer.play();
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public boolean isPlaying(){
        return mediaPlayer.getStatus().equals(Status.PLAYING);
    }
}
