package view;

import models.Monster;

import java.util.List;
public interface View {
    
    public void start();
    public void setData(List<Monster> monsters);
    public void add(Monster monster);
    
}
