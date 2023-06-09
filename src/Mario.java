import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Mario
{
	private int x, y, yVel, coinCount;
	private boolean jumping, faceRight, movingRight, movingLeft, touchingStage, goingUp, falling, dead, canMove, hasWon;
	private ImageIcon sprite;
	private String marioType;
	private final int JUMPHEIGHT;
	private Rectangle Hitbox;
	private ArrayList<Coin> coins;
	
	public Mario()
	{
		x = 1920/2;
		y = 980;
		jumping = false;
		touchingStage = false;
		marioType = "";
		sprite = new ImageIcon("assets/" + marioType + "MarioIdleRight.png");
		goingUp = false;
		faceRight = true;
		yVel = 2;
		JUMPHEIGHT = 35;
		coinCount = 0;
		dead = false;
		coins = new ArrayList<Coin>();
		Hitbox = new Rectangle(x+10, y+3, 18, 50);
		canMove = true;
	}
	
	public void loadSprite(Graphics2D g, JPanel panel)
	{ 
		sprite.paintIcon(panel, g, x, y); 
	}
	
	public void loadCoins(Graphics2D g, JPanel panel)
	{
		for(int i = 0; i < coins.size(); i++)
		{
			if(coins.get(i).isTiming())
				coins.get(i).getSprite().paintIcon(panel, g, coins.get(i).getX(), coins.get(i).getY());
			else
				coins.remove(i);
		}
	}
	
	public ArrayList<Coin> getCoins()
	{
		return coins;
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

	public boolean isHasWon()
	{
		return hasWon;
	}

	public void setHasWon(boolean hasWon)
	{
		this.hasWon = hasWon;
	}

	public boolean isCanMove()
	{
		return canMove;
	}

	public void setCanMove(boolean canMove)
	{
		this.canMove = canMove;
	}

	public int getCoinCount()
	{
		return coinCount;
	}

	public void setCoinCount(int coinCount)
	{
		this.coinCount = coinCount;
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

	public boolean isTouchingStage(ArrayList<StageHitbox> h, Graphics2D g, JPanel panel)
	{
		touchingStage = false;
		
		if(hasWon)
			sprite = new ImageIcon("assets/" + marioType + "MarioWin.png");
		
		for(int i = 0; i < h.size(); i++)
		{
			StageHitbox t = h.get(i);
			
			if(t.getHitbox().intersects(Hitbox))
			{
				boolean checker = false;
				
				if(movingLeft)
				checker = x >= t.getHitbox().x && x <= t.getHitbox().x + t.getHitbox().width && y + Hitbox.height > t.getHitbox().y && y+Hitbox.height <= t.getHitbox().y + t.getHitbox().height;
				else
				checker = x+20 >= t.getHitbox().x && x+20 <= t.getHitbox().x + t.getHitbox().width && y +Hitbox.height > t.getHitbox().y && y+Hitbox.height <= t.getHitbox().y + t.getHitbox().height;	
				
				if(falling && Hitbox.y < t.getHitbox().y)
					y = t.getHitbox().y - Hitbox.height;
				
				if(falling && Hitbox.y > t.getHitbox().y)
				{
					if(movingRight)
						for(StageHitbox b : h)
							b.setHitbox(new Rectangle(b.getHitbox().x+10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
					else
						for(StageHitbox b : h)
							b.setHitbox(new Rectangle(b.getHitbox().x-10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
				}
				
				else if(goingUp)
				{
					y = t.getHitbox().y + t.getHitbox().height;
					goingUp = false;
					falling = true;
					yVel = 2;
					if(t.isBreakable() && !t.isQMark() && !marioType.equals(""))
						h.remove(i);
					if(t.isQMark())
					{
						t.setBreakable(false); 
						
						int j = (int)(Math.random()*4);
						
						t.setQMark(false);
						
						t.setSprite(new ImageIcon("assets/usedQuestion.jpg"));
						
						if(j == 3)
							new Mushroom(Hitbox.x, t.getHitbox().y, new ImageIcon("assets/Mushroom.png"), !faceRight, g, panel).powerMarioUp(this);
						else
						{
							coins.add(new Coin(t.getHitbox().x, t.getHitbox().y, new ImageIcon("assets/Coin.png"), !faceRight, g, panel));
							coins.get(coins.size()-1).powerMarioUp(this);
						}
					}
				}
				
				
				else if(movingRight && checker)
				{
					movingRight = false;
					falling = true;
					for(StageHitbox b : h)
						b.setHitbox(new Rectangle(b.getHitbox().x+10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
				}
					
				
				else if(movingLeft && checker)
				{
					movingLeft = false;
					falling = true;
					for(StageHitbox b : h)
						b.setHitbox(new Rectangle(b.getHitbox().x-10, b.getHitbox().y, b.getHitbox().width, b.getHitbox().height));
				}
				
				touchingStage = true;
				
				
				if(t.getSprite().toString().equals("assets/endBox.png"))
				{
					canMove = false;
					hasWon = true;
				}
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
		Hitbox = new Rectangle(x+10, y+3, Hitbox.width, Hitbox.height);
	}
	
	public void setHitbox(Rectangle hitbox)
	{
		Hitbox = hitbox;
	}
	
	public void updateSprite()
	{
		if(movingRight)
			sprite = new ImageIcon("assets/" + marioType + "MarioWalkRight.gif");
		else if(movingLeft)
			sprite = new ImageIcon("assets/" + marioType + "MarioWalkLeft.gif");
		//leave the faceRights at the bottom to not mess up code
		else if(faceRight)
			sprite = new ImageIcon("assets/" + marioType + "MarioIdleRight.png");
		else if(!faceRight)
			sprite = new ImageIcon("assets/" + marioType + "MarioIdleLeft.png");
			
	}

	public boolean isGoingUp()
	{
		return goingUp;
	}

	public void setGoingUp(boolean goingUp)
	{
		this.goingUp = goingUp;
	}
	
	public void killMario()
	{
		sprite = new ImageIcon("marioDying.png");
	}

	public boolean isFalling()
	{
		return falling;
	}

	public void setFalling(boolean falling)
	{
		this.falling = falling;
	}

	public boolean isDead()
	{
		if(y > 1080)
			dead = true;
		return dead;
	}

	public String getMarioType()
	{
		return marioType;
	}

	public void setMarioType(String marioType)
	{
		this.marioType = marioType;
	}
	
	public boolean canKillEnemy(Enemy e)
	{
		if(e.getHitbox().intersects(Hitbox))
		{
			if(falling)
				return true;
			else
				dead = true;
		}
		return false;
	}

	
	
	
	
	
}
