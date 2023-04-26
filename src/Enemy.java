import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Enemy
{
	private ImageIcon sprite;
	private Rectangle Hitbox;
	private boolean moveRight, falling, touchingStage;
	private ArrayList<StageHitbox> h;
	private int width, height, x, y, yVel;
	
	public Enemy(ImageIcon sprite, ArrayList<StageHitbox> h, int width, int height)
	{
		moveRight = false;
		this.sprite = sprite;
		this.h = h;
		falling = true;
		yVel = 25;
		
		int rand = (int) (Math.random()*h.size());
		
		Rectangle temp = h.get(rand).getHitbox();
		
		x = temp.x;
		y = temp.y-height;
		touchingStage = true;
		
		Hitbox = new Rectangle(x, y, width, height);
	}
	
	public void loadEnemy(Graphics2D g, JPanel panel)
	{
		sprite.paintIcon(panel, g, Hitbox.x, Hitbox.y);
	}
	
	public void isTouchingStage(ArrayList<StageHitbox> h, Graphics2D g, JPanel panel)
	{
		
		for(int i = 0; i < h.size(); i++)
		{
			StageHitbox t = h.get(i);
			
			touchingStage = false;
			
			if(t.getHitbox().intersects(Hitbox))
			{
				boolean checker = false;
				
				if(!moveRight)
				checker = x >= t.getHitbox().x && x <= t.getHitbox().x + t.getHitbox().width && y-25 + Hitbox.height > t.getHitbox().y && y-25+Hitbox.height <= t.getHitbox().y + t.getHitbox().height;
				else
				checker = x+20 >= t.getHitbox().x && x+20 <= t.getHitbox().x + t.getHitbox().width && y-25 +Hitbox.height > t.getHitbox().y && y-25+Hitbox.height <= t.getHitbox().y + t.getHitbox().height;	
				
				if(falling && Hitbox.y < t.getHitbox().y)
				{
					y = t.getHitbox().y - Hitbox.height;
					Hitbox = new Rectangle(Hitbox.x, y, Hitbox.width, Hitbox.height);
				}
					
				
				if(falling && Hitbox.y > t.getHitbox().y)
				{
					if(moveRight)
					{
						moveRight = false;
						falling = true;
					}
					else
					{
						moveRight = true;
						falling = true;
					}
				}
				
				
				else if(moveRight && checker)
				{
					moveRight = false;
					falling = true;
				}
					
				else if(!moveRight && checker)
				{
					moveRight = true;
					falling = true;
				}
			}
		}
	}
	
	public void fall()
	{
		if(falling)
		{
			y += yVel;
			Hitbox = new Rectangle(Hitbox.x, y, Hitbox.width, Hitbox.height);
		}
	}
	
	public void moveEnemy(Mario mario)
	{
		
		if(moveRight && mario.isMovingRight())
		{
			x = x-5;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
		else if(moveRight && mario.isMovingLeft())
		{
			x = x + 15;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
		else if(moveRight)
		{
			x = x + 5;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
		if(!moveRight && mario.isMovingRight())
		{
			x = x - 15;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
		else if(!moveRight && mario.isMovingLeft())
		{
			x = x + 5;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
		else if(!moveRight)
		{
			x = x - 5;
			Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
		}
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
		Hitbox = new Rectangle(Hitbox.x, y, Hitbox.width, Hitbox.height);
	}
	
	

	public Rectangle getHitbox()
	{
		return Hitbox;
	}

	public void setHitbox(Rectangle hitbox)
	{
		Hitbox = hitbox;
	}

	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
		Hitbox = new Rectangle(x, Hitbox.y, Hitbox.width, Hitbox.height);
	}

	public boolean isFalling()
	{
		return falling;
	}

	public void setFalling(boolean falling)
	{
		this.falling = falling;
	}
	
	
	
}
