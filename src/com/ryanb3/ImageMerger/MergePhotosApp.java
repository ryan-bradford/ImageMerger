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
	
	public static void loadImages() {
		for (int i = 1; i <= 112; i++) {
			if(i < 10) {
				images.add(MarvinImageIO.loadImage("/Users/Ryan/Documents/Programming/ImageMerger/Pictures/VKNX0338 00" + i + ".jpg"));
			} else if(i < 100) {
				images.add(MarvinImageIO.loadImage("/Users/Ryan/Documents/Programming/ImageMerger/Pictures/VKNX0338 0" + i + ".jpg"));
			} else {
				images.add(MarvinImageIO.loadImage("/Users/Ryan/Documents/Programming/ImageMerger/Pictures/VKNX0338 " + i + ".jpg"));
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
	
	public static void combineImagesMyWay() {
		int[][] redTotal = new int[output.getWidth()][output.getHeight()];
		int[][] greenTotal = new int[output.getWidth()][output.getHeight()];
		int[][] blueTotal = new int[output.getWidth()][output.getHeight()];
		for(int i = 0; i < images.size(); i++) {
			for(int x = 0; x < images.get(i).getWidth(); x++) {
				for(int y = 0; y < images.get(i).getHeight(); y++) {
					redTotal[x][y] += images.get(i).getIntComponent0(x, y);
					greenTotal[x][y] += images.get(i).getIntComponent1(x, y);
					blueTotal[x][y] += images.get(i).getIntComponent2(x, y);
				}
			}
		}
		
		int divisorFactor = images.size() / 2;
		
		
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				redTotal[x][y] /= divisorFactor;
				greenTotal[x][y] /= divisorFactor;
				blueTotal[x][y] /= divisorFactor;

			}
		}
		for(int x = 0; x < output.getWidth(); x++) {
			for(int y = 0; y < output.getHeight(); y++) {
				output.setIntColor(x, y, new Color(redTotal[x][y], greenTotal[x][y], blueTotal[x][y]).hashCode());
			}
		}
	}
	
	public static void writeImage() {
		MarvinImageIO.saveImage(output, "merge_output1.jpg");
	}

	public static void main(String[] args) {
		loadImages();
		//combineImagesThroughMarvin();
		combineImagesMyWay();
		writeImage();
	}
}