
public class Main {

	static char[][] board = new char[7][9];
	static int depthCount = 3;
	static String[] moveUndo = new String[100];
	static int undoIndex = 0;
	static String[][] legalMoves;
	static int moveIndex;
	static int playerNumber = 1;
	
	public static void main(String[] args) {
		setup();
		printBoard();
		System.out.println("");
		legalMoves = new String[100][depthCount];
		LegalMoves(0);
		PrintMoves(0);
	}

	private static void setup() {
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				board[x][y] = '-';
			}
		}
		
		board[1][0] = 'n';
		board[2][0] = 'r';
		board[3][0] = 'k';
		board[4][0] = 'r';
		board[5][0] = 'n';
		board[3][1] = 'b';
		board[3][2] = 'b';
		board[0][3] = 'p';
		board[2][3] = 'p';
		board[4][3] = 'p';
		board[6][3] = 'p';
		
		board[1][8] = 'N';
		board[2][8] = 'R';
		board[3][8] = 'K';
		board[4][8] = 'R';
		board[5][8] = 'N';
		board[3][7] = 'B';
		board[3][6] = 'B';
		board[0][5] = 'P';
		board[2][5] = 'P';
		board[4][5] = 'P';
		board[6][5] = 'P';
	}
	
	private static void printBoard() {
		for(int y = 8; y >= 0; y--) {
			System.out.print(y+1 + "  ");
			for(int x = 0; x < 7; x++) {
				System.out.print(board[x][y] + " ");
			}
			System.out.println("");
		}
		
		System.out.println("   A B C D E F G");
	}
	
	private static void LegalMoves(int depth) {
		moveIndex = 0;
		if(playerNumber == 1) {
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					switch(board[x][y]) {
					case 'p':
						PawnMoves(x, y, depth);
						break;
					case 'b':
						BishopMoves(x, y, depth);
						break;
					case 'n':
						KnightMoves(x, y, depth);
						break;
					}
				}
			}
		}
		else {
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					
				}
			}
		}
		
	}

	private static void PrintMoves(int depth) {
		System.out.println("LegaMoves");
		for(int x = 0; x < moveIndex; x++) {
			System.out.println(legalMoves[x][depth]);
		}
	}
	
	private static void PawnMoves(int x, int y, int depth) {
		if(y < 8) {
			if(x == 0) {
				if(board[x][y+1] == '-') {
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+2);
					moveIndex++;
				}
				if(board[x+1][y+1] != '-' && board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y+2);
					moveIndex++;
				}
			}
			else if (x == 6) {
				if(board[x][y+1] == '-') {
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+2);
					moveIndex++;
				}
				if(board[x-1][y+1] != '-' && board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y+2);
					moveIndex++;
				}
			}
			else {
				if(board[x][y+1] == '-') {
					switch(x) {
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+2);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+2);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+2);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+2);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+2);
						moveIndex++;
						break;
					}
				}
				if(board[x+1][y+1] != '-' && board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
					switch(x) {
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (y+2);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (y+2);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (y+2);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (y+2);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (y+2);
						moveIndex++;
						break;
					}
				}
				if(board[x-1][y+1] != '-' && board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
					switch(x) {
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (y+2);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (y+2);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (y+2);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (y+2);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (y+2);
						moveIndex++;
						break;
					}
				}
			}
		}
	}
	
	private static void BishopMoves(int x, int y, int depth) {
		if(x == 0) {
			int tempX, tempY;
			if(y < 8) {
				tempX = x + 1;
				tempY = y + 1;
				outerloop: while(board[tempX][tempY] == '-') {
					switch(tempX) {
					case 1:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 6:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break outerloop;
					}
					if(tempY >= 8) {
						break outerloop;
					}
					tempX++;
					tempY++;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 1:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 6:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					}
				}
			}
			if(y > 0) {
				tempX = x + 1;
				tempY = y - 1;
				while(board[tempX][tempY] == '-') {
					if(tempY < 1)
						break;
					tempX++;
					tempY--;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 1:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 6:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					}
				}
			}
			 
		}
		else if( x == 6) {
			int tempX, tempY;
			if(y < 8) {
				tempX = x - 1;
				tempY = y + 1;
				outerloop: while(board[tempX][tempY] == '-') {
					switch(tempX) {
					case 5:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 0:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "A" + (tempY+1);
						moveIndex++;
						break outerloop;
					}
					
					if(tempY >= 8) {
						break outerloop;
					}
					tempX--;
					tempY++;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 5:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 0:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "A" + (tempY+1);
						moveIndex++;
						break;
					}
				}
			}
			if(y > 0) {
				tempX = x - 1;
				tempY = y - 1;
				while(board[tempX][tempY] == '-') {
					if(tempY < 1)
						break;
					tempX--;
					tempY--;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 5:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
					case 0:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "A" + (tempY+1);
						moveIndex++;
						break;
					}
				}
			}
		}
		else {
			int tempX, tempY;
			if(y < 8) {
				tempX = x + 1;
				tempY = y + 1;
				outerloop: while(board[tempX][tempY] == '-') {
					switch(tempX) {
					case 2:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 4:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 5:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 6:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 3 :
							legalMoves[moveIndex][depth] = "D" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break outerloop;
						}
					}
					if(tempY >= 8 || tempX >= 6) {
						break outerloop;
					}
					tempX++;
					tempY++;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 2:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 4:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 5:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 6:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 3 :
							legalMoves[moveIndex][depth] = "D" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					}
				}
				tempX = x - 1;
				tempY = y + 1;
				outerloop: while(board[tempX][tempY] == '-') {
					switch(tempX) {
					case 4:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 2:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 1:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 0:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break outerloop;
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break outerloop;
						}
						break outerloop;
					}
					
					if(tempY >= 8 || tempX >= 6) {
						break outerloop;
					}
					tempX--;
					tempY++;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 4:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 2:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 1:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 0:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					}
				}
				
			}
			if(y > 0) {
				tempX = x + 1;
				tempY = y - 1;
				while(board[tempX][tempY] == '-') {
					if(tempY < 1 || tempX > 5)
						break;
					tempX++;
					tempY--;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 2:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 4:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						case 3 :
							legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 5:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 3 :
							legalMoves[moveIndex][depth] = "D" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 6:
						switch(x) {
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 3 :
							legalMoves[moveIndex][depth] = "D" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					}
				}
				tempX = x - 1;
				tempY = y - 1;
				while(board[tempX][tempY] == '-') {
					if(tempY < 1 || tempX < 1)
						break;
					tempX--;
					tempY--;
				}
				if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
					switch(tempX) {
					case 4:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 3:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 2:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 1:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					case 0:
						switch(x) {
						case 5:
							legalMoves[moveIndex][depth] = "F" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 4:
							legalMoves[moveIndex][depth] = "E" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 3:
							legalMoves[moveIndex][depth] = "D" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 2:
							legalMoves[moveIndex][depth] = "C" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						case 1:
							legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (tempY+1);
							moveIndex++;
							break;
						}
						break;
					}
				}
			}
			
		}
	}
	
	private static void KnightMoves(int x, int y, int depth) {
		if(x > 1 && y < 8) {
			int tempX = x-2;
			int tempY = y+1;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k') {
				switch(x) {
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "A" + (y+2);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "B" + (y+2);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "C" + (y+2);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "D" + (y+2);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (y+2);
					moveIndex++;
					break;
				}
			}
		}
		if(x > 0 && y < 7) {
			int tempX = x-1;
			int tempY = y+2;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k') {
				switch(x) {
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (y+3);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (y+3);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (y+3);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (y+3);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (y+3);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y+3);
					moveIndex++;
					break;
				}
			}
		}
		if(x < 6 && y < 7) {
			int tempX = x+1;
			int tempY = y+2;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y+3);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (y+3);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (y+3);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (y+3);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (y+3);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (y+3);
					moveIndex++;
					break;
				}
			}
		}
		if(x < 5 && y < 8) {
			int tempX = x+2;
			int tempY = y+1;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (y+2);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (y+2);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "E" + (y+2);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "F" + (y+2);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "G" + (y+2);
					moveIndex++;
					break;
				}
			}
		}
		if(x > 1 && y > 0) {
			int tempX = x-2;
			int tempY = y-1;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(x) {
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "A" + (y);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "B" + (y);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "C" + (y);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "D" + (y);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (y);
					moveIndex++;
					break;
				}
			}
		}
		if(x > 0 && y > 1) {
			int tempX = x-1;
			int tempY = y-2;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(x) {
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (y-1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (y-1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (y-1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (y-1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (y-1);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y-1);
					moveIndex++;
					break;
				}
			}
		}
		if(x < 6 && y > 1) {
			int tempX = x+1;
			int tempY = y-2;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y-1);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (y-1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (y-1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (y-1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (y-1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (y-1);
					moveIndex++;
					break;
				}
			}
		}
		if(x < 5 && y > 0) {
			int tempX = x+2;
			int tempY = y-1;
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (y);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "D" + (y);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "E" + (y);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "F" + (y);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "G" + (y);
					moveIndex++;
					break;
				}
			}
		}
	}
}
