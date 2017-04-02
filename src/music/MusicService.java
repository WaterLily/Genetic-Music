package music;

import models.Model;

import java.util.List;
import java.util.Set;

public interface MusicService{

    void playMelody(List<Model.Note> notes);
    void playMultipleParts(Set<List<Model.Note>> notes);
}