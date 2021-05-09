import matplotlib.pyplot as plt
import numpy as np

# with open('./tp4/energy_10E-13.csv') as f:
#     lines = f.readlines()
#     x = [float(line.split(',')[0]) for line in lines]
#     y = [float(line.split(',')[1]) for line in lines]
#     yerr = [float(line.split(',')[2]) for line in lines]

with open('./tp4/energy_50E-14.csv') as f:
    lines = f.readlines()
    x1 = [float(line.split(',')[0]) for line in lines]
    y1 = [float(line.split(',')[1]) for line in lines]
    yerr1 = [float(line.split(',')[2]) for line in lines]

with open('./tp4/energy_10E-14.csv') as f:
    lines = f.readlines()
    x2 = [float(line.split(',')[0]) for line in lines]
    y2 = [float(line.split(',')[1]) for line in lines]
    yerr2 = [float(line.split(',')[2]) for line in lines]

with open('./tp4/energy_50E-15.csv') as f:
    lines = f.readlines()
    x3 = [float(line.split(',')[0]) for line in lines]
    y3 = [float(line.split(',')[1]) for line in lines]
    yerr3 = [float(line.split(',')[2]) for line in lines]

# with open('/home/fer/Documents/SS/TP3/SS_TP3/tp3/frecuencies-130') as f:
#     lines = f.readlines()
#     x4 = [float(line.split(',')[0]) for line in lines]
#     y4 = [float(line.split(',')[1]) for line in lines]
#     yerr4 = [float(line.split(',')[2]) for line in lines]

# with open('/home/fer/Downloads/analytics (5).csv') as f:
#     lines = f.readlines()
#     x5 = [float(line.split(' ')[0]) for line in lines]
#     y5 = [float(line.split(' ')[1]) for line in lines]
#     yerr5 = [float(line.split(' ')[2]) for line in lines]

# fig = plt.figure(figsize=(15,10))
# ax1 = fig.add_subplot(111)
# y_pos = np.arange(len(y))
# # ax1.set_title("Neighbors Pair", fontsize=20)
# ax1.set_xlabel('Tiempo entre colisiones (s)', fontsize=20)
# ax1.set_ylabel("Probabilidad", fontsize=20)
# # ax1.locator_params(axis="x")
# ax1.bar(y_pos,yerr,align='center', alpha=0.5)
# # plt.xticks(y_pos, objects)


fig = plt.figure(figsize=(15, 10))
ax1 = fig.add_subplot(111)
# ax1.set_title("Neighbors Pair", fontsize=20)
ax1.set_xlabel('Tiempo', fontsize=20)
ax1.set_ylabel("delta energia", fontsize=20)
# ax1.errorbar(x, y, yerr=yerr, fmt='-o', label="1e-13")
ax1.errorbar(x1,y1,yerr=yerr1,fmt='-o', label="5e-14")
ax1.errorbar(x2,y2,yerr=yerr2,fmt='-o', label="1e-14")
ax1.errorbar(x3,y3,yerr=yerr3,fmt='-o', label="5e-15")
# ax1.errorbar(x4,y4,yerr=yerr4,fmt='-o', label=130)
# ax1.errorbar(x5,y5,yerr=yerr5,fmt='-o', label=0.8)

box = ax1.get_position()
ax1.set_position([box.x0, box.y0 + box.height * 0.1,
                 box.width, box.height * 0.9])

# get handles
handles, labels = ax1.get_legend_handles_labels()
# remove the errorbars
handles = [h[0] for h in handles]
# use them in the legend
ax1.legend(handles, labels, loc='upper center',numpoints=1, bbox_to_anchor=(1.04,1))


fig1 = plt.gcf()

plt.xticks(size=20)
plt.yticks(size=20)

# every_nth = 20
# for n, label in enumerate(ax1.xaxis.get_ticklabels()):
#     if n % every_nth != 0:
#         label.set_visible(False)

plt.yscale("log")
plt.xscale("log")

plt.grid()
plt.show()
plt.draw()

# fig = plt.figure()
# ax = fig.add_axes([0,0,1,1])
# ax.bar(y,yerr)
# plt.show()
