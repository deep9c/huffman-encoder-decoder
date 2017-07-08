import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class decoder {

	public static void main(String args[]){
		//System.out.println("Decompression started.......");
		final String BINARY_FILENAME = args[0];		//"encoded.bin";
		final String DECODED_FILENAME = "decoded.txt";
		final String CODETABLE_FILENAME = args[1];		//"code_table.txt";

		//Read code table and create hash table				
		//System.out.println("Reading code table and creating hash table...");

		FrequencyTableElem rootnode = new FrequencyTableElem(-1,0);	//root node
		FrequencyTableElem curnode = null;
		
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(CODETABLE_FILENAME);
			
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);			
			String line;
			
			while ((line = bufferedReader.readLine()) != null) {
				curnode = rootnode;
				
				if(line.trim().equals("")){
					continue;
				}
				
				String[] linesplit = line.split(" ");
				
				//--------- Build code table starts --------------
				String code = linesplit[1];
				String symbol = linesplit[0];
				
				for(int i=0; i<code.length(); i++){
					char c = code.charAt(i);
					if(c=='0'){
						if(curnode.getLeftchild()==null){
							FrequencyTableElem newnode;
							if(i==code.length()-1){	//leaf node
								newnode = new FrequencyTableElem(Integer.parseInt(symbol),0);
							}
							else{
								newnode = new FrequencyTableElem(-1,0);
							}
							
							curnode.setLeftchild(newnode);
							curnode = newnode;
						}
						else{
							curnode = curnode.getLeftchild();
						}
					}
					else{		//got 1
						if(curnode.getRightchild()==null){
							FrequencyTableElem newnode;
							if(i==code.length()-1){	//leaf node
								newnode = new FrequencyTableElem(Integer.parseInt(symbol),0);
							}
							else{
								newnode = new FrequencyTableElem(-1,0);
							}
							
							curnode.setRightchild(newnode);
							curnode = newnode;
						}
						else{
							curnode = curnode.getRightchild();
						}
					}
				}
				
				//--------- Build code table ends --------------								
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{			
				try {
					if(fileReader!=null)
						fileReader.close();
					if(bufferedReader!=null)
						bufferedReader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		//System.out.println("Tree created.");				
		
		
		//Printtree
		//printtree(rootnode);
		
		
		FileInputStream fis =null;
		byte[] bytesArray =null;
		//long binfilesize;
		try {
			File file = new File(BINARY_FILENAME);
			
			//init array with file length
			bytesArray = new byte[(int) file.length()];

			fis = new FileInputStream(file);
			//binfilesize = file.length()*8;
			fis.read(bytesArray); //read file into bytes[]
			fis.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println("Bin file read.");
		
		StringBuilder compressedcode = new StringBuilder("");
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(DECODED_FILENAME);//,false);	
			bw = new BufferedWriter(fw);
			//fw.write("");
			
			
			for (byte b : bytesArray) {
				compressedcode.append(Integer.toBinaryString(b & 255 | 256).substring(1));	
							
				curnode = rootnode;
				int codelen = compressedcode.length();
				int deleteupto=-1;
				for(int i=0; i<codelen;i++){
					if(compressedcode.charAt(i)=='0'){
						curnode=curnode.getLeftchild();
						if(curnode.getLeftchild()==null && curnode.getRightchild()==null){	//leaf node
							bw.write(curnode.getEntry()+"\n");
							deleteupto=i;
							curnode=rootnode;
						}
					}
					else{
						curnode=curnode.getRightchild();
						if(curnode.getLeftchild()==null && curnode.getRightchild()==null){	//leaf node
							bw.write(curnode.getEntry()+"\n");
							deleteupto=i;
							curnode=rootnode;
						}
					}
					
				}
				compressedcode.delete(0, deleteupto+1);
				//bw.write("\n");
			}
			//System.out.println("compressedcode= "+compressedcode);						
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(bw!=null)
					bw.flush();
				if(fw!=null)
					fw.flush();
				if(bw!=null)
					bw.close();
				if(fw!=null)
					fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//validateoutputfile(DECODED_FILENAME, "sample_input_large.txt");
				
	}

}
