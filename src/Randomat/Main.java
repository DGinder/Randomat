package Randomat;
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
	static boolean gameOver = false;
	
	public static void main(String[] args) {//main class calls everything else
		Setup();
		
		System.out.println("Welcome to the RANDOMAT");
		reader = new Scanner(System.in);  // Reading from System.in
		System.out.print("Select Difficulty(Higher difficulties = longer wiat times): ");
		int difficulty = reader.nextInt(); // Scans the next tokens of the input.
		depthCount = difficulty;
		  //Reading from System.in
		System.out.print("Would you like to go first or second? ");
		int turn = reader.nextInt(); // Scans the next tokens of the input.
		System.out.println();
		if(turn == 1) {//loop for if player first
			for (;;){
				PrintBoard();//print board
				GetMove();//get player move
				GameOver(1);//check game over
				if(gameOver == true)//if game over break
					break;
				PrintBoard();//print new board
				MakeComputerMove();//computer makes move
				GameOver(0);//check game over
				if(gameOver == true)//if game over break ot of loop
					break;
			} 
		}
		else {//if player second
			for (;;){//dame as above but computer first
				PrintBoard();
				MakeComputerMove();
				GameOver(0);
				if(gameOver == true)
					break;
				PrintBoard();
				GetMove();
				GameOver(1);
				if(gameOver == true)
					break;
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
	
	private static void GameOver(int side) {//checks if either player or computer has has lost if both lose at same time 
		LegalMoves(1, depthCount);
		if(moveIndex == 0) {//if there are no legal moves for player
			System.out.println("I win because you cannot move");
			System.out.println();
			PrintBoard();
			reader.close();
			gameOver = true;
		}
		if(CheckKing(1)) {//if there is no king for player
			System.out.println("I win because your King was killed");
			System.out.println();
			PrintBoard();
			reader.close();
			gameOver = true;
		}
		LegalMoves(0, depthCount);//if the computer has nop legal moves
		if(moveIndex == 0) {
			System.out.println("You Win because I cannot move");
			System.out.println();
			PrintBoard();
			reader.close();
			gameOver = true;
		}
		if(CheckKing(0)) {//if the computer has no king
			System.out.println("You win Because my King was killed");
			System.out.println();
			PrintBoard();
			reader.close();
			gameOver = true;
		}
		if(CheckKing(1) && CheckKing(0)) {//if both kings die player wins
			if(side == 0) {
				System.out.println("You Win because I caused both our Kings to die");
				System.out.println();
				PrintBoard();
				reader.close();
				gameOver = true;
			}
			else {
				System.out.println("I Win because you caused both our Kings to die");
				System.out.println();
				PrintBoard();
				reader.close();
			}
		}

	}
	
	private static int CheckWinner(int player) {//same as game over but returns heristic value

		LegalMoves(1, depthCount);
		if(moveIndex == 0) {//check for any legal moves
			return 5000;
		}
		if(CheckKing(1) && CheckKing(0) == false) {//check if computer king died but ot players
			return 5000;
		}
		LegalMoves(0, depthCount);//check for computer moves
		if(moveIndex == 0) {
			return -5000;
		}
		if(CheckKing(0) && CheckKing(1) == false) {//check if comptuer king lives but not player
			return -5000;
		}
		if(CheckKing(1) && CheckKing(0)) {//if both dead computer victory

			if(player == 1) {//if players turn
				return 5000;
			}
			else {
				return -5000;
			}
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
	
	private static void MakeComputerMove() {//make computer move
		AnalyzeMoves();//analyse which move is best with min max
		if(finalComputerMove != null) {
			finalComputerMove = finalComputerMove + " / For Tournament: " + MoveConversion(finalComputerMove);// add to end of string the corrospnding player move
			System.out.println();
			System.out.println("My Move is: " + finalComputerMove);//print out move
			System.out.println();
			MakeUndoableMove(finalComputerMove);//make move that cannot be undone
		}
	}
	
	private static void AnalyzeMoves() {//max start node for minmax
		int best=-20000,  beta = 20000, depth=0,score = -20000;///starter values
		String tempMove = null;//move holder
		LegalMoves(0, depth);//Calculate moves
		int temp = moveIndex;//save current total
		for(int i = 0; i < temp; i++) {//repeat for all moves
			MakeMove(legalMoves[i][depth]);//make undoable move
			score = Min(depth+1, best, beta);//calcualte score by calling min
			if(score > best) {//if score better 
				best = score;//change score
				tempMove = legalMoves[i][depth];	//save move			 
			}
			UndoMove();//undo move
		}
		finalComputerMove = tempMove;//save move to final move
	}
	
	private static String MoveConversion(String move) {//converts move to player perspective
		String temp = "";//hoder string
		switch(move.charAt(0)) {//first space depending on value switches and adds to holder string
		case 'A':
			temp += "G";
			break;
		case 'B':
			temp += "F";
			break;
		case 'C':
			temp += "E";
			break;
		case 'D':
			temp += "D";
			break;
		case 'E':
			temp += "C";
			break;
		case 'F':
			temp += "B";
			break;
		case 'G':
			temp += "A";
			break;
		}
		switch(move.charAt(1)) {//second space
		case '9':
			temp += "1";
			break;
		case '8':
			temp += "2";
			break;
		case '7':
			temp += "3";
			break;
		case '6':
			temp += "4";
			break;
		case '5':
			temp += "5";
			break;
		case '4':
			temp += "6";
			break;
		case '3':
			temp += "7";
			break;
		case '2':
			temp += "8";
			break;
		case '1':
			temp += "9";
			break;
		}
		switch(move.charAt(2)) {//third space
		case 'A':
			temp += "G";
			break;
		case 'B':
			temp += "F";
			break;
		case 'C':
			temp += "E";
			break;
		case 'D':
			temp += "D";
			break;
		case 'E':
			temp += "C";
			break;
		case 'F':
			temp += "B";
			break;
		case 'G':
			temp += "A";
			break;
		}
		switch(move.charAt(3)) {//fourth space
		case '9':
			temp += "1";
			break;
		case '8':
			temp += "2";
			break;
		case '7':
			temp += "3";
			break;
		case '6':
			temp += "4";
			break;
		case '5':
			temp += "5";
			break;
		case '4':
			temp += "6";
			break;
		case '3':
			temp += "7";
			break;
		case '2':
			temp += "8";
			break;
		case '1':
			temp += "9";
			break;
		}
		return temp;//return converted move
	}
	
	private static int Min(int depth, int alpha, int beta) {//min value
		if (CheckWinner(1) != -1) return (CheckWinner(1)+depth);//if winner return 5000/-5000 + depth depending on board
		if(depth == depthCount) {//if at depth count
			return CalculateHeristic();//calcualte heristic
		}
		int best=20000,score = 20000;//esle set variables
		LegalMoves(1, depth);//calculate legal moves
		int temp = moveIndex;//save max moves
		for(int i = 0; i < temp; i++) {//for each move
			MakeMove(legalMoves[i][depth]);//make move
			score = Max(depth+1, alpha, best);//calcualte score by calling max
			if(score < best) {//if score better for player 
				best = score;			 //copy score
			}
			UndoMove();//undo move
			if(best < alpha) {//alpha beta pruning
				return best;
			}
		}
		return best;//return best score to max parent
	}
	
	private static int Max(int depth, int alpha, int beta) {//max value
		if (CheckWinner(0) != -1) return (CheckWinner(0)-depth);//if winner return 5000/-5000-depth
		if(depth == depthCount) {//if at depth count calculate heristic
			return CalculateHeristic();
		}
		int best=-20000,score = -20000;//starter variables
		LegalMoves(0, depth);//calculate legal moves
		int temp = moveIndex;//save max move index
		for(int i = 0; i < temp; i++) {//for each move
			MakeMove(legalMoves[i][depth]);//make move
			score = Min(depth+1, best, beta);//calculate score by calling min
			if(score > best) {//if score better for computer
				best = score;			//copy score	 
			}
			UndoMove();//undo move
			if(best > beta) {//alpha beta pruning
				return best;
			}
		}
		return best;//return best score to min parent
	}
	
	private static int CalculateHeristic() {//calculate heristic
		int value;
		value = ((3*CalculateMobility()) + (2 * CalculatePieces()) + CalculateCenter());//weight heavily for mobility, then piece value, then control of center
		return value;
	}
	
	private static int CalculateMobility() {//mobility heristic
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					switch(board[x][y]){//for each piece add total possible moves it can ever make
						case 'P'://for computer pieces
							value += 3;
							break;
						case 'R':
							value += 8;
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
						case 'p'://for player pieces
							value -= 3;
							break;
						case 'r':
							value -= 8;
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
		return value;//return final value
	}
	
	private static int CalculatePieces() {//calculate by piece value
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					switch(board[x][y]){//for each piece add weight
						case 'P'://for computer pieces
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
						case 'p'://for player pieces
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
		return value;//return final value
	}
	
	private static int CalculateCenter() {//center heristic
		int value = 0;
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {
				if(board[x][y] == '-') ;
				else {
					int tempDistance = 0;
					switch(board[x][y]){//for each piec the father it is from the center the less it is worth
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
		return value;//return final value
	}
	
	private static boolean CheckLegality(String move){//check if move is legal
		boolean legal = false;
		for(int i = 0; i < moveIndex; i++) {
			if(legalMoves[i][depthCount].equals(move))//cehck legal move array for move
				legal = true;//if found return true
		}
		return legal;//else return false
	}
	
	private static void MakeMove(String move) {//make undoable move
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;;
		switch (move.charAt(0)) {//get chars at positions 1, 2, 3, & 4
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
		char tempPiece = board[x][y-1];//copy pieve
		if(x != xDes || y != yDes) {//if not explosion see if piece taken
			if(board[xDes][yDes-1] == '-') {//if not
				moveUndo[undoIndex] = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes);//move piece and save to undo array
				undoIndex++;
				board[xDes][yDes-1] = board[x][y-1];
				board[x][y-1] = '-';
			}
			else {//if yes then move piece and copy both pieces to undo array
				char temp = board[xDes][yDes-1];
				moveUndo[undoIndex] = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(xDes) + Integer.toString(yDes) + temp +  Integer.toString(xDes) + Integer.toString(yDes);
				undoIndex++;
				board[xDes][yDes-1] = board[x][y-1];
				board[x][y-1] = '-';
			}
		}
		else {//if explosion depending on position copy move and pieces destroyed in corresponding places
			board[x][y-1] = '-';
			if(x == 0 && y-1 == 0) {//if bottom left corner
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {//top
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {//top right
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {//right
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6 && y-1 == 0) {//lower right corner
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {//top
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {//top left
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {//left
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 0 && y-1 == 8) {//upper left corner
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y-2] != '-') {//bottom
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x+1][y-2] != '-') {//bottom right
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x+1][y-1] != '-') {//right
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6 && y-1 == 8) {//upper right corner
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y-2] != '-') {//bottom
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {//bottom left
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {//left
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 0) {//if left column
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {//top
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {//top right
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {//right
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {//bottom right
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {//bottom
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(x == 6) {//right column
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x][y] != '-') {
					char temp = board[x][y];//top
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x-1][y] != '-') {
					char temp = board[x-1][y];//top left
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x-1][y-1] != '-') {
					char temp = board[x-1][y-1];//left
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {
					char temp = board[x-1][y-2];//bottom left
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {
					char temp = board[x][y-2];//bottom
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
			else if(y-1 == 0) {//bottom row
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x-1][y-1] != '-') {//left
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {//upper left
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {//top
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {//upper right
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				if(board[x+1][y-1] != '-') {//right
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;

			}
			else if(y-1 == 8) {//top roe
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x+1][y-1] != '-') {
					char temp = board[x+1][y-1];//right
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x-1][y-2] != '-') {//bottom right
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {//bottom
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {//bottom left
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {//left
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;

			}
			else {//any other place
				String tempString = tempPiece + Integer.toString(x) + Integer.toString(y) + Integer.toString(x) + Integer.toString(y);
				if(board[x+1][y-1] != '-') {//right
					char temp = board[x+1][y-1];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y);
					board[x+1][y-1] = '-';
				}
				if(board[x+1][y-2] != '-') {//bottom right
					char temp = board[x+1][y-2];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y-1);
					board[x+1][y-2] = '-';
				}
				if(board[x][y-2] != '-') {//bottom
					char temp = board[x][y-2];
					tempString += temp + Integer.toString(x) + Integer.toString(y-1);
					board[x][y-2] = '-';
				}
				if(board[x-1][y-2] != '-') {//bottom left
					char temp = board[x-1][y-2];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y-1);
					board[x-1][y-2] = '-';
				}
				if(board[x-1][y-1] != '-') {//left
					char temp = board[x-1][y-1];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y);
					board[x-1][y-1] = '-';
				}
				if(board[x-1][y] != '-') {//upper left
					char temp = board[x-1][y];
					tempString += temp + Integer.toString(x-1) + Integer.toString(y+1);
					board[x-1][y] = '-';
				}
				if(board[x][y] != '-') {//top
					char temp = board[x][y];
					tempString += temp + Integer.toString(x) + Integer.toString(y+1);
					board[x][y] = '-';
				}
				if(board[x+1][y] != '-') {//upper right
					char temp = board[x+1][y];
					tempString += temp + Integer.toString(x+1) + Integer.toString(y+1);
					board[x+1][y] = '-';
				}
				moveUndo[undoIndex] = tempString;
				undoIndex++;
			}
		}
	}
	
	private static void MakeUndoableMove(String move) {//nmake move that doesn;t add to m=undo array
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;;
		switch (move.charAt(0)) {//check from and to characters
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
		if(x != xDes || y != yDes) {//if not explosion
			board[xDes][yDes-1] = board[x][y-1];//move piece
			board[x][y-1] = '-';
		}
		else {//if exposion destroy piece and surrounding pieces
			board[x][y-1] = '-';
			if(x == 0 && y-1 == 0) {//corners
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
			else if(x == 0) {//edges
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
			else {//middle area
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
	
	private static void UndoMove() {//undo move
		undoIndex--;//move index down
		int x = 0;
		int y = 0;
		int xDes = 0;
		int yDes = 0;
		if(moveUndo[undoIndex].length() >= 5) {//move recorded piece back
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
		if(moveUndo[undoIndex].length() >= 8) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(5);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(6));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(7));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 11) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(8);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(9));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(10));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}

		if(moveUndo[undoIndex].length() >= 14) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(11);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(12));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(13));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 17) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(14);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(15));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(16));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		if(moveUndo[undoIndex].length() >= 20) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(17);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(18));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(19));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}if(moveUndo[undoIndex].length() >= 23) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(20);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(21));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(22));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}if(moveUndo[undoIndex].length() == 26) {//return taken/destroyed pieces back to board
			char tempPiece = moveUndo[undoIndex].charAt(23);
			xDes = Character.getNumericValue(moveUndo[undoIndex].charAt(24));
			yDes = Character.getNumericValue(moveUndo[undoIndex].charAt(25));
			yDes-=1;
			board[xDes][yDes] = tempPiece;
		}
		moveUndo[undoIndex] = null;//set current undo to null
	}
	
	

	private static void Setup() {//setup board
		for(int y = 0; y < 9; y++) {
			for(int x = 0; x < 7; x++) {//fill board with the "blank" spaces
				board[x][y] = '-';
			}
		}
		
		board[1][0] = 'n';//fill board with pieces
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
	
	private static void PrintBoard() {//print board method
		for(int y = 8; y >= 0; y--) {
			System.out.print(y+1 + "  ");//print row number
			for(int x = 0; x < 7; x++) {
				System.out.print(board[x][y] + " ");
			}
			System.out.println("");
		}
		
		System.out.println("   A B C D E F G");//print column labels
	}
	
	private static void LegalMoves(int player, int depth) {//legal move generator
		moveIndex = 0;//reset index
		if(player == 1) {//if checking for player
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					switch(board[x][y]) {//if piece found
					case 'p'://pawn
						PawnMoves(x, y, depth);
						break;
					case 'b'://bishop
						BishopMoves(x, y, depth);
						break;
					case 'n'://knight
						KnightMoves(x, y, depth);
						break;
					case 'r'://rook
						RookMoves(x, y, depth);
						break;
					case 'k'://king
						KingMoves(x, y, depth);
						break;
					}
				}
			}
		}
		else {//if checking for computer
			for(int y = 0; y < 9; y++) {
				for(int x = 0; x < 7; x++) {
					switch(board[x][y]) {//if piece found
					case 'P'://pawn
						ComputerPawnMoves(x, y, depth);
						break;
					case 'B'://bishop
						ComputerBishopMoves(x, y, depth);
						break;
					case 'N'://knight
						ComputerKnightMoves(x, y, depth);
						break;
					case 'R'://rook
						ComputerRookMoves(x, y, depth);
						break;
					case 'K'://king
						ComputerKingMoves(x, y, depth);
						break;
					}
				}
			}
		}
		
	}
	
	private static void PawnMoves(int x, int y, int depth) {//Calculate player legal pawn moves only move up normally and capture diagonally
		if(y < 8) {
			if(x == 0) {//if at left column 
				if(board[x][y+1] == '-') {//check square in from
					legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+2);
					moveIndex++;
				}//check up right
				if(board[x+1][y+1] != '-' && board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
					legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y+2);
					moveIndex++;
				}
			}
			else if (x == 6) {//if right column
				if(board[x][y+1] == '-') {//top square
					legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+2);
					moveIndex++;
				}//top left square
				if(board[x-1][y+1] != '-' && board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
					legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y+2);
					moveIndex++;
				}
			}
			else{//if in middle
				if(board[x][y+1] == '-') {//check top square
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
				}//check top right square
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
				}//check top left square
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
		switch(x) {//explosion move
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
	
	private static void BishopMoves(int x, int y, int depth) {//player bishop moes
		if(x == 0) {//if left column
			int tempX, tempY;
			if(y < 8) {
				tempX = x + 1;//Continuously check if up right is '-' if so all to array and continue
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
				}//Afterwards check if enemy piece if soo add to legal moves
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
			if(y > 0) {//check backwards
				tempX = x + 1;
				tempY = y - 1;
				while(board[tempX][tempY] == '-') {//keep skipping along if '-'
					if(tempY < 1 || tempX < 5)
						break;
					tempX++;
					tempY--;
				}//afterwards checks if not a ally piece if so add square to move array
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
		else if( x == 6) {//if right column
			int tempX, tempY;
			if(y < 8) {//check up and left like above up and right
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
			}//check back and left like above
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
		else {//if in middle
			int tempX, tempY;//check both directions
			if(y < 8) {
				tempX = x + 1;
				tempY = y + 1;
				outerloop: while(board[tempX][tempY] == '-') {
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
		switch(x) {//explosion move
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
	
	private static void KnightMoves(int x, int y, int depth) {//player knight moves
		if(x > 1 && y < 8) {//if can move left 2 and up one
			int tempX = x-2;
			int tempY = y+1;//chekc locations for enemy piece or empty
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
		if(x > 0 && y < 7) {//left one up 2
			int tempX = x-1;
			int tempY = y+2;//check if empty or enemy peice
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
		if(x < 6 && y < 7) {//right 1 and up two
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
		if(x < 5 && y < 8) {//right 2 and up one
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
		}//nect moves check if enemy piece not empty
		if(x > 1 && y > 0) {//left 2 and down 1
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
		if(x > 0 && y > 1) {//left 1 and down 2
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
		if(x < 6 && y > 1) {//right one and down 2
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
		if(x < 5 && y > 0) {//right 2 and down 1
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
		switch(x) {//explosion move
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
	private static void RookMoves(int x, int y, int depth) {//player rook moves
		int tempX;
		int tempY;
		if(y < 8) {//if can move up
			tempX = x;
			tempY = y + 1;
			outerloop: while(board[tempX][tempY] == '-') {//loop until edge or peice found
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
			}//if enemy piece add its square to legal moes
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
		if(x > 0) {//move left only if enemy piece there
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
		if(x < 6) {//move right only if enemy piece their
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
		if(y > 0) {//backwards only if enemy piece there
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

		switch(x) {//explosion move
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
	
	private static void KingMoves(int x, int y, int depth){//king moves forward or diagonal forward
		if(x == 0 && y < 8) {//if left colum only check foward and right
			if(board[x][y+1] != 'p' && board[x][y+1] != 'b' && board[x][y+1] != 'n' && board[x][y+1] != 'r' && board[x][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "A" + (y+2);
				moveIndex++;
				
			}
			if(board[x+1][y+1] != 'p' && board[x+1][y+1] != 'b' && board[x+1][y+1] != 'n' && board[x+1][y+1] != 'r' && board[x+1][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "A" + (y+1) + "B" + (y+2);
				moveIndex++;
			}
		}
		else if(x == 6 && y < 8) {//left column only check foeard left
			if(board[x][y+1] != 'p' && board[x][y+1] != 'b' && board[x][y+1] != 'n' && board[x][y+1] != 'r' && board[x][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "G" + (y+2);
				moveIndex++;
				
			}
			if(board[x-1][y+1] != 'p' && board[x-1][y+1] != 'b' && board[x-1][y+1] != 'n' && board[x-1][y+1] != 'r' && board[x-1][y+1] != 'k') {
				legalMoves[moveIndex][depth] = "G" + (y+1) + "F" + (y+2);
				moveIndex++;
			}
		}
		else if(y < 8) {//else do al three
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
			}//if it is not a ally piece for each move add to array
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
	
	private static void ComputerPawnMoves(int x, int y, int depth) {//computer pawn moes mirror of player pawn
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
	
	private static void ComputerBishopMoves(int x, int y, int depth) {//computer bishop, mirror of player bishop
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
	
	private static void ComputerKnightMoves(int x, int y, int depth) {//computer knight, mirror of player
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
	
	private static void ComputerRookMoves(int x, int y, int depth){//computer rook, mirror of player rook
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
	
	private static void ComputerKingMoves(int x, int y, int depth) {//computer king mirror player king movements
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
