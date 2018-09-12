package farkle;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
//import java.awt.event.ActionListener; Will be used if we want to make the buttons self contained action listeners

public class Button {

    final Rectangle Rect;
    final String Text;

    final Color Border;
    final Color Inner;
    final Color Fade;
    int Opacity;

    final int BorderWidth;
    final int FontSize;

    public Button(Rectangle Rect, String Text, Color Border, Color Inner, Color Fade, int BorderWidth, int FontSize) {
        this.Rect = Rect;
        this.Text = Text;
        this.Border = Border;
        this.Inner = Inner;
        this.Fade = Fade;
        this.BorderWidth = BorderWidth;
        this.FontSize = FontSize;
    }

    public boolean ContainsPoint(Vector2 p) {
        return Rect.ContainsPoint(p);
    }

    public void Update(Vector2 MousePos) {
        if (Rect.ContainsPoint(MousePos)) {
            if (Opacity <= 240) {
                Opacity += 15;
            }
        }
        else {
            if (Opacity >= 15) {
                Opacity -= 15;
            }
        }
    }

    public void Draw(Graphics g) {
        g.setColor(Border);
        g.fillRect(Rect.GetX(), Rect.GetY(), Rect.GetWidth(), Rect.GetHeight());
        g.setColor(Inner);
        g.fillRect(Rect.GetX() + BorderWidth, Rect.GetY() + BorderWidth, Rect.GetWidth() - BorderWidth * 2, Rect.GetHeight() - BorderWidth * 2);
        g.setColor(new Color(Fade.getRed(), Fade.getGreen(), Fade.getBlue(), Opacity));
        g.fillRect(Rect.GetX() + BorderWidth, Rect.GetY() + BorderWidth, Rect.GetWidth() - BorderWidth * 2, Rect.GetHeight() - BorderWidth * 2);
        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, FontSize));
        int Width = g.getFontMetrics().stringWidth(Text);
        int Height = g.getFontMetrics().getHeight();
        g.drawString(Text, Rect.GetX() + Rect.GetWidth() / 2 - Width / 2, Rect.GetY() + Rect.GetHeight() / 2 + Height / 4);
    }
}
