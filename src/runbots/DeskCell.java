
package runbots;

import java.awt.Color;

public class DeskCell {
    
    private int grade;
    private Color wantedColor;
    
    public DeskCell(){}

    public void setGrade(int grade) {
        this.grade = grade;
    }
    
    public void setWantedColor(Color wantedColor) {
        this.wantedColor = wantedColor;
    }

    public int getGrade() {
        return grade;
    }
    
    public Color getWantedColor() {
        return wantedColor;
    }
    
    
}
