package view;
import models.Monster;

import javax.swing.JFrame;
import java.util.List;

public class WindowView implements View {


  public void start() {
      JFrame window = new JFrame("Genetic Music");
      window.show();
  }
  
  public void display(Monster monster) {
      
  }

    @Override
    public void display(List<Monster> monsters) {

    }

    @Override
    public void setData(List<Monster> monsters) {

    }

    @Override
    public void add(Monster monster) {

    }
}
