#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view.domain.menu;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.logging.LoggingConstants;
import org.romaframework.aspect.logging.annotation.LoggingAction;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.module.users.ActivityLogCategories;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.domain.BaseProfile;
import org.romaframework.module.users.view.domain.ChangePassword;


@ViewClass(render = ViewConstants.RENDER_MENU)
@CoreClass(orderFields = "backOffice", orderActions = "homePage changePassword logout")
public class MainMenu implements MessageResponseListener {
	private BackOfficeMenu	backOffice	= new BackOfficeMenu();

	public void homePage() throws ClassNotFoundException {
		BaseProfile currProfile = ((BaseAccount)Roma.session().getAccount()).getProfile();

		String homePage = "HomePage";
		if (currProfile != null && currProfile.getHomePage() != null) {
			homePage = currProfile.getHomePage();
		}

		Roma.flow().forward(homePage, "body");
	}

	public void changePassword() {
		BaseAccount currAccount = (BaseAccount)Roma.session().getAccount();
		Roma.flow().popup(new ChangePassword(currAccount, this));
	}

  @LoggingAction(level = LoggingConstants.LEVEL_INFO, category = ActivityLogCategories.CATEGORY_LOGIN, post = "Logout")
	public void logout() {
		ObjectContext.getInstance().logout();
	}

	public BackOfficeMenu getBackOffice() {
		return backOffice;
	}

	/**
    * Called from ChangePassword.ok action.
    */
	public void responseMessage(Message iMessage, Object iResponse) {
		try {
			homePage();
		} catch (ClassNotFoundException e) {
		}
	}
}
