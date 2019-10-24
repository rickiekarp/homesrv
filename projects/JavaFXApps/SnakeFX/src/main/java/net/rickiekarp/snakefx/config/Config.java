package net.rickiekarp.snakefx.config;

/**
 * This enum represents configuration parameters.
 */
public enum Config {

	/**
	 * The number of rows and columns of the grid. In this game the grid is a
	 * square and so the number of rows and columns are equal.
	 */
	ROW_AND_COLUMN_COUNT(20),

	/**
	 * The size of the grid in pixel.
	 */
	GRID_SIZE_IN_PIXEL(500),

	/**
	 * The x coordinate of the starting point of the snake
	 */
	SNAKE_START_X(10),

	/**
	 * The y coordinate of the starting point of the snake
	 */
	SNAKE_START_Y(10),

	/**
	 * The max number of HighScore entries that are saved and persisted
	 */
	MAX_SCORE_COUNT(10)

	;

	private Integer value;

	private Config(final Integer value) {
		this.value = value;
	}

	/**
	 * @return the configuration value of this enum constant.
	 */
	public Integer get() {
		return value;
	}
}