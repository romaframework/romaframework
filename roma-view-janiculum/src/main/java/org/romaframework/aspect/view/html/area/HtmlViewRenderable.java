package org.romaframework.aspect.view.html.area;

import java.io.IOException;
import java.io.Writer;

import org.romaframework.aspect.view.html.transformer.Transformer;

public interface HtmlViewRenderable {

	/**
	 * Write the html of the entire component
	 * @throws IOException 
	 */
	public void render(Writer writer) throws IOException;

	/**
	 * Write the html of a part of the component
	 * 
	 * @param part
	 *          the part to render
	 */
	public void renderPart(String part, Writer writer) throws IOException;

	/**
	 * Get the ID used to bind the request to the components
	 * 
	 * @return
	 */
	public long getId();

	/**
	 * Get the transformer to be used for the render
	 * 
	 * @return
	 */
	public Transformer getTransformer();

	/**
	 * Get the id to be used as tag Id in the produced code
	 * 
	 * @return
	 */
	public String getHtmlId();

	/**
	 * Validate the component
	 * 
	 * @return
	 */
	public boolean validate();

	/**
	 * Reset the previous validation
	 */
	public void resetValidation();
	
}
