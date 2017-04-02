package models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lily on 4/1/2017.
 */
public class Monster {
    private Model.MelodyBase melodyChromosome1;
    private Model.MelodyBase melodyChromosome2;

    public Monster(Model.Gamete gamete1, Model.Gamete gamete2) {
        this.melodyChromosome1 = gamete1.melodyChromosome; //fixme
        this.melodyChromosome2 = gamete2.melodyChromosome;
    }

    public Monster(Model.Note[] notes1, Model.Note[] notes2) { //fixme
        this.melodyChromosome1 = Model.makeMelodyBases(notes1);
        this.melodyChromosome2 = Model.makeMelodyBases(notes2);
    }

    public List<Model.Note> getMelody() {
        // apply all genes
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
        boolean r = Math.random() < 0.5;
        Model.MelodyBase first = r ? melodyChromosome1 : melodyChromosome2;
        Model.MelodyBase second = r ? melodyChromosome2 : melodyChromosome1;
        Model.MelodyBase newBase = new Model.MelodyBase(first.note);
        addNotes(newBase, first.next(), second.next());
        return g.setMelody(newBase).build();
    }

    private void addNotes(Model.MelodyBase base, Model.MelodyBase current, Model.MelodyBase other) {
        if (current == null) {
            return;
        } else {
            base.setNext(new Model.MelodyBase(current.note));
        }
        boolean cross = Math.random() < getCrossoverChance(current, other);
        Model.MelodyBase first = cross ? other : current;
        Model.MelodyBase second = cross ? current : other;
        addNotes(base.next(), first.next(), second.next());
    }

    private double getCrossoverChance(Model.MelodyBase one, Model.MelodyBase two) {
        return 0.25;
    }


}
