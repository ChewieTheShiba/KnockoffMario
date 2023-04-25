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
	protected int x, y;
	
	public PowerUp(int x, int y, ImageIcon sprite, boolean moveRight)
	{
		this.x = x;
		this.y = y;
		falling = true;
		this.sprite = sprite;
	}
	
	public ImageIcon getSprite()
	{
		return sprite;
	}
	
	public Rectangle getHitbox()
	{
		return Hitbox;
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
