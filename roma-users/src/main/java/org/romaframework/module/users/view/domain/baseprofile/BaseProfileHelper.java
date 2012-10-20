package org.romaframework.module.users.view.domain.baseprofile;

public class BaseProfileHelper {
	
  public static final byte     MODE_ALLOW_ALL_BUT_VALUE = 0;
  public static final byte     MODE_DENY_ALL_BUT_VALUE  = 1;

  public static final String   MODE_ALLOW_ALL_BUT       = "Allow all but";
  public static final String   MODE_DENY_ALL_BUT        = "Deny all but";

  public static final String[] MODES                    = { MODE_ALLOW_ALL_BUT, MODE_DENY_ALL_BUT };
}
