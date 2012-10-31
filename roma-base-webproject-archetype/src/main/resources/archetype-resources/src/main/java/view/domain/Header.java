#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package  ${package}.view.domain;

import org.romaframework.aspect.flow.annotation.FlowAction;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.crud.CRUDHelper;
import org.romaframework.frontend.domain.message.Message;
import org.romaframework.frontend.domain.message.MessageResponseListener;
import org.romaframework.frontend.view.domain.RomaControlPanel;
import org.romaframework.module.users.domain.BaseAccount;
import org.romaframework.module.users.view.domain.ChangePassword;

public class Header implements MessageResponseListener {

	public Header() {
	}

	@ViewAction(label = "", render = ViewConstants.RENDER_BUTTON)
	public void home() {
		BaseAccount currAccount = Roma.session().getAccount();
		Roma.flow().forward(currAccount.getProfile().getHomePage(),"body");
	}

	@ViewAction(label = "", render = ViewConstants.RENDER_BUTTON)
	public void changePassword() {
		BaseAccount currAccount = Roma.session().getAccount();
		Roma.flow().popup(new ChangePassword(currAccount, this));
	}

	@ViewAction(label = "", render = ViewConstants.RENDER_BUTTON)
	@FlowAction(next = RomaControlPanel.class, position="body")
	public void controlPanel() {
	}

	public void responseMessage(Message iMessage, Object iResponse) {
		home();
	}

}
