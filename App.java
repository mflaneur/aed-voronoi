
// ML: This is a modified version of the map code tutorial located at
// https://developers.arcgis.com/java/maps-2d/tutorials/display-a-map/

// ML: ... and the point geometry tutorial located at
// https://developers.arcgis.com/java/maps-2d/tutorials/add-a-point-line-and-polygon/

// ML: Obtained campus boundary coordinates with online Polyline Tool
// https://www.keene.edu/campus/maps/tool/

// ML: I wrote a main hook to display Voronoi vertices from old code found at:
// https://sourceforge.net/projects/simplevoronoi/
// which was discussed on these sites:
// https://ageeksnotes.blogspot.com/2010/11/fast-java-implementation-fortunes.html
// https://shaneosullivan.wordpress.com/2007/04/05/fortunes-sweep-line-voronoi-algorithm-implemented-in-java/

//   Copyright 2020 Esri
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

package com.example.app;

// ML: Added imports from point geometry tutorial
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.view.MapView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    private MapView mapView;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        // ML: Changed map title
        // set the title and size of the stage and show it
        // stage.setTitle("Display a map tutorial");
        stage.setTitle("IUK AEDs (Spring 2022 Java Project by Michael LaFleur)");

        // ML: Changed width and height to personal preference
        // stage.setWidth(800);
        // stage.setHeight(700);
        stage.setWidth(1500);
        stage.setHeight(800);
        stage.show();

        // create a JavaFX scene with a stack pane as the root node, and add it to the scene
        StackPane stackPane = new StackPane();
        Scene scene = new Scene(stackPane);
        stage.setScene(scene);

        // ML: Replaced YOUR_API_KEY with my unique developer API key (then redacted for GitHub posting)
        // Note: it is not best practice to store API keys in source code.
        // The API key is referenced here for the convenience of this tutorial.
        String yourApiKey = "YOUR_API_KEY";
        ArcGISRuntimeEnvironment.setApiKey(yourApiKey);

        // create a map view to display the map and add it to the stack pane
        mapView = new MapView();
        stackPane.getChildren().add(mapView);

        // ML: Changed TOPOGRAPHIC to IMAGERY (satellite map)
        // ML: A list of basemap styles can be found at
        // https://developers.arcgis.com/java/api-reference/reference/com/esri/arcgisruntime/mapping/BasemapStyle.html
        // ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_TOPOGRAPHIC);
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_IMAGERY);

        // set the map on the map view
        mapView.setMap(map);

        // ML: Changed map and scale using IUK coordinates
        // ML: Obtained DMS coordinates from Google Earth and converted with my program
        // mapView.setViewpoint(new Viewpoint(34.02700, -118.80543, 144447.638572));
        mapView.setViewpoint(new Viewpoint(40.45775, -86.12750, 5500));

        // ML: Added overlay from point geometry tutorial
        // create a graphics overlay and add it to the map view
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);

        // ML: Point (graphic) added from point geometry tutorial
        // ML: Changed coordinates to Havens Auditorium AED geotag
        // create a point geometry with a location and spatial reference
        // Point point = new Point(-118.80657463861, 34.0005930608889, SpatialReferences.getWgs84());
        Point pointAED01 = new Point(-86.13314, 40.46048, SpatialReferences.getWgs84());
        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        SimpleMarkerSymbol simpleMarkerSymbol =
                new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CROSS, 0xFFCC0000, 10);
        SimpleLineSymbol blueOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFFCC0000, 2);

        // ML: Added orangeOutlineSymbol for campus boundary
        SimpleLineSymbol orangeOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFFFF6600, 3);

        // ML: Added blackOutlineSymbol for Thiessen polygons boundaries
        SimpleLineSymbol blackOutlineSymbol =
                new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, 0xFF000000, 2);

        simpleMarkerSymbol.setOutline(blueOutlineSymbol);

        // L:M (Point) graphic added from point geometry tutorial
        // create a graphic with the point geometry and symbol
        Graphic pointGraphicAED01 = new Graphic(pointAED01, simpleMarkerSymbol);
        // add the point graphic to the graphics overlay
        // graphicsOverlay.getGraphics().add(pointGraphicAED01);

        // ML: Added points for the remaining geotagged AED locations
        Point pointAED02 = new Point(-86.13246, 40.4602, SpatialReferences.getWgs84());  // Main (1st Flr Ctr)
        Point pointAED03 = new Point(-86.13105, 40.46, SpatialReferences.getWgs84());    // East (1st Flr Ctr)
        Point pointAED04 = new Point(-86.13014, 40.45971, SpatialReferences.getWgs84()); // Hunt (1st Flr Ctr)
        Point pointAED05 = new Point(-86.13148, 40.45903, SpatialReferences.getWgs84()); // Library (Entrance)
        Point pointAED06 = new Point(-86.13137, 40.45923, SpatialReferences.getWgs84()); // Kelley (Elevator x 2)
        Point pointAED07 = new Point(-86.12869, 40.45847, SpatialReferences.getWgs84()); // SAEC (Gym)
        Point pointAED08 = new Point(-86.13055, 40.45803, SpatialReferences.getWgs84()); // Observatory (Lobby)
        Point pointAED09 = new Point(-86.12740, 40.45627, SpatialReferences.getWgs84()); // Early Outreach
        Point pointAED10 = new Point(-86.12744, 40.45546, SpatialReferences.getWgs84()); // Fine Arts (Lobby)

        // ML: Added (point) graphics for the remaining geotagged AED locations
        Graphic pointGraphicAED02 = new Graphic(pointAED02, simpleMarkerSymbol);
        Graphic pointGraphicAED03 = new Graphic(pointAED03, simpleMarkerSymbol);
        Graphic pointGraphicAED04 = new Graphic(pointAED04, simpleMarkerSymbol);
        Graphic pointGraphicAED05 = new Graphic(pointAED05, simpleMarkerSymbol);
        Graphic pointGraphicAED06 = new Graphic(pointAED06, simpleMarkerSymbol);
        Graphic pointGraphicAED07 = new Graphic(pointAED07, simpleMarkerSymbol);
        Graphic pointGraphicAED08 = new Graphic(pointAED08, simpleMarkerSymbol);
        Graphic pointGraphicAED09 = new Graphic(pointAED09, simpleMarkerSymbol);
        Graphic pointGraphicAED10 = new Graphic(pointAED10, simpleMarkerSymbol);

        // ML: Created a polygon to use as a campus boundary
        // ML: https://www.keene.edu/campus/maps/tool/
        // create a point collection with a spatial reference, and add points to it
        PointCollection polygonPointsCampusBoundary = new PointCollection(SpatialReferences.getWgs84());
        polygonPointsCampusBoundary.add(new Point(-86.1336848, 40.4621852));
        polygonPointsCampusBoundary.add(new Point(-86.1336966, 40.4611026));
        polygonPointsCampusBoundary.add(new Point(-86.1336896, 40.4606277));
        polygonPointsCampusBoundary.add(new Point(-86.1336896, 40.4603798));
        polygonPointsCampusBoundary.add(new Point(-86.1336102, 40.4598489));
        polygonPointsCampusBoundary.add(new Point(-86.1334611, 40.4593748));
        polygonPointsCampusBoundary.add(new Point(-86.1333527, 40.4591167));
        polygonPointsCampusBoundary.add(new Point(-86.1332325, 40.4588468));
        polygonPointsCampusBoundary.add(new Point(-86.1331033, 40.4586152));
        polygonPointsCampusBoundary.add(new Point(-86.1329370, 40.4583514));
        polygonPointsCampusBoundary.add(new Point(-86.1327138, 40.4580578));
        polygonPointsCampusBoundary.add(new Point(-86.1325990, 40.4579085));
        polygonPointsCampusBoundary.add(new Point(-86.1324622, 40.4577560));
        polygonPointsCampusBoundary.add(new Point(-86.1320904, 40.4573329));
        polygonPointsCampusBoundary.add(new Point(-86.1317069, 40.4574125));
        polygonPointsCampusBoundary.add(new Point(-86.1310444, 40.4569184));
        polygonPointsCampusBoundary.add(new Point(-86.1296437, 40.4569784));
        polygonPointsCampusBoundary.add(new Point(-86.1285574, 40.4570232));
        polygonPointsCampusBoundary.add(new Point(-86.1283788, 40.4563213));
        polygonPointsCampusBoundary.add(new Point(-86.1282474, 40.4558251));
        polygonPointsCampusBoundary.add(new Point(-86.1280033, 40.4554780));
        polygonPointsCampusBoundary.add(new Point(-86.1280140, 40.4552881));
        polygonPointsCampusBoundary.add(new Point(-86.1270779, 40.4553167));
        polygonPointsCampusBoundary.add(new Point(-86.1270677, 40.4561829));
        polygonPointsCampusBoundary.add(new Point(-86.1270618, 40.4570789));
        polygonPointsCampusBoundary.add(new Point(-86.1270543, 40.4582338));
        polygonPointsCampusBoundary.add(new Point(-86.1270452, 40.4589689));
        polygonPointsCampusBoundary.add(new Point(-86.1270382, 40.4596247));
        polygonPointsCampusBoundary.add(new Point(-86.1270345, 40.4597640));
        polygonPointsCampusBoundary.add(new Point(-86.1270452, 40.4604521));
        polygonPointsCampusBoundary.add(new Point(-86.1270495, 40.4607143));
        polygonPointsCampusBoundary.add(new Point(-86.1271622, 40.4607143));
        polygonPointsCampusBoundary.add(new Point(-86.1273478, 40.4606886));
        polygonPointsCampusBoundary.add(new Point(-86.1275570, 40.4606375));
        polygonPointsCampusBoundary.add(new Point(-86.1277394, 40.4605926));
        polygonPointsCampusBoundary.add(new Point(-86.1278708, 40.4605967));
        polygonPointsCampusBoundary.add(new Point(-86.1279647, 40.4605987));
        polygonPointsCampusBoundary.add(new Point(-86.1280666, 40.4606191));
        polygonPointsCampusBoundary.add(new Point(-86.1281524, 40.4606334));
        polygonPointsCampusBoundary.add(new Point(-86.1282329, 40.4606395));
        polygonPointsCampusBoundary.add(new Point(-86.1283375, 40.4606498));
        polygonPointsCampusBoundary.add(new Point(-86.1284770, 40.4606865));
        polygonPointsCampusBoundary.add(new Point(-86.1286309, 40.4607722));
        polygonPointsCampusBoundary.add(new Point(-86.1289877, 40.4609254));
        polygonPointsCampusBoundary.add(new Point(-86.1292505, 40.4609662));
        polygonPointsCampusBoundary.add(new Point(-86.1295268, 40.4610336));
        polygonPointsCampusBoundary.add(new Point(-86.1302456, 40.4612990));
        polygonPointsCampusBoundary.add(new Point(-86.1306447, 40.4614121));
        polygonPointsCampusBoundary.add(new Point(-86.1310573, 40.4615273));
        polygonPointsCampusBoundary.add(new Point(-86.1316420, 40.4616784));
        polygonPointsCampusBoundary.add(new Point(-86.1321275, 40.4618091));
        polygonPointsCampusBoundary.add(new Point(-86.1326060, 40.4619316));
        polygonPointsCampusBoundary.add(new Point(-86.1330003, 40.4620316));
        polygonPointsCampusBoundary.add(new Point(-86.1332765, 40.4620949));
        polygonPointsCampusBoundary.add(new Point(-86.1334240, 40.4621296));
        polygonPointsCampusBoundary.add(new Point(-86.1335689, 40.4621643));
        polygonPointsCampusBoundary.add(new Point(-86.1336547, 40.4621868));
        polygonPointsCampusBoundary.add(new Point(-86.1336848, 40.4621852));

        // ML: create a polygon geometry from the point collection
        Polygon polygonCampusBoundary = new Polygon(polygonPointsCampusBoundary);

        // ML: create a fill symbol with % transparency and the simple line symbol
        SimpleFillSymbol polygonFillSymbolCampusBoundary =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FFFFFF, orangeOutlineSymbol);

        // ML: create a polygon graphic from the polygon geometry and symbol
        Graphic polygonGraphicCampusBoundary = new Graphic(polygonCampusBoundary, polygonFillSymbolCampusBoundary);
        // ML: add the polygon graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(polygonGraphicCampusBoundary);

        // --------------------------------------------------------------------
        // ML: VORONOI DIAGRAM THIESSEN POLYGONS START HERE
        // --------------------------------------------------------------------

        // ML: create point collections with spatial references, and add points to them
        PointCollection polygonPointsHavensAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsMainAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsEastAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsHuntAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsKelleyAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsLibraryAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsGymAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsObservatoryAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsOutreachAED = new PointCollection(SpatialReferences.getWgs84());
        PointCollection polygonPointsArtsAED = new PointCollection(SpatialReferences.getWgs84());

        // ML: I wrote a main hook to display Voronoi vertices from old code found at:
        // https://sourceforge.net/projects/simplevoronoi/

        // ML: In each polygon, vertices are listed clockwise from top left
        // ML: Main (Havens) 4 vertices
        polygonPointsHavensAED.add(new Point(-86.133697, 40.462187));
        polygonPointsHavensAED.add(new Point(-86.132040, 40.462187));
        polygonPointsHavensAED.add(new Point(-86.133693, 40.458172));
        polygonPointsHavensAED.add(new Point(-86.133697, 40.458168));

        // ML: Main (central) 5 vertices
        polygonPointsMainAED.add(new Point(-86.132040, 40.462187));
        polygonPointsMainAED.add(new Point(-86.131459, 40.462187));
        polygonPointsMainAED.add(new Point(-86.131790, 40.459856));
        polygonPointsMainAED.add(new Point(-86.132104, 40.459503));
        polygonPointsMainAED.add(new Point(-86.133693, 40.458172));

        // ML: East (central) 4 vertices
        polygonPointsEastAED.add(new Point(-86.131459, 40.462187));
        polygonPointsEastAED.add(new Point(-86.129852, 40.462187));
        polygonPointsEastAED.add(new Point(-86.130734, 40.459417));
        polygonPointsEastAED.add(new Point(-86.131790, 40.459856));

        // ML: Hunt (central) 6 vertices
        polygonPointsHuntAED.add(new Point(-86.129852, 40.462187));
        polygonPointsHuntAED.add(new Point(-86.127035, 40.462187));
        polygonPointsHuntAED.add(new Point(-86.127035, 40.461874));
        polygonPointsHuntAED.add(new Point(-86.129731, 40.458720));
        polygonPointsHuntAED.add(new Point(-86.130539, 40.458917));
        polygonPointsHuntAED.add(new Point(-86.130734, 40.459417));

        // ML: Kelley (elevator) 5 vertices
        polygonPointsKelleyAED.add(new Point(-86.131790, 40.459856));
        polygonPointsKelleyAED.add(new Point(-86.130734, 40.459417));
        polygonPointsKelleyAED.add(new Point(-86.130539, 40.458917));
        polygonPointsKelleyAED.add(new Point(-86.130762, 40.458765));
        polygonPointsKelleyAED.add(new Point(-86.132104, 40.459503));

        // ML: Library (entrance) 5 vertices
        polygonPointsLibraryAED.add(new Point(-86.132104, 40.459503));
        polygonPointsLibraryAED.add(new Point(-86.130762, 40.458765));
        polygonPointsLibraryAED.add(new Point(-86.133697, 40.456036));
        polygonPointsLibraryAED.add(new Point(-86.133697, 40.458168));
        polygonPointsLibraryAED.add(new Point(-86.133693, 40.458172));

        // ML: SAEC (gym) 4 vertices
        polygonPointsGymAED.add(new Point(-86.127035, 40.461874));
        polygonPointsGymAED.add(new Point(-86.127035, 40.457963));
        polygonPointsGymAED.add(new Point(-86.129245, 40.456666));
        polygonPointsGymAED.add(new Point(-86.129731, 40.458720));

        // ML: Observatory (lobby) 8 vertices
        polygonPointsObservatoryAED.add(new Point(-86.130539, 40.458917));
        polygonPointsObservatoryAED.add(new Point(-86.129731, 40.458720));
        polygonPointsObservatoryAED.add(new Point(-86.129245, 40.456666));
        polygonPointsObservatoryAED.add(new Point(-86.129632, 40.455974));
        polygonPointsObservatoryAED.add(new Point(-86.130199, 40.455288));
        polygonPointsObservatoryAED.add(new Point(-86.133697, 40.455288));
        polygonPointsObservatoryAED.add(new Point(-86.133697, 40.456036));
        polygonPointsObservatoryAED.add(new Point(-86.130762, 40.458765));

        // ML: Early Outreach 4 vertices
        polygonPointsOutreachAED.add(new Point(-86.127035, 40.457963));
        polygonPointsOutreachAED.add(new Point(-86.127035, 40.455846));
        polygonPointsOutreachAED.add(new Point(-86.129632, 40.455974));
        polygonPointsOutreachAED.add(new Point(-86.129245, 40.456666));

        // ML: Fine Arts (lobby) 4 vertices
        polygonPointsArtsAED.add(new Point(-86.129632, 40.455974));
        polygonPointsArtsAED.add(new Point(-86.127035, 40.455846));
        polygonPointsArtsAED.add(new Point(-86.127035, 40.455288));
        polygonPointsArtsAED.add(new Point(-86.130199, 40.455288));

        // ML: create polygon geometries from the point collections
        Polygon polygonHavensAED = new Polygon(polygonPointsHavensAED);
        Polygon polygonMainAED = new Polygon(polygonPointsMainAED);
        Polygon polygonEastAED = new Polygon(polygonPointsEastAED);
        Polygon polygonHuntAED = new Polygon(polygonPointsHuntAED);
        Polygon polygonKelleyAED = new Polygon(polygonPointsKelleyAED);
        Polygon polygonLibraryAED = new Polygon(polygonPointsLibraryAED);
        Polygon polygonGymAED = new Polygon(polygonPointsGymAED);
        Polygon polygonObservatoryAED = new Polygon(polygonPointsObservatoryAED);
        Polygon polygonOutreachAED = new Polygon(polygonPointsOutreachAED);
        Polygon polygonArtsAED = new Polygon(polygonPointsArtsAED);

        // ML: create fill symbols with % transparency and the blue simple line symbols
        SimpleFillSymbol polygonFillSymbolHavensAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FF0000, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolMainAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FFFF00, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolEastAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FF6600, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolHuntAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x400033FF, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolKelleyAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x4033FF00, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolLibraryAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40CC00FF, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolGymAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40CC00FF, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolObservatoryAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FF6600, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolOutreachAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FFFF00, blackOutlineSymbol);
        SimpleFillSymbol polygonFillSymbolArtsAED =
                new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, 0x40FF0000, blackOutlineSymbol);

        // ML: create polygon graphics from the polygon geometries and symbols
        Graphic polygonGraphicHavensAED = new Graphic(polygonHavensAED, polygonFillSymbolHavensAED);
        Graphic polygonGraphicMainAED = new Graphic(polygonMainAED, polygonFillSymbolMainAED);
        Graphic polygonGraphicEastAED = new Graphic(polygonEastAED, polygonFillSymbolEastAED);
        Graphic polygonGraphicHuntAED = new Graphic(polygonHuntAED, polygonFillSymbolHuntAED);
        Graphic polygonGraphicKelleyAED = new Graphic(polygonKelleyAED, polygonFillSymbolKelleyAED);
        Graphic polygonGraphicLibraryAED = new Graphic(polygonLibraryAED, polygonFillSymbolLibraryAED);
        Graphic polygonGraphicGymAED = new Graphic(polygonGymAED, polygonFillSymbolGymAED);
        Graphic polygonGraphicObservatoryAED = new Graphic(polygonObservatoryAED, polygonFillSymbolObservatoryAED);
        Graphic polygonGraphicOutreachAED = new Graphic(polygonOutreachAED, polygonFillSymbolOutreachAED);
        Graphic polygonGraphicArtsAED = new Graphic(polygonArtsAED, polygonFillSymbolArtsAED);

        // ML: add the polygon graphics to the graphics overlay
        graphicsOverlay.getGraphics().add(polygonGraphicHavensAED);
        graphicsOverlay.getGraphics().add(polygonGraphicMainAED);
        graphicsOverlay.getGraphics().add(polygonGraphicEastAED);
        graphicsOverlay.getGraphics().add(polygonGraphicHuntAED);
        graphicsOverlay.getGraphics().add(polygonGraphicKelleyAED);
        graphicsOverlay.getGraphics().add(polygonGraphicLibraryAED);
        graphicsOverlay.getGraphics().add(polygonGraphicGymAED);
        graphicsOverlay.getGraphics().add(polygonGraphicObservatoryAED);
        graphicsOverlay.getGraphics().add(polygonGraphicOutreachAED);
        graphicsOverlay.getGraphics().add(polygonGraphicArtsAED);

        // --------------------------------------------------------------------
        // ML: VORONOI DIAGRAM THIESSEN POLYGONS END HERE
        // --------------------------------------------------------------------

        // add the point graphic to the graphics overlay
        graphicsOverlay.getGraphics().add(pointGraphicAED01);

        // ML: Added the remaining point graphics to the graphics overlay
        graphicsOverlay.getGraphics().add(pointGraphicAED02);
        graphicsOverlay.getGraphics().add(pointGraphicAED03);
        graphicsOverlay.getGraphics().add(pointGraphicAED04);
        graphicsOverlay.getGraphics().add(pointGraphicAED05);
        graphicsOverlay.getGraphics().add(pointGraphicAED06);
        graphicsOverlay.getGraphics().add(pointGraphicAED07);
        graphicsOverlay.getGraphics().add(pointGraphicAED08);
        graphicsOverlay.getGraphics().add(pointGraphicAED09);
        graphicsOverlay.getGraphics().add(pointGraphicAED10);

    }

    /**
     * Stops and releases all resources used in application.
     */
    @Override
    public void stop() {
        if (mapView != null) {
            mapView.dispose();
        }
    }

}
