package net.rickiekarp.core.view.login

import net.rickiekarp.core.controller.LanguageController
import net.rickiekarp.core.ui.windowmanager.ThemeSelector
import net.rickiekarp.core.ui.windowmanager.WindowScene
import net.rickiekarp.core.ui.windowmanager.WindowStage
import net.rickiekarp.core.ui.windowmanager.ImageLoader
import net.rickiekarp.core.view.MainScene
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage

/**
 * This class is used for creating different message dialogs.
 * Example: Error Message
 */
class AccountScene {
    init {
        create(500, 400)
    }

    fun create(width: Int, height: Int) {
        val stage = Stage()
        stage.icons.add(ImageLoader.getAppIconSmall())
        stage.width = (width + 50).toDouble()
        stage.height = (height + 50).toDouble()
        stage.minWidth = width.toDouble()
        stage.minHeight = height.toDouble()
        stage.title = LanguageController.getString("account")
        stage.initModality(Modality.APPLICATION_MODAL)
        val windowStage = WindowStage("account", stage)

        //Layout
        val contentPane = BorderPane()

        val modalDialogScene = WindowScene(windowStage, contentPane, 1)

        val vbox = VBox()
        vbox.padding = Insets(0.0, 0.0, 0.0, 20.0)

        val controls = HBox()
        controls.padding = Insets(10.0, 0.0, 10.0, 0.0)  //padding top, left, bottom, right
        controls.alignment = Pos.CENTER

        val progress = ProgressIndicator()
        progress.progress = ProgressIndicator.INDETERMINATE_PROGRESS
        vbox.children.add(progress)

        val listview = ListView<String>()
        listview.padding = Insets(10.0, 0.0, 0.0, 0.0)
        listview.style = "-fx-font-size: 11pt;"

        val okButton = Button("OK")
        okButton.setOnAction { arg0 -> modalDialogScene.win.controller!!.close() }
        controls.children.add(okButton)

        // The UI (Client Area) to display
        contentPane.center = vbox
        contentPane.bottom = controls

        ThemeSelector.setTheme(modalDialogScene, this.javaClass.classLoader)

        stage.scene = modalDialogScene
        stage.show()

        MainScene.stageStack.push(windowStage)
    }
}
