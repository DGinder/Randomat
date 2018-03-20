import java.util.Random;
import java.util.Scanner;

public class Main {

	static char[][] board = new char[7][9];
	static int depthCount = 3;
	static String[] moveUndo = new String[100];
	static int undoIndex = 0;
	static String[][] legalMoves;
	static int moveIndex;
	static Scanner reader = new Scanner(System.in);  // Reading from System.in
	
	public static void main(String[] args) {
		setup();
		System.out.println("Welcome to the RANDOMAT");
		System.out.print("Would you liek to go first or second(1/2): ");
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		System.out.println();
		int turn = reader.nextInt(); // Scans the next tokens of the input.
		if(turn == 1) {
			for (;;){
				printBoard();
				GetMove();
				GameOver();
				printBoard();
				MakeRComputerMove();
				ClearUndo();
				GameOver();
			} 
		}
		else {
			for (;;){
				printBoard();
				MakeRComputerMove();
				GameOver();
				ClearUndo();
				printBoard();
				GetMove();
				GameOver();
			}
		}
	}
	
	private static void GetMove() {
		LegalMoves(1, depthCount);
		System.out.println();
		System.out.println("Enter a Move: ");
		String move = reader.next(); // Scans the next tokens of the input.
		System.out.println();
		move = move.toUpperCase();
		move = move.trim();
		//once finished
		while(CheckLegality(move) == false) {
			System.out.println("Not a legal move");
			System.out.print("Enter a Move: ");
			System.out.println();
			move = reader.next(); // Scans the next tokens of the input.
			move = move.toUpperCase();
			move = move.trim();
		}
		MakeUndoableMove(move);
		
	}
	
	private static void GameOver() {
		LegalMoves(1, depthCount);
		if(moveIndex == 0) {
			System.out.println("I Win");
			reader.close();
			System.exit(0);
		}
		if(CheckKing(1)) {
			System.out.println("I Win");
			reader.close();
			System.exit(0);
		}
		LegalMoves(0, depthCount);
		if(moveIndex == 0) {
			System.out.println("You Win");
			reader.close();
			System.exit(0);
		}
		if(CheckKing(0)) {
			System.out.println("You Win");
			reader.close();
			System.exit(0);
		}
	}
	
	private static boolean CheckKing(int player) {
		boolean kingNotPresent = true;
		if(player == 1) {
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					if(board[x][y] == 'k')
						kingNotPresent = false;
				}
			}
		}
		else {
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					if(board[x][y] == 'K')
						kingNotPresent = false;
				}
			}
		}
		return kingNotPresent;
	}
	private static void MakeRComputerMove() {
		LegalMoves(0, depthCount);
		Random rand = new Random();
		int index = rand.nextInt(moveIndex);
		System.out.println();
		System.out.println("My Move is: " + legalMoves[index][depthCount].toString());
		System.out.println();
		MakeMove(legalMoves[index][depthCount]);
	}
	
	private static boolean CheckLegality(String move){
		boolean legal = false;
		for(int i = 0; i < moveIndex; i++) {
			if(legalMoves[i][depthCount].equals(move))
				legal = true;
		}
		return legal;
	}
	
	private static void MakeMove(String move) {
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;;
		switch (move.charAt(0)) {
		case 'A':
			x = 0;
			break;
		case 'B':
			x = 1;
			break;
		case 'C':
			x = 2;
			break;
		case 'D':
			x = 3;
			break;
		case 'E':
			x = 4;
			break;
		case 'F':
			x = 5;
			break;
		case 'G':
			x = 6;
			break;
		}
		y = Character.getNumericValue(move.charAt(1));
		switch (move.charAt(2)) {
		case 'A':
			xDes = 0;
			break;
		case 'B':
			xDes = 1;
			break;
		case 'C':
			xDes = 2;
			break;
		case 'D':
			xDes = 3;
			break;
		case 'E':
			xDes = 4;
			break;
		case 'F':
			xDes = 5;
			break;
		case 'G':
			xDes = 6;
			break;
		}
		yDes = Character.getNumericValue(move.charAt(3));
		if(board[xDes][yDes-1] == '-') {
			moveUndo[undoIndex] = Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes);
			undoIndex++;
			board[xDes][yDes-1] = board[x][y-1];
			board[x][y-1] = '-';
		}
		else {
			char temp = board[xDes][yDes];
			moveUndo[undoIndex] = Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes) + temp +  Integer.toString(xDes) + Integer.toString(yDes);
			undoIndex++;
			board[xDes][yDes-1] = board[x][y-1];
			board[x][y-1] = '-';
		}
	}
	
	private static void MakeUndoableMove(String move) {
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;;
		switch (move.charAt(0)) {
		case 'A':
			x = 0;
			break;
		case 'B':
			x = 1;
			break;
		case 'C':
			x = 2;
			break;
		case 'D':
			x = 3;
			break;
		case 'E':
			x = 4;
			break;
		case 'F':
			x = 5;
			break;
		case 'G':
			x = 6;
			break;
		}
		y = Character.getNumericValue(move.charAt(1));
		switch (move.charAt(2)) {
		case 'A':
			xDes = 0;
			break;
		case 'B':
			xDes = 1;
			break;
		case 'C':
			xDes = 2;
			break;
		case 'D':
			xDes = 3;
			break;
		case 'E':
			xDes = 4;
			break;
		case 'F':
			xDes = 5;
			break;
		case 'G':
			xDes = 6;
			break;
		}
		yDes = Character.getNumericValue(move.charAt(3));
		board[xDes][yDes-1] = board[x][y-1];
		board[x][y-1] = '-';
	}
	
	private static void ClearUndo() {
		for(int i = 0; i < undoIndex; i++) {
			moveUndo[i] = null;
		}
		undoIndex = 0;
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
		legalMoves = new String[100][depthCount+1];
		
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
				if(board[x-1][y+1] != '-' && board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
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
				if(board[x-1][y-1] != '-' && board[x-1][y-1] != 'P' && board[x-1][y-1] != 'B' && board[x-1][y-1] != 'N' && board[x-1][y-1] != 'R' && board[x-1][y-1] != 'K') {
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
