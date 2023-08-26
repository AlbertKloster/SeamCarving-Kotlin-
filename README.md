# Stage 4/6: Find the seam
## Description
Now you are ready to find the best seam to remove. Vertical seam is a sequence of adjacent pixels crossing the image top to bottom. The seam can have only one pixel in each row of the image. For example, subsequent pixels for pixel $(x,y)$ are $(x−1,y+1)$, $(x,y+1)$, and $(x+1,y+1)$.

The best seam to remove is the seam with the lowest sum of pixel energies from all possible seams. The problem of finding the best seam is very similar to finding the shortest path in a graph. Think of pixels as vertices. Connect them with imaginary edges. Edge weight should be equal to the energy of the pixel this edge is pointing to.

![energy graph shortest path](img1.png)

The easiest way to apply the shortest path finding algorithm to your energy graph is to add imaginary zero rows with the horizontal links on the top and on the bottom. Then you can search for the shortest path between top-left and bottom-right corners.

![energy graph shortest path between corners](img2.png)

Also, you can come up with a dynamic programming solution without adding rows.

## Objective
At this stage, your program should find the seam with the lowest energy and highlight it in red. Red is (255, 0, 0).

## Example
The greater-than symbol followed by a space (`> `) represents the user input. Note that it's not part of the input.
```
> java Main -in sky.png -out sky-seam.png
```
For the following `sky.png`:

![river side building construction](img3.png)

`sky-seam.png` should look like this:

![river side building construction vertical seam](img4.png)