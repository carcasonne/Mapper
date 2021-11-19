package bfst21.vector.Exceptions;

import javafx.scene.control.Alert;

public class AlertBox {

    public void fileAlert(){
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setHeaderText("Invalid file!");
        error.setContentText("The input must be either .osm or .txt! If you are trying to load a .zip file, please make sure that it is the correct one!");
        error.showAndWait();
    }

    public void addressAlert(){
        Alert wrongAddress = new Alert(Alert.AlertType.INFORMATION);
        wrongAddress.setHeaderText("Invalid Address");
        wrongAddress.setContentText("Both addresses must be complete with street, house number, postcode and city!");
        wrongAddress.showAndWait();
    }

    public void pathAlert(){
        Alert noPath = new Alert(Alert.AlertType.INFORMATION);
        noPath.setHeaderText("No Path");
        noPath.setContentText("There is no path between the two specified addresses you chose!");
        noPath.showAndWait();
    }
    public void sameAddressAlert(){
        Alert sameAddress = new Alert(Alert.AlertType.INFORMATION);
        sameAddress.setHeaderText("Same Address");
        sameAddress.setContentText("The two addresses can't be the same! Please try again with another adress");
        sameAddress.showAndWait();
    }
}