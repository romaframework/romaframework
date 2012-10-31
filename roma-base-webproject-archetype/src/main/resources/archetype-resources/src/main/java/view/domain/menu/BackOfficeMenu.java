#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view.domain.menu;

import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewClass;
import org.romaframework.frontend.view.domain.RomaControlPanel;
import org.romaframework.core.Roma;
import org.romaframework.module.users.view.domain.menu.UsersMenu;

@ViewClass(render = ViewConstants.RENDER_MENU)
@CoreClass(orderFields = "controlPanel users")
public class BackOfficeMenu {

	protected UsersMenu			users						= new UsersMenu();

	public UsersMenu getUsers() {
		return users;
	}
	
	public void controlPanel(){
		Roma.flow().forward(new RomaControlPanel(),"body");
	}
}
