import java.io.*;
import java.util.Arrays;


public class AES {

	public static int[][] cryptMatrix = new int[4][4]; 
	public static int[][] roundKey;
	public static int idx = 0; 

	public static int [][] sBox = new int [] []{ 
		{0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
		{0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
		{0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
		{0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
		{0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
		{0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
		{0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
		{0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
		{0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
		{0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
		{0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
		{0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
		{0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
		{0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
		{0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
		{0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}
	};

	final static int[] LogTable = {
		0,   0,  25,   1,  50,   2,  26, 198,  75, 199,  27, 104,  51, 238, 223,   3, 
		100,   4, 224,  14,  52, 141, 129, 239,  76, 113,   8, 200, 248, 105,  28, 193, 
		125, 194,  29, 181, 249, 185,  39, 106,  77, 228, 166, 114, 154, 201,   9, 120, 
		101,  47, 138,   5,  33,  15, 225,  36,  18, 240, 130,  69,  53, 147, 218, 142, 
		150, 143, 219, 189,  54, 208, 206, 148,  19,  92, 210, 241,  64,  70, 131,  56, 
		102, 221, 253,  48, 191,   6, 139,  98, 179,  37, 226, 152,  34, 136, 145,  16, 
		126, 110,  72, 195, 163, 182,  30,  66,  58, 107,  40,  84, 250, 133,  61, 186, 
		43, 121,  10,  21, 155, 159,  94, 202,  78, 212, 172, 229, 243, 115, 167,  87, 
		175,  88, 168,  80, 244, 234, 214, 116,  79, 174, 233, 213, 231, 230, 173, 232, 
		44, 215, 117, 122, 235,  22,  11, 245,  89, 203,  95, 176, 156, 169,  81, 160, 
		127,  12, 246, 111,  23, 196,  73, 236, 216,  67,  31,  45, 164, 118, 123, 183, 
		204, 187,  62,  90, 251,  96, 177, 134,  59,  82, 161, 108, 170,  85,  41, 157, 
		151, 178, 135, 144,  97, 190, 220, 252, 188, 149, 207, 205,  55,  63,  91, 209, 
		83,  57, 132,  60,  65, 162, 109,  71,  20,  42, 158,  93,  86, 242, 211, 171, 
		68,  17, 146, 217,  35,  32,  46, 137, 180, 124, 184,  38, 119, 153, 227, 165, 
		103,  74, 237, 222, 197,  49, 254,  24,  13,  99, 140, 128, 192, 247, 112,   7};

	final static int[] AlogTable = {
		1,   3,   5,  15,  17,  51,  85, 255,  26,  46, 114, 150, 161, 248,  19,  53, 
		95, 225,  56,  72, 216, 115, 149, 164, 247,   2,   6,  10,  30,  34, 102, 170, 
		229,  52,  92, 228,  55,  89, 235,  38, 106, 190, 217, 112, 144, 171, 230,  49, 
		83, 245,   4,  12,  20,  60,  68, 204,  79, 209, 104, 184, 211, 110, 178, 205, 
		76, 212, 103, 169, 224,  59,  77, 215,  98, 166, 241,   8,  24,  40, 120, 136, 
		131, 158, 185, 208, 107, 189, 220, 127, 129, 152, 179, 206,  73, 219, 118, 154, 
		181, 196,  87, 249,  16,  48,  80, 240,  11,  29,  39, 105, 187, 214,  97, 163, 
		254,  25,  43, 125, 135, 146, 173, 236,  47, 113, 147, 174, 233,  32,  96, 160, 
		251,  22,  58,  78, 210, 109, 183, 194,  93, 231,  50,  86, 250,  21,  63,  65, 
		195,  94, 226,  61,  71, 201,  64, 192,  91, 237,  44, 116, 156, 191, 218, 117, 
		159, 186, 213, 100, 172, 239,  42, 126, 130, 157, 188, 223, 122, 142, 137, 128, 
		155, 182, 193,  88, 232,  35, 101, 175, 234,  37, 111, 177, 200,  67, 197,  84, 
		252,  31,  33,  99, 165, 244,   7,   9,  27,  45, 119, 153, 176, 203,  70, 202, 
		69, 207,  74, 222, 121, 139, 134, 145, 168, 227,  62,  66, 198,  81, 243,  14, 
		18,  54,  90, 238,  41, 123, 141, 140, 143, 138, 133, 148, 167, 242,  13,  23, 
		57,  75, 221, 124, 132, 151, 162, 253,  28,  36, 108, 180, 199,  82, 246,   1};


	public static int[][] rcon = {
		{0x01, 00, 00, 00},
		{0x02, 00, 00, 00},
		{0x04, 00, 00, 00},
		{0x08, 00, 00, 00},
		{0x10, 00, 00, 00},
		{0x20, 00, 00, 00},
		{0x40, 00, 00, 00},
		{0x80, 00, 00, 00},
		{0x1b, 00, 00, 00},
		{0x36, 00, 00, 00}
	};
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		cryptMatrix = setUpArray(args[2]); 
		roundKey = setUpArray (args[1]); 
		//java AES option keyFile inputFile
			System.out.println("Original Matrix:"); 
			printMatrix(cryptMatrix);
			System.out.println(); 
			
			System.out.println("Original RoundKey: ");
			printMatrix(roundKey); 
			System.out.println(); 

			int i = 0; 
			while (i < 9) {
				System.out.println( "New Round " +(i + 1)); 
				runRounds();
				i++; 
			} 
	}
	
	private static void runRounds() {
		System.out.println("SubByters Results: " );
		subBytes();
		printMatrix(cryptMatrix);
		System.out.println(); 

		System.out.println("ShiftRows Results: " );
		shiftRows();
		printMatrix(cryptMatrix);
		System.out.println();

		System.out.println("MixColumns Results: " );
		mixColumns(); 
		printMatrix(cryptMatrix); 
		System.out.println(); 

		System.out.println("Add Round Key Results: " );
		addRoundKey(); 
		printMatrix(cryptMatrix); 
		System.out.println();
		
		System.out.println("New RoundKey Generated: "); 
		printMatrix(roundKey); 
		System.out.println(); 
	}
	
	private static int[][] setUpArray(String file) {
		BufferedReader in;
		int [][] making = new int[4][4]; 
		try {
			in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line !=null)
			{	

				int col = 0;
				int row = 0;
				for (int i = 0; line != null && i<line.length(); i=i +2)
				{
					if (col == 4)
					{
						col = 0;
						row++;
					}
					int hex = Integer.decode("0x"+line.substring(i,i+2));

					making[row][col] = hex;
					col++;
				}
				line = in.readLine();
			}
			in.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return making; 
	}

	//print matrix
	//used for debugging purposes
	public static void printMatrix (int[][] matrix) {
		for (int i = 0; i<matrix.length; i++)
		{
			for (int j =0; j<matrix[i].length; j++)
			{
				System.out.print(Integer.toHexString(matrix[i][j])+ " ");
			}
			System.out.println();
		}
	}
	//subBytes
	public static void subBytes(){
		int row = 0;
		int col = 0;
		int firstdigit = 0;
		int lastdigit = 0;
		int parameter = 0;
		while (parameter < (cryptMatrix.length*cryptMatrix[0].length))
		{
			if (col == 4)
			{
				col = 0;
				row++;
			}
			firstdigit = (cryptMatrix[row][col]>>4)&0xF;
			lastdigit = cryptMatrix[row][col]&0xF;

			cryptMatrix[row][col] = sBox[firstdigit][lastdigit];
			col++;
			parameter++;
		}
	}

	//shiftRows
	public static void shiftRows(){

		//temporary variables with number indicating original column
		int temp1=0;
		int temp2=0;
		int temp3=0;
		int temp4=0;

		//first row stays the same
		//second row: 2nd to 1st, 3rd to 2nd, 4th to 3rd, 1st goes to 4th
		temp1=cryptMatrix[1][0];
		temp2=cryptMatrix[1][1];
		temp3=cryptMatrix[1][2];
		temp4=cryptMatrix[1][3];
		cryptMatrix[1][0]= temp2;
		cryptMatrix[1][1]= temp3;
		cryptMatrix[1][2]= temp4;
		cryptMatrix[1][3]= temp1;


		//third row: 3rd to 1st, 4th to 2nd, 1st to 3rd, 2nd to 4th
		temp1=cryptMatrix[2][0];
		temp2=cryptMatrix[2][1];
		temp3=cryptMatrix[2][2];
		temp4=cryptMatrix[2][3];
		cryptMatrix[2][0]= temp3;
		cryptMatrix[2][1]= temp4;
		cryptMatrix[2][2]= temp1;
		cryptMatrix[2][3]= temp2;


		//fourth row: 4th to 1st, 1st to 2nd, 2nd to 3rd, 3rd to 4th
		temp1=cryptMatrix[3][0];
		temp2=cryptMatrix[3][1];
		temp3=cryptMatrix[3][2];
		temp4=cryptMatrix[3][3];
		cryptMatrix[3][0]= temp4;
		cryptMatrix[3][1]= temp1;
		cryptMatrix[3][2]= temp2;
		cryptMatrix[3][3]= temp3;

	}

	//mixColumns
	public static void mixColumns() {
		
		for (int i = 0; i < cryptMatrix.length; i++){
			mixColumn2(i); 
		}
	}

	public static void mixColumn2 (int c) {
		// This is another alternate version of mixColumn, using the 
		// logtables to do the computation.

		int a[] = new int[4];

		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
			a[i] = cryptMatrix[i][c];

		// This is exactly the same as mixColumns1, if 
		// the mul columns somehow match the b columns there.
		cryptMatrix[0][c] = (mul(2,a[0]) ^ a[2] ^ a[3] ^ mul(3,a[1]));
		cryptMatrix[1][c] = (mul(2,a[1]) ^ a[3] ^ a[0] ^ mul(3,a[2]));
		cryptMatrix[2][c] = (mul(2,a[2]) ^ a[0] ^ a[1] ^ mul(3,a[3]));
		cryptMatrix[3][c] = (mul(2,a[3]) ^ a[1] ^ a[2] ^ mul(3,a[0]));
	} // mixColumn2

	public void invMixColumn2 (int c) {
		int a[] = new int[4];

		// note that a is just a copy of st[.][c]
		for (int i = 0; i < 4; i++) 
			a[i] = cryptMatrix[i][c];

		cryptMatrix[0][c] = (mul(0xE,a[0]) ^ mul(0xB,a[1]) ^ mul(0xD, a[2]) ^ mul(0x9,a[3]));
		cryptMatrix[1][c] = (mul(0xE,a[1]) ^ mul(0xB,a[2]) ^ mul(0xD, a[3]) ^ mul(0x9,a[0]));
		cryptMatrix[2][c] = (mul(0xE,a[2]) ^ mul(0xB,a[3]) ^ mul(0xD, a[0]) ^ mul(0x9,a[1]));
		cryptMatrix[3][c] = (mul(0xE,a[3]) ^ mul(0xB,a[0]) ^ mul(0xD, a[1]) ^ mul(0x9,a[2]));
	} // invMixColumn2

	private static int mul (int a, int b) {
		byte b2 = (byte) (b & 0xFF); 
		int inda = (a < 0) ? (a + 256) : a;
		int indb = (b2 < 0) ? (b2 + 256) : b2;

		if ( (a != 0) && (b != 0) ) {
			int index = (LogTable[inda] + LogTable[indb]);
			int val = (AlogTable[ index % 255 ] );
			return val;
		}
		else 
			return 0;
	} // mul

	//addRoundKey
	public static void addRoundKey(){
		updateRoundKey(); 
		for (int i = 0; i < cryptMatrix[0].length; i++){
			for (int j = 0; j < cryptMatrix.length; j++){
				cryptMatrix[j][i] = cryptMatrix[j][i] ^ roundKey[j][i]; 
			} 
		}
		
		

	}
	
	//updates the RoundKey
	public static void updateRoundKey(){
		//word = last column
		//rotate word
		int[] rotWord = rotateWord(); 
		
		//subbytes word
		rotWord = subBytesWord(rotWord);
		
		//XOR first column with word and RCON
		//then XOR next column with result
		int[] rconWord = rcon[idx]; 
		
//		for (int i = 0; i < rconWord.length; i++)
//			System.out.print(Integer.toHexString(rconWord[i])+ " "); 
//		System.out.println(); 
		
		//THIS LOOP IS MESSED UP!!! I MIGHT BE OVERWRITING ROTWORD BEFORE i USED ALL OF IT
		for (int i = 0; i < roundKey[0].length; i++){
			for (int j = 0; j < roundKey.length; j++){
				if (i == 0) roundKey[j][i] = roundKey[j][i] ^ rotWord[j] ^ rconWord[j];
				else roundKey[j][i] = roundKey[j][i] ^ rotWord[j]; 
				rotWord[j] = roundKey[j][i];  
			} 
		}
		 idx++; 
	}
	
	private static int[] subBytesWord(int[] rotWord) {
		int row = 0;

		while (row < (rotWord.length))
		{
			int firstdigit = (rotWord[row]>>4)&0xF;
			int lastdigit = rotWord[row]&0xF;

			rotWord[row] = sBox[firstdigit][lastdigit]; 
			row++; 
		}
		return rotWord;
	}

	private static int[] rotateWord(){
		int[] rotWord = new int[4];
		
		for (int i = 0; i < roundKey.length; i++) {
			if (i <3) rotWord[i] = roundKey[i+1][3]; 
			else rotWord[i] = roundKey[0][3]; 
		}
 
		return rotWord; 
	}
}