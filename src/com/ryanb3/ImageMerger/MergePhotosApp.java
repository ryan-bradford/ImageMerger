package com.ryanb3.ImageMerger;

import java.awt.FileDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

public class MergePhotosApp {
	
	JFrame dialogDisplay;
	Merger merger;
	FileDialog fd;

	public List<MarvinImage> loadImages(File[] files) {
		List<MarvinImage> images = new ArrayList<MarvinImage>();
		for (int i = 0; i < files.length; i++) {
			images.add(MarvinImageIO.loadImage(files[i].toString()));
		}
		return images;
	}

	public void handleMarvinOption() {
		int option1 = JOptionPane.showConfirmDialog(null, "Would you like to combine but seeking color difference?");
		if (option1 == 0) {
			MarvinImage toWrite = merger.combineImagesThroughMarvin(merger.getImages());
			merger.writeImage(toWrite);
			finish(true, false);
		} else if (option1 == 1) {
			handleScaleOption(false);
		} else {
			startMerger(false);
		}
	}

	public void handleScaleOption(boolean didMarvin) {
		int option2 = JOptionPane.showConfirmDialog(null, "Would you like to scale the values?");
		if (option2 == 0) {
			merger.scaleValues();
			handleThresholdOption(didMarvin, true);
		} else if (option2 == 1) {
			handleThresholdOption(didMarvin, false);
		} else {
			startMerger(false);
		}
	}

	public void handleThresholdOption(boolean didMarvin, boolean didScale) {
		int option3 = JOptionPane.showConfirmDialog(null, "Would you like to apply a threshold?");
		if (option3 == 0) {
			merger.setThreshold();
			handleCeilOption(didMarvin, didScale);
		} else if (option3 == 1) {
			handleCeilOption(didMarvin, didScale);
		} else {
			startMerger(false);
		}
	}
	
	public void handleCeilOption(boolean didMarvin, boolean didScale) {
		int option4 = JOptionPane.showConfirmDialog(null, "Would you like to apply a ceiling?");
		if (option4 == 0) {
			merger.ceilValues();
			finish(didMarvin, didScale);
		} else if(option4 == 1) {
			finish(didMarvin, didScale);
		} else {
			startMerger(false);
		}
	}

	public void finish(boolean didMarvin, boolean didScale) {
		if (!didMarvin) {
			MarvinImage toWrite = merger.combineImages(didScale, merger.getRed(), merger.getGreen(), merger.getBlue(), merger.getImages().get(0).clone());
			merger.writeImage(toWrite);
		}
		startMerger(false);
	}

	public void startMerger(boolean fromCrash) {
		if (fromCrash) {
			JOptionPane.showMessageDialog(null, "Sorry, that was not a proper file");
		}
		int option0 = JOptionPane.showConfirmDialog(null, "Would you like to combine some images?");
		if (option0 == 0) {
			try {
				fd = new FileDialog(dialogDisplay, "Choose a file", FileDialog.LOAD);
				fd.setDirectory("C:\\");
				fd.setMultipleMode(true);
				fd.setVisible(true);
				File[] files = fd.getFiles();
				List<MarvinImage> images = loadImages(files);
				merger = new Merger(images, images.get(0).clone());
				merger.createArrays();
				handleMarvinOption();
			} catch (IndexOutOfBoundsException ex) {
				startMerger(true);
			}
		} else {
			System.exit(0);
		}
	}

	public void initMerger() {
		dialogDisplay = new JFrame();
		dialogDisplay.setVisible(true);
		dialogDisplay.pack();
		startMerger(false);
	}
	
	public MergePhotosApp() {
		initMerger();
	}

	public static void main(String[] args) {
		new MergePhotosApp();
	}
}