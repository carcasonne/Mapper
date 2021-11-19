package bfst21.vector;

import javafx.scene.paint.Color;

import java.util.HashMap;

public class MapTheme {

    private HashMap<String, Color> typeToColorMap;
    private boolean isDarkMode;

    public MapTheme() {
        typeToColorMap = new HashMap<>();
        init();
    }

    public void init(){
        typeToColorMap.put("background", Color.valueOf("#ffffb3"));

        Color parkColor = new Color(0f, 1f, 0f,.6f);
        typeToColorMap.put("leisure_park_color", parkColor);

        typeToColorMap.put("amenity_graveyard_color", Color.valueOf("#d9ff66"));

        typeToColorMap.put("landuse_meadow_color", Color.valueOf("#70db70"));
        typeToColorMap.put("landuse_forest_color", Color.valueOf("#008000"));
        typeToColorMap.put("landuse_basin_color", Color.LIGHTBLUE);
        typeToColorMap.put("landuse_residential_color", Color.LIGHTGRAY);

        typeToColorMap.put("natural_water_color", Color.LIGHTBLUE);
        typeToColorMap.put("natural_heath_color", Color.LIGHTGREEN);
        typeToColorMap.put("natural_scree_color", Color.valueOf("#ccccb3"));

        typeToColorMap.put("manmade_bridge_color", Color.valueOf("#d9d9d9"));
        typeToColorMap.put("manmade_pier_color", Color.valueOf("#f2f2f2"));

        typeToColorMap.put("building_color", Color.valueOf("#e6e6e6"));

        typeToColorMap.put("road_primary_color", Color.ORANGE);
        typeToColorMap.put("road_secondary_color", Color.valueOf("#ff9900"));
        typeToColorMap.put("road_tertiary_color", Color.valueOf("#ff9900"));
        typeToColorMap.put("road_unclassified_color", Color.DARKGREY);
        typeToColorMap.put("road_residential_color", Color.valueOf("#4d4d4d"));
        typeToColorMap.put("road_motorway_color", Color.RED);
        typeToColorMap.put("road_trunk_color", Color.valueOf("#e65c00"));
        typeToColorMap.put("road_livingStreet_color", Color.DARKGRAY);
        typeToColorMap.put("road_service_color", Color.DARKGRAY);
        typeToColorMap.put("road_pedestrian_color", Color.DARKGRAY);
        typeToColorMap.put("road_track_color", Color.SANDYBROWN);
        typeToColorMap.put("road_default_color", Color.DARKGRAY);

        typeToColorMap.put("electrified_rail_color", Color.valueOf("#5c0099"));
        typeToColorMap.put("electrified_contactLine_color", Color.valueOf("#00004d"));

        typeToColorMap.put("bridge_color", Color.BLACK);

        typeToColorMap.put("aeroway_default_color", Color.SLATEGRAY);

        Color specialColor = new Color(1f, 0f, 0f,.2f);
        typeToColorMap.put("priorityzone_military_color", specialColor);

        typeToColorMap.put("dijkstra_edges_driven", Color.valueOf("#5900b3"));
        typeToColorMap.put("dijkstra_vertices_driven", Color.valueOf("#5900b3"));
        typeToColorMap.put("dijkstra_edges_notDriven", Color.valueOf("#b30047"));
        typeToColorMap.put("dijkstra_vertices_notDriven", Color.valueOf("#b30047"));
    }

