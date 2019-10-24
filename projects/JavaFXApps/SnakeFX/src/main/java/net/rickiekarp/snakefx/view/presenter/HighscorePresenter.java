package net.rickiekarp.snakefx.view.presenter;

import net.rickiekarp.snakefx.highscore.HighScoreEntry;
import net.rickiekarp.snakefx.highscore.HighscoreManager;
import net.rickiekarp.snakefx.viewmodel.ViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import static net.rickiekarp.snakefx.config.Config.*;

public class HighscorePresenter {

    @FXML
    private TableView<HighScoreEntry> tableView;

    private final ListProperty<HighScoreEntry> highScoreEntries = new SimpleListProperty<>();

    private ViewModel viewModel;

    public HighscorePresenter(ViewModel viewModel, HighscoreManager highscoreManager) {
        this.viewModel = viewModel;
        viewModel.collision.addListener((observable, oldValue, collisionHappend) -> {
            if (collisionHappend) {
                gameFinished();
            }
        });

        this.highScoreEntries.bind(highscoreManager.highScoreEntries());
    }

    public ListProperty<HighScoreEntry> highScoreEntries() {
        return highScoreEntries;
    }

    public void gameFinished() {
        final int points = viewModel.points.get();

        final int size = highScoreEntries.size();

        if (size < MAX_SCORE_COUNT.get()) {
            viewModel.newHighscoreWindowOpen.set(true);
        } else {
            // check whether the last entry on the list has more points then the
            // current game

            if (highScoreEntries.get(size - 1).getPoints() < points) {
                viewModel.newHighscoreWindowOpen.set(true);
            }
        }
    }

    @FXML
    public void initialize() {
        tableView.setItems(highScoreEntries);
    }


}
