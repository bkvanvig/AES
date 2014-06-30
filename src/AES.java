import java.io.*;


public class AES {

	public static int[][] cryptMatrix = new int [4][4]; 
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//java AES option keyFile inputFile
		BufferedReader in;
		try {
			in = new BufferedReader(new FileReader(args[2]));
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
					//System.out.println("length: "+line.length() + " i "+i);
					int hex = Integer.decode("0x"+line.substring(i,i+2));
					
					cryptMatrix[row][col] = hex;
					col++;
				}
				line = in.readLine();
				
			}
			for (int i = 0; i<cryptMatrix.length; i++)
			{
				for (int j =0; j<cryptMatrix[i].length; j++)
				{
					System.out.print(cryptMatrix[i][j] + " ");
				}
				System.out.println();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//Find out which option is given
		String option = args[0];
		if (option.equals("e"))
		{
			
		}
		else if (option.equals("d"))
		{
			
		}
		else
		{
			System.out.println("Invalid input.");
			System.exit(1);
		}
		
		//Read in keyFile
		//Convert to Binary
		//Encrypt
		//Store as hex string to output file
		
		
	}

	//subBytes
	
	
	//shiftRows
	
	//mixColumns
	
	//addRoundKey
	
}
