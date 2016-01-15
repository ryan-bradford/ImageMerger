package com.ryanb3.ImageMerger;

import java.awt.Color;
import java.util.List;

import javax.swing.JOptionPane;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

public class Merger {
	
	int[][][] imageRed;
	int[][][] imageGreen;
	int[][][] imageBlue;
	List<MarvinImage> images;
	MarvinImage baseImage;
	int scalePower;
	
	public Merger(List<MarvinImage> images, MarvinImage baseImage) {
		this.images = images;
		this.baseImage = baseImage;
	}
	
	public void createArrays() {
		imageRed = new int[images.size()][baseImage.getWidth()][baseImage.getHeight()];
		imageBlue = new int[images.size()][baseImage.getWidth()][baseImage.getHeight()];
		imageGreen = new int[images.size()][baseImage.getWidth()][baseImage.getHeight()];
		for (int i = 0; i < images.size(); i++) {
			for (int x = 0; x < images.get(i).getWidth(); x++) {
				for (int y = 0; y < images.get(i).getHeight(); y++) {
					imageRed[i][x][y] = images.get(i).getIntComponent0(x, y);
					imageGreen[i][x][y] = images.get(i).getIntComponent1(x, y);
					imageBlue[i][x][y] = images.get(i).getIntComponent2(x, y);
				}
			}
		}
	}
	
	public void setThreshold() {
		double threshold = Integer.parseInt(JOptionPane.showInputDialog("What percent of color would you like to drop?"));
		threshold /= 100;
		threshold *= 255;
		for (int i = 0; i < imageRed.length; i++) {
			for (int x = 0; x < imageRed[i].length; x++) {
				for (int y = 0; y < imageRed[i][x].length; y++) {
					if (imageRed[i][x][y] < threshold) {
						imageRed[i][x][y] = 0;
					}
					if (imageGreen[i][x][y] < threshold) {
						imageGreen[i][x][y] = 0;
					}
					if (imageBlue[i][x][y] < threshold) {
						imageBlue[i][x][y] = 0;
					}
				}
			}
		}
	}
	
	public void scaleValues() {
		scalePower = Integer.parseInt(JOptionPane.showInputDialog("What power would you like to scale to?"));
		for (int i = 0; i < imageRed.length; i++) {
			for (int x = 0; x < imageRed[i].length; x++) {
				for (int y = 0; y < imageRed[i][x].length; y++) {
					imageRed[i][x][y] = (int) Math.pow(imageRed[i][x][y], scalePower);
					imageGreen[i][x][y] = (int) Math.pow(imageGreen[i][x][y], scalePower);
					imageBlue[i][x][y] = (int) Math.pow(imageBlue[i][x][y], scalePower);

				}
			}
		}
	}
	
	public void ceilValues() {
		int ceilLower = Integer.parseInt(JOptionPane.showInputDialog("What is the lower limit for the ceiling?"));
		for (int i = 0; i < imageRed.length; i++) {
			for (int x = 0; x < imageRed[i].length; x++) {
				for (int y = 0; y < imageRed[i][x].length; y++) {
					if(imageRed[i][x][y] > ceilLower) {
						imageRed[i][x][y] = 255;
					}
					if(imageGreen[i][x][y] > ceilLower) {
						imageGreen[i][x][y] = 255;
					}
					if(imageBlue[i][x][y] > ceilLower) {
						imageBlue[i][x][y] = 255;
					}
				}
			}
		}
	}
	
	public MarvinImage combineImagesThroughMarvin(List<MarvinImage> images) {
		MarvinImage output = images.get(0).clone();
		MarvinImagePlugin merge = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.combine.mergePhotos");
		merge.setAttribute("threshold", 34);
		merge.process(images, output);
		return output;
	}
	
	public MarvinImage combineImages(boolean didScale, int[][][] imageRed, int[][][] imageBlue, int[][][] imageGreen, MarvinImage baseImage) {
		MarvinImage output = images.get(0).clone();
		int[][] redTotal = new int[baseImage.getWidth()][baseImage.getHeight()];
		int[][] greenTotal = new int[baseImage.getWidth()][baseImage.getHeight()];
		int[][] blueTotal = new int[baseImage.getWidth()][baseImage.getHeight()];
		double divisors = 0;
		for (int i = 0; i < imageRed.length; i++) {
			divisors += 1;
			for (int x = 0; x < imageRed[i].length; x++) {
				for (int y = 0; y < imageRed[i][x].length; y++) {
					redTotal[x][y] += imageRed[i][x][y];
					greenTotal[x][y] += imageGreen[i][x][y];
					blueTotal[x][y] += imageBlue[i][x][y];
				}
			}
		}
		divisors /= 8;
		for (int x = 0; x < output.getWidth(); x++) {
			for (int y = 0; y < output.getHeight(); y++) {
				if (didScale) {
					output.setIntColor(x, y,
							new Color((int) (Math.pow(redTotal[x][y], 1.0 / scalePower) / divisors),
									(int) (Math.pow(greenTotal[x][y], 1.0 / scalePower) / divisors),
									(int) ((Math.pow(blueTotal[x][y], 1.0 / scalePower)) / divisors)).hashCode());
				} else {
					output.setIntColor(x, y, new Color((int) (redTotal[x][y] / divisors),
							(int) (greenTotal[x][y] / divisors), (int) (blueTotal[x][y] / divisors)).hashCode());

				}
			}
		}
		return output;

	}
	
	public void writeImage(MarvinImage output) {
		MarvinImageIO.saveImage(output, JOptionPane.showInputDialog("Enter the name of the new File") + ".jpg");
	}
	
	public int[][][] getRed() {
		return imageRed;
	}
	
	public int[][][] getGreen() {
		return imageGreen;
	}
	
	public int[][][] getBlue() {
		return imageBlue;
	}
	
	public List<MarvinImage> getImages() {
		return images;
	}
	
}
