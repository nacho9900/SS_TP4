import matplotlib.pyplot as plt
import matplotlib
import sys

fig = plt.figure(figsize=(15,10))
ax1 = fig.add_subplot(111)
ax1.set_xlabel('Tiempo (10^-10 s)', fontsize=27)
ax1.set_ylabel('Distancia recorrida acumulada (10^-5 m)', fontsize=27)
ax1.tick_params(axis='both', which='major', labelsize=20, width=2.5, length=10)

vMs = [i for i in range(5, 55, 5)]
for vM in vMs:
    with open(f'ej2_3/vM_{vM}.csv') as f:
        lines = f.readlines()
        x = [float(line.split(' ')[0]) for line in lines]
        y = [float(line.split(' ')[1]) for line in lines]
        yerr = [float(line.split(' ')[2]) for line in lines]
    ax1.errorbar(x, y, yerr=yerr, label=f'{vM}x')

fig1=plt.gcf()
plt.legend(loc='best', fontsize=23)
plt.grid()
# plt.show()
plt.draw()
plt.savefig(f'ej2_3/plot.png')
