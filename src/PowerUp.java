import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;


//made this class for if more power ups are ever made
public abstract class PowerUp
{
	protected ImageIcon sprite;
	protected Rectangle Hitbox;
	protected boolean moveRight, falling;
	protected Graphics2D g;
	protected JPanel panel;
	protected int x, y;
	
	public PowerUp(int x, int y, ImageIcon sprite, boolean moveRight, Graphics2D g, JPanel panel)
	{
		this.x = x;
		this.y = y;
		falling = true;
		this.sprite = sprite;
		this.g = g;
		this.panel = panel;
	}
	
	public ImageIcon getSprite()
	{
		return sprite;
	}
	
	public Rectangle getHitbox()
	{
		return Hitbox;
	}
	
	public void loadSprite(Graphics2D g, JPanel panel)
	{
		sprite.paintIcon(panel, g, x, y);
	}

	public void setHitbox(Rectangle hitbox)
	{
		this.Hitbox = hitbox;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}

	public abstract void powerMarioUp(Mario mario);
}
