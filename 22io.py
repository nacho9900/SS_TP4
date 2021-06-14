import matplotlib.pyplot as plt
import numpy as np


with open('./tp4/io_energy_vs_dt.csv.csv') as f:
    lines = f.readlines()
    x1 = [float(line.split(',')[0]) for line in lines]
    y1 = [float(line.split(',')[1]) for line in lines]
    yerr1 = [float(line.split(',')[2]) for line in lines]

fig = plt.figure(figsize=(15, 10))
ax1 = fig.add_subplot(111)
ax1.set_xlabel('dt (s)', fontsize=20)
ax1.set_ylabel("dE medio (J)", fontsize=20)
ax1.errorbar(x1, y1, yerr=yerr1, fmt='o')

box = ax1.get_position()
ax1.set_position([box.x0, box.y0 + box.height * 0.1,
                 box.width, box.height * 0.9])

# get handles
handles, labels = ax1.get_legend_handles_labels()
# remove the errorbars
handles = [h[0] for h in handles]


fig1 = plt.gcf()

plt.xticks(size=20)
plt.yticks(size=20)

plt.yscale("log")
plt.xscale("log")

plt.grid()
plt.show()
plt.draw()
