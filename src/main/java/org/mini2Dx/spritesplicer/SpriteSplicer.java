/*******************************************************************************
 * Copyright 2018 Thomas Cashman
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.mini2Dx.spritesplicer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

/**
 *
 */
public class SpriteSplicer {

	public static void main(String [] args) throws Exception {
		final Options options = new Options();
		options.addRequiredOption("w", "width", true, "Width of individual frame");
		options.addRequiredOption("h", "height", true, "Height of individual frame");
		options.addRequiredOption("i", "input", true, "Input image filepath");
		
		final CommandLine commandLine = new DefaultParser().parse(options, args);
		final int frameWidth = Integer.parseInt(commandLine.getOptionValue('w'));
		final int frameHeight = Integer.parseInt(commandLine.getOptionValue('h'));
		final String inputImagePath = commandLine.getOptionValue('i');
		
		final File inputImageFile = new File(inputImagePath);
		System.out.println("Splitting " + inputImageFile.getName());
		
		final String inputImageName = inputImageFile.getName().substring(0, inputImageFile.getName().lastIndexOf('.'));
		final String inputImageExtension = inputImageFile.getName().substring(inputImageFile.getName().lastIndexOf('.') + 1);
		
		if(!inputImageFile.exists()) {
			throw new Exception("No such file " + inputImagePath);
		}
        final FileInputStream fileInputStream = new FileInputStream(inputImageFile);
        final BufferedImage inputImage = ImageIO.read(fileInputStream);
        
        int frameCounter = 0;
        for(int y = 0; y < inputImage.getHeight(); y += frameHeight) {
        	if(y + frameHeight > inputImage.getHeight()) {
    			continue;
    		}
        	
        	for(int x = 0; x < inputImage.getWidth(); x += frameWidth) {
        		if(x + frameWidth > inputImage.getWidth()) {
        			continue;
        		}
        		
        		final BufferedImage outputImage = new BufferedImage(frameWidth, frameHeight, inputImage.getType());
        		final Graphics2D graphics = outputImage.createGraphics();
        		graphics.drawImage(inputImage, 0, 0, frameWidth, frameHeight, frameWidth * x, frameHeight * y, (frameWidth * x) + frameWidth, (frameHeight * y) + frameHeight, null);
        		graphics.dispose();
        		
        		final File frameFile = new File(inputImageFile.getParentFile(), inputImageName + "_" + String.format("%03d", frameCounter) + "." + inputImageExtension);
        		ImageIO.write(outputImage, inputImageExtension, frameFile);
        		frameCounter++;
        		System.out.println("Writing " + frameFile.getName());
        	}
        }
        System.out.println("Done");
	}
}
