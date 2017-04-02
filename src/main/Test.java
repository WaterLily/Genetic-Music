package main;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;

public final class Test implements JMC {

    public static void main(String[] args) {
        Score s = new Score(new Part(new Phrase(new Note(C4, MINIM))));
        Play.midi(s);
    }
}
