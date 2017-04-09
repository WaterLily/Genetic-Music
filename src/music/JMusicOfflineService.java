package music;

import jm.audio.RTMixer;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Play;
import models.Model;

import java.util.List;
import java.util.Set;

public class JMusicService implements MusicService {

    public JMusicService() {
        RTMixer mixer = new RTMixer();
    }

    @Override
    public void playMelody(List<Model.Note> notes) {
        Phrase phrase = makePhrase(notes);
        Score score = new Score(new Part(phrase));
        play(score);
    }

    @Override
    public void playMultipleParts(Set<List<Model.Note>> notes) {
        Score score = new Score();
        for (List<Model.Note> part : notes) {
            score.add(new Part(makePhrase(part)));
        }
        play(score);
    }

    private Phrase makePhrase(List<Model.Note> notes) {
        Note[] noteArray = new Note[notes.size()];
        for (int i = 0; i < notes.size(); i++) {
            noteArray[i] = new Note(notes.get(i).pitch, 1.0 / (double) notes.get(i).fraction);
        }
        return new Phrase(noteArray);
    }

    private void play(Score score) {
        Play.midi(score);
    }

}