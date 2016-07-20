/*
 * © 2016 Guilherme Rios All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package com.github.gasrios.raw;

import java.io.FileInputStream;

import com.github.gasrios.raw.lang.Math;
import com.github.gasrios.raw.lang.TiffProcessorException;
import com.github.gasrios.raw.processor.LoadHighResolutionImage;
import com.github.gasrios.raw.processor.TiffProcessorEngine;
import com.github.gasrios.raw.swing.ImageFrame;
import com.github.gasrios.raw.swing.ImageSRGB;

public class BlackAndWhite extends LoadHighResolutionImage {

	private static final boolean IGNORE_UNKNOWN_TAGS = true;

	public static void main(String[] args) throws Exception {
		new TiffProcessorEngine(new FileInputStream(args[0]), new BlackAndWhite(), IGNORE_UNKNOWN_TAGS).run();
	}

	@Override public void end() throws TiffProcessorException {

		ImageSRGB imageSRGB = new ImageSRGB(blackAndWhite(image));

		// Does not seem to make much of a difference in practice, but just in case let's try and free some memory here.
		image = null;
		System.gc();

		new ImageFrame(imageSRGB, 1075, 716);

	}

	public static double[][][] blackAndWhite(double[][][] image) {
		for (int i = 0; i < image.length; i++) for (int j = 0; j < image[0].length; j ++) {
			double[] lsh = Math.luv2lsh(Math.xyz2luv(image[i][j]));
			lsh[1] = 0;
			image[i][j] = Math.luv2xyz(Math.lsh2luv(lsh));
		}
		return image;
	}

}