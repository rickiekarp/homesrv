package com.rkarp.appcore.view.layout;

import com.rkarp.appcore.AppContext;
import com.rkarp.appcore.account.Account;
import com.rkarp.appcore.debug.LogFileHandler;
import com.rkarp.appcore.net.NetResponse;
import com.rkarp.appcore.net.NetworkApi;
import com.rkarp.appcore.view.MessageDialog;
import com.squareup.okhttp.Response;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.logging.Level;

/**
 * Main Login Mask layout class.
 */
class RegistrationLayout {
    private VBox main;
    private GridPane grid;
    private Label loginLabel;
    private TextField username;
    private PasswordField password;

    RegistrationLayout() {
        main = new VBox();
        main.setSpacing(20);
        main.setAlignment(Pos.CENTER);
        grid = new GridPane();
        //grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        loginLabel = new Label("Register");
        main.getChildren().add(loginLabel);

        Label usernameLabel = new Label("User");
        grid.add(usernameLabel, 0, 1);

        username = new TextField();
        grid.add(username, 1, 1);

        Label passwordLabel = new Label("Password");
        grid.add(passwordLabel, 0, 2);

        password = new PasswordField();
        grid.add(password, 1, 2);

        Button registerButton = new Button("Submit");
        registerButton.setOnAction(arg0 -> {
            if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                Account account = new Account(username.getText(), password.getText());
                InputStream inputStream = AppContext.getContext().getNetworkApi().runNetworkAction(NetworkApi.requestCreateAccount(account));
                JSONObject responseJson = NetResponse.getResponseJson(inputStream);
            } else {
                new MessageDialog(0, "Enter account details!", 400, 200);
            }
        });

        main.getChildren().add(grid);
        main.getChildren().add(registerButton);
    }

    public Node getMaskNode() {
        return main;
    }

    private boolean requestLogin(Account account) {
        Response tokenAction = AppContext.getContext().getNetworkApi().requestResponse(
                NetworkApi.requestAccessToken(account)
        );

        LogFileHandler.logger.log(Level.INFO, String.valueOf(tokenAction.code()));
        switch (tokenAction.code()) {
            case 200:
                AppContext.getContext().getAccountManager().setAccount(account);
                return true;
            default:
                return false;
        }
    }
}
