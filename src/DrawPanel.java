import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.Color;
public class DrawPanel extends JPanel{
	private int posX = -10, oldX = -10;
	//Position Y du pointeur
	private int posY = -10, oldY = -10;
	private boolean erasing = true;
	private boolean clickGaucheStatus = false;
	private boolean leaveMouseStatus = false;
	private Color couleurPointer = Color.red;
	private String formePointer = "rond";
	private ArrayList<Point> points = new ArrayList<Point>();
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.white);
		g.fillRect(0, 0, this.getWidth(), this.getWidth());
		if(this.erasing){
			this.erasing = false;
			}
			else{
			//On parcourt notre collection de points
			for(Point p : this.points)
			{
			//On récupère la couleur
			g.setColor(p.getColor());
			//Selon le type de point
			if(p.getType().equals("carre")){
			g.fillRect(p.getX(), p.getY(), p.getSize(), p.getSize());
			}
			else{
			g.fillOval(p.getX(), p.getY(), p.getSize(), p.getSize());
			}
			}
			}
			
		
	}
public void setCouleurPointer(Color c)
{
	this.couleurPointer = c;
}
public void setFormePointer(String s)
{
	this.formePointer = s;
}
public void setClickGaucheStatus(boolean s)
{
	this.clickGaucheStatus = s;
}
public void setLeaveMouseStatus(boolean s)
{
	this.leaveMouseStatus = s;
}
public void setPosX(int posX) {
	this.posX = posX;
	}
public void setPosY(int posY) {
	this.posY = posY;
	}
public int getPosX() {
	return posX;
	}
public int getPosY() {
	return posY;
	}
public boolean getClickGaucheStatus()
{
	return clickGaucheStatus;
}
public boolean getLeaveMouseStatus()
{
	return leaveMouseStatus;
}
public Color getCouleurPointer()
{
	return this.couleurPointer;
}
public String getFormePointer()
{
	return this.formePointer;
}
public ArrayList<Point> getPoints()
{
	return this.points;
}
public void setPoints(ArrayList<Point> a)
{
	this.points = a;
	this.repaint();
}

public void erase(){
	this.erasing = true;
	this.points = new ArrayList<Point>();
	repaint();
	}
}
