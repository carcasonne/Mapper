package bfst21.vector.addressparser;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Address {
  public final String street, houseNumber, postcode, city;
  ArrayList<String> matchingAddressNames = new ArrayList<>();
  private static ArrayList<Pattern> patterns = new ArrayList<Pattern>();

  private Address(String _street, String _houseNumber, String _postcode, String _city) {
    street = _street;
    houseNumber = _houseNumber;
    postcode = _postcode;
    city = _city;
  }

  public String toString() {
    String addressString =  new String();

    if (houseNumber != null) {
      addressString += street;
      addressString += " " + houseNumber;
    }
    if (postcode != null) {
      addressString += " " + postcode;
    }
    if (city != null) {
      addressString += " " + city;
    }

    return addressString;
  }

  public String housePostcodeCityString(){
    String addressString =  new String();
    if (houseNumber != null) {
      addressString += houseNumber;
    }
    if (postcode != null) {
      addressString += " " + postcode;
    }
    if (city != null) {
      addressString += " " + city;
    }
    return addressString;
  }

  public String getStreet(){
    return street;
  }


  static String streetRegX = "([a-zæøåéäöëüA-ZÆØÅÉÄÖËÜ -./]*)";
  static String houseRegX = "([0-9a-zæøåéA-ZÆØÅÉ]{0,3})";
  static String postcodeRX = "([0-9]{4})";
  static String cityRX = "([a-zæøåA-ZÆØÅ .]*)";
  static String whitespace = "[ ,.-]*";

  public static void addAllPatterns() {
    patterns.add(Pattern.compile(streetRegX + " " + whitespace));// 0
    patterns.add(Pattern.compile(streetRegX + " " + whitespace + cityRX + whitespace)); // 1
    patterns.add(Pattern.compile(streetRegX + " " + whitespace + houseRegX + whitespace + cityRX + whitespace)); // 2
    patterns.add(Pattern.compile(streetRegX + " " + whitespace + houseRegX + whitespace + postcodeRX + whitespace)); // 3
    patterns.add(Pattern.compile(streetRegX + " " + whitespace + houseRegX + whitespace + cityRX + whitespace + postcodeRX + whitespace)); // 4
    patterns.add(Pattern.compile(streetRegX + " " + whitespace + houseRegX + whitespace + postcodeRX + whitespace + cityRX + whitespace)); // 5
  }

  //Checking if the input matches patterns, then builds and parses the address
  public static Address parse(String input) {
    addAllPatterns();
    Builder address = new Builder();

    for(int i = 0; i < patterns.size(); i++){
      Matcher matcher = patterns.get(i).matcher(input);

      if(matcher.matches()){
        if(i==0){
          address.street(matcher.group(1));
        } else if(i==1){
          address.street(matcher.group(1)).city(matcher.group(2));
        } else if(i==2){
          address.street(matcher.group(1)).house(matcher.group(2)).city(matcher.group(3));
        } else if(i==3){
          address.street(matcher.group(1)).house(matcher.group(2)).postcode(matcher.group(3));
        } else if(i==4){
          address.street(matcher.group(1)).house(matcher.group(2)).city(matcher.group(3)).postcode(matcher.group(4));
        } else if(i==5){
          address.street(matcher.group(1)).house(matcher.group(2)).postcode(matcher.group(3)).city(matcher.group(4));
        }
      }
    }
    return address.build();
  }

  public static class Builder {
    private String street, house, postcode, city;

    public Builder street(String _street) {
      street = _street;
      return this;
    }

    public Builder house(String _house) {
        house = _house;
        return this;
    }

    public Builder postcode(String _postcode) {
      postcode = _postcode;
      return this;
    }

    public Builder city(String _city) {
      city = _city;
      return this;
    }

    public Address build() {
      return new Address(street, house, postcode, city);
    }
  }
}
