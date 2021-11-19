package bfst21.vector;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View {
    Stage currentView;
    public View(Model model, Stage stage) throws Exception {
        currentView = stage;
        var loader = new FXMLLoader(View.class.getResource("View.fxml"));
        Scene scene = loader.load();
        scene.getStylesheets().add(View.class.getResource("imageStyle.css").toExternalForm());
        currentView.setScene(scene);
        currentView.setTitle("Danmarkskort");
        Controller controller = loader.getController();
        currentView.show();
        controller.init(model, this);
    }

    public void closeCurrentView(){
        currentView.close();
    }

    public Stage getStage() {
        return currentView;
    }
}
