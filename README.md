# Stage 3/6: Look at energy
## Description
Now you are able to manipulate pictures and ready to start.

The first step is to calculate the <b>energy</b> for each pixel of the image. Energy is the pixel's importance. The higher the pixel energy, the less likely this pixel is to be removed from the picture while reducing.

There are several different energy functions invented for seam carving. In this project, we will use <b>dual-gradient energy function</b>.

The energy of the pixel $(x,y)$ is $(E(x,y)=\sqrt{Δ^2_x(x,y)+Δ^2_y(x,y)})$

Where the square of x-gradient $(Δ2x(x,y)=Rx(x,y)2+Gx(x,y)2+Bx(x,y)2)$$

y-gradient Δ2y(x,y)=Ry(x,y)2+Gy(x,y)2+By(x,y)2

Don't be confused! You will calculate values Δ2x(x,y)
and Δ2y(x,y)
that are already squared, so you don't need to square them again to calculate E(x,y)
.

Where Rx(x,y)
, Gx(x,y)
, Bx(x,y)
are differences of red, green and blue components between pixels (x+1,y)
and (x−1,y)
.

Let's consider a grey pixel (2,1)
on the example image:

dual gradient energy calculation

X-differences are:
Rx(2,1)=255−150=105,  Gx(2,1)=250−150=100,  Bx(2,1)=155−100=55

So, x-gradient is Δ2x(2,1)=1052+1002+552=24050

Y-differences are:
Ry(2,1)=50−10=40,  Gy(2,1)=255−250=5,  By(2,1)=255−40=215

Same for y-gradient Δ2y=402+52+2152=47850

Finally, E(2,1)=24050+47850−−−−−−−−−−−−√=268.14

Energy for border pixels is calculated with a 1-pixel shift from the border. For example:
E(0,2)=Δ2x(1,2)+Δ2y(0,2)−−−−−−−−−−−−−−−√

E(1,0)=Δ2x(1,0)+Δ2y(1,1)−−−−−−−−−−−−−−−√

E(0,0)=Δ2x(1,0)+Δ2y(0,1)−−−−−−−−−−−−−−−√

Objective
Calculate energies for all pixels of the image. Normalize calculated energies using the following formula:
intensity = (255.0 * energy / maxEnergyValue).toInt()

To display energy as a grey-scale image, set color components of the output image pixels to calculated intensity. For example, (red = intensity, green = intensity, blue = intensity).

You should use double precision in this and in the following stages.

Example
The greater-than symbol followed by a space (> ) represents the user input. Note that it's not part of the input.

> java Main -in sky.png -out sky-energy.png
> For the following sky.png:

river side building construction

sky-energy.png should look like this:

river side building energy map