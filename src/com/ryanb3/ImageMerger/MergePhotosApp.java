package com.ryanb3.ImageMerger;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;
import marvin.plugin.MarvinImagePlugin;
import marvin.util.MarvinPluginLoader;

public class MergePhotosApp {

	static List<MarvinImage> images = new ArrayList<MarvinImage>();
	static MarvinImage output;
	static double threshhold = 50;
	static double scalePower = 3;
	
	public static void loadImages() {
		for (int i = 1; i <= 112; i++) {
			if(i < 10) {
				images.add(MarvinImageIO.loadImage("Pictures/VKNX0338 00" + i + ".jpg"));
			} else if(i < 100) {
				images.add(MarvinImageIO.loadImage("Pictures/VKNX0338 0" + i + ".jpg"));
			} else {
				images.add(MarvinImageIO.loadImage("Pictures/VKNX0338 " + i + ".jpg"));
			}
		}
		output = images.get(0).clone();
	}
	
	public static void combineImagesThroughMarvin() {
		// 2. Load plug-in and process the image
		MarvinImagePlugin merge = MarvinPluginLoader.loadImagePlugin("org.marvinproject.image.combine.mergePhotos");
		merge.setAttribute("threshold", 34);
		merge.process(images, output);
	}
	
	public static void combineImagesMyWayWithThreashold() {
		int[][] redTotal = new int[output.getWidth()][output.getHeight()];
		int[][] greenTotal = new int[output.getWidth()][output.getHeight()];
		int[][] blueTotal = new int[output.getWidth()][output.getHeight()];
		double[][] redDivideTotal = new double[output.getWidth()][output.getHeight()];
		double[][] greenDivideTotal = new double[output.getWidth()][output.getHeight()];
		double[][] blueDivideTotal = new double[output.getWidth()][output.getHeight()];
		for(int i = 0; i < images.size(); i++) {
			for(int x = 0; x < images.get(i).getWidth(); x++) {
				for(int y = 0; y < images.get(i).getHeight(); y++) {
					if(images.get(i).getIntComponent0(x, y) > threshhold) {
						redDivideTotal[x][y]++;
						redTotal[x][y] += images.get(i).getIntComponent0(x, y);
					} else {
						redDivideTotal[x][y] += threshhold / 255;
					}
					if(images.get(i).getIntComponent1(x, y) > threshhold) {
						greenDivideTotal[x][y]++;
						greenTotal[x][y] += images.get(i).getIntComponent1(x, y);
					} else {
						greenDivideTotal[x][y] += threshhold / 255;
					}
					if(images.get(i).getIntComponent2(x, y) > threshhold) {
						blueDivideTotal[x][y]++;
						blueTotal[x][y] += images.get(i).getIntComponent2(x, y);
					} else {
						blueDivideTotal[x][y] += threshhold / 255;
					}
				}
			}
		}		
		
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				redTotal[x][y] /= Math.ceil(redDivideTotal[x][y]);
				greenTotal[x][y] /= Math.ceil(greenDivideTotal[x][y]);
				blueTotal[x][y] /= Math.ceil(blueDivideTotal[x][y]);;

			}
		}
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				output.setIntColor(x, y, new Color(redTotal[x][y], greenTotal[x][y], blueTotal[x][y]).hashCode());
			}
		}
	}
	
	public static void combineImagesMyWayWithScaling() {
		int[][] redTotal = new int[output.getWidth()][output.getHeight()];
		int[][] greenTotal = new int[output.getWidth()][output.getHeight()];
		int[][] blueTotal = new int[output.getWidth()][output.getHeight()];
		for(int i = 0; i < images.size(); i++) {
			for(int x = 0; x < images.get(i).getWidth(); x++) {
				for(int y = 0; y < images.get(i).getHeight(); y++) {
					redTotal[x][y] += Math.pow(images.get(i).getIntComponent0(x, y), scalePower);					
					greenTotal[x][y] += Math.pow(images.get(i).getIntComponent1(x, y), scalePower);					
					blueTotal[x][y] += Math.pow(images.get(i).getIntComponent2(x, y), scalePower);
					
				}
			}
		}		
		
		int divisor = images.size();
		
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				redTotal[x][y] /= Math.ceil(divisor);
				greenTotal[x][y] /= Math.ceil(divisor);
				blueTotal[x][y] /= Math.ceil(divisor);;

			}
		}
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				output.setIntColor(x, y, new Color((int)Math.pow(redTotal[x][y], 1.0/scalePower), (int)Math.pow(greenTotal[x][y], 1.0/scalePower), (int)Math.pow(blueTotal[x][y], 1.0/scalePower)).hashCode());
			}
		}
	}
	
	public static void writeImage() {
		MarvinImageIO.saveImage(output, "merge_output2.jpg");
	}

	public static void main(String[] args) {
		loadImages();
		//combineImagesThroughMarvin();
		combineImagesMyWayWithScaling();
		//combineImagesMyWayWithThreashold();
		writeImage();
	}
}