package farkle;

import java.awt.*;
/**
 * Implements a simple button class without swing
 * @author Joshua
 */
public class Button {
	
    private final Rectangle rect;	//Rectangle representing the full area of the button
    private final String text;		//The text that is displayed in the button

    private final Color border;		//The Color of the border
    private final Color inner;		//The Color of the inner rectangle
    private final Color fade;		//The Color that the inner fades to when hovered
    private int opacity;			//How much the button has fadded in

    private final int borderWidth;	//How big the border is
    private final int fontSize;		//How large the text is

	/**
	 * Sets the fields with the Param Data
	 * @param rect
	 * @param text
	 * @param border
	 * @param inner
	 * @param fade
	 * @param borderWidth
	 * @param fontSize 
	 */
    public Button(Rectangle rect, String text, Color border, Color inner, Color fade, int borderWidth, int fontSize) {
        this.rect = rect;
        this.text = text;
        this.border = border;
        this.inner = inner;
        this.fade = fade;
        this.borderWidth = borderWidth;
        this.fontSize = fontSize;
    }
	
	/**
	 * This checks if the point is within the rectangle the button represents
	 * @param p this is the point to check 
	 * @return If this point is within the button rectangle
	 */
    public boolean containsPoint(Vector2 p) {
        return rect.containsPoint(p);
    }
	/**
	 * This updates the hover state of the button
	 * @param MousePos This is where the mouse button is
	 */
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
	/**
	 * This draws the button on the graphics provided
	 * @param g This is where the button will draw
	 */
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
