package net.rickiekarp.snakefx.core;

import net.rickiekarp.snakefx.viewmodel.ViewModel;


/**
 * This class generates new food that the snake can eat.
 * 
 * "Food" means that the given field gets the state {@link State#FOOD}.
 * 
 * The food is generated at an empty field at a random location.
 */
public class FoodGenerator {

	private final Grid grid;

	public FoodGenerator(final ViewModel viewModel, final Grid grid) {
		this.grid = grid;

        viewModel.points.addListener((observable, oldValue, newValue) -> {
            if (oldValue.intValue() < newValue.intValue()) {
                generateFood();
            }
        });
	}

	/**
	 * Generates new food.
	 */
	public void generateFood() {
		final Field field = grid.getRandomEmptyField();

		field.changeState(State.FOOD);
	}
}