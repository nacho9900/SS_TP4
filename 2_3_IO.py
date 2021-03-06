import matplotlib.pyplot as plt
from statistics import stdev


def avg(l):
    if len(l) == 0:
        return 0
    return sum(l)/len(l)


fig = plt.figure(figsize=(15,10))
ax1 = fig.add_subplot(111)
ax1.set_xlabel('Vx inicial (m/s)', fontsize=27)
ax1.set_ylabel(f'Trayectoria (m)', fontsize=27)
ax1.tick_params(axis='both', which='major', labelsize=20, width=2.5, length=10)

# vMs = [1, 20, 40, 60, 80, 100, 120, 140, 160, 180]
# vMs = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50]
vMs = [100, 150, 200, 250, 300, 350, 400, 450, 500, 550]
vMs_labels = ['1x10⁴', '1.5x10⁴', '2x10⁴', '2.5x10⁴', '3x10⁴', '3.5x10⁴', '4x10⁴', '4.5x10⁴', '5x10⁴', '5.5x10⁴']
coolwarm = [(i/len(vMs), 0 , 1-i/len(vMs), 1) for i in range(len(vMs))]
# print()
xs = []
ys = []
yerrs = []
for i, vM in enumerate(vMs):
    with open(f'ej2_3/acum_vM_{vM}.csv') as f:
        lines = f.readlines()
        x = [float(line.split(' ')[0]) for line in lines]
        y = [float(line.split(' ')[1]) for line in lines]
        yerr = [float(line.split(' ')[2]) for line in lines]
    xs.append(vMs_labels[i])
    ys.append(avg(y))
    yerrs.append(stdev(y))
    # ax1.errorbar(x, y, yerr=yerr, label=f'vx0={vMs_labels[i]}m/s', color=coolwarm[i])

ax1.errorbar(xs, ys, yerr=yerrs, fmt='o')
fig1=plt.gcf()
# plt.legend(loc='best', fontsize=18)
plt.grid()
ax1.ticklabel_format(style='sci', axis='y', scilimits=(0,0), useOffset=False)
# plt.yscale("log")
# plt.xscale("log")


plt.show()
# plt.savefig(f'ej2_3/plot.png')
