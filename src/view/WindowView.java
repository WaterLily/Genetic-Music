package view;

import main.Controller;
import models.Monster;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WindowView implements View {

  private static final Color SOFT_BLUE = new Color(100, 100, 200);
  private static final Color GREY_BLUE = new Color(75, 75, 100);

  private List<Singer> singers;
  private boolean playing;
  private JPanel singerPane;
  private DetailsPane detailsPane;
  private JButton breed;

  public WindowView() {
    singers = new ArrayList<>();
    playing = false;
  }

  @Override
  public void start() {
    JFrame window = new JFrame("Genetic Music");
    window.setLayout(new BorderLayout());
    window.setSize(700, 400);
    window.setLocationRelativeTo(null);
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    //create pane for displaying creature squares
    singerPane = new JPanel();
    singerPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    singerPane.setPreferredSize(new Dimension(350, 400));
//    singerPane.setBorder(BorderFactory.createEtchedBorder(SOFT_BLUE, GREY_BLUE));
    window.getContentPane().add(singerPane, BorderLayout.WEST);
    displaySingers();

    //create pane for displaying details
    detailsPane = new DetailsPane();
    window.getContentPane().add(detailsPane, BorderLayout.EAST);

    //create global play/pause button
    JButton play = new JButton("Play");
    play.setBackground(Color.ORANGE);
    play.addActionListener(e -> {
      playing = !playing;
      if (playing) {
        Set<Monster> singing = new HashSet<>();
        for (Singer singer : singers) {
          if (singer.isActive()) {
            singing.add(singer.monster);
          }
        }
        Controller.play(singing);
        play.setText("Pause");
      } else {
        //stop singing
        play.setText("Play");
      }
    });
    //create breed button
    breed = new JButton("Breed");
    breed.addActionListener(e -> {
      Monster parent1 = null;
      Monster parent2 = null;
      for (Singer singer : singers) {
        if (singer.isActive() && parent1 == null) {
          parent1 = singer.monster;
        } else if (singer.isActive()) {
          parent2 = singer.monster;
        }
      }
      Controller.breed(parent1, parent2);
    });
    breed.setEnabled(false);
    JPanel controls = new JPanel();
    controls.add(play);
    controls.add(breed);
    window.getContentPane().add(controls, BorderLayout.SOUTH);

    window.setVisible(true);
  }

  @Override
  public void setData(List<Monster> monsters) {
    for (Monster monster : monsters) {
      this.singers.add(new Singer(monster));
    }
  }

  @Override
  public void add(Monster monster) {
    Singer singer = new Singer(monster);
    this.singers.add(singer);
    JButton button = makeButton(singer, singers.size() - 1 + "");
    singerPane.add(button);
    button.doClick();
    singerPane.revalidate();
  }

  private void displaySingers() {
    int i = 0;
    for (Singer singer : singers) {
      singerPane.add(makeButton(singer, i + ""));
      i++;
    }
  }

  private JButton makeButton(Singer singer, String name) {
    JButton button = new JButton(name);
    button.addActionListener(e -> {
      singer.setActive(!singer.isActive());
      button.setBackground(singer.isActive() ? Color.PINK : Color.GRAY);
      updateState(singer);
    });
    button.setBackground(singer.isActive() ? Color.PINK : Color.GRAY);
    return button;
  }

  private int numActive;

  private void updateState(Singer singer) {
    detailsPane.display(singer);
    numActive += singer.isActive() ? 1 : -1;
    breed.setEnabled(numActive == 2);
  }

  private static class DetailsPane extends JPanel {
    JTextPane details;
    DetailsPane() {
      this.details = new JTextPane();
      JScrollPane scroll = new JScrollPane(details);
      scroll.setPreferredSize(new Dimension(300, 400));
      add(scroll);
    }
    void display(Singer singer) {
      details.setText(details.getText() + singer.monster.toString() + "\n");
    }
  }

  private static class Singer {
    final Monster monster;
    private boolean active;

    private Singer(Monster monster) {
      this.monster = monster;
    }

    boolean isActive() {
      return active;
    }

    void setActive(boolean active) {
      this.active = active;
    }
  }
}
