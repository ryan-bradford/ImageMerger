# ImageMerger
For a physics project, this is an image merging application that takes the 
input of a selection of images and merges them either using the Marvin plugin set or using a manual method.

The manual methods consist of pixel combination. It takes the set of images, reads their pixel data to a set of the array. 
The merger then processes these array using a set of user-selected methods. 

The options are as follows: 

Threshold: It will ditch any pixel information if the color values are below a set amount.
Scale: Raises each color value to a power. This emphasizes strong colors and drops weak ones. 
Ceil: It will set any color values to 255 that is below a lower limit. This makes sure any strong color is seen.

It will then combine the data in the following methods:

combineImages: It first adds every value in each color array together, this simplifies it to a three arrays that processes data
for every pixel. While adding the colors to one array, it determines how many images were combined and stores this value 
to divisor. Then the application begins to make the image. First, it creates a blank image then adds the processed data to it.
If the user chose to scale the values, it takes the x root of each pixel data. It then divides each color number by "divisor."
This assures that no value will exceed 255. It then returns the image.
combineImagesThroughMarvin: Uses the Marvin library which analyzes a set of images for differences in pixel data between images
to combine them into one solid image.
