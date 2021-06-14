import matplotlib.pyplot as plt
import numpy as np
from statistics import stdev


def avg(l):
    if len(l) == 0:
        return 0
    return sum(l)/len(l)
# with open('./tp4/energy_10E-13.csv') as f:
#     lines = f.readlines()
#     x = [float(line.split(',')[0]) for line in lines]
#     y = [float(line.split(',')[1]) for line in lines]
#     yerr = [float(line.split(',')[2]) for line in lines]
xs = []
ys = []
yerrs = []
with open('./tp4/energy_10E-15.csv') as f:
    lines = f.readlines()
    x1 = [float(line.split(',')[0]) for line in lines]
    y1 = [float(line.split(',')[1]) for line in lines]
    yerr1 = [float(line.split(',')[2]) for line in lines]
    xs.append('1x10⁻¹⁵')
    ys.append(avg(y1))
    yerrs.append(stdev(y1))

with open('./tp4/energy_10E-16.csv') as f:
    lines = f.readlines()
    x2 = [float(line.split(',')[0]) for line in lines]
    y2 = [float(line.split(',')[1]) for line in lines]
    yerr2 = [float(line.split(',')[2]) for line in lines]
    xs.append('1x10⁻¹⁶')
    ys.append(avg(y2))
    yerrs.append(stdev(y2))

with open('./tp4/energy_10E-17.csv') as f:
    lines = f.readlines()
    x3 = [float(line.split(',')[0]) for line in lines]
    y3 = [float(line.split(',')[1]) for line in lines]
    yerr3 = [float(line.split(',')[2]) for line in lines]
    xs.append('1x10⁻¹⁷')
    ys.append(avg(y3))
    yerrs.append(stdev(y3))

fig = plt.figure(figsize=(15, 10))
ax1 = fig.add_subplot(111)
# ax1.set_title("Neighbors Pair", fontsize=20)
ax1.set_xlabel('Delta tiempos (s)', fontsize=20)
ax1.set_ylabel("dE (J)", fontsize=20)
print(len(xs), len(ys), len(yerrs))
ax1.errorbar(xs, ys, yerr=yerrs, fmt='o', alpha=0.7)

fig1 = plt.gcf()

plt.xticks(size=20)
plt.yticks(size=20)

# every_nth = 20
# for n, label in enumerate(ax1.xaxis.get_ticklabels()):
#     if n % every_nth != 0:
#         label.set_visible(False)

# plt.yscale("log")
# plt.xscale("log")

plt.grid()
plt.show()
plt.draw()

# fig = plt.figure()
# ax = fig.add_axes([0,0,1,1])
# ax.bar(y,yerr)
# plt.show()
