package script;

import java.awt.Image;

public class Monster {
    private int x;
    private int y;
    private final String word;
    private final Image image;

    public Monster(int x, int y, String word, Image image) {
        this.x = x;
        this.y = y;
        this.word = word;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getWord() {
        return word;
    }

    public Image getImage() {
        return image;
    }
}
