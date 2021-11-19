package bfst21.vector.addressparser;

import bfst21.vector.radix.RadixTree;
import bfst21.vector.Dijkstra.Coordinate;

import java.util.ArrayList;
public class Recommender {
    RadixTree<RadixTree<Coordinate>> trie;

    public Recommender(RadixTree<RadixTree<Coordinate>> trieST){
        this.trie = trieST;
    }


    // Checks the input and returns an ArrayList with 8 addresses that matches the prefix
    public ArrayList<String> recommendations(String input){
        String fixedInput = startRecommendHelper(input);

        Address address = Address.parse(fixedInput);
        ArrayList<String> list = new ArrayList<>();
        int counter = 0;
        if(fixedInput.matches(Address.streetRegX)){
            if(trie.getKeysWithPrefix(fixedInput) != null) {
                for (String s : trie.getKeysWithPrefix(fixedInput)) {
                    if (counter == 8) break;
                    list.add(s);
                    counter++;
                }
            }
        } else {
            for(String s: trie.get(address.getStreet()).getKeysWithPrefix(address.houseNumber)){
                if(counter == 8) break;
                list.add(address.getStreet() + " " + s);
                counter++;
            }
        }

        return list;
    }

    //Returns the Coordinates of the inserted address
    public Coordinate addressCordinates(String input){
        Address address = Address.parse(input);
        return trie.get(address.getStreet()).get(address.toString());
    }

    //Makes sure that both upper- and lowercase letters can be inserted as input
    private String startRecommendHelper(String input){
        String firstPart = input.split(" ")[0];
        firstPart = firstPart.toLowerCase();
        char[] firstPartArray = firstPart.toCharArray();
        if (firstPart.contains(".")){
            int i = 0;
            while (i < firstPart.length()){
                if (firstPart.charAt(i) == '.'){
                    if (i < firstPart.length() - 1){
                        firstPartArray[i+1] = firstPart.toUpperCase().charAt(i+1);
                    }
                }
                i++;
            }
        }
        firstPartArray[0] = firstPart.toUpperCase().charAt(0);
        int i = 0;
        String stringbuilder = "";
        while (i < firstPartArray.length){
            stringbuilder = stringbuilder + firstPartArray[i];
            i++;
        }
        i = 1;
        while (i <input.split(" ").length){
            stringbuilder = stringbuilder + " " + input.split(" ")[i];
            i++;
        }
        return stringbuilder;
    }
}
