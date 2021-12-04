/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;
	private static final int TITLE_MENU_HEIGHT = 20;
/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT - TITLE_MENU_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW-1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_DIAMETER = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		setupGame();
		playGame();
	}
	
	//setup functions
	
	private void setupGame() {
		setBackground(Color.BLACK);
		setBricks();
		setPaddle();
		setBall();
		setVelocities();
	}
	
	private void setBricks() {
		for(int y = 0; y < NBRICK_ROWS; y++) {
			int brickY = BRICK_Y_OFFSET + y*(BRICK_HEIGHT+BRICK_SEP);
			for(int x = 0; x < NBRICKS_PER_ROW; x++) {
				int brickX = x*BRICK_SEP + x*BRICK_WIDTH + 2; 
				//+2 is necessary because there is are a couple of pixels (4) that got rounded off due
				//to integer division earlier, and so +2 centers the bricks
				GRect brick = new GRect(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);			
				switch(y) {
					case 0:
					case 1:
						brick.setColor(Color.RED);
						break;
					case 2:
					case 3:
						brick.setColor(Color.ORANGE);
						break;
					case 4:
					case 5:
						brick.setColor(Color.YELLOW);
						break;
					case 6:
					case 7:
						brick.setColor(Color.GREEN);
						break;
					case 8:
					case 9:
						brick.setColor(Color.CYAN);
				}
				add(brick);
				brickCount++;
			}
		}
	}

	private void setPaddle() {
		paddle = new GRect((getWidth()/2)-(PADDLE_WIDTH/2),getHeight()-PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.WHITE);
		add(paddle);
	}

	private void setBall() {
		ball = new GOval((getWidth()/2)-BALL_DIAMETER/2, (getHeight()/2)-BALL_DIAMETER/2, BALL_DIAMETER, BALL_DIAMETER);
		ball.setFilled(true);
		ball.setFillColor(Color.WHITE);
		add(ball);
	}
	
	private void setVelocities() {
		vy = 3.0;
		vx = rgen.nextDouble(1.0, 3.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
	}
	
	//gametime functions
	
	private void playGame() {
		int life = 0;
		while(life < NTURNS) {
			ball.move(vx, vy);
			life += checkBallBoundaryCollision();
			GObject item = getCollidingObject();
			if(item != null ) {
				if(item == paddle) {
					ball.setLocation(ball.getX(), paddle.getY()-BALL_DIAMETER);
					vy = -vy;
				} else {
					vy = -vy;
					remove(item);
					brickCount--;
				}
			}
			if(brickCount == 0) {
				break;
			}
			pause(20);
			
		}
	}
	
	private int checkBallBoundaryCollision() {
		if(ball.getX() + BALL_DIAMETER > WIDTH) {
			ball.setLocation(WIDTH - BALL_DIAMETER, ball.getY());
			vx = -vx;
		} else if(ball.getX() < 0) {
			ball.setLocation(0, ball.getY());
			vx = -vx;
		}
		if(ball.getY() + BALL_DIAMETER > HEIGHT) {
			ball.setLocation(WIDTH/2, HEIGHT/2); //not perfectly centered, but easier to read and the ball is moving anyway
			setVelocities();
			return 1;
		} else if(ball.getY() < 0) {
			ball.setLocation(ball.getX(), 0);
			vy = -vy;
		}
		
		return 0;
	}
	
	private GObject getCollidingObject() {
		GObject gameItem = null;
		for(int y = 0; y < 2; y++) {
			for(int x = 0; x < 2; x++) {
				gameItem = getElementAt(ball.getX() + x*BALL_DIAMETER, ball.getY() + y*BALL_DIAMETER);
				if(gameItem != null) {
					return gameItem;
				}
			}
		}
		return gameItem;
	}
	
	public void mouseMoved(MouseEvent e) {
		paddle.setLocation(e.getX()-(PADDLE_WIDTH/2),paddle.getY());
		if(paddle.getX()+PADDLE_WIDTH > WIDTH) {
			int x = WIDTH - PADDLE_WIDTH;
			paddle.setLocation(x, paddle.getY());
		} else if(paddle.getX() < 0) {
			paddle.setLocation(0, paddle.getY());
		}
	}
	
	private GRect paddle;
	private GOval ball;
	private double vx, vy;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private int brickCount = 0;

}
