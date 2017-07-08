import java.util.Vector;

public class FourWayHeap {

    private Vector<FrequencyTableElem> list;

    /*public MinHeap() {

        this.list = new Vector<FrequencyTableElem>();
    }*/

    public FourWayHeap(Vector<FrequencyTableElem> items) {

        this.list = items;
        buildHeap();
    }

    public void insert(FrequencyTableElem item) {

        list.add(item);
        int i = list.size() - 1;
        int parent = parent(i);

        while (parent != i && list.get(i).getFreq() < list.get(parent).getFreq() && parent>=3) {

            swap(i, parent);
            i = parent;
            parent = parent(i);
            
            
        }
        
        /*System.out.println("**** 4way Heap insert *****");
		for(FrequencyTableElem ff:list){
			System.out.print(".."+ff.getEntry()+":"+ff.getFreq()+"..");
		}*/
    }

    public void buildHeap() {
    	int start = ((list.size()-4)/4)+3;
        for (int i = start; i >= 3; i--) {
            minHeapify(i);
        }
        
        /*System.out.println("**** 4way Heap *****");
		for(FrequencyTableElem ff:list){
			System.out.print(".."+ff.getEntry()+":"+ff.getFreq()+"..");
		}*/
        
    }

    public FrequencyTableElem extractMin() {

        if (list.size() == 3) {

            throw new IllegalStateException("MinHeap is EMPTY");
        } else if (list.size() == 4) {

        	FrequencyTableElem min = list.remove(3);
            return min;
        }

        // remove the last item ,and set it as new root
        FrequencyTableElem min = list.get(3);
        FrequencyTableElem lastItem = list.remove(list.size() - 1);
        list.set(3, lastItem);

        // bubble-down until heap property is maintained
        minHeapify(3);

        /*System.out.println("**** 4way Heap extract *****");
		for(FrequencyTableElem ff:list){
			System.out.print(".."+ff.getEntry()+":"+ff.getFreq()+"..");
		}*/
        
        
        // return min key
        return min;
    }

   /* public void decreaseKey(int i, int key) {

        if (list.get(i) < key) {

            throw new IllegalArgumentException("Key is larger than the original key");
        }

        list.set(i, key);
        int parent = parent(i);

        // bubble-up until heap property is maintained
        while (i > 0 && list.get(parent) > list.get(i)) {

            swap(i, parent);
            i = parent;
            parent = parent(parent);
        }
    } */

    private void minHeapify(int i) {

    	if(i<3){
    		return;
    	}
    	
        int left = left(i);
        int mid1 = mid1(i);
        int mid2 = mid2(i);
        int right = right(i);
        int smallest = -1;

        // find the smallest key between current node and its children.
        if (left <= list.size() - 1 && list.get(left).getFreq() < list.get(i).getFreq()) {
            smallest = left;
        } else {
            smallest = i;
        }

        if (mid1 <= list.size() - 1 && list.get(mid1).getFreq() < list.get(smallest).getFreq()) {
            smallest = mid1;
        }
        if (mid2 <= list.size() - 1 && list.get(mid2).getFreq() < list.get(smallest).getFreq()) {
            smallest = mid2;
        }
        if (right <= list.size() - 1 && list.get(right).getFreq() < list.get(smallest).getFreq()) {
            smallest = right;
        }

        // if the smallest key is not the current key then bubble-down it.
        if (smallest != i) {

            swap(i, smallest);
            minHeapify(smallest);
        }
    }

    public FrequencyTableElem getMin() {

        return list.get(3);
    }

    public boolean isEmpty() {

        return list.size() == 3;
    }

    

    private int left(int i) {

        return 4 * i - 8;
    }
    
    private int mid1(int i) {

        return 4 * i - 7;
    }
    
    private int mid2(int i) {

        return 4 * i - 6;
    }
    
    private int right(int i) {

        return 4 * i - 5;
    }

    private int parent(int i) {		//returns index of parent of index i

        if ((i-3) % 4 == 1) {
            return ((i-3) / 4)+3;
        }

        return ((i - 4) / 4)+3;
    }

    private void swap(int i, int parent) {

        FrequencyTableElem temp = list.get(parent);
        list.set(parent, list.get(i));
        list.set(i, temp);
    }

}