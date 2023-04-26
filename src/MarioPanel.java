
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
	private int w, h, leftMost, rightMost, last;
	private Timer ticker, faller, jumper;
	private Mario mario;
	private Graphics2D g;
	private JPanel panel;
	private ArrayList<StageHitbox> stageHitboxes;
	private ArrayList<Enemy> enemies;
	
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
		
		panel = new JPanel();
		this.add(panel);
		
		leftMost = 0;
		rightMost = 1920;
		last = 0;
		enemies = new ArrayList<Enemy>();
		
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
		
		g = (Graphics2D)tg;
		
		new ImageIcon("assets/bg.jpg").paintIcon(panel, g, 0, 0);
		
		//all drawings below here:
		
		for(int x = 0; x < 1920; x+=30)
		{
			g.drawString("" + x, x, 30);
		}
		
		for(StageHitbox h : stageHitboxes)
			h.loadSprite(g, panel);
		
		mario.loadSprite(g, panel);
		mario.loadCoins(g, panel);
		
		for(Enemy e : enemies)
			e.loadEnemy(g, panel);
		
		
		new ImageIcon("assets/coin.png").paintIcon(panel, g, 0, 53);
		g.setColor(Color.black);
		g.setFont(new Font("Comic Sans", Font.PLAIN, 50));
		g.drawString("" + mario.getCoinCount(), 50, 95);
		
		
		
	}
	
	private class ActionListen implements ActionListener
	{

		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			
			if(source.equals(ticker))
			{
				repaint();
				update();
			}
			if(source.equals(faller))
			{
				mario.setFalling(true);
				
				if(mario.isTouchingStage(stageHitboxes, g, panel))
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
				if(!(stageHitboxes.get(0).getHitbox().x >= 0))
				mario.setMovingLeft(true);
				break;
			case KeyEvent.VK_W:
				if(!mario.isJumping())
				{
					mario.setFalling(false);
					faller.stop();
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
		updateStage();
		updateMario();
		updateEnemies();
	}
	
	public void updateMario()
	{	
		if(stageHitboxes.size() > 0 && (stageHitboxes.get(0).getHitbox().x >= 0))
			mario.setMovingLeft(false);
		
		mario.updateHitbox();
		mario.updateSprite();
		
		if(mario.isDead())
			System.out.println("t");
			
		
		if(!mario.isTouchingStage(stageHitboxes, g, panel) && !mario.isGoingUp())
			faller.start();
		

		for(int i = 0; i < enemies.size(); i++)
		{
			if(mario.canKillEnemy(enemies.get(i)))
				enemies.remove(i);
		}
		
		if(mario.isDead())
			System.out.println("DEEEEEEEEAD");
	}
	
	public void updateStage()
	{
		if(mario.isMovingRight())
		{
			for(StageHitbox h : stageHitboxes)
				h.setHitbox(new Rectangle(h.getHitbox().x-10, h.getHitbox().y, h.getHitbox().width, h.getHitbox().height));
			for(Coin c : mario.getCoins())
				c.setX(c.getX()-10);
		}
			
		if(mario.isMovingLeft())
		{
			for(StageHitbox h : stageHitboxes)
				h.setHitbox(new Rectangle(h.getHitbox().x+10, h.getHitbox().y, h.getHitbox().width, h.getHitbox().height));
			for(Coin c : mario.getCoins())
				c.setX(c.getX()+10);
		}
	}
	
	public void updateEnemies()
	{
		for(int i = 0; i < enemies.size(); i++)
		{
			Enemy e = enemies.get(i);
			e.moveEnemy(mario);
			e.fall();
			e.isTouchingStage(stageHitboxes, g, panel);
		}
	}
	
	public void makeStage()
	{
		int constx = 0;
		for(int x = 0; x < 10; x++)
		{
			int rand = -1;
			
			do 
			{
				rand = (int)(Math.random()*3);
			}while(rand == last);
			
			last = rand;
			
			switch(rand)
			{
			case 0:
				for(int a = constx; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				constx += 1920;
				break;
			case 1:
				for(int a = constx; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx + 1920/2+25; a < constx + 1920/2+525; a += 50)
					stageHitboxes.add(new StageHitbox(a, 900, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				for(int a = constx + 1920/2+125; a < constx + 1920/2+575; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				for(int a = constx + 1920/2+225; a < constx + 1920/2+625; a += 50)
					stageHitboxes.add(new StageHitbox(a, 650, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				constx += 1920;
				break;
				
			case 2: 
				for(int a = constx; a < constx+1200; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx+1400; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx+1050; a < constx+1200; a += 50)
					stageHitboxes.add(new StageHitbox(a, 980, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				for(int a = constx+1100; a < constx+1200; a += 50)
					stageHitboxes.add(new StageHitbox(a, 930, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				for(int a = constx+1150; a < constx+1200; a += 50)
					stageHitboxes.add(new StageHitbox(a, 880, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx+1400; a < constx+1550; a += 50)
					stageHitboxes.add(new StageHitbox(a, 980, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				for(int a = constx+1400; a < constx+1500; a += 50)
					stageHitboxes.add(new StageHitbox(a, 930, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				for(int a = constx+1400; a < constx+1450; a += 50)
					stageHitboxes.add(new StageHitbox(a, 880, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				constx += 1920;
				break;
				
			case 3:
				
				for(int a = constx; a < constx+1200; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx+1350; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				for(int a = constx + 300; a < constx + 450; a += 50)
					stageHitboxes.add(new StageHitbox(a, 900, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				for(int a = constx + 450; a < constx + 700; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				for(int a = constx + 850; a < constx + 1050; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				constx += 1920;
				break;
				
			case 4:
				
				for(int a = constx; a < constx+1920; a += 50)
					stageHitboxes.add(new StageHitbox(a, 1030, 50, 50, new ImageIcon("assets/floor.png"), false, false));
				
				stageHitboxes.add(new StageHitbox(constx+1200, 900, 50, 50, new ImageIcon("assets/qMark.png"), true, true));
				stageHitboxes.add(new StageHitbox(constx+1450, 900, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				stageHitboxes.add(new StageHitbox(constx+1550, 900, 50, 50, new ImageIcon("assets/qMark.png"), true, true));
				stageHitboxes.add(new StageHitbox(constx+1600, 900, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				stageHitboxes.add(new StageHitbox(constx+1650, 900, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				stageHitboxes.add(new StageHitbox(constx+1700, 900, 50, 50, new ImageIcon("assets/qMark.png"), true, true));
				
				for(int a = constx + 450; a < constx + 700; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				for(int a = constx + 850; a < constx + 1050; a += 50)
					stageHitboxes.add(new StageHitbox(a, 775, 50, 50, new ImageIcon("assets/brick.png"), true, false));
				
				constx += 1920;
				break;
			}
		}
		
		for(int i = 0; i < 5; i++)
			enemies.add(new Enemy(new ImageIcon("assets/Goomba.png"), stageHitboxes, 50, 50));
		
		enemies.get(4).setX(1700);
	}
}