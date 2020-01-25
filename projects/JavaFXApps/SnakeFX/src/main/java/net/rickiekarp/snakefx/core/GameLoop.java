package net.rickiekarp.snakefx.core;

import net.rickiekarp.snakefx.view.ViewModel;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is the game loop of the game.
 */
public class GameLoop {
    private static final int ONE_SECOND = 1000;
    private Timeline timeline;
    private final List<Consumer<?>> actions = new ArrayList<>();
    private final ViewModel viewModel;

    public GameLoop(final ViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.getCollision().addListener(new CollisionListener());
        viewModel.getSpeed().addListener(new SpeedChangeListener());
        viewModel.getGameloopStatus().addListener(new StatusChangedListener());

        init();
    }


    /**
     * Added Actions are called on every keyframe of the GameLoop. The order of invocation is not guaranteed.
     *
     * @param actions the action that gets called.
     */
    void addActions(final Consumer<?>... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    /**
     * Initialize the timeline instance.
     */
    private void init() {
        timeline = new Timeline(buildKeyFrame());
        timeline.setCycleCount(Animation.INDEFINITE);

        // in this place we can't use a direct binding as the ViewModel property
        // can also be changed in other places.
        timeline.statusProperty().addListener((observable, oldStatus,
                                               newStatus) -> viewModel.getGameloopStatus().set(newStatus));
    }

    /**
     * This method creates a {@link KeyFrame} instance according to the configured framerate.
     */
    private KeyFrame buildKeyFrame() {

        final int fps = viewModel.getSpeed().get().getFps();
        final Duration duration = Duration.millis((double) ONE_SECOND / fps);

        return new KeyFrame(duration, event -> {
            actions.forEach(consumer -> consumer.accept(null));
        });
    }


    /**
     * This listener controls the timeline when there are external changes to the status property. This needs to be done
     * because the {@link Timeline#statusProperty()} is readonly and can't be bound bidirectional.
     */
    private final class StatusChangedListener implements ChangeListener<Status> {
        @Override
        public void changed(final ObservableValue<? extends Status> arg0, final Status oldStatus,
                            final Status newStatus) {

            switch (newStatus) {
                case PAUSED:
                    pause();
                    break;
                case RUNNING:
                    play();
                    break;
                case STOPPED:
                    stop();
                    break;
            }
        }
    }

    /**
     * This listener controls the timeline when the desired speed has changed.
     */
    private final class SpeedChangeListener implements ChangeListener<SpeedLevel> {
        @Override
        public void changed(final ObservableValue<? extends SpeedLevel> arg0, final SpeedLevel oldSpeed,
                            final SpeedLevel newSpeed) {

            final Status oldStatus = timeline.getStatus();

            if (Status.RUNNING.equals(oldStatus)) {
                pause();
            }

            // change points depending on difficulty
            viewModel.getPointIncrement().set(newSpeed.getFps() / 5);

            init();

            if (Status.RUNNING.equals(oldStatus)) {
                play();
            }
        }
    }

    /**
     * This listener stops the timeline when an collision is detected.
     */
    private final class CollisionListener implements ChangeListener<Boolean> {
        @Override
        public void changed(final ObservableValue<? extends Boolean> arg0, final Boolean oldValue,
                            final Boolean newCollision) {
            if (newCollision) {
                stop();
            }
        }
    }

    private void play() {
        timeline.play();
    }

    private void pause() {
        timeline.pause();
    }

    private void stop() {
        timeline.stop();
    }

    public Timeline getTimeline() {
        return timeline;
    }
}