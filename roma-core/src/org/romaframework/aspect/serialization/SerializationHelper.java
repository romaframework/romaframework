/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
package org.romaframework.aspect.serialization;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaFeatures;

/**
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 */
public class SerializationHelper {

	/**
	 * Transform the features to make serializable
	 * 
	 * @param features
	 */
	public static void allowSerializeFeatures(SchemaFeatures features) {
		SchemaClass schemaClass = (SchemaClass) features.getFeature(CoreFieldFeatures.EMBEDDED_TYPE);
		if (schemaClass != null) {
			features.setFeature(CoreFieldFeatures.EMBEDDED_TYPE, schemaClass);
		}
	}

	/**
	 * Realline the features on deserialization.
	 * 
	 * @param features
	 */
	public static void reallineFeature(SchemaFeatures features) {
	}


	/**
	 * Transform an input stream to a string.
	 * 
	 * @param is
	 *          input to read.
	 * @return result String
	 */
	public static String inputStreamToString(java.io.InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception ex) {
			throw new SerializationException("Error on Input Stream read.", ex);
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
		return sb.toString();
	}

}
