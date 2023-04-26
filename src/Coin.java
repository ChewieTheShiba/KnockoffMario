import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;

public class Coin extends PowerUp
{
	private Timer timer;
	private boolean isTiming;
	
	public Coin(int x, int y, ImageIcon sprite, boolean moveRight, Graphics2D g,JPanel panel)
	{
		super(x, y-50, sprite, moveRight, g, panel);
		timer = new Timer(500, new ActionListen());
		isTiming = true;
	}
	
	public void powerMarioUp(Mario mario)
	{
		timer.start();
		mario.setCoinCount(mario.getCoinCount()+1);
	}
	
	private class ActionListen implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			
			if(source.equals(timer))
			{
				timer.stop();
				isTiming = false;
			}
		}
	}
	
	public boolean isTiming()
	{
		return isTiming;
	}

}
