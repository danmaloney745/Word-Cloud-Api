package sw.gmit.ie;


import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


import javax.imageio.ImageIO;

//Tag Cloud generator, needs work, size can be off
public class TagCloud {

	private CollisionDetector cd;
	private FontHandler fh;
	private BufferedImage image;
	private Graphics2D graphics;
	private int imageWidth;
	private int imageHeight;
	private int xPos;
	private int yPos;
	private int direction = 0;
	private int maxWords;
	
	public TagCloud(int width, int height) {
		
		cd = new CollisionDetector();
		fh = new FontHandler();
		image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		graphics = (Graphics2D) image.getGraphics();
		
		this.imageWidth = width;
		this.imageHeight = height;
		this.xPos = width/2;
		this.yPos = height/2;
	}
	
	//draw the word and check for collisions
	public void drawWord(Font font, Color color, String word, int direction) {
		
		Rectangle rect; 
		graphics.setColor(color);
		graphics.setFont(font);

		rect = getStringMargin(graphics, word, xPos, yPos);
		
		while(cd.checkCollision(rect)) {
			move(direction, rect);
			rect = getStringMargin(graphics, word, xPos, yPos);			
		}
		cd.addCollisionMargin(rect);
		
		graphics.drawString(word, xPos, yPos);
	}
	
	//create the tagCloud
	public void createTagCloud(Map<String, Integer> words) throws Exception {
		int i = 0;
		
		for(Map.Entry<String, Integer> word : words.entrySet()) {			
			fh.setFontSize(word.getValue());
			fh.setFont();
			
			drawWord(fh.getFont(), fh.getColor(), word.getKey(), direction);
			
			if(i % 2 == 0){
				changeDirection();
			}
			
			if(i > 30) {
				break;
			}
			i++;
		}
		finishedTagCloud();
	}
	
	//method used to move the words
	private void move(int direction, Rectangle2D rect) {
		switch (direction) {
		case 0:
			if (!(rect.getY() < 0)) {
				yPos--;
			}
			else{
				resetPos();
			}
			break;
			
		case 1:
			if(!(rect.getX() + rect.getWidth() > imageWidth))		
				xPos++;				
			else
				resetPos();
			break;
			
		case 2:
			if(!(rect.getY() + rect.getHeight() > imageHeight))		
				yPos++;				
			else
				resetPos();
			break;
			
		case 3:
			if(!(rect.getX() < 0))				
				xPos--;				
			else
				resetPos();
			break;
		}
	}
	
	private void changeDirection() {
		// TODO Auto-generated method stub
		if(direction > 2) {
			direction = 0;
		}
		
		else{
			direction++;
		}
	}
	
	private Rectangle getStringMargin(Graphics2D g2d, String word, int xHeight, int yWidth)
	{
		FontRenderContext frc = g2d.getFontRenderContext();
		GlyphVector gv = g2d.getFont().createGlyphVector(frc,  word);
		return gv.getPixelBounds(null, xHeight, yWidth);
	}
	
	private void resetPos() {
		xPos = imageWidth/2;
		yPos = imageHeight/2;
		changeDirection();
	}

	//finalise the tag cloud
	public void finishedTagCloud() throws Exception {
		graphics.dispose();
		ImageIO.write(image, "png", new File("tagCloud.png"));
		System.out.println("Word Cloud Generated");
	}

}
