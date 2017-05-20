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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WindowView implements View {

  private static final Color SOFT_BLUE = new Color(100, 100, 200);
  private static final Color GREY_BLUE = new Color(75, 75, 100);

  private Set<Singer> active;
  private boolean playing;
  private JFrame window;
  private SingerPane singerPane;
  private DetailsPane detailsPane;
  private JButton breed;

  public WindowView() {
    active = new HashSet<>();
    playing = false;

    window = new JFrame("Genetic Music");
    window.setLayout(new BorderLayout());
    window.setSize(800, 400);
    window.setLocationRelativeTo(null);
    window.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        Controller.end();
      }
    });
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    //create pane for displaying creature squares
    singerPane = new SingerPane();
    singerPane.setLayout(new FlowLayout(FlowLayout.LEFT));
    singerPane.setPreferredSize(new Dimension(280, 400));
//    singerPane.setBorder(BorderFactory.createEtchedBorder(SOFT_BLUE, GREY_BLUE));
    window.getContentPane().add(singerPane, BorderLayout.WEST);

    //create pane for displaying details
    detailsPane = new DetailsPane();
    window.getContentPane().add(detailsPane, BorderLayout.EAST);

    //create global play/pause button
    JButton play = new JButton("Play");
    play.setBackground(Color.ORANGE);
    play.addActionListener(e -> {
      playing = !playing;
      if (playing) {
        Set<Monster> singers = new HashSet<>();
        Set<String> names = new HashSet<>();
        for (Singer singer : singerPane.singers) {
          singers.add(singer.monster);
          if (singer.isActive()) {
            names.add(singer.monster.getName());
          }
        }
        Controller.play(singers, names);
        play.setText("Pause");
      } else {
        Controller.pause();
        play.setText("Play");
      }
    });
    //create breed button
    breed = new JButton("Breed");
    breed.addActionListener(e -> {
      Monster parent1 = null;
      Monster parent2 = null;
      for (Singer singer : active) {
        if (parent1 == null) {
          parent1 = singer.monster;
        } else {
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
  }

  @Override
  public void start() {
    window.setVisible(true);
  }

  @Override
  public void setData(List<Monster> monsters) {
    singerPane.clear();
    for (Monster monster : monsters) {
      Singer singer = new Singer(monster);
      singerPane.add(singer);
    }
  }

  @Override
  public void add(Monster monster) {
    Singer singer = new Singer(monster);
    singerPane.add(singer);
  }

  private class SingerPane extends JPanel {
    final List<Singer> singers;

    SingerPane() {
      this.singers = new ArrayList<>();
    }

    void clear() {
      singers.clear();
      removeAll();
    }

    void add(Singer singer) {
      singers.add(singer);
      JButton button = makeButton(singer, singer.monster.getName());
      add(button);
      button.doClick();
      revalidate();
    }

    private JButton makeButton(Singer singer, String name) {
      JButton button = new JButton(name);
      button.addActionListener(e -> {
        singer.setActive(!singer.isActive());
        button.setBackground(singer.isActive() ? Color.PINK : Color.GRAY);
        if (singer.isActive()) {
          active.add(singer);
          Controller.activateSinger(singer.monster);
        } else {
          active.remove(singer);
          Controller.deactivateSinger(singer.monster);
        }

        detailsPane.display(singer);

        breed.setEnabled(active.size() == 2);
      });
      button.setBackground(singer.isActive() ? Color.PINK : Color.GRAY);
      return button;
    }
  }

  private static class DetailsPane extends JPanel {
    JTextPane details;
    DetailsPane() {
      this.details = new JTextPane();
      JScrollPane scroll = new JScrollPane(details);
      scroll.setPreferredSize(new Dimension(490, 380));
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
