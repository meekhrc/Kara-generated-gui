package de.unistuttgart.hamster.ui;

import de.unistuttgart.hamster.facade.HamsterGame;
import de.unistuttgart.hamster.viewmodel.impl.HamsterGameViewPresenter;
import de.unistuttgart.iste.sqa.mpw.framework.mpw.UserInputInterface;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewInput;
import de.unistuttgart.iste.sqa.mpw.framework.viewmodel.GameViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class JavaFXUI extends Application {

    private static final CountDownLatch initLatch = new CountDownLatch(1);
    private static final JavaFXInputInterface inputInterface = new JavaFXInputInterface();
    private static volatile boolean isStarted = false;

    /*@
     @ requires true;
     @*/
    /**
     * Displays the hamster game associated with the provided hamster game adapter in a new window
     * This automatically starts the UI, adds the necessary input interface and opens the scene
     */
    public static void displayInNewGameWindow(final HamsterGame game) {
        var gameViewPresenter = new HamsterGameViewPresenter(game);
        gameViewPresenter.bind();
        game.setUserInputInterface(inputInterface);
        openSceneFor(gameViewPresenter, gameViewPresenter.getViewModel());
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/
    /**
     * Starts the javafx ui thread if not already started
     */
    public static void start() {
        if (!isStarted) {
            new Thread(()->Application.launch(JavaFXUI.class)).start();
            waitForJavaFXStart();
            isStarted = true;
            Platform.setImplicitExit(true);
        }
    }
    
    public static void setKeepFXRunningAfterLastWindow() {
        Platform.setImplicitExit(false);        
    }
    
    @Override
    public void stop() throws Exception {
        isStarted = false;
        super.stop();
    }

    private static void waitForJavaFXStart() {
        try {
            initLatch.await();
        } catch (final InterruptedException ignored) { }
    }

    @Override
    public void start(final Stage primaryStage) throws Exception {
        initLatch.countDown();
    }

    /*@
     @ requires true;
     @ ensures isStarted;
     @*/
    /**
     * Opens a scene for the hamster game associated with hamsterGameViewModel
     * requires that the JavaFXUI is started
     * @param gameViewModel the adapter for the hamster game to display
     */
    public static void openSceneFor(final GameViewInput gameViewInput, final GameViewModel gameViewModel) {
        start();
        JavaFXUtil.blockingExecuteOnFXThread(() -> {
            Stage stage;
            try {
                stage = new GameStage(gameViewInput, gameViewModel);
                stage.show();
            } catch (final IOException e) {
                inputInterface.confirmAlert(e);
            }
        });
    }

    /*@
     @ requires true;
     @ ensures \result != null;
     @*/
    /**
     * Getter for the JavaFXInputInterface singleton
     * @return the JavaFXInputInterface to display
     */
    public static UserInputInterface getJavaFXInputInterface() {
        return inputInterface;
    }

}
