import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    MediaPlayer mediaPlayer;

    public Sound(String filepath) {
        this(filepath, false);
    }

    public Sound(String filepath, boolean loop) {
        Media sound = new Media(new File("src/" + filepath).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
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
}
