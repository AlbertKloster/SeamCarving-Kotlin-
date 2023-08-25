# Stage 3/6: Look at energy
## Description
Now you are able to manipulate pictures and ready to start.

The first step is to calculate the <b>energy</b> for each pixel of the image. Energy is the pixel's importance. The higher the pixel energy, the less likely this pixel is to be removed from the picture while reducing.

There are several different energy functions invented for seam carving. In this project, we will use <b>dual-gradient energy function</b>.

The energy of the pixel $(x,y)$ is $E(x,y)=\sqrt{Δ^2_x(x,y)+Δ^2_y(x,y)}$

Where the square of x-gradient $Δ^2_x(x,y)=R_x(x,y)^2+G_x(x,y)^2+B_x(x,y)^2$

y-gradient $Δ^2_y(x,y)=R_y(x,y)^2+G_y(x,y)^2+B_y(x,y)^2$

Don't be confused! You will calculate values $Δ^2_x(x,y)$ and $Δ^2_y(x,y)$ that are already squared, so you don't need to square them again to calculate $E(x,y)$.

Where $R_x(x,y)$, $G_x(x,y)$, $B_x(x,y)$ are differences of red, green and blue components between pixels $(x+1,y)$ and $(x−1,y)$.

Let's consider a grey pixel $(2,1)$ on the example image:

![dual gradient energy calculation](img1.png)

X-differences are:
$R_x(2,1)=255−150=105$, $G_x(2,1)=250−150=100$, $B_x(2,1)=155−100=55$

So, x-gradient is $Δ^2_x(2,1)=1052+1002+552=24050$

Y-differences are:
$R_y(2,1)=50−10=40$, $G_y(2,1)=255−250=5$, $B_y(2,1)=255−40=215$

Same for y-gradient $Δ^2_y=402+52+2152=47850$

Finally, $E(2,1)=\sqrt{24050+47850}=268.14$

Energy for border pixels is calculated with a 1-pixel shift from the border. For example:
$E(0,2)=\sqrt{Δ^2_x(1,2)+Δ^2_y(0,2)}$

$E(1,0)=\sqrt{Δ^2_x(1,0)+Δ^2_y(1,1)}$

$E(0,0)=\sqrt{Δ^2_x(1,0)+Δ^2_y(0,1)}$

## Objective
Calculate energies for all pixels of the image. Normalize calculated energies using the following formula:
`intensity = (255.0 * energy / maxEnergyValue).toInt()`

To display energy as a grey-scale image, set color components of the output image pixels to calculated intensity. For example, `(red = intensity, green = intensity, blue = intensity)`.

You should use `double` precision in this and in the following stages.

## Example
The greater-than symbol followed by a space (`> `) represents the user input. Note that it's not part of the input.
```
> java Main -in sky.png -out sky-energy.png

```
For the following `sky.png`:

![river side building construction](img2.png)

`sky-energy.png` should look like this:

[!river side building energy map](img3.png)