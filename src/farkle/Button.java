package farkle;

import java.awt.*;
//import java.awt.event.ActionListener; Will be used if we want to make the buttons self contained action listeners

public class Button {

    private final Rectangle rect;
    private final String text;

    private final Color border;
    private final Color inner;
    private final Color fade;
    private int opacity;

    private final int borderWidth;
    private final int fontSize;

    public Button(Rectangle rect, String text, Color border, Color inner, Color fade, int borderWidth, int fontSize) {
        this.rect = rect;
        this.text = text;
        this.border = border;
        this.inner = inner;
        this.fade = fade;
        this.borderWidth = borderWidth;
        this.fontSize = fontSize;
    }

    public boolean containsPoint(Vector2 p) {
        return rect.containsPoint(p);
    }

    public void update(Vector2 MousePos) {
        if (rect.containsPoint(MousePos)) {
            if (opacity <= 240) {
                opacity += 15;
            }
        }
        else {
            if (opacity >= 15) {
                opacity -= 15;
            }
        }
    }

    public void Draw(Graphics g) {
        g.setColor(border);
        g.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

        g.setColor(inner);
        g.fillRect(rect.getX() + borderWidth, rect.getY() + borderWidth,
                rect.getWidth() - borderWidth * 2, rect.getHeight() - borderWidth * 2);

        g.setColor(new Color(fade.getRed(), fade.getGreen(), fade.getBlue(), opacity));
        g.fillRect(rect.getX() + borderWidth, rect.getY() + borderWidth, rect.getWidth()
                - borderWidth * 2, rect.getHeight() - borderWidth * 2);

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, fontSize));

        int width = g.getFontMetrics().stringWidth(text);
        int height = g.getFontMetrics().getHeight();
        g.drawString(text, rect.getX() + rect.getWidth() / 2 - width / 2, rect.getY()
                + rect.getHeight() / 2 + height / 4);
    }
}
