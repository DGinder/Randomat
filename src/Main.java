import java.util.Random;
import java.util.Scanner;

public class Main {

	static char[][] board = new char[7][9];
	static int depthCount = 5;
	static String[] moveUndo = new String[100];
	static int undoIndex = 0;
	static String[][] legalMoves;
	static int moveIndex;
	static Scanner reader = new Scanner(System.in);  // Reading from System.in
	static String finalComputerMove = "";
	
	public static void main(String[] args) {//main class calls everything else
		setup();
	System.out.println(Character.toChars(9812));
		
		System.out.println("Welcome to the RANDOMAT");
		System.out.print("Would you liek to go first or second(1/2): ");
		reader = new Scanner(System.in);  // Reading from System.in
		System.out.println();
		int turn = reader.nextInt(); // Scans the next tokens of the input.
		if(turn == 1) {
			for (;;){
				printBoard();
				GetMove();
				GameOver();
				printBoard();
				MakeComputerMove();
				ClearUndo();
				GameOver();
			} 
		}
		else {
			for (;;){
				printBoard();
				MakeComputerMove();
				GameOver();
				ClearUndo();
				printBoard();
				GetMove();
				GameOver();
			}
		}
	}
	
	private static void GetMove() {//reads human move and plays it on board
		LegalMoves(1, depthCount);//gets human legal moves
		System.out.println();
		System.out.println("Enter a Move: ");//prompt
		String move = reader.next(); // Scans the next tokens of the input.
		System.out.println();
		move = move.toUpperCase();//refactors the move to be more readable
		move = move.trim();
		//once finished
		while(CheckLegality(move) == false) {//checks move legality and if the move is not legal
			System.out.println("Not a legal move");//prompt and read for a new move
			System.out.print("Enter a Move: ");
			System.out.println();
			move = reader.next(); // Scans the next tokens of the input.
			move = move.toUpperCase();
			move = move.trim();
		}
		MakeUndoableMove(move);//makes the move
		
	}
	
	private static void GameOver() {//checks if either player or computer has has lost if both lose at same time ddefaults to computer win
		LegalMoves(1, depthCount);
		if(moveIndex == 0) {//if there are no legal moves for player
			System.out.println("I Win");
			reader.close();
			System.exit(0);
		}
		if(CheckKing(1)) {//if there is no king for player
			System.out.println("I Win");
			reader.close();
			System.exit(0);
		}
		LegalMoves(0, depthCount);//if the computer has nop legal moves
		if(moveIndex == 0) {
			System.out.println("You Win");
			reader.close();
			System.exit(0);
		}
		if(CheckKing(0)) {//if the computer has no king
			System.out.println("You Win");
			reader.close();
			System.exit(0);
		}
	}
	
	private static int CheckWinner() {//same as game over but returns heristic value
		LegalMoves(1, depthCount);
		if(moveIndex == 0) {
			return 5000;
		}
		if(CheckKing(1)) {
			return 5000;
		}
		LegalMoves(0, depthCount);
		if(moveIndex == 0) {
			return -5000;
		}
		if(CheckKing(0)) {
			return -5000;
		}
		return -1;
	}
	
