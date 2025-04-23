// Time Complexity : check below
// Space Complexity : O(n*l) n - number of words, l - average length of the word
// Did this code successfully run on Leetcode : yes
// Any problem you faced while coding this : no

import java.util.*;
class trie
{
    trie[] children;
    boolean EOW;
    int hotDegree;
    
    public trie()
    {
        this.children = new trie[27]; //25 indexes for lower case letters and 26th index is for space.
        this.EOW = false;
        this.hotDegree = 0;
    }
    
    public void insert(String word, int hotDegree)
    {
        trie root = this;
        
        for(int i=0; i<word.length(); i++)
        {
            char c = word.charAt(i);
            int idx = c == ' ' ? 26 : c - 'a';
            
            if(root.children[idx] == null)
                root.children[idx] = new trie();
            root = root.children[idx];
        }
        root.EOW = true;
        root.hotDegree += hotDegree;
    }
}
class Pair
{
    String word;
    int hotDegree;
    Pair(String word, int hotDegree)
    {
        this.word = word;
        this.hotDegree = hotDegree;
    }
}
class AutocompleteSystem {
    
    trie root = new trie();
    StringBuilder  userString = new StringBuilder();
    
    private void insertString(String s, int hotDegree)
    {
        root.insert(s, hotDegree);
    }
    
    private List<String> getResults(String s)
    {
        //we need to prioritize ascending order instead of descending, because we need to get rid of least importance words
        //when the size of the pq is more than 3, so we just need to take the minheap.
        PriorityQueue<Pair> pq = new PriorityQueue<>((a, b) -> {
            if(a.hotDegree == b.hotDegree) return b.word.compareTo(a.word);
            return Integer.compare(a.hotDegree, b.hotDegree);
        });
        List<String> result = new ArrayList<>();
        trie curr = root;
        StringBuilder str = new StringBuilder();
        for(int i=0; i<s.length(); i++)
        {
            char c = s.charAt(i);
            int idx =  c == ' ' ? 26 : c - 'a';
            if(curr.children[idx] == null) return result;
            str.append(c);
            curr = curr.children[idx];
        }
        
        //applying dfs backtracking approach for getting all the paths starting from the matched prefix and store that in pq.
        dfs(curr, str, pq);
        
        //priority queue contains the results according to the hotdegree and if same hotdegree then lexographical descending order.
        while(!pq.isEmpty())
            result.add(pq.poll().word);
        //just reverse the list gives us the correct order.
        Collections.reverse(result);
        return result;
    }
    
    private void dfs(trie root, StringBuilder str, PriorityQueue<Pair> pq)
    {
        if(root == null) return;
        if(root.EOW)
        {
            pq.offer(new Pair(str.toString(), root.hotDegree));
            if(pq.size() > 3) pq.poll();
        }
        for(int i=0; i<27; i++)
        {
            if(root.children[i] == null) continue;
            char c = i == 26 ? ' ' : (char) (i+'a');
            str.append(c);
            dfs(root.children[i], str, pq);
            str.setLength(str.length()-1);
        }
    }
    
    //Creation of trie data structure with all the given words and hot degree.
    //time complexity - O(n*l) n - number of words, l - average length of the word.
    public AutocompleteSystem(String[] sentences, int[] times) {
        
        for(int i=0; i<sentences.length; i++)
            insertString(sentences[i], times[i]);
    }
    
    
    //getResults method will give us the time complexity
    // we try to match the userstring length type and all the words associated withthat prefix
    // so in worst case we need to list out all the words from the dictionary.
    // Time Complexity - O(n*l) n - number of words, l - average length of the word. 
    public List<String> input(char c) {
        //if the character is # then the previous string entered by the user should be inserted into the trie and clear the user stringbuffer.
        if(c == '#')
        {
            if(userString.length() > 0)
            {
                insertString(userString.toString(), 1);
                userString.setLength(0);
            }
            return new ArrayList<>();
        }
        // if any other character then just get the results matching the stringbuffer as prefix
        userString.append(c);
        return getResults(userString.toString());
    }
}

/**
 * Your AutocompleteSystem object will be instantiated and called as such:
 * AutocompleteSystem obj = new AutocompleteSystem(sentences, times);
 * List<String> param_1 = obj.input(c);
 */