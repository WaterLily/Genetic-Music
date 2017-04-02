package models;

import java.util.ArrayList;
import java.util.List;

public class Model {
    
    static class MelodyBase {
        final Note note;
        private MelodyBase next;
        
        MelodyBase(Note note) {
            this.note = note;
        }
        
        MelodyBase setNext(MelodyBase next) {
            this.next = next;
            return this;
        }
        
        MelodyBase next() {
            return next;
        }
        
        public String toString() {
            String result = note.toString();
            if (next != null) {
                result += " " + next.toString();
            }
            return result;
        }
    }
    
    public static class Note {
        final int pitch; // 60 = Middle C
        
        public Note(int pitch) {
            this.pitch = pitch;
        }
        
        public String toString() {
            if (pitch < 0) return "--";
            
            int octave = (pitch / 12) - 1;
            int noteIndex = (pitch % 12);
            String note = noteString[noteIndex];
            return note + octave;
        }
    }
    private static final String[] noteString = new String[] { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B" };

    
    static class Gamete {
        final MelodyBase melodyChromosome;
        
        private Gamete(MelodyBase melodyChromosome) {
            this.melodyChromosome = melodyChromosome;
        }
        
        static Builder builder() {
            return new Builder();
        }
        
        static final class Builder {
            private MelodyBase melodyChromosome;
            private MelodyBase last;
            Builder setMelody(MelodyBase melodyBase){ //fixme allow building melodybases?
                this.melodyChromosome = melodyBase;
                return this;
            }
            Gamete build() {
                return new Gamete(melodyChromosome);
            }
            private Builder() {}
        }
        
    }
    
    static MelodyBase makeMelodyBases(Note[] notes) {
        MelodyBase last = null;
        for (int i = notes.length - 1; i >= 0; i--) {
            MelodyBase base = new MelodyBase(notes[i]);
            base.setNext(last);
            last = base;
        }
        return last;
    }

    
    public static Monster breed(Monster parent1, Monster parent2) {
        Gamete one = parent1.makeGamete();
        Gamete two = parent2.makeGamete();
        return new Monster(one, two);
    }   
    
}