	private static boolean CheckKing(int player) {//boolean to check if a king exists with parameter determining which side
		boolean kingNotPresent = true;
		if(player == 1) {//if checking on human king
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {//check board
					if(board[x][y] == 'k')
						kingNotPresent = false; // if found return false
				}
			}
		}
		else {//if checking on computer king
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {//check board
					if(board[x][y] == 'K')
						kingNotPresent = false;//if found return false
				}
			}
		}
		return kingNotPresent;// if not found return true
	}
	
	private static void MakeComputerMove() {
		AnalyzeMoves();
		System.out.println();
		System.out.println("My Move is: " + finalComputerMove);
		System.out.println();
		MakeUndoableMove(finalComputerMove);
	}
	
	private static void AnalyzeMoves() {
		int best=-20000,  beta = 20000, depth=0,score = -20000;
		String tempMove = null;
		LegalMoves(0, depth);
		int temp = moveIndex;
		for(int i = 0; i < temp; i++) {
			MakeMove(legalMoves[i][depth]);
			score = Min(depth+1, best, beta, i);
			if(score > best) {
				best = score;
				tempMove = legalMoves[i][depth];				 
			}
			UndoMove();
		}
		finalComputerMove = tempMove;
	}
	
	private static int Min(int depth, int alpha, int beta, int baseCount) {
		if (CheckWinner() != -1) return (CheckWinner()+depth);
		if(depth == depthCount) {
			return CalculateHeristic();
		}
		int best=20000,score = 20000;
		LegalMoves(1, depth);
		int temp = moveIndex;
		for(int i = 0; i < temp; i++) {
			MakeMove(legalMoves[i][depth]);
			score = Max(depth+1, alpha, best, i);
			if(score < best) {
				best = score;			 
			}
			UndoMove();
			if(best < alpha) {
				return best;
			}
		}
		return best;
	}
	
	private static int Max(int depth, int alpha, int beta, int baseCount) {
		if (CheckWinner() != -1) return (CheckWinner()+depth);
		if(depth == depthCount) {
			return CalculateHeristic();
		}
		int best=-20000,score = -20000;
		LegalMoves(0, depth);
		int temp = moveIndex;
		for(int i = 0; i < temp; i++) {
			MakeMove(legalMoves[i][depth]);
			score = Min(depth+1, best, beta,baseCount);
			if(score > best) {
				best = score;				 
			}
			UndoMove();
			if(best > beta) {
				return best;
			}
		}
		return best;
	}
	
	private static int CalculateHeristic() {
		int value;
		value = ((3*CalculateMobility()) + (2 * CalculatePieces()) + CalculateCenter());
		return value;
	}
	
	private static int CalculateMobility() {
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					switch(board[x][y]){
						case 'P':
							value += 3;
							break;
						case 'R':
							value += 9;
							break;
						case 'N':
							value += 8;
							break;
						case 'B':
							value += 8;
							break;
						case 'K':
							value += 3;
							break;
						case 'p':
							value -= 3;
							break;
						case 'r':
							value -= 9;
							break;
						case 'n':
							value -= 8;
							break;
						case 'b':
							value -= 8;
							break;
						case 'k':
							value -= 3;
							break;							
					}
				}
				
			}
		}
		return value;
	}
	
	private static int CalculatePieces() {
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					switch(board[x][y]){
						case 'P':
							value += 1;
							break;
						case 'R':
							value += 5;
							break;
						case 'N':
							value += 3;
							break;
						case 'B':
							value += 3;
							break;
						case 'K':
							value += 100;
							break;
						case 'p':
							value -= 1;
							break;
						case 'r':
							value -= 5;
							break;
						case 'n':
							value -= 3;
							break;
						case 'b':
							value -= 3;
							break;
						case 'k':
							value -= 100;
							break;							
					}
				}
				
			}
		}
		return value;
	}
	
	private static int CalculateCenter() {
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					int tempDistance = 0;
					switch(board[x][y]){
						case 'P':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value += (9 - tempDistance);
							break;
						case 'R':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value += (9 - tempDistance);
							break;
						case 'N':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value += (9 - tempDistance);
							break;
						case 'B':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value += (9 - tempDistance);
							break;
						case 'K':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value += (9 - tempDistance);
							break;
						case 'p':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value -= (9 - tempDistance);
							break;
						case 'r':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value -= (9 - tempDistance);
							break;
						case 'n':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value -= (9 - tempDistance);
							break;
						case 'b':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value -= (9 - tempDistance);
							break;
						case 'k':
							tempDistance = Math.abs(3 - x) + Math.abs(4 - y);
							value -= (9 - tempDistance);
							break;							
					}
				}
			}
		}
		return value;
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
		char tempPiece = board[x][y-1];
		if(x != xDes || y != yDes) {
			if(board[xDes][yDes-1] == '-') {
				moveUndo[undoIndex] = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes);
				undoIndex++;
				board[xDes][yDes-1] = board[x][y-1];
				board[x][y-1] = '-';
			}
			else {
				char temp = board[xDes][yDes-1];
				moveUndo[undoIndex] = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes) + temp +  Integer.toString(xDes) + Integer.toString(yDes);
				undoIndex++;
				board[xDes][yDes-1] = board[x][y-1];
				board[x][y-1] = '-';
			}
		}
		else {
			board[x][y-1] = '-';
			if(x == 0 && y-1 == 0) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6 && y-1 == 0) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 0 && y-1 == 8) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x+1][y-2] != '-') {
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6 && y-1 == 8) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 0) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(y-1 == 0) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;

			}
			else if(y-1 == 8) {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;

			}
			else {
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
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
		if(x != xDes || y != yDes) {
			yDes = Character.getNumericValue(move.charAt(3));
			board[xDes][yDes-1] = board[x][y-1];
			board[x][y-1] = '-';
		}
		else {
			board[x][y-1] = '-';
			if(x == 0 && y-1 == 0) {
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}
			}
			else if(x == 6 && y-1 == 0) {
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}
			}
			else if(x == 0 && y-1 == 8) {;
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
				if(board[x+1][y-2] != '-') {
					board[x+1][y-2] = '-';
				}
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}
			}
			else if(x == 6 && y-1 == 8) {
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}
			}
			else if(x == 0) {
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
			}
			else if(x == 6) {
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
			}
			else if(y-1 == 0) {
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}

			}
			else if(y-1 == 8) {
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}

			}
			else {
				if(board[x+1][y-1] != '-') {
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {
					board[x+1][y] = '-';
				}
			}
		}
	}
	
	private static void ClearUndo() {
		for(int i = 0; i < undoIndex; i++) {
			moveUndo[i] = null;
		}
		undoIndex = 0;
	}
	
	private static void UndoMove() {
		undoIndex--;
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;
		if(moveUndo[undoIndex].length() >= 5) {
			char tempPiece = moveUndo[undoIndex].charAt(0);
			x = Character.getNumericValue(moveUndo[undoIndex].charAt(1));
			y = Character.getNumericValue(moveUndo[undoIndex].charAt(2));
			y-=1;
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(3));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(4));
			yDes-=1;
			char temp = board[xDes][yDes];
			board[x][y] = temp;
			board[xDes][yDes] = '-';
			if(board[x][y] != tempPiece) {
				board[x][y] = tempPiece;				
			}
		}
		if(moveUndo[undoIndex].length() >= 8) {
			char tempPiece = moveUndo[undoIndex].charAt(5);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(6));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(7));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 11) {
			char tempPiece = moveUndo[undoIndex].charAt(8);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(9));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(10));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}

		if(moveUndo[undoIndex].length() >= 14) {
			char tempPiece = moveUndo[undoIndex].charAt(11);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(12));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(13));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 17) {
			char tempPiece = moveUndo[undoIndex].charAt(14);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(15));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(16));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 20) {
			char tempPiece = moveUndo[undoIndex].charAt(17);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(18));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(19));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}if(moveUndo[undoIndex].length() >= 23) {
			char tempPiece = moveUndo[undoIndex].charAt(20);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(21));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(22));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}if(moveUndo[undoIndex].length() == 26) {
			char tempPiece = moveUndo[undoIndex].charAt(23);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(24));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(25));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		moveUndo[undoIndex] = null;
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
		
		board[3][8] = 'K';
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
					if(tempY < 1 || tempX < 5)
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
					if(tempY < 1 || tempX < 1)
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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

		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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

		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
					if(tempY <= 0 || tempX > 5) {
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
			if(y > 0) {
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
					
					if(tempY <= 0 || tempX < 1) {
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
			if(y < 8) {
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
		switch(x) {
		case 6:
			legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+1);
			moveIndex++;
			break;
		case 5:
			legalMoves[moveIndex][depth] = "F" + (y+1) + "F" + (y+1);
			moveIndex++;
			break;
		case 4:
			legalMoves[moveIndex][depth] = "E" + (y+1) + "E" + (y+1);
			moveIndex++;
			break;
		case 3:
			legalMoves[moveIndex][depth] = "D" + (y+1) + "D" + (y+1);
			moveIndex++;
			break;
		case 2:
			legalMoves[moveIndex][depth] = "C" + (y+1) + "C" + (y+1);
			moveIndex++;
			break;
		case 1:
			legalMoves[moveIndex][depth] = "B" + (y+1) + "B" + (y+1);
			moveIndex++;
			break;
		case 0:
			legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+1);
			moveIndex++;
			break;
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
