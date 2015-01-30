/*******************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2014 Red Hat, Inc.
 *  All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Red Hat, Inc. - initial API and implementation
 *
 * @author Bob Brodt
 ******************************************************************************/

package org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation.validators;

import java.util.List;

import org.eclipse.bpmn2.EndEvent;
import org.eclipse.bpmn2.FlowElement;
import org.eclipse.bpmn2.Process;
import org.eclipse.bpmn2.StartEvent;
import org.eclipse.bpmn2.modeler.core.utils.ModelUtil;
import org.eclipse.bpmn2.modeler.core.validation.SyntaxCheckerUtils;
import org.eclipse.bpmn2.modeler.core.validation.validators.AbstractBpmn2ElementValidator;
import org.eclipse.bpmn2.modeler.core.validation.validators.BaseElementValidator;
import org.eclipse.bpmn2.modeler.core.validation.validators.FlowElementsContainerValidator;
import org.eclipse.bpmn2.modeler.runtime.jboss.jbpm5.validation.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.validation.IValidationContext;

public class ProcessValidator extends AbstractBpmn2ElementValidator<Process> {

	/**
	 * @param ctx
	 */
	public ProcessValidator(IValidationContext ctx) {
		super(ctx);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.bpmn2.modeler.core.validation.validators.AbstractBpmn2ElementValidator#validate(org.eclipse.bpmn2.BaseElement)
	 */
	@Override
	public IStatus validate(Process object) {
		boolean foundStartEvent = false;
		boolean foundEndEvent = false;
		List<FlowElement> flowElements = object.getFlowElements();
		for (FlowElement fe : flowElements) {
			if (fe instanceof StartEvent) {
				foundStartEvent = true;
			}
			if (fe instanceof EndEvent) {
				foundEndEvent = true;
			}
		}
		if (!foundStartEvent) {
			addStatus(object, Status.WARNING, "Process has no Start Event");
		}
		if (!foundEndEvent) {
			addStatus(object, Status.WARNING, "Process has no End Event");
		}
		
		if (isEmpty(object.getName())) {
			addStatus(object, "name", Status.WARNING, "Process {0} has no name", object.getId());
		}
		
		EStructuralFeature feature;
		feature = ModelUtil.getAnyAttribute(object, "packageName"); //$NON-NLS-1$
		String name = null;
		if (feature!=null) {
			name = (String) object.eGet(feature);
		}
		if (name==null || name.isEmpty()) {
			addStatus(object, "packageName", Status.ERROR, Messages.ProcessConstraint_No_Package_Name, object.getName(), object.getId());
		}
		else if (!SyntaxCheckerUtils.isJavaPackageName(name)) {
			addStatus(object, "packageName", Status.ERROR, "Package name is invalid: {0}", name);
		}

		name = object.getName();
		if (name==null || name.isEmpty()) {
			addStatus(object, "name", Status.ERROR, Messages.ProcessConstraint_No_Process_Name, object.getId());
		}
		
		feature = ModelUtil.getAnyAttribute(object, "adHoc"); //$NON-NLS-1$
		if (feature!=null) {
			Object v = object.eGet(feature);
			Boolean adHoc = false;
			if (v instanceof String)
				adHoc = Boolean.parseBoolean((String)v);
			else if (v instanceof Boolean)
				adHoc = (Boolean)v;
			if (!adHoc.booleanValue()) {
				// This is not an ad-hoc process:
				// need to make sure all nodes are connected,
				// same as core BPMN2
				new FlowElementsContainerValidator(this).validate(object);
			}
		}
		else {
			// Default value for missing "adHoc" attribute is "false"
			new FlowElementsContainerValidator(this).validate(object);
		}
		
		new BaseElementValidator(this).validate(object);
		
		return getResult();
	}
}