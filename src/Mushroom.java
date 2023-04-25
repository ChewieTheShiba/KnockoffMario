import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Mushroom extends PowerUp
{
	
	public Mushroom(int x, int y, ImageIcon sprite, boolean moveRight)
	{
		super(x, y-45, sprite, moveRight);
		Hitbox = new Rectangle(x, y, 50, 45);
	}
	
	public void powerMarioUp(Mario mario)
	{
		mario.setMarioType("Big");
		mario.setHitbox(new Rectangle(mario.getX(), mario.getY(), 38, 71));
	}
	
}
