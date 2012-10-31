#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.romaframework.aspect.authentication.LoginListener;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.logging.annotation.LoggingAction;
import org.romaframework.aspect.view.command.impl.RedirectViewCommand;
import org.romaframework.core.Roma;
import org.romaframework.core.config.AbstractApplicationConfiguration;
import org.romaframework.module.users.ActivityLogCategories;
import org.romaframework.module.users.listener.DefaultLoginListener;
import org.romaframework.module.users.view.domain.Login;

public class CustomApplicationConfiguration extends AbstractApplicationConfiguration {
  @LoggingAction(category = ActivityLogCategories.CATEGORY_SYSTEM, level = LoggingConstants.LEVEL_INFO, post = "Application startup")
	public void startup() {
		// INSERT APPLICATION STARTUP HERE
	}

  @LoggingAction(category = ActivityLogCategories.CATEGORY_SYSTEM, level = LoggingConstants.LEVEL_INFO, post = "Application Shutdown")
	public void shutdown() {
		// INSERT APPLICATION SHUTDOWN HERE
	}

	/**
	 * Callback called on every user connected to the application
	 */
	public void startUserSession() {
		login(new DefaultLoginListener());
	}

	/**
	 * Login into the application.
	 */
	public void login(LoginListener iListener) {
		Login form = new Login();
		form.setListener(iListener);
		Roma.flow().forward(form);
	}
	
	/**
	 * Callback called on every user disconnected from application
	 */
	public void endUserSession() {
		Roma.view().pushCommand(new RedirectViewCommand(Roma.view().getContextPath()+"/dynamic/logout.jsp"));
	}

	public String getStatus() {
		return STATUS_UNKNOWN;
	}
}
