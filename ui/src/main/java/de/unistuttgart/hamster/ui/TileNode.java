package de.unistuttgart.hamster.ui;

import de.unistuttgart.iste.sqa.mpw.framework.datatypes.Color;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCell;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.ViewModelCellLayer;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class TileNode extends StackPane {

    private static final Map<String, Image> images = new HashMap<>();

    static {
        loadHamsterImages();
        loadPropImages();
    }

    private static void loadHamsterImages() {
        var hamsterImage = new Image("images/kara.png");
        for (Color color : Color.values()) {
            var colorizedHamsterImage = JavaFXUtil.changeColor(hamsterImage, ViewModelColorConverter.toJavaFxColor(color));
            images.put("Hamster" + color.name(), colorizedHamsterImage);
        }
    }

    private static void loadPropImages() {
        images.put("Wall", new Image("images/tree.png"));
        for (int i = 1; i <= 12; i++) {
            images.put(i + "Corn", new Image("images/" + i + "Corn32.png"));
        }
    }

    private final ViewModelCell viewModelCell;
    private final Map<ViewModelCellLayer, ImageView> imageViews = new HashMap<>();

    private final ListChangeListener<ViewModelCellLayer> layerListener = new ListChangeListener<>(){

        @Override
        public void onChanged(final Change<? extends ViewModelCellLayer> change) {
            while(change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(layer -> addLayer(layer));
                }
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(layer -> removeLayer(layer));
                }
            }
        }

    };

    TileNode(final ViewModelCell cell) {
        super();
        this.viewModelCell = cell;

        configureStyle();
        cell.layersProperty().addListener(layerListener);
        cell.layersProperty().forEach(this::addLayer);
    }

    private void configureStyle() {
        this.getStyleClass().add("game-grid-cell");
        var tileImageView = createImageView();
        tileImageView.setImage(images.get("Tile"));
        this.getChildren().add(tileImageView);
    }

    private void addLayer(final ViewModelCellLayer layer) {
        var imageView = createImageView();
        imageView.visibleProperty().bind(layer.visibleProperty());
        imageView.imageProperty().bind(Bindings.createObjectBinding(() -> images.get(layer.getImageName()), layer.imageNameProperty()));
        imageView.rotateProperty().bind(layer.rotationProperty());

        imageViews.put(layer, imageView);
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.getChildren().add(imageView));
    }

    private ImageView createImageView() {
        var imageView = new ImageView();
        imageView.fitHeightProperty().bind(this.heightProperty());
        imageView.fitWidthProperty().bind(this.widthProperty());
        imageView.setPreserveRatio(true);
        return imageView;
    }

    private void removeLayer(final ViewModelCellLayer layer) {
        final ImageView view = imageViews.remove(layer);
        JavaFXUtil.blockingExecuteOnFXThread(() -> this.getChildren().remove(view));
    }

    public void dispose() {
        this.viewModelCell.layersProperty().removeListener(this.layerListener);
    }
}
