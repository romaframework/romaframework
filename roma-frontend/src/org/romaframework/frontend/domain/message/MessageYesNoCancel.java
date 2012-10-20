/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.frontend.domain.message;

/**
 * @author Luca Molino
 * 
 *         That class extends MessageYesNo class with the Cancel option, as the same of MessageYesNo returns a Boolean:
 * @return true if Yes selected
 * @return false if No selected
 * @return null if Cancel selected
 * 
 */
public class MessageYesNoCancel extends MessageYesNo {
	public MessageYesNoCancel(String iId, String iTitle, MessageResponseListener iListener) {
		super(iId, iTitle, iListener);
	}

	public MessageYesNoCancel(String iId, String iTitle, MessageResponseListener iListener, String iMessage) {
		super(iId, iTitle, iListener, iMessage);
	}

	public MessageYesNoCancel(String iId, String iTitle) {
		super(iId, iTitle);
	}

	public void cancel() {
		close();
		setResponse(null);
	}

}
