import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class StageHitbox
{
	private ImageIcon sprite;
	private Rectangle Hitbox;
	private boolean isBreakable;
	
	public StageHitbox(int x, int y, int width, int height, ImageIcon sprite, boolean isBreakable)
	{
		this.sprite = sprite;
		Hitbox = new Rectangle(x, y, width, height);
		this.isBreakable = isBreakable;
	}
	
	public void loadSprite(Graphics2D g, JPanel panel)
	{
		sprite.paintIcon(panel, g, Hitbox.x, Hitbox.y);
	}

	public Rectangle getHitbox()
	{
		return Hitbox;
	}

	public void setHitbox(Rectangle hitbox)
	{
		Hitbox = hitbox;
	}

	public boolean isBreakable()
	{
		return isBreakable;
	}

	public void setBreakable(boolean isBreakable)
	{
		this.isBreakable = isBreakable;
	}
	
	
	
	
}
