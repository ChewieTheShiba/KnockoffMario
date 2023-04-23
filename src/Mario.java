import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Mario
{
	private int x, y, yVel;
	private boolean jumping, faceRight, movingRight, movingLeft, touchingStage, goingUp, falling;
	private ImageIcon sprite;
	private final int JUMPHEIGHT;
	private Rectangle Hitbox;	
	public Mario()
	{
		x = 1920/2;
		y = 0;
		jumping = false;
		touchingStage = false;
		sprite = new ImageIcon("assets/MarioIdleRight.png");
		goingUp = false;
		faceRight = true;
		yVel = 2;
		JUMPHEIGHT = 35;
		Hitbox = new Rectangle(x+10, y+3, 18, 50);
	}
	
	public void loadSprite(Graphics2D g, JPanel panel)
	{ 
		sprite.paintIcon(panel, g, x, y); 
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

	public boolean isJumping()
	{
		return jumping;
	}

	public void setJumping(boolean jumping)
	{
		this.jumping = jumping;
	}

	public boolean isFaceRight()
	{
		return faceRight;
	}

	public void setFaceRight(boolean faceRight)
	{
		this.faceRight = faceRight;
	}

	public boolean isMovingRight()
	{
		return movingRight;
	}

	public void setMovingRight(boolean movingRight)
	{
		this.movingRight = movingRight;
		if(movingRight)
			faceRight = true;
	}

	public boolean isMovingLeft()
	{
		return movingLeft;
	}

	public void setMovingLeft(boolean movingLeft)
	{
		this.movingLeft = movingLeft;
		if(movingLeft)
			faceRight = false;
	}

	public boolean isTouchingStage(ArrayList<StageHitbox> h)
	{
		touchingStage = false;
		
		for(StageHitbox t : h)
		{
			if(t.getHitbox().intersects(Hitbox))
			{
				//System.out.println("ttttttttt\t" + falling);
				//System.out.println(movingRight);
				//System.out.println(goingUp);
				
				System.out.println(Hitbox.y);
				
				if(falling && Hitbox.y < t.getHitbox().y)
				{
					//System.out.println("yyyyyyyyyy\t" + y);
					y = t.getHitbox().y - Hitbox.height;
				}
				
				else if(goingUp && Hitbox.x > t.getHitbox().x && Hitbox.x < t.getHitbox().x + t.getHitbox().width)
				{
					y = t.getHitbox().y + t.getHitbox().height;
					System.out.println(y);
				}
				
				
				else if(movingRight && (falling || goingUp))
				{
					movingRight = false;
					falling = true;
					for(StageHitbox b : h)
						b.setHitbox(new Rectangle(b.getHitbox().x+10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
				}
					
				
				else if(movingLeft && (falling || goingUp))
				{
					movingLeft = false;
					falling = true;
					for(StageHitbox b : h)
						b.setHitbox(new Rectangle(b.getHitbox().x-10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
				}
				
				touchingStage = true;
			}
		}
		return touchingStage;
	}

	public void setTouchingStage(boolean touchingStage)
	{
		this.touchingStage = touchingStage;
	}

	public int getyVel()
	{
		return yVel;
	}

	public void setyVel(int yVel)
	{
		this.yVel = yVel;
	}

	public int getJUMPHEIGHT()
	{
		return JUMPHEIGHT;
	}
	
	public void updateHitbox()
	{
		Hitbox = new Rectangle(x+10, y+3, 18, 50);
	}
	
	public void updateSprite()
	{
		if(movingRight)
			sprite = new ImageIcon("assets/MarioWalkRight.gif");
		else if(movingLeft)
			sprite = new ImageIcon("assets/MarioWalkLeft.gif");
		//leave the faceRights at the bottom to not mess up code
		else if(faceRight)
			sprite = new ImageIcon("assets/MarioIdleRight.png");
		else if(!faceRight)
			sprite = new ImageIcon("assets/MarioIdleLeft.png");
			
	}

	public boolean isGoingUp()
	{
		return goingUp;
	}

	public void setGoingUp(boolean goingUp)
	{
		this.goingUp = goingUp;
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
