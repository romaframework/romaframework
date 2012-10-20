package org.romaframework.frontend.domain.wrapper;

import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.core.schema.SchemaHelper;

/**
 * An extension of SelectionBoxElement that allows to keep the selected elements list ordered
 * 
 * @author Luigi Quarta
 * 
 */
public class SortedSelectionBoxElement<T> extends SelectionBoxElement<T> {

	public SortedSelectionBoxElement(Object iInstance, String iFieldName, List<T> sourceList) throws IllegalArgumentException {
		super(iInstance, iFieldName, sourceList);
	}

	protected SortedSelectionBoxElement(Object iInstance, String iFieldName) throws IllegalArgumentException {
		super(iInstance, iFieldName);
	}

	protected SortedSelectionBoxElement(Object iInstance, String iFieldName, Class<T> iClass) throws IllegalArgumentException {
		super(iInstance, iFieldName, iClass);
	}

	@SuppressWarnings("unchecked")
	@ViewAction(label = "&uarr;")
	public void moveUp() {
		int elementIndex = -1;

		if (selectedElementSelected != null)
			elementIndex = selectedElements.indexOf(selectedElementSelected);

		if (elementIndex > 0 && elementIndex < selectedElements.size()) {
			selectedElements.remove(selectedElementSelected);
			selectedElements.add(elementIndex - 1, selectedElementSelected);
			Object value = SchemaHelper.getFieldValue(instance, fieldName);
			if (value instanceof List<?>) {
				((List<T>) value).remove(selectedElementSelected);
				((List<T>) value).add(elementIndex - 1, selectedElementSelected);
			} else {
				throw new IllegalArgumentException("Field " + fieldName + " isn't a collection.");
			}
			refreshSelectedElements();
		}
	}

	@SuppressWarnings("unchecked")
	@ViewAction(label = "&darr;")
	public void moveDown() {
		int elementIndex = -1;

		if (selectedElementSelected != null)
			elementIndex = selectedElements.indexOf(selectedElementSelected);

		if (elementIndex >= 0 && elementIndex < (selectedElements.size() - 1)) {
			selectedElements.remove(selectedElementSelected);
			selectedElements.add(elementIndex + 1, selectedElementSelected);
			Object value = SchemaHelper.getFieldValue(instance, fieldName);
			if (value instanceof List<?>) {
				((List<T>) value).remove(selectedElementSelected);
				((List<T>) value).add(elementIndex + 1, selectedElementSelected);
			} else {
				throw new IllegalArgumentException("Field " + fieldName + " isn't a collection.");
			}
			refreshSelectedElements();
		}
	}

	@ViewAction(label = ">>")
	public void addAll() throws IllegalArgumentException {
		super.addAll();
	}

	@ViewAction(label = ">", visible = AnnotationConstants.TRUE)
	public void selectElement() throws IllegalArgumentException {
		super.selectElement();
	}

	@ViewAction(label = "<", visible = AnnotationConstants.TRUE)
	public void unselectElement() throws IllegalArgumentException {
		super.unselectElement();
	}

	@ViewAction(label = "<<")
	public void removeAll() throws IllegalArgumentException {
		super.removeAll();
	}

	@Override
	protected void afterSelectedElementSelection() {
	}

	@Override
	protected void afterAvailableElementSelection() {
	}
}