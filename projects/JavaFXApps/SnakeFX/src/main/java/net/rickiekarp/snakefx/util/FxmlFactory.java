package net.rickiekarp.snakefx.util;

import net.rickiekarp.snakefx.view.FXMLFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Callback;

import java.io.IOException;

/**
 * This factory can be used to load FXML documents and get the root element of
 * the given fxml document.
 */
public class FxmlFactory {


	private final Callback<Class<?>, Object> controllerInjector;

	public FxmlFactory(final Callback<Class<?>, Object> injector) {
		controllerInjector = injector;
	}

	public Parent getFxmlRoot(final FXMLFile file) {
		final FXMLLoader loader = new FXMLLoader(file.url());

		loader.setControllerFactory(controllerInjector);

		try {
			loader.load();
		} catch (final IOException e) {
			throw new IllegalStateException("Can't load FXML file [" + file.url() + "]", e);
		}

		return loader.getRoot();
	}
}
