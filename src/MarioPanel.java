
//all imports are necessary
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;

//must 'extend' JPanel 
//You can rename the class to anything you wish - default is 'PanelTemplate'
public class MarioPanel extends JPanel
{
	//variables for the overall width and height
	private int w, h, leftMost, rightMost;
	private Timer ticker, faller, jumper;
	private Mario mario;
	private ArrayList<StageHitbox> stageHitboxes;
	
	//sets up the initial panel for drawing with proper size
	public MarioPanel(int w, int h)
	{
		this.w = w;
		this.h = h;
		this.setPreferredSize(new Dimension(w,h));
		mario = new Mario();
		ticker = new Timer(20, new ActionListen());
		faller = new Timer(20, new ActionListen());
		jumper = new Timer(20, new ActionListen());
		
		this.addKeyListener(new KeyListen());
		this.setFocusable(true);
		
		leftMost = 0;
		rightMost = 1920;
		
		stageHitboxes = new ArrayList<StageHitbox>();
		
		makeStage();
		
		ticker.start();
	}
	
	
	//all graphical components go here
	//this.setBackground(Color c) for example will change background color
	public void paintComponent(Graphics tg)
	{
		//this line sets up the graphics - always needed
		super.paintComponent(tg);
		
		Graphics2D g = (Graphics2D)tg;
		
		//all drawings below here:
		
		for(int x = 0; x < 1920; x+=30)
		{
			g.drawString("" + x, x, 30);
		}
		
		for(StageHitbox h : stageHitboxes)
			h.loadSprite(g, this);
		
		mario.loadSprite(g, this);
		
		
		
	}
	
	private class ActionListen implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			
			if(source.equals(ticker))
			{
				update();
				repaint();
			}
			if(source.equals(faller))
			{
				mario.setFalling(true);
				
				if(mario.isTouchingStage(stageHitboxes))
				{
					mario.setJumping(false);
					mario.setyVel(2);
					mario.setFalling(false);
					faller.stop();
				}
					
				else
				{
					mario.setY(mario.getY()+mario.getyVel());
					mario.setyVel(mario.getyVel()*2);
					if(mario.getyVel() > 25)
						mario.setyVel(25);
				}
			}
			if(source.equals(jumper))
			{
				mario.setY(mario.getY()-mario.getyVel());
				mario.setyVel(mario.getyVel()-5);
				
				if(mario.getyVel() < 0)
				{
					mario.setyVel(2);
					jumper.stop();
					mario.setGoingUp(false);
					mario.setTouchingStage(false);
					faller.start();
				}
			}
			
		}
		
	}
	
	private class KeyListen implements KeyListener
	{

		public void keyTyped(KeyEvent e)
		{
			// TODO Auto-generated method stub
			
		}

		public void keyPressed(KeyEvent e)
		{
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_D:
				mario.setMovingRight(true);
				break;
			case KeyEvent.VK_A:
				mario.setMovingLeft(true);
				break;
			case KeyEvent.VK_W:
				if(!mario.isJumping())
				{
					mario.setyVel(mario.getJUMPHEIGHT());
					mario.setY(mario.getY()-mario.getyVel());
					mario.setyVel(mario.getyVel()-5);
					mario.setGoingUp(true);
					mario.setJumping(true);
					jumper.start();
				}
				break;
			case KeyEvent.VK_S:
			}
			
		}

		public void keyReleased(KeyEvent e)
		{
			switch(e.getKeyCode()) 
			{
			case KeyEvent.VK_D:
				mario.setMovingRight(false);
				break;
			case KeyEvent.VK_A:
				mario.setMovingLeft(false);
				break;
			case KeyEvent.VK_S:
			}
			
		}
		
	}
	
	public void update()
	{
		updateMario();
	}
	
	public void updateMario()
	{
		mario.updateHitbox();
		mario.updateSprite();
		
		if(mario.isMovingRight())
		{
			for(StageHitbox h : stageHitboxes)
				h.setHitbox(new Rectangle(h.getHitbox().x-10, h.getHitbox().y, h.getHitbox().width, h.getHitbox().height));
		}
			
		if(mario.isMovingLeft())
		{
			for(StageHitbox h : stageHitboxes)
				h.setHitbox(new Rectangle(h.getHitbox().x+10, h.getHitbox().y, h.getHitbox().width, h.getHitbox().height));
		}
			
		
		if(!mario.isTouchingStage(stageHitboxes) && !mario.isGoingUp())
			faller.start();
	}
	
	public void makeStage()
	{
		int constx = 0;
		for(int x = 0; x < 10; x++)
		{
			int rand = (int)(Math.random()*10);
			rand = 1;
			
			switch(rand)
			{
			case 0:
				for(int a = constx; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png")));
				
				constx += 1920;
				break;
			case 1:
				for(int a = constx; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png")));
				
				for(int a = constx + 1920/2-25; a < constx + 1920/2-25+500; a += 50)
					stageHitboxes.add(new StageHitbox(a, 900, 50, 50, new ImageIcon("assets/brick.png")));
				
				for(int a = constx + 1920/2+75; a < constx + 1920/2-25+500; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png")));
				
				constx += 1920;
				break;
			}
		}
	}
}