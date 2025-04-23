// Time Complexity : O(1) for all the methods.
// Space Complexity : O(n) for storing all the numbers in either queue or set.
// Did this code successfully run on Leetcode : yes
// Any problem you faced while coding this : no

import java.util.*;
class PhoneDirectory {
    Set<Integer> unAvailable = new HashSet<>();
    Queue<Integer> que = new LinkedList<>();
    public PhoneDirectory(int maxNumbers) {
        for(int i=0; i<maxNumbers; i++)
            que.offer(i);
    }
    
    public int get() {
        if(que.isEmpty()) return -1;
        int num = que.poll();
        unAvailable.add(num);
        return num;
    }
    
    public boolean check(int number) {
        return !unAvailable.contains(number);
    }
    
    public void release(int number) {
        if(!unAvailable.contains(number)) return;
        unAvailable.remove(number);
        que.offer(number);
    }
}

/**
 * Your PhoneDirectory object will be instantiated and called as such:
 * PhoneDirectory obj = new PhoneDirectory(maxNumbers);
 * int param_1 = obj.get();
 * boolean param_2 = obj.check(number);
 * obj.release(number);
 */