    public void resetThemeToDefault() {
        typeToColorMap.replace("background", Color.valueOf("#ffffb3"));

        Color parkColor = new Color(0f, 1f, 0f,.6f);
        typeToColorMap.replace("leisure_park_color", parkColor);

        typeToColorMap.replace("amenity_graveyard_color", Color.valueOf("#d9ff66"));

        typeToColorMap.replace("landuse_meadow_color", Color.valueOf("#70db70"));
        typeToColorMap.replace("landuse_forest_color", Color.valueOf("#008000"));
        typeToColorMap.replace("landuse_basin_color", Color.LIGHTBLUE);
        typeToColorMap.replace("landuse_residential_color", Color.LIGHTGRAY);

        typeToColorMap.replace("natural_water_color", Color.LIGHTBLUE);
        typeToColorMap.replace("natural_heath_color", Color.LIGHTGREEN);
        typeToColorMap.replace("natural_scree_color", Color.valueOf("#ccccb3"));

        typeToColorMap.replace("manmade_bridge_color", Color.valueOf("#d9d9d9"));
        typeToColorMap.replace("manmade_pier_color", Color.valueOf("#f2f2f2"));

        typeToColorMap.replace("building_color", Color.valueOf("#e6e6e6"));

        typeToColorMap.replace("road_primary_color", Color.ORANGE);
        typeToColorMap.replace("road_secondary_color", Color.valueOf("#ff9900"));
        typeToColorMap.replace("road_tertiary_color", Color.valueOf("#ff9900"));
        typeToColorMap.replace("road_unclassified_color", Color.DARKGREY);
        typeToColorMap.replace("road_residential_color", Color.valueOf("#4d4d4d"));
        typeToColorMap.replace("road_motorway_color", Color.RED);
        typeToColorMap.replace("road_trunk_color", Color.valueOf("#e65c00"));
        typeToColorMap.replace("road_livingStreet_color", Color.DARKGRAY);
        typeToColorMap.replace("road_service_color", Color.DARKGRAY);
        typeToColorMap.replace("road_pedestrian_color", Color.DARKGRAY);
        typeToColorMap.replace("road_track_color", Color.SANDYBROWN);
        typeToColorMap.replace("road_default_color", Color.DARKGRAY);

        typeToColorMap.replace("electrified_rail_color", Color.valueOf("#5c0099"));
        typeToColorMap.replace("electrified_contactLine_color", Color.valueOf("#00004d"));
        
        typeToColorMap.replace("bridge_color", Color.BLACK);

        typeToColorMap.replace("aeroway_default_color", Color.SLATEGRAY);

        Color specialColor = new Color(1f, 0f, 0f,.2f);
        typeToColorMap.replace("priorityzone_military_color", specialColor);

        typeToColorMap.replace("dijkstra_edges_driven", Color.valueOf("#5900b3"));
        typeToColorMap.replace("dijkstra_vertices_driven", Color.valueOf("#5900b3"));
        typeToColorMap.replace("dijkstra_edges_notDriven", Color.valueOf("#b30047"));
        typeToColorMap.replace("dijkstra_vertices_notDriven", Color.valueOf("#b30047"));

        isDarkMode = false;
    }

    public void setThemeToDarkMode(){
        typeToColorMap.replace("background", Color.valueOf("#002266"));

        Color nightGrassColor = new Color(0f, 1f, 0f,.5f);
        typeToColorMap.replace("leisure_park_color", nightGrassColor);

        typeToColorMap.replace("amenity_graveyard_color", nightGrassColor);

        typeToColorMap.replace("landuse_meadow_color", nightGrassColor);
        Color nightForestColor = new Color(0f, 1f, 0f,.2f);
        typeToColorMap.replace("landuse_forest_color", Color.valueOf("#1a0d00"));
        typeToColorMap.replace("landuse_basin_color", Color.valueOf("#000033"));
        typeToColorMap.replace("landuse_residential_color", Color.valueOf("#7300e6"));

        typeToColorMap.replace("natural_water_color", Color.valueOf("#000033"));
        typeToColorMap.replace("natural_heath_color", nightGrassColor);
        typeToColorMap.replace("natural_scree_color", nightGrassColor);

        typeToColorMap.replace("manmade_bridge_color", Color.valueOf("#52527a"));
        typeToColorMap.replace("manmade_pier_color", Color.valueOf("#52527a"));

        typeToColorMap.replace("building_color", Color.valueOf("#330066"));

        typeToColorMap.replace("road_primary_color", Color.valueOf("#008ae6"));
        typeToColorMap.replace("road_secondary_color", Color.valueOf("#008ae6"));
        typeToColorMap.replace("road_tertiary_color", Color.valueOf("#008ae6"));
        typeToColorMap.replace("road_unclassified_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_residential_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_motorway_color", Color.valueOf("#944dff"));
        typeToColorMap.replace("road_trunk_color", Color.valueOf("#9966ff"));
        typeToColorMap.replace("road_livingStreet_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_service_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_pedestrian_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_track_color", Color.valueOf("#00b3b3"));
        typeToColorMap.replace("road_default_color", Color.valueOf("#00b3b3"));

        typeToColorMap.replace("electrified_rail_color", Color.valueOf("#999900"));
        typeToColorMap.replace("electrified_contactLine_color", Color.valueOf("#997a00"));

        typeToColorMap.replace("bridge_color", Color.BLACK);

        typeToColorMap.replace("aeroway_default_color", Color.valueOf("#248f8f"));

        Color specialColor = new Color(1f, 0f, 0f,.2f);
        typeToColorMap.replace("priorityzone_military_color", specialColor);

        typeToColorMap.replace("dijkstra_edges_driven", Color.valueOf("#33ff33"));
        typeToColorMap.replace("dijkstra_vertices_driven", Color.valueOf("#33ff33"));
        typeToColorMap.replace("dijkstra_edges_notDriven", Color.valueOf("#ffff4d"));
        typeToColorMap.replace("dijkstra_vertices_notDriven", Color.valueOf("#ffff4d"));

        isDarkMode = true;
    }

    public HashMap<String, Color> getColors() {
        return typeToColorMap;
    }

    public boolean getIsDarkMode(){
        return isDarkMode;
    }


}
