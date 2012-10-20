package org.romaframework.frontend.domain.image;

import java.util.ArrayList;
import java.util.List;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.view.ViewCallback;
import org.romaframework.aspect.view.ViewConstants;
import org.romaframework.aspect.view.annotation.ViewAction;
import org.romaframework.aspect.view.annotation.ViewField;
import org.romaframework.aspect.view.feature.ViewActionFeatures;
import org.romaframework.aspect.view.feature.ViewFieldFeatures;
import org.romaframework.core.Roma;

public class ImageGallery implements ViewCallback {

	public final static int			ATONCE_DEF_VALUE	= 4;
	public final static int			LARGE_DEF_VALUE		= 400;
	public final static String	URL_DEF_VALUE			= "";

	private int									progress					= 0;
	private int									index							= 0;
	private List<SizedImage>		images						= new ArrayList<SizedImage>();
	private List<SizedImage>		largeImages				= new ArrayList<SizedImage>();

	protected int								atOnce;
	protected int								width;
	protected int								largeWidth;
	protected String						url;

	public ImageGallery(String url, List<String> images, int width, int largeWidth, int atOnce) {
		super();
		this.atOnce = atOnce;
		this.width = width;
		this.largeWidth = largeWidth;
		this.url = url;
		initImages(images);
	}

	@ViewField(render = ViewConstants.RENDER_COLSET, label = "")
	public List<SizedImage> getImages() {
		List<SizedImage> views = new ArrayList<SizedImage>();
		for (int i = 0; i < (images.size()) && i < (atOnce); i++) {
			views.add(images.get(progress + i));
		}
		return views;
	}

	@ViewField(render = ViewConstants.RENDER_OBJECTEMBEDDED, label = "")
	public SizedImage getSelected() {
		return (conditionToViewSelected()) ? largeImages.get(index) : null;
	}

	public void back() {
		if (conditionToViewBack()) {
			deEvidenceSelection();
			index--;
			if (conditionToShiftLeft()) {
				progress--;
			}
			evidenceSelection();
			refAll();
		}
	}

	public void next() {
		if (conditionToViewNext()) {
			deEvidenceSelection();
			index++;
			if (conditionToShiftRight()) {
				progress++;
			}
			evidenceSelection();
			refAll();
		}
	}

	@ViewAction(visible = AnnotationConstants.FALSE)
	public void changePattern(String url, List<String> images) {
		this.url = url;
		initImages(images);
		refAll();
	}

	public void onDispose() {
	}

	public void onShow() {
		evidenceSelection();
		refAll();
	}

	private void initImages(List<String> images) {
		this.images.clear();

		for (String image : images) {
			this.images.add(new SizedImage(url + image, width));
			this.largeImages.add(new SizedImage(url + image, largeWidth));
		}

		refMySelect();
		refMyButton();
	}

	private void refMyButton() {
		Roma.setFeature(this, "back", ViewActionFeatures.ENABLED, conditionToViewBack());
		Roma.setFeature(this, "next", ViewActionFeatures.ENABLED, conditionToViewNext());
	}

	private void refMyImages() {
		Roma.fieldChanged(this, "images");
	}

	private void refMySelect() {
		Roma.setFeature(this, "selected", ViewFieldFeatures.VISIBLE, conditionToViewSelected());

		if (conditionToViewSelected())
			Roma.fieldChanged(this, "selected");
	}

	private void refAll() {
		refMyButton();
		refMyImages();
		refMySelect();
	}

	private void evidenceSelection() {
		if (conditionToViewSelected()) {
			images.get(index).setSelected(true);
			Roma.fieldChanged(images.get(index), "path");
		}
	}

	private void deEvidenceSelection() {
		images.get(index).setSelected(false);
		Roma.fieldChanged(images.get(index), "path");
	}

	private boolean conditionToViewBack() {
		return (index > 0);
	}

	private boolean conditionToViewNext() {
		return (index < images.size() - 1);
	}

	private boolean conditionToViewSelected() {
		return index < images.size();
	}

	private boolean conditionToShiftLeft() {
		return (index > 0) && (progress == index);
	}

	private boolean conditionToShiftRight() {
		return (index < images.size()) && (progress + atOnce == index);
	}

}
