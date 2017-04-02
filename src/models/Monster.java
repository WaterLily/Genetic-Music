package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static utils.Utils.checkNotNull;

/** Models a singing creature. */
public class Monster {
    private Model.MelodyBase melodyChromosome1;
    private Model.MelodyBase melodyChromosome2;
    private Random random;

    public Monster(Model.Gamete gamete1, Model.Gamete gamete2) {
        this.melodyChromosome1 = checkNotNull(gamete1).melodyChromosome; //fixme
        this.melodyChromosome2 = checkNotNull(gamete2).melodyChromosome;
        this.random = new Random();
    }

    public Monster(Model.Note[] notes1, Model.Note[] notes2) { //fixme shouldn't exist
        this.melodyChromosome1 = checkNotNull(Model.makeMelodyBases(notes1));
        this.melodyChromosome2 = checkNotNull(Model.makeMelodyBases(notes2));
        this.random = new Random();
    }

    // Visible for testing
    void setRandom(Random random) {
        this.random = random;
    }

    public List<Model.Note> getMelody() {
        // todo: apply all genes
        List<Model.Note> notes = new ArrayList<>();
        for (Model.MelodyBase base = melodyChromosome1; base != null; base = base.next()) {
            notes.add(base.note);
        }
        for (Model.MelodyBase base = melodyChromosome2; base != null; base = base.next()) {
            notes.add(base.note);
        }
        return notes;
    }

    public Model.Gamete makeGamete() {
        Model.Gamete.Builder g = Model.Gamete.builder();
        boolean r = random.nextDouble() < 0.5;
        Model.MelodyBase first = r ? melodyChromosome1 : melodyChromosome2;
        Model.MelodyBase second = r ? melodyChromosome2 : melodyChromosome1;
        Model.MelodyBase fakeBase = new Model.MelodyBase(new Model.Note(-1, 8));
        addNotes(fakeBase, first, second);
        return g.setMelody(fakeBase.next()).build();
    }

    private void addNotes(Model.MelodyBase base, Model.MelodyBase current, Model.MelodyBase other) {
        if (current == null) {
            return;
        } else {
            base.setNext(new Model.MelodyBase(current.note));
        }
        boolean cross = random.nextDouble() < getCrossoverChance(current, other);
        Model.MelodyBase first = cross ? other : current;
        Model.MelodyBase second = cross ? current : other;
        addNotes(base.next(), first.next(), second.next());
    }

    private double getCrossoverChance(Model.MelodyBase one, Model.MelodyBase two) {
        return 0.25;
    }


}
