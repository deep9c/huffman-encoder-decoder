
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class encoder {
	
	public static void main(String args[]){
		try{
		//System.out.println("Starting compression...");
		final String BINARY_FILENAME = "encoded.bin";
		final String CODETABLE_FILENAME = "code_table.txt";
		final String INPUT_FILENAME = args[0];	//input_file1.txt, sample_input_large, inp100mil
		//final String INPUT_FILENAME = "sample_input_large.txt";
		
		HashMap<Integer,FrequencyTableElem> freqTableVector = new HashMap<Integer,FrequencyTableElem>();
		//Vector<FrequencyTableElem> freqTableVector = new Vector<FrequencyTableElem>();
		//Vector<Integer> inputs = new Vector<Integer>();
		Vector<Integer> rawInputs = new Vector<Integer>();
		HashMap<Integer,String> codeTableVector = new HashMap<Integer,String>();
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		try {
			File file = new File(INPUT_FILENAME);
			
			fileReader = new FileReader(file);
			bufferedReader = new BufferedReader(fileReader);			
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if(line.trim().equals("")){
					continue;
				}
				
				int inp = Integer.parseInt(line);
				if(inp<0 || inp>999999){
					throw new NumberFormatException("For input string: "+line);
				}
				rawInputs.add(inp);
				if(!freqTableVector.containsKey(inp)){
					//inputs.add(inp);
					freqTableVector.put(inp,new FrequencyTableElem(inp,1));
				}
				else{
					//int index = inputs.indexOf(inp);
					FrequencyTableElem curelem = freqTableVector.get(inp);										
					
					int newfreq = curelem.getFreq()+1;
					curelem.setFreq(newfreq);
				}
				
			}
		}catch(NumberFormatException e){
			//e.printStackTrace();
			//System.out.println("Inputs should be numbers in the range of 0 and 999999");
			throw new NumberFormatException("Inputs should be numbers in the range of 0 and 999999. \n"+e.getMessage());
		} 
		catch (IOException e) {
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
		//System.out.println("Frequency table generated.");
		
		FrequencyTableElem huffmanTree = null;
		long starttime = System.currentTimeMillis();
		Vector<FrequencyTableElem> freqtableVector = new Vector<FrequencyTableElem>(freqTableVector.values());
		freqtableVector.insertElementAt(new FrequencyTableElem(-1,0), 0);
		freqtableVector.insertElementAt(new FrequencyTableElem(-1,0), 0);
		freqtableVector.insertElementAt(new FrequencyTableElem(-1,0), 0);
		for(int i = 0; i < 1; i++){
			huffmanTree = build_tree_using_binary_heap(freqtableVector);
		}
		long timetaken = System.currentTimeMillis()-starttime;
		//System.out.println("Time taken to build HuffmanTree= "+timetaken);
		
		
		//------------ Min Pairing Heap ---------------------
		//build_tree_using_minpair_heap(freqTableVector, codeTableVector);
		
		
		//comment from here for Min Pairing Heap
		
		if(huffmanTree.getLeftchild()==null && huffmanTree.getRightchild()==null){	//only 1 node
			codeTableVector.put(huffmanTree.getEntry(), "0");			
		}
		else{
			build_code_table(huffmanTree,codeTableVector, new StringBuilder(""));
		}
		
		//Create code_table.txt
		StringBuilder codetabletext = new StringBuilder();
		Iterator it = codeTableVector.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			codetabletext = codetabletext.append(pair.getKey()).append(" ").append(pair.getValue());
			if(it.hasNext()){
				codetabletext = codetabletext.append("\n");
			}
		}

		
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter(CODETABLE_FILENAME);
			bw = new BufferedWriter(fw);
			bw.write(codetabletext.toString().trim());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
				bw.close();
				if (fw != null)
				fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		//System.out.println("Code_table.txt written. ");
		//comment upto here for Min Pairing Heap
		
		
		//Create binary file
		StringBuilder codestringbuilder = new StringBuilder();	
		//File encodedBinFile = new File(BINARY_FILENAME);
		BufferedOutputStream outputAppend = null;		

		try {						
			outputAppend = new BufferedOutputStream(new FileOutputStream(BINARY_FILENAME));
			//System.out.println("Bin file creation started....");
			for(int i=0; i<rawInputs.size(); i++){
				int inp = rawInputs.get(i);			
				codestringbuilder.append(codeTableVector.get(inp));
				int currCodeLen = codestringbuilder.length();								
				
				//Encode starts
				if(codestringbuilder.length()>=8){
					int bitpos = 0;
					int bytecount=0;
					while(bitpos < currCodeLen-7){
						bytecount++;
						byte byteToBeWritten = 0x00;
						for(int ii=0; ii<8 && bitpos+ii<codestringbuilder.length(); ii++){
							byteToBeWritten = (byte) (byteToBeWritten << 1);
							byteToBeWritten += codestringbuilder.charAt(bitpos+ii)=='0'?0x0:0x1;
						}
						outputAppend.write(byteToBeWritten);
						bitpos+=8;		//pos=pos+8												
					}
					codestringbuilder.delete(0,8*bytecount);
				}
				//Encode ends
				
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		finally{
			try {
				if(outputAppend!=null){				
					outputAppend.close();
				} 
			}catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		long timetakencomplete = System.currentTimeMillis()-starttime;		
		//System.out.println("Bin file written. Total time taken= "+timetakencomplete);
		
		/* File file =new File(BINARY_FILENAME);
		long bytes = file.length();
		System.out.println("bytes : " + bytes);		*/
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}	


	private static void build_code_table(FrequencyTableElem huffmanTree, HashMap<Integer,String> codeTableVector, StringBuilder code) {
		
		if(huffmanTree==null){
			return;
		}
		
		if(huffmanTree.getLeftchild()==null && huffmanTree.getRightchild()==null){
			codeTableVector.put(huffmanTree.getEntry(),code.toString());
			//return;
		}
		
		if(huffmanTree!=null){
			build_code_table(huffmanTree.getLeftchild(),codeTableVector,code.append("0"));
			code.deleteCharAt(code.length()-1);
			build_code_table(huffmanTree.getRightchild(),codeTableVector,code.append("1"));
			code.deleteCharAt(code.length()-1);
		}
		
	}

	private static FrequencyTableElem build_tree_using_binary_heap(Vector<FrequencyTableElem> freqTableVector) {
		//BinaryHeap binaryheap = new BinaryHeap(freqTableVector);
		FourWayHeap binaryheap = new FourWayHeap(freqTableVector);
		int initialSize = freqTableVector.size()-3;
		int i=0;
		for( i=0; i<(initialSize-1); i++){			
			FrequencyTableElem min1 = binaryheap.extractMin();
			FrequencyTableElem min2 = binaryheap.extractMin();
			FrequencyTableElem newelem = new FrequencyTableElem(-1,min1.getFreq()+min2.getFreq());
			
			newelem.setLeftchild(min1);
			newelem.setRightchild(min2);
			
			binaryheap.insert(newelem);
		}		
		return binaryheap.extractMin();
	}
	
	
	
}
