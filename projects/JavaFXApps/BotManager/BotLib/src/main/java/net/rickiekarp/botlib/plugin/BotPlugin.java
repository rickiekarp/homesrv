package net.rickiekarp.botlib.plugin;

import net.rickiekarp.botlib.runner.BotRunner;

/**
 * Interface for the plugin classes.
 */
public interface BotPlugin {

	/**
	 * Performs the plugin layout node action.
	 */
	void setLayout(BotRunner runner);

	/**
	 * Performs the plugin action.
	 */
	void run(BotRunner runner);
}