package net.rickiekarp.snakefx.core;

import net.rickiekarp.snakefx.viewmodel.ViewModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.Whitebox;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SnakeTest {
	private Snake snake;

	private Grid gridMock;

	private GameLoop gameLoopMock;

	private static final int X = 4;
	private static final int Y = 2;

	private ViewModel viewModel;

	@Before
	public void setUp() {
		gridMock = mock(Grid.class);
		gameLoopMock = mock(GameLoop.class);

		viewModel = new ViewModel();

		snake = new Snake(viewModel, gridMock, gameLoopMock);
		Whitebox.setInternalState(snake, "x", X);
		Whitebox.setInternalState(snake, "y", Y);

	}

	@Test
	public void testInitialization() {
		final Field field = mock(Field.class);

		when(gridMock.getXY(X, Y)).thenReturn(field);

		snake.init();

		verify(gridMock, times(1)).getXY(X, Y);
		verify(field, times(1)).changeState(State.HEAD);

		assertThat(getHead()).isEqualTo(field);

		// The direction of the snake is UP on start.
		final Direction direction = currentDirectionFromSnake();
		assertThat(direction).isEqualTo(Direction.UP);
	}

	@Test
	public void testChangeDirection() {
		viewModel.snakeDirection.set(Direction.LEFT);
		final Direction direction = nextDirectionFromSnake();

		assertThat(direction).isEqualTo(Direction.LEFT);
	}

	/**
	 * When the new direction has the same orientation as the old one ( both are
	 * horizontal or both are vertical) no change of the direction should be
	 * made.
	 * 
	 * Otherwise the head of the snake would move directly into the tail.
	 * 
	 * 
	 * But if the player pressed LEFT and then DOWN faster then the gap between
	 * two frames, then the Snake would make a 180 degree turnaround. The LEFT
	 * keypress wouldn't be filtered out because LEFT has another orientation
	 * then UP and the DOWN keypress wouldn't be filtered out because LEFT
	 * (which is the "next direction" now) has another orientation then DOWN.
	 * 
	 * To prevend this we have two variables for the direction: "nextDirection"
	 * and "currentDirection". When the player likes to change the direction,
	 * only nextDirection is changed but he test whether the orientation is the
	 * same is done with the "currentDirection". When the snake moves, the
	 * "currentDirection" variable gets the value from "nextDirection".
	 * 
	 */
	@Test
	public void testChangeDirectionNewHasSameOrientationAsOld() {
		final Field head = mock(Field.class);
		when(gridMock.getXY(X, Y)).thenReturn(head);

		final Field newHead = mock(Field.class);
		when(newHead.getState()).thenReturn(State.EMPTY);
		when(gridMock.getFromDirection(head, Direction.LEFT)).thenReturn(newHead);

		// Snake is initialized with currentDirection=UP and nextDirection=UP
		snake.init();

		viewModel.snakeDirection.set(Direction.DOWN);

		// currentDirection and nextDirection is still UP because the
		// orientation is the same
		assertThat(nextDirectionFromSnake()).isEqualTo(Direction.UP);
		assertThat(currentDirectionFromSnake()).isEqualTo(Direction.UP);

		viewModel.snakeDirection.set(Direction.LEFT);
		// the nextDirection is now changed...
		assertThat(nextDirectionFromSnake()).isEqualsToByComparingFields(Direction.LEFT);
		// ... the currentDirection is still the old one. It is only changed
		// when the
		// snake moves.
		assertThat(currentDirectionFromSnake()).isEqualTo(Direction.UP);

		viewModel.snakeDirection.set(Direction.DOWN);
		// nextDirection is not changed as the currentDirection is still UP and
		// has the same orientation as DOWN
		assertThat(nextDirectionFromSnake()).isEqualTo(Direction.LEFT);
		assertThat(currentDirectionFromSnake()).isEqualTo(Direction.UP);

		snake.move();

		assertThat(nextDirectionFromSnake()).isEqualTo(Direction.LEFT);
		// now the currentDirection has changed.
		assertThat(currentDirectionFromSnake()).isEqualTo(Direction.LEFT);
	}

	@Test
	public void testMove() {
		final Field oldHead = mock(Field.class);
		when(oldHead.getState()).thenReturn(State.EMPTY);
		when(gridMock.getXY(X, Y)).thenReturn(oldHead);

		snake.init();

		final Field newHead = mock(Field.class);
		when(newHead.getState()).thenReturn(State.EMPTY);
		when(gridMock.getFromDirection(oldHead, Direction.UP)).thenReturn(newHead);

		snake.move();

		assertThat(getHead()).isEqualTo(newHead);

		verify(oldHead).changeState(State.EMPTY);
	}

	/**
	 * When the snake moves to a field that has the state "FOOD" the snake
	 * should grow by 1 field.
	 */
	@Test
	public void testGrow() {
		final Field field1 = new Field(0, 3, 10);
		// at the start field1 is the head
		when(gridMock.getXY(X, Y)).thenReturn(field1);

		// field2 is above field1
		final Field field2 = new Field(0, 2, 10);
		field2.changeState(State.FOOD);
		when(gridMock.getFromDirection(field1, Direction.UP)).thenReturn(field2);

		// field3 is above field2
		final Field field3 = new Field(0, 1, 10);
		when(gridMock.getFromDirection(field2, Direction.UP)).thenReturn(field3);

		snake.init();

		snake.move();

		// the head of the snake is now on field2
		assertThat(getHead()).isEqualTo(field2);

		// field1 is now a part of the tail
		assertThat(field1.getState()).isEqualTo(State.TAIL);

		// One Point has to be added.
		assertThat(viewModel.points.get()).isEqualTo(1);

		// Now the snake is moving another field forward. This time the new
		// field (field3)
		// is empty.

		snake.move();

		// field3 becomes the new head
		assertThat(getHead()).isEqualTo(field3);

		// field2 becomes the tail
		assertThat(field2.getState()).isEqualTo(State.TAIL);

		// field1 is now empty
		assertThat(field1.getState()).isEqualTo(State.EMPTY);
	}

	@Test
	public void testCollision() {

		final Field oldHead = mock(Field.class);
		when(oldHead.getState()).thenReturn(State.EMPTY);
		when(gridMock.getXY(X, Y)).thenReturn(oldHead);

		snake.init();

		final Field tail = mock(Field.class);
		when(tail.getState()).thenReturn(State.TAIL);
		when(gridMock.getFromDirection(oldHead, Direction.UP)).thenReturn(tail);

		snake.move();

		assertThat(viewModel.collision.get()).isTrue();
	}

	/**
	 * When the newGame method is called, the head must be set to null and the
	 * tails arraylist has to be reset.
	 * 
	 * The init method has to be called too.
	 */
	@Test
	public void testNewGame() {
		final Field head = mock(Field.class);
		when(head.getState()).thenReturn(State.EMPTY);
		when(gridMock.getXY(X, Y)).thenReturn(head);

		final Field food = mock(Field.class);
		when(food.getState()).thenReturn(State.FOOD);
		when(gridMock.getFromDirection(head, Direction.UP)).thenReturn(food);

		snake.init();
		snake.move();

		assertThat(getHead()).isEqualTo(food);
		assertThat(getTail()).hasSize(1);
		assertThat(getTail().contains(head));

		snake.newGame();

		// the head is reset and the tail is empty
		assertThat(getHead()).isEqualTo(head);
		assertThat(getTail()).isEmpty();

	}

	@SuppressWarnings("unchecked")
	private List<Field> getTail() {
		return (List<Field>) Whitebox.getInternalState(snake, "tail");
	}

	private Field getHead() {
		return (Field) Whitebox.getInternalState(snake, "head");
	}

	private Direction nextDirectionFromSnake() {
		return (Direction) Whitebox.getInternalState(snake, "nextDirection");
	}

	private Direction currentDirectionFromSnake() {
		return (Direction) Whitebox.getInternalState(snake, "currentDirection");
	}
}
