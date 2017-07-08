
public class FrequencyTableElem {
	private int entry;
	private int freq;
	private FrequencyTableElem leftchild;
	private FrequencyTableElem rightchild;
	
	public FrequencyTableElem(int entry, int freq){
		this.entry=entry;
		this.freq=freq;
		this.leftchild=null;
		this.rightchild=null;
	}
	
	public FrequencyTableElem getLeftchild() {
		return leftchild;
	}

	public void setLeftchild(FrequencyTableElem leftchild) {
		this.leftchild = leftchild;
	}

	public FrequencyTableElem getRightchild() {
		return rightchild;
	}

	public void setRightchild(FrequencyTableElem rightchild) {
		this.rightchild = rightchild;
	}

	public int getEntry() {
		return entry;
	}

	public void setEntry(int entry) {
		this.entry = entry;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
	
	@Override
	public String toString(){
		return (entry+":"+freq+"::left="+leftchild+"...right="+rightchild);
	}
	
}
