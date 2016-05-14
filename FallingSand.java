package FallingSand;

import apcs.*;

public class FallingSand {
	
	static int[][] state;
	static int width = 500;
	static int height = 500;
	
	static int empty = 0;
	static int sand = 1;
	static int water = 2;

	public static void main(String[] args) {
		Window.size(width, height);
		Window.setFrameRate(200);
		state = new int[width][height];
		
		while (true) {
			mouseGenerate();
			draw();
			next();
		}
	}
	
	private static void mouseGenerate() {
		// If the mouse is clicked
			// 10 times,
				// generate sand around the mouse position
		
		if (Window.mouse.clicked()) {
			for (int i = 0; i < 10; i++) {
				int x = Window.mouse.getX() + Window.rollDice(11) - 6;
				int y = Window.mouse.getY() + Window.rollDice(11) - 6;
				
				if (x >= 0 && x < width && y >= 0 && y < height) {
					if (Window.key.pressed("s"))
						state[x][y] = sand;
					else if (Window.key.pressed("w"))
						state[x][y] = water;
				}
			}
		}
		
	}

	private static void draw() {
		Window.out.background("black");
		
		// Go to every pixel in the window
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				
				// If there is a sand particle
				if (state[x][y] == sand) {
					Window.out.color("tan");
					Window.out.square(x, y, 1);
				}
				else if (state[x][y] == water) {
					Window.out.color("blue");
					Window.out.square(x, y, 1);
				}
			}
		}
		
		Window.frame();
	}

	public static void next() {
		int[][] nextState = new int[width][height];
		
		//Figure out where things go in the next state
		for (int x = width - 1; x >= 0; x--) {
			for (int y = height - 1; y >= 0; y--) {
				
				//If there is a sand particle at x, y
				if (state[x][y] == sand) {
					// If the particle can move down
					if (y + 1 < height && state[x][y + 1] == empty && nextState[x][y + 1] == empty) {
						nextState[x][y + 1] = sand;
					}
					// Otherwise, is there "pressure" on this particle
					else if (y - 2 >= 0 && state[x][y - 1] == sand && state[x][y - 2] == sand) {
                        boolean left = false;
                        boolean right = false;
                        
                        // Check if the particle can move left
                        if (x > 0 && state[x - 1][y] == empty && nextState[x - 1][y] == empty)
                            left = true;
                            
                        // Check if the particle can move right
                        if (x + 1 < width && state[x + 1][y] == empty && nextState[x + 1][y] == empty)
                            right = true;
                        
                        // Check left and right (4 cases)
                        // - move to left or right
                        if (left && right) {
                            if (Window.flipCoin()) {
                                nextState[x - 1][y] = sand;
                            }
                            else {
                                nextState[x + 1][y] = sand;
                            }
                        }
                        // - move to left
                        else if (left) {
                            nextState[x - 1][y] = sand;
                        }
                        // - move to right
                        else if (right) {
                            nextState[x + 1][y] = sand;
                        }
                        // - stay in same place
                        else {
                            nextState[x][y] = sand;
                        }
                    }
					// If there isn't any "pressure," stay
					else {
						nextState[x][y] = sand;
					}
					
					
				}
				 // If there is a water particle at this position
                else if (state[x][y] == water) {
                    // If the particle can move down
                    if (y + 1 < height && state[x][y + 1] == empty && nextState[x][y + 1] == empty) {
                        nextState[x][y + 1] = water;
                    }
                    // Try to move the particle left or right
                    else {
                        boolean left = x > 0 && state[x - 1][y] == empty && nextState[x - 1][y] == empty;
                        boolean right = x + 1 < width && state[x + 1][y] == empty && nextState[x + 1][y] == empty;
                        
                        // If both are true, set one to false randomly
                        if (left && right) {
                            if (Window.flipCoin()) {
                                right = false;
                            }
                            else {
                                left = false;
                            }
                        }
                        
                        // To move left, keep moving left until you can't
                        if (left) {
                            int fx = x - 1;
                            int fy = y;
                            int i = 0;
                            while (i < 5 && fx - 1 >= 0 && state[fx - 1][fy] == empty && nextState[fx - 1][fy] == empty) {
                                fx--;
                                if (fy + 1 < height && state[fx][fy + 1] == empty && nextState[fx][fy + 1] == empty) {
                                    fy++;
                                }
                            }
                            nextState[fx][fy] = water;
                        }
                        // To move right, keep moving right until you can't
                        else if (right) {
                            int fx = x + 1;
                            int fy = y;
                            int i = 0;
                            while (i < 5 && fx + 1 < width && state[fx + 1][fy] == empty && nextState[fx + 1][fy] == empty) {
                                fx++;
                                if (fy + 1 < height && state[fx][fy + 1] == empty && nextState[fx][fy + 1] == empty) {
                                    fy++;
                                }
                            }
                            nextState[fx][fy] = water;
                        }
                        else {
                            nextState[x][y] = water;
                        }
                    }
                }
			}
		}
		
		state = nextState;
	}

}
