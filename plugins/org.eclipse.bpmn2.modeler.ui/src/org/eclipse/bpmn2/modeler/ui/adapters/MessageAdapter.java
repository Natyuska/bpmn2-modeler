/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpmn2.modeler.ui.adapters;

import org.eclipse.bpmn2.modeler.core.adapters.AbstractAdapter;
import org.eclipse.bpmn2.modeler.ui.Activator;
import org.eclipse.bpmn2.modeler.ui.IConstants;
import org.eclipse.bpmn2.modeler.ui.Messages;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.wsdl.Message;

public class MessageAdapter extends AbstractAdapter implements ILabeledElement {

	/* ILabeledElement */
	
	public Image getSmallImage(Object object) {
		return Activator.getDefault().getImage(IConstants.ICON_MESSAGE_16);
	}
	
	public Image getLargeImage(Object object) {
		return Activator.getDefault().getImage(IConstants.ICON_MESSAGE_32);
	}	
	
	public String getTypeLabel(Object object) {
		return Messages.MessageAdapter_Message_1; 
	}	
	
	public String getLabel (Object object) {
		if (object == null){
			return null;
		}
		Message msg = (Message)object;
		if (msg.getQName() != null) {
			if (msg.getQName().getLocalPart() != null)  return msg.getQName().getLocalPart();
		}
		return getTypeLabel(object);
	}
}