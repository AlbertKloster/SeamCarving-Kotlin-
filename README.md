# Stage 6/6: Resize
## Description
Now you have everything to resize the image while preserving its content. Simply remove the seam, then remove the seam from the resulting image and so on!

## Objective
Add two more command line parameters. Use parameter `-width` for the number of vertical seams to remove and `-height` for horizontal seams.

At this stage, your program should reduce the input image and save the result using the following algorithm:
- Find a vertical seam and remove all the pixels that this seam contains. Then find another vertical seam on the resulted image and delete all the pixels that the second seam contains. Repeat the process until you remove the specified number of vertical seams.
- Do the same, but with horizontal seams.

## Example
The greater-than symbol followed by a space (`> `) represents the user input. Note that it's not part of the input.

<b>Example 1:</b>

In the following example, the program should reduce the image width by 125 pixels and height by 50 pixels
```
> java Main -in sky.png -out sky-reduced.png -width 125 -height 50
```

For the following `sky.png`:

![river side building construction](img1.png)

`sky-reduced.png` should look like this:

![river side building construction resize](img2.png)

<b>Example 2:</b>
```
> java Main -in trees.png -out trees-reduced.png -width 100 -height 30
```
For `trees.png`:

![savanna trees](img3.png)

`trees-reduced.png` should look like this:

![savanna trees resize](img4.png)