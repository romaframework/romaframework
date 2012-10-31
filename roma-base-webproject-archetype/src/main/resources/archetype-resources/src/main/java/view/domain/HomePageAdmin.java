#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package  ${package}.view.domain;

import org.romaframework.core.Roma;
import org.romaframework.frontend.domain.page.HomePageBasic;

public class HomePageAdmin extends HomePageBasic {

	public HomePageAdmin(){
		Roma.flow().forward(new AdminHeader(),"header");
		System.out.println(this.getClass().getPackage().getName());
		
	}
	
	@Override
	protected void fillPages() {
		super.fillPages();
	}
	
	

}