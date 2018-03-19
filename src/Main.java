
public class Main {

	static char[][] board = new char[7][9];
	static int depthCount = 3;
	static String[] moveUndo = new String[100];
	static int undoIndex = 0;
	static String[][] legalMoves;
	static int moveIndex;
	
	public static void main(String[] args) {
		setup();
		printBoard();
		System.out.println("");
		legalMoves = new String[100][depthCount];
		LegalMoves(1, 0);
		PrintMoves(1, 0);
		LegalMoves(0, 0);
		PrintMoves(0, 0);
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
	
	private static void LegalMoves(int player, int depth) {
		for(int x = 0; x < 100; x++) {
			legalMoves[x][depth] = null;
		}
		moveIndex = 0;
		if(player == 1) {
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
					case 'r':
						RookMoves(x, y, depth);
						break;
					case 'k':
						KingMoves(x, y, depth);
						break;
					}
				}
			}
		}
		else {
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					switch(board[x][y]) {
					case 'P':
						ComputerPawnMoves(x, y, depth);
						break;
					case 'B':
						ComputerBishopMoves(x, y, depth);
						break;
					case 'N':
						ComputerKnightMoves(x, y, depth);
						break;
					case 'R':
						ComputerRookMoves(x, y, depth);
						break;
					case 'K':
						ComputerKingMoves(x, y, depth);
						break;
					}
				}
			}
		}
		
	}

	private static void PrintMoves(int player, int depth) {
		if(player == 1) {
			System.out.println("Legal Human Moves");
			for(int x = 0; x < moveIndex; x++) {
				System.out.println(legalMoves[x][depth]);
			}
		}
		else {
			System.out.println("Legal Computer Moves");
			for(int x = 0; x < moveIndex; x++) {
				System.out.println(legalMoves[x][depth]);
			}
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
			else{
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
				if(board[x-1][y+1] != '-' && board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
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
	private static void RookMoves(int x, int y, int depth) {
		int tempX;
		int tempY;
		if(y < 8) {
			tempX = x;
			tempY = y + 1;
			outerloop: while(board[tempX][tempY] == '-') {
				switch(tempX) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (tempY+1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (tempY+1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break outerloop;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (tempY+1);
					moveIndex++;
					break outerloop;
				}
				if(tempY >= 8) {
					break outerloop;
				}
				tempY++;
			}
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 1:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
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
		if(x > 0) {
			tempX = x - 1;
			tempY = y;
			while(board[tempX][tempY] == '-') {
				if(tempX < 1)
					break;
				tempX--;
			}
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 5:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					}
					break;
				case 3:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
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
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
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
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
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
				case 0:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "A" + (tempY+1);
						moveIndex++;
						break;
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
		if(x < 6) {
			tempX = x + 1;
			tempY = y;
			while(board[tempX][tempY] == '-') {
				if(tempX > 5)
					break;
				tempX++;
			}
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 1:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					}
					break;
				case 3:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
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
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
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
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
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
				case 6:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 3:
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
		}
		if(y > 0) {
			tempX = x;
			tempY = y-1;
			while(board[tempX][tempY] == '-') {
				if(tempY < 1)
					break;
				tempY--;
			}
			if(board[tempX][tempY] != 'p' && board[tempX][tempY] != 'b' && board[tempX][tempY] != 'n' && board[tempX][tempY] != 'r' && board[tempX][tempY] != 'k' && board[tempX][tempY] != '-') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (tempY+1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (tempY+1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (tempY+1);
					moveIndex++;
					break;
				}
			}
		}
	}
	
	private static void KingMoves(int x, int y, int depth){
		if(x == 0 && y < 8) {
			if(board[x][y+1] != 'p' && board[x][y+1] != 'b' && board[x][y+1] != 'n' && board[x][y+1] != 'r' && board[x][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+2);
				moveIndex++;
				
			}
			if(board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y+2);
				moveIndex++;
			}
		}
		else if(x == 6 && y < 8) {
			if(board[x][y+1] != 'p' && board[x][y+1] != 'b' && board[x][y+1] != 'n' && board[x][y+1] != 'r' && board[x][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+2);
				moveIndex++;
				
			}
			if(board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y+2);
				moveIndex++;
			}
		}
		else if(y < 8) {
			if(board[x][y+1] != 'p' && board[x][y+1] != 'b' && board[x][y+1] != 'n' && board[x][y+1] != 'r' && board[x][y+1] != 'k') {
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
			if(board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
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
			if(board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
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
	
	private static void ComputerPawnMoves(int x, int y, int depth) {
		if(y > 0) {
			if(x == 0) {
				if(board[x][y-1] == '-') {
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y);
					moveIndex++;
				}
				if(board[x+1][y+1] != '-' && board[x+1][y-1] != 'P' && board[x+1][y-1] != 'B' && board[x+1][y-1] != 'N' && board[x+1][y-1] != 'R' && board[x+1][y-1] != 'K') {
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y);
					moveIndex++;
				}
			}
			else if (x == 6) {
				if(board[x][y-1] == '-') {
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y);
					moveIndex++;
				}
				if(board[x-1][y-1] != '-' && board[x+1][y-1] != 'P' && board[x+1][y-1] != 'B' && board[x+1][y-1] != 'N' && board[x+1][y-1] != 'R' && board[x+1][y-1] != 'K') {
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y);
					moveIndex++;
				}
			}
			else{
				if(board[x][y-1] == '-') {
					switch(x) {
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y);
						moveIndex++;
						break;
					}
				}
				if(board[x+1][y-1] != '-' && board[x+1][y-1] != 'P' && board[x+1][y-1] != 'B' && board[x+1][y-1] != 'N' && board[x+1][y-1] != 'R' && board[x+1][y-1] != 'K') {
					switch(x) {
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (y);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (y);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (y);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (y);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (y);
						moveIndex++;
						break;
					}
				}
				if(board[x-1][y-1] != '-' && board[x-1][y-1] != 'P' && board[x-1][y-1] != 'B' && board[x-1][y-1] != 'N' && board[x-1][y-1] != 'R' && board[x-1][y-1] != 'K') {
					switch(x) {   
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (y);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (y);
						moveIndex++;
						break;
					case 3:
						legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (y);
						moveIndex++;
						break;
					case 4:
						legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (y);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (y);
						moveIndex++;
						break;
					}
				}
			}
		}
	}
	
	private static void ComputerBishopMoves(int x, int y, int depth) {
		if(x == 0) {
			int tempX, tempY;
			if(y > 0) {
				tempX = x + 1;
				tempY = y - 1;
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
					if(tempY <= 0) {
						break outerloop;
					}
					tempX++;
					tempY--;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'P' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
				tempY = y + 1;
				while(board[tempX][tempY] == '-') {
					if(tempY > 7)
						break;
					tempX++;
					tempY++;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
				tempY = y - 1;
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
					
					if(tempY <= 0) {
						break outerloop;
					}
					tempX--;
					tempY--;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
				tempY = y + 1;
				while(board[tempX][tempY] == '-') {
					if(tempY > 7)
						break;
					tempX--;
					tempY++;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
			if(y > 0) {
				tempX = x + 1;
				tempY = y - 1;
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
					if(tempY <= 0 || tempX >= 6) {
						break outerloop;
					}
					tempX++;
					tempY--;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
				tempY = y - 1;
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
					
					if(tempY <= 0 || tempX >= 6) {
						break outerloop;
					}
					tempX--;
					tempY--;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
			if(y < 8) {
				tempX = x + 1;
				tempY = y + 1;
				while(board[tempX][tempY] == '-') {
					if(tempY > 7 || tempX > 5)
						break;
					tempX++;
					tempY++;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
				tempY = y + 1;
				while(board[tempX][tempY] == '-') {
					if(tempY > 7 || tempX < 1)
						break;
					tempX--;
					tempY++;
				}
				if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
	
	private static void ComputerKnightMoves(int x, int y, int depth) {
		if(x > 1 && y > 0) {
			int tempX = x-2;
			int tempY = y-1;
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K') {
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
		if(x > 1 && y < 8) {
			int tempX = x-2;
			int tempY = y+1;
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
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
	}
	
	private static void ComputerRookMoves(int x, int y, int depth){
		int tempX;
		int tempY;
		if(y > 0) {
			tempX = x;
			tempY = y - 1;
			outerloop: while(board[tempX][tempY] == '-') {
				switch(tempX) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (tempY+1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (tempY+1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break outerloop;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (tempY+1);
					moveIndex++;
					break outerloop;
				}
				if(tempY <= 0) {
					break outerloop;
				}
				tempY--;
			}
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 1:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
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
		if(x > 0) {
			tempX = x - 1;
			tempY = y;
			while(board[tempX][tempY] == '-') {
				if(tempX < 1)
					break;
				tempX--;
			}
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 5:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					case 5:
						legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
					}
					break;
				case 3:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
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
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
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
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "B" + (tempY+1);
						moveIndex++;
						break;
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
				case 0:
					switch(x) {
					case 6:
						legalMoves[moveIndex][depth] = "G" + (y+1) + "A" + (tempY+1);
						moveIndex++;
						break;
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
		if(x < 6) {
			tempX = x + 1;
			tempY = y;
			while(board[tempX][tempY] == '-') {
				if(tempX > 5)
					break;
				tempX++;
			}
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
				switch(tempX) {
				case 1:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (tempY+1);
						moveIndex++;
						break;
					}
					break;
				case 3:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "D" + (tempY+1);
						moveIndex++;
						break;
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
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "E" + (tempY+1);
						moveIndex++;
						break;
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
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "F" + (tempY+1);
						moveIndex++;
						break;
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
				case 6:
					switch(x) {
					case 0:
						legalMoves[moveIndex][depth] = "A" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 1:
						legalMoves[moveIndex][depth] = "B" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 2:
						legalMoves[moveIndex][depth] = "C" + (y+1) + "G" + (tempY+1);
						moveIndex++;
						break;
					case 3:
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
		}
		if(y < 8) {
			tempX = x;
			tempY = y+1;
			while(board[tempX][tempY] == '-') {
				if(tempY > 7)
					break;
				tempY++;
			}
			if(board[tempX][tempY] != 'P' && board[tempX][tempY] != 'B' && board[tempX][tempY] != 'N' && board[tempX][tempY] != 'R' && board[tempX][tempY] != 'K' && board[tempX][tempY] != '-') {
				switch(x) {
				case 0:
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (tempY+1);
					moveIndex++;
					break;
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (tempY+1);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (tempY+1);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (tempY+1);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (tempY+1);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (tempY+1);
					moveIndex++;
					break;
				case 6:
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (tempY+1);
					moveIndex++;
					break;
				}
			}
		}
	}
	
	private static void ComputerKingMoves(int x, int y, int depth) {
		if(x == 0 && y > 0) {
			if(board[x][y-1] != 'P' && board[x][y-1] != 'B' && board[x][y-1] != 'N' && board[x][y-1] != 'R' && board[x][y-1] != 'K') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y);
				moveIndex++;
				
			}
			if(board[x+1][y-1] != 'P' && board[x+1][y-1] != 'B' && board[x+1][y-1] != 'N' && board[x+1][y-1] != 'R' && board[x+1][y-1] != 'K') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y);
				moveIndex++;
			}
		}
		else if(x == 6 && y > 0) {
			if(board[x][y-1] != 'P' && board[x][y-1] != 'B' && board[x][y-1] != 'N' && board[x][y-1] != 'R' && board[x][y-1] != 'K') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y);
				moveIndex++;
				
			}
			if(board[x-1][y-1] != 'P' && board[x-1][y-1] != 'B' && board[x-1][y-1] != 'N' && board[x-1][y-1] != 'R' && board[x-1][y-1] != 'K') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y);
				moveIndex++;
			}
		}
		else if(y > 0) {
			if(board[x][y-1] != 'P' && board[x][y-1] != 'B' && board[x][y-1] != 'N' && board[x][y-1] != 'R' && board[x][y-1] != 'K') {
				switch(x) {
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y);
					moveIndex++;
					break;
				}								
			}
			if(board[x+1][y-1] != 'P' && board[x+1][y-1] != 'B' && board[x+1][y-1] != 'N' && board[x+1][y-1] != 'R' && board[x+1][y-1] != 'K') {
				switch(x) {
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "C" + (y);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "D" + (y);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "E" + (y);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "F" + (y);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "G" + (y);
					moveIndex++;
					break;
				}
			}
			if(board[x-1][y-1] != 'P' && board[x-1][y-1] != 'B' && board[x-1][y-1] != 'N' && board[x-1][y-1] != 'R' && board[x-1][y-1] != 'K') {
				switch(x) {
				case 1:
					legalMoves[moveIndex][depth] = "B" + (y+1) + "A" + (y);
					moveIndex++;
					break;
				case 2:
					legalMoves[moveIndex][depth] = "C" + (y+1) + "B" + (y);
					moveIndex++;
					break;
				case 3:
					legalMoves[moveIndex][depth] = "D" + (y+1) + "C" + (y);
					moveIndex++;
					break;
				case 4:
					legalMoves[moveIndex][depth] = "E" + (y+1) + "D" + (y);
					moveIndex++;
					break;
				case 5:
					legalMoves[moveIndex][depth] = "F" + (y+1) + "E" + (y);
					moveIndex++;
					break;
				}
			}
		}
	}
}
